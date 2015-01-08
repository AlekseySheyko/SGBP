package aleksey.sheyko.sgbp.models;

import com.orm.SugarRecord;

public class Store extends SugarRecord<Store> {
    private int storeid;
    private String name;
    private String address;
    private String phone;
    private String latitude;
    private String longitude;
    private String category;

    public Store() {
    }

    public Store(int storeid, String name, String address, String phone, String latitude, String longitude, String category) {
        this.storeid = storeid;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
    }

    public int getStoreid() {
        return storeid;
    }

    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
