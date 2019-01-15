package id.co.indocyber.android.starbridges.model.EntertainReimbursement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EntertainReimbursement {
    @SerializedName("ID")
    @Expose
    private String id;

    @SerializedName("DecisionNumber")
    @Expose
    private String decisionNumber;

    @SerializedName("ApprovedDate")
    @Expose
    private String approvedDate;

    @SerializedName("ProcessPeriod")
    @Expose
    private String processPeriod;

    @SerializedName("Year")
    @Expose
    private Integer year;

    @SerializedName("Month")
    @Expose
    private String month;

    @SerializedName("Type")
    @Expose
    private String type;

    @SerializedName("IsPreProcess")
    @Expose
    private Boolean isPreProcess;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("Amount")
    @Expose
    private Integer amount;

    @SerializedName("EncryptedAmount")
    @Expose
    private String encryptedAmount;

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

    public String getDecisionNumber() {
        return decisionNumber;
    }

    public void setDecisionNumber(String decisionNumber) {
        this.decisionNumber = decisionNumber;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getProcessPeriod() {
        return processPeriod;
    }

    public void setProcessPeriod(String processPeriod) {
        this.processPeriod = processPeriod;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPreProcess() {
        return isPreProcess;
    }

    public void setPreProcess(Boolean preProcess) {
        isPreProcess = preProcess;
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

    public String getEncryptedAmount() {
        return encryptedAmount;
    }

    public void setEncryptedAmount(String encryptedAmount) {
        this.encryptedAmount = encryptedAmount;
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
