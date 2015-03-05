package aleksey.sheyko.sgbp.model;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Device {

    private static final String MODEL_NUMBER = "1";
    private static final String SYSTEM_NAME = "Android";
    private static final String SOFTWARE_VERSION = "1.0";

    private Context mContext;

    public Device(Context context) {
        mContext = context;
    }

    public String getDeviceType() {
        return "Phone";
    }

    public boolean isCameraAvailable() {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public String getModelName() {
        return Build.MODEL;
    }

    public String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getTimeZone() {
        return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
    }

    public String getLocale() {
        return Locale.getDefault().getDisplayName();
    }

    public String getStartDate() {
        return new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'", Locale.US).format(new Date());
    }

    public String getEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 1);
        return new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'", Locale.US).format(calendar.getTime());
    }

    public String getModelNumber() {
        return MODEL_NUMBER;
    }

    public String getSystemName() {
        return SYSTEM_NAME;
    }

    public String getSoftwareVersion() {
        return SOFTWARE_VERSION;
    }

    public String getDeviceOs() {
        return SYSTEM_NAME;
    }
}
