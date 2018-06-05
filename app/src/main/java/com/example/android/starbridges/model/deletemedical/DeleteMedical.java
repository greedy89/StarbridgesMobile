
package com.example.android.starbridges.model.deletemedical;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteMedical implements Serializable, Parcelable
{

    @SerializedName("CustomField")
    @Expose
    private CustomField customField;
    @SerializedName("ReturnValue")
    @Expose
    private Object returnValue;
    @SerializedName("isSucceed")
    @Expose
    private Boolean isSucceed;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Creator<DeleteMedical> CREATOR = new Creator<DeleteMedical>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DeleteMedical createFromParcel(Parcel in) {
            return new DeleteMedical(in);
        }

        public DeleteMedical[] newArray(int size) {
            return (new DeleteMedical[size]);
        }

    }
    ;
    private final static long serialVersionUID = 7333596151388007985L;

    protected DeleteMedical(Parcel in) {
        this.customField = ((CustomField) in.readValue((CustomField.class.getClassLoader())));
        this.returnValue = ((Object) in.readValue((Object.class.getClassLoader())));
        this.isSucceed = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public DeleteMedical() {
    }

    /**
     * 
     * @param message
     * @param isSucceed
     * @param customField
     * @param returnValue
     */
    public DeleteMedical(CustomField customField, Object returnValue, Boolean isSucceed, String message) {
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

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Boolean getIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(Boolean isSucceed) {
        this.isSucceed = isSucceed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(customField);
        dest.writeValue(returnValue);
        dest.writeValue(isSucceed);
        dest.writeValue(message);
    }

    public int describeContents() {
        return  0;
    }

}
