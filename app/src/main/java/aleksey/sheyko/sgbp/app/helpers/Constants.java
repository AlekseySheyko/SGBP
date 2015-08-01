package aleksey.sheyko.sgbp.app.helpers;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String CATEGORY_MOBILE = "Mobile Businesses";
    public static final String CATEGORY_EDUCATION = "Arts + Education";
    public static final String CATEGORY_AUTO = "Automotive";
    public static final String CATEGORY_FOOD = "Restaurants/Food";
    public static final String CATEGORY_SERVICES = "Services";
    public static final String CATEGORY_SHOPPING = "Shopping";
    public static final String CATEGORY_SPORTS = "Sports/Entertainment";

    public static final int VIEW_CATEGORIES = -1;
    public static final int VIEW_NEAREST = 0;
    public static final int VIEW_NOTIFICATIONS = 1;
    public static final int VIEW_COUPONS = 2;

    public Map<String, String> categories;

    public Constants() {
        categories = new HashMap<>();
        categories.put("0", Constants.CATEGORY_MOBILE);
        categories.put("1", Constants.CATEGORY_EDUCATION);
        categories.put("2", Constants.CATEGORY_AUTO);
        categories.put("3", Constants.CATEGORY_FOOD);
        categories.put("4", Constants.CATEGORY_SERVICES);
        categories.put("5", Constants.CATEGORY_SHOPPING);
        categories.put("6", Constants.CATEGORY_SPORTS);
    }
}
