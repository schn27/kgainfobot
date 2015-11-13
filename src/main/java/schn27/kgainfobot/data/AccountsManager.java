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
		XmlLoader.load(fileName, (Document doc) -> {
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
		});
	}
	
	public void saveToFile() {
		XmlSaver.save(fileName, "accounts", (Document doc) -> {
			for (Account a : accounts) {
				final Element node = doc.createElementNS(null, "account");
				node.setAttribute("name", a.name);
				node.setAttribute("login", a.login);
				node.setAttribute("password", a.password);
				doc.getDocumentElement().appendChild(node);
			}
		});
	}

	private final String fileName;
	private final List<Account> accounts;
}
