package id.co.indocyber.android.starbridges.model.TransportReimbursementDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TransportReimbursementDetail implements Serializable {
    @SerializedName("ID")
    @Expose
    private String id;

    @SerializedName("IdHeader")
    @Expose
    private String idHeader;

    @SerializedName("Min")
    @Expose
    private String min;

    @SerializedName("Max")
    @Expose
    private String max;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("AmountDetail")
    @Expose
    private Integer amountDetail;

    @SerializedName("Notes")
    @Expose
    private String notes;

    @SerializedName("LocationID")
    @Expose
    private Integer locationId;

    @SerializedName("Location")
    @Expose
    private String location;

    @SerializedName("LogIn")
    @Expose
    private String logIn;

    @SerializedName("LogInString")
    @Expose
    private String logInString;

    @SerializedName("LogOutString")
    @Expose
    private String logOutString;

    @SerializedName("AttendanceLogUID")
    @Expose
    private String attendanceLogUid;

    @SerializedName("StatusLog")
    @Expose
    private String statusLog;

    @SerializedName("AttendanceLocation")
    @Expose
    private String attendanceLocation;

    @SerializedName("ApprovalStatus")
    @Expose
    private String approvalStatus;

    @SerializedName("ApproverNotes")
    @Expose
    private String approverNotes;

    @SerializedName("DetailTransactionStatusID")
    @Expose
    private Integer detailTransactionStatusId;

    @SerializedName("FullAccess")
    @Expose
    private Boolean fullAccess;

    @SerializedName("ExclusionFields")
    @Expose
    private List<Object> exclusionFields;

    @SerializedName("AccessibilityAttribute")
    @Expose
    private String accessibilityAttribute;

    private boolean isSelected = false;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAmountDetail() {
        return amountDetail;
    }

    public void setAmountDetail(Integer amountDetail) {
        this.amountDetail = amountDetail;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogIn() {
        return logIn;
    }

    public void setLogIn(String logIn) {
        this.logIn = logIn;
    }

    public String getLogInString() {
        return logInString;
    }

    public void setLogInString(String logInString) {
        this.logInString = logInString;
    }

    public String getLogOutString() {
        return logOutString;
    }

    public void setLogOutString(String logOutString) {
        this.logOutString = logOutString;
    }

    public String getAttendanceLogUid() {
        return attendanceLogUid;
    }

    public void setAttendanceLogUid(String attendanceLogUid) {
        this.attendanceLogUid = attendanceLogUid;
    }

    public String getStatusLog() {
        return statusLog;
    }

    public void setStatusLog(String statusLog) {
        this.statusLog = statusLog;
    }

    public String getAttendanceLocation() {
        return attendanceLocation;
    }

    public void setAttendanceLocation(String attendanceLocation) {
        this.attendanceLocation = attendanceLocation;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApproverNotes() {
        return approverNotes;
    }

    public void setApproverNotes(String approverNotes) {
        this.approverNotes = approverNotes;
    }

    public Integer getDetailTransactionStatusId() {
        return detailTransactionStatusId;
    }

    public void setDetailTransactionStatusId(Integer detailTransactionStatusId) {
        this.detailTransactionStatusId = detailTransactionStatusId;
    }

    public Boolean getFullAccess() {
        return fullAccess;
    }

    public void setFullAccess(Boolean fullAccess) {
        this.fullAccess = fullAccess;
    }

    public List<Object> getExclusionFields() {
        return exclusionFields;
    }

    public void setExclusionFields(List<Object> exclusionFields) {
        this.exclusionFields = exclusionFields;
    }

    public String getAccessibilityAttribute() {
        return accessibilityAttribute;
    }

    public void setAccessibilityAttribute(String accessibilityAttribute) {
        this.accessibilityAttribute = accessibilityAttribute;
    }
}
