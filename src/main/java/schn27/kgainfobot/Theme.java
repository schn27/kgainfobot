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
public final class Theme {
	public Theme(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Theme(String id, String name) {
		this(Integer.parseInt(id), name);
	}
	
	public final int id;
	public final String name;
}
