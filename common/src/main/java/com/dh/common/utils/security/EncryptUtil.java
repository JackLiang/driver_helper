/**
 * 友魄科技
 */
package com.dh.common.utils.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

/**
 * 加密类
 * 
 * @author ying_liu 2015年4月9日
 */
public class EncryptUtil {
	/**
	 * 返回md5摘要的值
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5(String str) {
		return encode(str, "MD5");
	}

	/**
	 * 返回SHA-1摘要
	 * 
	 * @param str
	 * @return
	 */
	public static String getSHA1(String str) {
		return encode(str, "SHA-1");
	}

	/**
	 * 截取第8位到20位，从0位开始计算
	 * 
	 * @param str
	 * @return
	 */
	public static String getLittleMD5(String str) {
		String estr = encode(str, "MD5");
		return estr.substring(8, 20);
	}
	
	/**
	 * 截取第8位到8+num位，从0位开始计算
	 * 
	 * @param str
	 * @param num
	 * @return
	 */
	public static String getLittleMD5(String str, int num) {
		String estr = encode(str, "MD5");
		return estr.substring(8, 8 + num);
	}

	/**
	 * 截取第0位到20位
	 * 
	 * @param str
	 * @return
	 */
	public static String getLittleSHA1(String str) {
		String estr = encode(str, "SHA-1");
		return estr.substring(0, 20);
	}

	private static String encode(String str, String type) {
		try {
			MessageDigest alga = MessageDigest.getInstance(type);
			alga.update(str.getBytes("UTF-8"));
			byte[] digesta = alga.digest();
			return byte2hex(digesta);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static String getSHA1s(String[] strs) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			for (int i = 0; i < strs.length; i++) {
				if (StringUtils.isNotEmpty(strs[i])) {
					md.update(strs[i].getBytes());
				}
			}
			byte[] bs = md.digest();
			return byte2hex(bs);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static String byte2hex(byte[] b) {
		StringBuilder buf = new StringBuilder(32);
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				buf.append("0").append(stmp);
			else
				buf.append(stmp);
		}
		return buf.toString().toUpperCase();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 异或加密
	 * 
	 * @param str
	 * @param key
	 * @return
	 */
	public static String xorEncrypt(String str, String key) {
		BigInteger strbi = new BigInteger(str.getBytes());
		BigInteger keybi = new BigInteger(key.getBytes());
		BigInteger encryptbi = strbi.xor(keybi);

		return new String(encryptbi.toByteArray());
	}

	/**
	 * 异或解密
	 * 
	 * @param encryptStr
	 * @param key
	 * @return
	 */
	public static String xorDecrypt(String encryptStr, String key) {
		BigInteger encryptbi = new BigInteger(encryptStr.getBytes());
		BigInteger keybi = new BigInteger(key.getBytes());
		BigInteger decryptbi = encryptbi.xor(keybi);
		return new String(decryptbi.toByteArray());
	}

	// 将 s 进行 BASE64 编码
	public static String base64Encode(String s) {
		if (StringUtils.isBlank(s))
			return "";
		try {
			return Base64.encode(s.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static String base64Decode(String s) {
		if (StringUtils.isBlank(s))
			return "";
		try {
			return new String(Base64.decode(s), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
		
	}

	/**
	 * DES加密
	 * 
	 * @param datasource
	 * @param password
	 * @return
	 */
	public static byte[] desEncrypt(byte[] datasource, String password)  throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(datasource);
	}

	/**
	 * DES解密
	 * 
	 * @param src
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] desDecrypt(byte[] src, String password) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}

	public static void main(String[] args) throws Exception {
//		System.out.println(getLittleMD5("A10174qws345fd67yhgbv00wsa", 8));
//		System.out.println("f8aeabd0cba6efdf28d65e9d805c837c".equalsIgnoreCase(getMD5("sk@#2usjdkf")));
//		String test = "123456";
//		String key = "f8aeabd0cba6efdf28d65e9d805c837c";
//		byte[] encry = EncryptUtil.desEncrypt(test.getBytes(), key);
//		String str = Base64.encode(encry);
//		System.out.println("加密后：" + str);
//		System.out.println("解密后：" + new String(EncryptUtil.desDecrypt(encry, key)));
		
		String plaintext = "qwer@87632e23";
		String key = "20120401";
		byte[] encry = EncryptUtil.desEncryptWithIV(plaintext.getBytes(), key);
		String str = Base64.encode(encry);
	     System.out.println("明文：" + plaintext);
	     System.out.println("密钥：" + key);
	     System.out.println("加密并base64：" + str);
	     System.out.println("解密后：" + new String(EncryptUtil.desDecryptWithIV(encry, key)));
	}
	
	/** 特别算法的des加解密    begin */
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };
    public static byte[] desEncryptWithIV(byte[] datasource, String encryptKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        return cipher.doFinal(datasource);
    }
    
    public static byte[] desDecryptWithIV(byte[] src, String decryptKey) throws Exception {
          IvParameterSpec zeroIv = new IvParameterSpec(iv);
          SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
          Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
          cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
          return cipher.doFinal(src);
     }
     /** end */
    
}
