/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.kgainfobot;

/**
 *
 * @author amalikov
 */
public class RegisterRequest {
    public RegisterRequest() {
        structureCode = 0;
        departmentCode = 0;
        themeId = 0;
        desiredTime = new Time("10:00");
        comment = "";
        timeout = 60;
    }
    
	public int structureCode;
	public int departmentCode;
	public int themeId;
	public Time desiredTime;
	public String comment;
    public int timeout;
}
