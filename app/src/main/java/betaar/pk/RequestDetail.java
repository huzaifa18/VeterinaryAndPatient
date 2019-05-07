package betaar.pk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import betaar.pk.Config.API;
import betaar.pk.Helpers.CustomProgressDialog;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class RequestDetail extends AppCompatActivity {

    String visit_id = null;
    String immediate_visit= null;
    String date_of_visit= null;
    String time_of_visit= null;
    String user_address= null;
    String lat= null;
    String lng= null;
    String reason_of_visit= null;
    String no_of_animals= null;
    String protocol= null;
    String name= null;
    String phone= null;
    String category= null;
    String sub_category= null;
    String request_type= null;
    String type = null;

    RelativeLayout accept_btn;
    RelativeLayout cancel_btn;
    TextView tv_visit_id;
    TextView tv_name;
    TextView tv_protocol;
    TextView tv_request_type;
    TextView tv_date;
    TextView tv_time;
    TextView tv_noOfAnimals;
    TextView tv_address;
    TextView tv_reason;
    TextView tv_category;
    TextView tv_sub_category;
    TextView tv_btn_cancel;
    TextView tv_btn_accept;
    TextView tv_note;

    LinearLayout ll_date;
    LinearLayout ll_time;

    View v_line_date;
    View v_line_time;

    CustomProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        
        getData();
        initialize();
    }

    private void initialize() {
        
        accept_btn = (RelativeLayout) findViewById(R.id.rl_bt_accept);
        cancel_btn = (RelativeLayout) findViewById(R.id.rl_bt_cancel);
        tv_visit_id = (TextView) findViewById(R.id.tv_visit_id);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_protocol = (TextView) findViewById(R.id.tv_protocol);
        tv_request_type = (TextView) findViewById(R.id.tv_type);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_noOfAnimals = (TextView) findViewById(R.id.tv_no_of_animals);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_reason = (TextView) findViewById(R.id.tv_reason);
        tv_category = (TextView) findViewById(R.id.tv_category);
        tv_sub_category = (TextView) findViewById(R.id.tv_sub_category);
        tv_btn_cancel = (TextView) findViewById(R.id.tv_btn_cancel);
        tv_btn_accept = (TextView) findViewById(R.id.tv_btn_accept);
        tv_note = (TextView) findViewById(R.id.tv_note);

        ll_date = (LinearLayout) findViewById(R.id.ll_date);
        ll_time = (LinearLayout) findViewById(R.id.ll_time);

        v_line_date = (View) findViewById(R.id.v_line_date);
        v_line_time = (View) findViewById(R.id.v_line_time);

        progressDialog = new CustomProgressDialog(RequestDetail.this, 1);
        
        setData();
        acceptButtonClickListener();
        cancelButtonClickListener();

        Log.e("TEST","Cancel BTN TXT: " + tv_btn_cancel.getText().toString());
        
    }

    private void setData() {

        if (request_type.equals("cancelled")){

            accept_btn.setVisibility(View.GONE);
            tv_note.setVisibility(View.VISIBLE);
            tv_note.setText("Request Cancelled!");
            tv_btn_cancel.setText("Go to DashBoard");

        } else if (request_type.equals("accepted") || request_type.equals("current")){

            cancel_btn.setVisibility(View.GONE);
            tv_note.setVisibility(View.VISIBLE);
            tv_note.setText("Request Accepted!");
            tv_btn_accept.setText("View Location");

        } else if (request_type.equals("completed")){

            cancel_btn.setVisibility(View.GONE);
            tv_note.setVisibility(View.GONE);
            tv_note.setText("Request Completed!");
            accept_btn.setVisibility(View.GONE);

        }
        
        tv_name.setText(name);
        tv_protocol.setText(protocol);

        if (immediate_visit.equals("1")) {
            tv_request_type.setText("Immidiate");
        } else {
            tv_request_type.setText("Scheduled");
        }

        tv_visit_id.setText(visit_id);
        tv_date.setText(date_of_visit);
        tv_time.setText(time_of_visit);
        tv_noOfAnimals.setText(no_of_animals);
        tv_address.setText(user_address);
        tv_reason.setText(reason_of_visit);
        tv_category.setText(category);
        tv_sub_category.setText(sub_category);

        if (immediate_visit.equals("1")){

            ll_date.setVisibility(View.GONE);
            ll_time.setVisibility(View.GONE);
            v_line_date.setVisibility(View.GONE);
            v_line_time.setVisibility(View.GONE);

        }
        
    }

    private void getData() {

        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        lat = getIntent().getStringExtra("lat");
        lng = getIntent().getStringExtra("lng");
        visit_id = getIntent().getStringExtra("visit_id");
        immediate_visit = getIntent().getStringExtra("immediate_visit");
        date_of_visit = getIntent().getStringExtra("date_of_visit");
        time_of_visit = getIntent().getStringExtra("time_of_visit");
        user_address = getIntent().getStringExtra("user_address");
        reason_of_visit = getIntent().getStringExtra("reason_of_visit");
        no_of_animals = getIntent().getStringExtra("no_of_animals");
        protocol = getIntent().getStringExtra("protocol");
        category = getIntent().getStringExtra("category");
        sub_category = getIntent().getStringExtra("sub_category");
        request_type = getIntent().getStringExtra("request_type");
        type = getIntent().getStringExtra("type");

        Log.e("TAG","Request Type: " + request_type);
        Log.e("TAG","Type: " + type);

    }

    private void acceptButtonClickListener() {
        
        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestDetail.this, PathDrawActivity.class);
                i.putExtra("lat",lat);
                i.putExtra("lng",lng);
                i.putExtra("visit_id",visit_id);
                i.putExtra("name",name);
                i.putExtra("address",user_address);
                i.putExtra("phone",phone);

                if (request_type.equals("pending") && type.equals("sender")){
                    i.putExtra("status","accepted");
                }else {
                    i.putExtra("status",request_type);
                }
                i.putExtra("type", type);
                RequestDetail.this.startActivity(i);
            }
        });
        
    }

    private void cancelButtonClickListener() {
        
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_btn_cancel.getText().toString().equals("Go to DashBoard")){

                    if (Prefs.getUserRoleFromPref(RequestDetail.this).equals("veterinarian")) {

                        Intent i = new Intent(RequestDetail.this, DashboardVeterinarian.class);
                        startActivity(i);
                        finish();

                    } else if (Prefs.getUserRoleFromPref(RequestDetail.this).equals("client")) {

                        Intent i = new Intent(RequestDetail.this, DashboardClient.class);
                        startActivity(i);
                        finish();

                    }

                } else {
                    cancelRequest(visit_id);
                }
            }
        });
        
    }

    // visit cancel webservice
    public void cancelRequest(final String visit_id){

        // Tag used to cancel the request
        String cancel_req_tag = "visit cancel";

        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, API.Respond_to_request, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "visit cancel response: " + response.toString());
                //hideDialog();
                progressDialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String msg = jObj.getString("msg");

                        Toast.makeText(RequestDetail.this, msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(RequestDetail.this,DashboardVeterinarian.class);
                        startActivity(i);
                        finish();

                    } else {

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(RequestDetail.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(RequestDetail.this);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "visit cancel Error: " + error.getMessage());
                Toast.makeText(RequestDetail.this, "Service Not working", Toast.LENGTH_LONG).show();
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
                params.put("status", "cancelled");
                params.put("user_id", Prefs.getUserIDFromPref(RequestDetail.this));

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(RequestDetail.this).addToRequestQueue(strReq, cancel_req_tag);
    }
    
}
