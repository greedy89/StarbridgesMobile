package id.co.indocyber.android.starbridges.model.TransportReimbursement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransportReimbursement {
    @SerializedName("ID")
    @Expose
    private String id;

    @SerializedName("DecisionNumber")
    @Expose
    private String decisionNumber;
    @SerializedName("IsPreProcess")
    @Expose
    private Boolean isPreProcess;
    @SerializedName("ApprovedDate")
    @Expose
    private String approvedDate;
    @SerializedName("ProcessPeriod")
    @Expose
    private String processPeriod;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("EncryptedAmount")
    @Expose
    private String encryptedAmount;
    @SerializedName("Amount")
    @Expose
    private Integer amount;
    @SerializedName("Month")
    @Expose
    private Integer month;
    @SerializedName("Year")
    @Expose
    private Integer year;

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

    public Boolean getPreProcess() {
        return isPreProcess;
    }

    public void setPreProcess(Boolean preProcess) {
        isPreProcess = preProcess;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEncryptedAmount() {
        return encryptedAmount;
    }

    public void setEncryptedAmount(String encryptedAmount) {
        this.encryptedAmount = encryptedAmount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
