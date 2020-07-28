package com.framework.utils;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;

/**
 * Class帮助类
 * <p>类全名称转换为对象
 * <p>将bean对象属c性注入值
 * @author lyq
 */
public class ClassUtils {
	protected static Logger logger = Logger.getLogger(ClassUtils.class);
	
	private static final String STR_FINAL = " final ";
	/**
     * Attempts to create a class from a String.
     * @param className the name of the class to create.
     * @return the class.  CANNOT be NULL.
     * @throws IllegalArgumentException if the className does not exist.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(final String className) throws IllegalArgumentException {
        try {
            return (Class<T>) Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(className + " class not found.");
        }
    }


    /**
     * Creates a new instance of the given class by passing the given arguments
     * to the constructor.
     * @param className Name of class to be created.
     * @param args Constructor arguments.
     * @return New instance of given class.
     */
    public static <T> T newInstance(final String className, final Object ... args) {
        return newInstance(ClassUtils.<T>loadClass(className), args);
    }
    
    /**
     * Creates a new instance of the given class by passing the given arguments
     * to the constructor.
     * @param clazz Class of instance to be created.
     * @param args Constructor arguments.
     * @return New instance of given class.
     */
    public static <T> T newInstance(final Class<T> clazz, final Object ... args) {
        final Class<?>[] argClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argClasses[i] = args[i].getClass();
        }
        try {
            return clazz.getConstructor(argClasses).newInstance(args);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error creating new instance of " + clazz, e);
        }
    }

    /**
     * Gets the property descriptor for the named property on the given class.
     * @param clazz Class to which property belongs.
     * @param propertyName Name of property.
     * @return Property descriptor for given property or null if no property with given
     * name exists in given class.
     */
    public static PropertyDescriptor getPropertyDescriptor(final Class<?> clazz, final String propertyName) {
        try {
            return getPropertyDescriptor(Introspector.getBeanInfo(clazz), propertyName);
        } catch (final IntrospectionException e) {
            throw new RuntimeException("Failed getting bean info for " + clazz, e);
        }
    }

    /**
     * Gets the property descriptor for the named property from the bean info describing
     * a particular class to which property belongs.
     * @param info Bean info describing class to which property belongs.
     * @param propertyName Name of property.
     * @return Property descriptor for given property or null if no property with given
     * name exists.
     */
    public static PropertyDescriptor getPropertyDescriptor(final BeanInfo info, final String propertyName) {
        for (int i = 0; i < info.getPropertyDescriptors().length; i++) {
            final PropertyDescriptor pd = info.getPropertyDescriptors()[i];
            if (pd.getName().equals(propertyName)) {
                return pd;
            }
        }
        return null;
    }
    
