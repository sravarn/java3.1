package com.webapp.beans;

import java.sql.Date;

/**
 * Attendance bean class to represent attendance data
 */
public class Attendance {
    private int attendanceId;
    private int studentId;
    private Date attendanceDate;
    private String status;
    private String remarks;
    
    // Default constructor
    public Attendance() {}
    
    // Constructor with parameters
    public Attendance(int attendanceId, int studentId, Date attendanceDate, String status, String remarks) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.remarks = remarks;
    }
    
    // Constructor without attendanceId (for new records)
    public Attendance(int studentId, Date attendanceDate, String status, String remarks) {
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.remarks = remarks;
    }
    
    // Getters and Setters
    public int getAttendanceId() {
        return attendanceId;
    }
    
    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public Date getAttendanceDate() {
        return attendanceDate;
    }
    
    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId=" + attendanceId +
                ", studentId=" + studentId +
                ", attendanceDate=" + attendanceDate +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
