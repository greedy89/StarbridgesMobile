
package id.co.indocyber.android.starbridges.model.EditDraftLoan;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnValue implements Serializable, Parcelable
{

    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("EmployeeID")
    @Expose
    private String employeeID;
    @SerializedName("EmployeeLoanBalanceID")
    @Expose
    private Integer employeeLoanBalanceID;
    @SerializedName("DecisionNumber")
    @Expose
    private Object decisionNumber;
    @SerializedName("TransactionStatusID")
    @Expose
    private Integer transactionStatusID;
    @SerializedName("LoanTransactionTypeID")
    @Expose
    private Integer loanTransactionTypeID;
    @SerializedName("LoanTransactionType")
    @Expose
    private String loanTransactionType;
    @SerializedName("LoanTransactionTypeLookUp")
    @Expose
    private Object loanTransactionTypeLookUp;
    @SerializedName("LoanPolicyID")
    @Expose
    private Integer loanPolicyID;
    @SerializedName("LoanPolicyLookUp")
    @Expose
    private Object loanPolicyLookUp;
    @SerializedName("StartNewLoanDate")
    @Expose
    private String startNewLoanDate;
    @SerializedName("CreditAmount")
    @Expose
    private Integer creditAmount;
    @SerializedName("EmployeeLoanScheduleID")
    @Expose
    private Object employeeLoanScheduleID;
    @SerializedName("Amount")
    @Expose
    private Integer amount;
    @SerializedName("Description")
    @Expose
    private Object description;
    @SerializedName("LoanSettingName")
    @Expose
    private Object loanSettingName;
    @SerializedName("Limit")
    @Expose
    private Object limit;
    @SerializedName("FullAccess")
    @Expose
    private Boolean fullAccess;
    @SerializedName("ExclusionFields")
    @Expose
    private List<Object> exclusionFields = null;
    @SerializedName("AccessibilityAttribute")
    @Expose
    private String accessibilityAttribute;
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
    private final static long serialVersionUID = -7365328065402375557L;

    protected ReturnValue(Parcel in) {
        this.iD = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeID = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeLoanBalanceID = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.decisionNumber = ((Object) in.readValue((Object.class.getClassLoader())));
        this.transactionStatusID = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.loanTransactionTypeID = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.loanTransactionType = ((String) in.readValue((String.class.getClassLoader())));
        this.loanTransactionTypeLookUp = ((Object) in.readValue((Object.class.getClassLoader())));
        this.loanPolicyID = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.loanPolicyLookUp = ((Object) in.readValue((Object.class.getClassLoader())));
        this.startNewLoanDate = ((String) in.readValue((String.class.getClassLoader())));
        this.creditAmount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.employeeLoanScheduleID = ((Object) in.readValue((Object.class.getClassLoader())));
        this.amount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.description = ((Object) in.readValue((Object.class.getClassLoader())));
        this.loanSettingName = ((Object) in.readValue((Object.class.getClassLoader())));
        this.limit = ((Object) in.readValue((Object.class.getClassLoader())));
        this.fullAccess = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        in.readList(this.exclusionFields, (Object.class.getClassLoader()));
        this.accessibilityAttribute = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ReturnValue() {
    }

    /**
     * 
     * @param limit
     * @param employeeLoanScheduleID
     * @param accessibilityAttribute
     * @param employeeID
     * @param loanSettingName
     * @param fullAccess
     * @param loanPolicyLookUp
     * @param employeeLoanBalanceID
     * @param iD
     * @param loanPolicyID
     * @param exclusionFields
     * @param startNewLoanDate
     * @param amount
     * @param loanTransactionTypeID
     * @param decisionNumber
     * @param loanTransactionType
     * @param description
     * @param creditAmount
     * @param loanTransactionTypeLookUp
     * @param transactionStatusID
     */
    public ReturnValue(String iD, String employeeID, Integer employeeLoanBalanceID, Object decisionNumber, Integer transactionStatusID, Integer loanTransactionTypeID, String loanTransactionType, Object loanTransactionTypeLookUp, Integer loanPolicyID, Object loanPolicyLookUp, String startNewLoanDate, Integer creditAmount, Object employeeLoanScheduleID, Integer amount, Object description, Object loanSettingName, Object limit, Boolean fullAccess, List<Object> exclusionFields, String accessibilityAttribute) {
        super();
        this.iD = iD;
        this.employeeID = employeeID;
        this.employeeLoanBalanceID = employeeLoanBalanceID;
        this.decisionNumber = decisionNumber;
        this.transactionStatusID = transactionStatusID;
        this.loanTransactionTypeID = loanTransactionTypeID;
        this.loanTransactionType = loanTransactionType;
        this.loanTransactionTypeLookUp = loanTransactionTypeLookUp;
        this.loanPolicyID = loanPolicyID;
        this.loanPolicyLookUp = loanPolicyLookUp;
        this.startNewLoanDate = startNewLoanDate;
        this.creditAmount = creditAmount;
        this.employeeLoanScheduleID = employeeLoanScheduleID;
        this.amount = amount;
        this.description = description;
        this.loanSettingName = loanSettingName;
        this.limit = limit;
        this.fullAccess = fullAccess;
        this.exclusionFields = exclusionFields;
        this.accessibilityAttribute = accessibilityAttribute;
    }

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public ReturnValue withID(String iD) {
        this.iD = iD;
        return this;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public ReturnValue withEmployeeID(String employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public Integer getEmployeeLoanBalanceID() {
        return employeeLoanBalanceID;
    }

    public void setEmployeeLoanBalanceID(Integer employeeLoanBalanceID) {
        this.employeeLoanBalanceID = employeeLoanBalanceID;
    }

    public ReturnValue withEmployeeLoanBalanceID(Integer employeeLoanBalanceID) {
        this.employeeLoanBalanceID = employeeLoanBalanceID;
        return this;
    }

    public Object getDecisionNumber() {
        return decisionNumber;
    }

    public void setDecisionNumber(Object decisionNumber) {
        this.decisionNumber = decisionNumber;
    }

    public ReturnValue withDecisionNumber(Object decisionNumber) {
        this.decisionNumber = decisionNumber;
        return this;
    }

    public Integer getTransactionStatusID() {
        return transactionStatusID;
    }

    public void setTransactionStatusID(Integer transactionStatusID) {
        this.transactionStatusID = transactionStatusID;
    }

    public ReturnValue withTransactionStatusID(Integer transactionStatusID) {
        this.transactionStatusID = transactionStatusID;
        return this;
    }

    public Integer getLoanTransactionTypeID() {
        return loanTransactionTypeID;
    }

    public void setLoanTransactionTypeID(Integer loanTransactionTypeID) {
        this.loanTransactionTypeID = loanTransactionTypeID;
    }

    public ReturnValue withLoanTransactionTypeID(Integer loanTransactionTypeID) {
        this.loanTransactionTypeID = loanTransactionTypeID;
        return this;
    }

    public String getLoanTransactionType() {
        return loanTransactionType;
    }

    public void setLoanTransactionType(String loanTransactionType) {
        this.loanTransactionType = loanTransactionType;
    }

    public ReturnValue withLoanTransactionType(String loanTransactionType) {
        this.loanTransactionType = loanTransactionType;
        return this;
    }

    public Object getLoanTransactionTypeLookUp() {
        return loanTransactionTypeLookUp;
    }

    public void setLoanTransactionTypeLookUp(Object loanTransactionTypeLookUp) {
        this.loanTransactionTypeLookUp = loanTransactionTypeLookUp;
    }

    public ReturnValue withLoanTransactionTypeLookUp(Object loanTransactionTypeLookUp) {
        this.loanTransactionTypeLookUp = loanTransactionTypeLookUp;
        return this;
    }

    public Integer getLoanPolicyID() {
        return loanPolicyID;
    }

    public void setLoanPolicyID(Integer loanPolicyID) {
        this.loanPolicyID = loanPolicyID;
    }

    public ReturnValue withLoanPolicyID(Integer loanPolicyID) {
        this.loanPolicyID = loanPolicyID;
        return this;
    }

    public Object getLoanPolicyLookUp() {
        return loanPolicyLookUp;
    }

    public void setLoanPolicyLookUp(Object loanPolicyLookUp) {
        this.loanPolicyLookUp = loanPolicyLookUp;
    }

    public ReturnValue withLoanPolicyLookUp(Object loanPolicyLookUp) {
        this.loanPolicyLookUp = loanPolicyLookUp;
        return this;
    }

    public String getStartNewLoanDate() {
        return startNewLoanDate;
    }

    public void setStartNewLoanDate(String startNewLoanDate) {
        this.startNewLoanDate = startNewLoanDate;
    }

    public ReturnValue withStartNewLoanDate(String startNewLoanDate) {
        this.startNewLoanDate = startNewLoanDate;
        return this;
    }

    public Integer getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Integer creditAmount) {
        this.creditAmount = creditAmount;
    }

    public ReturnValue withCreditAmount(Integer creditAmount) {
        this.creditAmount = creditAmount;
        return this;
    }

    public Object getEmployeeLoanScheduleID() {
        return employeeLoanScheduleID;
    }

    public void setEmployeeLoanScheduleID(Object employeeLoanScheduleID) {
        this.employeeLoanScheduleID = employeeLoanScheduleID;
    }

    public ReturnValue withEmployeeLoanScheduleID(Object employeeLoanScheduleID) {
        this.employeeLoanScheduleID = employeeLoanScheduleID;
        return this;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ReturnValue withAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public ReturnValue withDescription(Object description) {
        this.description = description;
        return this;
    }

    public Object getLoanSettingName() {
        return loanSettingName;
    }

    public void setLoanSettingName(Object loanSettingName) {
        this.loanSettingName = loanSettingName;
    }

    public ReturnValue withLoanSettingName(Object loanSettingName) {
        this.loanSettingName = loanSettingName;
        return this;
    }

    public Object getLimit() {
        return limit;
    }

    public void setLimit(Object limit) {
        this.limit = limit;
    }

    public ReturnValue withLimit(Object limit) {
        this.limit = limit;
        return this;
    }

    public Boolean getFullAccess() {
        return fullAccess;
    }

    public void setFullAccess(Boolean fullAccess) {
        this.fullAccess = fullAccess;
    }

    public ReturnValue withFullAccess(Boolean fullAccess) {
        this.fullAccess = fullAccess;
        return this;
    }

    public List<Object> getExclusionFields() {
        return exclusionFields;
    }

    public void setExclusionFields(List<Object> exclusionFields) {
        this.exclusionFields = exclusionFields;
    }

    public ReturnValue withExclusionFields(List<Object> exclusionFields) {
        this.exclusionFields = exclusionFields;
        return this;
    }

    public String getAccessibilityAttribute() {
        return accessibilityAttribute;
    }

    public void setAccessibilityAttribute(String accessibilityAttribute) {
        this.accessibilityAttribute = accessibilityAttribute;
    }

    public ReturnValue withAccessibilityAttribute(String accessibilityAttribute) {
        this.accessibilityAttribute = accessibilityAttribute;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(iD);
        dest.writeValue(employeeID);
        dest.writeValue(employeeLoanBalanceID);
        dest.writeValue(decisionNumber);
        dest.writeValue(transactionStatusID);
        dest.writeValue(loanTransactionTypeID);
        dest.writeValue(loanTransactionType);
        dest.writeValue(loanTransactionTypeLookUp);
        dest.writeValue(loanPolicyID);
        dest.writeValue(loanPolicyLookUp);
        dest.writeValue(startNewLoanDate);
        dest.writeValue(creditAmount);
        dest.writeValue(employeeLoanScheduleID);
        dest.writeValue(amount);
        dest.writeValue(description);
        dest.writeValue(loanSettingName);
        dest.writeValue(limit);
        dest.writeValue(fullAccess);
        dest.writeList(exclusionFields);
        dest.writeValue(accessibilityAttribute);
    }

    public int describeContents() {
        return  0;
    }

}
