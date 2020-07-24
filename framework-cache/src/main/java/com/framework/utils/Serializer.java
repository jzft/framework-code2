package com.framework.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;
/**
 * 序列化类
 *
 */
public class Serializer {
	protected static final Logger log =  Logger.getLogger(Serializer.class);
	/**
	 * 序列化
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static byte[] serialize(Object object) throws Exception{
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
	}
/**
 * 反序列话
 * @param bytes
 * @return
 * @throws Exception
 */
	public static Object unserialize(byte[] bytes) throws Exception {
		ByteArrayInputStream bais = null;
		if(bytes==null){
			return null;
		}
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
	}
}