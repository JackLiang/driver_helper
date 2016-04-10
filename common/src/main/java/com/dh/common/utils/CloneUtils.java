package com.dh.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 复制属性值
 * create by 2015-10-30 
 * @author jiayu_zheng
 *
 */
public class CloneUtils {

	/**
	 * 将o里面的属性值 ，set进去 目标clazz创建出来的对象
	 * 
	 * @param o 源对象
	 * @param clazz  目标对象
	 * @return 目标对象
	 * @throws Exception
	 */
	public static <T> T cloneByField(Object o,Class<T> clazz) throws Exception{
		
		Field[] fields = o.getClass().getDeclaredFields();
		T o2  =null;
		try{
			o2 = clazz.newInstance();

			for(Field f1:fields){
				try{
					f1.setAccessible(true);
					
					Field f2 = 	clazz.getDeclaredField(f1.getName());
					
					if(f2!=null){
						 f2.setAccessible(true);
						 f2.set(o2, f1.get(o));
					}
				}catch(Exception e){
					
				}
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
					
		return   o2;
	}
	
	
	/**
	 * 通过方法克隆对象
	 * @param o
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T cloneByMethod(Object o,Class<T> clazz) throws Exception{
		
		Method[] methos = clazz.getMethods();
		try{
			T o2  =clazz.newInstance();
			for(Method method :methos){
				String methodName = method.getName();
				if(methodName.startsWith("set")){
					String getName = "get"+firstCharToUpperCase(methodName.substring(3));
					Object value = findGetValue(o, getName);
					
					if(value!=null){
						set(method, o2, value);
					}
					
				}
			}
			return o2;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * 给set方法赋值
	 * @param m
	 * @param o
	 * @param value
	 */
	private static void set(Method m ,Object o ,Object value ){
		
		Class clazz = m.getParameterTypes()[0];
		String simpleName = clazz.getSimpleName();
		String valueStr = String.valueOf(value);
		
		
			try {
				
				if("int".equals(simpleName)||"Integer".equals(simpleName)){
					m.invoke(o, Integer.valueOf(valueStr));					
				}else if("boolean".equals(simpleName)||"Boolean".equals(simpleName)){
					m.invoke(o, Boolean.valueOf(valueStr));
				}else if("double".equals(simpleName)||"Double".equals(simpleName)){
					m.invoke(o, Double.valueOf(valueStr));
				}else if("float".equals(simpleName)||"Float".equals(simpleName)){
					m.invoke(o, Float.valueOf(valueStr));
				}else if("short".equals(simpleName)||"Short".equals(simpleName)){
					m.invoke(o,Short.valueOf(valueStr));
				}else if("Byte".equals(simpleName)||"byte".equals(simpleName)){
					m.invoke(o,Byte.valueOf(valueStr));
				}else if("Long".equals(simpleName)||"long".equals(simpleName)){
					m.invoke(o,Long.valueOf(valueStr));
				}else if("char".equals(simpleName)){
					m.invoke(o,(char)value);
				}else if("String".equals(simpleName)){
					m.invoke(o,valueStr);
				}else if("BigDecimal".equals(simpleName)){
					m.invoke(o,(BigDecimal)value);
				}else if("BigInteger".equals(simpleName)){
					m.invoke(o,(BigInteger)value);					
				}else if("Object".equals(simpleName)){
					m.invoke(o,value);
				}else if("Date".equals(simpleName)){
					m.invoke(o,(Date)value);
				}else if("List".equals(simpleName)){
					m.invoke(o,(List)value);
				}else if("Map".equals(simpleName)){
					m.invoke(o,(Map)value);
				}
				
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		
	}
	
	
	
	/**
	 * 找出get方法的值
	 * @param o
	 * @param methodName
	 * @return
	 */
	private static Object findGetValue(Object o,String methodName){
		try{
			Method m = o.getClass().getMethod(methodName, null);
			return m.invoke(o, null);
		}catch(Exception e){
			
			return null;
		}
		
	}
	
	/**
	 * 第一个字母小写
	 * @param str
	 * @return
	 */
	private static String firstCharToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * 第一个字母大写
	 * @param str
	 * @return
	 */
	private static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
}
