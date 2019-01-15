package id.co.indocyber.android.starbridges.model.EntertainReimbursement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EntertainReimbursementDetail implements Serializable {

    @SerializedName("ID")
    @Expose
    private String id;

    @SerializedName("GeneralReimbursementID")
    @Expose
    private String generalReimbursementId;

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

    @SerializedName("EntertainPlace")
    @Expose
    private String entertainPlace;

    @SerializedName("Project")
    @Expose
    private String project;

    @SerializedName("Participant")
    @Expose
    private String participant;

    @SerializedName("Position")
    @Expose
    private String position;

    @SerializedName("EncryptedAmountDetail")
    @Expose
    private String encryptedAmountDetail;

    @SerializedName("AmountDetail")
    @Expose
    private Integer amountDetail;

    @SerializedName("Notes")
    @Expose
    private String notes;

    @SerializedName("AttachmentID")
    @Expose
    private Integer attachmentId;

    @SerializedName("CompanyName")
    @Expose
    private String companyName;

    @SerializedName("BusinessType")
    @Expose
    private String businessType;

    @SerializedName("AttachmentFile")
    @Expose
    private String attachmentFile;

    @SerializedName("DelDate")
    @Expose
    private String delDate;

    @SerializedName("DetailTransactionStatusID")
    @Expose
    private Integer detailTransactionStatusId;

    @SerializedName("ApprovalStatus")
    @Expose
    private String approvalStatus;

    @SerializedName("ApproverNotes")
    @Expose
    private String approverNotes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGeneralReimbursementId() {
        return generalReimbursementId;
    }

    public void setGeneralReimbursementId(String generalReimbursementId) {
        this.generalReimbursementId = generalReimbursementId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEntertainPlace() {
        return entertainPlace;
    }

    public void setEntertainPlace(String entertainPlace) {
        this.entertainPlace = entertainPlace;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEncryptedAmountDetail() {
        return encryptedAmountDetail;
    }

    public void setEncryptedAmountDetail(String encryptedAmountDetail) {
        this.encryptedAmountDetail = encryptedAmountDetail;
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

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(String attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public String getDelDate() {
        return delDate;
    }

    public void setDelDate(String delDate) {
        this.delDate = delDate;
    }

    public Integer getDetailTransactionStatusId() {
        return detailTransactionStatusId;
    }

    public void setDetailTransactionStatusId(Integer detailTransactionStatusId) {
        this.detailTransactionStatusId = detailTransactionStatusId;
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
}
