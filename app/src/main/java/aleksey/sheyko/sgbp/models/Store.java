package aleksey.sheyko.sgbp.models;

import java.io.Serializable;

public class Store implements Serializable {
    private int mId = -1;
    private String mName;
    private String mAddress;
    private String mPhone;
    private String mLatitude;
    private String mLongitude;
    private String mCategory;

    public Store() {
    }
    public Store(int id, String name, String address, String phone, String latitude, String longitude, String category) {
        mId = id;
        mName = name;
        mAddress = address;
        mPhone = phone;
        mLatitude = latitude;
        mLongitude = longitude;
        mCategory = category;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

}
