package betaar.pk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import betaar.pk.Adapters.SpinnerListingAdapter;
import betaar.pk.Config.API;
import betaar.pk.Helpers.CustomProgressDialog;
import betaar.pk.Models.DataIds;
import betaar.pk.Models.MedGroups;
import betaar.pk.Preferences.Prefs;
import betaar.pk.Services.UpdateLatLong;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class IndirectMedBilling extends AppCompatActivity {

    Spinner sp_select_group_type;
    TextView basic_bill_tv;
    ArrayList<Integer> arr;
    LinearLayout ll_to_inflat;
    LinearLayout ll_to_inflat2;
    ArrayList<MedGroups> medGroup;
    ArrayList<MedGroups> medDetail;
    String bill;
    String visit_id;
    MedGroups med;
    Boolean a = false;
    View rowView;
    Button submitBtn;
    EditText description;
    CustomProgressDialog progressDialog;
    SharedPreferences sharedPreferencesVisitStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indirect_med_billing);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        medGroup = (ArrayList<MedGroups>) getIntent().getSerializableExtra("ARRAYLIST");
        visit_id = intent.getStringExtra("visit_id");
        bill = intent.getStringExtra("bill");
        Log.e("Tag","MedGroup: " + medGroup.size());
        Log.e("Tag","bill: " + bill);

        initialize();

    }

    private void initialize() {

        sp_select_group_type = (Spinner) findViewById(R.id.sp_select_group_type);
        basic_bill_tv = (TextView) findViewById(R.id.basic_bill_tv);
        ll_to_inflat = (LinearLayout) findViewById(R.id.to_inflate);
        ll_to_inflat2 = (LinearLayout) findViewById(R.id.to_inflate2);
        submitBtn = (Button) findViewById(R.id.checkupCompleteSubmitButton);
        description = (EditText) findViewById(R.id.checkupCompleteDescription);
        medDetail = new ArrayList<>();
        progressDialog=new CustomProgressDialog(IndirectMedBilling.this, 1);
        arr = new ArrayList<>();

        ll_to_inflat.removeAllViews();
        ll_to_inflat2.removeAllViews();

        List<DataIds> dti = new ArrayList<>();
        for (int i = 0 ; i < medGroup.size(); i++) {

            dti.add(new DataIds(medGroup.get(i).getName(), medGroup.get(i).getId()));

        }

        SpinnerListingAdapter category = new SpinnerListingAdapter(IndirectMedBilling.this, dti);
        sp_select_group_type.setAdapter(category);
        category.notifyDataSetChanged();

        basic_bill_tv.setText(bill);
        spinnerChangeListener();
        submitBtnClickListener();

    }

    private void submitBtnClickListener() {

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fbill = null;
                JSONArray jarr2 = new JSONArray();

                try {
                for(int i = 0; i < ll_to_inflat2.getChildCount(); i++){

                    JSONObject jobj3 = new JSONObject();
                    final View parantView = ll_to_inflat2.getChildAt(i);
                    CheckBox cb_name = (CheckBox) parantView.findViewById(R.id.cb_name);
                    EditText et_price = (EditText) parantView.findViewById(R.id.et_unit_price);

                    if (cb_name.isChecked()) {

                        if (et_price.getText().toString().equals("")){

                            et_price.setError("This field can not be empty!");

                        } else {

                            Log.e("TAG", "Childern: " + ll_to_inflat2.getChildCount());
                            //Log.e("TAG", "Group: " + medGroup.get(sp_select_group_type.getSelectedItemPosition()).getName() + " Name: " + cb_name.getText() + "\n Price: " + Integer.valueOf(et_price.getText().toString()) * Double.valueOf(medDetail.get(i).getMedData().get(0).getUnit_price()));
                            Toast.makeText(IndirectMedBilling.this, "Group: " + medGroup.get(sp_select_group_type.getSelectedItemPosition()).getName() + " Name: " + cb_name.getText(), Toast.LENGTH_LONG).show();

                            //jobj3.put("med_id",medGroup.get(arr.get(i)).getMedData().get(0).getId());
                            jobj3.put("med_id", arr.get(i));
                            jobj3.put("quantity", et_price.getText().toString());
                            jarr2.put(jobj3);
                            fbill = String.valueOf((Double.valueOf(et_price.getText().toString()) * Double.valueOf(medDetail.get(i).getMedData().get(0).getUnit_price())) + Double.valueOf(bill));
                            Log.e("TAG", "Price: " + et_price.getText().toString());
                            Log.e("TAG", "Unit Price: " + medDetail.get(i).getMedData().get(0).getUnit_price());

                        }

                    }

                }

                if (description.getText().toString().isEmpty()){

                    description.setError("This field can not be empty!");

                } else {

                    Log.e("TEG","OBJ: " + jarr2);

                    Log.e("TAG","user_id: " + Prefs.getUserIDFromPref(IndirectMedBilling.this));
                    Log.e("TAG","visit_id: " + visit_id);
                    Log.e("TAG","prescription: " + description.getText().toString());
                    Log.e("TAG","rx: " + fbill);
                    Log.e("TAG","json: " + jarr2);

                    endVisitRequest(fbill,jarr2.toString(),description.getText().toString());

                }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    private void spinnerChangeListener() {

        sp_select_group_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0){

                } else if (i == 1){

                } else if (i == 2){

                } else {

                }

                addView(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void addView(final int pos) {

        ll_to_inflat.removeAllViews();

        Log.e("ABC","Index: " + pos);

        String name = "";

    for (int i = 0; i < medGroup.get(pos).getMedData().size(); i++) {

        /*if (!medDetail.isEmpty() && medDetail.get(pos).getMedData().get(i).getName().equals(medGroup.get(pos).getMedData().get(i).getName())){
            Log.e("ABC","EXISTS");
        }*/

        for (int j = 0; j < ll_to_inflat2.getChildCount(); j++) {

            final View parantView = ll_to_inflat2.getChildAt(j);
            CheckBox cb_name = (CheckBox) parantView.findViewById(R.id.cb_name);

            name = cb_name.getText().toString();

        }

        Log.e("ABC", "Name: " + name);

        if (medGroup.get(pos).getMedData().get(i).getName().equals(name)) {

            Log.e("ABC", "EXISTS");

        } else {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.medicines_custom_layout, null);

            Log.e("TAG", "the count: " + i);

            ll_to_inflat.addView(rowView, ll_to_inflat.getChildCount());

            Log.e("TAG", "Selected Item Name: " + medGroup.get(pos).getMedData().get(i).getName());

            final CheckBox cb_name = (CheckBox) rowView.findViewById(R.id.cb_name);
            final EditText et_unit_price = (EditText) rowView.findViewById(R.id.et_unit_price);
            TextView tv_unit = (TextView) rowView.findViewById(R.id.tv_unit);

            et_unit_price.setHint(medGroup.get(pos).getMedData().get(i).getUnit_price() + "/" + medGroup.get(pos).getMedData().get(i).getUnit());
            tv_unit.setText(medGroup.get(pos).getMedData().get(i).getUnit());
            cb_name.setText(medGroup.get(pos).getMedData().get(i).getName());

            med = new MedGroups(medGroup.get(pos).getId(), medGroup.get(pos).getName(),
                    medGroup.get(pos).getMedData());

            final int index = i;
            a = false;
            cb_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    a = b;

                    if (b) {

                        addView2(pos, index);
                        ViewGroup parent = (ViewGroup) ll_to_inflat.getChildAt(index).getParent();
                        parent.removeView(ll_to_inflat.getChildAt(index));
                        cb_name.setChecked(false);
                        et_unit_price.setText("");
                        medDetail.add(med);
                        //Log.e("TAG","Size: " + medGroup.get(pos).getMedData().size());
                        //medGroup.get(pos).getMedData().remove(index);
                        //Log.e("TAG","Size: " + medGroup.get(pos).getMedData().size());

                    } else {

                        medDetail.remove(med);
                        //medGroup.add(med);
                    /*ViewGroup parent = (ViewGroup) ll_to_inflat.getChildAt(index).getParent();
                    parent.removeView(ll_to_inflat.getChildAt(index));*/

                    }

                }
            });

        }

    }
    }

    private void addView2(final int pos, final int indexx) {

        //ll_to_inflat2.removeAllViews();

        if (ll_to_inflat2.getChildCount() < 1){
            ll_to_inflat2.setVisibility(View.VISIBLE);
        }

        //for (int i = 0; i < medGroup.get(pos).getMedData().size(); i++) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.medicines_custom_layout, null);

            ll_to_inflat2.addView(rowView, ll_to_inflat2.getChildCount());

            Log.e("TAG", "Selected Item Name: " + medGroup.get(pos).getMedData().get(indexx).getName());

            CheckBox cb_name = (CheckBox) rowView.findViewById(R.id.cb_name);
            EditText et_unit_price = (EditText) rowView.findViewById(R.id.et_unit_price);
            TextView tv_unit = (TextView) rowView.findViewById(R.id.tv_unit);

            et_unit_price.setHint(medGroup.get(pos).getMedData().get(indexx).getUnit_price() + "/" + medGroup.get(pos).getMedData().get(indexx).getUnit());
            tv_unit.setText(medGroup.get(pos).getMedData().get(indexx).getUnit());
            cb_name.setText(medGroup.get(pos).getMedData().get(indexx).getName());
            cb_name.setChecked(true);

            med = new MedGroups(medGroup.get(pos).getId(), medGroup.get(pos).getName(),
                    medGroup.get(pos).getMedData());

        arr.add(Integer.valueOf(medGroup.get(pos).getMedData().get(indexx).getId()));

        Log.e("TAG", "the index: " + medGroup.get(pos).getMedData().get(indexx).getId());

        final int j = pos;
            a = false;
            cb_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    a = b;

                    if (b) {

                        //medDetail.add(med);

                    } else {

                        arr.remove(indexx);
                        medDetail.remove(med);
                        //medGroup.add(med);
                                //.get(j).setMedData(medGroup.get(j).getMedData());
                        //.add(indexx,medGroup.get(j).getMedData())
                        ViewGroup parent = (ViewGroup) ll_to_inflat2.getChildAt(indexx).getParent();
                        parent.removeView(ll_to_inflat2.getChildAt(indexx));
                        Log.e("ABC","Index: " + j);
                        addView(j);

                    }


                    Log.e("TAG","Index: " + indexx);
                    Log.e("TAG","rowView: " + rowView.getId());

                    Log.e("TAG", "MedDetail Size: " + medDetail.size());

                }
            });

        //}
    }

    // end visit webservice
    public void endVisitRequest(final String rx, final String json, final String description) {
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
                        Toast.makeText(IndirectMedBilling.this, msg, Toast.LENGTH_SHORT).show();

                        sharedPreferencesVisitStarted = getSharedPreferences("VisitStarted" , MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferencesVisitStarted.edit();
                        editor.clear();
                        editor.commit();

                        Intent intent = new Intent(IndirectMedBilling.this , DashboardVeterinarian.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    } else {

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){

                            String message = jObj.getString("msg");
                            Toast.makeText(IndirectMedBilling.this, message, Toast.LENGTH_SHORT).show();

                        } else {

                            API.logoutService(IndirectMedBilling.this);

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
                params.put("rx", rx);
                params.put("json", json);
                params.put("user_id", Prefs.getUserIDFromPref(IndirectMedBilling.this));

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}