package com.quickbase;

import java.sql.Connection;

import com.quickbase.devint.DBManager;
import com.quickbase.devint.DBManagerImpl;

// Impl note: There's a Main in backend; this one takes precedence

public class Main {
	public static void main(String... args) {
		System.out.println("Starting.");
        System.out.print("Getting DB Connection...");

        DBManager dbm = new DBManagerImpl();
        Connection c = dbm.getConnection();
        if (null == c ) {
            System.out.println("failed.");
            System.exit(1);
        }
	}
}
