package betaar.pk;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import betaar.pk.Preferences.Prefs;
import betaar.pk.Services.UpdateLatLong;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class VerificationActivity extends AppCompatActivity {

    Button bt_submit;
    TextView tv_resent_code;
    String mUSERID = "-1";
    String user_name = "-1";
    String mUDID = "-1";
    String mVCODE = "-1";

    EditText et_firt_digit, et_second_digit, et_thirld_digit, et_fourth_digit;

    ImageView progress_logo;
    Animation rotate;

    String activity = "activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        init();
        submitClickListener();
        resentCode();
        textChangeListenerForField();
    }

    private void init(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        bt_submit = (Button)findViewById(R.id.bt_submit);
        tv_resent_code = (TextView) findViewById(R.id.tv_resent_code);

        activity = getIntent().getStringExtra("activity");
        Log.e("TAG","activity: "+activity);
        mUSERID = getIntent().getStringExtra("user_id");
        user_name = getIntent().getStringExtra("user_name");
        mUDID = Prefs.gettUserUDID(VerificationActivity.this);
        mVCODE = getIntent().getStringExtra("v_code");

        et_firt_digit = (EditText) findViewById(R.id.et_firt_digit);
        et_second_digit = (EditText) findViewById(R.id.et_second_digit);
        et_thirld_digit = (EditText) findViewById(R.id.et_thirld_digit);
        et_fourth_digit = (EditText) findViewById(R.id.et_fourth_digit);

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

    }


    private void submitClickListener(){

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                String firstDigit = et_firt_digit.getText().toString();
                String secondDigit = et_second_digit.getText().toString();
                String thirdDigit = et_thirld_digit.getText().toString();
                String fourthDigit = et_fourth_digit.getText().toString();

                if (firstDigit.length() == 0){

                    et_firt_digit.setAnimation(animShake);
                    Toast.makeText(VerificationActivity.this, "Should not be empty", Toast.LENGTH_SHORT).show();
                }
                else if (secondDigit.length() == 0){

                    et_second_digit.setAnimation(animShake);
                    Toast.makeText(VerificationActivity.this, "Should not be empty", Toast.LENGTH_SHORT).show();
                }
                else if (thirdDigit.length() == 0){

                    et_thirld_digit.setAnimation(animShake);
                    Toast.makeText(VerificationActivity.this, "Should not be empty", Toast.LENGTH_SHORT).show();
                }
                else if (fourthDigit.length() == 0){

                    et_fourth_digit.setAnimation(animShake);
                    Toast.makeText(VerificationActivity.this, "Should not be empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    String finalTypiedCode = firstDigit+secondDigit+thirdDigit+fourthDigit;
                    if (mVCODE.equals(finalTypiedCode)){

                        if (!activity.equals("ForgotPassword")){

                            verfiyUserService(mUSERID, mUDID, API.KEY);

                        } else {

                            Intent i = new Intent(VerificationActivity.this, ResetPasswordActivity.class);
                            i.putExtra("user_id",mUSERID);
                            startActivity(i);
                            finish();

                        }

                    }
                    else {
                        Toast.makeText(VerificationActivity.this, "Code not verified", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void showingDialog(){

        if (Prefs.getUserRoleFromPref(VerificationActivity.this).equals("veterinarian")){

            final Dialog dialog = new Dialog(VerificationActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_for_update_veterinary_profile);
            TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
            TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);
            final RelativeLayout bt_complte_profile = (RelativeLayout) dialog.findViewById(R.id.bt_complte_profile);
            final RelativeLayout bt_not_now = (RelativeLayout) dialog.findViewById(R.id.bt_not_now);

            //end now button of dialog
            bt_not_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(VerificationActivity.this, DashboardVeterinarian.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    startService(new Intent(VerificationActivity.this, UpdateLatLong.class));
                    finish();
                    dialog.dismiss();

                }
            }); //end of not now dialog button

            //update profile dialog button handler
            bt_complte_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(VerificationActivity.this, ProfileUpdateForVeterinary.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    startService(new Intent(VerificationActivity.this, UpdateLatLong.class));
                    finish();
                    dialog.dismiss();
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
            dialog.setCancelable(false);
            dialog.show();

        } else if (Prefs.getUserRoleFromPref(VerificationActivity.this).equals("client")) {

            final Dialog dialog = new Dialog(VerificationActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_layout_client_verfication);
            TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
            TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);
            final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

            bt_proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(VerificationActivity.this, DashboardClient.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    startService(new Intent(VerificationActivity.this, UpdateLatLong.class));
                    finish();
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
            dialog.setCancelable(false);
            dialog.show();

        } else if (Prefs.getUserRoleFromPref(VerificationActivity.this).equals("organization")){

            final Dialog dialog = new Dialog(VerificationActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_layout_client_verfication);
            TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
            TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);
            final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

            bt_proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(VerificationActivity.this, DashBoardOrganization.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("userId",mUSERID);
                    i.putExtra("userName",user_name);
                    startActivity(i);
                    startService(new Intent(VerificationActivity.this, UpdateLatLong.class));
                    finish();
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
            dialog.setCancelable(false);
            dialog.show();

        }

    }

    private void resentCode(){

        tv_resent_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                recentCodeService(mUSERID, mUDID, API.KEY);
            }
        });
    }

    //resend code service
    private void recentCodeService(final String userId, final String UDID_TOKEN, final String key){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.ResendCode, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Resend Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAg", "the Delete: " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("msg");
                        showingDialogForResendCode(message);


                    } else {
                        String message = jObj.getString("msg");
                        Toast.makeText(VerificationActivity.this, message, Toast.LENGTH_SHORT).show();

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){

                        } else {

                            API.logoutService(VerificationActivity.this);

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
                        error.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("key", key);
                params.put("user_id", userId);
                params.put("udid", UDID_TOKEN);
                params.put("type", "reset");

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }//end for resend code web service


    //verify user service
    //resend code service
    private void verfiyUserService(final String userId, final String UDID_TOKEN, final String key){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        //show pregress here

        Log.e("TAG", "Verify user Test: USER ID: " + userId + " UDID: " + UDID_TOKEN );

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.VerifyUser, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Verify user Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                            String info = jObj.getString("info");
                            JSONObject infoObject = new JSONObject(info);

                            String user_id = infoObject.getString("user_id");
                            String name = infoObject.getString("name");
                            String username = infoObject.getString("username");
                            String email = infoObject.getString("email");
                            String phone = infoObject.getString("phone");
                            String role = infoObject.getString("role");

                            Log.e("TAG", "the message from server is user id: " + user_id);
                            Log.e("TAG", "the message from server is name: " + name);
                            Log.e("TAG", "the message from server is username: " + username);
                            Log.e("TAG", "the message from server is email: " + email);
                            Log.e("TAG", "the message from server is phone: " + phone);
                            Log.e("TAG", "the message from server is role: " + role);

                            //adding data in preferences
                            Prefs.addPrefsForLogin(getApplicationContext(), user_id, name, username, email, phone, role, "123456");
                            String message = jObj.getString("msg");
                            Toast.makeText(VerificationActivity.this, message, Toast.LENGTH_SHORT).show();

                            showingDialog();


                    } else {
                        String message = jObj.getString("msg");
                        Toast.makeText(VerificationActivity.this, message, Toast.LENGTH_SHORT).show();

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){

                        } else {

                            API.logoutService(VerificationActivity.this);

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
                        "Server Connection Failed", Toast.LENGTH_LONG).show();
                //hid pregress here
                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", key);
                params.put("user_id", userId);
                params.put("udid", UDID_TOKEN);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of for verify user serice


    private void showingDialogForResendCode(final String dialogMessage){

        final  Dialog dialog = new Dialog(VerificationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_client_verfication);
        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);
        RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);
        TextView tv_proceed = (TextView) dialog.findViewById(R.id.tv_proceed);
        tv_dialog_title.setText("Code Resended" );
        tv_messasge.setText(dialogMessage);
        tv_proceed.setText("Ok");

        //end now button of dialog
        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        }); //end of not now dialog button


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.show();

    }

    private void textChangeListenerForField(){

        et_firt_digit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_second_digit.requestFocus();
            }
        });

        et_second_digit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_thirld_digit.requestFocus();
            }
        });


        et_thirld_digit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_fourth_digit.requestFocus();
            }
        });

    }
}


