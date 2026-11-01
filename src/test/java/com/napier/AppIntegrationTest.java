
package com.napier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest {

    static App app;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("localhost:33060", 1000);
    }

    @AfterAll
    static void cleanup() {
        app.disconnect();
    }

    // Test 1: Get an existing employee
    @Test
    void testGetEmployee() {
        Employee emp = app.getEmployee(255530);

        assertNotNull(emp);
        assertEquals(255530, emp.emp_no);
        assertEquals("Ronghao", emp.first_name);
        assertEquals("Garigliano", emp.last_name);
    }

    // Test 2: Get a non-existing employee
    @Test
    void testGetInvalidEmployee() {
        Employee emp = app.getEmployee(-1);

        assertNull(emp);
    }

    // Test 3: Get an existing department
    @Test
    void testGetDepartment() {
        Department dept = app.getDepartment("Development");

        assertNotNull(dept);
        assertEquals("Development", dept.dept_name);
        assertEquals("d005", dept.dept_no);
    }

    // Test 4: Get a non-existing department
    @Test
    void testGetInvalidDepartment() {
        Department dept = app.getDepartment("InvalidDepartment");

        assertNull(dept);
    }

    // Test 5: Get salaries by department
    @Test
    void testGetSalariesByDepartment() {
        Department dept = app.getDepartment("Development");

        assertNotNull(dept);

        ArrayList<Employee> employees =
                app.getSalariesByDepartment(dept);

        assertNotNull(employees);
        assertFalse(employees.isEmpty());

        for (Employee emp : employees) {
            assertNotNull(emp);
            assertTrue(emp.salary > 0);
        }
    }

    // Test 6: Get salaries with null department
    @Test
    void testNullDepartment() {
        ArrayList<Employee> employees =
                app.getSalariesByDepartment(null);

        assertNotNull(employees);
        assertTrue(employees.isEmpty());
    }
    @Test
    void testAddEmployee()
    {
        Employee emp = new Employee();
        emp.emp_no = 500000;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        app.addEmployee(emp);
        emp = app.getEmployee(500000);
        assertEquals(emp.emp_no, 500000);
        assertEquals(emp.first_name, "Kevin");
        assertEquals(emp.last_name, "Chalmers");
    }
}
