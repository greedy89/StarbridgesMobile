
package id.co.indocyber.android.starbridges.model.ListOvertimeReimbursement;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReturnValue implements Serializable, Parcelable
{

    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("DecisionNumber")
    @Expose
    private String decisionNumber;
    @SerializedName("IsPreProcess")
    @Expose
    private boolean isPreProcess;
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
    private int amount;
    @SerializedName("Month")
    @Expose
    private int month;
    @SerializedName("Year")
    @Expose
    private int year;
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
    private final static long serialVersionUID = 1332041992046488376L;

    protected ReturnValue(Parcel in) {
        this.iD = ((String) in.readValue((String.class.getClassLoader())));
        this.decisionNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.isPreProcess = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.approvedDate = ((String) in.readValue((String.class.getClassLoader())));
        this.processPeriod = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.encryptedAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.amount = ((int) in.readValue((int.class.getClassLoader())));
        this.month = ((int) in.readValue((int.class.getClassLoader())));
        this.year = ((int) in.readValue((int.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ReturnValue() {
    }

    /**
     * 
     * @param amount
     * @param decisionNumber
     * @param processPeriod
     * @param description
     * @param month
     * @param encryptedAmount
     * @param year
     * @param isPreProcess
     * @param approvedDate
     * @param type
     * @param iD
     */
    public ReturnValue(String iD, String decisionNumber, boolean isPreProcess, String approvedDate, String processPeriod, String type, String description, String encryptedAmount, int amount, int month, int year) {
        super();
        this.iD = iD;
        this.decisionNumber = decisionNumber;
        this.isPreProcess = isPreProcess;
        this.approvedDate = approvedDate;
        this.processPeriod = processPeriod;
        this.type = type;
        this.description = description;
        this.encryptedAmount = encryptedAmount;
        this.amount = amount;
        this.month = month;
        this.year = year;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(iD);
        dest.writeValue(decisionNumber);
        dest.writeValue(isPreProcess);
        dest.writeValue(approvedDate);
        dest.writeValue(processPeriod);
        dest.writeValue(type);
        dest.writeValue(description);
        dest.writeValue(encryptedAmount);
        dest.writeValue(amount);
        dest.writeValue(month);
        dest.writeValue(year);
    }

    public int describeContents() {
        return  0;
    }

}
