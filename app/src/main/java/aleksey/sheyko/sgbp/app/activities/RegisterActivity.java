package aleksey.sheyko.sgbp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import aleksey.sheyko.sgbp.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends Activity {

    protected static final String TAG = RegisterActivity.class.getSimpleName();

    private SharedPreferences mSharedPrefs;

    @InjectView(R.id.firstNameField)
    EditText mFirstNameField;
    @InjectView(R.id.lastNameField)
    EditText mLastNameField;
    @InjectView(R.id.emailField)
    EditText mEmailField;
    @InjectView(R.id.schoolField)
    EditText mSchoolField;
    @InjectView(R.id.gradeField)
    EditText mGradeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRegistered = mSharedPrefs.getBoolean("registered", false);
        if (isRegistered) {
            navigateToMainScreen();
        }
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
    }

    @Override protected void onResume() {
        super.onResume();
        listSchools();
    }

    private void listSchools() {

    }

    private final OkHttpClient client = new OkHttpClient();

    private void register() {
        setProgressBarIndeterminateVisibility(true);

        String firstName = mFirstNameField.getText().toString();
        String lastName = mLastNameField.getText().toString();
        String email = mEmailField.getText().toString();
        String school = mSchoolField.getText().toString();
        String grade = mGradeField.getText().toString();

        if (firstName.isEmpty()) {
            showError(mFirstNameField);
            return;
        }
        if (lastName.isEmpty()) {
            showError(mLastNameField);
            return;
        }
        if (email.isEmpty()) {
            showError(mEmailField);
            return;
        }
        if (school.isEmpty()) {
            showError(mSchoolField);
            return;
        }
        if (grade.isEmpty()) {
            showError(mGradeField);
            return;
        }

        try {
            register(firstName, lastName, email, school, grade);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RegisterActivity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(String firstName, String lastName, String email, String school, String grade) throws Exception {

        String deviceId = getDeviceId();
        boolean is18 = ((CheckBox) findViewById(R.id.checkbox_age)).isChecked();
        boolean isMultiGrade = ((CheckBox) findViewById(R.id.checkbox_level)).isChecked();
        boolean notify = ((CheckBox) findViewById(R.id.checkbox_notifications)).isChecked();
        boolean trackLocation = ((CheckBox) findViewById(R.id.checkbox_location)).isChecked();
        boolean receiveCoupons = ((CheckBox) findViewById(R.id.checkbox_coupons)).isChecked();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("www.test.sgbp.info.com")
                .appendPath("SGBPWS.asmx")
                .appendQueryParameter("Key", deviceId)
                .appendQueryParameter("Device_Info_Id", deviceId)
                .appendQueryParameter("First_Name", firstName)
                .appendQueryParameter("Last_Name", firstName)
                .appendQueryParameter("Device_UID", deviceId)
                .appendQueryParameter("Device_UID", deviceId)
        ;
        String registerUrl = builder.toString();

        Request request = new Request.Builder()
                .url(registerUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Log.i(TAG, response.body().string());

                mSharedPrefs.edit().putBoolean("registered", true).apply();
                setProgressBarIndeterminateVisibility(false);
                navigateToMainScreen();
            }
        });
    }

    public String getDeviceId() {
        return Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        register();
        return true;
    }

    private void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void showError(EditText editText) {
        String error = getResources().getString(R.string.empty_field_error);
        editText.setError(
                editText.getHint().toString() + " " + error);
    }

    public void register(View v) {
        register();
    }
}
