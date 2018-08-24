package com.quickbase;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.quickbase.cityservice.APIPopulationService;
import com.quickbase.cityservice.DBPopulationService;
import com.quickbase.cityservice.PopulationService;
import com.quickbase.cityservice.ServiceError;

public class DBAggregationTests {
	
	private PopulationService a;
	private PopulationService b;
	
	@Before
	public void setup() {
		a = new ConstantPopulationService(ImmutableMap.of("peru", 1024, "france", 768));
		b = new ConstantPopulationService(ImmutableMap.of("peru", 640, "spain", 480));
	}
	
	
	/** Should produce an empty result if given no inputs */
	@Test
	public void handleNoData() throws ServiceError {
		HashMap<String, Integer> subject = Main.combineData();
		Assert.assertTrue(subject.isEmpty());
	}
	
	
	/** Should acquire and comprehend database data */
	@Test
	public void acquireAnyDBData() throws ServiceError {
		HashMap<String, Integer> subject = Main.combineData(new DBPopulationService());
		Assert.assertTrue(subject.size()>0); //The main failure case here is that there is either no data or an exception was thrown.
	}
	
	/** Should acquire and comprehend data from the API endpoint */
	@Test
	public void acquireAnyAPIData() throws ServiceError {
		HashMap<String, Integer> subject = Main.combineData(new APIPopulationService());
		Assert.assertTrue(subject.size()>0);
	}
	
	/** Should turn non-canonical or subsidiary country names into canonical ones */
	@Test
	public void canonicalizeCountries() throws ServiceError {
		PopulationService nonCanonicalCountries = new ConstantPopulationService(ImmutableMap.of("u.s.a.", 1, "new caledonia (france)", 2, "aruba (netherlands)", 3));

		HashMap<String, Integer> subject = Main.combineData(nonCanonicalCountries);
		
		
		Assert.assertEquals(
				ImmutableMap.of(
					"united states of america", 1,
					"france",                   2,
					"netherlands",              3
					),
				subject
			);
	}
	
	
	/** Should combine and sum duplicate canonical and non-canonical entries from the same source */
	@Test
	public void sumSameCountryFromSameSource() throws ServiceError {
		PopulationService duplicateCountries = new ConstantPopulationService(ImmutableMap.of("new caledonia (france)", 1, "france", 2, "mayotte (france)", 3));

		HashMap<String, Integer> subject = Main.combineData(duplicateCountries);
		
		
		Assert.assertEquals(
				ImmutableMap.of(
					"france", 6
				),
				subject);
	}
	
	
	/** Should favor data from the sql database if there are conflicting entries" */
	@Test
	public void mergeWithPrecedence() throws ServiceError {
		HashMap<String, Integer> subject = Main.combineData(a, b);
		
		Assert.assertEquals(
				ImmutableMap.of(
						"peru", 1024,
						"france", 768,
						"spain", 480),
				subject
			);
	}
}
