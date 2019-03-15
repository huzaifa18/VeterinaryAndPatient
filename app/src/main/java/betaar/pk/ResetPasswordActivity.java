package betaar.pk;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class ResetPasswordActivity extends AppCompatActivity {

    String mUSERID = "-1";

    EditText newPassword;
    EditText confirmPassword;

    Button btn_submit;

    ImageView progress_logo;
    Animation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initialize();
    }

    private void initialize() {

        newPassword = (EditText) findViewById(R.id.et_new_password);
        confirmPassword = (EditText) findViewById(R.id.et_confirm_password);

        btn_submit = (Button) findViewById(R.id.bt_submit);

        mUSERID = getIntent().getStringExtra("user_id");

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        buttonClickListener();
    }

    private void buttonClickListener() {

        final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newPasswordtxt = newPassword.getText().toString();
                String confirmPasswordtxt = confirmPassword.getText().toString();

                if (newPasswordtxt.isEmpty()){

                    newPassword.setError("Required");
                    newPassword.setAnimation(animShake);

                } else if (confirmPasswordtxt.isEmpty()){

                    confirmPassword.setError("Required");
                    confirmPassword.setAnimation(animShake);

                } else if (newPasswordtxt.equals(confirmPasswordtxt)){

                    resetPasswordService(mUSERID,confirmPasswordtxt);

                }

            }
        });

    }

    //reset password service
    private void resetPasswordService(final String userId, final String password){

        // Tag used to cancel the request
        String cancel_req_tag = "reset";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.ResetPassword, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Reset Password Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAg", "Obj: " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("msg");
                        showingDialogForResetCode(message);

                    } else {
                        String message = jObj.getString("msg");
                        Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){

                        } else {

                            API.logoutService(ResetPasswordActivity.this);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Reset Password Error: " + error.getMessage());
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
                params.put("key", API.KEY);
                params.put("user_id", userId);
                params.put("password", password);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }//end for reset password web service

    private void showingDialogForResetCode(final String dialogMessage){

        final Dialog dialog = new Dialog(ResetPasswordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_client_verfication);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(false);
        dialog.show();


        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);
        tv_dialog_title.setText("Password Reset");
        tv_messasge.setText(dialogMessage);
        final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResetPasswordActivity.this, UserLoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });



    }

}
