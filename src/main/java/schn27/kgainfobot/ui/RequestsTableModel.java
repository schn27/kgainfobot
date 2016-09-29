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
package schn27.kgainfobot.ui;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import schn27.kgainfobot.data.Request;
import schn27.kgainfobot.data.RequestsManager;

/**
 *
 * @author amalikov
 */
public class RequestsTableModel extends AbstractTableModel {

	private static final String[] Headers = new String[]{
		"Account", "Structure", "Department", "Theme", "Comment", "Time", "Status"};
	
	public RequestsTableModel(RequestsManager requestsManager) {
		this.requestsManager = requestsManager;
	}
	
	@Override
	public int getRowCount() {
		return requestsManager.getNumberOfRequests();
	}

	@Override
	public int getColumnCount() {
		return Headers.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Request request = requestsManager.get(row);
		switch (col) {
		case 0:
			return request.account.name;
		case 1:
			return request.structure.name;
		case 2:
			return request.department.name;
		case 3:
			return request.theme.name;
		case 4:
			return request.comment;
		case 5:
			return request.desiredTime.toString();
		case 6:
			return request.status.toString();
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
		return false;
	}

	public void addRow(Request request) {
		requestsManager.add(request);
		fireTableChanged(new TableModelEvent(this));
	}

	public void removeRow(int row) {
		requestsManager.remove(row);
		fireTableChanged(new TableModelEvent(this));
	}
	
	public Request getAccount(int row) {
		return requestsManager.get(row);
	}
	
	private final RequestsManager requestsManager;
}
