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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.rest.SchoolsXmlParser;
import aleksey.sheyko.sgbp.rest.SchoolsXmlParser.School;

public class RegisterActivity extends Activity {

    private SharedPreferences mSharedPrefs;

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
    }

    List<String> mSchoolList;

    @Override protected void onResume() {
        super.onResume();
        if (mSchoolList == null) {
            try {
                loadSchoolsFromNetwork();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final OkHttpClient client = new OkHttpClient();

    private void loadSchoolsFromNetwork() throws Exception {
        String schoolsUrl = "http://test.sgbp.info/SGBPWS.asmx/GetSchoolName";

        Request request = new Request.Builder()
                .url(schoolsUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                try (InputStream in = response.body().byteStream()) {
                    SchoolsXmlParser schoolsXmlParser = new SchoolsXmlParser();
                    List<School> schoolsList = schoolsXmlParser.parse(in);
                    String[] schoolNames = new String[schoolsList.size()];
                    for (int i = 0; i < schoolsList.size(); i++) {
                        schoolNames[i] = schoolsList.get(i).name;
                    }

                    Spinner schoolSpinner = (Spinner) findViewById(R.id.school);
                    schoolSpinner.setAdapter(new ArrayAdapter<>(
                            RegisterActivity.this,
                            android.R.layout.simple_spinner_item,
                            schoolNames
                    ));
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void register() {
        setProgressBarIndeterminateVisibility(true);

        EditText firstNameField = (EditText) findViewById(R.id.firstName);
        EditText lastNameField = (EditText) findViewById(R.id.lastName);
        EditText emailField = (EditText) findViewById(R.id.email);
        EditText schoolField = (EditText) findViewById(R.id.school);
        EditText gradeField = (EditText) findViewById(R.id.gradeField);

        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String email = emailField.getText().toString();
        String school = schoolField.getText().toString();
        String grade = gradeField.getText().toString();

        if (firstName.isEmpty()) {
            showError(firstNameField);
            return;
        }
        if (lastName.isEmpty()) {
            showError(lastNameField);
            return;
        }
        if (email.isEmpty()) {
            showError(emailField);
            return;
        }
        if (school.isEmpty()) {
            showError(schoolField);
            return;
        }
        if (grade.isEmpty()) {
            showError(gradeField);
            return;
        }

        try {
            register(firstName, lastName, email, school, grade);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RegisterActivity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
        }
    }

    protected static final String TAG = RegisterActivity.class.getSimpleName();

    public void register(String firstName, String lastName, String email, String school, String grade) throws Exception {

        String deviceId = getDeviceId();
        boolean is18 = ((CheckBox) findViewById(R.id.checkbox_age)).isChecked();
        boolean isMultiGrade = ((CheckBox) findViewById(R.id.checkbox_level)).isChecked();
        boolean getNotifications = ((CheckBox) findViewById(R.id.checkbox_notifications)).isChecked();
        boolean trackLocation = ((CheckBox) findViewById(R.id.checkbox_location)).isChecked();
        boolean receiveCoupons = ((CheckBox) findViewById(R.id.checkbox_coupons)).isChecked();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("test.sgbp.info")
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
