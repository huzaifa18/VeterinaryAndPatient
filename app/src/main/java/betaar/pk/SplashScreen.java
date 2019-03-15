package betaar.pk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import betaar.pk.Adapters.ListingAdapterCheckupRequest;
import betaar.pk.Config.API;
import betaar.pk.Models.MedData;
import betaar.pk.Models.MedGroups;
import betaar.pk.Models.RequestModelVet;
import betaar.pk.Preferences.Prefs;
import betaar.pk.Services.UpdateLatLong;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class SplashScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static int SplashScreenTimeOut = 3000;//3 seconds8
    private int timer = 3;
    Handler mHandler;
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
    LatLng mLatLng = null;
    Boolean bool = false;
    //TextView tv_please_wait;
    ImageView progress_logo;
    Animation rotate;

    ArrayList<MedData> meds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        meds = new ArrayList<>();

        Log.e("TAG","On Create");
        Log.e("TAG","Role: " + Prefs.getUserRoleFromPref(SplashScreen.this));

        mHandler = new Handler();
        setUpGClient();
        if (isLocationEnabled(SplashScreen.this)){
            if (Prefs.getUserRoleFromPref(SplashScreen.this).equals("veterinarian")) {

                checkVisitVetService();

            } else if (Prefs.getUserRoleFromPref(SplashScreen.this).equals("client")) {

                checkVisitClientService();

            } else {

                getMyLocation();
                useHandler();

            }
        } else {

            Log.e("TAG","Location Disabled");
            if (Prefs.getUserRoleFromPref(SplashScreen.this).equals("veterinarian")) {

                checkVisitVetService();

            } else if (Prefs.getUserRoleFromPref(SplashScreen.this).equals("client")) {

                checkVisitClientService();

            } else {

                getMyLocation();
                useHandler();

            }

        }

        // tv_please_wait = (TextView) findViewById(R.id.tv_please_wait);

    }

    @Override
    protected void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(SplashScreen.this);
        googleApiClient.disconnect();
    }

    private void autoLogin(){

        Log.e("TAG","AutoLogin");

        //Prefs.clearPrefData(SplashScreen.this);

        String UDID = Prefs.gettUserUDID(SplashScreen.this);
        String userName = Prefs.getUserNameFromPref(SplashScreen.this);
        String password = Prefs.getPasswordFromPref(SplashScreen.this);

        if (userName != null && password != null && UDID != null)
        {
            //username and password are present, do your stuff
            String role = Prefs.getUserRoleFromPref(SplashScreen.this);

            if (role != null){

                if (role.equals("veterinarian")){
                    startActivity(new Intent(SplashScreen.this, DashboardVeterinarian.class));
                    finish();
                    startService(new Intent(SplashScreen.this, UpdateLatLong.class));
                } else if (role.equals("client")){
                    startActivity(new Intent(SplashScreen.this, DashboardClient.class));
                    finish();
                    startService(new Intent(SplashScreen.this, UpdateLatLong.class));
                } else if (role.equals("organization")){
                    startActivity(new Intent(SplashScreen.this, DashBoardOrganization.class));
                    finish();
                    startService(new Intent(SplashScreen.this, UpdateLatLong.class));
                } else {
                    Intent i = new Intent(SplashScreen.this, UserLoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }

        } else {

        }

    }

    //Thread for starting mainActivity
    private Runnable mRunnableStartMainActivity = new Runnable() {
        @Override
        public void run() {
            Log.e("Handler", " Calls");
            timer--;
            mHandler = new Handler();
            mHandler.postDelayed(this, 1000);

            if (timer == 2) {
                //tv_please_wait.setText("Please Wait...");
            }
            if (timer == 1) {
                //tv_please_wait.setText("Please Wait.");
            }
            if (timer == 0) {
                autoLogin();
            }
        }
    };

    //handler for the starign activity
    Handler newHandler;
    public void useHandler(){

        newHandler = new Handler();
        newHandler.postDelayed(mRunnableStartMainActivity, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnableStartMainActivity);
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        googleApiClient.connect();
                    }
                })
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            Double latitude = mylocation.getLatitude();
            Double longitude = mylocation.getLongitude();
            Log.e("TAg", "the latitue from location chnage are: " + latitude);
            Log.e("TAg", "the longitude from location chnage are: " + longitude);

            mLatLng = new LatLng(latitude, longitude);

            //Or Do whatever you want with your location
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here
    }

    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(SplashScreen.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    bool = true;
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(SplashScreen.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(SplashScreen.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        if (bool){
                            useHandler();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    private void checkVisitVetService(){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.CheckVisit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Check Visit response Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    Log.e("tag","test3");

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("msg");
                        Toast.makeText(SplashScreen.this, message, Toast.LENGTH_SHORT).show();

                        Log.e("tag","test4");
                        JSONObject visits = jObj.getJSONObject("visit");

                            String visit_id = visits.getString("visit_id");
                            String user_id_sender = visits.getString("user_id_sender");
                            String user_id_receiver = visits.getString("user_id_receiver");
                            String immediate_visit = visits.getString("immediate_visit");
                            String date_of_visit = visits.getString("date_of_visit");
                            String time_of_visit = visits.getString("time_of_visit");
                            String user_address = visits.getString("user_address");
                            String user_lat = visits.getString("user_lat");
                            String user_lng = visits.getString("user_lng");
                            String reason_of_visit = visits.getString("reason_of_visit");
                            String no_of_animals = visits.getString("no_of_animals");
                            String speciality_id = visits.getString("speciality_id");
                            String protocol_id = visits.getString("protocol_id");
                            //String protocol = visits.getString("protocol");
                            String visit_started_at = visits.getString("visit_started_at");
                            String visit_ended_at = visits.getString("visit_ended_at");
                            String created_at = visits.getString("created_at");

                            JSONObject obj1 = visits.getJSONObject("sender");

                            String name = obj1.getString("name");
                            String phone = obj1.getString("phone");

                            JSONObject obj2 = visits.getJSONObject("category");
                            String category_name = obj2.getString("name");

                            JSONObject obj3 = visits.getJSONObject("sub_category");
                            String sub_category_name = obj3.getString("name");

                        Intent i = new Intent(SplashScreen.this, PathDrawActivity.class);
                        i.putExtra("lat", user_lat);
                        i.putExtra("lng", user_lng);
                        i.putExtra("name", name);
                        i.putExtra("address", user_address);
                        i.putExtra("phone", phone);
                        i.putExtra("visit_id", visit_id);
                        i.putExtra("status", "accepted");
                        i.putExtra("type", "sender");
                        i.putExtra("visitStarted", true);
                        startActivity(i);

                    } else {
                        String message = jObj.getString("msg");
                        Toast.makeText(SplashScreen.this, message, Toast.LENGTH_SHORT).show();
                        autoLogin();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Server Connection Fail", Toast.LENGTH_LONG).show();
                mHandler = new Handler();
                setUpGClient();
                if (isLocationEnabled(SplashScreen.this)){
                    getMyLocation();
                    useHandler();
                }
                //hid pregress here
                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


               /* SharedPreferences sharedPreferences  = getSharedPreferences("udid", 0);
                String userUdid = sharedPreferences.getString("udid", "null");*/



                // String mPhone = phone.substring(1);

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", Prefs.getUserIDFromPref(SplashScreen.this));

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void checkVisitClientService(){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.CheckVisit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Check Visit response Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("msg");
                        Toast.makeText(SplashScreen.this, message, Toast.LENGTH_SHORT).show();

                        JSONObject visits = jObj.getJSONObject("visit");

                        String visit_id = visits.getString("visit_id");
                        String user_id_sender = visits.getString("user_id_sender");
                        String user_id_receiver = visits.getString("user_id_receiver");
                        String immediate_visit = visits.getString("immediate_visit");
                        String date_of_visit = visits.getString("date_of_visit");
                        String time_of_visit = visits.getString("time_of_visit");
                        String user_address = visits.getString("user_address");
                        String user_lat = visits.getString("user_lat");
                        String user_lng = visits.getString("user_lng");
                        String reason_of_visit = visits.getString("reason_of_visit");
                        String no_of_animals = visits.getString("no_of_animals");
                        String speciality_id = visits.getString("speciality_id");
                        String protocol_id = visits.getString("protocol_id");
                        String protocol = "No Protocol";
                        String visit_started_at = visits.getString("visit_started_at");
                        String visit_ended_at = visits.getString("visit_ended_at");
                        String created_at = visits.getString("created_at");

                        JSONObject obj1 = visits.getJSONObject("receiver");

                        String name = obj1.getString("name");
                        String phone = obj1.getString("phone");

                        JSONObject obj2 = visits.getJSONObject("category");
                        String category_name = obj2.getString("name");

                        JSONObject obj3 = visits.getJSONObject("sub_category");
                        String sub_category_name = obj3.getString("name");

                        if (!protocol_id.equals("0")) {

                            JSONObject obj4 = visits.getJSONObject("sub_category");
                            protocol = obj4.getString("name");

                        }

                        String status = visits.getString("status");

                        /*Intent i = new Intent(SplashScreen.this, PathDrawActivity.class);
                        i.putExtra("lat", user_lat);
                        i.putExtra("lng", user_lng);
                        i.putExtra("name", name);
                        i.putExtra("address", user_address);
                        i.putExtra("phone", phone);
                        i.putExtra("visit_id", visit_id);
                        i.putExtra("status", "accepted");
                        i.putExtra("type", "receiver");
                        i.putExtra("visitStarted", true);
                        startActivity(i);*/

                        if (status.equals("awaiting_for_client_bill_confirmation")){

                            ArrayList<MedData> meds = new ArrayList<>();

                            ArrayList<MedGroups> group = new ArrayList<>();

                            String bill = visits.getString("bill");

                            JSONArray medArr = visits.getJSONArray("medicines");

                            String groupId = null;
                            String groupName = null;

                            for (int i = 0; i < medArr.length(); i++){

                                JSONObject jObjmed = medArr.getJSONObject(i);

                                String med_id = jObjmed.getString("medicine_id");
                                String quantity = jObjmed.getString("quantity");
                                String unit_price = jObjmed.getString("unit_price");
                                String unit = jObjmed.getString("unit");
                                JSONObject medobj = jObjmed.getJSONObject("medicine");
                                String med_name = medobj.getString("name");

                                JSONObject medGroupObj = medobj.getJSONObject("group");
                                groupId = medGroupObj.getString("id");
                                groupName = medGroupObj.getString("name");

                                quantity = quantity + " " + unit;

                                meds.add(new MedData(med_id,med_name,groupId,unit_price,quantity));

                                group.add(new MedGroups(groupId,groupName,meds));

                            }

                            Intent i = new Intent(SplashScreen.this,BillConfirmation.class);
                            i.putExtra("bill",bill);
                            i.putExtra("array",group);
                            i.putExtra("visit_id",visit_id);
                            startActivity(i);


                        } else {

                            Intent i = new Intent(SplashScreen.this, ClientHistory.class);
                            i.putExtra("Type", "Current");
                            startActivity(i);

                        }

                    } else {
                        String status = jObj.getString("status");

                        Toast.makeText(SplashScreen.this, status, Toast.LENGTH_LONG).show();
                        Log.e("TAG","Status: " + status);

                        String message = jObj.getString("msg");
                        Toast.makeText(SplashScreen.this, message, Toast.LENGTH_SHORT).show();
                        Log.e("tag","test");
                        autoLogin();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Server Connection Fail", Toast.LENGTH_LONG).show();
                //hid pregress here
                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


               /* SharedPreferences sharedPreferences  = getSharedPreferences("udid", 0);
                String userUdid = sharedPreferences.getString("udid", "null");*/



                // String mPhone = phone.substring(1);

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", Prefs.getUserIDFromPref(SplashScreen.this));

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

}//***************** Huzaifa Asif ********************