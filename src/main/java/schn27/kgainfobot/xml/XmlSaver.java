/* 
 * Copyright (C) 2016 Aleksandr Malikov <schn27@gmail.com>
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
package schn27.kgainfobot.xml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

/**
 *
 * @author amalikov
 */
public class XmlSaver {
	
	public static void save(String fileName, String root, DocGenerator docGenerator) {
		try (Writer file = new OutputStreamWriter(new FileOutputStream(fileName, false), "UTF-8")) {
			StreamResult streamResult = new StreamResult(file);
			TransformerFactory tf = TransformerFactory.newInstance();
			tf.setAttribute("indent-number", 4); 
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.METHOD, "xml");
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.transform(createDOMSource(root, docGenerator), streamResult);
        } catch (TransformerException | IOException | ParserConfigurationException ex) {
			Logger.getLogger(XmlSaver.class.getName()).log(Level.SEVERE, null, ex);
        }			
	}
	
	private static DOMSource createDOMSource(String root, DocGenerator docGenerator) throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		DOMImplementation impl = db.getDOMImplementation();
		Document doc = impl.createDocument(null, root, null);
		doc.setXmlStandalone(true);
		docGenerator.generate(doc);
		return new DOMSource(doc);
		
	}
}
