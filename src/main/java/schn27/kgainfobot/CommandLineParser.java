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

import java.util.HashMap;
import java.util.Map;
import schn27.kgainfobot.data.Department;
import schn27.kgainfobot.data.Request;
import schn27.kgainfobot.data.Structure;
import schn27.kgainfobot.data.Theme;
import schn27.kgainfobot.data.Time;

/**
 * Command line parser. 
 * See usage for the full description.
 * 
 * @author amalikov
 */
public class CommandLineParser {
    public static final int INVALID_COMMAND = -1;
    public static final int REGISTER = 0;
    public static final int GET_STRUCTURE_LIST = 1;
    public static final int GET_DEPARTMENT_LIST = 2;
    public static final int GET_THEME_LIST = 3;
	public static final int CREATE_INFO_FILE = 4;
    
    public CommandLineParser(String[] args) {
        this.args = parse(args);
    }
    
    public int getCommand() {
        switch (args.getOrDefault("cmd", "register")) {
            case "register":
                return REGISTER;
            case "getstructures":
                return GET_STRUCTURE_LIST;
            case "getdepartments":
                return GET_DEPARTMENT_LIST;
            case "getthemes":
                return GET_THEME_LIST;
			case "infofile":
				return CREATE_INFO_FILE;
            default:
                return INVALID_COMMAND;
        }
    }
    
    public String getLogin() {
        return args.getOrDefault("login", "");
    }

    public String getPass() {
        return args.getOrDefault("pass", "");
    }
    
    public Request getRegisterRequest() {
        Request request = new Request();
        request.structure = new Structure(args.getOrDefault("structure", "0"), "name-here");
        request.department = new Department("0", args.getOrDefault("department", "0"), "name-here", "position-here");
        request.theme = new Theme(args.getOrDefault("theme", "0"), "theme-here");
        request.desiredTime = new Time(args.getOrDefault("time", "10:00"));
        request.comment = args.getOrDefault("comment", "-");
		request.timeout = Integer.parseInt(args.getOrDefault("timeout", "10"));
        return request;
    }
    
    public int getCode() {
        return Integer.parseInt(args.getOrDefault("code", "0"));
    }
	
	public String getFileName() {
		return args.getOrDefault("file", "kgainfo.xml");
	}

    private Map<String, String> parse(String[] args) {
        Map<String, String> res = new HashMap<>();
        for (String arg : args) {
            String[] split = arg.split("=");
            if (split.length == 2)
                res.put(split[0].toLowerCase(), split[1]);
        }
		
		if (res.isEmpty())
			printUsage();
		
        return res;
    }
	
	private void printUsage() {
		System.out.println("Usage: java -jar kgainfobot.jar login=<login> pass=<password> cmd=<command> [parameters]");
		System.out.println("Required parameter list for command:");
		System.out.println("  register: structure=<structure_code> department=<department_code> theme=<theme_id> time=<desired_time> comment=<any_text>");
		System.out.println("  getstructures: no parameter is required");
		System.out.println("  getdepartments: code=<structure_code>");
		System.out.println("  getthemes: code=<department_code>");
		System.out.println("  infofile: file=<file_name>");
		System.out.println("Example: java -jar kgainfobot-1.0-exe.jar login=H6Pu9bp pass=NawVUVi cmd=register structure=812000 department=812003 theme=11 time=11:30 comment=\"...\" timeout=60");
	}

    private final Map<String, String> args;
}
