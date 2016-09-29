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
package schn27.kgainfobot.data;

/**
 *
 * @author amalikov
 */
public final class Status {
	
	private static final String[] names = new String[]{"Waiting", "Started", "Done", "TimedOut"};	
	
	public static final int Waiting = 0;
	public static final int Started = 1;
	public static final int Done = 2;
	public static final int TimedOut = 3;
	
	public Status() {
		set(Waiting);
	}
	
	public Status(String strStatus) {
		set(Waiting);
		for (int i = Waiting; i <= TimedOut; ++i) {
			if (names[i].equalsIgnoreCase(strStatus)) {
				set(i);
			}
		}
			
	}
	
	@Override
	public String toString() {
		return names[status];
	}
	
	public void set(int newStatus) {
		if (newStatus >= 0 && newStatus < names.length) {
			status = newStatus;
		}
	}
	
	public int get() {
		return status;
	}
	
	private int status;
}
