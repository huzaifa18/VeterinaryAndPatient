package betaar.pk;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import betaar.pk.Config.API;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class RequestForm extends AppCompatActivity {

    EditText et_location;
    EditText et_date;
    EditText et_time;
    EditText et_description;
    Spinner sp_request;
    RelativeLayout bt_confirm;
    RelativeLayout rl_spiner;

    String capture_city;
    String capture_area;
    String capture_address;
    double capture_lat;
    double capture_lng ;

    int LOCATION_REQUEST = 500;

    ImageView progress_logo;
    Animation rotate;

    String user_id_reciever;
    String speciality_id;
    String no_of_animals;
    String protocol;
    String category_id;
    String sub_category_id;

    String date = "";
    String time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_form);

        user_id_reciever = getIntent().getStringExtra("user_id_reciever");
        speciality_id = getIntent().getStringExtra("speciality_id");
        no_of_animals = getIntent().getStringExtra("no_of_animals");
        protocol = getIntent().getStringExtra("protocol");
        category_id = getIntent().getStringExtra("category_id");
        sub_category_id = getIntent().getStringExtra("sub_category_id");

        initialize();
    }

    private void initialize() {

        et_location = (EditText) findViewById(R.id.location);
        et_time = (EditText) findViewById(R.id.time);
        et_date = (EditText) findViewById(R.id.date);
        et_description = (EditText) findViewById(R.id.et_description);
        sp_request = (Spinner) findViewById(R.id.spinner);
        bt_confirm = (RelativeLayout) findViewById(R.id.rl_bt_confirm);
        rl_spiner = (RelativeLayout) findViewById(R.id.rl_spiner);

        et_time.setVisibility(View.GONE);
        et_date.setVisibility(View.GONE);

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        locationClickListener();
        timeClickListener();
        dateClickListener();
        requestClickListener();
        confirmButtonClickListener();

        Intent i = new Intent(RequestForm.this,CaptureLocation.class);
        startActivityForResult(i , LOCATION_REQUEST);

    }

    private void requestClickListener() {

        sp_request.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0){

                    et_time.setVisibility(View.GONE);
                    et_date.setVisibility(View.GONE);
                    Toast.makeText(RequestForm.this,"Please select the request type",Toast.LENGTH_LONG);

                } else if (i == 1){

                    Calendar mcurrentTime = Calendar.getInstance();
                    int[] hour = {mcurrentTime.get(Calendar.HOUR_OF_DAY)};
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    int year = mcurrentTime.get(Calendar.YEAR);
                    int month = mcurrentTime.get(Calendar.MONTH);
                    int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);

                    et_time.setVisibility(View.GONE);
                    et_date.setVisibility(View.GONE);
                    time = hour+":"+minute+":00";
                    date = year+"-"+month+"-"+day;

                } else if (i == 2){

                    et_time.setVisibility(View.VISIBLE);
                    et_date.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void locationClickListener() {

        et_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RequestForm.this,CaptureLocation.class);
                startActivityForResult(i , LOCATION_REQUEST);

            }
        });

    }

    private void timeClickListener() {

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar mcurrentTime = Calendar.getInstance();
                final int[] hour = {mcurrentTime.get(Calendar.HOUR_OF_DAY)};
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RequestForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        time = selectedHour+":"+selectedMinute+":00";

                        String timeSet = "";
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            timeSet = "PM";
                        } else if (selectedHour == 0) {
                            selectedHour += 12;
                            timeSet = "AM";
                        } else if (selectedHour == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }

                        String min = "";
                        if (selectedMinute < 10)
                            min = "0" + selectedMinute ;
                        else
                            min = String.valueOf(selectedMinute);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(selectedHour).append(':')
                                .append(selectedMinute ).append(" ").append(timeSet).toString();
                        et_time.setText(aTime);

