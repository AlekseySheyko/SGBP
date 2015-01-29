package com.widevision.sgbp;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.widevision.sgbp.util.AlertDialogManager;
import com.widevision.sgbp.util.ConnectionDetector;
import com.widevision.sgbp.util.Constant;
import com.widevision.sgbp.util.Get_Deviceid;
import com.widevision.sgbp.util.JSONCreation;
import com.widevision.sgbp.util.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

@SuppressLint("NewApi")
public class Findparticipatingbusiness extends FragmentActivity {
    Dialog dialog1;
    Button home, mapview, listview, get_direction;
    // ImageView mapview;
    Button milespinner;
    // Handler handler;
    // static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    // static final LatLng KIEL = new LatLng(53.551, 9.993);

    private GoogleMap googleMap;
    private SupportMapFragment mMapFragment;
    private ViewGroup infoWindow;
    private TextView titletxt;
    private Button phonetxt;
    private Button directionButton;
    private OnInfoWindowElemTouchListener infoButtonListener, infoButtonListener1;
    boolean IsLocationServiceAllowed;
    MapWrapperLayout mapWrapperLayout;
    TextView titleView;

    ListView storelistview;

    ProgressBar prgLoading;

    String TAG = "Findparticipatingbusiness", Result, title, phone, UserId, device_idString, miles = "2";
    int UserIdint;
    JSONParser jsonParser = new JSONParser();
    JSONCreation jsonCreation;
    boolean IsStorePaticipating;
    ArrayList<HashMap<String, String>> getParticipatingBusinessInfo;

    double latitude, storelatitude;
    double longitude, storelongitude, distance, mindistance;
    ArrayList<Double> distanceList = new ArrayList<Double>();

    Typeface type;

    Get_Deviceid get_dv_id;

    StoreBean bean;
    StoreListAdapter storeListAdapter;

    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();
    //
    // Connection detector
    ConnectionDetector cd;

    ArrayList<Object> storelistArrayList = new ArrayList<Object>();
    SharedPreferences pref, userdpref, prefd;
    private String Is_Location_Service_Allowed;

