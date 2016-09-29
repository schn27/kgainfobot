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

/**
 * Immutable container of department info.
 * Department is referenced by its numerical code (the field 'code').
 * Fields 'name' and 'position' are used only for UI.
 * The field 'id' is useless.
 * 
 * @author amalikov
 */
public final class Department {

	public Department(int id, int code, String name, String position) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.position = position;
	}

	public Department(String id, String code, String name, String position) {
		this(Integer.parseInt(id), Integer.parseInt(code), name, position);
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s)", name, position);
	}

	public final int id;			///< useless id of the department
	public final int code;			///< code of the department
	public final String name;		///< human readable name in Russian of the department
	public final String position;	///< human readable additional description in Russian of the department
}
