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
public final class Time {
	public static final int INVALID_VALUE = -1;
	
	public Time(int value) {
		if (value >= 0 && value < 24 * 60)
			this.value = value;
		else
			this.value = INVALID_VALUE;
	}
	
	public Time(String time) {
		this(fromString(time));
	}
	
	@Override
	public String toString() {
		return String.format("%02d:%02d", value / 60, value % 60);
	}
	
	public final int value;
	
	private static int fromString(String time) {
		String[] parts = time.split(":");
		return (parts.length == 2) ? Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]) : 0;
	}
}
