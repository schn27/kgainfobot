/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.kgainfobot;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author amalikov
 */
public class KgaInfoSession {
	public KgaInfoSession() {
		uncaptcha = new CaptchaMd5Decoder();
	}

	public boolean login(String username, String password) {
		boolean result = false;
		
		try {
			Unirest.get("http://priem.kgainfo.spb.ru").asString();

			HttpResponse<String> response = Unirest.post("http://priem.kgainfo.spb.ru/login")
					.header("Content-type", "application/x-www-form-urlencoded")
					.header("Accept-encoding", "identity")
					.body("username=" + username + "&password=" + password + "&login=%D0%92%D0%BE%D0%B9%D1%82%D0%B8")
					.asString();

			if (response != null && response.getStatus() == 302) {
				Unirest.get("http://priem.kgainfo.spb.ru").asString();
				result = true;
			}
			
		} catch (UnirestException ex) {
			Logger.getLogger(CaptchaMd5Decoder.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return result;
	}
	
	public boolean register() {
		boolean result = false;
		
		try {
			HttpResponse<String> response = Unirest.get("http://priem.kgainfo.spb.ru/user/requests").asString();
			Document doc = Jsoup.parse(response.getBody());
			String captchaHash = getCaptchaHash(doc);
			System.out.println("captcha = " + uncaptcha.decode(captchaHash) + " " + captchaHash);
			
		} catch (UnirestException ex) {
			Logger.getLogger(CaptchaMd5Decoder.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return result;
	}
	
	private String getCaptchaHash(Document doc) {
		Elements inputTags = doc.getElementsByTag("input");
		for (Element inputTag : inputTags) {
			if (inputTag.attr("name").equals("captcha_md5"))
				return inputTag.attr("value");
		}
		return "";
	}

	private final CaptchaMd5Decoder uncaptcha;
}
