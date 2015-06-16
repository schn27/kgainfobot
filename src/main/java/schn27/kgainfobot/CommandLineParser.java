/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.kgainfobot;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AVIA
 */
public class CommandLineParser {
    public static final int INVALID_COMMAND = -1;
    public static final int REGISTER = 0;
    public static final int GET_STRUCTURE_LIST = 1;
    public static final int GET_DEPARTMENT_LIST = 2;
    public static final int GET_THEME_LIST = 3;
    
    public CommandLineParser(String[] args) {
        this.args = parse(args);
    }
    
    public int getCommand() {
        String command = args.getOrDefault("cmd", "register");
        switch (command) {
            case "register":
                return REGISTER;
            case "getstructures":
                return GET_STRUCTURE_LIST;
            case "getdepartments":
                return GET_DEPARTMENT_LIST;
            case "getthemes":
                return GET_THEME_LIST;
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
    
    public RegisterRequest getRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.structureCode = Integer.parseInt(args.getOrDefault("structure", "0"));
        request.departmentCode = Integer.parseInt(args.getOrDefault("department", "0"));
        request.themeId = Integer.parseInt(args.getOrDefault("theme", "0"));
        request.desiredTime = new Time(args.getOrDefault("time", "10:00"));
        request.comment = args.getOrDefault("comment", "-");
        return request;
    }
    
    public int getCode() {
        return Integer.parseInt(args.getOrDefault("code", "0"));
    }

    private Map<String, String> parse(String[] args) {
        Map<String, String> res = new HashMap<>();
        for (String arg : args) {
            String[] split = arg.split("=");
            if (split.length == 2)
                res.put(split[0].toLowerCase(), split[1]);
        }
        return res;
    }

    private Map<String, String> args;
}
