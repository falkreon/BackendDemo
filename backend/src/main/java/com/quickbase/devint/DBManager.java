package com.quickbase.devint;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by ckeswani on 9/16/15.
 */
public interface DBManager {
    public Connection getConnection();
    public List<Pair<String, Integer>> getPopulationData() throws SQLException;
}
