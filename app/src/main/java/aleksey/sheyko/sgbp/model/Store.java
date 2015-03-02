package aleksey.sheyko.sgbp.model;

import com.orm.SugarRecord;

public class Store extends SugarRecord<Store> {
    private int storeid;
    private String name;
    private String address;
    private String phone;
    private String latitude;
    private String longitude;
    private String category;
    private String geofenceId;
    private float distance;
    private boolean isParticipated;
    private long timeAllowNext;

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

    public String getGeofenceId() {
        return geofenceId;
    }

    public void setGeofenceId(String id) {
        this.geofenceId = id;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public boolean isParticipated() {
        return isParticipated;
    }

    public void setParticipated(boolean isParticipated) {
        this.isParticipated = isParticipated;
    }

    public long getTimeAllowNext() {
        return timeAllowNext;
    }

    public void setTimeAllowNext(long timeAllowNext) {
        this.timeAllowNext = timeAllowNext;
    }
}
