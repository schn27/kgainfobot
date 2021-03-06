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
package schn27.kgainfobot.networking;

import schn27.kgainfobot.data.Time;
import schn27.kgainfobot.data.Theme;
import schn27.kgainfobot.data.Structure;
import schn27.kgainfobot.data.Request;
import schn27.kgainfobot.data.Department;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import schn27.kgainfobot.CaptchaMd5Decoder;

/**
 *
 * @author amalikov
 */
public class Session {

	private final static String host = "http://priem.kgainfo.spb.ru";
	private final static String loginURL = host + "/login";
	private final static String registrationURL = host + "/user/requests";
	private final static String getDepartmentURL = host + "/service/getdep";
	private final static String getThemeURL = host + "/service/getthemes";
	private final static String addCommentURL = host + "/user/addcomments";
	private final static String getDateURL = host + "/service/getdate";
	private final static String getTimeURL = host + "/service/getfreetime";	
	
	public Session() {
		uncaptcha = new CaptchaMd5Decoder();
	}

	public boolean login(String username, String password) {
		boolean result = false;

		try {
			Unirest.get(host).asString();
			
			HttpResponse<String> response = Unirest.post(loginURL)
					.header("Accept-encoding", "identity")
					.field("username", username)
					.field("password", password)
					.field("login", "Войти")
					.asString();

			
			
			if (response.getStatus() == 302) {
				Unirest.get(host).asString();
				result = true;
			} else {
				Logger.getLogger(Session.class.getName()).log(Level.SEVERE, String.format("Failed to login with status = %d", response.getStatus()));
			}

		} catch (UnirestException ex) {
			Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
		}

		return result;
	}

	public List<Structure> getStructureList() {
		List<Structure> list = new ArrayList<>();

		try {
			HttpResponse<String> response = Unirest.get(registrationURL).asString();
			Document doc = Jsoup.parse(response.getBody());

			Elements tags = doc.getElementsByTag("select");
			for (Element tag : tags) {
				if (tag.attr("name").equals("structure_code")) {
					Elements options = tag.children();
					for (Element option : options) {
						if (!option.attr("label").isEmpty()) {
							Structure s = new Structure(option.attr("value"), option.attr("label"));
							list.add(s);
							Logger.getLogger(Session.class.getName()).log(Level.INFO, String.format("%d %s", s.code, s.name));
						}
					}
				}
			}
			
			if (list.isEmpty()) {
				Logger.getLogger(Session.class.getName()).log(Level.SEVERE, 
						String.format("Can't find structures in response: \n%s", response.getBody()));
			}
		} catch (UnirestException ex) {
			Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
		}

		return list;
	}

	public List<Department> getDepartmentList(String code) throws UnirestException {
		List<Department> list = new ArrayList<>();

		HttpResponse<JsonNode> response = Unirest.post(getDepartmentURL)
				.field("code", code)
				.asJson();
		
		Logger.getLogger(Session.class.getName()).log(Level.INFO, response.getBody().toString());

		JSONArray array = response.getBody().getArray();
		for (int i = 0; i < array.length(); ++i) {
			JSONObject o = array.getJSONObject(i);
			list.add(new Department(o.getString("id"), o.getString("code"), o.getString("name"), o.getString("position")));
		}

		return list;
	}

	public List<Theme> getThemeList(String code) throws UnirestException {
		List<Theme> list = new ArrayList<>();

		HttpResponse<JsonNode> response = Unirest.post(getThemeURL)
				.field("code", code)
				.asJson();
		
		Logger.getLogger(Session.class.getName()).log(Level.INFO, response.getBody().toString());

		JSONArray array = response.getBody().getObject().optJSONArray("info");
		if (array != null) {
			for (int i = 0; i < array.length(); ++i) {
				JSONObject o = array.getJSONObject(i);
				list.add(new Theme(o.getString("id"), o.getString("name")));
			}
		}

		return list;
	}

	public boolean register(Request request) {
		boolean result = false;

		try {
			HttpResponse<String> response = Unirest.get(registrationURL).asString();
			Document doc = Jsoup.parse(response.getBody());
			String captchaHash = getCaptchaHash(doc);

			List<String> dateList = waitForDateList(request.department.code, request.timeout);
			if (!dateList.isEmpty() && !dateList.get(0).isEmpty()) {
				Time time = getClosestTime(request.department.code, dateList.get(0), request.desiredTime);
				
				if (time.isValid()) {
					response = Unirest.post(registrationURL)
							.header("Accept-encoding", "identity")
							.field("themes_id", request.theme.id)
							.field("structure_code", request.structure.code)
							.field("otdel_code", request.department.code)
							.field("date_start", dateList.get(0))
							.field("time_start", time.toString())
							.field("captcha_text", uncaptcha.decode(captchaHash))
							.field("captcha_md5", captchaHash)
							.field("requests", "Отправить")
							.field("id", "0")
							.field("date_reg", ".")
							.asString();

					if (response != null && response.getStatus() == 302) {
						Unirest.get(addCommentURL).asString();
						Unirest.post(addCommentURL)
								.field("comments", request.comment)
								.field("send", "Отправить")
								.asString();
						result = true;
					} else {
						Logger.getLogger(Session.class.getName()).log(Level.SEVERE, 
								String.format("Failed to register: %d", response != null ? response.getStatus() : 0));
					}
				}
			}

		} catch (UnirestException ex) {
			Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
		}

		return result;
	}

	private List<String> waitForDateList(int code, int timeout) throws UnirestException {
		long startTime = System.currentTimeMillis();

		while (System.currentTimeMillis() - startTime < timeout * 1000) {
			List<String> list = getDateList(code);

			if (!list.isEmpty() && !list.get(0).isEmpty()) {
				return list;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
			}
		}

		return new ArrayList<>();
	}
	
	private Time getClosestTime(int code, String date, Time desiredTime) throws UnirestException {
		List<Time> timeList = getTimeList(code, date);

		if (timeList.isEmpty()) {
			return Time.getInvalid();
		}
		
		Time time = timeList.get(0);
		int delta = Math.abs(desiredTime.value - time.value);
		for (int i = 1; i < timeList.size(); ++i) {
			int newDelta = Math.abs(desiredTime.value - timeList.get(i).value);
			if (newDelta >= delta) {
				break;
			} else {
				time = timeList.get(i);
				delta = newDelta;
			}
		}

		return time;
	}

	private List<String> getDateList(int code) throws UnirestException {
		List<String> list = new ArrayList<>();

		HttpResponse<JsonNode> response = Unirest.post(getDateURL)
				.field("code", code)
				.asJson();

		JSONArray array = response.getBody().getArray();
		for (int i = 0; i < array.length(); ++i) {
			list.add(array.getString(i));
		}
		
		return list;
	}

	private List<Time> getTimeList(int code, String date) throws UnirestException {
		List<Time> list = new ArrayList<>();

		HttpResponse<JsonNode> response = Unirest.post(getTimeURL)
				.field("code", code).field("dates", date)
				.asJson();

		JSONArray array = response.getBody().getArray();
		for (int i = 0; i < array.length(); ++i) {
			list.add(new Time(array.getString(i)));
		}

		return list;
	}

	private String getCaptchaHash(Document doc) {
		Elements tags = doc.getElementsByTag("input");
		for (Element tag : tags) {
			if (tag.attr("name").equals("captcha_md5")) {
				return tag.attr("value");
			}
		}
		return "";
	}

	private final CaptchaMd5Decoder uncaptcha;
}
