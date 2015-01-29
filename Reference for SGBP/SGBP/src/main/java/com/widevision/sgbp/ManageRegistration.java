package com.widevision.sgbp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.widevision.sgbp.RegistertoParticipate.sendData;
import com.widevision.sgbp.util.AlertDialogManager;
import com.widevision.sgbp.util.ConnectionDetector;
import com.widevision.sgbp.util.Constant;
import com.widevision.sgbp.util.Get_Deviceid;
import com.widevision.sgbp.util.JSONCreation;
import com.widevision.sgbp.util.JSONParser;

public class ManageRegistration extends Activity {
	Button home, cancel, update;
	
	
	private Pattern emailPattern;
	TextView titleView;
	LinearLayout fnll, lnll, bottomlyt, gradelevelbtnlayout, schoolbtnlayout;
	TextView textViewg, textViewm, textViewl, textViewp, textViewk, textViewr,
			textViewe, textViewi, textViewn, textVieww;
	EditText schoolname, emailid, fname, lname;
	CheckBox multiplechilechkBox, pushnotificationchkBox,
			apptouseloactionchkBox, rcvcoupancodechkBox, iam18yearoldchkBox;
	TextView schoolbtn, gradelevelbtn;
	String keyString, schoolnameString, gradelevelString, nameString,
			device_idString, passwordString, user_typeString, emailidString,
			lnameString, fnameString;

	int user_type, user_id, userRegInfo_id, grade_id, grade_info_id;
	boolean multiplechilechk, pushnotificationchk, apptouseloactionchk,
			rcvcoupancodechk, iam18yearoldchk, userregistered;
	String multiplechilechkString, pushnotificationchkString,
			apptouseloactionchkString, rcvcoupancodechkString,
			iam18yearoldchkString, userregisteredString;
	String Result, TAG = "ManageRegistration", Message, Resultgrade,
			Messagegrade, UserId, User_Reg_Grade_Info_Id, User_Reg_Info_Id;
	boolean fnameval = false, lnameval = false;
	JSONParser jsonParser = new JSONParser();
	JSONCreation jsonCreation = new JSONCreation();
	ArrayAdapter<String> grade_leveldataAdapter, school_namedataAdapter;

	ArrayList<HashMap<String, String>> getUserGradeinfo = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> getUserReginfo = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> getSchoolIdData = new ArrayList<HashMap<String, String>>();

	ArrayList<HashMap<String, String>> getGradeNameData = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> gradeleveldata = new ArrayList<HashMap<String, String>>();
	ArrayList<String> UserGradeinfolist = new ArrayList<String>();
	ArrayList<String> UserReginfolist = new ArrayList<String>();

	ArrayList<String> all_school_name_list = new ArrayList<String>();

	String school_id;
	Get_Deviceid get_dv_id;
	int count;
	Bundle bundle;

	ProgressBar prgLoading;

	SharedPreferences gradepref, schoolpref, deviceinfoidpref;

	String Grade_Id1, School_Id, School_Name, Grade_Name, Grade_Id,
			Has_Multiple_Child, Is_Coupon_Allowed, Is_Location_Service_Allowed,
			Is_Notification_Allowed, Is_User_Over_18_Year, Is_User_Registered,
			User_Email, First_Name, Last_Name, Device_Info_Id;
	// School_Id_old,School_Name_old
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	//
	// Connection detector
	ConnectionDetector cd;
	ArrayList<HashMap<String, Integer>> selectgradedata = new ArrayList<HashMap<String, Integer>>();
	ArrayList<HashMap<String, String>> schoolnamelistdata = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> gradelevellistdata = new ArrayList<HashMap<String, String>>();
	ArrayList<String> grade_level_list = new ArrayList<String>();
	ArrayList<String> grade_name_list = new ArrayList<String>();
	ArrayList<String> grade_id_list = new ArrayList<String>();
	ArrayList<String> school_id_list = new ArrayList<String>();

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
	String regId = "";
	Typeface type;
	int schoolclick = 0, gradeclick = 0;
	private Button next, previous, done;
	private LinearLayout bottom1;

	boolean fnameb = true, lnameb = false, emailb = false;

	SharedPreferences pref, userdpref;

	@Override
	public void onBackPressed() {

		startActivity(new Intent(ManageRegistration.this, MainActivity.class));

		finish();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manageregistration);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		get_dv_id = new Get_Deviceid(getApplicationContext());

		device_idString = get_dv_id.get_unique_deviceid();

		regId = PreferenceConnector.readString(getApplicationContext(),
				PreferenceConnector.GCM_ID, "");
		gradelevelbtn = (TextView) findViewById(R.id.gradelevelbtn);
		emailid = (EditText) findViewById(R.id.emailid);
		fname = (EditText) findViewById(R.id.fname);
		lname = (EditText) findViewById(R.id.lname);
		schoolbtn = (TextView) findViewById(R.id.schoolbtn);
		multiplechilechkBox = (CheckBox) findViewById(R.id.multiplechilechk);
		pushnotificationchkBox = (CheckBox) findViewById(R.id.pushnotificationchk);
		apptouseloactionchkBox = (CheckBox) findViewById(R.id.apptouseloactionchk);
		rcvcoupancodechkBox = (CheckBox) findViewById(R.id.rcvcoupancodechk);
		iam18yearoldchkBox = (CheckBox) findViewById(R.id.iam18yearoldchk);
		prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
		fnll = (LinearLayout) findViewById(R.id.fn_llmg);
		lnll = (LinearLayout) findViewById(R.id.ln_llmg);
		next = (Button) findViewById(R.id.submit1);
		previous = (Button) findViewById(R.id.submit2);
		done = (Button) findViewById(R.id.submit3);
		bottom1 = (LinearLayout) findViewById(R.id.transparent_panel);
		bottomlyt = (LinearLayout) findViewById(R.id.bottomlyt);

		gradelevelbtnlayout = (LinearLayout) findViewById(R.id.gradelevelbtnlayout);
		schoolbtnlayout = (LinearLayout) findViewById(R.id.schoolbtnlayout);

		gradelevelbtn.setInputType(InputType.TYPE_NULL);
		schoolbtn.setInputType(InputType.TYPE_NULL);

		previous.setBackgroundResource(R.drawable.previousunactive);
		next.setBackgroundResource(R.drawable.nextactive);

		bundle = getIntent().getExtras();

		// TelephonyManager telephonyManager = (TelephonyManager)
		// getSystemService(Context.TELEPHONY_SERVICE);
		// device_idString = telephonyManager.getDeviceId()+ "12345";

		userdpref = getSharedPreferences("userid", Context.MODE_PRIVATE);
		UserId = userdpref.getString("UserId", null);

		deviceinfoidpref = getSharedPreferences("deviceinfoid",
				Context.MODE_PRIVATE);
		Device_Info_Id = bundle.getString("Device_Info_Id_saved");

		Log.d(TAG, "UserId----------" + UserId + "....Device_Info_Id---------"
				+ Device_Info_Id);

		final View activityRootView = findViewById(R.id.mainlytmang);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						int heightDiff = activityRootView.getRootView()
								.getHeight() - activityRootView.getHeight();

						Log.d("difference is>>>>>>>>>>>..", "...." + heightDiff);

