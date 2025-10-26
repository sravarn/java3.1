package com.webapp.servlets;

import com.webapp.beans.Attendance;
import com.webapp.beans.Student;
import com.webapp.utils.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Part C: Attendance Servlet
 * Handles student attendance portal using JSP and Servlet with database storage
 */
@WebServlet("/attendance")
public class AttendanceServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Load all students for the dropdown
        List<Student> students = getAllStudents();
        request.setAttribute("students", students);
        
        // Forward to JSP page
        request.getRequestDispatcher("/jsp/attendance.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get form parameters
        String studentIdStr = request.getParameter("studentId");
        String attendanceDateStr = request.getParameter("attendanceDate");
        String status = request.getParameter("status");
        String remarks = request.getParameter("remarks");
        
        // Validate input
        if (studentIdStr == null || studentIdStr.trim().isEmpty() ||
            attendanceDateStr == null || attendanceDateStr.trim().isEmpty() ||
            status == null || status.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "Please fill in all required fields.");
            doGet(request, response);
            return;
        }
        
        try {
            int studentId = Integer.parseInt(studentIdStr);
            Date attendanceDate = Date.valueOf(attendanceDateStr);
            
            // Create attendance object
            Attendance attendance = new Attendance(studentId, attendanceDate, status, remarks);
            
            // Save to database
            boolean success = saveAttendance(attendance);
            
            if (success) {
                request.setAttribute("successMessage", 
                    "Attendance recorded successfully for Student ID: " + studentId + 
                    " on " + attendanceDate + " with status: " + status);
            } else {
                request.setAttribute("errorMessage", 
                    "Failed to record attendance. Please try again.");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Student ID format.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Invalid date format.");
        }
        
        // Reload the form
        doGet(request, response);
    }
    
    /**
     * Get all students from database
     * @return List of Student objects
     */
    private List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT StudentID, Name, Department, Email FROM Student ORDER BY StudentID";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Student student = new Student(
                    resultSet.getInt("StudentID"),
                    resultSet.getString("Name"),
                    resultSet.getString("Department"),
                    resultSet.getString("Email")
                );
                students.add(student);
            }
            
        } catch (SQLException e) {
            System.err.println("Database error while fetching students: " + e.getMessage());
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
        
        return students;
    }
    
    /**
     * Save attendance record to database
     * @param attendance Attendance object to save
     * @return true if successful, false otherwise
     */
    private boolean saveAttendance(Attendance attendance) {
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Attendance (StudentID, AttendanceDate, Status, Remarks) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, attendance.getStudentId());
            statement.setDate(2, attendance.getAttendanceDate());
            statement.setString(3, attendance.getStatus());
            statement.setString(4, attendance.getRemarks());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Database error while saving attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (statement != null) statement.close();
                DatabaseConnection.closeConnection(connection);
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
    }
}
