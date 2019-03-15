package betaar.pk;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import betaar.pk.Adapters.DocListingMapAdapter;
import betaar.pk.Adapters.SpinnerListingAdapter;
import betaar.pk.Config.API;
import betaar.pk.Helpers.CustomProgressDialog;
import betaar.pk.Models.DataIds;
import betaar.pk.Models.RequestModelVet;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;


public class CallVetForDairy extends DrawerActivityForClient {

    RelativeLayout bt_treatment, bt_nutritionist, bt_breeding, bt_surgeon;

    String category_id = null;
    String sub_category_id = null;
    CustomProgressDialog progressDialog;
    RadioGroup ll;
    Spinner sp_select_surgery;
    SpinnerListingAdapter subCategory;

    private ArrayList<DataIds> protocols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_call_vet_for_dairy);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_call_vet_for_dairy, null, false);
        mDrawerLayout.addView(contentView, 0);

        category_id = getIntent().getStringExtra("category_id");
        sub_category_id = getIntent().getStringExtra("sub_category_id");
        Log.e("TAG","Sub Category2: " + sub_category_id);

        init();
        btTreatmentClickHandler();
        btNuetritionClickHanlder();
        btBreedingClickHanlder();
        btSurGeonClickHandler();

    }

    private void init(){

        bt_treatment = (RelativeLayout) findViewById(R.id.bt_treatment);
        bt_nutritionist = (RelativeLayout) findViewById(R.id.bt_nutritionist);
        bt_breeding = (RelativeLayout) findViewById(R.id.bt_breeding);
        bt_surgeon = (RelativeLayout) findViewById(R.id.bt_surgeon);
        progressDialog = new CustomProgressDialog(CallVetForDairy.this, 1);
        protocols = new ArrayList<>();

    }

    private void btTreatmentClickHandler(){

        bt_treatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(CallVetForDairy.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_treament);
                final EditText dialog_edit_text = (EditText) dialog.findViewById(R.id.dialog_edit_text);
                final RadioGroup dialog_radio_group = (RadioGroup) dialog.findViewById(R.id.dialog_radio_group);
                Button bt_dialog_find = (Button) dialog.findViewById(R.id.bt_dialog_find);

                getProtocols(category_id,sub_category_id,"1",dialog_radio_group.getId(),dialog);

                bt_dialog_find.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.e("TAG", "the seleted Radio Group id is: " + ll.getCheckedRadioButtonId());
                        if (dialog_edit_text.getText().toString().equals("")){

                            Toast.makeText(CallVetForDairy.this,"Please enter Number of Animals", Toast.LENGTH_SHORT).show();

                        }else {

                            Intent intent = new Intent(CallVetForDairy.this, MapsActivityForFindingVet.class);
                            intent.putExtra("specialization_id","1");
                            intent.putExtra("category_id",category_id);
                            intent.putExtra("sub_category_id",sub_category_id);
                            intent.putExtra("purpose_id",String.valueOf(ll.getCheckedRadioButtonId()));
                            Log.e("TEG","Purpose_ID: " + ll.getCheckedRadioButtonId());
                            intent.putExtra("animal_no",dialog_edit_text.getText().toString());
                            startActivity(intent);
                            dialog.dismiss();

                        }
                    }
                });

            }
        });


    }

    private void btNuetritionClickHanlder(){

        bt_nutritionist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(CallVetForDairy.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_number_of_animals_nutritionist);
                final EditText dialog_edit_text = (EditText) dialog.findViewById(R.id.dialog_edit_text);
                Button bt_dialog_find = (Button) dialog.findViewById(R.id.bt_dialog_find);
                bt_dialog_find.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (dialog_edit_text.getText().toString().equals("")) {

                            Toast.makeText(CallVetForDairy.this, "Please enter Number of Animals", Toast.LENGTH_SHORT).show();

                        } else {

                            dialog.dismiss();

                            Intent intent = new Intent(CallVetForDairy.this, MapsActivityForFindingVet.class);
                            intent.putExtra("specialization_id", "2");
                            intent.putExtra("category_id", category_id);
                            intent.putExtra("sub_category_id", sub_category_id);
                            intent.putExtra("purpose_id","");
                            intent.putExtra("animal_no", dialog_edit_text.getText().toString());
                            startActivity(intent);

                        }
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                dialog.show();
            }
        });
    }

        private void btBreedingClickHanlder(){

        bt_breeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(CallVetForDairy.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_for_breeding);

                final RadioGroup dialog_radio_group = (RadioGroup) dialog.findViewById(R.id.dialog_radio_group);
                final EditText dialog_edit_text = (EditText) dialog.findViewById(R.id.dialog_edit_text);

                getProtocols(category_id,sub_category_id,"3",dialog_radio_group.getId(),dialog);

                Button bt_dialog_find = (Button) dialog.findViewById(R.id.bt_dialog_find);
                bt_dialog_find.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.e("TAG", "the seleted Radio Group id is: " + ll.getCheckedRadioButtonId());

                        if (dialog_edit_text.getText().toString().equals("")) {

                            Toast.makeText(CallVetForDairy.this, "Please enter Number of Animals", Toast.LENGTH_SHORT).show();

                        } else {

                            dialog.dismiss();
                        Intent intent = new Intent(CallVetForDairy.this, MapsActivityForFindingVet.class);
                            intent.putExtra("specialization_id","3");
                            intent.putExtra("category_id",category_id);
                            intent.putExtra("sub_category_id",sub_category_id);
                            intent.putExtra("purpose_id",String.valueOf(ll.getCheckedRadioButtonId()));
                            intent.putExtra("animal_no",dialog_edit_text.getText().toString());
                            startActivity(intent);
                    }

                    }
                });
            }
        });
    }

    private void btSurGeonClickHandler(){
        bt_surgeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(CallVetForDairy.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_number_of_animals);

                sp_select_surgery = (Spinner) dialog.findViewById(R.id.sp_select_surgery);

                //ArrayAdapter adapter = ArrayAdapter.createFromResource(CallVetForDairy.this,R.array.Surgeon,R.layout.spinner_item);

                subCategory = new SpinnerListingAdapter(CallVetForDairy.this, protocols);
                sp_select_surgery.setAdapter(subCategory);

                getProtocols(category_id,sub_category_id,"4",0,dialog);

                //adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                //sp_select_surgery.setAdapter(adapter);

                final EditText dialog_edit_text = (EditText) dialog.findViewById(R.id.dialog_edit_text);

                Button bt_dialog_find = (Button) dialog.findViewById(R.id.bt_dialog_find);

                bt_dialog_find.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (sp_select_surgery.getSelectedItemPosition() == 0){

                            Toast.makeText(CallVetForDairy.this, "Please Select Surgery", Toast.LENGTH_SHORT).show();

                        } else if (dialog_edit_text.getText().toString().equals("")) {

                            Toast.makeText(CallVetForDairy.this, "Please enter Number of Animals", Toast.LENGTH_SHORT).show();

                        } else {

                            dialog.dismiss();

                            Intent intent = new Intent(CallVetForDairy.this, MapsActivityForFindingVet.class);
                            intent.putExtra("specialization_id", "4");
                            intent.putExtra("category_id", category_id);
                            intent.putExtra("sub_category_id", sub_category_id);
                            Log.e("TAG","Purpose_id: " + protocols.get(sp_select_surgery.getSelectedItemPosition()).getName());
                            intent.putExtra("purpose_id",protocols.get(sp_select_surgery.getSelectedItemPosition()).getId());
                            intent.putExtra("animal_no", dialog_edit_text.getText().toString());
                            startActivity(intent);

                        }
                    }
                });
            }
        });
    }

    private void getProtocols(final String category_id, final String sub_category_id, final String speciality_id, final int rd_id, final Dialog dialog) {

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        //show pregress here

        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, API.Get_protocols, new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Get Vets response Response: " + response.toString());

                progressDialog.dismiss();

                protocols.clear();

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String protocol_id = null;
                        String protocolName = null;

                        JSONArray vetsArray = jObj.getJSONArray("protocols");

                        for (int i = 0 ; i < vetsArray.length() ; i++) {

                            JSONObject obj = vetsArray.getJSONObject(i);

                            protocol_id = obj.getString("id");
                            protocolName = obj.getString("type");

                            Log.e("TAG", "the message from server is protocol_name: " + protocolName);

                            if (speciality_id.equals("4") && i == 0){
                                protocols.add(new DataIds("Surgeries","0"));
                            }

                            protocols.add(new DataIds(protocolName,protocol_id));

                            Log.e("TAG","Protocol: " + protocolName + " PID: " + protocol_id);
                            //docsList.add(new MapGetterSetter(name,protocol_id,distance,latlng));

                        }

                        if (speciality_id.equals("4") && rd_id == 0){
                            SpinnerListingAdapter subCategory = new SpinnerListingAdapter(CallVetForDairy.this, protocols);
                            sp_select_surgery.setAdapter(subCategory);
                        } else {
                            addRadioButtons(rd_id, dialog, vetsArray.length(), protocols);
                        }

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                        dialog.show();

                        String message = jObj.getString("msg");
                        Toast.makeText(CallVetForDairy.this, message, Toast.LENGTH_SHORT).show();

                    } else {
                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(CallVetForDairy.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(CallVetForDairy.this);

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
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", Prefs.getUserIDFromPref(CallVetForDairy.this));
                params.put("category_id", category_id);
                params.put("sub_category_id", sub_category_id);
                params.put("speciality_id", speciality_id);

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    public void addRadioButtons(int rd_id , Dialog dialog, int number, ArrayList<DataIds> protocols) {
        for (int row = 0; row < 1; row++) {
            ll = new RadioGroup(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 1; i <= number; i++) {
                RadioButton rdbtn = new RadioButton(this);
                //rdbtn.setId((row * 2) + i);
                rdbtn.setId(Integer.valueOf(protocols.get(i-1).getId()));
                rdbtn.setText("Radio " + rdbtn.getId());
                rdbtn.setText(protocols.get(i-1).getName());
                rdbtn.setTextColor(R.color.colorBlue);
                rdbtn.setTextSize(14);
                /*if (i == 1){
                    ll.check(rdbtn.getId());
                }*/
                //rdbtn.setBackgroundTintList(CallVetForDairy.this.getResources().getColorStateList(R.color.colorGreen));
                ll.addView(rdbtn);
            }
            ((ViewGroup) dialog.findViewById(rd_id)).addView(ll);

        }
    }

}