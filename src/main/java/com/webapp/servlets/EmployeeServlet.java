package com.webapp.servlets;

import com.webapp.beans.Employee;
import com.webapp.utils.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Part B: Employee Servlet
 * Displays employee records with JDBC integration and search functionality
 */
@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String searchId = request.getParameter("searchId");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Employee Records</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }");
        out.println(".container { max-width: 1000px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
        out.println("h2 { text-align: center; color: #333; margin-bottom: 30px; }");
        out.println(".search-form { background-color: #f8f9fa; padding: 20px; border-radius: 5px; margin-bottom: 30px; }");
        out.println(".search-form input[type=number] { padding: 10px; margin: 10px 0; border: 1px solid #ddd; border-radius: 5px; font-size: 16px; width: 200px; }");
        out.println(".search-form input[type=submit] { background-color: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; margin-left: 10px; }");
        out.println(".search-form input[type=submit]:hover { background-color: #0056b3; }");
        out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        out.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
        out.println("tr:hover { background-color: #f5f5f5; }");
        out.println(".nav-links { text-align: center; margin-top: 20px; }");
        out.println(".nav-links a { margin: 0 10px; text-decoration: none; color: #4CAF50; padding: 10px 20px; background-color: #f0f0f0; border-radius: 5px; }");
        out.println(".nav-links a:hover { background-color: #e0e0e0; }");
        out.println(".no-results { text-align: center; color: #666; font-style: italic; margin: 20px 0; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h2>Employee Records Management</h2>");
        
        // Search form
        out.println("<div class='search-form'>");
        out.println("<form method='get' action='employees'>");
        out.println("<label for='searchId'>Search Employee by ID:</label><br>");
        out.println("<input type='number' id='searchId' name='searchId' placeholder='Enter Employee ID'>");
        out.println("<input type='submit' value='Search'>");
        out.println("</form>");
        out.println("</div>");
        
        if (searchId != null && !searchId.trim().isEmpty()) {
            // Search for specific employee
            Employee employee = getEmployeeById(Integer.parseInt(searchId));
            if (employee != null) {
                out.println("<h3>Search Result:</h3>");
                out.println("<table>");
                out.println("<tr><th>Employee ID</th><th>Name</th><th>Salary</th></tr>");
                out.println("<tr>");
                out.println("<td>" + employee.getEmpId() + "</td>");
                out.println("<td>" + employee.getName() + "</td>");
                out.println("<td>$" + String.format("%.2f", employee.getSalary()) + "</td>");
                out.println("</tr>");
                out.println("</table>");
            } else {
                out.println("<div class='no-results'>No employee found with ID: " + searchId + "</div>");
            }
        } else {
            // Display all employees
            List<Employee> employees = getAllEmployees();
            if (!employees.isEmpty()) {
                out.println("<h3>All Employee Records:</h3>");
                out.println("<table>");
                out.println("<tr><th>Employee ID</th><th>Name</th><th>Salary</th></tr>");
                
                for (Employee emp : employees) {
                    out.println("<tr>");
                    out.println("<td>" + emp.getEmpId() + "</td>");
                    out.println("<td>" + emp.getName() + "</td>");
                    out.println("<td>$" + String.format("%.2f", emp.getSalary()) + "</td>");
                    out.println("</tr>");
                }
                
                out.println("</table>");
            } else {
                out.println("<div class='no-results'>No employee records found.</div>");
            }
        }
        
        out.println("<div class='nav-links'>");
        out.println("<a href='login'>User Login</a>");
        out.println("<a href='attendance'>Attendance Portal</a>");
        out.println("<a href='employees'>View All Employees</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Get all employees from database
     * @return List of Employee objects
     */
    private List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT EmpID, Name, Salary FROM Employee ORDER BY EmpID";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Employee employee = new Employee(
                    resultSet.getInt("EmpID"),
                    resultSet.getString("Name"),
                    resultSet.getDouble("Salary")
                );
                employees.add(employee);
            }
            
        } catch (SQLException e) {
            System.err.println("Database error while fetching all employees: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                DatabaseConnection.closeConnection(connection);
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        
        return employees;
    }
    
    /**
     * Get employee by ID from database
     * @param empId Employee ID to search for
     * @return Employee object if found, null otherwise
     */
    private Employee getEmployeeById(int empId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT EmpID, Name, Salary FROM Employee WHERE EmpID = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, empId);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return new Employee(
                    resultSet.getInt("EmpID"),
                    resultSet.getString("Name"),
                    resultSet.getDouble("Salary")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Database error while fetching employee by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                DatabaseConnection.closeConnection(connection);
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        
        return null;
    }
}
