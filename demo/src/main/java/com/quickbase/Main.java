package com.quickbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.quickbase.cityservice.APICityService;
import com.quickbase.cityservice.CityService;
import com.quickbase.cityservice.DBCityService;
import com.quickbase.cityservice.ServiceError;

// Impl note: There's a Main in backend; this one takes precedence

public class Main {
	public static void main(String... args) {
		try {
			HashMap<String, Integer> populations = combineData(new DBCityService(), new APICityService());
			List<String> flat = flattenToJson(populations);
			for(String s : flat) {
				System.out.println(s);
			}
		} catch (ServiceError e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Regularize a city name. Could convert to IATA code later, but for now we'll just clean up and combine obvious variants
	 */
	public static String canonicalize(String cityName) {
		String clean = cityName.trim().toLowerCase(Locale.ROOT);
		
		switch(clean) {
		case "u.s.a.":
		case "u.s. virgin islands (us)":
			return "united states of america";
			
		case "new caledonia (france)":
		case "mayotte (france)":
			return "france";
		
		case "aruba (netherlands)":
			return "netherlands";
			
			
		default:
			return clean;
		}
	}
	
	
	
	public static HashMap<String, Integer> combineData(CityService... services) throws ServiceError {
		HashMap<String, Integer> result = new HashMap<>();
		for(int i=services.length-1; i>=0; i--) {
			CityService service = services[i];
			
			for(Pair<String, Integer> pair : service.getAllSync()) {
				result.put(canonicalize(pair.getKey()), pair.getValue());
			}
		}
		
		return result;
	}
	
	public static List<String> flattenToJson(HashMap<String, Integer> map) {
		ArrayList<String> result = new ArrayList<>();
		result.add("{");
		for(Map.Entry<String, Integer> entry : map.entrySet()) {
			result.add("  \""+entry.getKey()+"\": "+entry.getValue()+",");
		}
		result.add("}");
		
		return result;
	}
}
