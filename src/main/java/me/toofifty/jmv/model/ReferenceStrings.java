package me.toofifty.jmv.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

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
	private HashMap<String, String> references = new HashMap<>();
	
	/**
	 * Get reference from string
	 * 
	 * @param var
	 * @return referred texture string
	 */
	public String getString(String var) {
		return references.get(var);
	}
	
	/**
	 * Add reference to map
	 * 
	 * @param var new var
	 * @param var2 #var
	 */
	public void add(String var, String var2) {
		references.put(var, var2);
	}
	
	public HashMap<String, String> getReferenceMap() {
		return references;
	}
	
	public void printStrings() {
		Iterator stringIter = references.entrySet().iterator();
		while (stringIter.hasNext()) {
			final Entry e = (Entry) stringIter.next();
			final String field = e.getKey().toString();
			final String value = e.getValue().toString();
			System.out.println(field + " mapped to " + value);
		}
	}

}
