package adiractech.ams.tables;

import com.orm.SugarRecord;

import adiractech.ams.utils.Constants;

/**
 * Created by root on 13/04/17.
 */

public class Attendance  extends SugarRecord {
    private int employeeId;
    private long inTime;
    private String inTimeImagePath;
    private long outTime;
    private String outTimeImagePath;
    private int status;
    private boolean isSyncedWithServer;



    public Attendance(){
        status = Constants.ATTENDANCE_STATUS_NA;
        inTime = 0;
        outTime = 0;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setIsSyncedWithServer(boolean isSyncedWithServer){
        this.isSyncedWithServer = isSyncedWithServer;
    }

    public boolean getIsSyncedWithServer(){
        return isSyncedWithServer;
    }
}