						if (heightDiff > 100) {
							bottom1.setVisibility(View.VISIBLE);
							bottomlyt.setVisibility(View.GONE);
						} else {
							bottom1.setVisibility(View.GONE);
							bottomlyt.setVisibility(View.VISIBLE);
						}
					}
				});
		pref = getSharedPreferences("device", Context.MODE_PRIVATE);

		// device_idString = pref.getString("device_idString", null);
		type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
		titleView = (TextView) findViewById(R.id.title);

		textViewg = (TextView) findViewById(R.id.textViewg);
		textViewm = (TextView) findViewById(R.id.textViewm);
		textViewl = (TextView) findViewById(R.id.textViewl);
		textViewp = (TextView) findViewById(R.id.textViewp);
		textViewk = (TextView) findViewById(R.id.textViewk);
		textViewr = (TextView) findViewById(R.id.textViewr);
		textViewe = (TextView) findViewById(R.id.textViewe);
		textViewi = (TextView) findViewById(R.id.textViewi);
		textViewn = (TextView) findViewById(R.id.textViewn);
		textVieww = (TextView) findViewById(R.id.textVieww);

		textViewg.setTypeface(type);
		textViewm.setTypeface(type);
		textViewl.setTypeface(type);
		textViewp.setTypeface(type);
		textViewk.setTypeface(type);
		textViewr.setTypeface(type);
		textViewe.setTypeface(type);
		textViewi.setTypeface(type);
		textViewn.setTypeface(type);
		textVieww.setTypeface(type);

		schoolbtn.setTypeface(type);
		gradelevelbtn.setTypeface(type);

		gradelevelbtn.setEnabled(false);
		schoolbtn.setEnabled(false);

		emailid.setTypeface(type);
		fname.setTypeface(type);
		lname.setTypeface(type);

		titleView.setTypeface(type);
		titleView.setText("MANAGE ACCOUNT");

		fname.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Log.d(TAG, "fnameval-------" + fnameval);
				Log.d(TAG, "fnameval---------" + s.length());
				if (fnameval) {
					if (s.length() == 20) {
						showMesgDlgfalse("The First Name cannot be more than 20 characters.");

					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		lname.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Log.d(TAG, "lnameval-----------" + lnameval);
				Log.d(TAG, "lnameval--------" + s.length());
				if (lnameval) {
					if (s.length() == 20) {
						showMesgDlgfalse("The Last Name cannot be more than 20 characters.");
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		previous.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (emailb) {
					emailid.requestFocus();
				} else if (fnameb) {
					emailid.requestFocus();
				} else if (lnameb) {
					fname.requestFocus();
				}
			}
		});
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.d("multiplechilechkBox.isChecked()>>>>>>>>>>", ">>>>>"
						+ multiplechilechkBox.isChecked());

				if (iam18yearoldchkBox.isChecked()) {
					if (emailb) {
						fname.requestFocus();
					} else if (fnameb) {
						lname.requestFocus();
					} else if (lnameb) {
						lname.requestFocus();
					}
				} else {
					emailid.requestFocus();
				}

			}
		});
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(bottom1.getWindowToken(), 0);
				bottom1.setVisibility(View.GONE);
			}
		});
		home = (Button) findViewById(R.id.home);
		update = (Button) findViewById(R.id.update);
		cancel = (Button) findViewById(R.id.cancel);
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(ManageRegistration.this,
						MainActivity.class));

				finish();
			}
		});
		schoolbtnlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				initiatePopupWindow();
			}
		});
		gradelevelbtnlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (grade_level_list.isEmpty()) {
					showMesgDlgfalse("No Record For Grade.");
					multiplechilechkBox.setChecked(false);
				} else {
					if (multiplechilechkBox.isChecked()) {
						gradeinitiatePopupWindow();

					} else {
						singlegradeinitiatePopupWindow();
					}
				}

			}
		});
		pushnotificationchkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (pushnotificationchkBox.isChecked()) {
					showAlertDialog(Constant.pushnotimsgchk,
							Constant.allowpushnotititle);
				} else {
					showAlertDialog(Constant.pushnotimsgunchk,
							Constant.allowpushnotititle);
				}
			}
		});

		apptouseloactionchkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (apptouseloactionchkBox.isChecked()) {
					showAlertDialog(Constant.locationmsgchk,
							Constant.allowlocationtitle);
				} else {
					showAlertDialog(Constant.locationmsgunchk,
							Constant.allowlocationtitle);
				}
			}
		});

		rcvcoupancodechkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (rcvcoupancodechkBox.isChecked()) {
					showAlertDialog(Constant.couponmsgchk,
							Constant.allowcoupontitle);
				} else {
					showAlertDialog(Constant.couponmsgunchk,
							Constant.allowcoupontitle);
				}
			}
		});
		multiplechilechkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (multiplechilechkBox.isChecked()) {

					showAlertDialogmultiplechild(Constant.multiplechildmsgchk,
							Constant.allowmultiplechilttitle);
					Editor editor = gradepref.edit();
					editor.remove("grade");
					editor.remove("gradecount");
					editor.commit();
					gradelevelbtn.setText("");
				} else {
					gradelevelbtn.setText("");
					showAlertDialog(Constant.multiplechildmsgunchk,
							Constant.allowmultiplechilttitle);
					Editor editor = gradepref.edit();
					editor.remove("grade");
					editor.remove("gradecount");
					editor.commit();
				}
			}
		});

		iam18yearoldchkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (iam18yearoldchkBox.isChecked()) {

					showAlertDialogiam(Constant.agemsgchk,
							Constant.allowagetitle);
					fname.setEnabled(true);
					lname.setEnabled(true);
					float alpha = 1.0f;
					AlphaAnimation alphaUp = new AlphaAnimation(alpha, alpha);
					alphaUp.setFillAfter(true);
					fnll.startAnimation(alphaUp);
					lnll.startAnimation(alphaUp);
				} else {
					fname.setText("");
					lname.setText("");
					fname.setEnabled(false);
					lname.setEnabled(false);
					float alpha = 0.45f;
					AlphaAnimation alphaUp = new AlphaAnimation(alpha, alpha);
					alphaUp.setFillAfter(true);
					fnll.startAnimation(alphaUp);
					lnll.startAnimation(alphaUp);
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ManageRegistration.this,
						MainActivity.class));

				finish();
			}
		});

		school_namedataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, all_school_name_list);

		gradepref = getSharedPreferences("grade", Context.MODE_PRIVATE);
		schoolpref = getSharedPreferences("schoolmng", Context.MODE_PRIVATE);
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cd = new ConnectionDetector(getApplicationContext());

				// Check if Internet present
				if (!cd.isConnectingToInternet()) {
					// Internet Connection is not present
					alert.showAlertDialog(
							ManageRegistration.this,
							"Oops!!",
							"Internet Connection seems to offline.Please check your network.",
							false);
					// stop executing code by return
					return;
				} else {
					schoolbtnlayout.setEnabled(true);
					gradelevelbtnlayout.setEnabled(true);
					multiplechilechkBox.setEnabled(true);
					pushnotificationchkBox.setEnabled(true);
					apptouseloactionchkBox.setEnabled(true);
					apptouseloactionchkBox.setEnabled(true);
					rcvcoupancodechkBox.setEnabled(true);
					iam18yearoldchkBox.setEnabled(true);
					fname.setEnabled(true);
					lname.setEnabled(true);
				}
				emailidString = emailid.getText().toString();
				lnameString = lname.getText().toString();
				fnameString = fname.getText().toString();
				if (multiplechilechkBox.isChecked()) {
					multiplechilechkString = "true";
				} else {
					multiplechilechkString = "false";
				}
				if (pushnotificationchkBox.isChecked()) {
					pushnotificationchkString = "true";
				} else {
					pushnotificationchkString = "false";
				}
