
package id.co.indocyber.android.starbridges.model.BeaconData;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import id.co.indocyber.android.starbridges.StarbridgesApplication;

@Table(database = StarbridgesApplication.class)
@org.parceler.Parcel(analyze = ReturnValue.class)
public class ReturnValue extends BaseModel implements Serializable, Parcelable
{

    @SerializedName("BeaconID")
    @Expose
    @PrimaryKey
    private Integer beaconID;
    @SerializedName("UUID")
    @Expose
    @Column
    private String uUID;
    @SerializedName("Major")
    @Expose
    @Column
    private String major;
    @SerializedName("Minor")
    @Expose
    @Column
    private String minor;
    @SerializedName("LocationID")
    @Expose
    @Column
    private Integer locationID;
    @SerializedName("LocationName")
    @Expose
    @Column
    private String locationName;
    @SerializedName("LocationAddress")
    @Expose
    @Column
    private String locationAddress;
    @SerializedName("Latitude")
    @Expose
    @Column
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    @Column
    private String longitude;
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
    private final static long serialVersionUID = -5768902457116689976L;

    protected ReturnValue(Parcel in) {
        this.beaconID = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.uUID = ((String) in.readValue((String.class.getClassLoader())));
        this.major = ((String) in.readValue((String.class.getClassLoader())));
        this.minor = ((String) in.readValue((String.class.getClassLoader())));
        this.locationID = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.locationName = ((String) in.readValue((String.class.getClassLoader())));
        this.locationAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ReturnValue() {
    }

    /**
     * 
     * @param minor
     * @param beaconID
     * @param uUID
     * @param longitude
     * @param locationID
     * @param latitude
     * @param locationName
     * @param locationAddress
     * @param major
     */
    public ReturnValue(Integer beaconID, String uUID, String major, String minor, Integer locationID, String locationName, String locationAddress, String latitude, String longitude) {
        super();
        this.beaconID = beaconID;
        this.uUID = uUID;
        this.major = major;
        this.minor = minor;
        this.locationID = locationID;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(Integer beaconID) {
        this.beaconID = beaconID;
    }

    public ReturnValue withBeaconID(Integer beaconID) {
        this.beaconID = beaconID;
        return this;
    }

    public String getUUID() {
        return uUID;
    }

    public void setUUID(String uUID) {
        this.uUID = uUID;
    }

    public ReturnValue withUUID(String uUID) {
        this.uUID = uUID;
        return this;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public ReturnValue withMajor(String major) {
        this.major = major;
        return this;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public ReturnValue withMinor(String minor) {
        this.minor = minor;
        return this;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public ReturnValue withLocationID(Integer locationID) {
        this.locationID = locationID;
        return this;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public ReturnValue withLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public ReturnValue withLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
        return this;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public ReturnValue withLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ReturnValue withLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(beaconID);
        dest.writeValue(uUID);
        dest.writeValue(major);
        dest.writeValue(minor);
        dest.writeValue(locationID);
        dest.writeValue(locationName);
        dest.writeValue(locationAddress);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
    }

    public int describeContents() {
        return  0;
    }

}
