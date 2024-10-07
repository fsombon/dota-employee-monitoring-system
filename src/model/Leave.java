package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Leave {

    SQLRun objSQLRun = new SQLRun();
    public Employee objEmployee = new Employee();

    private double vl = 0.0;
    private double sl = 0.0;
    private double totalleave = 0.0;

    public double getVacationleave() {
        return vl;
    }

    public void setVacationleave(double vl) {
        this.vl = vl;
    }

    public double getSickleave() {
        return sl;
    }

    public void setSickleave(double sl) {
        this.sl = sl;
    }
    

    

    public boolean getLeaveDetails(String empId) {
        try {
            String sql = "SELECT * FROM employee e WHERE e.empId ='" + empId + "'";
            ResultSet resSet = objSQLRun.sqlQuery(sql);

            if (resSet.next()) {
                objEmployee.setEmpId(empId);
                objEmployee.setFname(resSet.getString("fname"));
                objEmployee.setLname(resSet.getString("lname"));
                objEmployee.setDesignation(resSet.getString("designation"));
                objEmployee.setDepartment(resSet.getString("department"));
                this.vl = resSet.getDouble("vacation_leave");
                this.sl = resSet.getDouble("sick_leave");
                
                

                return true;

            } else {
                JOptionPane.showMessageDialog(null, "No Record Found for Employee ID : " + empId, "ERROR", 0);
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Failed to Retrieve Data! Please Contact Your System Administrator!\n\n" + ex.getMessage(), "ERROR", 0);
            return false;
        }
    }
    public boolean getLeaveDetailsvianame(String empfname, String emplname) {
        try {
            String sql = "SELECT * FROM employee WHERE fname ='" + empfname + "' AND lname = '" + emplname + "' ";
            ResultSet resSet = objSQLRun.sqlQuery(sql);

            if (resSet.next()) {
                objEmployee.setEmpId(resSet.getString("empId"));
                objEmployee.setFname(empfname);
                objEmployee.setLname(emplname);
                objEmployee.setDesignation(resSet.getString("designation"));
                objEmployee.setDepartment(resSet.getString("department"));
                this.vl = resSet.getDouble("vacation_leave");
                this.sl = resSet.getDouble("sick_leave");
                
                

                return true;

            } else {
                JOptionPane.showMessageDialog(null, "No Record Found for Employee ID : " + empfname + " " + emplname, "ERROR", 0);
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Failed to Retrieve Data! Please Contact Your System Administrator!\n\n" + ex.getMessage(), "ERROR", 0);
            return false;
        }
    }
    
    public boolean applyLeave() {
        try {

            String sqlUpdate = "UPDATE employee SET vacation_leave=" + vl + ",sick_leave=" + sl +
                    " WHERE empId='" + objEmployee.getEmpId() + "'";

            int updated = objSQLRun.sqlUpdate(sqlUpdate);

            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Your Leave has been Recorded", "", 1);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error in Applying Leave", "ERROR", 0);
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error! Please Contact Your System Administrator!\n\n" + ex.getMessage(), "ERROR", 0);
            return false;
        }
    }
    public boolean resetLeave() {
    try {
            String sqlResetleave = "UPDATE employee SET vacation_leave=" + vl + ",sick_leave=" + sl ;
            
            int updated = objSQLRun.sqlUpdate(sqlResetleave);
            
            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Leave section has been reset", "", 1);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error in Applying Leave", "ERROR", 0);
                return false;
        }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error! Please Contact Your System Administrator!\n\n" + ex.getMessage(), "ERROR", 0);
                return false;
        }
    }

}
