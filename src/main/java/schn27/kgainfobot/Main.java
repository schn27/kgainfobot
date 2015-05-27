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
package schn27.kgainfobot;

import schn27.kgainfobot.data.Time;
import schn27.kgainfobot.data.RegistrationRequest;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;

/**
 *
 * @author amalikov
 */
public class Main {

	public static void main(String[] args) throws IOException, UnirestException {
		setShutdownHook(true);

		Session session = new Session();
		if (session.login("H6Pu9bp", "NawVUVi")) {
			RegistrationRequest request = new RegistrationRequest();
			request.structureCode = 811000;
			request.departmentCode = 811032;
			request.themeId = 18;
			request.themeName = "Красные линии УДС";
			request.desiredTime = new Time("11:32");
			request.comment = "123";
			boolean res = session.register(request);
			System.out.println("register result = " + res);
		} else
			System.err.println("Login failed");

		Unirest.shutdown();
		setShutdownHook(false);
	}

	private static void setShutdownHook(boolean set) {
		if (hook == null)
			hook = new Thread() {
				@Override
				public void run() {
					System.out.println("Shutdown hook");

					try {
						Unirest.shutdown();
					} catch (IOException ex) {
					}
				}
			};

		if (set)
			Runtime.getRuntime().addShutdownHook(hook);
		else
			Runtime.getRuntime().removeShutdownHook(hook);
	}

	private static Thread hook = null;
}
