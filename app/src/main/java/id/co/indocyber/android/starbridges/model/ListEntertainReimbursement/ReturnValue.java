
package id.co.indocyber.android.starbridges.model.ListEntertainReimbursement;

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
    private int year;
    @SerializedName("Month")
    @Expose
    private int month;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("IsPreProcess")
    @Expose
    private boolean isPreProcess;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Amount")
    @Expose
    private int amount;
    @SerializedName("EncryptedAmount")
    @Expose
    private String encryptedAmount;
    @SerializedName("FullAccess")
    @Expose
    private boolean fullAccess;
    @SerializedName("ExclusionFields")
    @Expose
    private List<Object> exclusionFields = null;
    @SerializedName("AccessibilityAttribute")
    @Expose
    private String accessibilityAttribute;
    public final static Parcelable.Creator<ReturnValue> CREATOR = new Creator<ReturnValue>() {


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
    private final static long serialVersionUID = -8771664855580164457L;

    protected ReturnValue(Parcel in) {
        this.iD = ((String) in.readValue((String.class.getClassLoader())));
        this.decisionNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.approvedDate = ((String) in.readValue((String.class.getClassLoader())));
        this.processPeriod = ((String) in.readValue((String.class.getClassLoader())));
        this.year = ((int) in.readValue((int.class.getClassLoader())));
        this.month = ((int) in.readValue((int.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.isPreProcess = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.amount = ((int) in.readValue((int.class.getClassLoader())));
        this.encryptedAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.fullAccess = ((boolean) in.readValue((boolean.class.getClassLoader())));
        in.readList(this.exclusionFields, (java.lang.Object.class.getClassLoader()));
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
     * @param accessibilityAttribute
     * @param processPeriod
     * @param fullAccess
     * @param approvedDate
     * @param type
     * @param iD
     * @param exclusionFields
     * @param amount
     * @param decisionNumber
     * @param description
     * @param month
     * @param encryptedAmount
     * @param year
     * @param isPreProcess
     */
    public ReturnValue(String iD, String decisionNumber, String approvedDate, String processPeriod, int year, int month, String type, boolean isPreProcess, String description, int amount, String encryptedAmount, boolean fullAccess, List<Object> exclusionFields, String accessibilityAttribute) {
        super();
        this.iD = iD;
        this.decisionNumber = decisionNumber;
        this.approvedDate = approvedDate;
        this.processPeriod = processPeriod;
        this.year = year;
        this.month = month;
        this.type = type;
        this.isPreProcess = isPreProcess;
        this.description = description;
        this.amount = amount;
        this.encryptedAmount = encryptedAmount;
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

    public String getDecisionNumber() {
        return decisionNumber;
    }

    public void setDecisionNumber(String decisionNumber) {
        this.decisionNumber = decisionNumber;
    }

    public ReturnValue withDecisionNumber(String decisionNumber) {
        this.decisionNumber = decisionNumber;
        return this;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public ReturnValue withApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
        return this;
    }

    public String getProcessPeriod() {
        return processPeriod;
    }

    public void setProcessPeriod(String processPeriod) {
        this.processPeriod = processPeriod;
    }

    public ReturnValue withProcessPeriod(String processPeriod) {
        this.processPeriod = processPeriod;
        return this;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ReturnValue withYear(int year) {
        this.year = year;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public ReturnValue withMonth(int month) {
        this.month = month;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ReturnValue withType(String type) {
        this.type = type;
        return this;
    }

    public boolean isIsPreProcess() {
        return isPreProcess;
    }

    public void setIsPreProcess(boolean isPreProcess) {
        this.isPreProcess = isPreProcess;
    }

    public ReturnValue withIsPreProcess(boolean isPreProcess) {
        this.isPreProcess = isPreProcess;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReturnValue withDescription(String description) {
        this.description = description;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ReturnValue withAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String getEncryptedAmount() {
        return encryptedAmount;
    }

    public void setEncryptedAmount(String encryptedAmount) {
        this.encryptedAmount = encryptedAmount;
    }

    public ReturnValue withEncryptedAmount(String encryptedAmount) {
        this.encryptedAmount = encryptedAmount;
        return this;
    }

    public boolean isFullAccess() {
        return fullAccess;
    }

    public void setFullAccess(boolean fullAccess) {
        this.fullAccess = fullAccess;
    }

    public ReturnValue withFullAccess(boolean fullAccess) {
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
        dest.writeValue(decisionNumber);
        dest.writeValue(approvedDate);
        dest.writeValue(processPeriod);
        dest.writeValue(year);
        dest.writeValue(month);
        dest.writeValue(type);
        dest.writeValue(isPreProcess);
        dest.writeValue(description);
        dest.writeValue(amount);
        dest.writeValue(encryptedAmount);
        dest.writeValue(fullAccess);
        dest.writeList(exclusionFields);
        dest.writeValue(accessibilityAttribute);
    }

    public int describeContents() {
        return  0;
    }

}
