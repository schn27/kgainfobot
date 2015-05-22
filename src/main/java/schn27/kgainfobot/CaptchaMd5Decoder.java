/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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