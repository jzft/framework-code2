package com.framework.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESUtils { 

	private static final String PASSWORD_CRYPT_KEY = "11111111";  

	//private final static String DES = "DES";  
	
	//private static final byte[] desKey;   
	
	//

	//解密数据
	public static String decrypt(String message,String key) throws Exception {    

		byte[] bytesrc =convertHexString(message);       
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");        
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("GBK"));       
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");       
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);       
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("GBK"));    
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);          
		byte[] retByte = cipher.doFinal(bytesrc);         
		return new String(retByte,"GBK");     
	}   

	public static byte[] encryptByte(String message, String key) throws Exception {    
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");   
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));   
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");    
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);    
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));    
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);   
		return cipher.doFinal(message.getBytes("GBK"));    
	}   

	public static String encrypt(String value, String key){ 

		String result=""; 

		try{ 
//			value=java.net.URLEncoder.encode(value, "UTF-8");  
			result=toHexString(encryptByte(value, key)).toUpperCase();
		}catch(Exception ex){ 
			ex.printStackTrace(); 
			return ""; 
		} 

		return result;  
	} 
	
	public static String encrypt(String value){ 

		String result=""; 

		try{ 
//			value=java.net.URLEncoder.encode(value, "UTF-8");  
			result=toHexString(encryptByte(value, PASSWORD_CRYPT_KEY)).toUpperCase();
		}catch(Exception ex){ 
			ex.printStackTrace(); 
			return ""; 
		} 

		return result;  
	} 

	public static byte[] convertHexString(String ss){     

		byte digest[] = new byte[ss.length() / 2];     

		for(int i = 0; i < digest.length; i++){     
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);     
			digest[i] = (byte)byteValue;     
		}     

		return digest;     
	}    

	public static String toHexString(byte b[]) {    
		StringBuffer hexString = new StringBuffer();    

		for (int i = 0; i < b.length; i++) {    
			
			String plainText = Integer.toHexString(0xff & b[i]);    
			if (plainText.length() < 2)              
				plainText = "0" + plainText;    
			hexString.append(plainText);    
		}       
		return hexString.toString();    

	}   
	
	public static void main(String[] args) throws Exception {
		String pass = encrypt("", PASSWORD_CRYPT_KEY);
		
		System.out.println(pass);
		System.out.println(decrypt(pass, PASSWORD_CRYPT_KEY));
		
//		Boolean bo = true;
//		System.out.println( bo.hashCode());
//		Pattern p = Pattern.compile("^\\w+(\\.?\\w+)+@\\w+(\\.\\w+)+(\\,\\w+@\\w+(\\.\\w+)+)*$");     
//        Matcher m = p.matcher("2292048d97@qq.c,om");  
// 
//        System.out.println( m.matches());
//	    
//		String cacheKey = String.format("MebPropertyTypeList-Flag:%1$d", 1333);
//		System.out.println("cacheKey:"+cacheKey);
//		String value="caisp";    
//
//		    
//
//		System.out.println("加密数据:"+value);  
//
//		    
//
//		System.out.println("密码为:"+"abcdefgh");  

		    

//		String a=StringUtils.encrypt( value);    

		    

//		System.out.println("加密后的数据为:"+a); 
		Long start = System.currentTimeMillis();
		for(int i=0;i<100;i++){
//			System.out.println(MD5Util.encode("携程同学_221"));
			
//		System.out.println(DESUtils.encrypt("携程同学_221"));
//		String b = DESUtils.decrypt("EE2128C49399B457A0519571B18AEFEE", PASSWORD_CRYPT_KEY);
//		System.out.println("解密后的数据为:"+b);
		}
		System.out.println((System.currentTimeMillis()-start));
		}
}

