import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class WebAppServlet extends HttpServlet {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/webappdemo";
    private static final String USER = "root";
    private static final String PASS = "1234";

    // Load JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main entry point (GET for pages, POST for form submissions)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        if (action == null) action = "home";

        out.println("<html><head><title>Web Application Portal</title></head><body>");
        out.println("<h2>Web Application Using Servlets, JSP, and JDBC</h2>");
        out.println("<nav>");
        out.println("<a href='WebAppServlet?action=login'>Login</a> | ");
        out.println("<a href='WebAppServlet?action=employees'>Employees</a> | ");
        out.println("<a href='WebAppServlet?action=attendance'>Attendance</a>");
        out.println("</nav><hr>");

        switch (action) {
            case "login":
                showLoginForm(out);
                break;
            case "employees":
                showEmployeeSearchForm(out);
                showEmployees(request, out);
                break;
            case "attendance":
                showAttendanceForm(out);
                break;
            default:
                out.println("<p>Welcome! Use the navigation links above to explore features.</p>");
        }

        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");

        out.println("<html><body>");
        out.println("<h2>Processing Request...</h2>");

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            if ("login".equals(action)) {
                handleLogin(request, out, con);
            } else if ("attendance".equals(action)) {
                handleAttendance(request, out, con);
            }

        } catch (Exception e) {
            e.printStackTrace(out);
        }

        out.println("<hr><a href='WebAppServlet'>Back to Home</a>");
        out.println("</body></html>");
    }

    // ======= Login Feature =======
    private void showLoginForm(PrintWriter out) {
        out.println("<h3>User Login</h3>");
        out.println("<form action='WebAppServlet' method='post'>");
        out.println("<input type='hidden' name='action' value='login'>");
        out.println("Username: <input type='text' name='username' required><br><br>");
        out.println("Password: <input type='password' name='password' required><br><br>");
        out.println("<input type='submit' value='Login'>");
        out.println("</form>");
    }

    private void handleLogin(HttpServletRequest request, PrintWriter out, Connection con) throws SQLException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?");
        ps.setString(1, user);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            out.println("<h3>Welcome, " + user + "!</h3>");
        } else {
            out.println("<h3 style='color:red;'>Invalid username or password.</h3>");
        }
    }

    // ======= Employee Feature =======
    private void showEmployeeSearchForm(PrintWriter out) {
        out.println("<h3>Employee Records</h3>");
        out.println("<form action='WebAppServlet' method='get'>");
        out.println("<input type='hidden' name='action' value='employees'>");
        out.println("Search by Employee ID: <input type='text' name='empid'>");
        out.println("<input type='submit' value='Search'>");
        out.println("</form><br>");
    }

    private void showEmployees(HttpServletRequest request, PrintWriter out) {
        String empid = request.getParameter("empid");

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            PreparedStatement ps;
            if (empid != null && !empid.isEmpty()) {
                ps = con.prepareStatement("SELECT * FROM employee WHERE EmpID=?");
                ps.setInt(1, Integer.parseInt(empid));
            } else {
                ps = con.prepareStatement("SELECT * FROM employee");
            }

            ResultSet rs = ps.executeQuery();
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");
            boolean found = false;

            while (rs.next()) {
                found = true;
                out.println("<tr><td>" + rs.getInt("EmpID") + "</td><td>"
                        + rs.getString("Name") + "</td><td>"
                        + rs.getDouble("Salary") + "</td></tr>");
            }
            out.println("</table>");
            if (!found) out.println("<p>No records found.</p>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    // ======= Attendance Feature =======
    private void showAttendanceForm(PrintWriter out) {
        out.println("<h3>Record Student Attendance</h3>");
        out.println("<form action='WebAppServlet' method='post'>");
        out.println("<input type='hidden' name='action' value='attendance'>");
        out.println("Student ID: <input type='text' name='studentID' required><br><br>");
        out.println("Date: <input type='date' name='date' required><br><br>");
        out.println("Status: <select name='status'>");
        out.println("<option value='Present'>Present</option>");
        out.println("<option value='Absent'>Absent</option>");
        out.println("</select><br><br>");
        out.println("<input type='submit' value='Submit Attendance'>");
        out.println("</form>");
    }

    private void handleAttendance(HttpServletRequest request, PrintWriter out, Connection con) throws SQLException {
        String sid = request.getParameter("studentID");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        PreparedStatement ps = con.prepareStatement("INSERT INTO attendance VALUES (?, ?, ?)");
        ps.setInt(1, Integer.parseInt(sid));
        ps.setString(2, date);
        ps.setString(3, status);

        int rows = ps.executeUpdate();
        if (rows > 0)
            out.println("<h3>Attendance recorded successfully!</h3>");
        else
            out.println("<h3 style='color:red;'>Failed to record attendance.</h3>");
    }
}
