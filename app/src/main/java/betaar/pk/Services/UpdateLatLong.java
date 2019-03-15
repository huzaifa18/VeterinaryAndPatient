package betaar.pk.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager; 
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
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

import betaar.pk.CheckupRequestsListing;
import betaar.pk.Config.API;
import betaar.pk.Preferences.Prefs;
import betaar.pk.SignUpAsClient;
import betaar.pk.UserLoginActivity;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class UpdateLatLong extends Service {
    
    private static final String TAG = "CurrentLocation";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 20000;
    private static final float LOCATION_DISTANCE = 0;
    String user_id;

    public UpdateLatLong() {

    }
    
    private class LocationListener implements android.location.LocationListener {
        
        Location mLastLocation;

        public LocationListener(String provider)
        {
            //Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            //Log.e(TAG, "onLocationChanged: " + location);
            //Log.e("tag", "onLocationChanged in service lat : " + location.getLatitude());
            //Log.e("tag", "onLocationChanged in service lng : " + location.getLongitude());

            if(isInternetOn())
            {
                updateLatLngToServerRequest(location.getLatitude() , location.getLongitude());
            }
            else
            {
                Log.e("tag" , "Internet is not on in getting current lat lng service: ");
            }

            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        SharedPreferences sharedPreferencesStarted = getSharedPreferences("betaar_user" , MODE_PRIVATE);

        if (sharedPreferencesStarted!=null){

            user_id = sharedPreferencesStarted.getString("user_id" ,  null);
            //Log.e("tag", "The id is in update lat lng service: " + user_id);

        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(UpdateLatLong.this.LOCATION_SERVICE);
        }
    }

    public void updateLatLngToServerRequest(final double lat, final double lng)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "update latlng";

        StringRequest strReq = new StringRequest(Request.Method.POST, API.UPDATE_LAT_LNG, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "update latlng response: " + response.toString());
                //hideDialog();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String msg = jObj.getString("msg");
                        //Toast.makeText(PhysiciansMap.this, msg, Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "Update latlng on server when message false : "+msg + response.toString());

                    } else {

                        String errorMsg = jObj.getString("msg");

                        boolean exist = jObj.getBoolean("exist");

                        Log.e("LATLNG", "Exist: " + exist);

                        if (exist){

                        } else {

                            //API.logoutService(UpdateLatLong.this);
                            Prefs.clearPrefData(UpdateLatLong.this);
                            Intent i = new Intent(UpdateLatLong.this, UserLoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            UpdateLatLong.this.startActivity(i);
                            stopService(new Intent(UpdateLatLong.this, UpdateLatLong.class));

                        }

                        boolean request_error = jObj.getBoolean("request_cancel");
                        Log.e("TAG", "request_cancel is : "+request_error);

                        if (!request_error) {

                            String msg = jObj.getString("msg");

                        } else {

                            String msg = jObj.getString("msg");
                            Log.e("tag", "request_cancel is : "+msg);

                            Intent intent = new Intent();
                            intent.setAction("adminCancelRequest");
                            intent.putExtra("msg" , msg);
                            LocalBroadcastManager.getInstance(UpdateLatLong.this).sendBroadcast(intent);
                        }

                        Log.e("TAG", "Error while updating latlng on server when message true: "+errorMsg + response.toString());

                        //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Update latlng Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", user_id);
                params.put("udid", Prefs.gettUserUDID(UpdateLatLong.this));
                Log.e("TAG", "udid: " + Prefs.gettUserUDID(UpdateLatLong.this));
                params.put("lat", String.valueOf(lat));
                params.put("lng", String.valueOf(lng));

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

    // update lat lng after starting webservice
    public boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }
        return false;
    }

    /*public static boolean canGetLocation() {
        return isLocationEnabled(App.appInstance); // application context
    }*/

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

}