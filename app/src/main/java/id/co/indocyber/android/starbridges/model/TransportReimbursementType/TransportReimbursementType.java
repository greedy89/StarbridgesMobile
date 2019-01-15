package id.co.indocyber.android.starbridges.model.TransportReimbursementType;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransportReimbursementType {
    @SerializedName("Text")
    @Expose
    private String text;

    @SerializedName("Value")
    @Expose
    private String value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return text;
    }
}
