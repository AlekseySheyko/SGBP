package aleksey.sheyko.sgbp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;

import java.io.InputStream;
import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.rest.ApiService;
import aleksey.sheyko.sgbp.rest.RestClient;
import aleksey.sheyko.sgbp.rest.SchoolsXmlParser;
import aleksey.sheyko.sgbp.rest.SchoolsXmlParser.School;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
            loadSchoolsListFromNetwork();
        }
    }

    private Spinner mSchoolSpinner;
    private Spinner mGradeSpinner;

    private void loadSchoolsListFromNetwork() {
        ApiService service = new RestClient().getApiService();
        service.getSchoolsList(new ResponseCallback() {
            @Override public void success(Response response) {
                try (InputStream in = response.getBody().in()) {
                    SchoolsXmlParser schoolsXmlParser = new SchoolsXmlParser();
                    List<School> schoolsList = schoolsXmlParser.parse(in);
                    ArrayAdapter<String> schoolAdapter = new ArrayAdapter<String>(
                            RegisterActivity.this, android.R.layout.simple_spinner_item) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            if (position == getCount()) {
                                ((TextView) v.findViewById(android.R.id.text1)).setText("");
                                ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                            }
                            return v;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you don't display last item. It is used as hint.
                        }
                    };
                    schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    for (School school : schoolsList) {
                        schoolAdapter.add(school.name);
                    }
                    schoolAdapter.add("School");

                    mSchoolSpinner = (Spinner) findViewById(R.id.school);
                    mSchoolSpinner.setAdapter(schoolAdapter);
                    mSchoolSpinner.setSelection(schoolAdapter.getCount());

                    ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(
                            RegisterActivity.this, android.R.layout.simple_spinner_item) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            if (position == getCount()) {
                                ((TextView) v.findViewById(android.R.id.text1)).setText("");
                                ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                            }
                            return v;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you don't display last item. It is used as hint.
                        }
                    };
                    gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    String[] gradeStrings = getResources().getStringArray(R.array.grade_levels);
                    for (String gradeString : gradeStrings) {
                        gradeAdapter.add(gradeString);
                    }
                    gradeAdapter.add("Grade");

                    mGradeSpinner = (Spinner) findViewById(R.id.grade);
                    mGradeSpinner.setAdapter(gradeAdapter);
                    mGradeSpinner.setSelection(gradeAdapter.getCount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override public void failure(RetrofitError e) {
                e.printStackTrace();
            }
        });
    }

    private void register() {
        setProgressBarIndeterminateVisibility(true);

        EditText firstNameField = (EditText) findViewById(R.id.firstName);
        EditText lastNameField = (EditText) findViewById(R.id.lastName);
        EditText emailField = (EditText) findViewById(R.id.email);

        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String email = emailField.getText().toString();
        String school = mSchoolSpinner.getSelectedItem().toString();
        String grade = mGradeSpinner.getSelectedItem().toString();

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

        //        client.newCall(request).enqueue(new Callback() {
        //            @Override public void onFailure(Request request, IOException e) {
        //                e.printStackTrace();
        //            }
        //
        //            @Override public void onResponse(Response response) throws IOException {
        //                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        //
        //                Log.i(TAG, response.body().string());
        //
        //                mSharedPrefs.edit().putBoolean("registered", true).apply();
        //                setProgressBarIndeterminateVisibility(false);
        //                navigateToMainScreen();
        //            }
        //        });
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
