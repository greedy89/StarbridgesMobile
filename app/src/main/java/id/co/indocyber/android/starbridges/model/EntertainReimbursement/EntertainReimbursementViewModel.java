package id.co.indocyber.android.starbridges.model.EntertainReimbursement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EntertainReimbursementViewModel {
    @SerializedName("CreateDetail")
    @Expose
    private List<EntertainReimbursementDetail> createDetail;

    @SerializedName("HeaderID")
    @Expose
    private String headerId;

    @SerializedName("EmployeeID")
    @Expose
    private String employeeId;

    @SerializedName("ReimbursementTypeID")
    @Expose
    private Integer reimbursementTypeId;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("Amount")
    @Expose
    private Integer amount;

    @SerializedName("TranscationDate")
    @Expose
    private String transactionDate;

    @SerializedName("AttachmentID")
    @Expose
    private Integer attachmentId;

    @SerializedName("AttachmentFile")
    @Expose
    private String attachmentFile;

    @SerializedName("TransactionStatusID")
    @Expose
    private Integer transactionStatusId;

    @SerializedName("DecisionNumber")
    @Expose
    private String decisionNumber;

    @SerializedName("ProcessStep")
    @Expose
    private String processStep;

    @SerializedName("IsTaxable")
    @Expose
    private Boolean isTaxable;

    @SerializedName("Process_Period_Entertain")
    @Expose
    private String processPeriodEntertain;

    @SerializedName("TransactionStatusSaveOrSubmit")
    @Expose
    private String transactionStatusSaveOrSubmit;

    public List<EntertainReimbursementDetail> getCreateDetail() {
        return createDetail;
    }

    public void setCreateDetail(List<EntertainReimbursementDetail> createDetail) {
        this.createDetail = createDetail;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getReimbursementTypeId() {
        return reimbursementTypeId;
    }

    public void setReimbursementTypeId(Integer reimbursementTypeId) {
        this.reimbursementTypeId = reimbursementTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(String attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public Integer getTransactionStatusId() {
        return transactionStatusId;
    }

    public void setTransactionStatusId(Integer transactionStatusId) {
        this.transactionStatusId = transactionStatusId;
    }

    public String getDecisionNumber() {
        return decisionNumber;
    }

    public void setDecisionNumber(String decisionNumber) {
        this.decisionNumber = decisionNumber;
    }

    public String getProcessStep() {
        return processStep;
    }

    public void setProcessStep(String processStep) {
        this.processStep = processStep;
    }

    public Boolean getTaxable() {
        return isTaxable;
    }

    public void setTaxable(Boolean taxable) {
        isTaxable = taxable;
    }

    public String getProcessPeriodEntertain() {
        return processPeriodEntertain;
    }

    public void setProcessPeriodEntertain(String processPeriodEntertain) {
        this.processPeriodEntertain = processPeriodEntertain;
    }

    public String getTransactionStatusSaveOrSubmit() {
        return transactionStatusSaveOrSubmit;
    }

    public void setTransactionStatusSaveOrSubmit(String transactionStatusSaveOrSubmit) {
        this.transactionStatusSaveOrSubmit = transactionStatusSaveOrSubmit;
    }
}
