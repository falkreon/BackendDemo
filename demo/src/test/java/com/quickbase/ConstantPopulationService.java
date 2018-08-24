package com.quickbase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.quickbase.cityservice.PopulationService;

/** Dummy population service to provide predictable data for testing */
public class ConstantPopulationService implements PopulationService {
	List<Pair<String, Integer>> data = new ArrayList<>();
	
	public ConstantPopulationService(Map<String, Integer> data) {
		this.data = data.entrySet()
				.stream()
				.map((entry)->Pair.of(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}
	
	@Override
	public ListenableFuture<List<Pair<String, Integer>>> getAllAsync() {
		return Futures.immediateFuture(data);
	}
}
