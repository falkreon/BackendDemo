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
		
		//Clip off the trailing comma if there is one
		if (result.size()>2) {
			int lastEntryPosition = result.size()-2;
			String lastEntry = result.get(lastEntryPosition);
			lastEntry = lastEntry.substring(0, lastEntry.length()-1);
			result.set(lastEntryPosition, lastEntry);
		}
		
		return result;
	}
}
