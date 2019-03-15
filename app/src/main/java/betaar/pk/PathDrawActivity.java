package betaar.pk;
    
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import betaar.pk.Adapters.SpinnerListingAdapter;
import betaar.pk.Config.API;
import betaar.pk.Helpers.CustomProgressDialog;
import betaar.pk.Models.MedData;
import betaar.pk.Models.MedGroups;
import betaar.pk.Preferences.Prefs;
import betaar.pk.Services.UpdateLatLong;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

import static android.Manifest.permission.CALL_PHONE;

public class PathDrawActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener  {

    String lat;
    String lng;
    String visit_id;
    String status;
    String name;
    String address;
    String phone;
    String type;

    Button startVisit;
    SupportMapFragment mapFragment;

    private GoogleMap mMap;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    Location myCurrentLocation;
    Marker mCurrLocationMarker;
    double latitude;
    double longitude;
    private int timer = 9;
    Handler mHandler;
    MapHelper mapHelper;
    LatLng destination;
    String mDropOffLocation;

    PolylineOptions lineOptions;
    Polyline polyline = null;

    CustomProgressDialog progressDialog;
    Boolean visitStarted = false;
    private static int LOCATION_TIME_OUT = 30000;
    SharedPreferences sharedPreferencesVisitStarted;
    Dialog noInternetDialog;
    //MyReceiverForNetworkDialog myReceiver;
    TextView nameInMap , addressInMap;
    ImageView callIconInMap;
    public static final int REQUEST_PERMISSION_CODE = 300;
    Intent service;

    Spinner sp_select_med_group,sp_select_med_name;
    ArrayList<MedGroups> medGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_draw);

        /*sharedPreferencesVisitStarted = getSharedPreferences("VisitStarted" , MODE_PRIVATE);
        visit_id = sharedPreferencesVisitStarted.getString("visit_id" ,  null);

        lat = sharedPreferencesVisitStarted.getString("patient_lat",null);
        lng = sharedPreferencesVisitStarted.getString("patient_lng",null);
        name = sharedPreferencesVisitStarted.getString("patient_name",null);
        address = sharedPreferencesVisitStarted.getString("patient_address",null);
        phone = sharedPreferencesVisitStarted.getString("patient_phone",null);*/

        /*if(visit_id != null) {
            status = "accepted";
            service = new Intent(PathDrawActivity.this, UpdateLatLong.class);
            startService(service);

            startVisit = (Button) findViewById(R.id.startVisit);
            startVisit.setText("Finish Visit");

            visitStarted = true;
            double dLat = Double.parseDouble(lat);
            double dLng = Double.parseDouble(lng);

            destination = new LatLng(dLat, dLng);

            LocalBroadcastManager.getInstance(this).registerReceiver(new cancelRequestReceiver(), new IntentFilter("adminCancelRequest"));*/

        //} else {

            getLatLngFromPreviousActivity();

            double dLat = Double.parseDouble(lat);
            double dLng = Double.parseDouble(lng);

            destination = new LatLng(dLat, dLng);
            Log.e("AddMarker" , "Destination: " + destination);

        //}

        LocalBroadcastManager.getInstance(this).registerReceiver(new cancelRequestReceiver(), new IntentFilter("adminCancelRequest"));

