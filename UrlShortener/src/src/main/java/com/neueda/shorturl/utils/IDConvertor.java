package com.neueda.shorturl.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/*
 * This class contains the logic for creating unique 7 digit url
 * using base 62 provided unique number from Redis and Current time
 * */
public class IDConvertor {

	public static final IDConvertor INSTANCE = new IDConvertor();

	private String converttoMD5Hash(String url) {
		try {

			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			// of an input digest() return array of byte
			byte[] messageDigest = md.digest(url.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	static String encodeIdToStr(long id) {
		char[] map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		while (id > 0) {
			int idx = (int) (id % 62);
			sb.append(map[idx]);
			id = id / 62;
		}
		return sb.toString();
	}

	public static String createUniqueID(Long id) {
		id = System.currentTimeMillis() + id;
		String uniqueURLID = encodeIdToStr(id);
		return uniqueURLID;
	}

}