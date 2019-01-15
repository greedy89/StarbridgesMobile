package id.co.indocyber.android.starbridges.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



public class WebServiceResponseList<T> implements Serializable, Parcelable
{

    @SerializedName("CustomField")
    @Expose
    private Object customField;
    @SerializedName("ReturnValue")
    @Expose
    private List<T> returnValue = null;
    @SerializedName("isSucceed")
    @Expose
    private Boolean isSucceed;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Creator<WebServiceResponseList> CREATOR = new Creator<WebServiceResponseList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public WebServiceResponseList createFromParcel(Parcel in) {
            return new WebServiceResponseList(in);
        }

        public WebServiceResponseList[] newArray(int size) {
            return (new WebServiceResponseList[size]);
        }

    }
            ;
    private final static long serialVersionUID = -8075637946668434888L;

    protected WebServiceResponseList(Parcel in) {
        this.customField = ((CustomField) in.readValue((CustomField.class.getClassLoader())));
        in.readList(this.returnValue, (ReturnValue.class.getClassLoader()));
        this.isSucceed = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public WebServiceResponseList() {
    }

    /**
     *
     * @param message
     * @param isSucceed
     * @param customField
     * @param returnValue
     */
    public WebServiceResponseList(Object customField, List<T> returnValue, Boolean isSucceed, String message) {
        super();
        this.customField = customField;
        this.returnValue = returnValue;
        this.isSucceed = isSucceed;
        this.message = message;
    }

    public Object getCustomField() {
        return customField;
    }

    public void setCustomField(Object customField) {
        this.customField = customField;
    }

    public WebServiceResponseList withCustomField(Object customField) {
        this.customField = customField;
        return this;
    }

    public List<T> getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(List<T> returnValue) {
        this.returnValue = returnValue;
    }

    public WebServiceResponseList withReturnValue(List<T> returnValue) {
        this.returnValue = returnValue;
        return this;
    }

    public Boolean getIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(Boolean isSucceed) {
        this.isSucceed = isSucceed;
    }

    public WebServiceResponseList withIsSucceed(Boolean isSucceed) {
        this.isSucceed = isSucceed;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WebServiceResponseList withMessage(String message) {
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
