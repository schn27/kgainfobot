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

import java.util.Arrays;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import schn27.kgainfobot.xml.XmlLoader;
import schn27.kgainfobot.xml.XmlSaver;

/**
 *
 * @author amalikov
 */
public class Schedule {
	
	public Schedule(String fileName) {
		this.fileName = fileName;
		weekDays = new boolean[]{false, true, false, true, false, false, false};
		startTime = new Time("9:55");
		endTime = new Time("10:10");
	}
	
	public boolean isScheduledTime() {
		return false;	// TODO
	}
	
	public Time getStartTime() {
		return startTime;
	}
	
	public Time getEndTime() {
		return endTime;
	}

	public boolean[] getWeekDays() {
		return Arrays.copyOf(weekDays, weekDays.length);
	}
	
	public void setAll(String startTime, String endTime, boolean[] weekDays) {
		Time oldStartTime = this.startTime;
		Time oldEndTime = this.endTime;
		boolean[] oldWeekDays = Arrays.copyOf(this.weekDays, this.weekDays.length);
		
		this.startTime = new Time(startTime);
		this.endTime = new Time(endTime);
		
		for (int i = 0; i < weekDays.length; ++i)
			this.weekDays[i] = weekDays[i];
		
		compareAndSave(oldStartTime, oldEndTime, oldWeekDays);
	}
	
	public void setStartTime(String value) {
		Time oldStartTime = startTime;
		startTime = new Time(value);
		compareAndSave(oldStartTime, endTime, weekDays);
	}

	public void setEndTime(String value) {
		Time oldEndTime = endTime;
		endTime = new Time(value);
		compareAndSave(startTime, oldEndTime, weekDays);
	}	
	
	public void setWeekDay(int i, boolean value) {
		boolean[] oldWeekDays = Arrays.copyOf(weekDays, weekDays.length);
		weekDays[i] = value;
		compareAndSave(startTime, endTime, oldWeekDays);
	}
	
	public void loadFromFile() {
		XmlLoader.load(fileName, (Document doc) -> {
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getChildNodes();
			int n = nodes.getLength();
			for (int i = 0; i < n; ++i) {
				Node node = nodes.item(i);
				NamedNodeMap attrs = node.getAttributes();
				
				if (attrs == null || attrs.getNamedItem("value") == null)
					continue;

				String value = attrs.getNamedItem("value").getNodeValue();

				switch (node.getNodeName()) {
					case "startTime":
						startTime = new Time(value);
						break;
					case "endTime":
						endTime = new Time(value);
						break;
					default:
						if (node.getNodeName().startsWith("weekDay-")) {
							int index = Integer.parseInt(node.getNodeName().substring(8)) - 1;
							if (index >= 0 && index < weekDays.length)
								weekDays[index] = Boolean.parseBoolean(value);
						}
				}
			}
		});
	}
	
	public void saveToFile() {
		XmlSaver.save(fileName, "schedule", (Document doc) -> {
			Element node = doc.createElementNS(null, "startTime");
			node.setAttribute("value", startTime.toString());
			doc.getDocumentElement().appendChild(node);
		
			node = doc.createElementNS(null, "endTime");
			node.setAttribute("value", endTime.toString());
			doc.getDocumentElement().appendChild(node);

			for (int i = 0; i < weekDays.length; ++i) {
				node = doc.createElementNS(null, String.format("weekDay-%d", i + 1));
				node.setAttribute("value", Boolean.toString(weekDays[i]));
				doc.getDocumentElement().appendChild(node);
			}
		});
	}

	private void compareAndSave(Time oldStartTime, Time oldEndTime, boolean[] oldWeekDays) {
		boolean changed = !startTime.equals(oldStartTime);
		changed |= !endTime.equals(oldEndTime);
		
		for (int i = 0; i < weekDays.length; ++i)
			changed |= weekDays[i] != oldWeekDays[i];
		
		if (changed)
			saveToFile();
	}
	
	private final String fileName;
	private Time startTime;
	private Time endTime;
	private final boolean[] weekDays;
}
