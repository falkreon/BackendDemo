package com.quickbase.cityservice;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Represents a source of population data by country. Ideally, ConcreteStatService and DBManager in "backend" would just implement
 * this interface, but sometimes we don't have that luxury, so the concrete subclasses are wrapper classes for now.
 * 
 * <p>A more sustainable model might be to skip the futures entirely and switch to a visitor pattern / streaming model:
 * <pre><code>
 * void addConsumer(Consumer&lt;Pair&lt;String, Integer&gt;&gt; consumer);
 * void start();
 * void stop();
 * </code></pre>
 * 
 * <p>But for the given scope, this will provide much clearer unit tests.
 */
public interface PopulationService {
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