//				if (apptouseloactionchkBox.isChecked()) {
//					apptouseloactionchkString = "true";
//				} else {
//					apptouseloactionchkString = "false";
//				}
				if (rcvcoupancodechkBox.isChecked()) {
					rcvcoupancodechkString = "true";
				} else {
					rcvcoupancodechkString = "false";
				}
				if (iam18yearoldchkBox.isChecked()) {
					iam18yearoldchkString = "true";
				} else {
					iam18yearoldchkString = "false";
				}

				if (emailidString.equals("")) {
					if (gradelevelbtn.getText().toString().equals("")) {
						showMesgDlgfalse("Grade Level can not be empty.");
					} else {
						
						 if (apptouseloactionchkBox.isChecked()) {
								apptouseloactionchkString = "true";
								new updateData().execute();
							} else {
								showMesgDlgfalse("Location service is required to update and participate in the School Give Back Program.");
								
							}
					}
				} else
					if (eMailValidation(emailidString)) {
					
					
					if (gradelevelbtn.getText().toString().equals("")) {
						showMesgDlgfalse("Grade Level can not be empty.");
					} else {
						
						 if (apptouseloactionchkBox.isChecked()) {
								apptouseloactionchkString = "true";
								new updateData().execute();
							} else {
								showMesgDlgfalse("Location service is required to update and participate in the School Give Back Program.");
								
							}
					
					}
				} else {
					showMesgDlgfalse("Enter valid email id.");
					emailid.requestFocus();
				}

			}
		});
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(
					ManageRegistration.this,
					"Oops!!",
					"Internet Connection seems to offline.Please check your network.",
					false);
			// stop executing code by return
			return;
		} else {
			try {
				new GetUserReginfoBYDevice_UID().execute();
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
		emailid.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {
					previous.setBackgroundResource(R.drawable.previousunactive);
					next.setBackgroundResource(R.drawable.nextactive);
					emailb = true;
					lnameb = false;
					fnameb = false;
					previous.setEnabled(false);
					next.setEnabled(true);

				}

			}
		});

		fname.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {
					previous.setBackgroundResource(R.drawable.previousactive);
					next.setBackgroundResource(R.drawable.nextactive);
					fnameb = true;
					lnameb = false;
					emailb = false;
					previous.setEnabled(true);
					next.setEnabled(true);
					fnameval = true;
				} else {
					fnameval = false;
				}
			}
		});
		lname.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {
					previous.setBackgroundResource(R.drawable.previousactive);
					next.setBackgroundResource(R.drawable.nextunactive);
					lnameb = true;
					fnameb = false;
					emailb = false;
					previous.setEnabled(true);
					next.setEnabled(false);
					lnameval = true;
				} else {
					lnameval = false;
				}
			}
		});
	}

