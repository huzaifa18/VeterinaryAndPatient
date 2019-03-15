package betaar.pk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import betaar.pk.Adapters.MedAdapter;
import betaar.pk.Config.API;
import betaar.pk.Helpers.CustomProgressDialog;
import betaar.pk.Models.MedGroups;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class BillConfirmation extends AppCompatActivity {

    TextView bill_tv;
    RecyclerView rv_medlist;

    String bill;
    ArrayList<MedGroups> medGroup = new ArrayList<>();

    MedAdapter medAdapter;

    CustomProgressDialog progressDialog;

    Button btn_confirm;

    String visit_id;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_confirmation);

        bill = getIntent().getStringExtra("bill");
        visit_id = getIntent().getStringExtra("visit_id");
        medGroup = (ArrayList<MedGroups>) getIntent().getSerializableExtra("array");

        initialize();

    }

    private void initialize() {

        bill_tv = (TextView) findViewById(R.id.bill_tv);
        rv_medlist = (RecyclerView) findViewById(R.id.rv_medlist);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        rv_medlist.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL,false));

        bill_tv.setText(bill);

        medAdapter = new MedAdapter(medGroup,BillConfirmation.this);
        rv_medlist.setAdapter(medAdapter);

        progressDialog = new CustomProgressDialog(BillConfirmation.this, 1);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmVisit(visit_id);
            }
        });

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

                        Toast.makeText(BillConfirmation.this, msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(BillConfirmation.this,DashboardVeterinarian.class);
                        startActivity(i);
                        finish();

                    } else {

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(BillConfirmation.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(BillConfirmation.this);

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
                Toast.makeText(BillConfirmation.this, "Service Not working", Toast.LENGTH_LONG).show();
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
                params.put("user_id", Prefs.getUserIDFromPref(BillConfirmation.this));
                params.put("feedback", "confirm");

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(BillConfirmation.this).addToRequestQueue(strReq, cancel_req_tag);
    }
    
}
