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
 * Container of request info.
 * 
 * @author amalikov
 */
public class RegistrationRequest {
    
    public RegistrationRequest() {
        desiredTime = new Time("10:00");
        comment = "...";
        timeout = 60;
    }

	public int structureCode;	///< structure code (f.e. 812000)
	public int departmentCode;	///< department code (f.e. 812003)
	public int themeId;			///< theme id (f.e. 11)
	public String themeName;	///< human readable theme name in Russian (probably obsolete) (f.e. "Согласование проектов в ОПС")
	public Time desiredTime;	///< desired time of registration (f.e. Time("10:00"))
	public String comment;		///< human readable comment (f.e. "...")
    public int timeout;			///< timeout of request processing in seconds (f.e. 60)
}
