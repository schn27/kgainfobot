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
package schn27.kgainfobot.data;

/**
 * Immutable container for time info.
 * Can contain invalid value for special cases.
 * 
 * @author amalikov
 */
public final class Time {

	public Time(int value) {
		this.value = (value >= 0 && value < 24 * 60) ? value : INVALID_VALUE;
	}

	public Time(String time) {
		this(fromString(time));
	}
	
	public static Time getInvalid() {
		return invalid;
	}

	public boolean isValid() {
		return value != INVALID_VALUE;
	}
	
	@Override
	public String toString() {
		return String.format("%02d:%02d", value / 60, value % 60);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		
		if (other == this) {
			return true;
		}
		
		if (!(other instanceof Time)) {
			return false;
		}
    
		Time otherTime = (Time)other;
    	return otherTime.value == value;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + this.value;
		return hash;
	}

	public final int value;		///< time in minutes of the day

	private Time() {
		value = INVALID_VALUE;
	}
	
	private static int fromString(String time) {
		String[] parts = time.split(":");
		return (parts.length == 2) ? Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]) : 0;
	}
	
	private static final int INVALID_VALUE = -1;
	private static final Time invalid = new Time();
}
