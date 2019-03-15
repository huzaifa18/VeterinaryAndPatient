package betaar.pk;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import betaar.pk.Config.API;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class SignUpAsVeterinarian extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    EditText et_username, et_name, et_eamil, et_mobile, et_cnic, et_address, et_password, et_confirm_password;
    CheckBox terms_and_conditions;
    Button bt_register;
    TextView tv_terms_and_conditions;
    //CustomProgressDialog customProgressDialog;

    ImageView progress_logo;
    Animation rotate;
    LatLng mLatLng = null;

    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    private static final int REQUEST_FINE_LOCATION = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sign_up_as_veterinarian);
        setContentView(R.layout.custome_registration_veterinarian_screen);

        init();
        btRegisterButtonClickHandler();

        Intent i = getIntent();
        double lat = i.getExtras().getDouble("LAT");
        double lng = i.getExtras().getDouble("LNG");
        Log.e("TAg", "the latitude from previouce activity is: " + lat);
        Log.e("TAg", "the longitude from previouce activity is: " + lng);

        mLatLng = new LatLng(lat, lng);

        setUpGClient();

    }

    private void init() {

        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        et_username = (EditText) findViewById(R.id.et_username);
        et_name = (EditText) findViewById(R.id.et_name);
        et_eamil = (EditText) findViewById(R.id.et_eamil);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_cnic = (EditText) findViewById(R.id.et_cnic);
        et_address = (EditText) findViewById(R.id.et_address);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);


        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        //customProgressDialog=new CustomProgressDialog(SignUpAsVeterinarian.this, 1);
        //customProgressDialog.show();

        terms_and_conditions = (CheckBox) findViewById(R.id.terms_and_conditions);
        tv_terms_and_conditions = (TextView) findViewById(R.id.tv_terms_and_conditions);
        SpannableString content = new SpannableString("Terms And Conditions");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tv_terms_and_conditions.setText(content);
        bt_register = (Button) findViewById(R.id.bt_register);

    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    private void btRegisterButtonClickHandler() {

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = et_username.getText().toString();
                String name = et_name.getText().toString();
                String email = et_eamil.getText().toString();
                String mobile = et_mobile.getText().toString();
                String cnic = et_cnic.getText().toString();
                String address = et_address.getText().toString();
                String password = et_password.getText().toString();
                String confirmPassword = et_confirm_password.getText().toString();
                String key = API.KEY;


               /* Intent verficationActivity = new Intent(SignUpAsVeterinarian.this, VerificationActivity.class);
                verficationActivity.putExtra("v_code", "1234");
                verficationActivity.putExtra("user_id", "7");
                startActivity(verficationActivity);
                finish();*/


                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                if (username.length() == 0) {

                    et_username.setError("Username is required");
                    et_username.setAnimation(animShake);

                } else if (name.length() == 0) {
                    et_name.setError("Name is required");
                    et_name.setAnimation(animShake);

                } else if (email.length() == 0) {
                    et_eamil.setError("Email is required");
                    et_eamil.setAnimation(animShake);

                } else if (!emailValidator(email)) {

                    et_eamil.setError("Invalid Email");
                    et_eamil.setAnimation(animShake);

                } else if (mobile.length() == 0) {
                    et_mobile.setError("Mobile No. is required");
                    et_mobile.setAnimation(animShake);

                } else if (mobile.length() < 11) {
                    et_mobile.setError("Invalid Mobile Number");
                    et_mobile.setAnimation(animShake);

                } else if (!mobile.substring(0, 2).equals("03")) {

                    et_mobile.setError("Invalid Mobile Number");
                    et_mobile.setAnimation(animShake);

                } else if (cnic.length() == 0) {

                    et_cnic.setError("CNIC is required");
                    et_cnic.setAnimation(animShake);

                } else if (cnic.length() > 0 && cnic.length() < 13) {

                    et_cnic.setError("Invalid CNIC");
                    et_cnic.setAnimation(animShake);

                } else if (address.length() == 0) {
                    et_address.setError("Please Provide Address");
                    et_address.setAnimation(animShake);
                } else if (password.length() == 0) {
                    et_password.setError("Password is required");
                    et_password.setAnimation(animShake);
                } else if (password.length() < 4) {
                    et_password.setError("Password must be greater than 3 characters");
                    et_password.setAnimation(animShake);
                } else if (confirmPassword.length() == 0) {
                    et_confirm_password.setError("Confirm Password is required");
                    et_confirm_password.setAnimation(animShake);
                } else if (!password.equals(confirmPassword)) {
                    et_password.setError("Passwords don't match");
                    Toast.makeText(SignUpAsVeterinarian.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                } else if (!terms_and_conditions.isChecked()) {
                    terms_and_conditions.setError("Required.");
                    terms_and_conditions.setAnimation(animShake);
                } else {

                    if (cnic.length() == 0) {
                        cnic = "";
                    }
                    Log.e("TAG", "the cnic is: " + cnic);
                    String mMobile = mobile.substring(1);
                    Log.e("TAG", "mobile number is: " + mMobile);

                    String UDID = Prefs.gettUserUDID(SignUpAsVeterinarian.this);
                    String lat = "32.142542";
                    String lng = "74.325455";

                    if (mLatLng!=null){
                        double Lat = mLatLng.latitude;
                        double Lng = mLatLng.longitude;
                        lat = String.valueOf(Lat);
                        lng = String.valueOf(Lng);
                    }

                    Log.e("TAg", "here is udid of user : " + UDID);
                    Log.e("TAg", "here is lat of user : " + lat);
                    Log.e("TAg", "here is lng of user : " + lng);

                    registringUser(username, name, email, mobile, cnic, address, password, lat, lng, UDID, key);
                }
            }
        });

        tv_terms_and_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.betaar.pk/terms-and-conditions"));
                startActivity(viewIntent);
            }
        });

    }


    public void registringUser(final String username, final String fullname, final String email, final String phone, final String cnic, final String address, final String password, final String lat, final String lng, final String UDID_TOKEN, final String key) {


        // Tag used to cancel the request
        String cancel_req_tag = "register";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.Register_veterinarian, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Register Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAg", "the Delete: " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String user_id = jObj.getString("user_id");
                        String V_CODE = jObj.getString("code");
                        String message = jObj.getString("msg");
                        Log.e("TAG", "the message from server user id: " + user_id);
                        Log.e("TAG", "the message from server V_CODE: " + V_CODE);
                        Log.e("TAG", "the message from server is message: " + message);


                        Intent verficationActivity = new Intent(SignUpAsVeterinarian.this, VerificationActivity.class);
                        verficationActivity.putExtra("v_code", V_CODE);
                        verficationActivity.putExtra("user_id", user_id);
                        verficationActivity.putExtra("user_name", username);
                        verficationActivity.putExtra("udid", UDID_TOKEN);
                        verficationActivity.putExtra("activity","signUp");
                        startActivity(verficationActivity);
                        finish();

                    } else {
                        String message = jObj.getString("msg");
                        Toast.makeText(SignUpAsVeterinarian.this, message, Toast.LENGTH_SHORT).show();

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){

                        } else {

                            API.logoutService(SignUpAsVeterinarian.this);

                        }

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
                        error.getMessage(), Toast.LENGTH_LONG).show();
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


                String mPhone = phone.substring(1);

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", key);
                params.put("name", fullname);
                params.put("username", username);
                params.put("email", email);
                params.put("phone", "92" + mPhone);
                params.put("cnic", cnic);
                params.put("address", address);
                params.put("password", password);
                params.put("lat", lat);
                params.put("lng", lng);
                params.put("udid", UDID_TOKEN);


                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    public static boolean emailValidator(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
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
                int permissionLocation = ContextCompat.checkSelfPermission(SignUpAsVeterinarian.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
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
                                            .checkSelfPermission(SignUpAsVeterinarian.this,
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
                                        status.startResolutionForResult(SignUpAsVeterinarian.this,
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
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(SignUpAsVeterinarian.this,
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
        int permissionLocation = ContextCompat.checkSelfPermission(SignUpAsVeterinarian.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }
}
