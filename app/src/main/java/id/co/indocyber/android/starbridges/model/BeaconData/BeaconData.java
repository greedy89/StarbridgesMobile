
package id.co.indocyber.android.starbridges.model.BeaconData;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Table;

import id.co.indocyber.android.starbridges.StarbridgeApplication;


public class BeaconData implements Serializable, Parcelable
{

    @SerializedName("CustomField")
    @Expose
    private CustomField customField;
    @SerializedName("ReturnValue")
    @Expose
    private List<ReturnValue> returnValue = null;
    @SerializedName("isSucceed")
    @Expose
    private Boolean isSucceed;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Creator<BeaconData> CREATOR = new Creator<BeaconData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public BeaconData createFromParcel(Parcel in) {
            return new BeaconData(in);
        }

        public BeaconData[] newArray(int size) {
            return (new BeaconData[size]);
        }

    }
    ;
    private final static long serialVersionUID = 5439788376119512013L;

    protected BeaconData(Parcel in) {
        this.customField = ((CustomField) in.readValue((CustomField.class.getClassLoader())));
        in.readList(this.returnValue, (id.co.indocyber.android.starbridges.model.BeaconData.ReturnValue.class.getClassLoader()));
        this.isSucceed = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public BeaconData() {
    }

    /**
     * 
     * @param message
     * @param isSucceed
     * @param customField
     * @param returnValue
     */
    public BeaconData(CustomField customField, List<ReturnValue> returnValue, Boolean isSucceed, String message) {
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

    public BeaconData withCustomField(CustomField customField) {
        this.customField = customField;
        return this;
    }

    public List<ReturnValue> getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(List<ReturnValue> returnValue) {
        this.returnValue = returnValue;
    }

    public BeaconData withReturnValue(List<ReturnValue> returnValue) {
        this.returnValue = returnValue;
        return this;
    }

    public Boolean getIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(Boolean isSucceed) {
        this.isSucceed = isSucceed;
    }

    public BeaconData withIsSucceed(Boolean isSucceed) {
        this.isSucceed = isSucceed;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BeaconData withMessage(String message) {
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
