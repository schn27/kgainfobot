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
public class RegistrationRequest {
    
    public RegistrationRequest() {
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
	public String themeName;
	public Time desiredTime;
	public String comment;
    public int timeout;
}
