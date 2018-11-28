
package id.co.indocyber.android.starbridges.model.ListOvertimeReimbursement;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OvertimeReimbursement implements Serializable, Parcelable
{

    @SerializedName("CustomField")
    @Expose
    private CustomField customField;
    @SerializedName("ReturnValue")
    @Expose
    private List<ReturnValue> returnValue = null;
    @SerializedName("isSucceed")
    @Expose
    private boolean isSucceed;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Creator<OvertimeReimbursement> CREATOR = new Creator<OvertimeReimbursement>() {


        @SuppressWarnings({
            "unchecked"
        })
        public OvertimeReimbursement createFromParcel(Parcel in) {
            return new OvertimeReimbursement(in);
        }

        public OvertimeReimbursement[] newArray(int size) {
            return (new OvertimeReimbursement[size]);
        }

    }
    ;
    private final static long serialVersionUID = -3003330530751870942L;

    protected OvertimeReimbursement(Parcel in) {
        this.customField = ((CustomField) in.readValue((CustomField.class.getClassLoader())));
        in.readList(this.returnValue, (id.co.indocyber.android.starbridges.model.ListOvertimeReimbursement.ReturnValue.class.getClassLoader()));
        this.isSucceed = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public OvertimeReimbursement() {
    }

    /**
     * 
     * @param message
     * @param isSucceed
     * @param customField
     * @param returnValue
     */
    public OvertimeReimbursement(CustomField customField, List<ReturnValue> returnValue, boolean isSucceed, String message) {
        super();
        this.customField = customField;
        this.returnValue = returnValue;
        this.isSucceed = isSucceed;
        this.message = message;
    }

    public CustomField getCustomField() {
        return customField;
    }

    public void setCustomField(CustomField customField) {
        this.customField = customField;
    }

    public OvertimeReimbursement withCustomField(CustomField customField) {
        this.customField = customField;
        return this;
    }

    public List<ReturnValue> getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(List<ReturnValue> returnValue) {
        this.returnValue = returnValue;
    }

    public OvertimeReimbursement withReturnValue(List<ReturnValue> returnValue) {
        this.returnValue = returnValue;
        return this;
    }

    public boolean isIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(boolean isSucceed) {
        this.isSucceed = isSucceed;
    }

    public OvertimeReimbursement withIsSucceed(boolean isSucceed) {
        this.isSucceed = isSucceed;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OvertimeReimbursement withMessage(String message) {
        this.message = message;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(customField);
        dest.writeList(returnValue);
        dest.writeValue(isSucceed);
        dest.writeValue(message);
    }

    public int describeContents() {
        return  0;
    }

}