//    public static Map<String,PropertyDescriptor>

    /**
     * Sets the given property on the target JavaBean using bean instrospection.
     * @param propertyName Property to set.
     * @param value Property value to set.
     * @param target Target java bean on which to set property.
     */
    public static void setProperty(final String propertyName, final Object value, final Object target) {
        try {
            setProperty(propertyName, value, target, Introspector.getBeanInfo(target.getClass()));
        } catch (final IntrospectionException e) {
            throw new RuntimeException("Failed getting bean info on target JavaBean " + target, e);
        }
    }

    /**
     * Sets the given property on the target JavaBean using bean instrospection.
     * @param propertyName Property to set.
     * @param value Property value to set.
     * @param target Target JavaBean on which to set property.
     * @param info BeanInfo describing the target JavaBean.
     */
    public static void setProperty(final String propertyName, final Object value, final Object target, final BeanInfo info) {
        try {
            final PropertyDescriptor pd = getPropertyDescriptor(info, propertyName);
            pd.getWriteMethod().invoke(target, value);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException("Error setting property " + propertyName, e.getCause());
        } catch (final Exception e) {
            throw new RuntimeException("Error setting property " + propertyName, e);
        }
    }
    
    public static Object getBeanPropertyValue(Object beanObject, String propertyName){
    	PropertyDescriptor pd = getPropertyDescriptor(beanObject.getClass(),propertyName);
    	try {
    		Method m = pd.getReadMethod();
			return m != null ? m.invoke(beanObject, new Object[]{}) : null;
		} catch (Exception e) {
			logger.error("bean实体中没有获取到属性"+propertyName+"值,"+e.getMessage());
		}
    	return null;
    }
    
    public static Object getBeanDeepProperty(Object beanObject, String propertyName){
    	if(StringUtils.isEmpty(propertyName)){
    		return null;
    	}
    	Object obj = beanObject;
    	String [] propertys = StringUtils.split(propertyName,".");
    	for(String property:propertys ){
    		obj = getBeanPropertyValue(obj, property);
    		if(obj==null){
    			return null;
    		}
    	}
    	return obj;
    }
    
    public static List<Field> getBeanAllFields(final Class<?> clazz){
    	List<Field> fs = new ArrayList<Field>();
    	Class<?> cl = clazz;
    	while(cl != null){
    		for(Field f : cl.getDeclaredFields()){
    			if(!f.getName().equalsIgnoreCase("serialVersionUID"))
    				fs.add(f);
    		}
    		cl = cl.getSuperclass();
    	}
    	return fs;
    }
    
    public static Map<String, PropertyDescriptor> getAllDescriptorMap(final Class<?> clazz) throws Exception{
    	Map<String,PropertyDescriptor> fs = new HashMap<String,PropertyDescriptor>();
    	BeanInfo info = Introspector.getBeanInfo(clazz);
    	 for (int i = 0; i < info.getPropertyDescriptors().length; i++) {
             final PropertyDescriptor pd = info.getPropertyDescriptors()[i];
             if(!"class".equals(pd.getName())){
            	 fs.put(pd.getName(), pd);
             }
         }

    	return fs;
    }
    /**
     * 对象内属性对应的属性值
     * @param bean
     * @return
     * @throws Exception
     */
	public static Map<String,Object> describe(Object bean) throws Exception{
		Map<String,Object> fs = new HashMap<String,Object>();
    	BeanInfo info = Introspector.getBeanInfo(bean.getClass());
    	 for (int i = 0; i < info.getPropertyDescriptors().length; i++) {
             final PropertyDescriptor pd = info.getPropertyDescriptors()[i];
             if(!"class".equals(pd.getName())){
            	 fs.put(pd.getName(), pd.getReadMethod().invoke(bean, new Object[]{}));
             }
         }
		return fs;
	}
    
    /**
     *  
     * 尽量确保对list里面的对象正确执行obj里面的方法mthod 
     * 每次方法执行错误，重新支持，如果错误尝试executeCount次
	 * @author lyq
     * @param list
     * @param obj
     * @param method
     * @param executeCount 错误执行后尝试次数
     * @param args method参数 （除了第一个为集合里面的对象，第二个以后自己补
     * 			   如list的对象为 a 那么method的第一个参数为a的类型，method(a.class,args)
     */
    public static void invokeMethods( List list, Object obj, Method method,int executeCount,Object...args){
    	int errorCount=0;
    	Object []params = new Object[args==null?1:args.length+1];
		int k=1;
		if(args!=null){
			for(Object param :args){
				params[k]=param;
				k++;
			}
		}
		for(int i=0;i<list.size();i++){
			Object row = list.get(i);
			params[0] = row;
			try{
				method.invoke(obj, params);
			}catch(Exception e){
				logger.error(e);
				if(errorCount>=executeCount){
					continue;
				}else{
					i--;
				}
				errorCount++;
			}
		}
    }
    
    
		
		/**
		 * 获取set里面的对象，是含有属性propertyName 值为 value的对象
		 * @param set
		 * @param propertyName
		 * @param value
		 * @return
		 * @throws Exception
		 */
		public static Object getExistValue(Collection set,String propertyName,String value) throws Exception{
			if(set==null){
				return null;
			}
			for(Object obj:set){
				String beanValue = BeanUtils.getProperty(obj, propertyName);
				if(StringUtils.equals(value, beanValue)){
					return obj;
				}
			}
			return null;
		}
		
		
		public static Map getKeyMapByList(List list,String[]keyFields) throws Exception{
			Map result = new HashMap();
			if(list==null||list.size()==0){
				return result;
			}
			for(Object obj:list){
					StringBuilder key = new StringBuilder();
					for(String keyField:keyFields){
						key.append( BeanUtils.getProperty(obj, keyField));
						key.append("_");
					}
					if(StringUtils.isNotBlank(key.toString())){
						result.put(key.substring(0, key.length()-1), obj);
					}
			}
			return result;
		}
		
		public static Map getKeyMapByList(List list,String keyField) throws Exception{
			String []keyFields = {keyField};
			return getKeyMapByList(list, keyFields);
		}
		
		public static Map getKeyMapByList(List list,Integer[] keyFields) throws Exception{
			Map result = new HashMap();
			if(list==null||list.size()==0){
				return result;
			}
			for(Object obj:list){
					 List cell = (List)obj;
					 StringBuilder key = new StringBuilder();
					 for(Integer keyField:keyFields){
							key.append( cell.get(keyField));
							key.append("_");
						}
					 if(StringUtils.isNotBlank(key.toString())){
							result.put(key.substring(0, key.length()-1), obj);
						}
			}
			return result;
		}
		public static Map getKeyMapByList(List list,Integer keyField) throws Exception{
			return getKeyMapByList(list, new Integer[]{keyField});
		}
		
		/**
		 * 将keyField 值为主键，分类放入集合里
		 * @param <T>
		 * @param list
		 * @param keyField
		 * @param val
		 * @return
		 * @throws Exception
		 */
		public static <T> Map<Object,List<T>> assortListByField(List<T> list,String keyField) throws Exception{
			Map<Object,List<T>> result = new HashMap<Object,List<T>>();
			if(list==null||list.size()==0){
				return result;
			}
			for(T obj:list){
					 Object objVal = BeanUtils.getProperty(obj, keyField);
						List <T>objList = null;
						if(result.get(objVal)==null){
							objList = new ArrayList<T>();
							result.put(objVal, objList);
						}else{
							objList = result.get(objVal);
						}
						objList.add(obj);
			}
			return result;
		}
		
		/**
		 * @author lyq
		 * @param dest
		 * @param orig
		 * @throws Exception
		 */
		@SuppressWarnings("rawtypes")
		public static void copyProperties(Object dest,Object orig) throws Exception{
			Class oclz = orig.getClass();
			Class dclz = dest.getClass();
			Map<String,PropertyDescriptor> omap = getAllDescriptorMap(oclz);
			Map<String,PropertyDescriptor> dmap = getAllDescriptorMap(dclz);
			Set <String>dset = dmap.keySet();
			for(String fieldName:dset){
				PropertyDescriptor ofield = omap.get(fieldName);
				if(ofield!=null){
					Method method = ofield.getReadMethod();
					Object value = null;
					if(method!=null){
						value = method.invoke(orig, new Object[]{});
					}else{
						System.out.println("no setMethod "+fieldName);
					}
					PropertyDescriptor dfield = dmap.get(fieldName);
					if(value!=null){
						Method wmethod = dfield.getWriteMethod();
						if(wmethod!=null){
							wmethod.invoke(dest, value);
						}else{
							System.out.println("no getMethod "+fieldName);
						}
					}
				}
			}
		}
		
		public static String getSplitStr(List beans,String propertyName,String separator){
			if(beans==null||beans.size()==0){
				return "";
			}
			StringBuffer buf = new StringBuffer();
			for(Object bean:beans){
				Object value = getBeanDeepProperty(bean, propertyName);
				if(value!=null){
					if(value instanceof String){
						value = "'"+value + "'";
					}
					buf.append(value).append(separator);
				}
			}
			return StringUtils.removeEnd(buf.toString(), separator);
		}
		
		/**
		 * 如果返回Undefined类，则表明target没有该属性，或没有超类。
		 * @param target
		 * @param filedname
		 * @return
		 * @throws IllegalAccessException 
		 */
		public static Object readSuperPrivateField(Object target,String filedname) {
			if( target == null){
				return new Undefined("ClassUtils.readSuperPrivateField：属性target  为空");
			}
			if( target.getClass().getSuperclass()  == Object.class){
				return new Undefined("ClassUtils.readSuperPrivateField：属性target"+target.getClass().getName()+"  没有超类");
			}
			Field field = FieldUtils.getDeclaredField(target.getClass().getSuperclass(), filedname,true);
			if (field == null) {
				return new Undefined("ClassUtils.readSuperPrivateField：属性target"+target.getClass().getName()+" 超类 没有属性："+ filedname);
	        }
			Object value;
			try {
				value = FieldUtils.readField(field, target, true);
			} catch (IllegalAccessException e) {
				return new Undefined(e.getMessage());
			}
			return value;
			
		}
		
		/**
		 * 如果返回Undefined类，则表明target没有该属性，或没有超类。
		 * @param target
		 * @param filedname
		 * @return
		 * @throws IllegalAccessException 
		 */
		public static Object writeSuperPrivateField(Object target,String filedname,Object value) {
			if( target == null){
				return new Undefined("ClassUtils.readSuperPrivateField：属性target  为空");
			}
			if( target.getClass().getSuperclass()  == Object.class){
				return new Undefined("ClassUtils.readSuperPrivateField：属性target "+target.getClass().getName()+"  没有超类");
			}
			Field field = FieldUtils.getDeclaredField(target.getClass().getSuperclass(), filedname,true);
			if (field == null) {
				return new Undefined("ClassUtils.readSuperPrivateField：属性target "+target.getClass().getName()+" 超类 没有属性："+ filedname);
	        }
			try {
				FieldUtils.writeField(field, target, value,true);
			} catch (IllegalAccessException e) {
				return new Undefined(e.getMessage());
			}
			return value;
			
		}
		
		public static Object writePrivateField(Object target,String filedname,Object value){
			if( target == null){
				return new Undefined("ClassUtils.readPrivateField：property target is null");
			}
			Field field = FieldUtils.getDeclaredField(target.getClass(), filedname,true);
			if (field == null) {
				return new Undefined("ClassUtils.readPrivateField：param:target"+target.getClass().getName()+" super class not have property："+ filedname);
	        }
			try {
				FieldUtils.writeField(field, target, value,true);
			} catch (IllegalAccessException e) {
				return new Undefined(e.getMessage());
			}
			return value;
		}
		
		public static Object readPrivateField(Object target,String filedname){
			if( target == null){
				return new Undefined("ClassUtils.readPrivateField：property target is null");
			}
			Field field = FieldUtils.getDeclaredField(target.getClass(), filedname,true);
			if (field == null) {
				return new Undefined("ClassUtils.readPrivateField：param:target"+target.getClass().getName()+" super class not have property："+ filedname);
	        }
			Object value;
			try {
				value = FieldUtils.readField(field, target, true);
			} catch (IllegalAccessException e) {
				return new Undefined(e.getMessage());
			}
			return value;
		}

		
		/**
		 * 如果返回Undefined类，则表明target没有该方法，或没有超类。
		 * @param target
		 * @param methodname
		 * @param args
		 * @return
		 * @throws SecurityException 
		 * @throws NoSuchMethodException 
		 * @throws InvocationTargetException 
		 * @throws IllegalArgumentException 
		 * @throws IllegalAccessException 
		 */
		public static Object invokeSuperPrivateMethod(Object target,String methodname,Object...args) {
			return invokeSuperPrivateMethod(target,methodname,null,args);
		}
		
		/**
		 * 如果返回Undefined类，则表明target没有该方法，或没有超类。
		 * @param target
		 * @param methodname
		 * @param args
		 * @return
		 * @throws SecurityException 
		 * @throws NoSuchMethodException 
		 * @throws InvocationTargetException 
		 * @throws IllegalArgumentException 
		 * @throws IllegalAccessException 
		 */
		public static Object invokeSuperPrivateMethod(Object target,String methodname,Class[] parameterTypes,Object...args) {
			if( target == null){
				return new Undefined("ClassUtils.invokeSuperPrivateMethod：属性target 为空");
			}
			Class superClass = target.getClass().getSuperclass();
			if(superClass==null) {
				return new Undefined("ClassUtils.invokeSuperPrivateMethod：属性target"+target.getClass().getName()+"  没有超类");
			}
			if(parameterTypes==null){
				parameterTypes =  new Class[0];
				if (args == null) {
					parameterTypes = new Class[0];
		        }  else {
		        	int arguments = args.length;
			            parameterTypes = new Class[arguments];
			        for (int i = 0; i < arguments; i++) {
			            parameterTypes[i] = args[i].getClass();
			        }
		        }
			}
	        
			Method method = null;
			try {
				Method[] methods = superClass.getDeclaredMethods();
				for(Method m:methods){
					if(!StringUtils.equals(methodname, m.getName())){
						continue;
					}
					if((parameterTypes==null&&m.getParameterTypes()!=null)
						||(parameterTypes!=null&&m.getParameterTypes()==null)){
						continue;
					}
					if(parameterTypes.length!=m.getParameterTypes().length){
						continue;
					}
					boolean equal = true;
					for(int i=0;i<m.getParameterTypes().length;i++){
						Class<?> type  = m.getParameterTypes()[i];
						Class argsClass = parameterTypes[i];
						if(argsClass.isAssignableFrom(Double.class)){
							argsClass = double.class;
						}if(argsClass.isAssignableFrom(Long.class)){
							argsClass = long.class;
						}else if(argsClass.isAssignableFrom(Integer.class)){
							argsClass = int.class;
						}
						if(!type.isAssignableFrom(argsClass)){
							equal = false;
							break;
						}
//						type.
//						if(args[i] instanceof type){
//							
//						}
					}
					if(equal){
						method = m;
					}
				} 
//				method = superClass.getDeclaredMethod(methodname,parameterTypes);
			} catch (Exception e) {
				e.printStackTrace();
				return new Undefined(e.getMessage());
			} 
			if (method == null) {
				return new Undefined("ClassUtils.invokeSuperPrivateMethod：属性target"+target.getClass().getName()+"  没有方法："+ methodname);
	        }
			method.setAccessible(true);
			try {
				return method.invoke(target, args);
			} catch (Exception e) {
				return new Undefined(e.getMessage());
			} 
		}
		
}




