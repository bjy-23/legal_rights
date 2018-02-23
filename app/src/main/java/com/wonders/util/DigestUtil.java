package com.wonders.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import org.apache.commons.lang.RandomStringUtils;
/**
 * 支持MD5和SHA1的散列函数方法工具类
 * @author tangjiawei
 *
 */
public class DigestUtil {
	private static final String SHA1 = "SHA-1";
	private static final String MD5 = "MD5";

	//-- String Hash function --//
	/**
	 * 对输入字符串进行sha1散列, 返回Hex编码的结果.
	 */
	public static String sha1Hex(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.encodeHex(digestResult);
	}

	/**
	 * 对输入字符串进行sha1散列, 返回Base64编码的结果.
	 */
	public static String sha1Base64(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.encodeBase64(digestResult);
	}

	/**
	 * 对输入字符串进行sha1散列, 返回Base64编码的URL安全的结果.
	 */
	public static String sha1Base64UrlSafe(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.encodeUrlSafeBase64(digestResult);
	}

	/**
	 * 对字符串进行散列, 支持md5与sha1算法.
	 */
	private static byte[] digest(String input, String algorithm) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			return messageDigest.digest(input.getBytes());
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	//-- File Hash function --//
	/**
	 * 对文件进行md5散列, 返回Hex编码结果.
	 */
	public static String md5Hex4File(InputStream input) throws IOException {
		return digest(input, MD5);
	}

	/**
	 * 对文件进行sha1散列, 返回Hex编码结果.
	 */
	public static String sha1Hex(InputStream input) throws IOException {
		return digest(input, SHA1);
	}

	private static String digest(InputStream input, String algorithm) throws IOException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			int bufferLength = 1024;
			byte[] buffer = new byte[bufferLength];
			int read = input.read(buffer, 0, bufferLength);

			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, bufferLength);
			}

			return EncodeUtils.encodeHex(messageDigest.digest());

		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 对字符串进行md5散列，返回Hex编码结果
	 * @param input
	 * @return
	 */
	public static String md5Hex4String(String input) {
		String result = EncodeUtils.encodeHex(digest(input, "MD5"));
		return result;
	}
	
    /**
     * 产生随机密码。
     * 
     * @param count
     *            密码长度
     * @return 随机密码
     */
    public static String createRandomPassword(int count) {
        if (count < 1) {
            count = 8;
        }
        return RandomStringUtils.randomAlphanumeric(count);
    }
}
