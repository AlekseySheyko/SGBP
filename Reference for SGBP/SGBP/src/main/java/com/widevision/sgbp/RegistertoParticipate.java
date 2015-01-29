package com.widevision.sgbp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.widevision.sgbp.util.AlertDialogManager;
import com.widevision.sgbp.util.ConnectionDetector;
import com.widevision.sgbp.util.Constant;
import com.widevision.sgbp.util.Get_Deviceid;
import com.widevision.sgbp.util.JSONCreation;
import com.widevision.sgbp.util.JSONParser;

@SuppressLint({ "InlinedApi", "NewApi" })
public class RegistertoParticipate extends Activity {
	Button home, reg, cancel;
	TextView titleView;
	LinearLayout fnll, lnll, bottomlytr, schoollayout, gradelevellayout, school_ed;
	TextView textViewg, textViewm, textViewl, textViewp, textViewk, textViewr, textViewe, textViewi, textViewn,
			textVieww, email_textview;
	private Pattern emailPattern;
	EditText emailid, fname111, lname111, dd;
	CheckBox multiplechilechkBox, pushnotificationchkBox, apptouseloactionchkBox, rcvcoupancodechkBox,
			iam18yearoldchkBox;
	ImageView multiplechileImageView, pushnotificationImageView, apptouseloactionImageView, rcvcoupancodeImageView,
			iam18yearoldImageView;
	TextView schoolspinner, gradelevelspinner;
	String keyString, schoolnameString, gradelevelString, emailidString, fnameString, device_idString, lnameString,
			user_typeString, device_infoidString, device_NameString, device_TypeString;
	ScrollView scroolview;
	int device_infoid, user_type, user_id, userRegInfo_id, grade_id, grade_info_id;
	boolean multiplechilechk, pushnotificationchk, apptouseloactionchk, rcvcoupancodechk, iam18yearoldchk,
			userregistered;

	boolean fnameval = false, lnameval = false;

	String multiplechilechkString, pushnotificationchkString, apptouseloactionchkString, rcvcoupancodechkString,
			iam18yearoldchkString, userregisteredString;

	String Resultteam, Resultdevice, Resultgrade, TAG = "RegistertoParticipate", Messageteam, Messagegrade, UserId,
			Device_Info_Id;
	JSONParser jsonParser = new JSONParser();
	int gradeflag = 0, treadflag = 0;
	ArrayAdapter<String> school_namedataAdapter;
	ArrayAdapter<String> grade_leveldataAdapter;

	ArrayList<HashMap<String, String>> schoolnamelistdata = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> gradelevellistdata = new ArrayList<HashMap<String, String>>();
	// ArrayList<HashMap<String, String>> deviceinfodata = new
	// ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> gradeleveldata = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> teammemberinfodata = new ArrayList<HashMap<String, String>>();
	ArrayList<String> school_name_list = new ArrayList<String>();
	ArrayList<String> grade_level_list = new ArrayList<String>();;
	ArrayList<String> school_id_list = new ArrayList<String>();

	String school_id;

	int count;
	Bundle bundle;
	String[] stringArray;
	ProgressBar prgLoading;
	Typeface type;
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	Get_Deviceid get_dv_id;
	// Connection detector
	ConnectionDetector cd;

	JSONCreation jsonCreation = new JSONCreation();
	private Button next, previous, done;
	private LinearLayout bottom1;

	boolean fnameb = true, lnameb = false, emailb = false;

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
			+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "." + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
	SharedPreferences pref, deviceinfoidpref, gradepref, schoolpref, onepref;

