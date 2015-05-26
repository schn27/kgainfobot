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
public final class Department {
	public Department(int id, int code, String name, String position) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.position = position;
	}

	public Department(String id, String code, String name, String position) {
		this(Integer.parseInt(id), Integer.parseInt(code), name, position);
	}
	
	public final int id;
	public final int code;
	public final String name;
	public final String position;
}
