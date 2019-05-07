package betaar.pk;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import betaar.pk.Adapters.ListingAdapterCheckupRequest;
import betaar.pk.Config.API;
import betaar.pk.Models.RequestModelVet;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class CheckupRequestsListing extends DrawerActivityForVeterinarian {

    RecyclerView rv_checkup_requests;
    LinearLayoutManager linearLayoutManager;
    ListingAdapterCheckupRequest dataAdapter;
    public ArrayList<RequestModelVet> requestData;

    ImageView progress_logo;
    Animation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_checkup_requests_listing);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_checkup_requests_listing, null, false);
        mDrawerLayout.addView(contentView, 0);

        ini();
        //myProducts("45");

    }


    private void ini(){

        rv_checkup_requests = (RecyclerView) findViewById(R.id.rv_checkup_requests);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL, false);
        rv_checkup_requests.setLayoutManager(linearLayoutManager);
        /*DividerItemDecoration itemDecorator = new DividerItemDecoration(FarmSolutionListing.this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(FarmSolutionListing.this, R.drawable.list_item_divider));
        rv_farm_solution.addItemDecoration(itemDecorator);*/
        requestData = new ArrayList<RequestModelVet>();

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        dataAdapter = new ListingAdapterCheckupRequest(CheckupRequestsListing.this, requestData,"sender");
        rv_checkup_requests.setAdapter(dataAdapter);

        checkUpRequests();

    }

    private void checkUpRequests(){

        // Tag used to cancel the request
        String cancel_req_tag = "My Products";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        StringRequest strReq = new StringRequest(Request.Method.POST, API.Get_requests, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Get Pending Requests Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONArray visits = jObj.getJSONArray("visits");

                        for (int i = 0; i < visits.length(); i++){

                            JSONObject obj = visits.getJSONObject(i);

                            String visit_id = obj.getString("visit_id");
                            String user_id_sender = obj.getString("user_id_sender");
                            String user_id_receiver = obj.getString("user_id_receiver");
                            String immediate_visit = obj.getString("immediate_visit");
                            String date_of_visit = obj.getString("date_of_visit");
                            String time_of_visit = obj.getString("time_of_visit");
                            String user_address = obj.getString("user_address");
                            String user_lat = obj.getString("user_lat");
                            String user_lng = obj.getString("user_lng");
                            String reason_of_visit = obj.getString("reason_of_visit");
                            String no_of_animals = obj.getString("no_of_animals");
                            String speciality_id = obj.getString("speciality_id");
                            String visit_started_at = obj.getString("visit_started_at");
                            String visit_ended_at = obj.getString("visit_ended_at");
                            String created_at = obj.getString("created_at");
                            String protocol_id = obj.getString("protocol_id");

                            JSONObject obj1 = obj.getJSONObject("sender");

                            String name = obj1.getString("name");
                            String phone = obj1.getString("phone");

                            JSONObject obj2 = obj.getJSONObject("category");
                            String category_name = obj2.getString("name");
                            String category_id = obj2.getString("id");

                            JSONObject obj3 = obj.getJSONObject("sub_category");
                            String sub_category_name = obj3.getString("name");
                            String sub_category_id = obj3.getString("id");

                            String protocol = "No Protocol";

                            if (!protocol_id.equals("0")) {

                                JSONObject obj4 = obj.getJSONObject("protocol");
                                protocol = obj4.getString("type");

                            }

                            Log.e("TeG","Protocol: " + protocol);

                            requestData.add(new RequestModelVet(visit_id,user_id_sender,user_id_receiver,immediate_visit,date_of_visit,time_of_visit,
                                    user_address,user_lat,user_lng,reason_of_visit,no_of_animals,speciality_id,protocol,visit_started_at,visit_ended_at,
                                    created_at,name,phone,category_name,sub_category_name));

                        }

                        dataAdapter = new ListingAdapterCheckupRequest(CheckupRequestsListing.this, requestData,"sender");
                        rv_checkup_requests.setAdapter(dataAdapter);

                        String message = jObj.getString("msg");
                        Toast.makeText(CheckupRequestsListing.this, message, Toast.LENGTH_SHORT).show();

                    } else {
                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(CheckupRequestsListing.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(CheckupRequestsListing.this);

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

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", Prefs.getUserIDFromPref(CheckupRequestsListing.this));
                params.put("type", "pending");

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


}
