/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.kgainfobot;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author amalikov
 */
public class Main {

	public static void main(String[] args) throws IOException, UnirestException {
        setShutdownHook(true);
        
        CommandLineParser cmdParser = new CommandLineParser(args);

		Session session = new Session();
		if (session.login(cmdParser.getLogin(), cmdParser.getPass())) {      // "H6Pu9bp", "NawVUVi"
			switch (cmdParser.getCommand()) {
                case CommandLineParser.REGISTER:
                    doRegister(session, cmdParser.getRegisterRequest());
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
                default:
                    System.err.println("Unknown command");
            }
		} else
			System.err.println("Login failed");

		Unirest.shutdown();
		setShutdownHook(false);
	}
   
    private static void doRegister(Session session, RegisterRequest request) {
/*        request.structureCode = 811000;
        request.departmentCode = 811032;
        request.themeId = 1;
        request.desiredTime = new Time("11:32");
        request.comment = "123";
        request.timeout = 60;*/
        boolean res = session.register(request);
        System.out.println("register result = " + res);
    }
    
    private static void doGetStructureList(Session session) {
        List<Structure> list = session.getStructureList();
        for (Structure s : list) {
            System.out.println(s.code + " " + s.name);
        }
    }

    private static void doGetDepartmentList(Session session, int code) throws UnirestException {
        List<Department> list = session.getDepartmentList(Integer.toString(code));
        for (Department d : list) {
            System.out.println(d.code + " " + d.id + " " + d.name + " " + d.position);
        }
    }

    private static void doGetThemeList(Session session, int code) throws UnirestException {
        List<Theme> list = session.getThemeList(Integer.toString(code));
        for (Theme t : list) {
            System.out.println(t.id + " " + t.name);
        }
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
