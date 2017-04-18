package adiractech.ams.tables;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.orm.SugarRecord;

import java.util.List;

import adiractech.ams.utils.Constants;

/**
 * Created by root on 13/04/17.
 */

public class Employee  extends SugarRecord {
    private int employeeId;
    private String firstName;
    private String middleName;
    private String lastName;
    private int managerId;
    private int franchiseId;
    private int roleId;
    private String secret;



    public Employee(){

    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(int franchiseId) {
        this.franchiseId = franchiseId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Attendance getAttendanceRecord(){
        List<Attendance> records = Attendance.find(Attendance.class,"employee_id = ? and status = ?",
                                                Integer.toString(employeeId),
                                                Integer.toString(Constants.ATTENDANCE_ACTION_ENTER));

        if(records.isEmpty()) {
            return new Attendance();
        }else{
            return records.get(0);
        }
    }


}
