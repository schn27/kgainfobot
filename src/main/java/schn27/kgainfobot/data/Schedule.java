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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
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
 *
 * @author amalikov
 */
public class Schedule {
	
	public Schedule(String fileName) {
		this.fileName = fileName;
		weekDays = new boolean[]{false, true, false, true, false, false, false};
		startTime = new Time("9:55");
		endTime = new Time("10:10");
	}
	
	public boolean isScheduledTime() {
		return false;	// TODO
	}
	
	public Time getStartTime() {
		return startTime;
	}
	
	public Time getEndTime() {
		return endTime;
	}

	public boolean[] getWeekDays() {
		return Arrays.copyOf(weekDays, weekDays.length);
	}
	
	public void setAll(String startTime, String endTime, boolean[] weekDays) {
		this.startTime = new Time(startTime);
		this.endTime = new Time(endTime);
		for (int i = 0; i < weekDays.length; ++i)
			this.weekDays[i] = weekDays[i];
		saveToFile();
	}
	
	public void setStartTime(String value) {
		startTime = new Time(value);
		saveToFile();
	}

	public void setEndTime(String value) {
		endTime = new Time(value);
		saveToFile();
	}	
	
	public void setWeekDay(int i, boolean value) {
		weekDays[i] = value;
		saveToFile();
	}
	
	public void loadFromFile() {
		try (Reader file = new InputStreamReader(new FileInputStream(fileName), "UTF-8")) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();		
			Document doc = db.parse(new InputSource(file));
			Element root = doc.getDocumentElement();

			NodeList nodes = root.getChildNodes();
			int n = nodes.getLength();
			for (int i = 0; i < n; ++i) {
				Node node = nodes.item(i);
				NamedNodeMap attrs = node.getAttributes();
				
				if (attrs == null || attrs.getNamedItem("value") == null)
					continue;

				String value = attrs.getNamedItem("value").getNodeValue();

				switch (node.getNodeName()) {
					case "startTime":
						startTime = new Time(value);
						break;
					case "endTime":
						endTime = new Time(value);
						break;
					default:
						if (node.getNodeName().startsWith("weekDay-")) {
							int index = Integer.parseInt(node.getNodeName().substring(8)) - 1;
							if (index >= 0 && index < weekDays.length)
								weekDays[index] = Boolean.parseBoolean(value);
						}
				}
			}
		} catch (FileNotFoundException ex) {
		} catch (ParserConfigurationException | SAXException | IOException  ex) {
			Logger.getLogger(Info.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void saveToFile() {
		try (Writer file = new OutputStreamWriter(new FileOutputStream(fileName, false), "UTF-8")) {
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
	
	private DOMSource createDOMSource() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		DOMImplementation impl = db.getDOMImplementation();
		Document doc = impl.createDocument(null, "schedule", null);
		doc.setXmlStandalone(true);
		
		Element node = doc.createElementNS(null, "startTime");
		node.setAttribute("value", startTime.toString());
		doc.getDocumentElement().appendChild(node);
		
		node = doc.createElementNS(null, "endTime");
		node.setAttribute("value", endTime.toString());
		doc.getDocumentElement().appendChild(node);

		for (int i = 0; i < weekDays.length; ++i) {
			node = doc.createElementNS(null, String.format("weekDay-%d", i + 1));
			node.setAttribute("value", Boolean.toString(weekDays[i]));
			doc.getDocumentElement().appendChild(node);
		}
		
		return new DOMSource(doc);
	}

	private final String fileName;
	private Time startTime;
	private Time endTime;
	private final boolean[] weekDays;
}
