
package id.co.indocyber.android.starbridges.model.LoanSchedule;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import id.co.indocyber.android.starbridges.network.StringConverter;

public class ReturnValue implements Serializable, Parcelable
{

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private Object name;
    @SerializedName("Installment")
    @Expose
    private Object installment;
    @SerializedName("ProcessMonth")
    @Expose
    private Integer processMonth;
    @SerializedName("ProcessYear")
    @Expose
    private Integer processYear;
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
    private final static long serialVersionUID = -7263321359087147160L;

    protected ReturnValue(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((Object) in.readValue((Object.class.getClassLoader())));
        this.installment = ((Object) in.readValue((Object.class.getClassLoader())));
        this.processMonth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.processYear = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ReturnValue() {
    }

    /**
     * 
     * @param processYear
     * @param installment
     * @param id
     * @param processMonth
     * @param name
     */
    public ReturnValue(Integer id, Object name, Object installment, Integer processMonth, Integer processYear) {
        super();
        this.id = id;
        this.name = name;
        this.installment = installment;
        this.processMonth = processMonth;
        this.processYear = processYear;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ReturnValue withId(Integer id) {
        this.id = id;
        return this;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public ReturnValue withName(Object name) {
        this.name = name;
        return this;
    }

    public Object getInstallment() {
        return installment;
    }

    public void setInstallment(Object installment) {
        this.installment = installment;
    }

    public ReturnValue withInstallment(Object installment) {
        this.installment = installment;
        return this;
    }

    public Integer getProcessMonth() {
        return processMonth;
    }

    public void setProcessMonth(Integer processMonth) {
        this.processMonth = processMonth;
    }

    public ReturnValue withProcessMonth(Integer processMonth) {
        this.processMonth = processMonth;
        return this;
    }

    public Integer getProcessYear() {
        return processYear;
    }

    public void setProcessYear(Integer processYear) {
        this.processYear = processYear;
    }

    public ReturnValue withProcessYear(Integer processYear) {
        this.processYear = processYear;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(installment);
        dest.writeValue(processMonth);
        dest.writeValue(processYear);
    }

    public int describeContents() {
        return  0;
    }



    @Override
    public String toString() {
        StringConverter stringConverter=new StringConverter();
        if(processMonth==null || processYear ==null)
            return "";
        return stringConverter.int2MonthName(processMonth-1)+ " " + processYear;
    }
}
