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

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import schn27.kgainfobot.xml.XmlLoader;
import schn27.kgainfobot.xml.XmlSaver;

/**
 *
 * @author amalikov
 */
public class RequestsManager {

	public RequestsManager(String fileName, AccountsManager accountsManager, Info info) {
		this.fileName = fileName;
		this.accountsManager = accountsManager;
		this.info = info;
		requests = new ArrayList<>();
	}

	public void setInfo(Info info) {
		this.info = info;
	}
	
	public int getNumberOfRequests() {
		return requests.size();
	}
	
	public Request get(int index) {
		return requests.get(index);
	}

	public void add(Request request) {
		requests.add(request);
		saveToFile();
	}
	
	public void set(int index, Request request) {
		requests.set(index, request);
		saveToFile();
	}	

	public void remove(int index) {
		requests.remove(index);
		saveToFile();
	}

	public void loadFromFile() {
		requests.clear();
		XmlLoader.load(fileName, (Document doc) -> {
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("request");
			int n = nodes.getLength();
			for (int i = 0; i < n; ++i) {
				Node node = nodes.item(i);
				NamedNodeMap attrs = node.getAttributes();
				
				Request request = new Request();
				request.account = accountsManager.get(attrs.getNamedItem("account").getNodeValue());
				request.structure = info.getStructure(Integer.parseInt(attrs.getNamedItem("structure").getNodeValue()));
				request.department = info.getDepartment(request.structure, Integer.parseInt(attrs.getNamedItem("department").getNodeValue()));
				request.theme = info.getTheme(request.structure, request.department, Integer.parseInt(attrs.getNamedItem("theme").getNodeValue()));
				request.comment = attrs.getNamedItem("comment").getNodeValue();
				request.desiredTime = new Time(attrs.getNamedItem("time").getNodeValue());
				request.status = new Status(attrs.getNamedItem("status").getNodeValue());
				requests.add(request);
			}
		});
	}
	
	public void saveToFile() {
		XmlSaver.save(fileName, "requests", (Document doc) -> {
			for (Request r : requests) {
				final Element node = doc.createElementNS(null, "request");
				node.setAttribute("account", r.account.name);
				node.setAttribute("structure", String.format("%d", r.structure.code));
				node.setAttribute("department", String.format("%d", r.department.code));
				node.setAttribute("theme", String.format("%d", r.theme.id));
				node.setAttribute("comment", r.comment);
				node.setAttribute("time", r.desiredTime.toString());
				node.setAttribute("status", r.status.toString());
				doc.getDocumentElement().appendChild(node);
			}
		});
	}
	
	
	private final String fileName;
	private final AccountsManager accountsManager;
	private Info info;
	private final List<Request> requests;
}
