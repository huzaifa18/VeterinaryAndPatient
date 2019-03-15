package betaar.pk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import betaar.pk.Adapters.JobsListingAdapter;
import betaar.pk.Config.API;
import betaar.pk.Models.JobInfo;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class JobsListing extends AppCompatActivity {

    RecyclerView rv_checkup_requests;
    LinearLayoutManager linearLayoutManager;
    JobsListingAdapter dataAdapter;

    public ArrayList<JobInfo> jobsArray;

    ImageView progress_logo;
    Animation rotate;

    String spCategoryPosition = "";
    String spSalaryRangePosition = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_listing);

        spCategoryPosition = getIntent().getStringExtra("spCategoryPosition");
        spSalaryRangePosition = getIntent().getStringExtra("spSalaryRangePosition");

        Log.e("TEG","spCategoryPosition: " + spCategoryPosition);
        Log.e("TEG","Salary Range: " + spSalaryRangePosition);

        init();
    }
    
    public void init(){

        rv_checkup_requests = (RecyclerView) findViewById(R.id.rv_checkup_requests);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL, false);
        rv_checkup_requests.setLayoutManager(linearLayoutManager);
        /*DividerItemDecoration itemDecorator = new DividerItemDecoration(FarmSolutionListing.this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(FarmSolutionListing.this, R.drawable.list_item_divider));
        rv_farm_solution.addItemDecoration(itemDecorator);*/
        jobsArray = new ArrayList<JobInfo>();

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

       /* for (int i = 0; i<=5; i++) {
            requestData.add(new JobInfo("This is Title "+ i, "Name here " + i, "Age here "+ i, "Distance here "+ i));
        }*/

        dataAdapter = new JobsListingAdapter(JobsListing.this,  jobsArray);
        rv_checkup_requests.setAdapter(dataAdapter);

        searchJob(String.valueOf(spCategoryPosition),String.valueOf(spSalaryRangePosition));
        
    }

    public void searchJob(final String category_id, final String range_id){

        Log.e("SearchJob","Category Id: " + category_id + "\n Salary Range Id: " + range_id);

        // Tag used to cancel the request
        final String cancel_req_tag = "post-product";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
        byte[] data = bos.toByteArray();*/

        //ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");

        StringRequest strReq = new StringRequest(Request.Method.POST, API.Get_jobs, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Register Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAg", "the Obj: " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("msg");
                        Log.e("TAG", "the message from server is message: " + message);

                        JSONArray jobsJsonArray = jObj.getJSONArray("jobs");

                        for (int i = 0 ; i < jobsJsonArray.length() ; i++){

                            JSONObject obj = jobsJsonArray.getJSONObject(i);

                            String id = obj.getString("id");
                            String title = obj.getString("title");
                            String required_experience = obj.getString("required_experience");
                            String qualification = obj.getString("qualification");
                            String user_id = obj.getString("user_id");
                            String category_for_job_id = obj.getString("category_for_job_id");
                            String min_salary = obj.getString("min_salary");
                            String max_salary = obj.getString("max_salary");
                            String description = obj.getString("description");

                            jobsArray.add(new JobInfo(id,title,required_experience,qualification,user_id,category_for_job_id,min_salary,max_salary,description));
                        }

                        dataAdapter = new JobsListingAdapter(JobsListing.this,  jobsArray);
                        rv_checkup_requests.setAdapter(dataAdapter);
                        //showDialogue(message);

                    } else {
                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(JobsListing.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(JobsListing.this);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Product Add Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                //hide pregress here
                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


                SharedPreferences sharedPreferences  = getSharedPreferences("udid", 0);
                String userUdid = sharedPreferences.getString("udid", "null");

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", Prefs.getUserIDFromPref(JobsListing.this));
                params.put("category_id",category_id);
                params.put("range", range_id);
                Log.e("JOB","UserId: " + Prefs.getUserIDFromPref(JobsListing.this) + "\n Category_ID: " +
                category_id + "\n Range: " + range_id);
                /*params.put("photo1", image1.toString());
                params.put("photo2", image2);
                params.put("photo3", image3);
                params.put("photo4", image4);*/


                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }
    
}
