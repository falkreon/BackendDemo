package com.quickbase.cityservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.quickbase.devint.DBManager;
import com.quickbase.devint.DBManagerImpl;

public class DBCityService implements CityService {
	
	@Override
	public List<Pair<String, Integer>> getAllSync() throws ServiceError {
		return getInternal();
	}
	
	@Override
	public ListenableFuture<List<Pair<String, Integer>>> getAllAsync() {
		SettableFuture<List<Pair<String, Integer>>> future = SettableFuture.create();
		
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					future.set(getInternal());
				} catch (ServiceError ex) {
					future.setException(ex);
				}
			}
		};
		
		t.start();
		return future;
	}
	
	protected List<Pair<String, Integer>> getInternal() throws ServiceError {
		DBManager dbm = new DBManagerImpl();
		try {
			return dbm.getPopulationData();
		} catch (SQLException ex) {
			throw new ServiceError(ex);
		}
	}
}
