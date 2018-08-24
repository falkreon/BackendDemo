package com.quickbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.quickbase.cityservice.APIPopulationService;
import com.quickbase.cityservice.PopulationService;
import com.quickbase.cityservice.DBPopulationService;
import com.quickbase.cityservice.ServiceError;

// Impl note: There's a Main in backend; this one takes precedence

/*
 * The original exercise seemed intended to be completed inside one project, but several pieces of the original project
 * wind up being proprietary and unchangeable in practice, so I decided to split the project and treat "backend" as unchangeable
 * except where changes were explicitly required by the exercise.
 * 
 * The artifact to be run is "BackendDemo-1.0-all.jar", which has backend and all other dependencies shaded. It will still
 * expect the resources folder to be available, but both the unit tests and the program should be resilient against changes in
 * the data or nonbreaking changes to the backend project.
 */



public class Main {
	public static void main(String... args) {
		try {
			HashMap<String, Integer> populations = combineData(new DBPopulationService(), new APIPopulationService());
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
	
	public static HashMap<String, Integer> combineData(PopulationService... services) throws ServiceError {
		HashMap<String, Integer> result = new HashMap<>();
		for(int i=services.length-1; i>=0; i--) {
			PopulationService service = services[i];
			
			HashMap<String, Integer> thisServiceData = new HashMap<>();
			
			for(Pair<String, Integer> pair : service.getAllSync()) {
				
				Integer existing = thisServiceData.get(canonicalize(pair.getKey()));
				if (existing==null) existing = 0;
				
				thisServiceData.put(canonicalize(pair.getKey()), existing + pair.getValue());
			}
			
			result.putAll(thisServiceData);
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
