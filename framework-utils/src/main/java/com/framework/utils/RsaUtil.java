package com.framework.utils;




import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.apache.commons.lang3.StringUtils;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class RsaUtil {
	  private static KeyPairGenerator keyGen; // Key pair generator for RSA
	    private static PrivateKey privateKey; // Private Key Class
	    private static PublicKey publicKey; // Public Key Class
	    private static KeyPair keypair; // KeyPair Class
	    @SuppressWarnings("unused")
		private static Signature sign; // Signature, used to sign the data
	    private static String publicKeyStr;
	    private static String  privateKeyStr;
//  /**
//   * 生成RSA密钥对
//   * @return
//   */
//
//  public static KeyPair generateRSAKeyPair() {
//      return generateRSAKeyPair(1024);
//  }
//  /**
//   * 生成RSA密钥对
//   * 
//   * @param keyLength
//   *            密钥长度，范围：512～2048
//   * @return
//   */
//  public static KeyPair generateRSAKeyPair(int keyLength) {
//      try {
//          KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//          kpg.initialize(keyLength);
//          return kpg.genKeyPair();
//      } catch (NoSuchAlgorithmException e) {
//    	  e.printStackTrace();
//          return null;
//      }
//  }
/**
 * 将公钥转换为XML字符串
 * @param key
 * @return
 */
  public static String encodePublicKeyToXml(PublicKey key) {
      if (!RSAPublicKey.class.isInstance(key)) {
          return null;
      }
      RSAPublicKey pubKey = (RSAPublicKey) key;
      StringBuilder sb = new StringBuilder();
      sb.append("<RSAKeyValue>")
      		  .append("<Modulus>")
              .append(Base64.getEncoder().encode(pubKey.getModulus().toByteArray()))
              .append("</Modulus>")
      		  .append("<Exponent>")
              .append(Base64.getEncoder().encode(pubKey.getPublicExponent().toByteArray()))
              .append("</Exponent>");
      sb.append("</RSAKeyValue>");
      return sb.toString();

  }


/**
 * 从XML字符串得到公钥
 * @param xml
 * @return
 */
  public static PublicKey decodePublicKeyFromXml(String xml) {
      xml = xml.replaceAll("\r", "").replaceAll("\n", "");
      BigInteger modulus = new BigInteger(1, Base64Helper.decode(StringUtils.substringBetween(xml, "<Modulus>", "</Modulus>")));
      BigInteger publicExponent = new BigInteger(1,
      Base64Helper.decode(StringUtils.substringBetween(xml,"<Exponent>", "</Exponent>")));
      RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);
      KeyFactory keyf;
      try {
          keyf = KeyFactory.getInstance("RSA");
          return keyf.generatePublic(rsaPubKey);
      } catch (Exception e) {
    	  e.printStackTrace();
          return null;
      }
  }

  /**
   * 将私钥转换为XML字符串
   * @param xml
   * @return
   */
  public static PrivateKey decodePrivateKeyFromXml(String xml) {
      xml = xml.replaceAll("\r", "").replaceAll("\n", "");
      BigInteger modulus = new BigInteger(1, Base64Helper.decode(StringUtils.substringBetween(xml, "<Modulus>", "</Modulus>")));
      BigInteger publicExponent = new BigInteger(1,
              Base64Helper.decode(StringUtils.substringBetween(xml,
                      "<Exponent>", "</Exponent>")));
      BigInteger privateExponent = new BigInteger(1,
              Base64Helper.decode(StringUtils.substringBetween(xml, "<D>","</D>")));

      BigInteger primeP = new BigInteger(1, Base64Helper.decode(StringUtils.substringBetween(xml, "<P>", "</P>")));

      BigInteger primeQ = new BigInteger(1, Base64Helper.decode(StringUtils.substringBetween(xml, "<Q>", "</Q>")));

      BigInteger primeExponentP = new BigInteger(1, Base64Helper.decode(StringUtils.substringBetween(xml, "<DP>","</DP>")));
      BigInteger primeExponentQ = new BigInteger(1,Base64Helper.decode(StringUtils.substringBetween(xml, "<DQ>","</DQ>")));
      BigInteger crtCoefficient = new BigInteger(1,Base64Helper.decode(StringUtils.substringBetween(xml,"<InverseQ>", "</InverseQ>")));
      RSAPrivateCrtKeySpec rsaPriKey = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ,primeExponentP, primeExponentQ, crtCoefficient);
      KeyFactory keyf;
      try {
          keyf = KeyFactory.getInstance("RSA");
          return keyf.generatePrivate(rsaPriKey);
      } catch (Exception e) {
    	  e.printStackTrace();
          return null;
      }

  }
  
