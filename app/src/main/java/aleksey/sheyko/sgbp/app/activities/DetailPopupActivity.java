package aleksey.sheyko.sgbp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.model.Store;

public class DetailPopupActivity extends Activity {

    private static final long PARTICIPATE_INTERVAL = 15 * 60 * 1000;

    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dialog);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        int storeId = mSharedPrefs.getInt("storeId", -1);
        final Store currentStore = Store.find(Store.class, "storeid = ?", String.valueOf(storeId)).get(0);

        final String name = currentStore.getName();
        String address = currentStore.getAddress();
        String phone = currentStore.getPhone();

        ((TextView) findViewById(R.id.details_header)).setText(name);
        ((TextView) findViewById(R.id.address)).setText(address);
        ((TextView) findViewById(R.id.phone)).setText(phone);

        if (address.isEmpty()) {
            ((TextView) findViewById(R.id.address)).setText(getString(R.string.unspecified));
            findViewById(R.id.button_map).setVisibility(View.GONE);
            findViewById(R.id.address_container).setClickable(false);
        }
        if (phone.isEmpty()) {
            ((TextView) findViewById(R.id.phone)).setText(getString(R.string.unspecified));
            findViewById(R.id.button_phone).setVisibility(View.GONE);
            findViewById(R.id.phone_container).setClickable(false);
        }
    }

    private void navigateToMap() {
        String name = mSharedPrefs.getString("name", "");
        double latitude = Double.parseDouble(mSharedPrefs.getString("latitude", ""));
        double longitude = Double.parseDouble(mSharedPrefs.getString("longitude", ""));

        startActivity(new Intent(DetailPopupActivity.this, MapPane.class)
                .putExtra("name", name)
                .putExtra("latitude", latitude)
                .putExtra("longitude", longitude));
    }

    public void showMap(View view) {
        navigateToMap();
    }

    public void dial(View view) {
        String phone = mSharedPrefs.getString("phone", "");
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone.replaceAll("[^0-9]", "")));
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }
}
