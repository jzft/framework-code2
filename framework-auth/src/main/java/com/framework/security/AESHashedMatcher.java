package com.framework.security;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/*import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import org.apache.commons.codec.binary.Base64Decoder;*/

/**
 * 
 * @ClassName:     AESHashedMatcher.java
 * @Description:   ASE匹配登录密码
 * @author         Administrator
 * @version        V1.0  
 * @Date           2017年1月5日 下午4:04:56
 */
public class AESHashedMatcher extends HashedCredentialsMatcher {
	private static Logger log = Logger.getLogger(AESHashedMatcher.class);
	private String encryptKey = "freight";
	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken,
			AuthenticationInfo info) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		
		String tokenCredentials;
		try {
			setStoredCredentialsHexEncoded(false);
			
			tokenCredentials = encrypt(String.valueOf(token.getPassword()));
		
		Object accountCredentials = info.getCredentials();
		if(tokenCredentials==null||accountCredentials==null){
			return false;
		}
		log.debug("--------accountCredentials:"+accountCredentials+";tokenCredentials:"+tokenCredentials+"------------");
		return tokenCredentials.equals(accountCredentials);
		} catch (Exception e) {
			throw new RuntimeException("AES加密失败",e);
		}
	}

	/** 
	 * @Title:        encrypt 
	 * @Description:  将传进来密码加密方法 
	 * @param:        @param data
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       String    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:01:05 
	 */
	private String encrypt(String data) throws Exception {
		
		return aesEncrypt(data,encryptKey);
	}

	/** 
	 * @Title:        aesEncryptToBytes 
	 * @Description:  AES加密 
	 * @param:        @param content 待加密的内容
	 * @param:        @param encryptKey 加密密钥
	 * @param:        @return 加密后的byte[]
	 * @param:        @throws Exception    
	 * @return:       byte[]    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:05:46 
	 */
	private byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(encryptKey.getBytes());
		kgen.init(128, secureRandom);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		
		return cipher.doFinal(content.getBytes("utf-8"));
	}
	
	/** 
	 * @Title:        aesEncrypt 
	 * @Description:  AES加密为base 64 code 
	 * @param:        @param content 待加密的内容
	 * @param:        @param encryptKey 加密密钥
	 * @param:        @return 加密后的base 64 code
	 * @param:        @throws Exception    
	 * @return:       String    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:06:32 
	 */
	public String aesEncrypt(String content, String encryptKey) throws Exception {
		return base64Encode(aesEncryptToBytes(content, encryptKey));
	}
	
	public String aesDecrypt(String content, String encryptKey) throws Exception {
		return aesDecryptByBytes(base64Decoder(content), encryptKey);
	}
	
	public static void main(String[] args) throws Exception {
		String e = new AESHashedMatcher().aesEncrypt("123123aa", "freight");
		System.out.println(e);
		String a = new AESHashedMatcher().aesDecrypt(e, "freight");
		System.out.println(a);
	} 
	
	/** 
	 * @Title:        aesDecryptByBytes 
	 * @Description:  AES解密 
	 * @param:        @param encryptBytes 待解密的byte[]
	 * @param:        @param decryptKey 解密密钥
	 * @param:        @return 解密后的String
	 * @param:        @throws Exception    
	 * @return:       String    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:07:15 
	 */
	private String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(encryptKey.getBytes());
		kgen.init(128, secureRandom);
		
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		
		return new String(decryptBytes);
	}
	
	/** 
	 * @Title:        base64Encode 
	 * @Description:  base 64 encode 
	 * @param:        @param bytes 待编码的byte[]
	 * @param:        @return 编码后的base 64 code    
	 * @return:       String    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:07:58 
	 */
	private String base64Encode(byte[] bytes){
		String res = Base64.encodeBase64String(bytes);
		return res;
		
	}
	
	private byte[] base64Decoder(String data) throws IOException{		
		byte[] bytes=Base64.decodeBase64(data);		
		return bytes;
	}


	public String getEncryptKey() {
		return encryptKey;
	}

	public void setEncryptKey(String encryptKey) {
		this.encryptKey = encryptKey;
	}
	
}