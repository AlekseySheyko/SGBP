package aleksey.sheyko.sgbp.utils.helpers;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String CATEGORY_FOOD = "Food & drinks";
    public static final String CATEGORY_AUTO = "Auto services";
    public static final String CATEGORY_SOUND = "Sound systems";
    public static final String CATEGORY_BODY_CARE = "Body care";
    public static final String CATEGORY_HOTELS = "Hotels";
    public static final String CATEGORY_OTHER = "Other";
    public static final String CATEGORY_MOBILE = "Mobile";

    public static final int VIEW_CATEGORIES = -1;
    public static final int VIEW_NEAREST = 0;
    public static final int VIEW_NOTIFICATIONS = 1;
    public static final int VIEW_COUPONS = 2;

    public Map<String, String> categories;

    public Constants() {
        categories = new HashMap<>();
        categories.put("0", Constants.CATEGORY_FOOD);
        categories.put("1", Constants.CATEGORY_AUTO);
        categories.put("2", Constants.CATEGORY_SOUND);
        categories.put("3", Constants.CATEGORY_BODY_CARE);
        categories.put("4", Constants.CATEGORY_HOTELS);
        categories.put("5", Constants.CATEGORY_OTHER);
        categories.put("6", Constants.CATEGORY_MOBILE);
    }
}
