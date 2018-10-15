
package id.co.indocyber.android.starbridges.model.EmployeeShiftSchedule;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnValue implements Serializable, Parcelable
{

    @SerializedName("EmployeeID")
    @Expose
    private Integer employeeID;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("IsHoliday")
    @Expose
    private Boolean isHoliday;
    @SerializedName("IsLeave")
    @Expose
    private Boolean isLeave;
    @SerializedName("IsWeekend")
    @Expose
    private Boolean isWeekend;
    @SerializedName("LoginTime")
    @Expose
    private String loginTime;
    @SerializedName("BreakStartTime")
    @Expose
    private String breakStartTime;
    @SerializedName("BreakEndTime")
    @Expose
    private String breakEndTime;
    @SerializedName("LogoutTime")
    @Expose
    private String logoutTime;
    public final static Creator<ReturnValue> CREATOR = new Creator<ReturnValue>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ReturnValue createFromParcel(Parcel in) {
            return new ReturnValue(in);
        }

        public ReturnValue[] newArray(int size) {
            return (new ReturnValue[size]);
        }

    }
    ;
    private final static long serialVersionUID = -4969643418683776841L;

    protected ReturnValue(Parcel in) {
        this.employeeID = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.isHoliday = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.isLeave = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.isWeekend = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.loginTime = ((String) in.readValue((String.class.getClassLoader())));
        this.breakStartTime = ((String) in.readValue((String.class.getClassLoader())));
        this.breakEndTime = ((String) in.readValue((String.class.getClassLoader())));
        this.logoutTime = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ReturnValue() {
    }

    /**
     * 
     * @param loginTime
     * @param employeeID
     * @param isLeave
     * @param isWeekend
     * @param isHoliday
     * @param logoutTime
     * @param date
     * @param breakEndTime
     * @param breakStartTime
     */
    public ReturnValue(Integer employeeID, String date, Boolean isHoliday, Boolean isLeave, Boolean isWeekend, String loginTime, String breakStartTime, String breakEndTime, String logoutTime) {
        super();
        this.employeeID = employeeID;
        this.date = date;
        this.isHoliday = isHoliday;
        this.isLeave = isLeave;
        this.isWeekend = isWeekend;
        this.loginTime = loginTime;
        this.breakStartTime = breakStartTime;
        this.breakEndTime = breakEndTime;
        this.logoutTime = logoutTime;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public ReturnValue withEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ReturnValue withDate(String date) {
        this.date = date;
        return this;
    }

    public Boolean getIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(Boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public ReturnValue withIsHoliday(Boolean isHoliday) {
        this.isHoliday = isHoliday;
        return this;
    }

    public Boolean getIsLeave() {
        return isLeave;
    }

    public void setIsLeave(Boolean isLeave) {
        this.isLeave = isLeave;
    }

    public ReturnValue withIsLeave(Boolean isLeave) {
        this.isLeave = isLeave;
        return this;
    }

    public Boolean getIsWeekend() {
        return isWeekend;
    }

    public void setIsWeekend(Boolean isWeekend) {
        this.isWeekend = isWeekend;
    }

    public ReturnValue withIsWeekend(Boolean isWeekend) {
        this.isWeekend = isWeekend;
        return this;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public ReturnValue withLoginTime(String loginTime) {
        this.loginTime = loginTime;
        return this;
    }

    public String getBreakStartTime() {
        return breakStartTime;
    }

    public void setBreakStartTime(String breakStartTime) {
        this.breakStartTime = breakStartTime;
    }

    public ReturnValue withBreakStartTime(String breakStartTime) {
        this.breakStartTime = breakStartTime;
        return this;
    }

    public String getBreakEndTime() {
        return breakEndTime;
    }

    public void setBreakEndTime(String breakEndTime) {
        this.breakEndTime = breakEndTime;
    }

    public ReturnValue withBreakEndTime(String breakEndTime) {
        this.breakEndTime = breakEndTime;
        return this;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

    public ReturnValue withLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(employeeID);
        dest.writeValue(date);
        dest.writeValue(isHoliday);
        dest.writeValue(isLeave);
        dest.writeValue(isWeekend);
        dest.writeValue(loginTime);
        dest.writeValue(breakStartTime);
        dest.writeValue(breakEndTime);
        dest.writeValue(logoutTime);
    }

    public int describeContents() {
        return  0;
    }

}
