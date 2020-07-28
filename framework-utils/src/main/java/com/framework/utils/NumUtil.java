package com.framework.utils;



/**
 * 
 * @author lyqs
 *
 */
public class NumUtil {
	/*public static Boolean isZero(Integer val){
		return val==null||val==0;
	}
	
	public static Boolean isZero(BigDecimal val){
		return val==null||val.intValue()==0;
	}
	
	public static Boolean isZero(Double val){
		return val==null||val.intValue()==0;
	}
	
	public static Boolean isZero(Float val){
		return val==null||val.intValue()==0;
	}*/
	
	public static boolean isZero(Number val){
		return val==null||val.intValue()==0;
	}
	
	public static double doubleValue(Number val){
		Double r = null;
		if(val==null){
			return 0D;
		}
		r = val.doubleValue();
		return r;
	}
	public static double doubleValue(String val){
		if(val==null||"".equals(val)){
			return 0D;
		}else{
			return Double.parseDouble(val);
		}
	}
	
	public static int intValue(Number val){
		return val==null?0:val.intValue();
	}
	
	public static int intValue(Object val){
		if(val==null){
			return 0;
		}
		if(val instanceof Number){
			return intValue((Number)val);
		}
		if(val instanceof String){
			return intValue((String)val);
		}
		return 0;
	}
	
	public static int intValue(String val){
		if(val==null||"".equals(val)){
			return 0;
		}else{
			return Integer.parseInt(val);
		}
	}

	
	public static boolean equals(Number num1,Number num2){
		if(num1==null&&num2==null){
			return true;
		}
		if(num1==null||num2==null){
			return false;
		}
		return num1.doubleValue() == num2.doubleValue();
	}
}
