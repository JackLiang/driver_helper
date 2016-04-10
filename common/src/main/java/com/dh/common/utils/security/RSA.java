package com.dh.common.utils.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * RSA实现
 * 
 * @author haibin_chen 2015年11月9日
 *
 */
public class RSA {

	private static final String ALGORITHM = "RSA";

	private static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * 随机生成密钥对
	 * 
	 * @return {"publicKey":RSAPublicKey Object, "privateKey":RSAPrivateKey Object}
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {  
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);  
        keyPairGen.initialize(1024);  
  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
  
        // 公钥  
        PublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
  
        // 私钥  
        PrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
  
        Map<String, Object> keyMap = new HashMap<String, Object>(2);  
  
        keyMap.put("publicKey", publicKey);  
        keyMap.put("privateKey", privateKey);  
        return keyMap;  
    }  
	
	 /**
     * 得到密钥字符串（经过base64编码）
     * 
     * @param key PublicKey或PublicKey对象
     * @return
     */
    public static String getKeyString(Key key) throws Exception {
          byte[] keyBytes = key.getEncoded();
          String s = Base64.encode(keyBytes);
          return s;
    }
    
	/**
	 * RSA 签名
	 * 
	 * @param content 签名内容
	 * @param privateKey 私钥
	 * @param algorithm 有SHA1WithRSA、MD5WithRSA等
	 * 
	 * @return 注，结果经过Base64编码
	 * 
	 * @throws Exception
	 */
	public static String sign(String content, String privateKey, String algorithm) throws Exception {
		
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
		KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
		PrivateKey priKey = keyf.generatePrivate(priPKCS8);

		Signature signature = Signature.getInstance(algorithm);

		signature.initSign(priKey);
		signature.update(content.getBytes(DEFAULT_CHARSET));

		byte[] signed = signature.sign();
		return Base64.encode(signed);
	}

	/**
	 * RSA签名检查，验证签名数据的sign和传入的sign是否一致
	 * 
	 * @param content 待签名数据
	 * @param sign 签名值，该值是经过Base64编码的
	 * @param publicKey 公钥，该值是经过Base64编码的
	 * @return 
	 * @throws Exception 
	 */
	public static boolean verifySign(String content, String sign, String publicKey, String algorithm) throws Exception {
		
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] encodedKey = Base64.decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
	
		Signature signature = Signature.getInstance(algorithm);
	
		signature.initVerify(pubKey);
		signature.update(content.getBytes(DEFAULT_CHARSET));
	
		return signature.verify(Base64.decode(sign));
	}
	
	 /** 
	  * 用公钥加密 
	  *  
	  * @param content 要加密的内容
	  * @param publicKey 公钥，该值是经过Base64编码的
	  * @return 加密后的字符串，经过Base64编码
	  * 
	  * @throws Exception 
	  */  
    public static String encrypt(String content, String publicKey) throws Exception {  
    	PublicKey pubKey = getPublicKey(publicKey);
  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);  
  
        InputStream ins = new ByteArrayInputStream(content.getBytes(DEFAULT_CHARSET));  
        ByteArrayOutputStream writer = new ByteArrayOutputStream();  
        // rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密  
        byte[] buf = new byte[128];  
        int bufl;  
  
        while ((bufl = ins.read(buf)) != -1) {  
            byte[] block = null;  
  
            if (buf.length == bufl) {  
                block = buf;  
            } else {  
                block = new byte[bufl];  
                for (int i = 0; i < bufl; i++) {  
                    block[i] = buf[i];  
                }  
            }  
  
            writer.write(cipher.doFinal(block));  
        }  
  
        return Base64.encode(writer.toByteArray());  
    }  
  
	/**
	 * 用私钥解密
	 * 
	 * @param content 密文，该值是经过Base64编码的
	 * @param privateKey 私钥，该值是经过Base64编码的
	 * 
	 * @return 解密后的字符串
	 */
	public static String decrypt(String content, String privateKey) throws Exception {
        PrivateKey prikey = getPrivateKey(privateKey);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //RSA解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), DEFAULT_CHARSET);
    }

	
	/**
	* 得到私钥
	* 
	* @param key 密钥字符串（经过base64编码）
	* 
	* @throws Exception
	*/
	public static PrivateKey getPrivateKey(String key) throws Exception {

		byte[] keyBytes;
		
		keyBytes = Base64.decode(key);
		
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		
		return privateKey;
	}
	
	/** 
     * 得到公钥 
     *  
     * @param key 公钥字符串（经过base64编码） 
     * 
     * @throws Exception 
     */  
    public static PublicKey getPublicKey(String key) throws Exception {  
        byte[] keyBytes = Base64.decode(key);  
  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
  
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);  
  
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);  
        return publicKey;  
    } 
    
    public static void main(String[] args) throws Exception {
		Map<String, Object> initKey = RSA.initKey();
		PublicKey pubKey = (PublicKey) initKey.get("publicKey");
		PrivateKey priKey = (PrivateKey) initKey.get("privateKey");
		
		String pubKeyStr = getKeyString(pubKey);
		String priKeyStr = getKeyString(priKey);
		System.out.println("公钥：" + pubKeyStr);
		System.out.println("私钥：" + priKeyStr);
		
		String content = "abc";
		String sign = RSA.sign(content, priKeyStr, "SHA1WithRSA");
		System.out.println("verify sign:" + RSA.verifySign(content, sign, pubKeyStr, "SHA1WithRSA"));
		
		String encrypt = RSA.encrypt(content, pubKeyStr);
		System.out.println("encrypt:" + encrypt);
		String decrypt = RSA.decrypt(encrypt, priKeyStr);
		System.out.println("decrypt:" + decrypt);
	}
}
