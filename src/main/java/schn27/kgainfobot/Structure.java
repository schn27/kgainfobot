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
public final class Structure {

	public Structure(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public Structure(String code, String name) {
		this(Integer.parseInt(code), name);
	}

	public final int code;
	public final String name;
}
