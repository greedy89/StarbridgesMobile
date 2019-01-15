package id.co.indocyber.android.starbridges.model.TransportReimbursement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditTransportReimbursement {
    @SerializedName("HeaderID")
    @Expose
    private String headerId;

    @SerializedName("EmployeeID")
    @Expose
    private String employeeId;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("Amount")
    @Expose
    private Integer amount;

    @SerializedName("ReimbursementTypeID")
    @Expose
    private Integer reimbursementTypeId;

    @SerializedName("TransactionStatusId")
    @Expose
    private String transactionStatusId;

    @SerializedName("Period")
    @Expose
    private String period;

    @SerializedName("Process_Period")
    @Expose
    private String processPeriod;

    @SerializedName("IsPreProcess")
    @Expose
    private Boolean isPreProcess;

    @SerializedName("IsTaxable")
    @Expose
    private String isTaxable;

    @SerializedName("AttachmentFile")
    @Expose
    private String attachmentFile;

    @SerializedName("TransactionStatusSaveOrSubmit")
    @Expose
    private String transactionStatusSaveOrSubmit;

    @SerializedName("ListDetail")
    @Expose
    private Object listDetail;

    @SerializedName("FullAccess")
    @Expose
    private Boolean fullAccess;

    @SerializedName("ExclusionFields")
    @Expose
    private List<String> exclusionFields;

    @SerializedName("AccessibilityAttribute")
    @Expose
    private String accessibilityAttribute;

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

    public Integer getReimbursementTypeId() {
        return reimbursementTypeId;
    }

    public void setReimbursementTypeId(Integer reimbursementTypeId) {
        this.reimbursementTypeId = reimbursementTypeId;
    }

    public String getTransactionStatusId() {
        return transactionStatusId;
    }

    public void setTransactionStatusId(String transactionStatusId) {
        this.transactionStatusId = transactionStatusId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getProcessPeriod() {
        return processPeriod;
    }

    public void setProcessPeriod(String processPeriod) {
        this.processPeriod = processPeriod;
    }

    public Boolean getPreProcess() {
        return isPreProcess;
    }

    public void setPreProcess(Boolean preProcess) {
        isPreProcess = preProcess;
    }

    public String getIsTaxable() {
        return isTaxable;
    }

    public void setIsTaxable(String isTaxable) {
        this.isTaxable = isTaxable;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(String attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public String getTransactionStatusSaveOrSubmit() {
        return transactionStatusSaveOrSubmit;
    }

    public void setTransactionStatusSaveOrSubmit(String transactionStatusSaveOrSubmit) {
        this.transactionStatusSaveOrSubmit = transactionStatusSaveOrSubmit;
    }

    public Object getListDetail() {
        return listDetail;
    }

    public void setListDetail(Object listDetail) {
        this.listDetail = listDetail;
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