//
//        getLatLngFromPreviousActivity();
//
//        double dLat = Double.parseDouble(patLat);
//        double dLng = Double.parseDouble(patLng);
//
////        double dLat = 31.3232323;
////        double dLng = 74.2323443;
////
//        destination = new LatLng(dLat, dLng);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }

            medGroup = new ArrayList<>();

            turnOnGPS();
            initiate();
            setUserInfo();
            clickListener();

        }

        public void getLatLngFromPreviousActivity(){

            lat = getIntent().getStringExtra("lat");
            lng = getIntent().getStringExtra("lng");
            visit_id = getIntent().getStringExtra("visit_id");
            status = getIntent().getStringExtra("status");
            name = getIntent().getStringExtra("name");
            address = getIntent().getStringExtra("address");
            phone = getIntent().getStringExtra("phone");
            type = getIntent().getStringExtra("type");
            visitStarted = getIntent().getBooleanExtra("visitStarted",false);

            Log.e("PreviousActivityData","Lat: " + lat+
                    "\nLng: "+lng +
                    "\nVisit Id: " + visit_id +
                    "\nStatus: " + status +
                    "\nname: " + name +
                    "\naddress: " + address +
                    "\ntype: " + type +
                    "\nphone: " + phone +
                    "\nvisitStarted: " + visitStarted);

        }

        public void initiate() {

            noInternetDialog = new Dialog(PathDrawActivity.this);

            nameInMap = (TextView) findViewById(R.id.nameInMap);
            addressInMap = (TextView) findViewById(R.id.addressInMap);
            callIconInMap = (ImageView) findViewById(R.id.callIconInMap);

            progressDialog=new CustomProgressDialog(PathDrawActivity.this, 1);

            startVisit = (Button) findViewById(R.id.startVisit);

            SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);

            if (status.equals("accepted") && visitStarted){
                startVisit.setText("End Visit");
            }

            if (status.equals("current")){
                startVisit.setVisibility(View.GONE);
            }

            if (type.equals("receiver")) {
                startVisit.setVisibility(View.GONE);
            }

            if(sharedPreferences != null) {
                //physicianId = sharedPreferences.getString("keyuserid", null);
                //Log.e("tag" , "user physicianId is in map :"+physicianId);
            }

            sp_select_med_group = (Spinner) findViewById(R.id.sp_select_med_group);
            sp_select_med_name = (Spinner) findViewById(R.id.sp_select_med_name);
            /*SpinnerListingAdapter subCategory = new SpinnerListingAdapter(PathDrawActivity.this, protocols);
            sp_select_med_group.setAdapter(subCategory);*/

        }


        public void setUserInfo()
        {
            nameInMap.setText(name);
            addressInMap.setText(address);

            callIconInMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(visitStarted)
                    {
                        if(checkPermission())
                        {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+phone));
                            startActivity(callIntent);
                        }
                        else
                        {
                            requestPermission();
                        }
                    }
                    else
                    {
                        //Toast.makeText(PathDrawActivity.this, "You must have to start visit to call this patient", Toast.LENGTH_SHORT).show();
                        //callAlertDialog();
                    }

                }
            });
        }


        public void callAlertDialog()
        {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.call_alert_dialog);
            dialog.show();

            TextView callAlertText = (TextView) dialog.findViewById(R.id.callAlertText);
            Button callAlertExitBtn = (Button) dialog.findViewById(R.id.callAlertExitBtn);

            callAlertText.setText("You must start visit before calling patient");

            callAlertExitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }

        private void requestPermission() {

            ActivityCompat.requestPermissions((Activity) PathDrawActivity.this, new String[]
                    {
                            CALL_PHONE,
                    }, REQUEST_PERMISSION_CODE);

        }

        public boolean checkPermission() {

            int phonePermissionResult = ContextCompat.checkSelfPermission(PathDrawActivity.this , CALL_PHONE);

            return phonePermissionResult == PackageManager.PERMISSION_GRANTED ;

        }

        public void clickListener()
        {
            startVisit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(startVisit.getText().toString().equals("End Visit"))
                    {
                        checkUpFinishDialog();
                    }
                    else
                    {
                        visitStartRequest();
                    }
                }
            });
        }

        public void checkUpFinishDialog() {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.checkup_finish_alert_dialog);
            dialog.show();

            Button finishVisitYes = (Button) dialog.findViewById(R.id.finishVisitYes);
            Button finishVisitNo = (Button) dialog.findViewById(R.id.finishVisitNo);

            finishVisitYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    billingService();
                }
            });

            finishVisitNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }

        public void checkUpCompleteDialog(String bill) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.checkup_complete_dialog);
        dialog.show();
        dialog.setCancelable(true);

        final TextView bill_tv = (TextView) dialog.findViewById(R.id.bill_tv);
        final EditText checkupCompleteDescription = (EditText) dialog.findViewById(R.id.checkupCompleteDescription);
        Button checkupCompleteSubmitButton = (Button) dialog.findViewById(R.id.checkupCompleteSubmitButton);

        bill_tv.setText(bill);

            checkupCompleteSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description = checkupCompleteDescription.getText().toString();

                if(description.equals("")) {
                    checkupCompleteDescription.setError("Please enter Prescription");
                }
                else {
                    endVisitRequest(description , dialog);
                }
                }
        });
    }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            //Initialize Google Play Services
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            }
            else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }

        protected synchronized void buildGoogleApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }


        @Override
        public void onConnected(Bundle bundle) {

            turnOnGPS();

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
            }

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            Log.e("log" , "mLastLocation is this "+mLastLocation);

            if (mLastLocation != null) {

                latitude = mLastLocation.getLatitude();
                longitude  = mLastLocation.getLongitude();
                Log.e("latlang" , "curlatitude "+latitude);
                Log.e("latlang" , "longitude "+longitude);

                latLng = new LatLng(latitude , longitude);

//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Customer Location");
//            markerOptions.icon((BitmapDescriptorFactory.fromResource(R.drawable.contact_us_loc)));
//            mCurrLocationMarker = mMap.addMarker(markerOptions);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(10).build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            } else {

                latLng = new LatLng(Double.valueOf(lat),Double.valueOf(lng));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(10).build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            if(mGoogleApiClient.isConnected()){

                final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    turnOnGPS();
                }
                else {

                    if(isInternetOn())
                    {
                        calculateShorDistance(latLng, destination);
                    }
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }

        }

        @Override
        public void onLocationChanged(Location location) {


            myCurrentLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            final double lat =  location.getLatitude();
            final double lng = location.getLongitude();
            Log.e("TAg", "  the change location lat is: " + lat);
            Log.e("TAg", "  the change location lng is: " + lng);
            //Place current location marker
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon((BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            //move map camera
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            LatLng currentLatLng = new LatLng(latitude, longitude);
            Log.e("TAG", "abc test CurrentLATLNG: " + latLng);
            Log.e("TAG", " abc test Static LatLng: " + currentLatLng);
            latitude = lat;
            longitude = lng;


            LatLng updateLatLng = new LatLng(latitude, longitude);

            if(isInternetOn())
            {
                calculateShorDistance(updateLatLng, destination);
            }


        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        //calculation distance
        public void calculateShorDistance(LatLng start, LatLng destination){

            String url = getUrl(start, destination);
            FetchUrl FetchUrl = new FetchUrl();
            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);

        }//end of calculate distance

        //market for DropOff Location
        public void addingMarkerOnDestination(LatLng drobOff, String dropOffTitle){
            mMap.addMarker(new MarkerOptions()
                    .position(drobOff)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                    .title(dropOffTitle));
        }

        private String getUrl(LatLng origin, LatLng dest) {

        Log.e("TEST","Origing Lat Lng: " + origin.latitude + "," + origin.longitude);
            // Origin of route
            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
            // Destination of route
            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
            // Sensor enabled
            String sensor = "sensor=false";
            // Building the parameters to the web service
            String parameters = str_origin + "&" + str_dest + "&" + sensor;
            // Output format
            String output = "json";
            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +"&key=AIzaSyDAJ7SvT6p-dOXxVQ7ALmMXHiOeqqV7Mhc";
            Log.e("TAG", "the url of map is for salman: " + url);
            return url;
        }

        /**
         * A method to download json data from url
         */
        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                Log.d("downloadUrl", data.toString());
                br.close();

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        // Fetches data from url passed
        private class FetchUrl extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... url) {
                // For storing data from web service
                String data = "";
                try {
                    // Fetching the data from web service
                    data = downloadUrl(url[0]);
                    Log.d("Background Task data", data.toString());
                } catch (Exception e) {
                    Log.d("Background Task", e.toString());
                }
                return data;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                ParserTask parserTask = new ParserTask();

                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);

            }
        }

        /**
         * A class to parse the Google Places in JSON format
         */
        private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

            // Parsing the data in non-ui thread
            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;

                try {
                    jObject = new JSONObject(jsonData[0]);
                    Log.d("ParserTask",jsonData[0].toString());
                    DataParser parser = new DataParser();
                    Log.d("ParserTask", parser.toString());

                    // Starts parsing data
                    routes = parser.parse(jObject);
                    Log.d("ParserTask","Executing routes");
                    Log.d("ParserTask",routes.toString());

                } catch (Exception e) {
                    Log.d("ParserTask",e.toString());
                    e.printStackTrace();
                }
                return routes;
            }

            // Executes in UI thread, after the parsing process
            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> result) {

                Log.e("tag" , "onPost Execute result data is : "+result);
                ArrayList<LatLng> points;

                lineOptions = null;

                // Traversing through all the routes
                if(result != null)
                {
                    if (result.size()!=0){
                        for (int i = 0; i < result.size(); i++) {
                            points = new ArrayList<>();
                            lineOptions = new PolylineOptions();
                            // Fetching i-th route
                            List<HashMap<String, String>> path = result.get(i);
                            // Fetching all the points in i-th route
                            for (int j = 0; j < path.size(); j++) {
                                HashMap<String, String> point = path.get(j);

                                double lat = Double.parseDouble(point.get("lat"));
                                double lng = Double.parseDouble(point.get("lng"));
                                LatLng position = new LatLng(lat, lng);

                                points.add(position);
                            }

                            // Adding all the points in the route to LineOptions
                            lineOptions.addAll(points);
                            lineOptions.width(10);
                            lineOptions.color(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));

                            Log.d("onPostExecute","onPostExecute lineoptions decoded");

                            addingMarkerOnDestination(destination, mDropOffLocation);

                        }
                    }

                }

                // Drawing polyline in the Google Map for the i-th route
                if(lineOptions != null) {
                    if (polyline!=null){
                        polyline.remove();
                        polyline =  mMap.addPolyline(lineOptions);
                    }else {
                        polyline = mMap.addPolyline(lineOptions);
                    }

                }
                else {
                    Log.e("onPostExecute","without Polylines drawn");
                }
            }
        }

        @Override
        protected void onDestroy() {

            if(mGoogleApiClient.isConnected()){
                mGoogleApiClient.disconnect();
            }

            super.onDestroy();

        }

        @Override
        protected void onPause() {
            super.onPause();
        }

    private void billingService(){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        //show pregress here

        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, API.Get_bill, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Billing Response: " + response.toString());

                progressDialog.dismiss();

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("msg");
                        Toast.makeText(PathDrawActivity.this, message, Toast.LENGTH_SHORT).show();

                        String bill = jObj.getString("bill");
                        String payment = jObj.getString("payment");

                        if (payment.equals("direct")){

                            checkUpCompleteDialog(bill);

                        } else if (payment.equals("indirect")){

                            String type = jObj.getString("type");

                            if (type.equals("medicines")){

                                JSONArray groupsArr = jObj.getJSONArray("groups");

                                for (int i = 0; i < groupsArr.length(); i++){

                                    ArrayList<MedData> medData = new ArrayList<>();

                                    JSONObject obj = groupsArr.getJSONObject(i);

                                    String medId = obj.getString("id");
                                    String medName = obj.getString("name");

                                    //Log.e("Bill: " ," Name: " + medName);

                                    JSONArray innerArr = obj.getJSONArray("medicines");

                                    for (int j = 0; j < innerArr.length(); j++){

                                        if (innerArr.length() != 0) {

                                            JSONObject innerObj = innerArr.getJSONObject(j);

                                            String id = innerObj.getString("id");
                                            String name = innerObj.getString("name");
                                            String group_id = innerObj.getString("group_id");
                                            String unit_price = innerObj.getString("unit_price");
                                            String unit = innerObj.getString("unit");

                                            medData.add(new MedData(id,name,group_id,unit_price,unit));

                                            //Log.e("Bill: ", " Name: " + name);

                                        }

                                    }

                                    medGroup.add(new MedGroups(medId,medName,medData));

                                }

                                Log.e("Tag","P MedGroup: " + medGroup.size());
                                Intent intent = new Intent(PathDrawActivity.this, IndirectMedBilling.class);
                                intent.putExtra("ARRAYLIST", medGroup);
                                intent.putExtra("bill",bill);
                                intent.putExtra("visit_id",visit_id);
                                startActivity(intent);
                                finish();

                            }

                            //checkUpCompleteIndirectDialog(bill);

                        }

                        /*Intent i = new Intent(SplashScreen.this, PathDrawActivity.class);
                        i.putExtra("visit_id", visit_id);
                        i.putExtra("status", "accepted");
                        i.putExtra("type", "sender");
                        i.putExtra("visitStarted", true);
                        startActivity(i);*/

                    } else {
                        String message = jObj.getString("msg");
                        Toast.makeText(PathDrawActivity.this, message, Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", Prefs.getUserIDFromPref(PathDrawActivity.this));
                params.put("visit_id", visit_id);

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

    // visit start webservice
    public void visitStartRequest(){

        // Tag used to cancel the request
        String cancel_req_tag = "visit start";

        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, API.Respond_to_request, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "visit start response: " + response.toString());
                //hideDialog();
                progressDialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String msg = jObj.getString("msg");

                        Toast.makeText(PathDrawActivity.this, msg, Toast.LENGTH_SHORT).show();

                        /*sharedPreferencesVisitStarted = getSharedPreferences("VisitStarted" , MODE_PRIVATE);

                           SharedPreferences.Editor editor = sharedPreferencesVisitStarted.edit();
                           editor.putString("visit_id" , visit_id);
                           editor.putString("patient_lat" , lat);
                           editor.putString("patient_lng" , lng);
                           editor.putString("patient_name" , "Patient Name");
                           editor.putString("patient_address" , "Address");
                           editor.putString("patient_phone" , "Phone Number");
                           editor.commit();*/

                        startVisit.setText("End Visit");
                        visitStarted = true;
                    } else {

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(PathDrawActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(PathDrawActivity.this);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", "visit start Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Service Not working", Toast.LENGTH_LONG).show();
                    //hideDialog();
                    progressDialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("key", API.KEY);
                    params.put("visit_id", visit_id);
                    params.put("status", status);
                    Log.e("TEST","Status: " + status);
                    params.put("user_id", Prefs.getUserIDFromPref(PathDrawActivity.this));

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

    // end visit webservice
    public void endVisitRequest(final String description, final Dialog dialog) {
            // Tag used to cancel the request
        String cancel_req_tag = "end visit";
        progressDialog.show();

        Log.e("tag" , "description in end visit : "+description);

        StringRequest strReq = new StringRequest(Request.Method.POST, API.Confirm_end_visit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "end visit response: " + response.toString());
                //hideDialog();
                progressDialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String msg = jObj.getString("msg");
                        Toast.makeText(PathDrawActivity.this, msg, Toast.LENGTH_SHORT).show();

                        service = new Intent(PathDrawActivity.this, UpdateLatLong.class);
                        stopService(service);

                        if(mGoogleApiClient.isConnected()){
                            mGoogleApiClient.disconnect();
                        }

                        sharedPreferencesVisitStarted = getSharedPreferences("VisitStarted" , MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferencesVisitStarted.edit();
                        editor.clear();
                        editor.commit();
                        visitStarted = false;

                        dialog.dismiss();
                        Intent intent = new Intent(PathDrawActivity.this , DashboardVeterinarian.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    } else {

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){

                            String message = jObj.getString("msg");
                            Toast.makeText(PathDrawActivity.this, message, Toast.LENGTH_SHORT).show();

                        } else {

                            API.logoutService(PathDrawActivity.this);

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();

                }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("TAG", "end visit Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Service Not Working", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("key", API.KEY);
                    params.put("visit_id", visit_id);
                    params.put("prescription", description);
                    params.put("user_id", Prefs.getUserIDFromPref(PathDrawActivity.this));

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

        // end visit webservice

        @Override
        public void onBackPressed() {
            if(visitStarted)
            {
                Log.e("tag" , "visit started that why not allow to back");
            }
            else
            {
                if(mGoogleApiClient.isConnected()){
                    mGoogleApiClient.disconnect();
                }
                finish();
                super.onBackPressed();
            }
        }

        public void turnOnGPS(){

            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(PathDrawActivity.this)
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
                                            PathDrawActivity.this, 1000);
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
           // networkChange();
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


        public class cancelRequestReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("msg" );

                // Toast.makeText(context, msg , Toast.LENGTH_SHORT).show();

                if(!isFinishing())
                {
                    //adminCancelAlertDialog(msg);

                }


            }
        }

        /*public void adminCancelAlertDialog(String msg)
        {



            final Dialog dialog = new Dialog(PathDrawActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.admin_cancel_request_dialog);
            dialog.setCancelable(false);
            //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();


            service = new Intent(PathDrawActivity.this, UpdateLatLong.class);
            stopService(service);

            TextView adminCancelRequest = (TextView) dialog.findViewById(R.id.adminCancelRequest);
            Button adminCancelRequestDone = (Button) dialog.findViewById(R.id.adminCancelRequestDone);

            adminCancelRequest.setText(msg);

            adminCancelRequestDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    LocalBroadcastManager.getInstance(PathDrawActivity.this).unregisterReceiver(new cancelRequestReceiver());


                    service = new Intent(PathDrawActivity.this, UpdateLatLong.class);
                    stopService(service);

                    sharedPreferencesVisitStarted = getSharedPreferences("VisitStarted" , MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesVisitStarted.edit();
                    editor.clear();
                    editor.commit();
                    visitStarted = false;

                    Intent intent = new Intent(PathDrawActivity.this , DashboardVeterinarian.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });

        }*/
    }