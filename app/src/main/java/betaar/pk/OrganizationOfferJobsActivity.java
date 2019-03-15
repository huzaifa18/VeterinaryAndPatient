package betaar.pk;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import betaar.pk.Adapters.SpinnerListingAdapter;
import betaar.pk.Config.API;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class OrganizationOfferJobsActivity extends AppCompatActivity {

    RelativeLayout rl_spiner_salary_range;
    Spinner sp_select_qualification, sp_select_salary_range;
    EditText et_job_title, et_required_experience, et_qualifications, et_description;
    TextView tvTitle;

    Button bt_submit;

    ImageView progress_logo;
    Animation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_offer_jobs);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //getSupportActionBar().setTitle(R.string.title_offer_jobs);;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(OrganizationOfferJobsActivity.this ,R.color.colorBlue)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        onSubmitButtonClickHanlder();
    }

    private void init(){

        rl_spiner_salary_range = (RelativeLayout) findViewById(R.id.rl_spiner_salary_range);
        sp_select_qualification = (Spinner) findViewById(R.id.sp_select_qualification);
        sp_select_salary_range = (Spinner) findViewById(R.id.sp_select_salary_range);

        et_job_title = (EditText) findViewById(R.id.et_job_title);
        et_required_experience = (EditText) findViewById(R.id.et_required_experience);
        et_qualifications = (EditText) findViewById(R.id.et_qualifications);
        et_description = (EditText) findViewById(R.id.et_description);

        bt_submit = (Button) findViewById(R.id.bt_submit);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        tvTitle.setText(R.string.title_offer_jobs);

        /*ArrayAdapter adapterProductType = ArrayAdapter.createFromResource(OrganizationOfferJobsActivity.this,
                R.array.job_categories, R.layout.spinner_item);
        adapterProductType.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_select_qualification.setAdapter(adapterProductType);*/
        SpinnerListingAdapter adapterProductType = new SpinnerListingAdapter(OrganizationOfferJobsActivity.this , Arrays.job_categories);
        sp_select_qualification.setAdapter(adapterProductType);
        adapterProductType.notifyDataSetChanged();

        /*ArrayAdapter adapterSalaryRange = ArrayAdapter.createFromResource(OrganizationOfferJobsActivity.this,
                R.array.salary_range, R.layout.spinner_item);
        adapterSalaryRange.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_select_salary_range.setAdapter(adapterSalaryRange);*/
        SpinnerListingAdapter adapterSalaryRange = new SpinnerListingAdapter(OrganizationOfferJobsActivity.this , Arrays.salary_range);
        sp_select_salary_range.setAdapter(adapterSalaryRange);
        adapterSalaryRange.notifyDataSetChanged();

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void onSubmitButtonClickHanlder(){

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                int spItmePostion = sp_select_qualification.getSelectedItemPosition();
                String etJobTitle = et_job_title.getText().toString();
                String etRequiredExperience = et_required_experience.getText().toString();
                String etQualification = et_qualifications.getText().toString();
                String etDescription = et_description.getText().toString();

                if (spItmePostion == 0){
                    ((TextView)sp_select_qualification.getSelectedView()).setError("Please Select Category");
                    rl_spiner_salary_range.setAnimation(animShake);
                    Toast.makeText(OrganizationOfferJobsActivity.this, "Please Select Category", Toast.LENGTH_SHORT).show();
                } else if (etJobTitle.length() == 0){
                    et_job_title.setAnimation(animShake);
                    et_job_title.setError("Should not empty");
                } else if (etRequiredExperience.length() == 0){
                    et_required_experience.setAnimation(animShake);
                    et_required_experience.setError("Should not empty");
                }else if (etQualification.length() == 0){
                    et_qualifications.setAnimation(animShake);
                    et_qualifications.setError("Should not empty");
                }else if (etDescription.length() == 0){
                    et_description.setAnimation(animShake);
                    et_description.setError("Should not empty");
                }else {

                    Log.e("TAg", "values to send job title: " + etJobTitle);
                    Log.e("TAg", "values to send job experience: " + etRequiredExperience);
                    Log.e("TAg", "values to send job qualification: " + etQualification);
                    Log.e("TAG", "values to send selected Category: " + String.valueOf(Arrays.job_categories.get(sp_select_qualification.getSelectedItemPosition()).getId()));
                    Log.e("TAg", "values to send salary: " + String.valueOf(Arrays.salary_range.get(sp_select_salary_range.getSelectedItemPosition()).getName()));
                    Log.e("TAg", "values to send job title: " + etDescription);

                    Log.e("TAG","User ID: " + Prefs.getUserIDFromPref(OrganizationOfferJobsActivity.this));
                    postJob(etJobTitle,etRequiredExperience,etQualification,String.valueOf(Arrays.job_categories.get(sp_select_qualification.getSelectedItemPosition()).getId()),Prefs.getUserIDFromPref(OrganizationOfferJobsActivity.this),String.valueOf(Arrays.salary_range.get(sp_select_salary_range.getSelectedItemPosition()).getName()),etDescription);

                }
            }
        });
    }

    public void postJob(final String title, final String experience, final String qualification, final String category_id, final String user_id, final String salary_range, final String description) {


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

        StringRequest strReq = new StringRequest(Request.Method.POST, API.Post_job, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Register Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAg", "the Delete: " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("msg");
                        Log.e("TAG", "the message from server is message: " + message);

                        showDialogue(message);

                    } else {
                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(OrganizationOfferJobsActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(OrganizationOfferJobsActivity.this);

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
                params.put("title", title);
                params.put("experience", experience);
                params.put("qualification", qualification);
                params.put("category_id",category_id);
                params.put("user_id", user_id);
                params.put("salary_range", salary_range);
                params.put("description", description);
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

    private void showDialogue(String message){

        final Dialog dialog = new Dialog(OrganizationOfferJobsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_client_verfication);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(false);
        dialog.show();

        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);

        tv_dialog_title.setText("Offer Job");
        tv_messasge.setText(message);

        final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrganizationOfferJobsActivity.this, DashBoardOrganization.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }

}
