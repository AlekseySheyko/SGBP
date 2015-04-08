package aleksey.sheyko.sgbp.app.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.app.helpers.Constants;
import aleksey.sheyko.sgbp.app.helpers.MultiSpinner;
import aleksey.sheyko.sgbp.model.Grade;
import aleksey.sheyko.sgbp.model.School;
import aleksey.sheyko.sgbp.rest.ApiService;
import aleksey.sheyko.sgbp.rest.GradesXmlParser;
import aleksey.sheyko.sgbp.rest.RestClient;
import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountFragment extends Fragment
        implements MultiSpinner.MultiSpinnerListener {

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
    MultiSpinner mGradeSpinner;
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

    private List<School> mSchoolsList;
    private List<Grade> mGradesList;
    private ArrayAdapter<String> mSchoolAdapter;

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

        view.findViewById(R.id.multigrade_container).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                mCheckBoxLevel.setChecked(!mCheckBoxLevel.isChecked());
            }
        });
        view.findViewById(R.id.age_container).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                mCheckBoxAge.setChecked(!mCheckBoxAge.isChecked());
            }
        });
        view.findViewById(R.id.notifications_container).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                mCheckBoxNotifications.setChecked(!mCheckBoxNotifications.isChecked());
            }
        });
        view.findViewById(R.id.location_container).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                mCheckBoxLocation.setChecked(!mCheckBoxLocation.isChecked());
            }
        });
        view.findViewById(R.id.coupons_container).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                mCheckBoxCoupons.setChecked(!mCheckBoxCoupons.isChecked());
            }
        });

        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item
        );
        mSchoolsList = School.listAll(School.class);
        setupSpinners();

        mSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        String firstName = mSharedPrefs.getString("first_name", "");
        String lastName = mSharedPrefs.getString("last_name", "");
        String email = mSharedPrefs.getString("email", "");
        int schoolId = mSharedPrefs.getInt("school_id", -1);
        int gradeId = mSharedPrefs.getInt("grade_id", -1);
        boolean is18 = mSharedPrefs.getBoolean("is18", true);
        boolean isMultiGrade = mSharedPrefs.getBoolean("multipleLevel", false);
        boolean notifications = mSharedPrefs.getBoolean("notifications", true);
        boolean location = mSharedPrefs.getBoolean("location", true);
        boolean coupons = mSharedPrefs.getBoolean("coupons", true);

        if (!firstName.isEmpty()) {
            mFirstNameField.setText(firstName);
        }
        if (!lastName.isEmpty()) {
            mLastNameField.setText(lastName);
        }
        if (!email.isEmpty()) {
            mEmailField.setText(email);
        }
        if (schoolId != -1) {
            mSchoolSpinner.setSelection(schoolId);
        }
        if (gradeId != -1) {
            mGradeSpinner.setSelection(gradeId);
        }
        mCheckBoxAge.setChecked(is18);
        mCheckBoxNotifications.setChecked(notifications);
        mCheckBoxLocation.setChecked(location);
        mCheckBoxCoupons.setChecked(coupons);
        mCheckBoxLevel.setChecked(isMultiGrade);
    }

    private void setupSpinners() {
        mSchoolAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you don't display last item. It is used as hint.
            }
        };
        mSchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (School school : mSchoolsList) {
            mSchoolAdapter.add(school.getName());
        }
        mSchoolAdapter.add("School");
        mSchoolSpinner.setAdapter(mSchoolAdapter);
        mSchoolSpinner.setSelection(mSchoolAdapter.getCount());

        mSchoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSchoolSpinner.getSelectedItemPosition() == mSchoolAdapter.getCount()) {
                    mGradeSpinner.setEnabled(false);
                } else {
                    mGradeSpinner.setEnabled(true);

                    ApiService service = new RestClient().getApiService();
                    String userId = getDeviceId().replaceAll("[^0-9]", "");
                    service.getGrade(userId, position + 1, new ResponseCallback() {
                        @Override
                        public void success(Response response) {
                            try {
                                InputStream in = response.getBody().in();
                                GradesXmlParser gradesXmlParser = new GradesXmlParser();
                                gradesXmlParser.parse(in);
                                mGradesList = Grade.listAll(Grade.class);
                                List<String> gradeStrings = new ArrayList<>();
                                for (Grade grade : mGradesList) {
                                    gradeStrings.add(grade.getName());
                                }
                                mGradeSpinner.setItems(gradeStrings, "Grade level",
                                        AccountFragment.this, true);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void update() {
        hideKeyboard();

        String firstName = mFirstNameField.getText().toString();
        String lastName = mLastNameField.getText().toString();
        String email = mEmailField.getText().toString();
        int schoolId = mSchoolSpinner.getSelectedItemPosition();

        try {
            update(firstName, lastName, schoolId);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to update values", Toast.LENGTH_SHORT).show();
        }
    }

    public void update(String firstName, String lastName, int schoolId) throws Exception {

        final boolean IS_REGISTERED = true;

        String deviceId = mSharedPrefs.getString("device_id", "");
        int userId = mSharedPrefs.getInt("user_id", -1);
        boolean is18 = mCheckBoxAge.isChecked();
        boolean isMultiGrade = mCheckBoxLevel.isChecked();
        boolean getNotifications = mCheckBoxNotifications.isChecked();
        boolean trackLocation = mCheckBoxLocation.isChecked();
        boolean receiveCoupons = mCheckBoxCoupons.isChecked();

        ApiService service = new RestClient().getApiService();
        service.update(deviceId, firstName, lastName, schoolId, isMultiGrade, IS_REGISTERED,
                receiveCoupons, getNotifications, trackLocation, is18, userId, new ResponseCallback() {
                    @Override public void success(Response response) {
                        navigateToMainScreen();
                    }

                    @Override public void failure(RetrofitError e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override public void onResume() {
        super.onResume();
        String firstName = mSharedPrefs.getString("first_name", "");
        String lastName = mSharedPrefs.getString("last_name", "");
        String email = mSharedPrefs.getString("email", "");
        boolean is18 = mSharedPrefs.getBoolean("is18", true);
        boolean isMultiGrade = mSharedPrefs.getBoolean("multipleLevel", false);
        boolean notifications = mSharedPrefs.getBoolean("notifications", true);
        boolean location = mSharedPrefs.getBoolean("location", true);
        boolean coupons = mSharedPrefs.getBoolean("coupons", true);

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
        mCheckBoxNotifications.setChecked(notifications);
        mCheckBoxLocation.setChecked(location);
        mCheckBoxCoupons.setChecked(coupons);
    }

    @Override public void onPause() {
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.account_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            update();
        }
        return super.onOptionsItemSelected(item);
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
        ft.commit();
    }

    private void showError(EditText editText) {
        String error = getResources().getString(R.string.empty_field_error);
        editText.setError(
                editText.getHint().toString() + " " + error);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSchoolSpinner.getWindowToken(), 0);
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
    }

    private String getDeviceId() {
        return Settings.Secure.getString(
                getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
