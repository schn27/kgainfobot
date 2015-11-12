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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
public class AccountsManager {
	
	public AccountsManager(String fileName) {
		this.fileName = fileName;
		accounts = new ArrayList<>();
	}
	
	public int getNumberOfAccounts() {
		return accounts.size();
	}
	
	public Account get(int index) {
		return accounts.get(index);
	}
	
	public void set(int index, Account account) {
		accounts.set(index, account);
		saveToFile();
	}

	public void add(Account account) {
		accounts.add(account);
		saveToFile();
	}
	
	public void remove(int index) {
		accounts.remove(index);
		saveToFile();
	}
	
	public void loadFromFile() {
		accounts.clear();
		try (Reader file = new InputStreamReader(new FileInputStream(fileName), "UTF-8")) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();		
			Document doc = db.parse(new InputSource(file));
			Element root = doc.getDocumentElement();

			NodeList nodes = root.getElementsByTagName("account");
			int n = nodes.getLength();
			for (int i = 0; i < n; ++i) {
				Node node = nodes.item(i);
				NamedNodeMap attrs = node.getAttributes();
				accounts.add(new Account(attrs.getNamedItem("name").getNodeValue(),
					attrs.getNamedItem("login").getNodeValue(),
					attrs.getNamedItem("password").getNodeValue()));
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
		Document doc = impl.createDocument(null, "accounts", null);
		doc.setXmlStandalone(true);
		
		for (Account a : accounts) {
			final Element node = doc.createElementNS(null, "account");
			node.setAttribute("name", a.name);
			node.setAttribute("login", a.login);
			node.setAttribute("password", a.password);
			doc.getDocumentElement().appendChild(node);
		}

		return new DOMSource(doc);
	}

	private final String fileName;
	private final List<Account> accounts;
}