    @SuppressLint("InlinedApi")
    public void showMesgDlg(String msg) {
        try {
            final Dialog alertDialog = new Dialog(Findparticipatingbusiness.this);

            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.popupwindow);
            alertDialog.setCancelable(true);
            Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
            Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
            TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
            TextView alerttxtwindow = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);
            aboutprogram.setText(msg);
            aboutprogram.setTypeface(type);
            alerttxtwindow.setTypeface(type);
            btnDismiss.setTypeface(type);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    mapview.setEnabled(false);
                    listview.setEnabled(false);
                    milespinner.setEnabled(false);
                }
            });
            alertDialog.getWindow().setGravity(Gravity.CENTER);
            alertDialog.show();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findparticipatingbusiness);

        // handler = new Handler();

        milespinner = (Button) findViewById(R.id.milesspinner);
        titleView = (TextView) findViewById(R.id.title);
        storelistview = (ListView) findViewById(R.id.storelistview);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        prefd = getSharedPreferences("device", Context.MODE_PRIVATE);

        get_dv_id = new Get_Deviceid(getApplicationContext());

        device_idString = get_dv_id.get_unique_deviceid();

        // TelephonyManager telephonyManager = (TelephonyManager)
        // getSystemService(Context.TELEPHONY_SERVICE);
        // device_idString = telephonyManager.getDeviceId();

        // device_idString = prefd.getString("device_idString", null);

        pref = getSharedPreferences("milevalue", Context.MODE_PRIVATE);
        userdpref = getSharedPreferences("userid", Context.MODE_PRIVATE);
        UserId = userdpref.getString("UserId", null);
        try {
            UserIdint = Integer.parseInt(UserId);
        } catch (Exception e1) {

            e1.printStackTrace();
        }
        My_Current_Location gpsTracker = new My_Current_Location(Findparticipatingbusiness.this);

        if (gpsTracker.canGetLocation()) {
            try {
                Location location1 = gpsTracker.getLocation();
                // Log.d("location----------", "" + location1);

                latitude = gpsTracker.getLocation().getLatitude();
                longitude = gpsTracker.getLocation().getLongitude();

                // latitude = 22.75071;
                // longitude = 75.89542;

                //				 latitude = 39.162461;
                //				 longitude = -119.761963;

                // Reno City
                // latitude = 39.529633;
                // longitude = -119.813803;

                Log.d(TAG, "latitude---------" + latitude);
                Log.d(TAG, "longitude-------" + longitude);
                // Toast.makeText(getApplicationContext(), "latitude->"+latitude
                // +"\n" + "longitude->"+longitude, 7000).show();
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {
            try {
                gpsTracker.showSettingsAlert();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        get_direction = (Button) findViewById(R.id.get_direction);
        type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
        titleView.setTypeface(type);
        titleView.setText("LIST VIEW");

        home = (Button) findViewById(R.id.home);

        mapview = (Button) findViewById(R.id.mapview);

        listview = (Button) findViewById(R.id.listview);

        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);

        // mapview.setTypeface(type);
        listview.setTypeface(type);
        // milespinner.setTypeface(type);
        miles = pref.getString("mile", "2");
        milespinner.setText(miles);

        home.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(Findparticipatingbusiness.this, MainActivity.class));

                finish();
            }
        });
        milespinner.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                initiatePopupWindow();
            }
        });

        // mapview.setBackgroundResource(R.drawable.mapviewselected);
        mapview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // mapview.setColorFilter(Color.argb(150, 155, 155, 155));
                //
                // final Runnable r = new Runnable() {
                //
                // public void run() {
                // mapview.setBackgroundResource(R.drawable.mapview);
                // handler.postDelayed(this, 1000);
                // }
                // };
                // handler.postDelayed(r, 100);

                titleView.setText("MAP VIEW");
                RefreshMap();
                mapview.setBackgroundResource(R.drawable.map_back);
                listview.setBackgroundResource(R.drawable.list_back_dull);
                mapWrapperLayout.setVisibility(View.VISIBLE);
                storelistview.setVisibility(View.GONE);

            }
        });
        listview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    titleView.setText("LIST VIEW");
                    mapWrapperLayout.setVisibility(View.GONE);
                    storelistview.setVisibility(View.VISIBLE);
                    listview.setBackgroundResource(R.drawable.list_back);
                    mapview.setBackgroundResource(R.drawable.map_back_dull);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });

        storelistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }
        });

        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(Findparticipatingbusiness.this, "Oops!!",
                    "Internet Connection seems to offline.Please check your network.", false);
            // stop executing code by return
            return;
        } else {
            try {
                // new GetLocationConsent().execute();

                Is_Location_Service_Allowed = PreferenceConnector.readString(getApplicationContext(),
                        PreferenceConnector.Is_Location_Service_Allowed, "false");

                if (Is_Location_Service_Allowed.equalsIgnoreCase("true")) {
                    new GetParticipatingBusinessInfo().execute();
                } else {
                    showAlertDialog();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        /*
		 * connected = isInternetOn(); connected1 = wifiConnectivity(); if
		 * ((connected == false) || (connected1 == false)) {
		 * showMesgDlg("Please check your internet connection and try again later."
		 * ); } else { try { new GetParticipatingBusinessInfo().execute(); }
		 * catch (Exception e) { e.printStackTrace(); } }
		 */

    }

	/*
	 * public class GetLocationConsent extends AsyncTask<Void, Void, Void> {
	 * 
	 * @Override protected void onPreExecute() { }
	 * 
	 * @Override protected Void doInBackground(Void... params) {
	 * IsLocationServiceAllowed = getLocationConsent(); Log.e(TAG,
	 * "Result-------" + Result); Log.e(TAG, "IsLocationServiceAllowed-------" +
	 * IsLocationServiceAllowed); return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) {
	 * prgLoading.setVisibility(View.GONE); try { if (IsLocationServiceAllowed)
	 * { new GetParticipatingBusinessInfo().execute(); } else {
	 * showAlertDialog(); } } catch (Exception e) { e.printStackTrace(); } } }
	 * public boolean getLocationConsent() { boolean
	 * IsLocationServiceAllowedreturn = false; JSONObject json =
	 * jsonParser.getJSONFromUrl(Constant.SERVER_URL +
	 * "GetLocationConsent?Device_UID=" + device_idString); Log.d(TAG,
	 * "-GetLocationConsent-----------" + json); if (json.length() == 0) {
	 * showMesgDlg("Server not found.Please try again later."); } JSONObject
	 * GetLocationConsentResult = null; try { GetLocationConsentResult = json
	 * .getJSONObject("GetLocationConsentResult"); } catch (Exception e1) {
	 * e1.printStackTrace(); } try { boolean IsLocationServiceAllowed =
	 * GetLocationConsentResult .getBoolean("IsLocationServiceAllowed");
	 * IsLocationServiceAllowedreturn = IsLocationServiceAllowed; } catch
	 * (Exception e) { e.printStackTrace(); } return
	 * IsLocationServiceAllowedreturn; }
	 */

    private void showAlertDialog() {

        try {

            final Dialog alertDialog = new Dialog(Findparticipatingbusiness.this);

            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.clickhere);
            alertDialog.setCancelable(true);
            Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
            Button dontallow = (Button) alertDialog.findViewById(R.id.dontallow);
            Button allow = (Button) alertDialog.findViewById(R.id.allow);

            TextView program = (TextView) alertDialog.findViewById(R.id.program);
            TextView alerttxtclick = (TextView) alertDialog.findViewById(R.id.alerttxtclick);
            alerttxtclick.setText("Find Participating Business");
            program.setText("To find participating businesses you need Location Service feature enabled. Do you want to allow application to use the Location Service?");
            program.setTypeface(type);
            alerttxtclick.setTypeface(type);
            dontallow.setTypeface(type);
            allow.setTypeface(type);
            dontallow.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();

                    PreferenceConnector.writeString(getApplicationContext(),
                            PreferenceConnector.Is_Location_Service_Allowed, "false");
                    startActivity(new Intent(Findparticipatingbusiness.this, MainActivity.class));

                    finish();
                }
            });

            allow.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                        new GetParticipatingBusinessInfo().execute();
                        new UpdateLocationConsent().execute();

						/*
						 * My_Current_Location gpsTracker = new
						 * My_Current_Location( Findparticipatingbusiness.this);
						 * if (gpsTracker.canGetLocation()) { Location location1
						 * = gpsTracker.getLocation();
						 * Log.d("location----------", "" + location1); latitude
						 * = gpsTracker.getLocation().getLatitude(); longitude =
						 * gpsTracker.getLocation().getLongitude(); } else {
						 * gpsTracker.showSettingsAlert(); }
						 */
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    alertDialog.dismiss();
                }
            });
            alertDialog.getWindow().setGravity(Gravity.CENTER);
            alertDialog.show();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public class UpdateLocationConsent extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            prgLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            String status = UpdateLocationConsent();
            // Log.e(TAG, "status-------" + status);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            prgLoading.setVisibility(View.GONE);
            PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Location_Service_Allowed,
                    "true");
        }
    }

    private String UpdateLocationConsent() {

        String status = null;
        try {
            JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "UpdateLocationConsent" + "?User_Id="
                    + UserIdint + "&Is_Location_Service_Allowed=true");
            // Log.d(TAG, "UpdateLocationConsent---------" + json);
            JSONObject UpdateLocationConsentResult = json.getJSONObject("UpdateLocationConsentResult");
            status = UpdateLocationConsentResult.getString("Status");
        } catch (Exception e) {

            e.printStackTrace();
        }
        return status;
    }

	/**/

    @Override
    public void onBackPressed() {

        startActivity(new Intent(Findparticipatingbusiness.this, MainActivity.class));

        finish();
    }

    private void addMarkersToMap() {

        try {
            googleMap.clear();
            JSONObject storeObject = jsonCreation.jsobparse();
            Log.d("jsonCreation.jsobparse()-------------", "" + jsonCreation.jsobparse());
            JSONArray result = null;
            LatLng ll = null;

            result = storeObject.getJSONArray("storedata");
            // Log.d(TAG, "StoreInfo-------" + result);

            for (int i = 0; i < result.length(); i++) {
                JSONObject c = result.getJSONObject(i);
                String Store_lat = c.getString("lat");
                String Store_lng = c.getString("lng");
                String Store_name = c.getString("name");
                String Store_phone = c.getString("phone");
                String Store_add = c.getString("add");
                String Store_add2 = c.getString("add2");
                String Is_storelocation_Physical = c.getString("is_storelocation_Physical");

                if (Is_storelocation_Physical.equalsIgnoreCase("false")) {

                    Log.d("jsonCreation.Is_storelocation_Physical-------------", "" + Is_storelocation_Physical);

                } else {

                    double Store_lat_double = Double.parseDouble(Store_lat);
                    double Store_lng_double = Double.parseDouble(Store_lng);

                    if (Store_lat_double == 0.0 && Store_lng_double == 0.0) {

                    } else {
                        ll = new LatLng(Store_lat_double, Store_lng_double);
                    }

                    googleMap.addMarker(new MarkerOptions().position(ll)
                            .title(Store_name + "\n" + Store_add + "\n" + Store_add2).snippet(Store_phone)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin)));
                }

            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 10));

            googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
                @Override
                public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
                    return null;
                }

                @Override
                public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {

                    title = marker.getTitle();
                    phone = marker.getSnippet();
                    if (!title.equals("null")) {
                        titletxt.setText(title);
                    }
                    if (!phone.equals("null")) {
                        phonetxt.setText(phone);
                    }
                    infoButtonListener.setMarker(marker);
                    mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                    return infoWindow;
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
            googleMap = mMapFragment.getMap();
            mapWrapperLayout.init(googleMap, getPixelsFromDp(this, 39 + 20));

            this.infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.markerdialog, null);
            this.titletxt = (TextView) infoWindow.findViewById(R.id.titletxt);
            this.phonetxt = (Button) infoWindow.findViewById(R.id.phonetxt);
            this.directionButton = (Button) infoWindow.findViewById(R.id.getdirectionbtn);

            this.phonetxt.setTypeface(type);
            this.titletxt.setTypeface(type);
            this.directionButton.setTypeface(type);

            this.infoButtonListener = new OnInfoWindowElemTouchListener(directionButton) {

                @Override
                protected void onClickConfirmed(View v, Marker marker2) {

                    LatLng latlng = marker2.getPosition();

                    double dlattd = latlng.latitude;
                    double dlongtd = latlng.longitude;
                    String urlString = "http://maps.google.com/maps?f=d&hl=en&saddr=" + latitude + "," + longitude
                            + "&daddr=" + dlattd + "," + dlongtd;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    intent.setComponent(new ComponentName("com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity"));
                    startActivity(intent);
                }
            };
            this.directionButton.setOnTouchListener(infoButtonListener);

            this.infoButtonListener1 = new OnInfoWindowElemTouchListener(phonetxt) {
                @Override
                protected void onClickConfirmed(View v, Marker marker2) {

                    if (phonetxt.getText().toString().length() > 0) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phonetxt.getText().toString()));
                        startActivity(callIntent);

                    } else {

                    }

                }
            };
            this.phonetxt.setOnTouchListener(infoButtonListener1);

            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected void RefreshMap() {
        try {
            googleMap.clear();
            addMarkersToMap();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void setUpMap() {
        // Hide the zoom controls as the button panel will cover it.
        googleMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        addMarkersToMap();
    }

    public class GetParticipatingBusinessInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            prgLoading.setVisibility(View.VISIBLE);
            mapview.setEnabled(false);
            listview.setEnabled(false);
            milespinner.setEnabled(false);
            jsonCreation = new JSONCreation();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getParticipatingBusinessInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            prgLoading.setVisibility(View.GONE);
            mapview.setEnabled(true);
            listview.setEnabled(true);
            milespinner.setEnabled(true);
            try {
                setUpMapIfNeeded();
            } catch (Exception e) {

                e.printStackTrace();
            }
            storeListAdapter = new StoreListAdapter(Findparticipatingbusiness.this, getParticipatingBusinessInfo);
            storelistview.setAdapter(storeListAdapter);
            RefreshMap();
        }
    }

    public void getParticipatingBusinessInfo() {

        getParticipatingBusinessInfo = new ArrayList<HashMap<String, String>>();
        JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetParticipatingBusinessInfo");
        try {
            if (json.length() == 0) {
                showMesgDlg("Server not found.Please try again later.");
            }
        } catch (Exception ignored) {
        }

        JSONArray result = null;

        JSONObject GetParticipatingBusinessInfoResult = null;
        JSONObject c = null;
        try {
            GetParticipatingBusinessInfoResult = json.getJSONObject("GetParticipatingBusinessInfoResult");
        } catch (Exception e2) {

            e2.printStackTrace();
        }

        try {
            result = GetParticipatingBusinessInfoResult.getJSONArray("StoreInfo");
        } catch (Exception e3) {

            e3.printStackTrace();
        }
        for (int i = 0; i < result.length(); i++) {

            try {
                c = result.getJSONObject(i);
            } catch (Exception e2) {

                e2.printStackTrace();
            }

            try {
                // Log.d(TAG,"--Store_Name-------" + c.getString("Store_Name") +
                // "----Is_Store_Participating----"
                // + c.getString("Is_Store_Participating"));
            } catch (Exception e2) {

                e2.printStackTrace();
            }

            String IsStorePaticipating = null;
            try {
                IsStorePaticipating = c.getString("Is_Store_Participating");
            } catch (Exception e2) {

                e2.printStackTrace();
            }
            if (IsStorePaticipating.equalsIgnoreCase("true")) {
                String Store_Phone = null;
                try {

                    Store_Phone = c.getString("Store_Phone");

                } catch (Exception e1) {

                    e1.printStackTrace();
                }
                String Store_Name = null;
                try {

                    Store_Name = c.getString("Store_Name");

                    // Log.d(TAG, "--Store_Name-------" + Store_Name);

                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                String Is_storelocation_Physical = "";
                try {

                    Is_storelocation_Physical = c.getString("Is_Store_Location_Physical");

                    // Log.d(TAG, "--Is_storelocation_Physical-------" +
                    // Is_storelocation_Physical);

                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                String Store_Address_Latitude = null;
                try {

                    Store_Address_Latitude = c.getString("Store_Address_Latitude");
                    storelatitude = Double.parseDouble(Store_Address_Latitude);

                } catch (Exception e1) {

                    e1.printStackTrace();
                }
                String Store_Address_Longitude = null;
                try {

                    Store_Address_Longitude = c.getString("Store_Address_Longitude");
                    storelongitude = Double.parseDouble(Store_Address_Longitude);

                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                String Store_Address_Line1 = null;
                try {

                    Store_Address_Line1 = c.getString("Store_Address_Line1");

                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                String Store_City = null;
                try {

                    Store_City = c.getString("Store_City");

                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                String Store_State = null;
                try {

                    Store_State = c.getString("Store_State");

                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                String Store_Zip = null;
                try {

                    Store_Zip = c.getString("Store_Zip");

                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                distance = distance(latitude, longitude, storelatitude, storelongitude);
                Double newdistance = null;
                // Log.d(TAG, "newdistance-------" + newdistance);

                // Log.d("distance----------", Store_Name + "---------" +
                // distance);
                newdistance = distance;
                if (distance > Float.parseFloat(miles)) {
                } else {

                    // Log.d("distance----------", "" + distance);
                    newdistance = distance;
                    distanceList.add(newdistance);

                    jsonCreation.jsonget(Store_Address_Latitude, Store_Address_Longitude, Store_Name, Store_Phone,
                            Store_Address_Line1, i, Store_City + "," + Store_State + " " + Store_Zip,
                            Is_storelocation_Physical);
                    float store_lat = 0;
                    float store_lon = 0;
                    try {
                        store_lat = Float.parseFloat(Store_Address_Latitude);
                        store_lon = Float.parseFloat(Store_Address_Longitude);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    Log.e(TAG, "Store_Name------->-" + Store_Name + " Store_Address_Latitude------>" + store_lat
                            + "Store_Address_Longitude----------->" + store_lon);
                    HashMap<String, String> map = new HashMap<String, String>();
                    try {
                        if (!c.getString("Store_Name").equals("null")) {
                            map.put("Store_Name", Store_Name);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    map.put("Is_storelocation_Physical", Is_storelocation_Physical);
                    map.put("Distance", "" + newdistance);

                    try {
                        if (!c.getString("Store_Phone").equals("null")) {
                            map.put("Store_Phone", Store_Phone);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    map.put("Store_Address_Latitude", Store_Address_Latitude);
                    map.put("Store_Address_Longitude", Store_Address_Longitude);
                    try {
                        if (!c.getString("Store_Address_Line1").equals("null")) {
                            map.put("Store_Address_Line1", Store_Address_Line1);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    try {
                        if (!c.getString("Store_City").equals("null")) {
                            map.put("Store_City_state_zip", Store_City + "," + Store_State + " " + Store_Zip);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    getParticipatingBusinessInfo.add(map);

                    Collections.sort(getParticipatingBusinessInfo, new Comparator<HashMap<String, String>>() {
                        @Override
                        public int compare(HashMap<String, String> a, HashMap<String, String> b) {
                            return a.get("Store_Name").compareTo(b.get("Store_Name"));
                        }
                    });

                }
            }

        }

    }

    public class StoreListAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<HashMap<String, String>> data;
        String miles;

        StoreListAdapter(Context context, ArrayList<HashMap<String, String>> data) {
            ctx = context;
            this.data = data;
            // this.miles = miles;
            lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final String address_line1, address_line2;

            View view = convertView;
            view = lInflater.inflate(R.layout.storelistitem, parent, false);

            Typeface type = Typeface.createFromAsset(ctx.getAssets(), "Candara Bold Italic.ttf");
            Typeface type1 = Typeface.createFromAsset(ctx.getAssets(), "Papyrus-Regular.ttf");

            TextView store_name;
            final TextView store_add;
            final TextView store_phone;
            TextView store_miles;
            TextView miles;
            ImageView arrowImageView;

            store_name = (TextView) view.findViewById(R.id.store_name);
            store_add = (TextView) view.findViewById(R.id.store_add);
            store_phone = (TextView) view.findViewById(R.id.store_phone);
            store_miles = (TextView) view.findViewById(R.id.store_miles);
            miles = (TextView) view.findViewById(R.id.miles);
            arrowImageView = (ImageView) view.findViewById(R.id.arrowimgview);
            Button getdirection = (Button) view.findViewById(R.id.get_direction);
            getdirection.setTypeface(type);

            store_name.setTypeface(type);
            store_add.setTypeface(type);
            store_phone.setTypeface(type1);
            store_miles.setTypeface(type);
            miles.setTypeface(type);

            // Log.d("data---------", "" + data.size() + "-----------" +
            // data.get(position).get("Store_Name") + ",,,,,"
            // + data.get(position).get("Is_storelocation_Physical"));

            store_name.setText(data.get(position).get("Store_Name"));
            store_phone.setText(data.get(position).get("Store_Phone"));
            store_miles.setText(data.get(position).get("Distance"));
            if (data.get(position).get("Is_storelocation_Physical").equalsIgnoreCase("false")) {

                store_miles.setVisibility(View.GONE);
                getdirection.setVisibility(View.GONE);
                miles.setText("Mobile Store");

            } else {
                store_miles.setVisibility(View.VISIBLE);
                getdirection.setVisibility(View.VISIBLE);

            }

            address_line1 = data.get(position).get("Store_Address_Line1").toString();
            address_line2 = data.get(position).get("Store_City_state_zip").toString();

            store_add.setText(address_line1 + "\n" + address_line2);
            float textWidth = store_add.getPaint().measureText(address_line1);

            Display display = getWindowManager().getDefaultDisplay();
            @SuppressWarnings("deprecation")
            int width = display.getWidth();
            // Log.d(TAG, "textWidth-------------" + textWidth +
            // "width-----------" + width);

            if (width == 240 && textWidth >= 220) {

                store_add.setEllipsize(TruncateAt.END);
                arrowImageView.setVisibility(View.VISIBLE);
            } else if (width == 320 && textWidth >= 310) {

                store_add.setEllipsize(TruncateAt.END);
                arrowImageView.setVisibility(View.VISIBLE);
            } else if (width == 480 && textWidth >= 458) {

                store_add.setEllipsize(TruncateAt.END);
                arrowImageView.setVisibility(View.VISIBLE);
            } else if (width == 600 && textWidth >= 590) {

                store_add.setEllipsize(TruncateAt.END);
                arrowImageView.setVisibility(View.VISIBLE);
            } else if (width == 720 && textWidth >= 710) {

                store_add.setEllipsize(TruncateAt.END);
                arrowImageView.setVisibility(View.VISIBLE);
            }

            arrowImageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    arroewDlg(address_line1);
                }
            });

            store_phone.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Log.d("store_phone.getText().toString()--------",
                    // store_phone.getText().toString());
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + store_phone.getText().toString()));
                    startActivity(callIntent);

                }
            });
            getdirection.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    String store_lat = data.get(position).get("Store_Address_Latitude");
                    String store_lng = data.get(position).get("Store_Address_Longitude");
                    double dlattd = Double.parseDouble(store_lat);
                    double dlongtd = Double.parseDouble(store_lng);
                    String urlString = "http://maps.google.com/maps?f=d&hl=en&saddr=" + latitude + "," + longitude
                            + "&daddr=" + dlattd + "," + dlongtd;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    intent.setComponent(new ComponentName("com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity"));
                    startActivity(intent);
                }
            });
            return view;
        }

        protected void arroewDlg(String store_add) {

            try {
                final Dialog alertDialog = new Dialog(Findparticipatingbusiness.this);

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.setContentView(R.layout.arrowpopup);
                alertDialog.setCancelable(true);
                type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
                Button okmsg = (Button) alertDialog.findViewById(R.id.okmsg);
                TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
                aboutprogram.setText(store_add);
                aboutprogram.setTypeface(type);
                okmsg.setTypeface(type);
                okmsg.setOnClickListener(new Button.OnClickListener() {

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
    }

    @SuppressLint("InlinedApi")
    protected void initiatePopupWindow() {

        final Dialog alertDialog = new Dialog(Findparticipatingbusiness.this);

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setContentView(R.layout.singlegrade_popup);

        ListView singlegradelist = (ListView) alertDialog.findViewById(R.id.singlegradelist);

        alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        ArrayList<String> milesList = new ArrayList<String>();
        milesList.add("1");
        milesList.add("2");
        milesList.add("4");
        milesList.add("6");
        milesList.add("8");
        milesList.add("10");
        milesList.add("15");
        milesList.add("20");
        milesList.add("25");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Findparticipatingbusiness.this,
                android.R.layout.simple_list_item_1, milesList);
        singlegradelist.setAdapter(adapter);
        singlegradelist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                miles = arg0.getItemAtPosition(pos).toString();
                milespinner.setText(miles);
                Editor editor = pref.edit();
                editor.putString("mile", miles);
                editor.commit();
                new GetParticipatingBusinessInfo().execute();
                // gradelevelspinner.setText(value);
                alertDialog.cancel();

            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        alertDialog.show();

    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = (dist * 60 * 1.1515);

        // dist=dist*1.609344;// just for testing

        String dst = "" + dist;

        String dst1 = "0.0";
        if (dst.length() > 4) {
            dst1 = dst.substring(0, dst.indexOf(".") + 3);
            System.out.print("1st---->" + dst1);
        } else if (dst.length() == 3) {
            dst1 = dst.substring(0, dst.indexOf(".") + 2);
            System.out.print("2nd---->" + dst1);
        } else if (dst.length() == 4) {
            dst1 = dst.substring(0, dst.indexOf(".") + 3);
            System.out.print("3rd---->" + dst1);
        }

        // String dst1 = dst.substring(0, dst.indexOf(".") + 3);

        double distanceval = Double.parseDouble(dst1);
        // Log.d(TAG, "distanceval-------" + distanceval);
        return (distanceval);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
