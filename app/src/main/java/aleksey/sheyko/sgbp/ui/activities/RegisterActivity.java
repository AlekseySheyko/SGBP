package aleksey.sheyko.sgbp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        //        if (mSharedPrefs.getBoolean("registered", false)) {
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

    public void register(View view) {
        mSharedPrefs.edit()
                .putBoolean("registered", true).apply();
        updateValues();
        navigateToMainScreen();
    }

    private void updateValues() {
        String firstName = mFirstNameField.getText().toString();
        String lastName = mLastNameField.getText().toString();
        String email = mEmailField.getText().toString();
        String school = mSchoolField.getText().toString();
        String grade = mGradeField.getText().toString();

        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString("first_name", firstName)
                .putString("last_name", lastName)
                .putString("email", email)
                .putString("school", school)
                .putString("grade", grade)
                .apply();
    }

    private void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
