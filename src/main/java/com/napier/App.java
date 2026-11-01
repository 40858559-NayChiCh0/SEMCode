
package com.napier;

import java.sql.*;
import java.util.ArrayList;

public class App {

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Main method.
     */
    public static void main(String[] args) {

        App a = new App();

        // Connect locally or using Docker arguments
        String location = args.length > 0
                ? args[0] : "localhost:33060";

        int delay = args.length > 1
                ? Integer.parseInt(args[1]) : 1000;

        a.connect(location, delay);

        // Check database connection
        if (a.con == null) {
            System.out.println("Database connection failed.");
            return;
        }

        // Get Development department
        Department dept = a.getDepartment("Development");

        // Get salaries for department
        ArrayList<Employee> employees =
                a.getSalariesByDepartment(dept);

        // Print salary report
        a.printSalaries(employees);

        // Disconnect from database
        a.disconnect();
    }

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            return;
        }

        int retries = 10;

        for (int i = 0; i < retries; i++) {

            System.out.println("Connecting to database...");

            try {
                Thread.sleep(delay);

                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location
                                + "/employees"
                                + "?allowPublicKeyRetrieval=true"
                                + "&useSSL=false",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");
                return;

            } catch (SQLException e) {
                System.out.println(
                        "Failed to connect to database attempt "
                                + (i + 1)
                );
                System.out.println(e.getMessage());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Connection interrupted");
                return;
            }
        }

        System.out.println("Could not connect to database.");
    }

    /**
     * Disconnect from database.
     */
    public void disconnect() {

        if (con != null) {
            try {
                con.close();
                con = null;
                System.out.println("Database connection closed");

            } catch (SQLException e) {
                System.out.println(
                        "Error closing database connection: "
                                + e.getMessage()
                );
            }
        }
    }

    /**
     * Get an employee by employee number.
     */
    public Employee getEmployee(int ID) {

        String sql =
                "SELECT emp_no, first_name, last_name "
                        + "FROM employees WHERE emp_no = ?";

        try (PreparedStatement stmt =
                     con.prepareStatement(sql)) {

            stmt.setInt(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Employee emp = new Employee();

                    emp.emp_no = rs.getInt("emp_no");
                    emp.first_name =
                            rs.getString("first_name");
                    emp.last_name =
                            rs.getString("last_name");

                    return emp;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(
                    "Failed to get employee details"
            );
        }

        return null;
    }

    /**
     * Display employee information.
     */
    public void displayEmployee(Employee emp) {

        if (emp == null) {
            System.out.println("Employee not found");
            return;
        }

        System.out.println(
                emp.emp_no + " "
                        + emp.first_name + " "
                        + emp.last_name + "\n"
                        + emp.title + "\n"
                        + "Salary: " + emp.salary + "\n"
                        + emp.dept_name + "\n"
                        + "Manager: " + emp.manager
        );
    }

    /**
     * Get department by department name.
     */
    public Department getDepartment(String departmentName) {

        String sql =
                "SELECT dept_no, dept_name "
                        + "FROM departments "
                        + "WHERE dept_name = ?";

        try (PreparedStatement stmt =
                     con.prepareStatement(sql)) {

            stmt.setString(1, departmentName);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Department dept = new Department();

                    dept.dept_no =
                            rs.getString("dept_no");

                    dept.dept_name =
                            rs.getString("dept_name");

                    return dept;
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Failed to get department: "
                            + e.getMessage()
            );
        }

        return null;
    }

    /**
     * Get current salaries for employees
     * in a selected department.
     */
    public ArrayList<Employee> getSalariesByDepartment(
            Department dept) {

        ArrayList<Employee> employees =
                new ArrayList<>();

        if (dept == null) {
            System.out.println("Department not found");
            return employees;
        }

        String sql =
                "SELECT e.emp_no, e.first_name, "
                        + "e.last_name, s.salary "
                        + "FROM employees e "
                        + "JOIN dept_emp de "
                        + "ON e.emp_no = de.emp_no "
                        + "JOIN salaries s "
                        + "ON e.emp_no = s.emp_no "
                        + "WHERE de.dept_no = ? "
                        + "AND de.to_date = '9999-01-01' "
                        + "AND s.to_date = '9999-01-01' "
                        + "ORDER BY e.emp_no ASC";

        try (PreparedStatement stmt =
                     con.prepareStatement(sql)) {

            stmt.setString(1, dept.dept_no);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Employee emp = new Employee();

                    emp.emp_no =
                            rs.getInt("emp_no");

                    emp.first_name =
                            rs.getString("first_name");

                    emp.last_name =
                            rs.getString("last_name");

                    emp.salary =
                            rs.getInt("salary");

                    emp.dept_name = dept.dept_name;

                    employees.add(emp);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Failed to get salaries: "
                            + e.getMessage()
            );
        }

        return employees;
    }

    /**
     * Get employees by job title.
     */
    public void getEmployeesByTitle(String title) {

        String sql =
                "SELECT e.emp_no, e.first_name, "
                        + "e.last_name, s.salary "
                        + "FROM employees e "
                        + "JOIN salaries s "
                        + "ON e.emp_no = s.emp_no "
                        + "JOIN titles t "
                        + "ON e.emp_no = t.emp_no "
                        + "WHERE s.to_date = '9999-01-01' "
                        + "AND t.to_date = '9999-01-01' "
                        + "AND t.title = ? "
                        + "ORDER BY e.emp_no ASC";

        try (PreparedStatement stmt =
                     con.prepareStatement(sql)) {

            stmt.setString(1, title);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    System.out.println(
                            rs.getInt("emp_no") + " "
                                    + rs.getString("first_name") + " "
                                    + rs.getString("last_name") + " "
                                    + rs.getInt("salary")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    /**
     * Print employee salary report.
     */
    public void printSalaries(
            ArrayList<Employee> employees) {

        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees");
            return;
        }

        System.out.println(
                String.format(
                        "%-10s %-15s %-20s %-8s",
                        "Emp No",
                        "First Name",
                        "Last Name",
                        "Salary"
                )
        );

        for (Employee emp : employees) {

            if (emp == null) {
                continue;
            }

            System.out.println(
                    String.format(
                            "%-10d %-15s %-20s %-8d",
                            emp.emp_no,
                            emp.first_name,
                            emp.last_name,
                            emp.salary
                    )
            );
        }
    }

    /**
     * Add a new employee.
     * Used for Lab 07 integration testing.
     */
    public void addEmployee(Employee emp) {

        if (emp == null) {
            System.out.println("Employee is null");
            return;
        }

        String sql =
                "INSERT INTO employees "
                        + "(emp_no, first_name, last_name, "
                        + "birth_date, gender, hire_date) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt =
                     con.prepareStatement(sql)) {

            stmt.setInt(1, emp.emp_no);
            stmt.setString(2, emp.first_name);
            stmt.setString(3, emp.last_name);
            stmt.setDate(4,
                    Date.valueOf("1990-01-01"));
            stmt.setString(5, "M");
            stmt.setDate(6,
                    Date.valueOf("2020-01-01"));

            stmt.executeUpdate();

            System.out.println(
                    "Employee added successfully"
            );

        } catch (SQLException e) {
            System.out.println(
                    "Failed to add employee: "
                            + e.getMessage()
            );
        }
    }
}
