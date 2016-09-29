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
package schn27.kgainfobot;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import schn27.kgainfobot.ui.MainFrame;

/**
 *
 * @author amalikov
 */
public class Main {

	public static void main(String[] args) throws IOException, UnirestException {
		setupLogger();
		MainFrame.createAndShow();
		setShutdownHook(true);
	}

	private static void setupLogger() {
		try {
			LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
		} catch (IOException | SecurityException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private static void setShutdownHook(boolean set) {
		if (hook == null) {
			hook = new Thread(() -> {
				try {
					Unirest.shutdown();
				} catch (IOException ex) {
				}
			});
		}

		if (set) {
			Runtime.getRuntime().addShutdownHook(hook);
		} else {
			Runtime.getRuntime().removeShutdownHook(hook);
		}
	}

	private static Thread hook;
}
