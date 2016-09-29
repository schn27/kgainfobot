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
package schn27.kgainfobot.networking;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.List;
import schn27.kgainfobot.data.Account;
import schn27.kgainfobot.data.Department;
import schn27.kgainfobot.data.Info;
import schn27.kgainfobot.data.Structure;
import schn27.kgainfobot.data.Theme;

/**
 *
 * @author amalikov
 */
public class InfoReader {
	public static void read(Account account, Info info) throws UnirestException, LoginFailedException {
		Session session = new Session();
		
		if (!session.login(account.login, account.password)) {
			throw new LoginFailedException();
		}

		List<Structure> structures = session.getStructureList();
		info.addStructures(structures);

		for (Structure s : structures) {
			List<Department> departments = session.getDepartmentList(Integer.toString(s.code));
			info.addDepartments(s.code, departments);

			for (Department d : departments) {
				List<Theme> themes = session.getThemeList(Integer.toString(d.code));
				info.addThemes(s.code, d.code, themes);
			}
		}		
	}
}