//	private boolean checkEmail(String email) {
//		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
//	}

	public boolean eMailValidation(String emailstring) {
		emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
				+ "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		return emailMatcher.matches();
	}
	
	
	
	public class updateData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
			schoolbtnlayout.setEnabled(false);
			gradelevelbtnlayout.setEnabled(false);
			multiplechilechkBox.setEnabled(false);
			pushnotificationchkBox.setEnabled(false);
			apptouseloactionchkBox.setEnabled(false);
			apptouseloactionchkBox.setEnabled(false);
			rcvcoupancodechkBox.setEnabled(false);
			iam18yearoldchkBox.setEnabled(false);
			emailid.setEnabled(false);
			fname.setEnabled(false);
			lname.setEnabled(false);
			update.setEnabled(false);
			home.setEnabled(false);
			cancel.setEnabled(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			Result = updateRequest(keyString, device_idString, School_Id,
					multiplechilechkString, userregisteredString,
					rcvcoupancodechkString, pushnotificationchkString,
					apptouseloactionchkString, iam18yearoldchkString,
					fnameString, lnameString, emailidString, user_typeString,
					Device_Info_Id);
			Log.d(TAG, "updateRequest---------Device_Info_Id------"
					+ Device_Info_Id);
			Log.e(TAG, "Result-------" + Result);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			prgLoading.setVisibility(View.GONE);
			schoolbtnlayout.setEnabled(true);
			gradelevelbtnlayout.setEnabled(true);
			multiplechilechkBox.setEnabled(true);
			pushnotificationchkBox.setEnabled(true);
			apptouseloactionchkBox.setEnabled(true);
			apptouseloactionchkBox.setEnabled(true);
			rcvcoupancodechkBox.setEnabled(true);
			iam18yearoldchkBox.setEnabled(true);
			emailid.setEnabled(true);
			fname.setEnabled(true);
			lname.setEnabled(true);
			update.setEnabled(true);
			home.setEnabled(true);
			cancel.setEnabled(true);
			resultAlert(Result);

		}
	}

	public void resultAlert(String HasilProses) {
		if (HasilProses.trim().equalsIgnoreCase("true")) {

			Log.d(TAG, "schoolclick-----" + schoolclick
					+ "----------gradeclick---------" + gradeclick);
			if (schoolclick == 0 && gradeclick == 0) {
				if (multiplechilechkBox.isChecked()
						&& gradelevelbtn.getText().toString().equals("")) {
					showMesgDlgfalse("Multiple Child is allow.Please fill the grade.");
				} else {
					Dialog_for_registration();
				}
			} else {
				Resultgrade = saveUserMultipleGrade();
				resultAlertGrade(Resultgrade);
			}
		} else if (HasilProses.trim().equalsIgnoreCase("false")) {
			showMesgDlgfalse(Message);
		}

	}

	public void resultAlertGrade(String Resultgrade1) {

		try {
			if (Resultgrade1.trim().equalsIgnoreCase("true")) {
				Dialog_for_registration();
			} else if (Resultgrade1.trim().equalsIgnoreCase("false")) {
				showMesgDlgfalse("Grade level can not be empty.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String updateRequest(String keyString2, String device_idString2,
			String school_idString2, String multiplechilechkString2,
			String userregisteredString2, String rcvcoupancodechkString2,
			String pushnotificationchkString2,
			String apptouseloactionchkString2, String iam18yearoldchkString2,
			String fnameString2, String lnameString2, String emailidString2,
			String user_typeString2, String Device_Info_Id2) {

		keyString2 = "123456";
		userregisteredString2 = "true";
		user_typeString2 = "1";
		String resultString = "";
		Log.d(TAG, "updateRequest------------Device_Info_Id-----------"
				+ Device_Info_Id2);
		JSONObject SaveTeamMemberInfoResult = null;
		JSONObject json = null;
		try {

			json = jsonParser.getJSONFromUrl(Constant.SERVER_URL
					+ "UpdateTeamMemberInfo" + "?Key=123456" + "&User_Email="
					+ emailidString2 + "&User_Reg_Info_Id=" + UserId
					+ "&School_Id=" + school_idString2 + "&Has_Multiple_Child="
					+ multiplechilechkString2 + "&Is_User_Registered="
					+ userregisteredString2 + "&Is_Coupon_Allowed="
					+ rcvcoupancodechkString2 + "&Is_Notification_Allowed="
					+ pushnotificationchkString2
					+ "&Is_Location_Service_Allowed="
					+ apptouseloactionchkString2 + "&Is_User_Over_18_Year="
					+ iam18yearoldchkString2 + "&First_Name=" + fnameString2
					+ "&Last_Name=" + lnameString2 + "&Is_Device_Registered="
					+ "true" + "&Device_UID=" + device_idString
					+ "&Device_Token_Registration_Id=" + regId);

			Log.d(TAG, "" + Constant.SERVER_URL + "UpdateTeamMemberInfo"
					+ "?Key=123456" + "&User_Email=" + emailidString2
					+ "&User_Reg_Info_Id=" + UserId + "&School_Id="
					+ school_idString2 + "&Has_Multiple_Child="
					+ multiplechilechkString2 + "&Is_User_Registered="
					+ userregisteredString2 + "&Is_Coupon_Allowed="
					+ rcvcoupancodechkString2 + "&Is_Notification_Allowed="
					+ pushnotificationchkString2
					+ "&Is_Location_Service_Allowed="
					+ apptouseloactionchkString2 + "&Is_User_Over_18_Year="
					+ iam18yearoldchkString2 + "&First_Name=" + fnameString2
					+ "&Last_Name=" + lnameString2 + "&Is_Device_Registered="
					+ "true" + "&Device_UID=" + device_idString
					+ "&Device_Token_Registration_Id=" + regId);
			Log.d(TAG, "-UpdateTeamMemberInfo-----------" + json);

			SaveTeamMemberInfoResult = json
					.getJSONObject("UpdateTeamMemberInfoResult");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Message = SaveTeamMemberInfoResult.getString("Message");
			resultString = SaveTeamMemberInfoResult.getString("Status");
		} catch (Exception e) {

			e.printStackTrace();
		}
		return resultString;
	}

	public class GetUserReginfoBYDevice_UID extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
			schoolbtnlayout.setEnabled(false);
			gradelevelbtnlayout.setEnabled(false);
			multiplechilechkBox.setEnabled(false);
			pushnotificationchkBox.setEnabled(false);
			apptouseloactionchkBox.setEnabled(false);
			apptouseloactionchkBox.setEnabled(false);
			rcvcoupancodechkBox.setEnabled(false);
			iam18yearoldchkBox.setEnabled(false);
			fname.setEnabled(false);
			lname.setEnabled(false);
			emailid.setEnabled(false);
			update.setEnabled(false);
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				getUserReginfoBYDevice_UID();
				getUserGradeinfoByDevice_UID();
				getSchoolNameFirstList();
			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			for (int i = 0; i < schoolnamelistdata.size(); i++) {

				all_school_name_list.add(schoolnamelistdata.get(i).get(
						"School_Name"));
			}
			try {
				if (Has_Multiple_Child.equalsIgnoreCase("true")) {
					multiplechilechkBox.setChecked(true);

				} else {
					multiplechilechkBox.setChecked(false);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
			try {
				if (Is_Notification_Allowed.equalsIgnoreCase("true")) {
					pushnotificationchkBox.setChecked(true);

				} else {
					pushnotificationchkBox.setChecked(false);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

			try {
				if (Is_Location_Service_Allowed.equalsIgnoreCase("true")) {
					apptouseloactionchkBox.setChecked(true);

				} else {
					apptouseloactionchkBox.setChecked(false);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

			try {
				if (Is_Coupon_Allowed.equalsIgnoreCase("true")) {
					rcvcoupancodechkBox.setChecked(true);
				} else {
					rcvcoupancodechkBox.setChecked(false);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

			try {
				if (Is_User_Over_18_Year.equalsIgnoreCase("true")) {
					iam18yearoldchkBox.setChecked(true);
					fname.setEnabled(true);
					lname.setEnabled(true);

				} else {
					iam18yearoldchkBox.setChecked(false);
					fname.setEnabled(false);
					lname.setEnabled(false);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
			try {
				emailid.setText(User_Email);
				fname.setText(First_Name);
				lname.setText(Last_Name);
			} catch (Exception e1) {

				e1.printStackTrace();
			}

			new GetSchoolNameList().execute();
		}
	}

	public class GetSchoolNameList extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {

			getSchoolNameList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				schoolbtn.setText(School_Name);
			} catch (Exception e1) {

				e1.printStackTrace();
			}
			new GetGradesList().execute();
		}
	}

	public class GetGradesList extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... params) {

			getGradesList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			new sendGradeData().execute();

			try {

				for (int j = 0; j < getGradeNameData.size(); j++) {
					String Grade_Name1 = getGradeNameData.get(j).get(
							"Grade_Name");
					String Grade_Id1 = getGradeNameData.get(j).get("Grade_Id");
					Log.d(TAG, "Grade_Name-------getGradeNameData----"
							+ Grade_Name1);
					grade_name_list.add(Grade_Name1);

					grade_id_list.add(Grade_Id1);
					Log.d(TAG, "Grade_Name---------" + Grade_Name1
							+ "---------Grade_Id1----------" + Grade_Id1);
					String[] stringArray = grade_name_list
							.toArray(new String[grade_name_list.size()]);
					StringBuilder sb = new StringBuilder();

					for (int i = 0; i < stringArray.length; ++i) {
						sb.append(", ");
						sb.append(stringArray[i]);
					}
					String string = sb.toString();
					Grade_Name = string.substring(2);
					Log.d(TAG, "StringBuilder-------Grade_Name----"
							+ Grade_Name);

					gradelevelbtn.setText(Grade_Name);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	public String getSchoolNameList() {
		String schlliststatus = null;
		JSONArray result = null;
		JSONObject json = null;
		try {
			json = jsonParser.getJSONFromUrl(Constant.SERVER_URL
					+ "GetSchoolList");
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}
			Log.d(TAG, "GetSchoolList----------" + json);
			JSONObject GetSchoolListResult = json
					.getJSONObject("GetSchoolListResult");

			result = GetSchoolListResult.getJSONArray("SchoolInfo");
			schlliststatus = GetSchoolListResult.getString("Status");

			for (int i = 0; i < result.length(); i++) {
				try {
					JSONObject c = result.getJSONObject(i);
					String School_Id1 = c.getString("School_Id");
					String School_Name1 = c.getString("School_Name");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("School_Id", School_Id1);
					map.put("School_Name", School_Name1);
					if (getSchoolIdData.contains(School_Id1)) {

					} else {
						getSchoolIdData.add(map);
					}

					if (School_Id1.equalsIgnoreCase(School_Id)) {
						Log.d("School_Id==========", School_Id
								+ "School_Id1============" + School_Id1);

						School_Name = School_Name1;
						School_Id = School_Id1;
						Log.d("School_Name1==========", School_Name1
								+ "-------School_Name--------" + School_Name);
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return schlliststatus;
	}

	public String getGradesList() {
		String gradliststatus = null;
		JSONArray result = null;
		JSONObject json = null;
		getGradeNameData.clear();
		try {
			json = jsonParser.getJSONFromUrl(Constant.SERVER_URL
					+ "GetGradesList");
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}
			Log.d(TAG, "GetGradesList----------" + json);
			JSONObject GetGradesListResult = json
					.getJSONObject("GetGradesListResult");

			result = GetGradesListResult.getJSONArray("GradeInfo");
			gradliststatus = GetGradesListResult.getString("Status");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < result.length(); i++) {
				try {
					JSONObject c = result.getJSONObject(i);
					String Grade_Id1 = c.getString("Grade_Id");
					String Grade_Name1 = c.getString("Grade_Name");
					String Grade_Info_Id1 = c.getString("Grade_Info_Id");
					String School_Id1 = c.getString("School_Id");
					Log.d(TAG,
							"getUserGradeinfo----------"
									+ getUserGradeinfo.size());
					for (int j = 0; j < getUserGradeinfo.size(); j++) {

						String Grade_Id = getUserGradeinfo.get(j).get(
								"Grade_Id");
						Log.d(TAG, "School_Id---------" + School_Id
								+ "School_Id1============" + School_Id1);
						if (School_Id1.equals(School_Id)) {
							if (Grade_Id1.equals(Grade_Id)) {
								HashMap<String, String> map = new HashMap<String, String>();

								if (getGradeNameData.contains(map)) {

								} else {
									map.put("Grade_Id", Grade_Id1);
									map.put("Grade_Name", Grade_Name1);
									map.put("Grade_Info_Id", Grade_Info_Id1);
									getGradeNameData.add(map);
								}

								Log.d(TAG, Grade_Id + "Grade_Id1============"
										+ Grade_Id1);
								Log.d(TAG,
										"-------Grade_Name1--getGradeNameData------"
												+ Grade_Name1);
							}
						}
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return gradliststatus;
	}

	public String getUserGradeinfoByDevice_UID() {
		String gradstatus = null;
		JSONArray result = null;
		JSONObject json = null;
		getUserGradeinfo.clear();
		try {
			json = jsonParser.getJSONFromUrl(Constant.SERVER_URL
					+ "GetUserGradeinfoByDevice_UID"
					+ "?Key=123456&Device_UID=" + device_idString);
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}
			Log.d(TAG, "-GetUserGradeinfoByDevice_UID-----" + json);
			JSONObject GetUserGradeinfoByDevice_UIDResult = json
					.getJSONObject("GetUserGradeinfoByDevice_UIDResult");
			result = GetUserGradeinfoByDevice_UIDResult
					.getJSONArray("UserRegGradeInfo");
			gradstatus = GetUserGradeinfoByDevice_UIDResult.getString("Status");
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < result.length(); i++) {
			try {
				JSONObject c = result.getJSONObject(i);
				String Grade_Id2 = c.getString("Grade_Id");
				String User_Reg_Grade_Info_Id2 = c
						.getString("User_Reg_Grade_Info_Id");
				String User_Reg_Info_Id2 = c.getString("User_Reg_Info_Id");
				HashMap<String, String> map = new HashMap<String, String>();

				Log.d(TAG, "-getUserGradeinfo-----Grade_Id2>>>>>>>>>>>>>>>>>>."
						+ Grade_Id2);
				if (getUserGradeinfo.contains(Grade_Id2)) {

				} else {
					map.put("Grade_Id", Grade_Id2);
					map.put("User_Reg_Grade_Info_Id", User_Reg_Grade_Info_Id2);
					map.put("User_Reg_Info_Id", User_Reg_Info_Id2);
					User_Reg_Grade_Info_Id = User_Reg_Grade_Info_Id2;
					User_Reg_Info_Id = User_Reg_Info_Id2;
					getUserGradeinfo.add(map);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return gradstatus;
	}

	public String getUserReginfoBYDevice_UID() {
		String regstatus = null;
		JSONArray result = null;
		JSONObject json = null;
		try {
			json = jsonParser.getJSONFromUrl(Constant.SERVER_URL
					+ "GetUserReginfoBYDevice_UID" + "?Device_UID="
					+ device_idString);
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}
			Log.d("-GetUserReginfoBYDevice_UID----------->", "-----------"
					+ json);
			JSONObject GetUserReginfoBYDevice_UIDResult = json
					.getJSONObject("GetUserReginfoBYDevice_UIDResult");

			result = GetUserReginfoBYDevice_UIDResult
					.getJSONArray("UserRegInfo");

			regstatus = GetUserReginfoBYDevice_UIDResult.getString("Status");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < result.length(); i++) {
			try {
				JSONObject c = result.getJSONObject(i);

				String Has_Multiple_Child2 = c.getString("Has_Multiple_Child");
				String Is_Coupon_Allowed2 = c.getString("Is_Coupon_Allowed");
				String Is_Location_Service_Allowed2 = c
						.getString("Is_Location_Service_Allowed");
				String Is_Notification_Allowed2 = c
						.getString("Is_Notification_Allowed");
				String Is_User_Over_18_Year2 = c
						.getString("Is_User_Over_18_Year");
				String Is_User_Registered2 = c.getString("Is_User_Registered");

				String School_Id2 = c.getString("School_Id");
				String User_Email2 = c.getString("User_Email");
				String First_Name2 = c.getString("First_Name");
				String Last_Name2 = c.getString("Last_Name");
				String User_Reg_Info_Id2 = c.getString("User_Reg_Info_Id");
				String Device_Info_Id2 = c.getString("Device_Info_Id");
				HashMap<String, String> map = new HashMap<String, String>();
				Log.d(TAG, "getUserReginfoBYDevice_UID---------School_Id------"
						+ School_Id2);
				map.put("School_Id", School_Id2);
				map.put("User_Email", User_Email2);
				map.put("First_Name", First_Name2);
				map.put("Last_Name", Last_Name2);
				map.put("User_Reg_Info_Id", User_Reg_Info_Id2);
				map.put("Device_Info_Id", Device_Info_Id2);

				map.put("Has_Multiple_Child", Has_Multiple_Child2);
				map.put("Is_Coupon_Allowed", Is_Coupon_Allowed2);
				map.put("Is_Location_Service_Allowed",
						Is_Location_Service_Allowed2);
				map.put("Is_Notification_Allowed", Is_Notification_Allowed2);
				map.put("Is_User_Over_18_Year", Is_User_Over_18_Year2);
				map.put("Is_User_Registered", Is_User_Registered2);
				Device_Info_Id = Device_Info_Id2;
				Log.d(TAG,
						"getUserReginfoBYDevice_UID---------Device_Info_Id------"
								+ Device_Info_Id);
				School_Id = School_Id2;
				Has_Multiple_Child = Has_Multiple_Child2;
				Is_Coupon_Allowed = Is_Coupon_Allowed2;
				Is_Location_Service_Allowed = Is_Location_Service_Allowed2;
				Is_Notification_Allowed = Is_Notification_Allowed2;
				Is_User_Over_18_Year = Is_User_Over_18_Year2;
				Is_User_Registered = Is_User_Registered2;
				User_Email = User_Email2;
				Last_Name = Last_Name2;
				First_Name = First_Name2;
				UserId = User_Reg_Info_Id2;
				Log.d(TAG, "-School_Id----" + School_Id
						+ "------School_Id2-------" + School_Id2);
				getUserReginfo.add(map);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return regstatus;
	}

	public void showMesgDlg(String msg) {

		try {
			final Dialog alertDialog = new Dialog(ManageRegistration.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(),
					"Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog
					.findViewById(R.id.aboutprogram);
			aboutprogram.setText(msg);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					schoolbtnlayout.setEnabled(false);
					gradelevelbtnlayout.setEnabled(false);
					multiplechilechkBox.setEnabled(false);
					pushnotificationchkBox.setEnabled(false);
					apptouseloactionchkBox.setEnabled(false);
					apptouseloactionchkBox.setEnabled(false);
					rcvcoupancodechkBox.setEnabled(false);
					iam18yearoldchkBox.setEnabled(false);
					fname.setEnabled(false);
					lname.setEnabled(false);
					emailid.setEnabled(false);
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void showMesgDlgtrue(String msg) {

		try {
			final Dialog alertDialog = new Dialog(ManageRegistration.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(),
					"Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog
					.findViewById(R.id.aboutprogram);
			TextView alerttxtwindow = (TextView) alertDialog
					.findViewById(R.id.alerttxtwindow);

			aboutprogram.setText(msg);
			btnDismiss.setText("Ok");
			alerttxtwindow.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					startActivity(new Intent(ManageRegistration.this,
							MainActivity.class));

					finish();
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void showMesgDlgfalse(String msg) {

		try {
			final Dialog alertDialog = new Dialog(ManageRegistration.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(),
					"Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog
					.findViewById(R.id.aboutprogram);
			TextView alerttxt = (TextView) alertDialog
					.findViewById(R.id.alerttxtwindow);
			alerttxt.setText("Error");
			aboutprogram.setText(msg);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void showAlertDialogGrade(String message, String message1) {

		try {
			final Dialog alertDialog = new Dialog(ManageRegistration.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(),
					"Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog
					.findViewById(R.id.aboutprogram);

			TextView alerttxt = (TextView) alertDialog
					.findViewById(R.id.alerttxtwindow);
			alerttxt.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			aboutprogram.setText(message);
			alerttxt.setText(message1);

			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					multiplechilechkBox.setChecked(false);
					gradelevelbtn.setText("");
					alertDialog.dismiss();
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void showAlertDialogiam(String message, String message1) {

		try {
			final Dialog alertDialog = new Dialog(ManageRegistration.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(),
					"Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog
					.findViewById(R.id.aboutprogram);

			TextView alerttxt = (TextView) alertDialog
					.findViewById(R.id.alerttxtwindow);
			alerttxt.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			aboutprogram.setText(message);
			alerttxt.setText(message1);

			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					fname.requestFocus();
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void showAlertDialog(String message, String message1) {

		try {
			final Dialog alertDialog = new Dialog(ManageRegistration.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(),
					"Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog
					.findViewById(R.id.aboutprogram);

			TextView alerttxt = (TextView) alertDialog
					.findViewById(R.id.alerttxtwindow);
			alerttxt.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			aboutprogram.setText(message);
			alerttxt.setText(message1);

			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void showAlertDialogmultiplechild(String message, String message1) {

		try {
			final Dialog alertDialog = new Dialog(ManageRegistration.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(),
					"Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog
					.findViewById(R.id.aboutprogram);
			TextView alerttxt = (TextView) alertDialog
					.findViewById(R.id.alerttxtwindow);
			alerttxt.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			aboutprogram.setText(message);
			alerttxt.setText(message1);

			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public class sendGradeData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
			schoolbtnlayout.setEnabled(false);
			gradelevelbtnlayout.setEnabled(false);
			multiplechilechkBox.setEnabled(false);
			pushnotificationchkBox.setEnabled(false);
			apptouseloactionchkBox.setEnabled(false);
			apptouseloactionchkBox.setEnabled(false);
			rcvcoupancodechkBox.setEnabled(false);
			iam18yearoldchkBox.setEnabled(false);
			update.setEnabled(false);
			fname.setEnabled(false);
			lname.setEnabled(false);
			emailid.setEnabled(false);
		}

		@Override
		protected Void doInBackground(Void... params) {

			getGradeNameList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (grade_level_list.isEmpty()) {
				String msg = "No grade value found";
				String msg1 = "Error";
				showAlertDialogGrade(msg, msg1);
			} else {
				grade_leveldataAdapter = new ArrayAdapter<String>(
						ManageRegistration.this,
						android.R.layout.simple_list_item_1, grade_level_list);
			}
			prgLoading.setVisibility(View.GONE);
			schoolbtnlayout.setEnabled(true);
			gradelevelbtnlayout.setEnabled(true);
			multiplechilechkBox.setEnabled(true);
			pushnotificationchkBox.setEnabled(true);
			apptouseloactionchkBox.setEnabled(true);
			apptouseloactionchkBox.setEnabled(true);
			rcvcoupancodechkBox.setEnabled(true);
			iam18yearoldchkBox.setEnabled(true);
			update.setEnabled(true);

			emailid.setEnabled(true);

			if (iam18yearoldchkBox.isChecked()) {
				Log.e(TAG, "iam18yearoldchkBox>>>>>>>>...if.."
						+ iam18yearoldchkBox.isChecked());
				fname.setEnabled(true);
				lname.setEnabled(true);
			} else {
				Log.e(TAG, "iam18yearoldchkBox>>>>>>>...else.."
						+ iam18yearoldchkBox.isChecked());
				fname.setText("");
				lname.setText("");
				fname.setEnabled(false);
				lname.setEnabled(false);
				float alpha = 0.45f;
				AlphaAnimation alphaUp = new AlphaAnimation(alpha, alpha);
				alphaUp.setFillAfter(true);
				fnll.startAnimation(alphaUp);
				lnll.startAnimation(alphaUp);
			}

		}
	}

	public void getGradeNameList() {

		grade_level_list.clear();
		gradelevellistdata.clear();
		JSONObject json = null;
		try {
			json = jsonParser
					.getJSONFromUrl(Constant.SERVER_URL + "GetGradebySchool"
							+ "?Key=123456&School_Id=" + School_Id);
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}
			Log.d(TAG, "getGradeNameList---------school_id------" + School_Id);
			JSONArray result = null;
			Log.d(TAG, "-GetGradebySchool-----------" + json);
			JSONObject GetSchoolNameResult = json
					.getJSONObject("GetGradebySchoolResult");

			result = GetSchoolNameResult.getJSONArray("GradeInfo");
			for (int i = 0; i < result.length(); i++) {
				try {
					JSONObject c = result.getJSONObject(i);
					String Grade_Name = c.getString("Grade_Name");
					String Grade_Id = c.getString("Grade_Id");
					String Grade_Info_Id = c.getString("Grade_Info_Id");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("Grade_Name", Grade_Name);
					map.put("Grade_Id", Grade_Id);
					map.put("Grade_Info_Id", Grade_Info_Id);
					if (gradelevellistdata.contains(Grade_Id)) {

					} else {
						gradelevellistdata.add(map);
					}
					if (grade_level_list.contains(Grade_Name)) {

					} else {
						grade_level_list.add(Grade_Name);
					}

					Log.d(TAG, "-------gradelevellistdata-----"
							+ "Grade_Id----" + Grade_Id + "---Grade_Name-----"
							+ Grade_Name);
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getSchoolNameFirstList() {

		String schlnameliststatus = null;
		JSONArray result = null;
		JSONObject json = null;
		try {

			json = jsonParser.getJSONFromUrl(Constant.SERVER_URL
					+ "GetSchoolName");
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}
			Log.d(TAG, "-GetSchoolName-----------" + json);
			JSONObject GetSchoolNameResult = json
					.getJSONObject("GetSchoolNameResult");

			result = GetSchoolNameResult.getJSONArray("SchoolInfo");
			schlnameliststatus = GetSchoolNameResult.getString("Status");
			for (int i = 0; i < result.length(); i++) {
				try {
					JSONObject c = result.getJSONObject(i);
					String School_Name = c.getString("School_Name");
					String School_Id = c.getString("School_Id");
					HashMap<String, String> map = new HashMap<String, String>();
					// adding each child node to HashMap key => value
					map.put("School_Name", School_Name);
					map.put("School_Id", School_Id);
					if (schoolnamelistdata.contains(School_Id)) {

					} else {
						schoolnamelistdata.add(map);
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return schlnameliststatus;

	}

	protected void initiatePopupWindow() {
		try {
			final Dialog alertDialog = new Dialog(ManageRegistration.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.school_popup);
			ListView schoollist = (ListView) alertDialog
					.findViewById(R.id.schoollist);

			alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			Log.d("school_name_list---------", "" + all_school_name_list.size());

			schoollist.setAdapter(school_namedataAdapter);

			schoollist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {

					schoolclick = 1;
					School_Id = schoolnamelistdata.get(pos).get("School_Id")
							.toString();
					schoolnameString = arg0.getItemAtPosition(pos).toString();
					Log.d("School_Id---------", "" + School_Id);
					Log.d("school_name---------", schoolnameString);

					try {
						Editor editor = gradepref.edit();
						int count = gradepref.getInt("gradecount", 0);
						for (int i = 0; i < count; ++i) {
							editor.remove("grade" + i);
						}
						editor.remove("gradecount");
						editor.commit();
						Log.d(TAG, "schoolpref.getInt(pos, 0)--------else-----"
								+ schoolpref.getInt("pos", 0));
						Editor editor1 = schoolpref.edit();
						editor1.putInt("school", pos);
						editor1.commit();
					} catch (Exception e) {

						e.printStackTrace();
					}

					if (multiplechilechkBox.isChecked()) {
						gradelevelbtn.setText("");
						grade_level_list.clear();
					}
					gradelevelbtn.setText("");
					grade_level_list.clear();
					gradelevellistdata.clear();
					new sendGradeData().execute();
					School_Id = School_Id;
					School_Name = schoolnameString;
					schoolbtn.setText(schoolnameString);
					alertDialog.cancel();
				}
			});
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	int flag = 0;
	MyArrayAdapter myArrayAdapter;

	protected void gradeinitiatePopupWindow() {
		try {

			final Dialog alertDialog = new Dialog(ManageRegistration.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.multiple_grade_popup);
			ListView multigradelist = (ListView) alertDialog
					.findViewById(R.id.multigradelist);
			Button donebtn = (Button) alertDialog.findViewById(R.id.donebtn);
			jsonCreation = new JSONCreation();
			myArrayAdapter = new MyArrayAdapter(this, R.layout.multicheck_item,
					android.R.id.text1, grade_level_list);
			multigradelist.setAdapter(myArrayAdapter);

			int count = gradepref.getInt("gradecount", 0);
			for (int i = 0; i < count; ++i) {
				myArrayAdapter.toggleChecked(gradepref.getInt("grade" + i, 0));

			}

			multigradelist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					flag = 1;
					myArrayAdapter.toggleChecked(position);
				}
			});
			donebtn.setTypeface(type);
			alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

			alertDialog.setCancelable(false);

			alertDialog.show();

			donebtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					cd = new ConnectionDetector(getApplicationContext());

					// Check if Internet present
					if (!cd.isConnectingToInternet()) {
						// Internet Connection is not present
						alert.showAlertDialog(
								ManageRegistration.this,
								"Oops!!",
								"Internet Connection seems to offline.Please check your network.",
								false);
						// stop executing code by return
						return;
					} else {
						schoolbtnlayout.setEnabled(true);
						gradelevelbtnlayout.setEnabled(true);
						multiplechilechkBox.setEnabled(true);
						pushnotificationchkBox.setEnabled(true);
						apptouseloactionchkBox.setEnabled(true);
						apptouseloactionchkBox.setEnabled(true);
						rcvcoupancodechkBox.setEnabled(true);
						iam18yearoldchkBox.setEnabled(true);
						emailid.setEnabled(true);
					}
					alertDialog.cancel();
					gradeclick = 1;
					// getCheckedItemPositions
					List<Integer> resultListint = myArrayAdapter
							.getCheckedItemPositions();
					// getCheckedItems
					List<String> resultList = myArrayAdapter.getCheckedItems();

					String combineGrade = "";
					Log.e("for...",
							">>>>>>selectedGrade>>>>>>" + resultList.size());
					for (int i = 0; i < resultList.size(); ++i) {
						combineGrade += resultList.get(i);
						if (i < resultList.size() - 1) {

							combineGrade += ", ";
						}

					}
					gradelevelString = combineGrade;
					myArrayAdapter.getCheckedItemPositions().toString();
					Log.d(TAG, "grade_level_list.size()----if-------"
							+ grade_level_list.size());
					for (int i = 0; i < resultListint.size(); ++i) {
						grade_id = Integer.parseInt(gradelevellistdata
								.get(resultListint.get(i)).get("Grade_Id")
								.toString());
						grade_info_id = Integer.parseInt(gradelevellistdata
								.get(resultListint.get(i)).get("Grade_Info_Id")
								.toString());
						Log.d(TAG, "resultListint.get(i)--------"
								+ resultListint.get(i));
						updateUserGrade(device_idString, grade_id,
								grade_info_id);
						Log.d(TAG, "gradelevelString-" + gradelevelString);

						Editor editor = gradepref.edit();
						editor.putInt("grade" + i, resultListint.get(i));
						editor.putInt("gradecount", resultListint.size());
						editor.commit();
					}
					if (resultListint.size() == 0) {
						gradelevelbtn.setText("");
						Editor editor = gradepref.edit();
						editor.remove("grade");
						editor.remove("gradecount");
						editor.commit();
					} else {
						gradelevelbtn.setText(gradelevelString);
					}

				}
			});
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	protected void singlegradeinitiatePopupWindow() {

		final Dialog alertDialog = new Dialog(ManageRegistration.this);

		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		alertDialog.setContentView(R.layout.singlegrade_popup);

		ListView singlegradelist = (ListView) alertDialog
				.findViewById(R.id.singlegradelist);

		alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		singlegradelist.setAdapter(grade_leveldataAdapter);

		singlegradelist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				gradeclick = 1;
				gradelevelString = arg0.getItemAtPosition(pos).toString();
				grade_id = Integer.parseInt(gradelevellistdata.get(pos)
						.get("Grade_Id").toString());
				Grade_Id = gradelevellistdata.get(pos).get("Grade_Id")
						.toString();
				Log.d("singlegradeinitiatePopupWindow----------", grade_id
						+ "-------------" + Grade_Id);
				grade_info_id = Integer.parseInt(gradelevellistdata.get(pos)
						.get("Grade_Info_Id").toString());
				Log.d(TAG, "selection.get(i)--------" + pos);

				Log.d(TAG, "gradelevelString-" + gradelevelString);
				gradelevelbtn.setText(gradelevelString);

				// gradelevelspinner.setText(value);
				alertDialog.cancel();
				updateUserSingleGrade(device_idString, grade_id, grade_info_id);
				// Editor editor = gradepref.edit();
				// editor.remove("grade");
				// editor.remove("gradecount");
				// editor.commit();

				Editor editor = gradepref.edit();
				editor.putInt("grade" + pos, pos);
				editor.putInt("gradecount", 1);
				editor.commit();

			}
		});

		alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		alertDialog.show();

	}

	public void updateUserGrade(String device_idString2, int grade_id2,
			int grade_info_id) {
		int i = 0;
		try {
			// jsonCreation = new JSONCreation();
			jsonCreation.savemultigrade(School_Id, "" + grade_id2,
					device_idString2, "" + grade_info_id, Device_Info_Id);
			Log.d(TAG, "savemultigrade-------" + "school_id--------"
					+ School_Id + "----grade_id2----" + grade_id2
					+ "-------grade_info_id-------" + grade_info_id
					+ "-----Device_Info_Id--------" + Device_Info_Id);
			i++;
			Log.d(TAG, "i---------" + i);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void updateUserSingleGrade(String device_idString2, int grade_id2,
			int grade_info_id) {
		int i = 0;
		try {
			jsonCreation = new JSONCreation();
			jsonCreation.savemultigrade(School_Id, "" + grade_id2,
					device_idString2, "" + grade_info_id, Device_Info_Id);
			Log.d(TAG, "savemultigrade-------" + "school_id--------"
					+ School_Id + "----grade_id2----" + grade_id2
					+ "-------grade_info_id-------" + grade_info_id
					+ "-----Device_Info_Id--------" + Device_Info_Id);
			i++;
			Log.d(TAG, "i---------" + i);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private String saveUserMultipleGrade() {
		String resultString = null;
		JSONArray result = null;
		JSONObject json = null;
		JSONObject multigradeobj = jsonCreation.savemultigrade();
		String url2345;
		Log.d(TAG, "multigradeobj----saveUserMultipleGrade--------"
				+ multigradeobj);

		ArrayList<String> stringArray = new ArrayList<String>();

		try {
			if (!multiplechilechkBox.isChecked()
					&& gradelevelbtn.getText().toString().equals("")) {
				stringArray.add("");
				url2345 = Constant.SERVER_URL + "SaveUserGrade" + "?Key="
						+ "123456" + "&UserID=" + UserId + "&InpupGradeList="
						+ stringArray;
			} else {
				JSONArray jsonArray = multigradeobj.getJSONArray("");
				Log.d(TAG, "jsonArray----jsonArray--------" + jsonArray);
				for (int i = 0, count = jsonArray.length(); i < count; i++) {
					try {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						stringArray.add(jsonObject.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				url2345 = Constant.SERVER_URL + "SaveUserGrade" + "?Key="
						+ "123456" + "&UserID=" + UserId + "&InpupGradeList="
						+ stringArray;

				Log.d(TAG, "url2345------------" + url2345);
			}

			try {
				json = jsonParser.getJSONFromUrl(url2345);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			Log.d(TAG, "SaveUserGrade------------" + json);
			JSONObject SaveUserGradeResult = json
					.getJSONObject("SaveUserGradeResult");

			result = SaveUserGradeResult.getJSONArray("UserRegGradeInfo");
			for (int i = 0; i < result.length(); i++) {
				try {
					JSONObject c = result.getJSONObject(i);
					String Device_Info_Id1 = c.getString("Device_Info_Id");
					String Device_UID1 = c.getString("Device_UID");
					String Grade_Id1 = c.getString("Grade_Id");
					String UserId1 = c.getString("UserId");
					String User_Reg_Grade_Info_Id1 = c
							.getString("User_Reg_Grade_Info_Id");
					String User_Reg_Info_Id1 = c.getString("User_Reg_Info_Id");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("Device_Info_Id", Device_Info_Id1);
					map.put("Device_UID", Device_UID1);
					map.put("Grade_Id", Grade_Id1);
					map.put("UserId", UserId1);
					map.put("User_Reg_Grade_Info_Id", User_Reg_Grade_Info_Id1);
					map.put("User_Reg_Info_Id", User_Reg_Info_Id1);
					gradeleveldata.add(map);

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			Messagegrade = SaveUserGradeResult.getString("Message");
			resultString = SaveUserGradeResult.getString("Status");
			Log.d(TAG, "SaveUserGrade-----resultString-----Message--"
					+ resultString + "-------------" + Messagegrade);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return resultString;

	}

	private class MyArrayAdapter extends ArrayAdapter<String> {

		private HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();

		public MyArrayAdapter(Context context, int resource,
				int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);

			for (int i = 0; i < objects.size(); i++) {
				myChecked.put(i, false);
			}
		}

		public void toggleChecked(int position) {
			if (myChecked.get(position)) {
				myChecked.put(position, false);
			} else {
				myChecked.put(position, true);
			}

			notifyDataSetChanged();
		}

		public List<Integer> getCheckedItemPositions() {
			List<Integer> checkedItemPositions = new ArrayList<Integer>();

			for (int i = 0; i < myChecked.size(); i++) {
				if (myChecked.get(i)) {
					(checkedItemPositions).add(i);
				}
			}

			return checkedItemPositions;
		}

		public List<String> getCheckedItems() {
			List<String> checkedItems = new ArrayList<String>();

			for (int i = 0; i < myChecked.size(); i++) {
				if (myChecked.get(i)) {
					(checkedItems).add(grade_level_list.get(i));
				}
			}

			return checkedItems;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.multicheck_item, parent, false);
			}

			CheckedTextView checkedTextView = (CheckedTextView) row
					.findViewById(R.id.text1);
			checkedTextView.setText(grade_level_list.get(position));

			Boolean checked = myChecked.get(position);
			if (checked != null) {
				checkedTextView.setChecked(checked);
			}

			return row;
		}

	}

	public void Dialog_for_registration() {

		try {
			final Dialog alertDialog = new Dialog(ManageRegistration.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.register_dialog);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(),
					"Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);

			TextView alerttxtwindow = (TextView) alertDialog
					.findViewById(R.id.alerttxtwindow);
			TextView aboutprogram = (TextView) alertDialog
					.findViewById(R.id.aboutprogram);
			alerttxtwindow.setText("Manage Account");
			aboutprogram
					.setText("Registration information updated successfully.");
			btnDismiss.setText("Ok");
			alerttxtwindow.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					alertDialog.dismiss();

					startActivity(new Intent(ManageRegistration.this,
							MainActivity.class));

					finish();
				}
			});

			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
