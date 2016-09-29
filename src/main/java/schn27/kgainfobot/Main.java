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

import schn27.kgainfobot.networking.Session;
import schn27.kgainfobot.data.Request;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.List;
import schn27.kgainfobot.data.Department;
import schn27.kgainfobot.data.Info;
import schn27.kgainfobot.data.Structure;
import schn27.kgainfobot.data.Theme;
import schn27.kgainfobot.ui.MainFrame;

/**
 *
 * @author amalikov
 */
public class Main {

	public static void main(String[] args) throws IOException, UnirestException {
		MainFrame.createAndShow();
		setShutdownHook(true);
        
/*      int exitCode = 0;  
		CommandLineParser cmdParser = new CommandLineParser(args);

		Session session = new Session();
		if (session.login(cmdParser.getLogin(), cmdParser.getPass())) {      // "H6Pu9bp", "NawVUVi"
			switch (cmdParser.getCommand()) {
			case CommandLineParser.REGISTER:
				exitCode = doRegister(session, cmdParser.getRegisterRequest()) ? 0 : -1;
				break;
			case CommandLineParser.GET_STRUCTURE_LIST:
				doGetStructureList(session);
				break;
			case CommandLineParser.GET_DEPARTMENT_LIST:
				doGetDepartmentList(session, cmdParser.getCode());
				break;
			case CommandLineParser.GET_THEME_LIST:
				doGetThemeList(session, cmdParser.getCode());
				break;
			case CommandLineParser.CREATE_INFO_FILE:
				doCreateInfoFile(session, cmdParser.getFileName());
				break;
			default:
				System.err.println("Unknown command");
				exitCode = -1;
            }
		} else {
			System.err.println("Login failed");
			exitCode = -1;
		}

		Unirest.shutdown();
		setShutdownHook(false);
		System.exit(exitCode);*/
	}
   
    private static boolean doRegister(Session session, Request request) {
        boolean res = session.register(request);
        System.out.println("register result = " + res);
		return res;
    }
    
    private static void doGetStructureList(Session session) {
        List<Structure> list = session.getStructureList();
		list.forEach((s) -> System.out.println(s.code + " " + s.name));
    }

    private static void doGetDepartmentList(Session session, int code) throws UnirestException {
        List<Department> list = session.getDepartmentList(Integer.toString(code));
		list.forEach((d) -> System.out.println(d.code + " " + d.id + " " + d.name + " " + d.position));
    }

    private static void doGetThemeList(Session session, int code) throws UnirestException {
        List<Theme> list = session.getThemeList(Integer.toString(code));
		list.forEach((t) -> System.out.println(t.id + " " + t.name));
    }

	private static void doCreateInfoFile(Session session, String fileName) throws UnirestException {
		Info info = new Info(fileName);
		
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
		
		info.saveToFile();
	}	
	
	private static void setShutdownHook(boolean set) {
		if (hook == null) {
			hook = new Thread(() -> {
				System.out.println("Shutdown hook");
				
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
