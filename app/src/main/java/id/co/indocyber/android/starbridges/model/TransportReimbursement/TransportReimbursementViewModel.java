package id.co.indocyber.android.starbridges.model.TransportReimbursement;

import com.google.gson.annotations.Expose;

import java.util.List;

import id.co.indocyber.android.starbridges.model.TransportReimbursementDetail.TransportReimbursementDetail;

public class TransportReimbursementViewModel {
    @Expose
    private String HeaderID;

    @Expose
    private String EmployeeID;

    @Expose
    private String Description;

    @Expose
    private Integer Amount;

    @Expose
    private Integer ReimbursementTypeID;

    private String ReimbursementType;

    @Expose
    private Integer TransactionStatusID;

    @Expose
    private String Period;

    @Expose
    private String Process_Period;

    @Expose
    private Boolean IsPreProcess;

    @Expose
    private Boolean IsTaxable;

    @Expose
    private String AttachmentFile;

    @Expose
    private String TransactionStatusSaveOrSubmit;

    @Expose
    private List<TransportReimbursementDetail> ListDetail;

    @Expose
    private Boolean FullAccess;

    @Expose
    private List<String> ExclusionFields;

    @Expose
    private String AccessibilityAttribute;

    public String getHeaderID() {
        return HeaderID;
    }

    public void setHeaderID(String headerID) {
        HeaderID = headerID;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Integer getAmount() {
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public Integer getReimbursementTypeID() {
        return ReimbursementTypeID;
    }

    public void setReimbursementTypeID(Integer reimbursementTypeID) {
        ReimbursementTypeID = reimbursementTypeID;
    }

    public Integer getTransactionStatusID() {
        return TransactionStatusID;
    }

    public void setTransactionStatusID(Integer transactionStatusID) {
        TransactionStatusID = transactionStatusID;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getProcess_Period() {
        return Process_Period;
    }

    public void setProcess_Period(String process_Period) {
        Process_Period = process_Period;
    }

    public Boolean getPreProcess() {
        return IsPreProcess;
    }

    public void setPreProcess(Boolean preProcess) {
        IsPreProcess = preProcess;
    }

    public Boolean getTaxable() {
        return IsTaxable;
    }

    public void setTaxable(Boolean taxable) {
        IsTaxable = taxable;
    }

    public String getAttachmentFile() {
        return AttachmentFile;
    }

    public void setAttachmentFile(String attachmentFile) {
        AttachmentFile = attachmentFile;
    }

    public String getTransactionStatusSaveOrSubmit() {
        return TransactionStatusSaveOrSubmit;
    }

    public void setTransactionStatusSaveOrSubmit(String transactionStatusSaveOrSubmit) {
        TransactionStatusSaveOrSubmit = transactionStatusSaveOrSubmit;
    }

    public List<TransportReimbursementDetail> getListDetail() {
        return ListDetail;
    }

    public void setListDetail(List<TransportReimbursementDetail> listDetail) {
        ListDetail = listDetail;
    }

    public Boolean getFullAccess() {
        return FullAccess;
    }

    public void setFullAccess(Boolean fullAccess) {
        FullAccess = fullAccess;
    }

    public List<String> getExclusionFields() {
        return ExclusionFields;
    }

    public void setExclusionFields(List<String> exclusionFields) {
        ExclusionFields = exclusionFields;
    }

    public String getAccessibilityAttribute() {
        return AccessibilityAttribute;
    }

    public void setAccessibilityAttribute(String accessibilityAttribute) {
        AccessibilityAttribute = accessibilityAttribute;
    }

    public String getReimbursementType() {
        return ReimbursementType;
    }

    public void setReimbursementType(String reimbursementType) {
        ReimbursementType = reimbursementType;
    }
}
