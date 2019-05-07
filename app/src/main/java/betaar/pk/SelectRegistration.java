package betaar.pk;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class SelectRegistration extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    RelativeLayout bt_call_a_veteriany, bt_client, bt_organization;

    TextView tv_help;

    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;

    public LatLng mLatLng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_select_registration);
        setContentView(R.layout.custome_singup_screen);

        init();
        signupAsVeterinarian();
        signupAsClient();
        signupAsOrganization();
        help();

        setUpGClient();

    }

    private void init() {

        bt_call_a_veteriany = (RelativeLayout) findViewById(R.id.bt_call_a_veteriany);
        bt_client = (RelativeLayout) findViewById(R.id.bt_client);
        bt_organization = (RelativeLayout) findViewById(R.id.bt_organization);
        tv_help = (TextView) findViewById(R.id.tv_help);

        tv_help.setText(Html.fromHtml("<u>" + getResources().getString(R.string.help_for_signup) + "</u>"));

    }

    private void signupAsVeterinarian() {

        bt_call_a_veteriany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // mLatLng = new LatLng(32.2215454, 74.2545454);

                if (mLatLng != null) {
                    Intent signupAsVeterinarian = new Intent(SelectRegistration.this, SignUpAsVeterinarian.class);
                    signupAsVeterinarian.putExtra("LAT", mLatLng.latitude);
                    signupAsVeterinarian.putExtra("LNG", mLatLng.longitude);
                    startActivity(signupAsVeterinarian);
                } else {

                    mLatLng = new LatLng(32.2215454, 74.2545454);
                    Intent signupAsVeterinarian = new Intent(SelectRegistration.this, SignUpAsVeterinarian.class);
                    signupAsVeterinarian.putExtra("LAT", mLatLng.latitude);
                    signupAsVeterinarian.putExtra("LNG", mLatLng.longitude);
                    startActivity(signupAsVeterinarian);
                }
            }
        });
    }

    private void signupAsClient() {

        bt_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLatLng = new LatLng(32.2215454, 74.2545454);
                if (mLatLng != null) {
                    Intent signupAsVeterinarian = new Intent(SelectRegistration.this, SignUpAsClient.class);
                    signupAsVeterinarian.putExtra("LAT", mLatLng.latitude);
                    signupAsVeterinarian.putExtra("LNG", mLatLng.latitude);
                    startActivity(signupAsVeterinarian);
                }

            }
        });
    }

    private void signupAsOrganization() {

        bt_organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLatLng = new LatLng(32.2215454, 74.2545454);
                if (mLatLng != null) {
                    Intent signupAsVeterinarian = new Intent(SelectRegistration.this, SignUpAsOrganization.class);
                    signupAsVeterinarian.putExtra("LAT", mLatLng.latitude);
                    signupAsVeterinarian.putExtra("LNG", mLatLng.latitude);
                    startActivity(signupAsVeterinarian);
                }
            }
        });
    }

    private void help() {

        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectRegistration.this, SignUpHelp.class));
            }
        });

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
                int permissionLocation = ContextCompat.checkSelfPermission(SelectRegistration.this,
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
                                            .checkSelfPermission(SelectRegistration.this,
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
                                        status.startResolutionForResult(SelectRegistration.this,
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
        int permissionLocation = ContextCompat.checkSelfPermission(SelectRegistration.this,
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
        int permissionLocation = ContextCompat.checkSelfPermission(SelectRegistration.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

}