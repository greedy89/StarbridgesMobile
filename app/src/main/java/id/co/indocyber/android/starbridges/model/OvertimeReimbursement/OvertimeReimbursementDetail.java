package id.co.indocyber.android.starbridges.model.OvertimeReimbursement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OvertimeReimbursementDetail implements Serializable {
    @SerializedName("ID")
    @Expose
    private String id;

    @SerializedName("IdHeader")
    @Expose
    private String idHeader;

    @SerializedName("OvertimeFrom")
    @Expose
    private String overtimeFrom;

    @SerializedName("OvertimeTo")
    @Expose
    private String overtimeTo;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("AmountDetail")
    @Expose
    private Integer amountDetail;

    @SerializedName("EncryptedAmountDetail")
    @Expose
    private String encryptedAmountDetail;

    @SerializedName("Notes")
    @Expose
    private String notes;

    @SerializedName("AttachmentFile")
    @Expose
    private String attachmentFile;

    @SerializedName("IsWeekend")
    @Expose
    private Boolean isWeekend;

    @SerializedName("AttachmentID")
    @Expose
    private Integer attachmentId;

    @SerializedName("ApproverNotes")
    @Expose
    private String approverNotes;

    @SerializedName("ApprovalStatus")
    @Expose
    private String approvalStatus;

    @SerializedName("DetailTransactionStatusID")
    @Expose
    private Integer detailTransactionStatusId;

    @SerializedName("FullAccess")
    @Expose
    private Boolean fullAccess;

    @SerializedName("ExclusionFields")
    @Expose
    private List<String> exclusionFields;

    @SerializedName("AccessibilityAttribute")
    @Expose
    private String accessibilityAttribute;

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

    public String getOvertimeFrom() {
        return overtimeFrom;
    }

    public void setOvertimeFrom(String overtimeFrom) {
        this.overtimeFrom = overtimeFrom;
    }

    public String getOvertimeTo() {
        return overtimeTo;
    }

    public void setOvertimeTo(String overtimeTo) {
        this.overtimeTo = overtimeTo;
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

    public String getEncryptedAmountDetail() {
        return encryptedAmountDetail;
    }

    public void setEncryptedAmountDetail(String encryptedAmountDetail) {
        this.encryptedAmountDetail = encryptedAmountDetail;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(String attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public Boolean getWeekend() {
        return isWeekend;
    }

    public void setWeekend(Boolean weekend) {
        isWeekend = weekend;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getApproverNotes() {
        return approverNotes;
    }

    public void setApproverNotes(String approverNotes) {
        this.approverNotes = approverNotes;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public List<String> getExclusionFields() {
        return exclusionFields;
    }

    public void setExclusionFields(List<String> exclusionFields) {
        this.exclusionFields = exclusionFields;
    }

    public String getAccessibilityAttribute() {
        return accessibilityAttribute;
    }

    public void setAccessibilityAttribute(String accessibilityAttribute) {
        this.accessibilityAttribute = accessibilityAttribute;
    }
}
