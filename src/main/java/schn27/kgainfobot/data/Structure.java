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
 * Immutable container of structure info.
 * Each structure is referenced by its numeric code (the field 'code').
 * The field 'name' is used only for UI.
 * 
 * @author amalikov
 */
public final class Structure {

	public Structure(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public Structure(String code, String name) {
		this(Integer.parseInt(code), name);
	}
	
	@Override
	public String toString() {
		return name;
	}

	public final int code;		///< code of the structure (f.e. 812000)
	public final String name;	///< human readable name of the structure in Russian (f.e. "Отдел подземных сооружений КГА")
}
