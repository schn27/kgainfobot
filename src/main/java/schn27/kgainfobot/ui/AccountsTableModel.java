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
package schn27.kgainfobot.ui;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import schn27.kgainfobot.data.Account;
import schn27.kgainfobot.data.AccountsManager;

/**
 *
 * @author amalikov
 */
public class AccountsTableModel extends AbstractTableModel {
	
	private static final String[] Headers = new String[]{"Name", "Login", "Password"};
	
	public AccountsTableModel(AccountsManager accountsManager) {
		this.accountsManager = accountsManager;
	}
	
	@Override
	public int getRowCount() {
		return accountsManager.getNumberOfAccounts();
	}

	@Override
	public int getColumnCount() {
		return Headers.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Account account = accountsManager.get(row);
		switch (col) {
			case 0:
				return account.name;
			case 1:
				return account.login;
			case 2:
				return account.password;
			default:
				return "";
		}
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		return String.class;
	}
	
	@Override
	public String getColumnName(int col) {
		try {
			return Headers[col];
		} catch (IndexOutOfBoundsException ex) {
			return "";
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	@Override
	public void setValueAt(Object val, int row, int col) {
		Account account = accountsManager.get(row);
		switch (col) {
			case 0:
				accountsManager.set(row, new Account((String)val, account.login, account.password));
				break;
			case 1:
				accountsManager.set(row, new Account(account.name, (String)val, account.password));
				break;
			case 2:
				accountsManager.set(row, new Account(account.name, account.login, (String)val));
				break;
		}
		fireTableChanged(new TableModelEvent(this));
	}
	
	public void addRow(Account account) {
		accountsManager.add(account);
		fireTableChanged(new TableModelEvent(this));
	}

	public void addRow() {
		addRow(new Account("noname", "", ""));
	}	
	
	public void removeRow(int row) {
		accountsManager.remove(row);
		fireTableChanged(new TableModelEvent(this));
	}
	
	public Account getAccount(int row) {
		return accountsManager.get(row);
	}
	
	private final AccountsManager accountsManager;
}
