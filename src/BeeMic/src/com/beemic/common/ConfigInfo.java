package com.beemic.common;

import java.util.HashMap;
import java.util.Map;

public class ConfigInfo {

	private static Map<Object,Object> contains;
	
	static {
		 contains = new HashMap<Object,Object>();
	}
	
	private ConfigInfo() {
	}
	
	public static void put(Object key,Object value) {
		contains.put(key, value);
	}
	
	public static Object get(Object key) {
		return contains.get(key);
	} 
}
