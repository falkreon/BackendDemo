package com.quickbase;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.quickbase.cityservice.APICityService;
import com.quickbase.cityservice.DBCityService;
import com.quickbase.cityservice.ServiceError;

public class DBAggregationTests {
	
	/** Should produce an empty result if given no inputs */
	@Test
	public void handleNoData() throws ServiceError {
		HashMap<String, Integer> subject = Main.combineData();
		Assert.assertTrue(subject.isEmpty());
	}
	
	
	//TODO: Mock services and test specific behavior such as combining duplicate entries.+
	
	
	
	/** Should acquire and comprehend database data */
	@Test
	public void acquireDBData() throws ServiceError {
		HashMap<String, Integer> subject = Main.combineData(new DBCityService());
		Assert.assertTrue(subject.size()>0); //The main failure case here is that there is either no data or an exception was thrown.
	}
	
	/** Should acquire and comprehend data from the API endpoint */
	@Test
	public void acquireAPIData() throws ServiceError {
		HashMap<String, Integer> subject = Main.combineData(new APICityService());
		Assert.assertTrue(subject.size()>0);
	}
	
	/** Should favor data from the sql database if there are conflicting entries */
	@Test
	public void mergeWithPrecedence() throws ServiceError {
		HashMap<String, Integer> subject = Main.combineData(new DBCityService(), new APICityService());
		//TODO: Needs mocked services
	}
}
