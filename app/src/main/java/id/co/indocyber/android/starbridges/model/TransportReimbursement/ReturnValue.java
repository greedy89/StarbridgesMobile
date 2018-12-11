
package id.co.indocyber.android.starbridges.model.TransportReimbursement;

import java.io.Serializable;
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
    private Object decisionNumber;
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
    private final static long serialVersionUID = 1881756130881122122L;

    protected ReturnValue(Parcel in) {
        this.iD = ((String) in.readValue((String.class.getClassLoader())));
        this.decisionNumber = ((Object) in.readValue((Object.class.getClassLoader())));
        this.isPreProcess = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.approvedDate = ((String) in.readValue((String.class.getClassLoader())));
        this.processPeriod = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.encryptedAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.amount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.month = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.year = ((Integer) in.readValue((Integer.class.getClassLoader())));
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
    public ReturnValue(String iD, Object decisionNumber, Boolean isPreProcess, String approvedDate, String processPeriod, String type, String description, String encryptedAmount, Integer amount, Integer month, Integer year) {
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

    public Boolean getIsPreProcess() {
        return isPreProcess;
    }

    public void setIsPreProcess(Boolean isPreProcess) {
        this.isPreProcess = isPreProcess;
    }

    public ReturnValue withIsPreProcess(Boolean isPreProcess) {
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

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public ReturnValue withMonth(Integer month) {
        this.month = month;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public ReturnValue withYear(Integer year) {
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
