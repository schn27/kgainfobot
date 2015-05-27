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
package schn27.kgainfobot;

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

/**
 *
 * @author amalikov
 */
public class Session {

	public Session() {
		host = "http://priem.kgainfo.spb.ru";
		uncaptcha = new CaptchaMd5Decoder();
	}

	public boolean login(String username, String password) {
		boolean result = false;

		try {
			Unirest.get(host).asString();

			HttpResponse<String> response = Unirest.post(host + "/login")
					.header("Accept-encoding", "identity")
					.field("username", username)
					.field("password", password)
					.field("login", "Войти")
					.asString();

			if (response.getStatus() == 302) {
				Unirest.get(host).asString();
				result = true;
			}

		} catch (UnirestException ex) {
			Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
		}

		return result;
	}

	public List<Structure> getStructureList() {
		List<Structure> list = new ArrayList<>();

		try {
			HttpResponse<String> response = Unirest.get(host + "/user/requests").asString();
			Document doc = Jsoup.parse(response.getBody());

			Elements tags = doc.getElementsByTag("select");
			for (Element tag : tags)
				if (tag.attr("name").equals("structure_code")) {
					Elements options = tag.children();
					for (Element option : options)
						if (!option.attr("label").isEmpty())
							list.add(new Structure(option.attr("value"), option.attr("label")));
				}
		} catch (UnirestException ex) {
			Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
		}

		return list;
	}

	public List<Department> getDepartmentList(String code) throws UnirestException {
		List<Department> list = new ArrayList<>();

		HttpResponse<JsonNode> response = Unirest.post(host + "/service/getdep")
				.field("code", code)
				.asJson();

		JSONArray array = response.getBody().getArray();
		for (int i = 0; i < array.length(); ++i) {
			JSONObject o = array.getJSONObject(i);
			list.add(new Department(o.getString("id"), o.getString("code"), o.getString("name"), o.getString("position")));
		}

		return list;
	}

	public List<Theme> getThemeList(String code) throws UnirestException {
		List<Theme> list = new ArrayList<>();

		HttpResponse<JsonNode> response = Unirest.post(host + "/service/getthemes")
				.field("code", code)
				.asJson();

		JSONArray array = response.getBody().getObject().optJSONArray("info");
		if (array != null)
			for (int i = 0; i < array.length(); ++i) {
				JSONObject o = array.getJSONObject(i);
				list.add(new Theme(o.getString("id"), o.getString("name")));
			}

		return list;
	}

	public boolean register(RegisterRequest request) {
		boolean result = false;

		try {
			HttpResponse<String> response = Unirest.get(host + "/user/requests").asString();
			Document doc = Jsoup.parse(response.getBody());
			String captchaHash = getCaptchaHash(doc);

			List<String> dateList = getDateList(request.departmentCode);
			if (!dateList.isEmpty()) {
				Time time = getClosestTime(request.departmentCode, dateList.get(0), request.desiredTime);
				if (time.value != Time.INVALID_VALUE) {
					System.out.println("chosen time = " + time);

					response = Unirest.post(host + "/user/requests")
							.header("Accept-encoding", "identity")
							.field("themes_id", request.themeId)
							.field("structure_code", request.structureCode)
							.field("otdel_code", request.departmentCode)
							.field("date_start", dateList.get(0))
							.field("time_start", time.toString())
							.field("captcha_text", uncaptcha.decode(captchaHash))
							.field("captcha_md5", captchaHash)
							.field("requests", "Отправить")
							.field("id", "0")
							.field("date_reg", ".")
							.asString();

					if (response != null && response.getStatus() == 302) {
						Unirest.get(host + "/user/addcomments").asString();
						Unirest.post(host + "/user/addcomments")
								.field("comments", request.comment)
								.field("send", "Отправить")
								.asString();
						result = true;
					}
				}
			}

		} catch (UnirestException ex) {
			Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
		}

		return result;
	}

	private Time getClosestTime(int code, String date, Time desiredTime) throws UnirestException {
		Time res = new Time(Time.INVALID_VALUE);
		List<Time> timeList = getTimeList(code, date);

		if (!timeList.isEmpty()) {
			res = timeList.get(0);
			int delta = Math.abs(desiredTime.value - res.value);
			for (int i = 1; i < timeList.size(); ++i) {
				int newDelta = Math.abs(desiredTime.value - timeList.get(i).value);
				if (newDelta >= delta)
					break;
				else {
					res = timeList.get(i);
					delta = newDelta;
				}
			}
		}

		return res;
	}

	private List<String> getDateList(int code) throws UnirestException {
		List<String> list = new ArrayList<>();

		HttpResponse<JsonNode> response = Unirest.post(host + "/service/getdate")
				.field("code", code)
				.asJson();

		JSONArray array = response.getBody().getArray();
		for (int i = 0; i < array.length(); ++i)
			list.add(array.getString(i));

		return list;
	}

	private List<Time> getTimeList(int code, String date) throws UnirestException {
		List<Time> list = new ArrayList<>();

		HttpResponse<JsonNode> response = Unirest.post(host + "/service/getfreetime")
				.field("code", code).field("dates", date)
				.asJson();

		JSONArray array = response.getBody().getArray();
		for (int i = 0; i < array.length(); ++i)
			list.add(new Time(array.getString(i)));

		return list;
	}

	private String getCaptchaHash(Document doc) {
		Elements tags = doc.getElementsByTag("input");
		for (Element tag : tags)
			if (tag.attr("name").equals("captcha_md5"))
				return tag.attr("value");
		return "";
	}

	private final String host;
	private final CaptchaMd5Decoder uncaptcha;
}
