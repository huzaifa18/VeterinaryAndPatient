package betaar.pk;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CaptureLocation extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    TextView tv_appeal_Blood_pickuplocation;
    RelativeLayout appeal_blood_rl_search_for_place_auto_complete;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int REQUEST_FINE_LOCATION = 11;
    private LatLng latlngPickup;

    private GoogleMap mMap;
    MapHelper mapHelper;
    private int timer = 3;
    Handler mHandler;
    JSONObject jsonObject;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    SupportMapFragment mapFragment;
    String mPickupAddress, mDropOffLocation;
    double latitude; // latitude
    double longitude; // longitude

    LatLng latLng;
    String mCity;
    String mArea = "not";
    LatLng mLatLng;
    String mAddress;

    Location myCurrentLocation;
    Location myStaticCurrentLocation;
    Marker mCurrLocationMarker;
    String cancel_req_tag = "MapActivity";
    Button bt_appeal_blood_submit;
    Dialog noInternetDialog;
    //MyReceiverForNetworkDialog myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        turnOnGPS();
        getPickupLocation();

        createNetErrorDialog();
        checkPermission();

        mapFragment.getMapAsync(this);

        mapHelper = new MapHelper();

        loadingHospitalHonServer();

    }


    private void init()
    {
        noInternetDialog = new Dialog(CaptureLocation.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.appealBloodMap);

        tv_appeal_Blood_pickuplocation = (TextView) findViewById(R.id.tv_appeal_Blood_pickuplocation);
        appeal_blood_rl_search_for_place_auto_complete = (RelativeLayout) findViewById(R.id.appeal_blood_rl_search_for_place_auto_complete);

        bt_appeal_blood_submit = (Button) findViewById(R.id.bt_appeal_blood_submit);
    }

    public void getPickupLocation()
    {

        appeal_blood_rl_search_for_place_auto_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callingLocationDialog();
            }
        });
    }//end of getting pickup location

    public void callingLocationDialog(){


        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setBoundsBias(new LatLngBounds(new LatLng(23.695,  68.149), new LatLng(35.88250, 76.51333)))//south and north latlong bourdy for pakistan
                            .build(CaptureLocation.this);

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("TAg", "the code is result: " + resultCode);
        Log.e("TAg", "the code is resquest: " + requestCode);
        Log.e("TAg", "the code is Intent: " + data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String plceName = place.getName().toString();
                String plceAddress = place.getAddress().toString();

                LatLng latlng = place.getLatLng();
                Log.i("TAG", "Place: 123" + place.getName());
                Log.i("TAG", "Place: " + place.getAddress());
                mAddress = place.getAddress().toString();

                Log.e("tag" , "address in onActivityResult from place : "+mAddress);

                //getAddressApi(latlng.latitude, latlng.longitude);
                getCompleteAddressString(latlng.latitude , latlng.longitude , "onActivityResult");

                Log.i("TAG", "Place Coordinates: " + place.getLatLng());


                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latlng);
                markerOptions.title(plceAddress);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_myplaces));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                mMap.addMarker(markerOptions).showInfoWindow();

                tv_appeal_Blood_pickuplocation.setText(plceAddress);
                tv_appeal_Blood_pickuplocation.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                latlngPickup = latlng;
                mLatLng = latlng;


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }//end of onActivity Result


    public void turnOnGPS()
    {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(CaptureLocation.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            //**************************
            builder.setAlwaysShow(true); //this is the key ingredient
            //**************************

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        CaptureLocation.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    protected void createNetErrorDialog() {

        if (isNetworkAvailable()==false){


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need a network connection to use this application. Please turn on mobile network or Wi-Fi in Settings.")
                    .setTitle("Unable to connect")
                    .setCancelable(false)
                    .setPositiveButton("Settings",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(i);
                                }
                            }
                    )
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    CaptureLocation.this.finish();
                                }
                            }
                    );
            AlertDialog alert = builder.create();
            alert.show();
        }else {
            //remainging
        }
    }




    //market for Pickup Location
    public void addingMarketForPickLocation(LatLng pickup, String pickupTitle){
        mMap.addMarker(new MarkerOptions()
                .position(pickup)
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup_icon))
                .title(pickupTitle));

    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d("MapActivity", "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }


    public void setMyLocationEnable(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }else {
            mMap.setMyLocationEnabled(true);


        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setMyLocationEnable();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        buildGoogleApiClient();
        mGoogleApiClient.connect();

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                double cLat = mMap.getCameraPosition().target.latitude;
                double cLng = mMap.getCameraPosition().target.longitude;
                Log.e("TAG", "the current postion on marker lat is: " + cLat);
                Log.e("TAG", "the current postion on marker lng is: " + cLng);
                //getAddressApi(cLat, cLng);
                getCompleteAddressString(cLat, cLng , "cameraIdle");
                mLatLng = new LatLng(cLat, cLng);


            }
        });

    }


    ///

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            myCurrentLocation = mLastLocation;
            myStaticCurrentLocation = mLastLocation;

            mapHelper.setLatitude(latitude);
            mapHelper.setLongitude(longitude);

            Log.e("latlang" , "latitudeCustomer "+latitude);
            Log.e("latlang" , "longitudeCustomer "+longitude);
            latLng = new LatLng(latitude , longitude);
            mLatLng = latLng;
   /*         MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Location");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_add));
            markerOptions.draggable(true);*/

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            //mMap.addMarker(markerOptions);


          /*  CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(mLatlngPickup).zoom(12.f).build();*/

     /*       mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));*/


        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //1 seconds
        mLocationRequest.setFastestInterval(1000); //1 seconds
        mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE , String type) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addressList != null) {
                Address returnedAddress = addressList.get(0);
                Log.e("tag", "My Current address returnedAddress : " + returnedAddress);

                mAddress = addressList.get(0).getAddressLine(0);
                mCity = addressList.get(0).getLocality();
                String state = addressList.get(0).getAdminArea();
                String country = addressList.get(0).getCountryName();
                String countryCode = addressList.get(0).getCountryCode();
                String knownName = addressList.get(0).getFeatureName();
                mArea = addressList.get(0).getSubLocality();

                tv_appeal_Blood_pickuplocation.setText(mAddress);





                Log.e("tag", "My Complete address is : " + mAddress);
                Log.e("tag", "My city address is : " + mCity);
                Log.e("tag", "My state address is : " + state);
                Log.e("tag", "My country address is : " + country);
                Log.e("tag", "My countryCode address is : " + countryCode);
                Log.e("tag", "My knownName address is : " + knownName);
                Log.e("tag", "My area address is : " + mArea);


            } else {
                Log.e("tag", "My Current address No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tag", "My Current address Cannnot get Address!");
        }
        return strAdd;
    }


    public void getAddressApi(double lat, double lng)
    {


        // Tag used to cancel the request
        //progressDialog.setMessage("Adding you ...");
        // showDialog();

        //String addressUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lang + "&key=" + getResources().getString(R.string.google_maps_key);

        StringRequest strReq = new StringRequest(Request.Method.GET, "http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=true", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "lat lang Response: " + response.toString());
                //hideDialog();
                String city = "";
                String area = "";
                try {

                    jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONArray("results").getJSONObject(0);
                    Log.e("TAG", "the result from address: " + data);
                    JSONArray cityName = data.getJSONArray("address_components");

                    String formatedAddress = data.getString("formatted_address");
                    Log.e("TAG", "address_components formated Address " + formatedAddress);
                    mAddress = formatedAddress;
                    for (int i = 0; i < cityName.length(); i++) {
                        JSONObject zero2 = cityName.getJSONObject(i);
                        String long_name = zero2.getString("long_name");
                        JSONArray mtypes = zero2.getJSONArray("types");


                        String Type = mtypes.getString(0);
                        if (Type.equalsIgnoreCase("locality") || Type.equalsIgnoreCase("administrative_area_level_2")  || Type.equalsIgnoreCase("administrative_area_level_3")) {
                            city = long_name;

                        }

                        if(mtypes.length() > 1)
                        {
                            String AreaType = mtypes.getString(1);
                            if(AreaType.equalsIgnoreCase("sublocality"))
                            {
                                area = long_name ;
                            }
                            Log.e("TAG", "the result from AREA: " + AreaType);
                        }



                    }


                    Log.e("TAG", "address_components area " + area);
                    Log.e("TAG", "address_components city " + city);
                    mCity = city;
                    mArea = area;




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), R.string.serviceError, Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);

                return params;
            }
        };
        // Adding request to request queue
        //AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }


    public void loadingHospitalHonServer()
    {
        bt_appeal_blood_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mLatLng!=null) {

                    double lat = mLatLng.latitude;
                    double lng = mLatLng.longitude;
                    Log.e("TAG", "Selected Hospital City is: " + mCity);
                    Log.e("TAG", "Selected Hospital area is: " + mArea);
                    Log.e("TAG", "Selected Hospital address is: " + mAddress);
                    Log.e("TAG", "Selected Hospital LatLng is: " + mLatLng);
                    Log.e("TAG", "Selected Hospital Lat is: " + lat);
                    Log.e("TAG", "Selected Hospital Lng is: " + lng);

                    Intent intent = new Intent();
                    intent.putExtra("City", mCity);
                    intent.putExtra("Area", mArea);
                    intent.putExtra("Address", mAddress );
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    setResult(RESULT_OK, intent);
                    finish();

                    if (mCity != null) {

                        Intent intent1 = new Intent();
                        intent1.putExtra("City", mCity);
                        intent1.putExtra("Area", mArea);
                        intent1.putExtra("Address", mAddress );
                        intent1.putExtra("lat", lat);
                        intent1.putExtra("lng", lng);
                        setResult(RESULT_OK, intent1);
                        finish();
                    } else {
                        Toast.makeText(CaptureLocation.this, "Error! Please Specify your location again", Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });
    }

    @Override
    protected void onStop() {
        //unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    protected void onStart() {
        //showHideNetworkErrorDialog();
        //networkChange();
        super.onStart();
    }

    @Override
    protected void onResume() {

        //showHideNetworkErrorDialog();
        //networkChange();
        super.onResume();
    }

    /*public void networkChange(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new CheckConnectivity(), intentFilter);
    }

    public void showHideNetworkErrorDialog()
    {
        myReceiver = new MyReceiverForNetworkDialog();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(API.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
    }*/


    /*public class MyReceiverForNetworkDialog extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            // TODO Auto-generated method stub

            int datapassed = intent.getIntExtra("DATAPASSED", 0);


            if (datapassed == 1234){

                noInternetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                noInternetDialog.setContentView(R.layout.internet_connection_dialog);
                noInternetDialog.setCancelable(false);
                Button enable = (Button) noInternetDialog.findViewById(R.id.enable);
                Button stayOffline = (Button) noInternetDialog.findViewById(R.id.stayOffline);
                noInternetDialog.show();

                enable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(i);
                    }
                });
                stayOffline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

            }

            if(datapassed == 12345)
            {
                Log.e("tag", "data passed form check connectivity : "+datapassed);
                noInternetDialog.dismiss();
            }

        }
    }*/



}