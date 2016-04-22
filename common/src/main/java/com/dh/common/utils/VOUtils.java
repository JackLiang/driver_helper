/**
 *  
 */
package com.dh.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

/**
 * VO、PO对象相互转换工具
 * @author ying_liu 2015年4月28日
 *
 */
public class VOUtils {

	/**
	 * 把 source 转换成指定对象 T，T必须有一个不带参数的构造函数。
	 * @param source - 源数据，不能为null
	 * @param clazz - 目标数据的class，不能为null
	 * @return T
	 * @throws RuntimeException 如果无法实例化 clazz，抛出异常
	 * @throws BeansException 如果转换失败，抛出异常
	 */
	public static <T> T from(Object source, Class<T> clazz) {
		T result = null;
		if (source != null && clazz != null) {
			try {
				result = clazz.newInstance();
				BeanUtils.copyProperties(source, result);
			} catch (InstantiationException e) {
				throw new RuntimeException("初始化" + clazz.getSimpleName() + "失败", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("初始化" + clazz.getSimpleName() + "失败", e);
			}
		}
		return result;
	}
	
	/**
	 * 把 source 列表转换成指定对象 T 的列表，T必须有一个不带参数的构造函数。
	 * @param list - 要转换的列表，不能为空
	 * @param clazz - 目标数据的class，不能为null
	 * @return T的列表
	 * @throws RuntimeException 如果无法实例化 clazz，抛出异常
	 * @throws BeansException 如果转换失败，抛出异常
	 */
	public static <T> List<T> fromList(Collection<? extends Object> list, Class<T> clazz) {
		List<T> result = new ArrayList<T>(list.size());
		for (Object source : list) {
			result.add(from(source, clazz));
		}
		return result;
		
	}


	/**
	 * 对object所有 java.lang.String 类型的属性执行 trim() 操作，<br/>
	 * 并调用 setXxxx() 方法设置 trim() 后的值。 <br/>
	 * 对 static 或 final 修饰的属性，不做任何操作。<br/>
	 * 注：单次调用大约 0.01 ~ 0.05ms，属性越多越慢。
	 * @param object - 要操作的对象，所有 String 类型的属性必须要有 get(), set() 方法
	 * @author linyi 2015/2/4
	 */
	public static void trim(final Object object) {
		ReflectionUtils.doWithFields(object.getClass(), new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				int modifiers = field.getModifiers();
				// 对 static 或 final 修饰的属性不操作
				if (!Modifier.isStatic(modifiers)
						&& !Modifier.isFinal(modifiers)
						&& field.getType().getCanonicalName().indexOf("java.lang.String") >= 0) {
					String getMethodName = getGetMethodName(field);
					Method getMethod = ReflectionUtils.findMethod(object.getClass(), getMethodName, null);
					if (getMethod != null) {
						String value = (String) ReflectionUtils.invokeMethod(getMethod, object);
						if (value != null) {
							value = value.trim();
							String setMethodName = getSetMethodName(field);
							Method setMethod = ReflectionUtils.findMethod(object.getClass(), setMethodName, new Class[] {field.getType()});
							if (setMethod != null) {
								ReflectionUtils.invokeMethod(setMethod, object, new Object[]{value});
							}
						}
					}


				} // end if ()
			} // end doWith()
		});
	}

	/**
	 * 返回 field 对应的 get 方法名称，格式是 getXxxx()。
	 * 注：这里不考虑 boolean 类型。
	 * @param field
	 * @return
	 */
	private static String getGetMethodName(Field field) {
		final String name = field.getName();
		String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
		return methodName;
	}

	/**
	 * 返回 field 对应的 set 方法名称，格式是 setXxxx()
	 * @param field
	 * @return
	 */
	private static String getSetMethodName(Field field) {
		final String name = field.getName();
		String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
		return methodName;
	}
}
