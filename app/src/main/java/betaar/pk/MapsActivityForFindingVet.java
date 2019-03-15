package betaar.pk;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import betaar.pk.Adapters.DocListingMapAdapter;
import betaar.pk.Config.API;
import betaar.pk.Helpers.MapHelper;
import betaar.pk.Models.MapGetterSetter;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class MapsActivityForFindingVet extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;

    private static final int REQUEST_FINE_LOCATION = 11;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;

    Location myCurrentLocation;
    Location myStaticCurrentLocation;
    Marker mCurrLocationMarker;
    Marker marker;

    private int timer = 3;
    Handler mHandler;

    double latitude; // latitude
    double longitude; // longitude

    MapHelper mapHelper;
    LatLng mLatlngPickup;
    String mPickupAddress, mDropOffLocation;

    PolylineOptions lineOptions;
    Polyline polyline = null;

    RecyclerView recyclerView;
    ArrayList<MapGetterSetter> docsList;
    DocListingMapAdapter adapter;
    LinearLayoutManager layoutManager;

    boolean c = true;
    int a = 0;

    //****************
    Double dhaLahoreLat,dhaLahoreLng;
    //***********

    String specialization_id,category_id,sub_category_id,animal_no,purpose;

    ImageView progress_logo;
    Animation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_for_call_vet);

        turnOnGPS();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        specialization_id = getIntent().getStringExtra("specialization_id");
        category_id = getIntent().getStringExtra("category_id");
        sub_category_id = getIntent().getStringExtra("sub_category_id");
        animal_no = getIntent().getStringExtra("animal_no");
        purpose = getIntent().getStringExtra("purpose_id");
        Log.e("TAG","Animals: " + animal_no);

        Log.e("TAG","Category ID: " + category_id);
        Log.e("TAG","sub_category_id: " + sub_category_id);
        Log.e("TAG","specialization_id: " + specialization_id);
        Log.e("TEG","purpose_id: " + purpose);

        createNetErrorDialog();
        checkPermission();

        mapFragment.getMapAsync(this);

        mapHelper = new MapHelper();

        initialization();

        getVets(category_id,sub_category_id,specialization_id);

    }

    //key ,  user_id , category_id , sub_category_id , speciality_id
    private void getVets(final String category_id, final String sub_category_id, final String speciality_id) {

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        StringRequest strReq = new StringRequest(Request.Method.POST, API.Get_vets, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Get Vets response Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String diploma_type;
                        String lat;
                        String lng;
                        String address;
                        String distance;
                        JSONObject userObj;
                        String user_id;
                        String name;

                        JSONArray vetsArray = jObj.getJSONArray("vets");

                        for (int i = 0 ; i < vetsArray.length() ; i++) {

                            JSONObject obj = vetsArray.getJSONObject(i);

                            diploma_type = obj.getString("diploma_type");
                            lat = obj.getString("lat");
                            lng = obj.getString("lng");
                            address = obj.getString("address");
                            distance = String.format("%.3f",Double.valueOf(obj.getString("distance"))) + " km";

                            userObj = obj.getJSONObject("user");

                            user_id = userObj.getString("id");
                            name = userObj.getString("name");

                            /*Log.e("TAG", "the message from server is diploma_type: " + diploma_type);
                            Log.e("TAG", "the message from server is lat: " + lat);
                            Log.e("TAG", "the message from server is lng: " + lng);
                            Log.e("TAG", "the message from server is address: " + address);
                            Log.e("TAG", "the message from server is distance: " + distance);
                            Log.e("TAG", "the message from server is user id: " + user_id);
                            Log.e("TAG", "the message from server is name: " + name);*/

                            LatLng latlng = new LatLng(Double.valueOf(lat), Double.valueOf(lng));

                            if (i == 0){
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                            }

                            addingMarkerForPickLocation(user_id,name,diploma_type,distance,latlng);

                            //docsList.add(new MapGetterSetter(name,diploma_type,distance,latlng));

                        }

                        Log.e("TEG","Purpose: "+purpose);
                        adapter = new DocListingMapAdapter(MapsActivityForFindingVet.this,docsList,specialization_id,animal_no,purpose,category_id,sub_category_id);
                        recyclerView.setAdapter(adapter);

                        //adapter = new DocListingMapAdapter(MapsActivityForFindingVet.this,docsList);
                        //recyclerView.setAdapter(adapter);
                        //adapter.notifyDataSetChanged();

                        String message = jObj.getString("msg");
                        //Toast.makeText(MapsActivityForFindingVet.this, message, Toast.LENGTH_SHORT).show();

                    } else {
                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(MapsActivityForFindingVet.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(MapsActivityForFindingVet.this);

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
                params.put("user_id", Prefs.getUserIDFromPref(MapsActivityForFindingVet.this));
                params.put("category_id", category_id);
                //params.put("category_id", "2");
                params.put("sub_category_id", sub_category_id);
                params.put("speciality_id", speciality_id);

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

    private void initialization() {

        recyclerView = (RecyclerView) findViewById(R.id.rv_vets_on_map);
        docsList = new ArrayList<MapGetterSetter>();
        Log.e("TEG","Purpose: " + purpose);
        adapter = new DocListingMapAdapter(MapsActivityForFindingVet.this,docsList,specialization_id,animal_no,purpose,category_id,sub_category_id);
        layoutManager = new LinearLayoutManager(MapsActivityForFindingVet.this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);
        
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int review_position = layoutManager.findFirstVisibleItemPosition();

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (c) {

                    c = false;

                } else {

                    Log.e("TAG", "newState: " + layoutManager.findFirstVisibleItemPosition());
                    Log.e("TAG", "LatLong: " + docsList.get(layoutManager.findFirstVisibleItemPosition()).getLatLong());

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(docsList.get(layoutManager.findFirstVisibleItemPosition()).getLatLong()).zoom(12.f).build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }

                /*if (prevPos > layoutManager.findFirstVisibleItemPosition()) {

                    //use to focus the item with index
                    recyclerView.scrollToPosition(layoutManager.findFirstVisibleItemPosition() - 1);
                    adapter.notifyDataSetChanged();

                } else if (prevPos < layoutManager.findFirstVisibleItemPosition()){

                    recyclerView.scrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
                    adapter.notifyDataSetChanged();

                }

                prevPos = layoutManager.findFirstVisibleItemPosition();*/

            }

        });

    }

    public void inisialization(LatLng current){

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMyLocationEnable();
        // mMap.setMyLocationEnabled(true);

        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);

    }

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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

          /*  MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Location");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_des));

            mMap.addMarker(markerOptions);*/

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(12.f).build();

            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //1 seconds
        mLocationRequest.setFastestInterval(1000); //1 seconds
        mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(mGoogleApiClient.isConnected()){

            //inisialization(latLng);

//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        /*LatLng latLng1 = new LatLng(31.5204432, 74.3587323);
        LatLng latLng2 = new LatLng(31.439751, 74.200632);
        LatLng latLng3 = new LatLng(31.393752, 74.184496);

        LatLng latLng4 = new LatLng(31.391407, 74.203035);
        LatLng latLng5= new LatLng(31.380856, 74.194109);
        LatLng latLng6 = new LatLng(31.359750, 74.189989);
        LatLng latLng7 = new LatLng(31.374701, 74.225008);
        LatLng latLng8 = new LatLng(31.387011, 74.235651);
        addingMarkerForPickLocation(latLng1, "Doc 1");
        addingMarkerForPickLocation(latLng2, "Doc 2");
        addingMarkerForPickLocation(latLng3, "Doc 3");
        addingMarkerForPickLocation(latLng4, "Doc 4");
        addingMarkerForPickLocation(latLng5, "Doc 5");
        addingMarkerForPickLocation(latLng6, "Doc 6");
        addingMarkerForPickLocation(latLng7, "Doc 7");
        addingMarkerForPickLocation(latLng8, "Doc 8");*/

        /*adapter = new DocListingMapAdapter(MapsActivityForFindingVet.this,docsList);
        recyclerView.setAdapter(adapter);*/

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d("MapActivity", "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
    // Asks for permission
    private void askPermission() {
        Log.d("MapActivity", "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION
        );
    }

    public void setMyLocationEnable(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }else {
            mMap.setMyLocationEnabled(true);
        }
    }

    //rouding double
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    //market for Pickup Location
    public void addingMarkerForPickLocation(String user_id, String name, String diploma_type, String distance, LatLng latlng){
        mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                .title(name));

        //Log.e("TAG","Title: " + pickupTitle);
        docsList.add(new MapGetterSetter(user_id,name,diploma_type,distance,latlng));

    }

    //market for DropOff Location
    public void addingMarketForDropOffLocation(LatLng drobOff, String dropOffTitle){
        mMap.addMarker(new MarkerOptions()
                .position(drobOff)
                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation))
                .title(dropOffTitle));

    }

    public double shortDistance(double fromLong, double fromLat, double toLong, double toLat){
        double d2r = Math.PI / 180;
        double dLong = (toLong - fromLong) * d2r;
        double dLat = (toLat - fromLat) * d2r;
        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
                * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367000 * c;
        return Math.round(d);
    }

    //distance between two points
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        Log.e("TAG", "Permission Granted");

                        onMapReady(mMap);

                       /* if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }*/

                        // setMyLocationEnable();
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    //converting time into hrs and day
    public String timeConvert(int time) {
        return time/24/60 + ":" + time/60%24 + ':' + time%60;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

       /*Toast toast =  Toast.makeText(this, "Location Changed " + location.getLatitude()
                + location.getLongitude(), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/

        myCurrentLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        double lat =  location.getLatitude();
        double lng = location.getLongitude();
        Log.e("TAg", "  the change location is: " + lat);
        Log.e("TAg", "  the change location is: " + lng);
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon((BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation)));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        LatLng currentLatLng = new LatLng(latitude, longitude);
        Log.e("TAG", "abc test CurrentLATLNG: " + latLng);
        Log.e("TAG", " abc test Static LatLng: " + currentLatLng);
        latitude = lat;
        longitude = lng;

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
                                    MapsActivityForFindingVet.this.finish();
                                }
                            }
                    );
            AlertDialog alert = builder.create();
            alert.show();
        }else {
            //remainging
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void turnOnGPS(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(MapsActivityForFindingVet.this)
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
                                        MapsActivityForFindingVet.this, 1000);
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

    @Override
    protected void onResume() {
        super.onResume();


    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        if ( a == 0) {

            recyclerView.setVisibility(View.VISIBLE);

            Animation animation = AnimationUtils.loadAnimation(MapsActivityForFindingVet.this, R.anim.slide_up);
            recyclerView.startAnimation(animation);
            a++;

        }

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        int pos = -1;

            for (int i = 0; i < docsList.size(); i++)
            {
                if (marker.getTitle().equals(docsList.get(i).getTitle()))
                {
                    pos = i;
                    break;
                }
            }

        recyclerView.scrollToPosition(pos); //use to focus the item with index
        adapter.notifyDataSetChanged();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(marker.getPosition()).zoom(12.f).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Animation animation = AnimationUtils.loadAnimation(MapsActivityForFindingVet.this, R.anim.anim_out);
        recyclerView.startAnimation(animation);
        recyclerView.setVisibility(View.GONE);
        a = 0;
    }

}//end of class