/**
 * 从XML字符串得到私钥 
 * @param key
 * @return
 */
  public static String encodePrivateKeyToXml(PrivateKey key) {
      if (!RSAPrivateCrtKey.class.isInstance(key)) {
          return null;
      }
      RSAPrivateCrtKey priKey = (RSAPrivateCrtKey) key;
      StringBuilder sb = new StringBuilder();
      sb.append("<RSAKeyValue>");
      sb.append("<Modulus>")
              .append(Base64Helper.encode(priKey.getModulus().toByteArray()))
              .append("</Modulus>");
      sb.append("<Exponent>")
              .append(Base64Helper.encode(priKey.getPublicExponent()
                      .toByteArray())).append("</Exponent>");
      sb.append("<P>")
              .append(Base64Helper.encode(priKey.getPrimeP().toByteArray()))
              .append("</P>");
      sb.append("<Q>")
              .append(Base64Helper.encode(priKey.getPrimeQ().toByteArray()))
              .append("</Q>");
      sb.append("<DP>")
              .append(Base64Helper.encode(priKey.getPrimeExponentP()
                      .toByteArray())).append("</DP>");
      sb.append("<DQ>")
              .append(Base64Helper.encode(priKey.getPrimeExponentQ()
                      .toByteArray())).append("</DQ>");
      sb.append("<InverseQ>")
              .append(Base64Helper.encode(priKey.getCrtCoefficient()
                      .toByteArray())).append("</InverseQ>");
      sb.append("<D>")
              .append(Base64Helper.encode(priKey.getPrivateExponent()
                      .toByteArray())).append("</D>");
      sb.append("</RSAKeyValue>");
      return sb.toString();
  }



  // 用公钥加密

  public static byte[] encryptData(byte[] data, PublicKey pubKey) {
      try {
          Cipher cipher = Cipher.getInstance("RSA");
          cipher.init(Cipher.ENCRYPT_MODE, pubKey);
          return cipher.doFinal(data);
      } catch (Exception e) {
    	  e.printStackTrace();
          return null;
      }
  }
 
  
  // 用私钥解密
  public static byte[] decryptData(byte[] encryptedData, PrivateKey priKey) {
      try {
          Cipher cipher = Cipher.getInstance("RSA");
          cipher.init(Cipher.DECRYPT_MODE, priKey);
          return cipher.doFinal(encryptedData);
      } catch (Exception e) {
          return null;
      }
  }
  
 
 /**
	 * 得到私钥
	 * @param key 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	private static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = (new BASE64Decoder()).decodeBuffer(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}
 
 /**
	 * 得到公钥
	 * @param key 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	private static PublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = (new BASE64Decoder()).decodeBuffer(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

 


  /**
   * 根据指定私钥对数据进行签名(默认签名算法为"SHA1withRSA")
   * 
   * @param data
   *            要签名的数据
   * @param priKey
   *            私钥
   * @return
   */
  public static byte[] signData(byte[] data, PrivateKey priKey) {
      return signData(data, priKey, "SHA1withRSA");
  }

  /**

   * 根据指定私钥和算法对数据进行签名

   * 

   * @param data

   *            要签名的数据

   * @param priKey

   *            私钥

   * @param algorithm

   *            签名算法

   * @return

   */

  public static byte[] signData(byte[] data, PrivateKey priKey,
          String algorithm) {
      try {
          Signature signature = Signature.getInstance(algorithm);
          signature.initSign(priKey);
          signature.update(data);
          return signature.sign();
      } catch (Exception ex) {
    	  ex.printStackTrace();
          return null;
      }
  }

  /**
   * 用指定的公钥进行签名验证(默认签名算法为"SHA1withRSA")
   * 
   * @param data
   *            数据
   * @param sign
   *            签名结果
   * @param pubKey
   *            公钥
   * @return
   */
  public static boolean verifySign(byte[] data, byte[] sign, PublicKey pubKey) {
      return verifySign(data, sign, pubKey, "SHA1withRSA");
  }

  /**
   * 
   * @param data 数据
   * @param sign 签名结果
   * @param pubKey 公钥
   * @param algorithm 签名算法
   * @return
   */
  public static boolean verifySign(byte[] data, byte[] sign,
          PublicKey pubKey, String algorithm) {
      try {
          Signature signature = Signature.getInstance(algorithm);
          signature.initVerify(pubKey);
          signature.update(data);
          return signature.verify(sign);
      } catch (Exception ex) {
    	  ex.printStackTrace();
          return false;
      }
  }
  public static void generateKeys(int size){
	  generateKeys(1024, null, null, null, null);
  }
  public static void generateKeys(int size,String publicKeyPath,String privateKeyPath,String netPublicKeyPath,String netPrivateKeyPath) {
      try {
          System.out.println("Generatign Keys");
          // Get Key Pair Generator for RSA.
          keyGen = KeyPairGenerator.getInstance("RSA");
          keyGen.initialize(size);
          keypair = keyGen.genKeyPair();
          privateKey = keypair.getPrivate();
          publicKey = keypair.getPublic();
          // Get the bytes of the public and private keys
          byte[] privateKeyBytes = privateKey.getEncoded();
          byte[] publicKeyBytes = publicKey.getEncoded();
          publicKeyStr = Base64Helper.encode(publicKeyBytes);
          privateKeyStr = Base64Helper.encode(privateKeyBytes);
         
          writeKeyBytesToFile(publicKeyStr.getBytes(), publicKeyPath);
          writeKeyBytesToFile(privateKeyStr.getBytes(), privateKeyPath);


          String netPrivateKey = getRSAPrivateKeyAsNetFormat(privateKeyBytes);
          writeKeyBytesToFile(netPrivateKey.getBytes(), netPrivateKeyPath);
          
          String netPublicKey = getRSAPublicKeyAsNetFormat(privateKeyBytes);
          writeKeyBytesToFile(netPublicKey.getBytes(), netPublicKeyPath);
//          System.out.println(publicKeyStr);
//        
//          System.out.println(privateKeyStr);
//          System.err.println(netPublicKey);
//          System.err.println(netPrivateKey);
          
      } catch (java.security.NoSuchAlgorithmException e) {
          System.out
                  .println("No such algorithm. Please check the JDK version."
                          + e.getCause());
      }  catch (Exception ex) {
          System.out.println(ex);
      }  

  }
  
  private static String getRSAPrivateKeyAsNetFormat(byte[] encodedPrivkey) {
      try {
          StringBuffer buff = new StringBuffer(1024);

          PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(
                  encodedPrivkey);
          KeyFactory keyFactory = KeyFactory.getInstance("RSA");
          RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory
                  .generatePrivate(pvkKeySpec);

          buff.append("<RSAKeyValue>");
          buff.append("<Modulus>"
                  + Base64Helper.encode(removeMSZero(pvkKey.getModulus().toByteArray()))
                  + "</Modulus>");

          buff.append("<Exponent>"
                  + Base64Helper.encode(removeMSZero(pvkKey.getPublicExponent()
                          .toByteArray())) + "</Exponent>");

          buff.append("<P>"
                  + Base64Helper.encode(removeMSZero(pvkKey.getPrimeP().toByteArray()))
                  + "</P>");

          buff.append("<Q>"
                  + Base64Helper.encode(removeMSZero(pvkKey.getPrimeQ().toByteArray()))
                  + "</Q>");

          buff.append("<DP>"
                  + Base64Helper.encode(removeMSZero(pvkKey.getPrimeExponentP()
                          .toByteArray())) + "</DP>");

          buff.append("<DQ>"
                  + Base64Helper.encode(removeMSZero(pvkKey.getPrimeExponentQ()
                          .toByteArray())) + "</DQ>");

          buff.append("<InverseQ>"
                  + Base64Helper.encode(removeMSZero(pvkKey.getCrtCoefficient()
                          .toByteArray())) + "</InverseQ>");

          buff.append("<D>"
                  + Base64Helper.encode(removeMSZero(pvkKey.getPrivateExponent()
                          .toByteArray())) + "</D>");
          buff.append("</RSAKeyValue>");

          return buff.toString().replaceAll("[ \t\n\r]", "");
      } catch (Exception e) {
          System.err.println(e);
          return null;
      }
  }

  // --- Returns XML encoded RSA public key string suitable for .NET
  // CryptoServiceProvider.FromXmlString(true) ------
  // --- Leading zero bytes (most significant) must be removed for XML
  // encoding for .NET; otherwise format error ---

  private static String getRSAPublicKeyAsNetFormat(byte[] encodedPrivkey) {
      try {
          StringBuffer buff = new StringBuffer(1024);
          PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(
                  encodedPrivkey);
          KeyFactory keyFactory = KeyFactory.getInstance("RSA");
          RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory
                  .generatePrivate(pvkKeySpec);
          buff.append("<RSAKeyValue>");
          buff.append("<Modulus>"
                  + Base64Helper.encode(removeMSZero(pvkKey.getModulus().toByteArray()))
                  + "</Modulus>");
          buff.append("<Exponent>"
                  + Base64Helper.encode(removeMSZero(pvkKey.getPublicExponent()
                          .toByteArray())) + "</Exponent>");
          buff.append("</RSAKeyValue>");
          return buff.toString().replaceAll("[ \t\n\r]", "");
      } catch (Exception e) {
          System.err.println(e);
          return null;
      }
  }
