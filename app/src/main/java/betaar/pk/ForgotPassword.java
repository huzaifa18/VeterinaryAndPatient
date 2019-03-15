package betaar.pk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ForgotPassword extends AppCompatActivity {

    EditText et_email;
    Button btn_submit;

    ImageView progress_logo;
    Animation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initialize();
        buttonListener();
    }

    public void initialize() {

        et_email = (EditText) findViewById(R.id.et_email);
        btn_submit = (Button) findViewById(R.id.bt_submit);

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

    }

    public void buttonListener(){

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                String emailText = et_email.getText().toString();

                if (emailText.length() == 0){

                    et_email.setError("Should not be empty");
                    et_email.setAnimation(animShake);

                } else {
                    emailText = emailText.trim();

                    if (emailText.startsWith("03")){
                        emailText = emailText.substring(1);
                        emailText = "92"+emailText;
                    }

                    //String UDID = Prefs.gettUserUDID(ForgotPassword.this);
                    forgotPasswordService(emailText);

                }

            }
        });

    }

    private void forgotPasswordService( final String email ){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.ForgotPassword, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Forgot Password Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String user_id = jObj.getString("user_id");
                        String code = jObj.getString("code");

                        Log.e("TAG", "the message from server is user id: " + user_id);
                        Log.e("TAG", "the message from server is code: " + code);

                        //adding data in preferences
                        //Prefs.addPrefsForLogin(getApplicationContext(), user_id, name, username, email, phone, role, password);

                        Intent i = new Intent(ForgotPassword.this, VerificationActivity.class);
                        i.putExtra("activity","ForgotPassword");
                        i.putExtra("user_id",user_id);
                        i.putExtra("v_code",code);
                        startActivity(i);

                        String message = jObj.getString("msg");
                        Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();

                    } else {
                        String message = jObj.getString("msg");

                        Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){

                        } else {
                            API.logoutService(ForgotPassword.this);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Forgot Password Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Server Connection Fail", Toast.LENGTH_LONG).show();
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
                params.put("text", email);
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