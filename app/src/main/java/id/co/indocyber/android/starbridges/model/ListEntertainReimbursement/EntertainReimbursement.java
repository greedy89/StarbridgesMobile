
package id.co.indocyber.android.starbridges.model.ListEntertainReimbursement;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntertainReimbursement implements Serializable, Parcelable
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
    public final static Parcelable.Creator<EntertainReimbursement> CREATOR = new Creator<EntertainReimbursement>() {


        @SuppressWarnings({
            "unchecked"
        })
        public EntertainReimbursement createFromParcel(Parcel in) {
            return new EntertainReimbursement(in);
        }

        public EntertainReimbursement[] newArray(int size) {
            return (new EntertainReimbursement[size]);
        }

    }
    ;
    private final static long serialVersionUID = -6788815561351511853L;

    protected EntertainReimbursement(Parcel in) {
        this.customField = ((CustomField) in.readValue((CustomField.class.getClassLoader())));
        in.readList(this.returnValue, (id.co.indocyber.android.starbridges.model.ListEntertainReimbursement.ReturnValue.class.getClassLoader()));
        this.isSucceed = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public EntertainReimbursement() {
    }

    /**
     * 
     * @param message
     * @param isSucceed
     * @param customField
     * @param returnValue
     */
    public EntertainReimbursement(CustomField customField, List<ReturnValue> returnValue, boolean isSucceed, String message) {
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

    public EntertainReimbursement withCustomField(CustomField customField) {
        this.customField = customField;
        return this;
    }

    public List<ReturnValue> getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(List<ReturnValue> returnValue) {
        this.returnValue = returnValue;
    }

    public EntertainReimbursement withReturnValue(List<ReturnValue> returnValue) {
        this.returnValue = returnValue;
        return this;
    }

    public boolean isIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(boolean isSucceed) {
        this.isSucceed = isSucceed;
    }

    public EntertainReimbursement withIsSucceed(boolean isSucceed) {
        this.isSucceed = isSucceed;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EntertainReimbursement withMessage(String message) {
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