//                        String AM_PM ;
//
//                        mcurrentTime.set(Calendar.HOUR,selectedHour);
//                        mcurrentTime.set(Calendar.MINUTE,selectedMinute);
//                        if(selectedHour < 12) {
//                            AM_PM = "AM";
//                            mcurrentTime.set(Calendar.AM_PM,0);
//                        } else {
//                            AM_PM = "PM";
//                            mcurrentTime.set(Calendar.AM_PM,1);
//                        }
//
//                        String myFormat = "hh:mm"; //In which you need put here
//                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
//
//                        //et_time.setText( selectedHour + ":" + selectedMinute + " " + AM_PM);
//                        et_time.setText(sdf.format(mcurrentTime.getTime() +AM_PM));
                        
                    }
                }, hour[0], minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }

    private void dateClickListener() {

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar myCalendar = Calendar.getInstance();

                DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        date = year+"-"+monthOfYear+"-"+dayOfMonth;

                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        et_date.setText(sdf.format(myCalendar.getTime()));
                    }

                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(RequestForm.this,  datepicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("TAg", "The Request data is " + data);
        Log.e("TAg", "The Request code is: " + requestCode);

        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == LOCATION_REQUEST)
            {
                if(data != null)
                {
                    capture_city = data.getStringExtra("City");
                    capture_area = data.getStringExtra("Area");
                    capture_address = data.getStringExtra("Address");
                    capture_lat = data.getDoubleExtra("lat"  , 0.0);
                    capture_lng = data.getDoubleExtra("lng" , 0.0);
                    et_location.setText(capture_address);

                    Log.e("tag" , "onActivityResult City : " + capture_city);
                    Log.e("tag" , "onActivityResult Area : " + capture_area);
                    Log.e("tag" , "onActivityResult Address : " + capture_address);
                    Log.e("tag" , "onActivityResult lat : " + capture_lat);
                    Log.e("tag" , "onActivityResult lng : " + capture_lng);
                }
            }
        }
    }//end of onActivity result

    private void confirmButtonClickListener() {

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation animShake = AnimationUtils.loadAnimation(RequestForm.this, R.anim.shake);

                if (et_location.getText().toString().equals("")){

                    Log.e("Confirm" , "Location: " + et_location.getText().toString());
                    Toast.makeText(RequestForm.this, "Please Set Loctaion", Toast.LENGTH_SHORT).show();
                    et_location.setError("This field can not be empty");
                    et_location.setAnimation(animShake);

                } else if (sp_request.getSelectedItemPosition() == 0) {

                    Log.e("Confirm" , "Spinner: " + sp_request.getSelectedItemPosition());
                    Toast.makeText(RequestForm.this, "Please Select Request Type", Toast.LENGTH_SHORT).show();
                    rl_spiner.setAnimation(animShake);

                } else if (et_date.getText().toString().equals("") && sp_request.getSelectedItemPosition() == 2){

                    Log.e("Confirm" , "Date: " + date);
                    Toast.makeText(RequestForm.this, "Please Enter Date", Toast.LENGTH_SHORT).show();
                    et_date.setError("This field can not be empty");
                    et_date.setAnimation(animShake);

                } else if (et_time.getText().toString().equals("") && sp_request.getSelectedItemPosition() == 2){

                    Log.e("Confirm" , "Please Enter Time.");
                    Toast.makeText(RequestForm.this, "Please Enter Date", Toast.LENGTH_SHORT).show();
                    et_time.setError("This field can not be empty");
                    et_time.setAnimation(animShake);

                } else if (et_description.getText().toString().equals("")){

                    Log.e("Confirm" , "Description: " + et_description.getText().toString());
                    Toast.makeText(RequestForm.this, "Please Enter Reason of Visit", Toast.LENGTH_SHORT).show();
                    et_description.setError("This field can not be empty");
                    et_description.setAnimation(animShake);

                } else {

                    Log.e("Confirm" , "useridreciever: " + user_id_reciever);

                    String immediate_request = String.valueOf(sp_request.getSelectedItemId());
                    String description = et_description.getText().toString();
                    String lat = String.valueOf(capture_lat);
                    String lng = String.valueOf(capture_lng);

                    if (protocol.equals("")){
                        protocol = "0";
                    }

                    sendRequest(user_id_reciever,immediate_request,date,time,capture_address,lat,lng,description,no_of_animals,speciality_id,protocol);

                }

            }
        });

    }

    private void sendRequest(final String user_id_reciever, final String immediate_visit, final String date_of_visit, final String time_of_visit, final String user_address,
                             final String user_lat, final String user_lng, final String reason_of_visit, final String no_of_animals, final String speciality_id, final String protocol) {

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.Send_Request_to_vets, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Send Request to Vets response Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("msg");
                        showDialogue(message);
                        //Toast.makeText(RequestForm.this, message, Toast.LENGTH_SHORT).show();

                    } else {
                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(RequestForm.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(RequestForm.this);

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

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", Prefs.getUserIDFromPref(RequestForm.this));
                params.put("user_id_receiver", user_id_reciever);
                params.put("immediate_visit", immediate_visit);
                params.put("date_of_visit", date_of_visit);
                params.put("time_of_visit", time_of_visit);
                params.put("user_address", user_address);
                params.put("user_lat", user_lat);
                params.put("user_lng", user_lng);
                params.put("reason_of_visit", reason_of_visit);
                params.put("no_of_animals", no_of_animals);
                params.put("speciality_id", speciality_id);
                params.put("protocol", protocol);
                params.put("category_id", category_id);
                params.put("sub_category_id", sub_category_id);

                Log.e("TEST","Protocol: "+ protocol);

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

    private void showDialogue(String msg){

        final Dialog dialog = new Dialog(RequestForm.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_client_verfication);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(false);
        dialog.show();

        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);

        tv_dialog_title.setText("Request Alert!");
        tv_messasge.setText(msg);

        final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(RequestForm.this, DashboardClient.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });


    }

}
