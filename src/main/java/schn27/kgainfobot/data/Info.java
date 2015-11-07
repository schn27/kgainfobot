/*
 * Copyright (C) 2015 amalikov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package schn27.kgainfobot.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Container of info of all structures, departments and themes
 * 
 * @author amalikov
 */
public final class Info {

	public Info() {
		structures = new HashMap<>();
	}
	
	public List<Structure> getStructures() {
		List<Structure> list = new ArrayList<>();
		
		for (StructureEntry s : structures.values())
			list.add(s.structure);
			
		return list;
	}

	public List<Department> getDepartments(int structureCode) {
		List<Department> list = new ArrayList<>();
		
		if (structures.containsKey(structureCode)) {
			for (DepartmentEntry d : structures.get(structureCode).departments.values())
				list.add(d.department);
		}
			
		return list;
	}
	
	public List<Theme> getThemes(int structureCode, int departmentCode) {
		if (structures.containsKey(structureCode)) {
			StructureEntry s = structures.get(structureCode);
			if (s.departments.containsKey(departmentCode)) 
				return s.departments.get(departmentCode).themes;
		}
		
		return new ArrayList<>();
	}
	
	public void addStructure(Structure structure) {
		structures.put(structure.code, new StructureEntry(structure));
	}
	
	public void addStructures(List<Structure>structures) {
		for (Structure s : structures)
			addStructure(s);
	}
	
	public void addDepartment(int code, Department department) {
		if (structures.containsKey(code))
			structures.get(code).departments.put(department.code, new DepartmentEntry(department));
	}

	public void addDepartments(int structureCode, List<Department> departments) {
		for (Department d : departments)
			addDepartment(structureCode, d);
	}
	
	public void addTheme(int structureCode, int departmentCode, Theme theme) {
		if (structures.containsKey(structureCode)) {
			StructureEntry se = structures.get(structureCode);
			if (se.departments.containsKey(departmentCode))
				se.departments.get(departmentCode).themes.add(theme);
		}
	}

	public void addThemes(int structureCode, int departmentCode, List<Theme> themes) {
		for (Theme t : themes)
			addTheme(structureCode, departmentCode, t);
	}	
	
	public void loadFromFile(String filename) {
		try (Reader file = new InputStreamReader(new FileInputStream(filename), "UTF-8")) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();		
			Document doc = db.parse(new InputSource(file));
			Element root = doc.getDocumentElement();
			
			loadStructures(root);
		} catch (ParserConfigurationException | SAXException | IOException ex) {
			Logger.getLogger(Info.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void saveToFile(String filename) {
		try (Writer file = new OutputStreamWriter(new FileOutputStream(filename, false), "UTF-8")) {
			StreamResult streamResult = new StreamResult(file);
			TransformerFactory tf = TransformerFactory.newInstance();
			tf.setAttribute("indent-number", 4); 
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.METHOD, "xml");
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.transform(createDOMSource(), streamResult);
        } catch (TransformerException | IOException | ParserConfigurationException ex) {
			Logger.getLogger(Info.class.getName()).log(Level.SEVERE, null, ex);
        }		
	}

	private void loadStructures(Element root) {
		NodeList nodes = root.getElementsByTagName("structure");
		int n = nodes.getLength();
		for (int i = 0; i < n; ++i) {
			Node node = nodes.item(i);
			NamedNodeMap attrs = node.getAttributes();
			Structure structure = new Structure(
					Integer.parseInt(attrs.getNamedItem("code").getNodeValue()), 
					attrs.getNamedItem("name").getNodeValue());
			addStructure(structure);
			loadDepartments(structure, (Element)node);
		}
	}

	private void loadDepartments(Structure structure, Element root) {
		NodeList nodes = root.getElementsByTagName("department");
		int n = nodes.getLength();
		for (int i = 0; i < n; ++i) {
			Node node = nodes.item(i);
			NamedNodeMap attrs = node.getAttributes();
			Department department = new Department(
					Integer.parseInt(attrs.getNamedItem("id").getNodeValue()), 
					Integer.parseInt(attrs.getNamedItem("code").getNodeValue()), 
					attrs.getNamedItem("name").getNodeValue(),
					attrs.getNamedItem("position").getNodeValue());
			addDepartment(structure.code, department);
			loadThemes(structure, department, (Element)node);
		}
	}	
	
	private void loadThemes(Structure structure, Department department, Element root) {
		NodeList nodes = root.getElementsByTagName("theme");
		int n = nodes.getLength();
		for (int i = 0; i < n; ++i) {
			Node node = nodes.item(i);
			NamedNodeMap attrs = node.getAttributes();
			Theme theme = new Theme(
					Integer.parseInt(attrs.getNamedItem("id").getNodeValue()), 
					attrs.getNamedItem("name").getNodeValue());
			addTheme(structure.code, department.code, theme);
		}		
	}	
	
	private DOMSource createDOMSource() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		DOMImplementation impl = db.getDOMImplementation();
		Document doc = impl.createDocument(null, "kgainfo", null);
		doc.setXmlStandalone(true);
		
		for (StructureEntry s : structures.values()) {
			final Element snode = doc.createElementNS(null, "structure");
			snode.setAttribute("code", Integer.toString(s.structure.code));
			snode.setAttribute("name", s.structure.name);
			
			for (DepartmentEntry d : s.departments.values()) {
				final Element dnode = doc.createElementNS(null, "department");
				dnode.setAttribute("code", Integer.toString(d.department.code));
				dnode.setAttribute("id", Integer.toString(d.department.id));
				dnode.setAttribute("name", d.department.name);
				dnode.setAttribute("position", d.department.position);
				
				for (Theme t : d.themes) {
					final Element tnode = doc.createElementNS(null, "theme");
					tnode.setAttribute("id", Integer.toString(t.id));
					tnode.setAttribute("name", t.name);
					dnode.appendChild(tnode);
				}
				
				snode.appendChild(dnode);
			}
			
			doc.getDocumentElement().appendChild(snode);
		}

		return new DOMSource(doc);
	}
	
	private final class StructureEntry {
		public StructureEntry(Structure structure) {
			this.structure = structure;
			departments = new HashMap<>();
		}
		
		public final Structure structure;
		public final Map<Integer, DepartmentEntry> departments;
	}
	
	private final class DepartmentEntry {
		public DepartmentEntry(Department department) {
			this.department = department;
			themes = new ArrayList<>();
		}
		
		public final Department department;
		public final List<Theme> themes;
	}
	
	private final Map<Integer, StructureEntry> structures;
}