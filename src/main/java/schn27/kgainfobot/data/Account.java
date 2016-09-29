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
package schn27.kgainfobot.data;

/**
 *
 * @author AVIA
 */
public class Account {
	
	public Account(String name, String login, String password) {
		this.name = name;
		this.login = login;
		this.password = password;
	}

	@Override
	public String toString() {
		return name;
	}

	public final String name;
	public final String login;
	public final String password;
}
