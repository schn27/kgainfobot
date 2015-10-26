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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Kgainfo captcha 'decoder' based on:
 *   1. kgainfo server transmits MD5 hash of the right answer to captcha
 *   2. the right answer to captcha is a 4 letters string representing a number in the range of 0-9999
 * So this class just decodes a string from MD5 hash with brut force.
 * 
 * @author amalikov
 */
public class CaptchaMd5Decoder {
	public CaptchaMd5Decoder() {
		md5 = new HashMap<>();
		init();
	}
	
	public String decode(String hash) {
		return md5.getOrDefault(hash, "");
	}
	
	private void init() {
		try {
			for (int i = 0; i < 10000; ++i) {
				String num = String.format("%04d", i);
				String hash = getMD5(num.getBytes("UTF-8"));
				md5.put(hash, num);
			}
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
			Logger.getLogger(CaptchaMd5Decoder.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private String getMD5(byte[] data) throws NoSuchAlgorithmException {
		byte[] digest = MessageDigest.getInstance("MD5").digest(data);
		String result = "";
		for (int i = 0; i < digest.length; i++)
			result += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
		return result;
	}
	
	private final Map<String, String> md5;
}
