package com.quickbase.cityservice;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.quickbase.devint.ConcreteStatService;

public class APIPopulationService implements PopulationService {

	@Override
	public ListenableFuture<List<Pair<String, Integer>>> getAllAsync() {
		ConcreteStatService stats = new ConcreteStatService();
		return Futures.immediateFuture(stats.GetCountryPopulations());
	}

}
