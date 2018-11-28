
package id.co.indocyber.android.starbridges.model.ListDraftEntertainReimbursement;

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
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("IsPreProcess")
    @Expose
    private boolean isPreProcess;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("EncryptedAmount")
    @Expose
    private String encryptedAmount;
    @SerializedName("Amount")
    @Expose
    private int amount;
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
    private final static long serialVersionUID = 3420393460793448544L;

    protected ReturnValue(Parcel in) {
        this.iD = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.isPreProcess = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.encryptedAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.amount = ((int) in.readValue((int.class.getClassLoader())));
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
     * @param amount
     * @param accessibilityAttribute
     * @param fullAccess
     * @param description
     * @param encryptedAmount
     * @param isPreProcess
     * @param type
     * @param iD
     * @param exclusionFields
     */
    public ReturnValue(String iD, String type, boolean isPreProcess, String description, String encryptedAmount, int amount, boolean fullAccess, List<Object> exclusionFields, String accessibilityAttribute) {
        super();
        this.iD = iD;
        this.type = type;
        this.isPreProcess = isPreProcess;
        this.description = description;
        this.encryptedAmount = encryptedAmount;
        this.amount = amount;
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
        dest.writeValue(type);
        dest.writeValue(isPreProcess);
        dest.writeValue(description);
        dest.writeValue(encryptedAmount);
        dest.writeValue(amount);
        dest.writeValue(fullAccess);
        dest.writeList(exclusionFields);
        dest.writeValue(accessibilityAttribute);
    }

    public int describeContents() {
        return  0;
    }

}
