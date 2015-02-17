package me.toofifty.jmv.model;

import java.util.HashMap;

/**
 * Provide a conversion for parent- reference variables
 * i.e. #side
 * to other reference variables.
 * 
 * @author Toofifty
 *
 */
public class ReferenceStrings {
	
	/**
	 * Map of String -> String
	 */
	private static HashMap<String, String> references = new HashMap<>();
	
	/**
	 * Get reference from string
	 * 
	 * @param var
	 * @return referred texture string
	 */
	public static String getString(String var) {
		return references.get(var);
	}
	
	/**
	 * Add reference to map
	 * 
	 * @param var new var
	 * @param var2 #var
	 */
	public static void add(String var, String var2) {
		references.put(var, var2);
	}

}
