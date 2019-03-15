package betaar.pk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import betaar.pk.Adapters.SpinnerListingAdapter;
import betaar.pk.Config.API;
import betaar.pk.Models.JobInfo;
import betaar.pk.Models.MyProductsGetterSetter;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class SearchJobCategory extends DrawerActivityForVeterinarian {

    Button bt_search;
    RelativeLayout rl_spiner_job_category, rl_spiner_salary_range;
    Spinner sp_select_job_category, sp_select_salary_range;

    public ArrayList<JobInfo> jobsArray;

    ImageView progress_logo;
    Animation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //setContentView(R.layout.activity_search_job_category);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_search_job_category, null, false);
        mDrawerLayout.addView(contentView, 0);

        init();
        btSearchClickListener();
    }

    private void init(){

        bt_search = (Button) findViewById(R.id.bt_search);
        rl_spiner_job_category = (RelativeLayout) findViewById(R.id.rl_spiner_job_category);
        rl_spiner_salary_range = (RelativeLayout) findViewById(R.id.rl_spiner_salary_range);
        sp_select_job_category = (Spinner) findViewById(R.id.sp_select_job_category);
        sp_select_salary_range = (Spinner) findViewById(R.id.sp_select_salary_range);

        jobsArray = new ArrayList<JobInfo>();

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        /*ArrayAdapter adapterJobCategory = ArrayAdapter.createFromResource(SearchJobCategory.this,
                R.array.job_categories, R.layout.spinner_item);
        adapterJobCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_select_job_category.setAdapter(adapterJobCategory);*/

        SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.job_categories);
        sp_select_job_category.setAdapter(subCategory);

        /*ArrayAdapter adapterSalaryRange = ArrayAdapter.createFromResource(SearchJobCategory.this,
                R.array.salary_range, R.layout.spinner_item);
        adapterSalaryRange.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_select_salary_range.setAdapter(adapterSalaryRange);*/

        SpinnerListingAdapter adapterSalaryRange = new SpinnerListingAdapter(getApplicationContext(), Arrays.salary_range);
        sp_select_salary_range.setAdapter(adapterSalaryRange);

    }

    private void btSearchClickListener(){

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                int spCategoryPosition = sp_select_job_category.getSelectedItemPosition();
                int spSalaryRangePosition = sp_select_salary_range.getSelectedItemPosition();

                if (spCategoryPosition == 0) {

                    Toast.makeText(SearchJobCategory.this, "Please Select Job Category", Toast.LENGTH_SHORT).show();
                    //((TextView) sp_select_category.getSelectedView()).setError("Please Select Prodcut Type");
                    rl_spiner_job_category.setAnimation(animShake);

                } else if (spSalaryRangePosition == 0){

                    Toast.makeText(SearchJobCategory.this, "Please Select Salary Range", Toast.LENGTH_SHORT).show();
                    //((TextView) sp_select_category.getSelectedView()).setError("Please Select Prodcut Type");
                    rl_spiner_salary_range.setAnimation(animShake);

                } else {

                    Intent i = new Intent(SearchJobCategory.this, JobsListing.class);
                    i.putExtra("spCategoryPosition",String.valueOf(spCategoryPosition));
                    i.putExtra("spSalaryRangePosition", String.valueOf(Arrays.salary_range.get(spSalaryRangePosition).getName()));
                    startActivity(i);

                }
            }
        });
    }



}
