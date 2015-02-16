package aleksey.sheyko.sgbp.ui.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.utils.helpers.Constants;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class AccountFragment extends Fragment {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        mSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.account_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            updateValues();
            navigateToMainScreen();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateValues() {
        String firstName = mFirstNameField.getText().toString();
        String lastName = mLastNameField.getText().toString();
        String email = mEmailField.getText().toString();
        String school = mSchoolField.getText().toString();
        String grade = mGradeField.getText().toString();

        mSharedPrefs.edit()
                .putString("first_name", firstName)
                .putString("last_name", lastName)
                .putString("email", email)
                .putString("school", school)
                .putString("grade", grade)
                .apply();
    }

    private void navigateToMainScreen() {
        CategoriesFragment firstFragment = new CategoriesFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, firstFragment, "By category");
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit().putInt("view_mode", Constants.VIEW_CATEGORIES).apply();
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setTitle("Places");
        getFragmentManager().popBackStack();

        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mFirstNameField.getWindowToken(), 0);
    }
}
