package com.quickbase.cityservice;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.util.concurrent.ListenableFuture;

public interface CityService {
	/** Block until all data is available, then return the entire thing as a list. */
	default List<Pair<String, Integer>> getAllSync() throws ServiceError {
		ListenableFuture<List<Pair<String, Integer>>> result = getAllAsync();
		
		try {
			return result.get();
		} catch (InterruptedException | ExecutionException | CancellationException e) {
			throw new ServiceError(e);
		}
	}
	
	/** Run in the background until all data is available, then return the entire thing as a list. */
	ListenableFuture<List<Pair<String, Integer>>> getAllAsync();
}
