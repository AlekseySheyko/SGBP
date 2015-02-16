package aleksey.sheyko.sgbp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import aleksey.sheyko.sgbp.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends Activity {

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
    @InjectView(R.id.checkbox_age)
    CheckBox mCheckBoxAge;
    @InjectView(R.id.checkbox_notifications)
    CheckBox mCheckBoxNotifications;
    @InjectView(R.id.checkbox_location)
    CheckBox mCheckBoxLocation;
    @InjectView(R.id.checkbox_coupons)
    CheckBox mCheckBoxCoupons;
    @InjectView(R.id.checkbox_level)
    CheckBox mCheckBoxLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean isRegistered = mSharedPrefs.getBoolean("registered", false);
//        if (isRegistered) {
//            navigateToMainScreen();
//        }
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

        String firstName = mSharedPrefs.getString("first_name", "");
        String lastName = mSharedPrefs.getString("last_name", "");
        String email = mSharedPrefs.getString("email", "");
        String school = mSharedPrefs.getString("school", "");
        String grade = mSharedPrefs.getString("grade", "");

        if (!firstName.isEmpty()) {
            mFirstNameField.setText(firstName);
        }
        if (!lastName.isEmpty()) {
            mLastNameField.setText(lastName);
        }
        if (!email.isEmpty()) {
            mEmailField.setText(email);
        }
        if (!school.isEmpty()) {
            mSchoolField.setText(school);
        }
        if (!grade.isEmpty()) {
            mGradeField.setText(grade);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (item.getItemId() == R.id.action_send) {
            register();
            return true;
        }
        return false;
    }

    private void register() {
        mSharedPrefs.edit()
                .putBoolean("registered", true).apply();
        updateValues();
        navigateToMainScreen();
    }

    public void register(View v) {
        register();
    }

    private void updateValues() {
        String firstName = mFirstNameField.getText().toString();
        String lastName = mLastNameField.getText().toString();
        String email = mEmailField.getText().toString();
        String school = mSchoolField.getText().toString();
        String grade = mGradeField.getText().toString();
        boolean is18 = mCheckBoxAge.isChecked();
        boolean notifications = mCheckBoxNotifications.isChecked();
        boolean location = mCheckBoxLocation.isChecked();
        boolean coupons = mCheckBoxCoupons.isChecked();
        boolean multipleLevel = mCheckBoxLevel.isChecked();

        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString("first_name", firstName)
                .putString("last_name", lastName)
                .putString("email", email)
                .putString("school", school)
                .putString("grade", grade)
                .putBoolean("notifications", notifications)
                .putBoolean("location", location)
                .putBoolean("coupons", coupons)
                .putBoolean("multipleLevel", multipleLevel)
                .apply();
    }

    private void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