	String regId = "";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		regId = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.GCM_ID, "");

		get_dv_id = new Get_Deviceid(getApplicationContext());

		device_idString = get_dv_id.get_unique_deviceid();

		// TelephonyManager telephonyManager = (TelephonyManager)
		// getSystemService(Context.TELEPHONY_SERVICE);
		// device_idString = telephonyManager.getDeviceId() + "12345";

		onepref = getSharedPreferences("one", Context.MODE_PRIVATE);
		school_ed = (LinearLayout) findViewById(R.id.school_ed);
		titleView = (TextView) findViewById(R.id.title);
		scroolview = (ScrollView) findViewById(R.id.scroolview);
		schoolspinner = (TextView) findViewById(R.id.schoolspinner);
		gradelevelspinner = (TextView) findViewById(R.id.gradelevel);
		reg = (Button) findViewById(R.id.reg);
		cancel = (Button) findViewById(R.id.cancel);

		dd = (EditText) findViewById(R.id.dd);
		emailid = (EditText) findViewById(R.id.emailid);
		fname111 = (EditText) findViewById(R.id.fnamenew);
		lname111 = (EditText) findViewById(R.id.lnamenew);
		multiplechilechkBox = (CheckBox) findViewById(R.id.multiplechilechk);
		pushnotificationchkBox = (CheckBox) findViewById(R.id.pushnotificationchk);
		apptouseloactionchkBox = (CheckBox) findViewById(R.id.apptouseloactionchk);
		rcvcoupancodechkBox = (CheckBox) findViewById(R.id.rcvcoupancodechk);
		iam18yearoldchkBox = (CheckBox) findViewById(R.id.iam18yearoldchknew);

		prgLoading = (ProgressBar) findViewById(R.id.prgLoading);

		multiplechileImageView = (ImageView) findViewById(R.id.multiplechileimageView1);
		pushnotificationImageView = (ImageView) findViewById(R.id.pushnotificationimageView1);
		apptouseloactionImageView = (ImageView) findViewById(R.id.apptouseloactionimageView1);
		rcvcoupancodeImageView = (ImageView) findViewById(R.id.rcvcoupancodeimageView1);
		iam18yearoldImageView = (ImageView) findViewById(R.id.iam18yearoldchknewimageView1);

		email_textview = (TextView) findViewById(R.id.email_textview);

		fnll = (LinearLayout) findViewById(R.id.fn_ll);
		lnll = (LinearLayout) findViewById(R.id.ln_ll);
		bottomlytr = (LinearLayout) findViewById(R.id.bottomlytr);

		schoollayout = (LinearLayout) findViewById(R.id.schoollayout);
		gradelevellayout = (LinearLayout) findViewById(R.id.gradelevellayout);

		float alpha = 0.45f;
		AlphaAnimation alphaUp = new AlphaAnimation(alpha, alpha);
		alphaUp.setFillAfter(true);
		fnll.startAnimation(alphaUp);
		lnll.startAnimation(alphaUp);
		fname111.setEnabled(false);
		lname111.setEnabled(false);

		fname111.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if (fnameval) {
					if (s.length() == 20) {
						showMesgDlgfalse("The First Name cannot be more than 20 characters.");
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		lname111.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				Log.d(TAG, "lnameval--------" + s.length());
				if (lnameval) {
					if (s.length() == 20) {
						showMesgDlgfalse("The Last Name cannot be more than 20 characters.");
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		next = (Button) findViewById(R.id.submit1);
		previous = (Button) findViewById(R.id.submit2);
		done = (Button) findViewById(R.id.submit3);
		bottom1 = (LinearLayout) findViewById(R.id.transparent_panel);

		previous.setBackgroundResource(R.drawable.previousunactive);
		next.setBackgroundResource(R.drawable.nextactive);

		final View activityRootView = findViewById(R.id.mainlytreg);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(

		new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();

				Log.d("difference is>>>>>>>>>>>..", "...." + heightDiff);

				if (heightDiff > 100) {
					bottom1.setVisibility(View.VISIBLE);
					bottomlytr.setVisibility(View.GONE);
				} else {
					bottom1.setVisibility(View.GONE);
					bottomlytr.setVisibility(View.VISIBLE);
				}
			}
		});

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
					fnameval = true;
				} else {
					fnameval = false;
				}

			}
		});

		fname111.setOnFocusChangeListener(new OnFocusChangeListener() {

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

					lnameval = true;
				} else {
					lnameval = false;
				}
			}
		});
		lname111.setOnFocusChangeListener(new OnFocusChangeListener() {

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

				}
			}
		});

		bundle = getIntent().getExtras();
		schoolnamelistdata = (ArrayList<HashMap<String, String>>) bundle.get("getschoolnamelistdata");

		Device_Info_Id = bundle.getString("Device_Info_Id_saved");

		for (int i = 0; i < schoolnamelistdata.size(); i++) {

			school_name_list.add(schoolnamelistdata.get(i).get("School_Name"));

		}
		pref = getSharedPreferences("device", Context.MODE_PRIVATE);

		gradepref = getSharedPreferences("grade", Context.MODE_PRIVATE);
		schoolpref = getSharedPreferences("schoolreg", Context.MODE_PRIVATE);
		// device_idString = pref.getString("device_idString", null);
		device_NameString = getDeviceName();

		device_TypeString = android.os.Build.DEVICE;

		// deviceinfoidpref = getSharedPreferences("deviceinfoid",
		// Context.MODE_PRIVATE);
		// Device_Info_Id = deviceinfoidpref.getString("Device_Info_Id", null);
		Log.d(TAG, "...Device_Info_Id--------------." + Device_Info_Id);
		type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");

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
		schoolspinner.setTypeface(type);
		gradelevelspinner.setTypeface(type);
		emailid.setTypeface(type);
		fname111.setTypeface(type);
		lname111.setTypeface(type);

		titleView.setTypeface(type);

		titleView.setText("REGISTRATION");

		previous.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (emailb) {
					emailid.requestFocus();
				} else if (fnameb) {
					emailid.requestFocus();
				} else if (lnameb) {
					fname111.requestFocus();
				}
			}
		});
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.d("multiplechilechkBox.isChecked()>>>>>>>>>>", ">>>>>" + multiplechilechkBox.isChecked());

				if (iam18yearoldchkBox.isChecked()) {
					if (emailb) {
						fname111.requestFocus();
					} else if (fnameb) {
						lname111.requestFocus();
					} else if (lnameb) {
						lname111.requestFocus();
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
		iam18yearoldImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				showAlertDialog(Constant.agemsgchk1, Constant.abtagetitle);
			}
		});

		multiplechileImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				showAlertDialog(Constant.multiplechildmsgchk, Constant.abtmultiplechilttitle);
			}
		});
		pushnotificationImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				showAlertDialog(Constant.pushnotimsgchk, Constant.abtpushnotititle);

			}
		});
		apptouseloactionImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				showAlertDialog(Constant.locationmsgchk, Constant.abtlocationtitle);

			}
		});
		rcvcoupancodeImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				showAlertDialog(Constant.couponmsgchk, Constant.abtcoupontitle);

			}
		});
		home = (Button) findViewById(R.id.home);
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				startActivity(new Intent(RegistertoParticipate.this, MainActivity.class));
				finish();
			}
		});
		schoollayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (school_name_list.isEmpty()) {
					showMesgDlgsc("No value in the school name.");
				} else {
					initiatePopupWindow();
				}
			}
		});
		gradelevellayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (grade_level_list.isEmpty()) {
					showAlertDialogGrade("No grade value in this school name.");
				} else {
					if (multiplechilechkBox.isChecked()) {
						gradeflag = 1;
						gradeinitiatePopupWindow();
					} else {
						singlegradeinitiatePopupWindow();
						gradeflag = 1;
					}
				}
			}
		});
		pushnotificationchkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					showAlertDialog(Constant.pushnotimsgchk, Constant.allowpushnotititle);
				} else {
					showAlertDialog(Constant.pushnotimsgunchk, Constant.allowpushnotititle);
				}
			}
		});

		apptouseloactionchkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					showAlertDialog(Constant.locationmsgchk, Constant.allowlocationtitle);
				} else {
					showAlertDialog(Constant.locationmsgunchk, Constant.allowlocationtitle);
				}
			}
		});

		rcvcoupancodechkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					showAlertDialog(Constant.couponmsgchk, Constant.allowcoupontitle);
				} else {
					showAlertDialog(Constant.couponmsgunchk, Constant.allowcoupontitle);
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(RegistertoParticipate.this, MainActivity.class));
				finish();
			}
		});

		reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Log.e(TAG, "REGistration button clicked--------" );

				cd = new ConnectionDetector(getApplicationContext());

				// Check if Internet present
				if (!cd.isConnectingToInternet()) {
					// Internet Connection is not present
					alert.showAlertDialog(RegistertoParticipate.this, "Oops!!",
							"Internet Connection seems to offline.Please check your network.", false);
					// stop executing code by return
					return;
				} else {
					schoollayout.setEnabled(true);
					gradelevellayout.setEnabled(true);
					multiplechilechkBox.setEnabled(true);
					pushnotificationchkBox.setEnabled(true);
					apptouseloactionchkBox.setEnabled(true);
					apptouseloactionchkBox.setEnabled(true);
					rcvcoupancodechkBox.setEnabled(true);
					iam18yearoldchkBox.setEnabled(true);
					emailid.setEnabled(true);
				}

				lnameString = lname111.getText().toString();
				emailidString = emailid.getText().toString();
				fnameString = fname111.getText().toString();

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
				// if (apptouseloactionchkBox.isChecked()) {
				// apptouseloactionchkString = "true";
				// } else {
				// apptouseloactionchkString = "false";
				// }
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
				if (gradelevelspinner.getText().toString().equals("")) {
					showMesgDlgfalse("Grade level can not be empty");
				} else {

//					Log.d(TAG, "emailidString--------" + emailidString);

					if (emailidString.equals("")) {
						if (apptouseloactionchkBox.isChecked()) {
							apptouseloactionchkString = "true";
							new sendData().execute();
						} else {
							showMesgDlgfalse("Location service is required to register and participate in the School Give Back Program.");

						}
					} else if (eMailValidation(emailidString)) {

						if (apptouseloactionchkBox.isChecked()) {
							apptouseloactionchkString = "true";
							new sendData().execute();
						} else {
							showMesgDlgfalse("Location service is required to register and participate in the School Give Back Program.");

						}

					} else {
						showMesgDlgfalse("Enter valid email id.");
						emailid.requestFocus();

					}
					Log.d(TAG, "checkEmail(emailidString)--------" + eMailValidation(emailidString));
				}

			}
		});
		multiplechilechkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					Log.d(TAG, "-gradeflag----multiplechilechkBox-------" + gradeflag);
					if (gradeflag == 0) {
						showAlertDialogmultiplechild(Constant.multiplechildmsgchk, Constant.allowmultiplechilttitle);
					} else if (gradeflag == 1) {
						gradeflag = 0;
						showAlertDialogmultiplechild(Constant.multiplechildmsgchk, Constant.allowmultiplechilttitle);
					}

					Editor editor = gradepref.edit();
					editor.remove("grade");
					editor.remove("gradecount");
					editor.commit();
					gradelevelspinner.setText("");

				} else {
					gradelevelspinner.setText("");
					if (gradeflag == 0) {
						showAlertDialog(Constant.multiplechildmsgunchk, Constant.allowmultiplechilttitle);
					} else if (gradeflag == 1) {
						gradeflag = 0;
						showAlertDialog(Constant.multiplechildmsgunchk, Constant.allowmultiplechilttitle);
					}
					Editor editor = gradepref.edit();
					editor.remove("grade");
					editor.remove("gradecount");
					editor.commit();
				}
			}
		});
		iam18yearoldchkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {

					showAlertDialogiam(Constant.agemsgchk, Constant.allowagetitle);
					fname111.setEnabled(true);
					lname111.setEnabled(true);
					float alpha = 1.0f;
					AlphaAnimation alphaUp = new AlphaAnimation(alpha, alpha);
					alphaUp.setFillAfter(true);
					fnll.startAnimation(alphaUp);
					lnll.startAnimation(alphaUp);
				}

				else {
					// showAlertDialog(Constant.agemsgunchk,
					// Constant.allowagetitle);
					fname111.setText("");
					lname111.setText("");
					fname111.setEnabled(false);
					lname111.setEnabled(false);
					float alpha = 0.45f;
					AlphaAnimation alphaUp = new AlphaAnimation(alpha, alpha);
					alphaUp.setFillAfter(true);
					fnll.startAnimation(alphaUp);
					lnll.startAnimation(alphaUp);

				}
			}
		});
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(RegistertoParticipate.this, "Oops!!",
					"Internet Connection seems to offline.Please check your network.", false);
			// stop executing code by return
			return;
		} else {
			schoollayout.setEnabled(true);
			gradelevellayout.setEnabled(true);
			multiplechilechkBox.setEnabled(true);
			pushnotificationchkBox.setEnabled(true);
			apptouseloactionchkBox.setEnabled(true);
			apptouseloactionchkBox.setEnabled(true);
			rcvcoupancodechkBox.setEnabled(true);
			iam18yearoldchkBox.setEnabled(true);
			emailid.setEnabled(true);
		}

		/*
		 * final ScheduledExecutorService exec =
		 * Executors.newSingleThreadScheduledExecutor(); Runnable task1 = new
		 * Runnable() { public void run() { treadflag++; if (treadflag == 1) {
		 * exec.shutdown(); exec = Executors.newSingleThreadScheduledExecutor();
		 * exec.scheduleAtFixedRate(task2, 0, 1000, TimeUnit.MILLISECONDS); } }
		 * }; exec.scheduleAtFixedRate(task1, 0, 1000, TimeUnit.MILLISECONDS);
		 * Timer timer2 = new Timer(); timer2.scheduleAtFixedRate(new Task2(),
		 * 0, 1000);
		 */
		/*
		 * final Handler handler = new Handler(); final Runnable r = new
		 * Runnable() { public void run() { if (treadflag == 0) { treadflag = 1;
		 * } Log.d(TAG, "treadflag--------" + treadflag); } };
		 * handler.postDelayed(r, 10);
		 */

		/*
		 * dd.requestFocus(); dd.postDelayed(new Runnable() { public void run()
		 * { bottom1.setVisibility(View.GONE);
		 * bottomlytr.setVisibility(View.VISIBLE); InputMethodManager imm =
		 * (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		 * imm.hideSoftInputFromWindow(bottom1.getWindowToken(), 0); } }, 10);
		 */

		new Reminder(1);
	}

	public class Reminder {
		Timer timer;

		public Reminder(int seconds) {
			timer = new Timer();
			timer.schedule(new RemindTask(), seconds * 300);
		}

		class RemindTask extends TimerTask {
			public void run() {

				// bottom1.setVisibility(View.GONE);
				// bottomlytr.setVisibility(View.VISIBLE);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(bottom1.getWindowToken(), 0);

				System.out.format("Time's up!%n");
				timer.cancel(); // Terminate the timer

			}
		}

	}

	/*
	 * public class Reminder { Timer timer; public Reminder(int seconds) { timer
	 * = new Timer(); timer.schedule(new RemindTask(), seconds * 1000); } class
	 * RemindTask extends TimerTask { public void run() {
	 * bottom1.setVisibility(View.GONE); bottomlytr.setVisibility(View.VISIBLE);
	 * InputMethodManager imm = (InputMethodManager)
	 * getSystemService(Context.INPUT_METHOD_SERVICE);
	 * imm.hideSoftInputFromWindow(bottom1.getWindowToken(), 0);
	 * System.out.format("Time's up!%n"); timer.cancel(); // Terminate the timer
	 * thread } } }
	 */

	private void showAlertDialogiam(String message, String message1) {

		try {
			final Dialog alertDialog = new Dialog(RegistertoParticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);

			TextView alerttxt = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);
			alerttxt.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			aboutprogram.setText(message);
			alerttxt.setText(message1);

			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					fname111.requestFocus();
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {

		startActivity(new Intent(RegistertoParticipate.this, MainActivity.class));
		finish();
		super.onBackPressed();
	}

	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	/*
	 * private boolean checkEmail(String email) { return
	 * EMAIL_ADDRESS_PATTERN.matcher(email).matches(); }
	 */

	public boolean eMailValidation(String emailstring) {
		emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		return emailMatcher.matches();
	}

	public class sendGradeData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
			schoollayout.setEnabled(false);
			gradelevellayout.setEnabled(false);
			multiplechilechkBox.setEnabled(false);
			pushnotificationchkBox.setEnabled(false);
			apptouseloactionchkBox.setEnabled(false);
			apptouseloactionchkBox.setEnabled(false);
			rcvcoupancodechkBox.setEnabled(false);
			iam18yearoldchkBox.setEnabled(false);
			emailid.setEnabled(false);
			reg.setEnabled(false);
		}

		@Override
		protected Void doInBackground(Void... params) {

			getGradeNameList();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (grade_level_list.isEmpty()) {
				showAlertDialogGrade("No grade value in this school name.");
			} else {

				stringArray = grade_level_list.toArray(new String[grade_level_list.size()]);
				grade_leveldataAdapter = new ArrayAdapter<String>(RegistertoParticipate.this,
						android.R.layout.simple_list_item_1, grade_level_list);
				// grade_leveldataAdapter
				// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			}

			prgLoading.setVisibility(View.GONE);
			schoollayout.setEnabled(true);
			gradelevellayout.setEnabled(true);
			multiplechilechkBox.setEnabled(true);
			pushnotificationchkBox.setEnabled(true);
			apptouseloactionchkBox.setEnabled(true);
			apptouseloactionchkBox.setEnabled(true);
			rcvcoupancodechkBox.setEnabled(true);
			iam18yearoldchkBox.setEnabled(true);
			emailid.setEnabled(true);
			reg.setEnabled(true);

		}
	}

	public void showMesgDlg(String msg) {

		try {
			final Dialog alertDialog = new Dialog(RegistertoParticipate.this);
			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
			TextView alerttxt = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);

			aboutprogram.setText(msg);
			aboutprogram.setTypeface(type);
			alerttxt.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					schoollayout.setEnabled(false);
					gradelevellayout.setEnabled(false);
					multiplechilechkBox.setEnabled(false);
					pushnotificationchkBox.setEnabled(false);
					apptouseloactionchkBox.setEnabled(false);
					apptouseloactionchkBox.setEnabled(false);
					rcvcoupancodechkBox.setEnabled(false);
					iam18yearoldchkBox.setEnabled(false);
					emailid.setEnabled(false);
				}
			});
			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void showAlertDialogGrade(String message1) {

		try {
			final Dialog alertDialog = new Dialog(RegistertoParticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);

			TextView alerttxt = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);
			alerttxt.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			aboutprogram.setText(message1);
			// alerttxt.setText(message1);

			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d(TAG, "-gradeflag-----------" + gradeflag);
					if (gradeflag == 0) {

					} else if (gradeflag == 1) {
						multiplechilechkBox.setChecked(false);
						gradeflag = 0;
					}
					gradelevelspinner.setText("");
					alertDialog.dismiss();
				}
			});

			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void getGradeNameList() {

		try {
			JSONObject json = null;
			try {
				Log.d(TAG, "-getGradeNameList-------school_id----" + school_id);
				json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetGradebySchool" + "?Key=123456&School_Id="
						+ school_id);
				if (json.length() == 0) {
					showMesgDlg("Server not found.Please try again later.");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			JSONArray result = null;
			Log.d(TAG, "-GetGradebySchool-----------" + json);
			JSONObject GetSchoolNameResult = json.getJSONObject("GetGradebySchoolResult");
			try {
				result = GetSchoolNameResult.getJSONArray("GradeInfo");
				for (int i = 0; i < result.length(); i++) {

					JSONObject c = result.getJSONObject(i);
					String Grade_Name = c.getString("Grade_Name");
					String Grade_Id = c.getString("Grade_Id");
					String Grade_Info_Id = c.getString("Grade_Info_Id");

					HashMap<String, String> map = new HashMap<String, String>();
					// adding each child node to HashMap key => value
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
					// gradelevellistdata.add(map);
					// grade_level_list.add(Grade_Name);
					Log.d(TAG, "-Grade_Name------gradelevellistdata-----" + Grade_Name);

				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public class sendData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);

			schoollayout.setEnabled(false);
			gradelevellayout.setEnabled(false);
			multiplechilechkBox.setEnabled(false);
			pushnotificationchkBox.setEnabled(false);
			apptouseloactionchkBox.setEnabled(false);
			apptouseloactionchkBox.setEnabled(false);
			rcvcoupancodechkBox.setEnabled(false);
			iam18yearoldchkBox.setEnabled(false);
			emailid.setEnabled(false);
			home.setEnabled(false);
			cancel.setEnabled(false);
			reg.setEnabled(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			Resultteam = getRequest(device_idString, school_id, multiplechilechkString, rcvcoupancodechkString,
					pushnotificationchkString, apptouseloactionchkString, iam18yearoldchkString, emailidString,
					fnameString, lnameString);

			Log.e(TAG, "Result-------" + Resultteam);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			prgLoading.setVisibility(View.GONE);
			multiplechilechkBox.setEnabled(true);
			pushnotificationchkBox.setEnabled(true);
			apptouseloactionchkBox.setEnabled(true);
			apptouseloactionchkBox.setEnabled(true);
			rcvcoupancodechkBox.setEnabled(true);
			iam18yearoldchkBox.setEnabled(true);
			schoollayout.setEnabled(true);
			gradelevellayout.setEnabled(true);
			emailid.setEnabled(true);
			home.setEnabled(true);
			cancel.setEnabled(true);
			reg.setEnabled(true);

			resultAlertTeam(Resultteam);

		}
	}

	public String getRequest(String device_idString2, String school_idString2, String multiplechilechkString2,
			String rcvcoupancodechkString2, String pushnotificationchkString2, String apptouseloactionchkString2,
			String iam18yearoldchkString2, String emailidString2, String fnameString2, String lnameString2) {
		String resultString = "";
		Log.d(TAG, "device_idString2------------" + device_idString2 + school_idString2
				+ "------multiplechilechkString2------------" + multiplechilechkString2
				+ "------rcvcoupancodechkString2------------" + rcvcoupancodechkString2
				+ "------pushnotificationchkString2------------" + pushnotificationchkString2
				+ "------apptouseloactionchkString2------------" + apptouseloactionchkString2
				+ "-----iam18yearoldchkString2------------" + iam18yearoldchkString2
				+ "------emailidString2------------" + emailidString2 + "------fnameString2------------" + fnameString2
				+ "------lnameString2------------" + lnameString2

		);
		JSONArray result = null;
		JSONObject json = null;
		JSONObject SaveTeamMemberInfoResult = null;

		try {
			json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "SaveTeamMemberInfo" + "?Key=" + "123456"
					+ "&Device_Info_Id=" + Device_Info_Id + "&First_Name=" + fnameString2 + "&Last_Name="
					+ lnameString2 + "&Device_UID=" + device_idString2 + "&School_Id=" + school_idString2
					+ "&User_Email=" + emailidString2 + "&User_Type=" + "1" + "&Has_Multiple_Child="
					+ multiplechilechkString2 + "&Is_User_Registered=" + "true" + "&Is_Coupon_Allowed="
					+ rcvcoupancodechkString2 + "&Is_Notification_Allowed=" + pushnotificationchkString2
					+ "&Is_Location_Service_Allowed=" + apptouseloactionchkString2 + "&Is_User_Over_18_Year="
					+ iam18yearoldchkString2 + "&Is_Device_Registered=" + "true" + "&Device_Token_Registration_Id="
					+ regId);
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}

			Log.d(TAG, "-SaveTeamMemberInfo-----------" + json);

			SaveTeamMemberInfoResult = json.getJSONObject("SaveTeamMemberInfoResult");
			result = SaveTeamMemberInfoResult.getJSONArray("UserRegInfo");
			for (int i = 0; i < result.length(); i++) {

				JSONObject c = result.getJSONObject(i);
				UserId = c.getString("User_Reg_Info_Id");
				Log.d(TAG, "-UserId----SaveTeamMemberInfoResult-------" + UserId);
				Device_Info_Id = c.getString("Device_Info_Id");
				Log.d(TAG, "-Device_Info_Id-------SaveTeamMemberInfoResult----" + Device_Info_Id);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("UserId", UserId);
				teammemberinfodata.add(map);

			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			Messageteam = SaveTeamMemberInfoResult.getString("Message");
			resultString = SaveTeamMemberInfoResult.getString("Status");
		} catch (Exception e) {

			e.printStackTrace();
		}

		return resultString;
	}

	public void updateUserSingleGrade(String device_idString2, int grade_id2, int grade_info_id) {
		int i = 0;
		try {
			jsonCreation = new JSONCreation();
			jsonCreation
					.savemultigrade(school_id, "" + grade_id2, device_idString2, "" + grade_info_id, Device_Info_Id);
			Log.d(TAG, "multigrade-------" + "school_id--------" + school_id + "----grade_id2----" + grade_id2
					+ "-------grade_info_id-------" + grade_info_id + "-----Device_Info_Id--------" + Device_Info_Id);
			i++;
			Log.d(TAG, "i---------" + i);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void saveUserGrade(String device_idString2, int grade_id2, int grade_info_id) {
		int i = 0;
		try {
			jsonCreation
					.savemultigrade(school_id, "" + grade_id2, device_idString2, "" + grade_info_id, Device_Info_Id);
			Log.d(TAG, "multigrade-------" + "school_id--------" + school_id + "----grade_id2----" + grade_id2
					+ "-------grade_info_id-------" + grade_info_id + "-----Device_Info_Id--------" + Device_Info_Id);
			i++;
			Log.d(TAG, "i---------" + i);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void resultAlertTeam(String Resultdevice1) {
		Log.e("registration dialog Resultdevice1", "----->" + Resultdevice1);
		if (Resultdevice1.trim().equalsIgnoreCase("true")) {
			if (multiplechilechkBox.isChecked() && gradelevelspinner.getText().toString().equals("")) {
				showMesgDlgfalse("Multiple child is allow.Please fill the grade");
			} else {
				try {
					Resultgrade = saveUserMultipleGrade();
					resultAlertGrade(Resultgrade);
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		} else if (Resultdevice1.trim().equalsIgnoreCase("false")) {

			GetAutoCheckFlagStatus bb = new GetAutoCheckFlagStatus();
			bb.execute();

		}
	}

	public void resultAlertGrade(String Resultgrade1) {
		if (Resultgrade1.trim().equalsIgnoreCase("true")) {
			Dialog_for_registration();

		} else if (Resultgrade1.trim().equalsIgnoreCase("false")) {
			showMesgDlgfalse("Grade Level can not be empty");
		}
	}

	private String saveUserMultipleGrade() {
		String resultString = null;
		JSONArray result = null;
		JSONObject SaveUserGradeResult = null;
		JSONObject json;
		String url2345;
		ArrayList<String> stringArray = new ArrayList<String>();
		try {
			if (!multiplechilechkBox.isChecked() && gradelevelspinner.getText().toString().equals("")) {
				stringArray.add("");
				url2345 = Constant.SERVER_URL + "SaveUserGrade" + "?Key=" + "123456" + "&UserID=" + UserId
						+ "&InpupGradeList=" + stringArray;
			} else {
				JSONObject multigradeobj = jsonCreation.savemultigrade();
				Log.d(TAG, "multigradeobj----saveUserGrade--------" + multigradeobj);

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
			}
			url2345 = Constant.SERVER_URL + "SaveUserGrade" + "?Key=" + "123456" + "&UserID=" + UserId
					+ "&InpupGradeList=" + stringArray;

			json = jsonParser.getJSONFromUrl(url2345);
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}
			Log.d(TAG, "SaveUserGrade------------" + json);
			SaveUserGradeResult = json.getJSONObject("SaveUserGradeResult");

			result = SaveUserGradeResult.getJSONArray("UserRegGradeInfo");
			for (int i = 0; i < result.length(); i++) {
				try {
					JSONObject c = result.getJSONObject(i);
					String Device_Info_Id1 = c.getString("Device_Info_Id");
					String Device_UID1 = c.getString("Device_UID");
					String Grade_Id1 = c.getString("Grade_Id");
					String UserId1 = c.getString("UserId");
					String User_Reg_Grade_Info_Id1 = c.getString("User_Reg_Grade_Info_Id");
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

		} catch (Exception e) {

			e.printStackTrace();
		}
		try {
			Messagegrade = SaveUserGradeResult.getString("Message");
			resultString = SaveUserGradeResult.getString("Status");
			Log.d(TAG, "SaveUserGrade-----resultString-----Message--" + resultString + "-------------" + Messagegrade);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return resultString;

	}

	public void showMesgDlgtrue(String msg) {

		try {
			final Dialog alertDialog = new Dialog(RegistertoParticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
			aboutprogram.setText(msg);
			btnDismiss.setText("Ok");
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					startActivity(new Intent(RegistertoParticipate.this, MainActivity.class));
					finish();
				}
			});

			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void showMesgDlgsc(String msg) {

		try {
			final Dialog alertDialog = new Dialog(RegistertoParticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
			aboutprogram.setText(msg);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					alertDialog.dismiss();

				}
			});
			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void showMesgDlgfalse(String msg) {

		try {
			final Dialog alertDialog = new Dialog(RegistertoParticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
			TextView alerttxt = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);
			alerttxt.setText("Error");
			aboutprogram.setText(msg);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					alertDialog.dismiss();
					Editor editor = onepref.edit();
					editor.putBoolean("register", false);
					editor.commit();

				}
			});
			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void showAlertDialog(String message, String message1) {

		try {
			final Dialog alertDialog = new Dialog(RegistertoParticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);

			TextView alerttxt = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);
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
			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void showAlertDialogmultiplechild(String message, String message1) {

		try {
			final Dialog alertDialog = new Dialog(RegistertoParticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
			TextView alerttxt = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);
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
			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	protected void initiatePopupWindow() {
		final Dialog alertDialog = new Dialog(RegistertoParticipate.this);

		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		alertDialog.setContentView(R.layout.school_popup);

		ListView schoollist = (ListView) alertDialog.findViewById(R.id.schoollist);
		alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		Log.d("school_name_list---------", "" + school_name_list.size());
		school_namedataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				android.R.id.text1, school_name_list);
		schoollist.setAdapter(school_namedataAdapter);

		schoollist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

				school_id = schoolnamelistdata.get(pos).get("School_Id").toString();
				schoolnameString = arg0.getItemAtPosition(pos).toString();

				Log.d("school_id---------", "" + school_id);
				Log.d("school_name---------", schoolnameString);

				try {
					// if (schoolpref.getInt("pos", 0) == pos) {
					// Log.d(TAG, "schoolpref.getInt(pos, 0)---------if-----"
					// + schoolpref.getInt("pos", 0));
					// } else {
					Editor editor = gradepref.edit();
					int count = gradepref.getInt("gradecount", 0);
					for (int i = 0; i < count; ++i) {
						editor.remove("grade" + i);
					}
					editor.remove("gradecount");
					editor.commit();
					Log.d(TAG, "schoolpref.getInt(pos, 0)--------else-----" + schoolpref.getInt("pos", 0));
					// }
					Editor editor1 = schoolpref.edit();
					editor1.putInt("school", pos);
					editor1.commit();
				} catch (Exception e) {

					e.printStackTrace();
				}

				if (multiplechilechkBox.isChecked()) {
					gradelevelspinner.setText("");
					grade_level_list.clear();
				}

				gradelevelspinner.setText("");
				grade_level_list.clear();
				new sendGradeData().execute();
				schoolspinner.setText(schoolnameString);

				alertDialog.cancel();
			}
		});
		alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		alertDialog.show();
	}

	int flag = 0;
	MyArrayAdapter myArrayAdapter;

	protected void gradeinitiatePopupWindow() {

		try {
			// final EditText edit3;

			final Dialog alertDialog = new Dialog(RegistertoParticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.multiple_grade_popup);
			ListView multigradelist = (ListView) alertDialog.findViewById(R.id.multigradelist);

			Button donebtn = (Button) alertDialog.findViewById(R.id.donebtn);
			jsonCreation = new JSONCreation();
			myArrayAdapter = new MyArrayAdapter(this, R.layout.multicheck_item, android.R.id.text1, grade_level_list);
			multigradelist.setAdapter(myArrayAdapter);
			int count = gradepref.getInt("gradecount", 0);
			Log.e(TAG, "count---------------" + count);
			if (!(count == 0)) {
				for (int i = 0; i < count; ++i) {
					myArrayAdapter.toggleChecked(gradepref.getInt("grade" + i, 0));
				}
			}
			multigradelist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					flag = 1;
					myArrayAdapter.toggleChecked(position);

				}
			});
			donebtn.setTypeface(type);
			alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

			alertDialog.show();

			donebtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					cd = new ConnectionDetector(getApplicationContext());

					// Check if Internet present
					if (!cd.isConnectingToInternet()) {
						// Internet Connection is not present
						alert.showAlertDialog(RegistertoParticipate.this, "Oops!!",
								"Internet Connection seems to offline.Please check your network.", false);
						// stop executing code by return
						return;
					} else {
						schoollayout.setEnabled(true);
						gradelevellayout.setEnabled(true);
						multiplechilechkBox.setEnabled(true);
						pushnotificationchkBox.setEnabled(true);
						apptouseloactionchkBox.setEnabled(true);
						apptouseloactionchkBox.setEnabled(true);
						rcvcoupancodechkBox.setEnabled(true);
						iam18yearoldchkBox.setEnabled(true);
						emailid.setEnabled(true);
					}
					alertDialog.cancel();

					// String result = "";

					// getCheckedItemPositions
					List<Integer> resultListint = myArrayAdapter.getCheckedItemPositions();
					for (int i = 0; i < resultListint.size(); i++) {
						// result += String.valueOf(resultListint.get(i)) + " ";
					}

					// getCheckedItems
					List<String> resultList = myArrayAdapter.getCheckedItems();
					/*
					 * //getCheckedItems List<String> resultList =
					 * myArrayAdapter.getCheckedItems(); for(int i = 0; i <
					 * resultList.size(); i++){ combineYears += ", "; result +=
					 * String.valueOf(resultList.get(i)) + ", "; }
					 */

					String combineGrade = "";
					Log.e("for...", ">>>>>>selectedGrade>>>>>>" + resultList.size());
					for (int i = 0; i < resultList.size(); ++i) {
						combineGrade += resultList.get(i);
						if (i < resultList.size() - 1) {

							combineGrade += ", ";
						} // if

					}

					gradelevelString = combineGrade;
					myArrayAdapter.getCheckedItemPositions().toString();
					// jsonCreation = new JSONCreation();
					/*
					 * Toast.makeText( getApplicationContext(), combineGrade,
					 * Toast.LENGTH_LONG).show();
					 */
					Log.d(TAG, "grade_level_list.size()----if-------" + grade_level_list.size());
					for (int i = 0; i < resultListint.size(); ++i) {
						grade_id = Integer.parseInt(gradelevellistdata.get(resultListint.get(i)).get("Grade_Id")
								.toString());
						grade_info_id = Integer.parseInt(gradelevellistdata.get(resultListint.get(i))
								.get("Grade_Info_Id").toString());
						Log.d(TAG, "resultListint.get(i)--------" + resultListint.get(i));
						saveUserGrade(device_idString, grade_id, grade_info_id);
						Log.d(TAG, "gradelevelString-" + gradelevelString);
						gradelevelspinner.setText(gradelevelString);

						Editor editor = gradepref.edit();
						editor.putInt("grade" + i, resultListint.get(i));
						editor.putInt("gradecount", resultListint.size());
						editor.commit();

					}
					if (resultListint.size() == 0) {
						gradelevelspinner.setText("");
						Editor editor = gradepref.edit();
						editor.remove("grade");
						editor.remove("gradecount");
						editor.commit();

					} else {
						gradelevelspinner.setText(gradelevelString);
					}
				}
			});
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	protected void singlegradeinitiatePopupWindow() {

		final Dialog alertDialog = new Dialog(RegistertoParticipate.this);

		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		alertDialog.setContentView(R.layout.singlegrade_popup);

		ListView singlegradelist = (ListView) alertDialog.findViewById(R.id.singlegradelist);

		alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		singlegradelist.setAdapter(grade_leveldataAdapter);

		singlegradelist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				gradelevelString = arg0.getItemAtPosition(pos).toString();
				grade_id = Integer.parseInt(gradelevellistdata.get(pos).get("Grade_Id").toString());
				grade_info_id = Integer.parseInt(gradelevellistdata.get(pos).get("Grade_Info_Id").toString());
				Log.d(TAG, "pos--------" + pos);
				Log.d(TAG, "gradelevelString-" + gradelevelString);
				gradelevelspinner.setText(gradelevelString);
				alertDialog.cancel();
				updateUserSingleGrade(device_idString, grade_id, grade_info_id);

				Editor editor = gradepref.edit();
				editor.putInt("grade" + pos, pos);
				editor.putInt("gradecount", 1);
				editor.commit();

			}
		});

		alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		alertDialog.show();
	}

	private class MyArrayAdapter extends ArrayAdapter<String> {

		private HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();

		public MyArrayAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
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

			CheckedTextView checkedTextView = (CheckedTextView) row.findViewById(R.id.text1);
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
			final Dialog alertDialog = new Dialog(RegistertoParticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.register_dialog);
			alertDialog.setCancelable(true);
			type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView alerttxtwindow = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
			alerttxtwindow.setText("Register Account");
			aboutprogram
					.setText("Registration is complete. Thank you for registering with the School Give Back Program.");
			btnDismiss.setText("Ok");
			alerttxtwindow.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					startActivity(new Intent(RegistertoParticipate.this, MainActivity.class));
					finish();

					Editor editor = onepref.edit();
					editor.putBoolean("register", true);
					editor.commit();
				}
			});

			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public class GetAutoCheckFlagStatus extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

			// prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(Void... params) {
			String Automatic_Check_Flag_Status = getAutoCheckFlag();
			Log.e("24 hours service response>>>>>>", "-------" + Automatic_Check_Flag_Status);
			return Automatic_Check_Flag_Status;
		}

		@Override
		protected void onPostExecute(String result) {

			PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.AUTOMATIC_CHECK_FLAG, result);
			if (result.equalsIgnoreCase("true")) {

				Log.w("Participation_service--->", "  started now-------in registration");
				
				Intent intent2 = new Intent(getApplicationContext(), Auto_flag_status_check_service.class);
				startService(intent2);
//				Intent intent2 = new Intent(getApplicationContext(), Participation_service.class);
//				startService(intent2);

				showMesgDlgfalse(Messageteam);

			} else {

				try {
					
					Intent intent2 = new Intent(getApplicationContext(), Auto_flag_status_check_service.class);
					startService(intent2);
//					Intent intent1 = new Intent(getApplicationContext(), Participation_service.class);
//					stopService(intent1);
				} catch (Exception e) {

					e.printStackTrace();
				}

				showMesgDlgfalse(Messageteam);
			}

		}
	}

	public String getAutoCheckFlag() {

		String resultString = "";
		JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetAutoCheckFlag");
		// Log.d("GetAutoCheckFlag------>>>>>>>>", "------------" + json);
		try {
			JSONArray AutomaticCheckFlag = json.getJSONObject("GetAutoCheckFlagResult").getJSONArray(
					"AutomaticCheckFlag");
			for (int i = 0; i < AutomaticCheckFlag.length(); i++) {
				JSONObject jo = AutomaticCheckFlag.getJSONObject(i);
				resultString = jo.getString("Automatic_Check_Flag");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultString;

	}

}
