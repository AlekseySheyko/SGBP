package aleksey.sheyko.sgbp.model;

import com.orm.SugarRecord;

public class Notification extends SugarRecord<Notification> {
    private String storeName;
    private String date;

    public Notification() {
    }

    public Notification(String storeName, String date) {
        this.storeName = storeName;
        this.date = date;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getDate() {
        return date;
    }
}
