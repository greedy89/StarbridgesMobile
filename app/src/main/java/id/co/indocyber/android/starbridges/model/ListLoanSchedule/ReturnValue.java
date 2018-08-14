
package id.co.indocyber.android.starbridges.model.ListLoanSchedule;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import id.co.indocyber.android.starbridges.network.StringConverter;

public class ReturnValue implements Serializable, Parcelable
{

    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("Amount")
    @Expose
    private Integer amount;
    @SerializedName("ProcessStep")
    @Expose
    private Object processStep;
    @SerializedName("Installment")
    @Expose
    private Integer installment;
    @SerializedName("ProcessPeriod")
    @Expose
    private String processPeriod;
    @SerializedName("IsProcessed")
    @Expose
    private Boolean isProcessed;
    @SerializedName("IsClosed")
    @Expose
    private Boolean isClosed;
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
    private final static long serialVersionUID = -8245928984376486092L;

    protected ReturnValue(Parcel in) {
        this.iD = ((String) in.readValue((String.class.getClassLoader())));
        this.amount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.processStep = ((Object) in.readValue((Object.class.getClassLoader())));
        this.installment = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.processPeriod = ((String) in.readValue((String.class.getClassLoader())));
        this.isProcessed = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.isClosed = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ReturnValue() {
    }

    /**
     * 
     * @param installment
     * @param amount
     * @param processPeriod
     * @param processStep
     * @param isProcessed
     * @param isClosed
     * @param iD
     */
    public ReturnValue(String iD, Integer amount, Object processStep, Integer installment, String processPeriod, Boolean isProcessed, Boolean isClosed) {
        super();
        this.iD = iD;
        this.amount = amount;
        this.processStep = processStep;
        this.installment = installment;
        this.processPeriod = processPeriod;
        this.isProcessed = isProcessed;
        this.isClosed = isClosed;
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

    public Object getProcessStep() {
        return processStep;
    }

    public void setProcessStep(Object processStep) {
        this.processStep = processStep;
    }

    public ReturnValue withProcessStep(Object processStep) {
        this.processStep = processStep;
        return this;
    }

    public Integer getInstallment() {
        return installment;
    }

    public void setInstallment(Integer installment) {
        this.installment = installment;
    }

    public ReturnValue withInstallment(Integer installment) {
        this.installment = installment;
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

    public Boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public ReturnValue withIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
        return this;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public ReturnValue withIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(iD);
        dest.writeValue(amount);
        dest.writeValue(processStep);
        dest.writeValue(installment);
        dest.writeValue(processPeriod);
        dest.writeValue(isProcessed);
        dest.writeValue(isClosed);
    }

    public int describeContents() {
        return  0;
    }

    @Override
    public String toString() {
        StringConverter stringConverter=new StringConverter();
        if(processPeriod==null)
            return "";
        return stringConverter.dateFormatMMMMYYYY(processPeriod);
    }
}
