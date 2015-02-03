package aleksey.sheyko.sgbp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import aleksey.sheyko.sgbp.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends Activity {

    private SharedPreferences mSharedPrefs;

    @InjectView(R.id.nameField)
    EditText mNameField;
    @InjectView(R.id.emailField)
    EditText mEmailField;
    @InjectView(R.id.schoolField)
    EditText mSchoolField;
    @InjectView(R.id.classField)
    EditText mClassField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRegistered = mSharedPrefs.getBoolean("is_registered", false);
        if (isRegistered) {
            navigateToMainScreen();
        } else {
            setContentView(R.layout.activity_register);
            ButterKnife.inject(this);
        }
    }

    public void register(View view) {
        String mName = mNameField.getText().toString();
        String mEmail = mEmailField.getText().toString();
        String mSchool = mSchoolField.getText().toString();
        String mClass = mClassField.getText().toString();

        if (mName.isEmpty() || mEmail.isEmpty() || mSchool.isEmpty() || mClass.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        mSharedPrefs.edit().putBoolean("is_registered", true).apply();
        navigateToMainScreen();
    }

    public void login(View view) {
        navigateToMainScreen();
    }

    private void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