//--------- remove leading (Most Significant) zero byte if present
  // ----------------
  private static byte[] removeMSZero(byte[] data) {
      byte[] data1;
      int len = data.length;
      if (data[0] == 0) {
          data1 = new byte[data.length - 1];
          System.arraycopy(data, 1, data1, 0, len - 1);
      } else
          data1 = data;
      return data1;
  }
  
  public static String encodeModulus(String key,String data) {
		if (data == null)
			return null;
		try {
			if(publicKey==null){
				byte[] modulusBytes = Base64Helper.decode(key);
				byte[] exponentBytes = Base64Helper.decode("AQAB");
				BigInteger modulus = new BigInteger(1, modulusBytes);
				BigInteger exponent = new BigInteger(1, exponentBytes);
				RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);
				KeyFactory fact = KeyFactory.getInstance("RSA");
				publicKey = fact.generatePublic(rsaPubKey);
			}
			if(publicKeyStr==null)
			publicKeyStr = getKeyString(publicKey);
			return Base64Helper.encode(encryptData(data.getBytes("UTF-8"),publicKey));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
  
  
  // 用公尺加密
  public static String encodeNet( String pubKeyXml,String data) {
      try {
    	  if(publicKey==null)
    		  publicKey = decodePublicKeyFromXml(pubKeyXml);
    	  if(publicKeyStr==null){
    		  publicKeyStr = getKeyString(publicKey) ;
    	  }
    	  return encode(publicKeyStr, data);
//     	  return Base64Helper.encode(encryptData(Base64Helper.decode(data), publicKey));
       } catch (Exception e) {
     	e.printStackTrace();
          return null;
      }
  }
  // 用私钥解密
	  public static String decodeNet(String data){
		  if (data == null)
				return null;
		   try{
			   
			   if(privateKey==null){
//				   String priKeyXml = ConfigContainer.getProperty("NET.RSA.PRIVATEKEY.XML");
				   String priKeyXml = null;
				   privateKey = decodePrivateKeyFromXml(priKeyXml);
			   }
			   if(privateKeyStr==null)
				   privateKeyStr = getKeyString(privateKey);
			  return decode(privateKeyStr,data);
		   }catch(Exception e){
			   //e.printStackTrace();
			   return null;
		   }
		    
	  }
	  
	  // 用私钥解密
	  public static String decode( String priKey,String data) {
	      try {
	    	  if(privateKey==null)
	     	  privateKey = getPrivateKey(priKey);
	    	  if(privateKeyStr==null){
	    		  privateKeyStr = getKeyString(privateKey) ;
	    	  }
	     	  return new String(decryptData(Base64Helper.decode(data), privateKey));
	       } catch (Exception e) {
	     	  //e.printStackTrace();
	          return null;
	      }
	  }
	  
	//用公钥加密
	  public static String encode(String pubKey,String data) {
		  if (data == null)
				return null;
	      try {
	    	  if(publicKey==null)
	    		  publicKey = getPublicKey(pubKey);
	    	  if(publicKeyStr==null){
	    		  publicKeyStr = getKeyString(publicKey) ;
	    	  }
	     	 return Base64Helper.encode(encryptData(data.getBytes(), publicKey));
	      } catch (Exception e) {
	    	  e.printStackTrace();
	          return null;
	      }
	  }
	  
	  
	  private static void writeKeyBytesToFile(byte[] key, String file)
	            throws Exception {
		  if(file==null){
			  return ;
		  }
	        OutputStream out = new FileOutputStream(file);
	        try {
	        	out.write(key);
			} catch (Exception e) {
				throw e;
			}finally{
				if(out!=null)
				 out.close();
			}
	        
	       
	    }
	  
	  
	  
  /**
	 * 得到密钥字符串（经过base64编码）
	 * @return
	 */
	private static String getKeyString(Key key) throws Exception {
		byte[] keyBytes = key.getEncoded();
		String s = (new BASE64Encoder()).encode(keyBytes);
		return s;
	}
	
	public static void main(String[] args) {
//		  RsaUtil.generateKeys(10);
//		  System.out.println(RsaUtil.publicKeyStr);
		  String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEsmliwpUOfTrKwhMtuDKQBZu7plHgPCQDo4cR";
		  publicKey +="aqpR3XSdc4qGPm7L1B/CM5FY8XILscnzmWqArRXPfogsVe8TvL65nExqJYmZ1v3AXOhMGOxoO8pB";
		  publicKey +="K8unHxcWEFM5AtVYABFw5jXdK2avZh2Ne2eU8N9oaq++t01RjUXLv5zP0wIDAQAB";
		  for(int i=0;i<100;i++){
			  Long start = System.currentTimeMillis();
		  String sb = RsaUtil.encode(publicKey, "baidu");
		  System.out.println(System.currentTimeMillis() - start);
		  System.out.println(sb);
		  }
//		  System.out.println("===========================");
		
//		  System.out.println("===========================");
		  
//		  System.out.println(RsaUtil.privateKeyStr);
		  System.out.println("===========================");
		  
		 
		  
		  
//		  System.out.println(RsaUtil.decode(privateKey, "ghRg5hS/j4rogprSshY4QPwGFcM9MPVGKlgHgHgl1h1K3eiwjlcUYPNP/JHDbHqzKk15XJaPwqY/WOy0SYsRnXXHeokGk8PLQwjPAKo9QEV+IdveykZgmPBhF553/vDgzKqRJjB4yej8ZmrtP4By3onfFdcF23g8L1pQX/G6mBE="));
//		  System.out.println(RsaUtil.decode(privateKey, "SNn87Hd0e13809BIdNlI0vam9snS0kwS8+k/BUBKvI3/8HSXXVkw2EGGjaH5a5bQS7K5Oj8el1+g/LuLIcLwaHh5TKJjc6SitzP/YSVd/iG/FIUagU7MccEZvXnk8MNqoj0lNqjaVJuiCkFrP5jMktTWbxbHUn9tVMYIJODTs20="));
//		  System.out.println(RsaUtil.decode(privateKey, "O5plQC+jh81wVPA4mtMsR1sql6BE7KOuQjgJ0oJCIc8EFSuA0LrT+Knh5oR63T6gSDtE/AbD0oYjv581wZlRBZyQVZb5KuonE+mi6X3mRFfCc7bqD/81Cyyv3Adj1SwB0qOmRzf7a99E8M4Ah3zMDTPmx4ROJp2bNSoMuSRqDBQ="));

//		  System.out.println(RsaUtil.decode(privateKey, sb));
	}
}


class Base64Helper {
    @SuppressWarnings("restriction")
	public static String encode(byte[] byteArray) {
        sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
        return base64Encoder.encode(byteArray);
    }

    @SuppressWarnings("restriction")
	public static byte[] decode(String base64EncodedString) {
        sun.misc.BASE64Decoder base64Decoder = new sun.misc.BASE64Decoder();
        try {
            return base64Decoder.decodeBuffer(base64EncodedString);
        } catch (IOException e) {
            return null;
        }
    }
  
} 
