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

public class VisitDetail extends AppCompatActivity {

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
    String bill = null;
    String user_id = null;

    RelativeLayout accept_btn;
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
    TextView tv_btn_accept;
    TextView tv_note;
    TextView tv_bill;

    LinearLayout ll_date;
    LinearLayout ll_time;

    View v_line_date;
    View v_line_time;

    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_detail);

        getData();
        initialize();

    }

    private void initialize() {

        accept_btn = (RelativeLayout) findViewById(R.id.rl_bt_accept);
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
        tv_btn_accept = (TextView) findViewById(R.id.tv_btn_accept);
        tv_note = (TextView) findViewById(R.id.tv_note);
        tv_bill = (TextView) findViewById(R.id.tv_bill);

        ll_date = (LinearLayout) findViewById(R.id.ll_date);
        ll_time = (LinearLayout) findViewById(R.id.ll_time);

        v_line_date = (View) findViewById(R.id.v_line_date);
        v_line_time = (View) findViewById(R.id.v_line_time);

        progressDialog = new CustomProgressDialog(VisitDetail.this, 1);

        setData();
        acceptButtonClickListener();


    }

    private void acceptButtonClickListener() {

        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (tv_btn_accept.getText().toString().equals("Go To Dashboard")){

                    if (Prefs.getUserRoleFromPref(VisitDetail.this).equals("Client")) {

                        Intent i = new Intent(VisitDetail.this, DashboardClient.class);
                        startActivity(i);
                        finish();

                    } else {

                        Intent i = new Intent(VisitDetail.this, DashboardVeterinarian.class);
                        startActivity(i);
                        finish();

                    }

                } else {
                    confirmVisit(visit_id);
                }

            }
        });

    }

    private void setData() {

        Log.e("TEST","Request Type: " + request_type);

        if (request_type.equals("finished")){
            accept_btn.setVisibility(View.VISIBLE);
            tv_note.setVisibility(View.VISIBLE);
            tv_note.setText("Visit Finished!");
            tv_btn_accept.setText("Go To Dashboard");
        }

        Log.e("TAG","Request Type: " + request_type);

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
        tv_bill.setText(bill);

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
        bill = getIntent().getStringExtra("bill");
        user_id = getIntent().getStringExtra("user_id");

        Log.e("TAG","Bill: " + bill);

    }

    // Bill Confirmation webservice
    public void confirmVisit(final String visit_id){

        // Tag used to cancel the request
        String cancel_req_tag = "visit cancel";

        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, API.Confirm_bill, new Response.Listener<String>() {

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

                        Toast.makeText(VisitDetail.this, msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(VisitDetail.this,DashboardVeterinarian.class);
                        startActivity(i);
                        finish();

                    } else {

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(VisitDetail.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(VisitDetail.this);

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
                Toast.makeText(VisitDetail.this, "Service Not working", Toast.LENGTH_LONG).show();
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
                params.put("user_id", Prefs.getUserIDFromPref(VisitDetail.this));
                params.put("feedback", "confirm");

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(VisitDetail.this).addToRequestQueue(strReq, cancel_req_tag);
    }
    
}
