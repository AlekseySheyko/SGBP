package aleksey.sheyko.sgbp.app.activities;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.rest.ApiService;
import aleksey.sheyko.sgbp.rest.RestClient;
import aleksey.sheyko.sgbp.rest.SchoolsXmlParser;
import aleksey.sheyko.sgbp.rest.SchoolsXmlParser.School;
import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterActivity extends Activity {

    private SharedPreferences mSharedPrefs;

    @InjectView(R.id.firstName)
    EditText mFirstNameField;
    @InjectView(R.id.lastName)
    EditText mLastNameField;
    @InjectView(R.id.email)
    EditText mEmailField;
    @InjectView(R.id.school)
    Spinner mSchoolSpinner;
    @InjectView(R.id.grade)
    Spinner mGradeSpinner;
    @InjectView(R.id.age)
    CheckBox mCheckBoxAge;
    @InjectView(R.id.notifications)
    CheckBox mCheckBoxNotifications;
    @InjectView(R.id.location)
    CheckBox mCheckBoxLocation;
    @InjectView(R.id.coupons)
    CheckBox mCheckBoxCoupons;
    @InjectView(R.id.multipleGrade)
    CheckBox mCheckBoxLevel;

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

    List<String> mSchoolList;

    @Override protected void onResume() {
        super.onResume();
        if (mSchoolList == null) {
            loadSchoolsListFromNetwork();
        }
        String firstName = mSharedPrefs.getString("first_name", "");
        String lastName = mSharedPrefs.getString("last_name", "");
        String email = mSharedPrefs.getString("email", "");
        boolean is18 = mSharedPrefs.getBoolean("is18", true);
        boolean isMultiGrade = mSharedPrefs.getBoolean("multipleLevel", false);
        boolean getNotifications = mSharedPrefs.getBoolean("notifications", true);
        boolean trackLocation = mSharedPrefs.getBoolean("location", true);
        boolean receiveCoupons = mSharedPrefs.getBoolean("coupons", true);

        if (!firstName.isEmpty()) {
            mFirstNameField.setText(firstName);
        }
        if (!lastName.isEmpty()) {
            mLastNameField.setText(lastName);
        }
        if (!email.isEmpty()) {
            mEmailField.setText(email);
        }
        mCheckBoxAge.setChecked(is18);
        mCheckBoxLevel.setChecked(isMultiGrade);
        mCheckBoxNotifications.setChecked(getNotifications);
        mCheckBoxLocation.setChecked(trackLocation);
        mCheckBoxCoupons.setChecked(receiveCoupons);
    }

    @Override protected void onPause() {
        super.onPause();

        String firstName = mFirstNameField.getText().toString();
        String lastName = mLastNameField.getText().toString();
        String email = mEmailField.getText().toString();
        int schoolId = mSchoolSpinner.getSelectedItemPosition();
        int gradeId = mGradeSpinner.getSelectedItemPosition();
        boolean is18 = mCheckBoxAge.isChecked();
        boolean isMultiGrade = mCheckBoxLevel.isChecked();
        boolean getNotifications = mCheckBoxNotifications.isChecked();
        boolean trackLocation = mCheckBoxLocation.isChecked();
        boolean receiveCoupons = mCheckBoxCoupons.isChecked();

        mSharedPrefs.edit()
                .putString("first_name", firstName)
                .putString("last_name", lastName)
                .putString("email", email)
                .putInt("school_id", schoolId)
                .putInt("grade_id", gradeId)
                .putBoolean("is18", is18)
                .putBoolean("notifications", getNotifications)
                .putBoolean("location", trackLocation)
                .putBoolean("coupons", receiveCoupons)
                .putBoolean("multipleLevel", isMultiGrade)
                .apply();
    }

    private void loadSchoolsListFromNetwork() {
        ApiService service = new RestClient().getApiService();
        service.listSchools(new ResponseCallback() {
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

                    int schoolId = mSharedPrefs.getInt("school_id", -1);
                    if (schoolId == -1) {
                        mSchoolSpinner.setSelection(schoolAdapter.getCount());
                    } else {
                        mSchoolSpinner.setSelection(schoolId);
                    }

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
                    gradeAdapter.add("Grade level");

                    mGradeSpinner = (Spinner) findViewById(R.id.grade);
                    mGradeSpinner.setAdapter(gradeAdapter);

                    int gradeId = mSharedPrefs.getInt("grade_id", -1);
                    if (gradeId == -1) {
                        mGradeSpinner.setSelection(gradeAdapter.getCount());
                    } else {
                        mGradeSpinner.setSelection(gradeId);
                    }
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
        hideKeyboard();

        EditText firstNameField = (EditText) findViewById(R.id.firstName);
        EditText lastNameField = (EditText) findViewById(R.id.lastName);
        EditText emailField = (EditText) findViewById(R.id.email);

        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String email = emailField.getText().toString();
        int schoolId = mSchoolSpinner.getSelectedItemPosition();
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
        if (mSchoolSpinner.getSelectedItem().equals("School")) {
            Toast.makeText(this, "Please select your school", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mGradeSpinner.getSelectedItem().equals("Grade level")) {
            Toast.makeText(this, "Please select your grade level", Toast.LENGTH_SHORT).show();
            return;
        }
        setProgressBarIndeterminateVisibility(true);

        try {
            // register(firstName, lastName, email, schoolId, grade);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RegisterActivity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(String firstName, String lastName, String email, int schoolId, String grade) throws Exception {

        final int USER_TYPE = 1;
        final boolean IS_REGISTERED = true;

        String deviceId = getDeviceId();
        int id = randInt(1000, 9999);
        boolean is18 = ((CheckBox) findViewById(R.id.age)).isChecked();
        boolean isMultiGrade = ((CheckBox) findViewById(R.id.multipleGrade)).isChecked();
        boolean getNotifications = ((CheckBox) findViewById(R.id.notifications)).isChecked();
        boolean trackLocation = ((CheckBox) findViewById(R.id.location)).isChecked();
        boolean receiveCoupons = ((CheckBox) findViewById(R.id.coupons)).isChecked();

        ApiService service = new RestClient().getApiService();
        service.register(deviceId, id, firstName, lastName, deviceId, schoolId, email, USER_TYPE, isMultiGrade,
                IS_REGISTERED, receiveCoupons, getNotifications, trackLocation, is18, IS_REGISTERED, new Callback<Response>() {
                    @Override public void success(Response response, Response response2) {
                        Log.i("TAG", response.getBody().toString());

                        mSharedPrefs.edit().putBoolean("registered", true).apply();
                        setProgressBarIndeterminateVisibility(false);
                        navigateToMainScreen();
                    }

                    @Override public void failure(RetrofitError e) {
                        e.printStackTrace();
                    }
                });
    }

    private String getDeviceId() {
        return Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSchoolSpinner.getWindowToken(), 0);
    }
}