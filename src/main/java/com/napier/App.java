package com.napier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    private Connection con = null;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        String host = System.getenv("DB_HOST");
        if (host == null) {
            host = "db";
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                Thread.sleep(5000);
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/world?useSSL=false", "root", "password");
                System.out.println("Successfully Connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted?");
            }
        }
    }

    public static void main(String[] args) {
        App a = new App();
        a.connect();
    }
}