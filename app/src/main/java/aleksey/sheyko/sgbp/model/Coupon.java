package aleksey.sheyko.sgbp.model;

import com.orm.SugarRecord;

public class Coupon extends SugarRecord<Coupon> {
    int storeid;
    String storeName;
    String code;
    String desc;
    String expireDate;

    public Coupon() {
    }

    public Coupon(int storeid, String storeName, String code, String desc, String expireDate) {
        this.storeid = storeid;
        this.storeName = storeName;
        this.code = code;
        this.desc = desc;
        this.expireDate = expireDate;
    }

    public int getStoreid() {
        return storeid;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getExpireDate() {
        return expireDate;
    }
}
