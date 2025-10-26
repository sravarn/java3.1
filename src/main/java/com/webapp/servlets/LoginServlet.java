package com.webapp.servlets;

import com.webapp.beans.User;
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

/**
 * Part A: Login Servlet
 * Handles user authentication through HTML form submission
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Display login form
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>User Login</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 50px; background-color: #f4f4f4; }");
        out.println(".container { max-width: 400px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
        out.println("h2 { text-align: center; color: #333; margin-bottom: 30px; }");
        out.println("form { display: flex; flex-direction: column; }");
        out.println("input[type=text], input[type=password] { padding: 12px; margin: 10px 0; border: 1px solid #ddd; border-radius: 5px; font-size: 16px; }");
        out.println("input[type=submit] { background-color: #4CAF50; color: white; padding: 12px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; margin-top: 10px; }");
        out.println("input[type=submit]:hover { background-color: #45a049; }");
        out.println(".nav-links { text-align: center; margin-top: 20px; }");
        out.println(".nav-links a { margin: 0 10px; text-decoration: none; color: #4CAF50; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h2>User Login</h2>");
        out.println("<form method='post' action='login'>");
        out.println("<input type='text' name='username' placeholder='Username' required>");
        out.println("<input type='password' name='password' placeholder='Password' required>");
        out.println("<input type='submit' value='Login'>");
        out.println("</form>");
        out.println("<div class='nav-links'>");
        out.println("<a href='employees'>View Employees</a> | ");
        out.println("<a href='attendance'>Attendance Portal</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Validate credentials
        User user = validateUser(username, password);
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Login Result</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 50px; background-color: #f4f4f4; }");
        out.println(".container { max-width: 500px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
        out.println("h2 { text-align: center; color: #333; margin-bottom: 30px; }");
        out.println(".success { color: #4CAF50; text-align: center; font-size: 18px; margin: 20px 0; }");
        out.println(".error { color: #f44336; text-align: center; font-size: 18px; margin: 20px 0; }");
        out.println(".nav-links { text-align: center; margin-top: 20px; }");
        out.println(".nav-links a { margin: 0 10px; text-decoration: none; color: #4CAF50; padding: 10px 20px; background-color: #f0f0f0; border-radius: 5px; }");
        out.println(".nav-links a:hover { background-color: #e0e0e0; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        
        if (user != null) {
            // Successful login
            out.println("<h2>Login Successful!</h2>");
            out.println("<div class='success'>");
            out.println("<p>Welcome, <strong>" + user.getFullName() + "</strong>!</p>");
            out.println("<p>Username: " + user.getUsername() + "</p>");
            out.println("<p>User ID: " + user.getUserId() + "</p>");
            out.println("</div>");
        } else {
            // Failed login
            out.println("<h2>Login Failed</h2>");
            out.println("<div class='error'>");
            out.println("<p>Invalid username or password. Please try again.</p>");
            out.println("</div>");
        }
        
        out.println("<div class='nav-links'>");
        out.println("<a href='login'>Try Again</a>");
        out.println("<a href='employees'>View Employees</a>");
        out.println("<a href='attendance'>Attendance Portal</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Validate user credentials against database
     * @param username Username to validate
     * @param password Password to validate
     * @return User object if valid, null if invalid
     */
    private User validateUser(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT UserID, Username, Password, FullName FROM Users WHERE Username = ? AND Password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return new User(
                    resultSet.getInt("UserID"),
                    resultSet.getString("Username"),
                    resultSet.getString("Password"),
                    resultSet.getString("FullName")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Database error during login validation: " + e.getMessage());
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
