<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.webapp.beans.Student" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Attendance Portal</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }
        .form-section {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        select, input[type=date], input[type=text], textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
            box-sizing: border-box;
        }
        textarea {
            height: 80px;
            resize: vertical;
        }
        input[type=submit] {
            background-color: #4CAF50;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
        }
        input[type=submit]:hover {
            background-color: #45a049;
        }
        .nav-links {
            text-align: center;
            margin-top: 20px;
        }
        .nav-links a {
            margin: 0 10px;
            text-decoration: none;
            color: #4CAF50;
            padding: 10px 20px;
            background-color: #f0f0f0;
            border-radius: 5px;
        }
        .nav-links a:hover {
            background-color: #e0e0e0;
        }
        .success-message {
            background-color: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #c3e6cb;
        }
        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Student Attendance Portal</h2>
        
        <!-- Display success/error messages -->
        <% if (request.getAttribute("successMessage") != null) { %>
            <div class="success-message">
                <%= request.getAttribute("successMessage") %>
            </div>
        <% } %>
        
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>
        
        <div class="form-section">
            <h3>Mark Attendance</h3>
            <form method="post" action="attendance">
                <div class="form-group">
                    <label for="studentId">Select Student:</label>
                    <select id="studentId" name="studentId" required>
                        <option value="">-- Select a Student --</option>
                        <%
                            List<Student> students = (List<Student>) request.getAttribute("students");
                            if (students != null) {
                                for (Student student : students) {
                        %>
                            <option value="<%= student.getStudentId() %>">
                                <%= student.getStudentId() %> - <%= student.getName() %> (<%= student.getDepartment() %>)
                            </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="attendanceDate">Attendance Date:</label>
                    <input type="date" id="attendanceDate" name="attendanceDate" required>
                </div>
                
                <div class="form-group">
                    <label for="status">Attendance Status:</label>
                    <select id="status" name="status" required>
                        <option value="">-- Select Status --</option>
                        <option value="Present">Present</option>
                        <option value="Absent">Absent</option>
                        <option value="Late">Late</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="remarks">Remarks (Optional):</label>
                    <textarea id="remarks" name="remarks" placeholder="Enter any additional remarks..."></textarea>
                </div>
                
                <input type="submit" value="Submit Attendance">
            </form>
        </div>
        
        <div class="nav-links">
            <a href="login">User Login</a>
            <a href="employees">View Employees</a>
            <a href="attendance">Refresh Page</a>
        </div>
    </div>
    
    <script>
        // Set today's date as default
        document.getElementById('attendanceDate').valueAsDate = new Date();
    </script>
</body>
</html>
