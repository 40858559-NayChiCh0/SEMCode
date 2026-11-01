
package com.napier;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest {

    static App app;
    static Connection connection;

    @BeforeAll
    public static void init() throws SQLException {

        app = new App();

        connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:33060/employees"
                        + "?connectTimeout=10000",
                "root",
                "example"
        );

        assertNotNull(connection);
        assertFalse(connection.isClosed());

        app.con = connection;

        System.out.println(
                "Database connection established."
        );
    }

    @AfterAll
    public static void cleanup() throws SQLException {

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }

        System.out.println(
                "Database connection closed."
        );
    }

    @Test
    public void testGetEmployee() {

        Employee emp = app.getEmployee(10001);

        assertNotNull(emp);
        assertEquals(10001, emp.emp_no);
    }

    @Test
    public void testGetInvalidEmployee() {

        Employee emp = app.getEmployee(-1);

        assertNull(emp);
    }

    @Test
    public void testGetDepartment() {

        Department dept = app.getDepartment("Marketing");

        assertNotNull(dept);
        assertEquals("d001", dept.dept_no);
        assertEquals("Marketing", dept.dept_name);
    }

    @Test
    public void testGetInvalidDepartment() {

        Department dept = app.getDepartment("invalid");

        assertNull(dept);
    }
}
