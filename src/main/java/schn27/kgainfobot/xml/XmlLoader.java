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
package schn27.kgainfobot.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import schn27.kgainfobot.data.Info;

/**
 *
 * @author amalikov
 */
public class XmlLoader {
	public static void load(String fileName, DocLoader docLoader) {
		try (Reader file = new InputStreamReader(new FileInputStream(fileName), "UTF-8")) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();		
			Document doc = db.parse(new InputSource(file));
			docLoader.load(doc);
		} catch (FileNotFoundException ex) {
		} catch (ParserConfigurationException | SAXException | IOException  ex) {
			Logger.getLogger(Info.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
}
