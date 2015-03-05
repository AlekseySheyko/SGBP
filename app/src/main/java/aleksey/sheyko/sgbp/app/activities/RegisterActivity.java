package aleksey.sheyko.sgbp.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.model.Device;
import aleksey.sheyko.sgbp.model.School;
import aleksey.sheyko.sgbp.rest.ApiService;
import aleksey.sheyko.sgbp.rest.DeviceXmlParser;
import aleksey.sheyko.sgbp.rest.RestClient;
import aleksey.sheyko.sgbp.rest.SchoolsXmlParser;
import aleksey.sheyko.sgbp.rest.UserXmlParser;
import aleksey.sheyko.sgbp.rest.UserXmlParser.User;
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
    private ArrayAdapter<String> mGradeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRegistered = mSharedPrefs.getBoolean("registered", false);
        /*
        TODO Uncomment before production
        if (isRegistered) {
            navigateToMainScreen();
        } else {
            checkRegistration();
        }
        */
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

        findViewById(R.id.multigrade_container).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                mCheckBoxLevel.setChecked(!mCheckBoxLevel.isChecked());
            }
        });
        findViewById(R.id.age_container).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                mCheckBoxAge.setChecked(!mCheckBoxAge.isChecked());
            }
        });
        findViewById(R.id.notifications_container).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                mCheckBoxNotifications.setChecked(!mCheckBoxNotifications.isChecked());
            }
        });
        findViewById(R.id.location_container).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                mCheckBoxLocation.setChecked(!mCheckBoxLocation.isChecked());
            }
        });
        findViewById(R.id.coupons_container).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                mCheckBoxCoupons.setChecked(!mCheckBoxCoupons.isChecked());
            }
        });

        disableNameFields();
        mCheckBoxAge.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    disableNameFields();
                } else {
                    enableNameFields();
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.age_dialog_message), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCheckBoxLevel.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showInfoDialog(mCheckBoxLevel);
                }
            }
        });
        mCheckBoxLocation.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    showInfoDialog(mCheckBoxLocation);
                }
            }
        });
        mCheckBoxNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    showInfoDialog(mCheckBoxNotifications);
                }
            }
        });
        mCheckBoxCoupons.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    showInfoDialog(mCheckBoxCoupons);
                }
            }
        });
    }

    private void disableNameFields() {
        mFirstNameField.setEnabled(false);
        mLastNameField.setEnabled(false);
        mFirstNameField.setFocusable(false);
        mLastNameField.setFocusable(false);
        mFirstNameField.setHintTextColor(getResources().getColor(R.color.hint_disabled));
        mLastNameField.setHintTextColor(getResources().getColor(R.color.hint_disabled));
    }

    private void enableNameFields() {
        mFirstNameField.setEnabled(true);
        mLastNameField.setEnabled(true);
        mFirstNameField.setFocusable(true);
        mLastNameField.setFocusable(true);
        mFirstNameField.setHintTextColor(getResources().getColor(R.color.hint_enabled));
        mLastNameField.setHintTextColor(getResources().getColor(R.color.hint_enabled));
    }

    private void checkRegistration() {
        setProgressBarIndeterminateVisibility(true);
        ApiService service = new RestClient().getApiService();
        service.checkRegistration(getDeviceId(), new ResponseCallback() {
            @Override public void success(Response response) {
                try (InputStream in = response.getBody().in()) {
                    UserXmlParser userInfoXmlParser = new UserXmlParser();
                    List<User> userList = userInfoXmlParser.parse(in);
                    if (userList == null) {
                        return;
                    }
                    User user = userList.get(0);
                    mSharedPrefs.edit()
                            .putBoolean("registered", true)
                            .putString("device_id", getDeviceId())
                            .putInt("user_id", user.id)
                            .putString("first_name", user.firstName)
                            .putString("last_name", user.lastName)
                            .putString("email", user.email)
                            .putBoolean("multipleLevel", user.multipleGrade)
                            .putBoolean("notifications", user.notifications)
                            .putBoolean("location", user.location)
                            .putBoolean("coupons", user.coupons)
                            .apply();
                    setProgressBarIndeterminateVisibility(false);
                    navigateToMainScreen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override public void failure(RetrofitError e) {
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    List<School> mSchoolsList;

    @Override protected void onResume() {
        super.onResume();
        mSchoolsList = School.listAll(School.class);
        if (mSchoolsList.size() == 0) {
            loadSchoolsFromNetwork();
        } else {
            setupSpinners();
        }
    }

    @Override protected void onPause() {
        super.onPause();

        String firstName = mFirstNameField.getText().toString();
        String lastName = mLastNameField.getText().toString();
        String email = mEmailField.getText().toString();
        int schoolId = mSchoolSpinner.getSelectedItemPosition();
        int gradeId = mGradeSpinner.getSelectedItemPosition();
        boolean is18 = mCheckBoxAge.isChecked();
        boolean multipleGrade = mCheckBoxLevel.isChecked();
        boolean notifications = mCheckBoxNotifications.isChecked();
        boolean location = mCheckBoxLocation.isChecked();
        boolean coupons = mCheckBoxCoupons.isChecked();

        if (!firstName.isEmpty()) {
            mSharedPrefs.edit().putString("first_name", firstName).apply();
        }
        if (!lastName.isEmpty()) {
            mSharedPrefs.edit().putString("last_name", lastName).apply();
        }
        if (!email.isEmpty()) {
            mSharedPrefs.edit().putString("email", email).apply();
        }
        if (schoolId != -1) {
            mSharedPrefs.edit().putInt("school_id", schoolId).apply();
        }
        if (gradeId != -1) {
            mSharedPrefs.edit().putInt("grade_id", gradeId).apply();
        }
        if (!is18) {
            mSharedPrefs.edit().putBoolean("is18", is18).apply();
        }
        if (!notifications) {
            mSharedPrefs.edit().putBoolean("notifications", notifications).apply();
        }
        if (!location) {
            mSharedPrefs.edit().putBoolean("location", location).apply();
        }
        if (!coupons) {
            mSharedPrefs.edit().putBoolean("coupons", coupons).apply();
        }
        if (multipleGrade) {
            mSharedPrefs.edit().putBoolean("multipleGrade", multipleGrade).apply();
        }
    }

    private void loadSchoolsFromNetwork() {
        ApiService service = new RestClient().getApiService();
        service.listSchools(new ResponseCallback() {
            @Override public void success(Response response) {
                try (InputStream in = response.getBody().in()) {
                    SchoolsXmlParser schoolsXmlParser = new SchoolsXmlParser();
                    schoolsXmlParser.parse(in);
                    mSchoolsList = School.listAll(School.class);
                    setupSpinners();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override public void failure(RetrofitError e) {
                e.printStackTrace();
            }
        });
    }

    private void setupSpinners() {
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
        for (School school : mSchoolsList) {
            schoolAdapter.add(school.getName());
        }
        schoolAdapter.add("School");
        mSchoolSpinner.setAdapter(schoolAdapter);
        mSchoolSpinner.setSelection(schoolAdapter.getCount());

        mGradeAdapter = new ArrayAdapter<String>(
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
        mGradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String[] gradeStrings = getResources().getStringArray(R.array.grade_levels);
        for (String gradeString : gradeStrings) {
            mGradeAdapter.add(gradeString);
        }
        mGradeAdapter.add("Grade level");
        mGradeSpinner.setAdapter(mGradeAdapter);
        mGradeSpinner.setSelection(mGradeAdapter.getCount());
    }

    private void register() {
        hideKeyboard();

        String firstName = mFirstNameField.getText().toString();
        String lastName = mLastNameField.getText().toString();
        String email = mEmailField.getText().toString();
        int schoolId = mSchoolSpinner.getSelectedItemPosition();

        if (!mCheckBoxAge.isChecked()) {
            Toast.makeText(this, "You must be 18 or older to enter program", Toast.LENGTH_SHORT).show();
            return;
        }
        if (schoolId == -1) {
            Toast.makeText(this, "Connect to a network to load school list", Toast.LENGTH_SHORT).show();
        }
        if (mSchoolSpinner.getSelectedItem().equals("School")) {
            Toast.makeText(this, "Please select your school", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mGradeSpinner.getSelectedItem().equals("Grade level")) {
            Toast.makeText(this, "Please select your grade level", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            showError(mEmailField);
            return;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        setProgressBarIndeterminateVisibility(true);

        try {
            register(firstName, lastName, email, schoolId);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RegisterActivity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(final String firstName, final String lastName, final String email, final int schoolId) throws Exception {

        final int USER_TYPE = 1;
        final boolean IS_REGISTERED = true;

        final String deviceId = getDeviceId();
        final String userId = deviceId.replaceAll("[^0-9]", "");
        final boolean is18 = mCheckBoxAge.isChecked();
        final boolean isMultiGrade = mCheckBoxLevel.isChecked();
        final boolean getNotifications = mCheckBoxNotifications.isChecked();
        final boolean trackLocation = mCheckBoxLocation.isChecked();
        final boolean receiveCoupons = mCheckBoxCoupons.isChecked();

        ApiService service = new RestClient().getApiService();
        Device device = new Device(this);
        service.registerDevice(userId, deviceId, device.getModelName(), device.getDeviceType(), device.getManufacturer(),
                device.getModelName(), device.getModelNumber(), device.getModelNumber(), device.getSystemName(),
                device.getSoftwareVersion(), device.getAndroidVersion(), device.getAndroidVersion(),
                device.getDeviceOs(), device.getTimeZone(), device.getLocale(), device.isCameraAvailable(), false, true,
                userId, IS_REGISTERED, userId, new ResponseCallback() {
                    @Override public void success(Response response) {
                        try (InputStream in = response.getBody().in()) {
                            DeviceXmlParser deviceXmlParser = new DeviceXmlParser();
                            int deviceInfoId = deviceXmlParser.parse(in);
                            mSharedPrefs.edit().putInt("device_info_id", deviceInfoId).apply();
                            registerUser(userId, deviceInfoId, firstName, lastName, deviceId, schoolId, email, USER_TYPE, isMultiGrade,
                                    IS_REGISTERED, receiveCoupons, getNotifications, trackLocation, is18, IS_REGISTERED);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override public void failure(RetrofitError e) {
                        e.printStackTrace();
                    }
                });
    }

    private void registerUser(final String userId, int deviceInfoId, String firstName, String lastName, final String deviceId, int schoolId, String email, int USER_TYPE, boolean isMultiGrade, boolean IS_REGISTERED, boolean receiveCoupons, boolean getNotifications, boolean trackLocation, boolean is18, boolean is_registered) {
        ApiService service = new RestClient().getApiService();
        service.registerUser(userId, deviceInfoId, firstName, lastName, deviceId, schoolId, email, USER_TYPE, isMultiGrade,
                IS_REGISTERED, receiveCoupons, getNotifications, trackLocation, is18, IS_REGISTERED, new Callback<Response>() {
                    @Override public void success(Response response, Response response2) {
                        mSharedPrefs.edit()
                                .putBoolean("registered", true)
                                .putString("device_id", deviceId)
                                .putString("user_id", userId)
                                .apply();
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
        Toast.makeText(this, editText.getHint().toString() + " " + error, Toast.LENGTH_SHORT).show();
    }

    public void register(View v) {
        register();
    }

    private void showInfoDialog(CheckBox checkBox) {
        String title = null;
        String message = null;
        switch (checkBox.getId()) {
            case R.id.multipleGrade:
                title = getResources().getString(R.string.multigrade_dialog_title);
                message = getResources().getString(R.string.multigrade_dialog_message);
                break;
            case R.id.notifications:
                title = getResources().getString(R.string.notifications_dialog_title);
                message = getResources().getString(R.string.notifications_dialog_message);
                break;
            case R.id.location:
                title = getResources().getString(R.string.location_dialog_title);
                message = getResources().getString(R.string.location_dialog_message);
                break;
            case R.id.coupons:
                title = getResources().getString(R.string.coupons_dialog_title);
                message = getResources().getString(R.string.coupons_dialog_message);
                break;
        }
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSchoolSpinner.getWindowToken(), 0);
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}