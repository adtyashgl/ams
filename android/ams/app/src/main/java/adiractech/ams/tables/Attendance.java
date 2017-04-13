package adiractech.ams.tables;

import com.orm.SugarRecord;

/**
 * Created by root on 13/04/17.
 */

public class Attendance  extends SugarRecord {
    private int employeeId;
    private long inTime;
    private String inTimeImagePath;
    private long outTime;
    private String outTimeImagePath;

    public Attendance(){

    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public long getInTime() {
        return inTime;
    }

    public void setInTime(long inTime) {
        this.inTime = inTime;
    }

    public String getInTimeImagePath() {
        return inTimeImagePath;
    }

    public void setInTimeImagePath(String inTimeImagePath) {
        this.inTimeImagePath = inTimeImagePath;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public String getOutTimeImagePath() {
        return outTimeImagePath;
    }

    public void setOutTimeImagePath(String outTimeImagePath) {
        this.outTimeImagePath = outTimeImagePath;
    }
}
