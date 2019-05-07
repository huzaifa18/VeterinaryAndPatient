package betaar.pk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import betaar.pk.Config.API;
import betaar.pk.LocaleHelper.LocaleHelper;
import betaar.pk.Preferences.Prefs;
import betaar.pk.Services.UpdateLatLong;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class UserLoginActivity extends AppCompatActivity {

    Button bt_login;
    ImageView bt_register_as;
    EditText et_email, et_password;
    TextView tv_new_account,tv_forgot_password;

    ImageView progress_logo;
    Animation rotate;

    Resources resources;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.custome_login_screen);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            Log.e("TAG","Version Code: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        init();
        startingRegistrationSelectScreen();
        startingDashboardSelectScreen();
        onLoginTextTextFieldChange();
        //autoLogin();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,"ur"));
    }

    public void relaunch(Activity activity) {
        Intent intent = new Intent(activity, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        Runtime.getRuntime().exit(0);
        activity.finish();
    }

    private void init(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(UserLoginActivity.this ,R.color.colorBlue)));

        bt_login = (Button) findViewById(R.id.bt_login);
        bt_register_as = (ImageView) findViewById(R.id.bt_register_as);
        tv_new_account = (TextView) findViewById(R.id.tv_new_account);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);

        context = LocaleHelper.setLocale(UserLoginActivity.this, "ur");
        resources = context.getResources();
        bt_login.setText(resources.getString(R.string.login));
        tv_new_account.setText(resources.getString(R.string.create_account));
        tv_forgot_password.setText(resources.getString(R.string.forgot_password));


        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

    }

    private void startingDashboardSelectScreen(){

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                String textUsername = et_email.getText().toString();
                String password = et_password.getText().toString();

                /*if (textUsername.equals("v")){
                    Intent i = new Intent(UserLoginActivity.this, DashboardVeterinarian.class);
                    startActivity(i);
                } else if (textUsername.equals("c")){
                    Intent i = new Intent(UserLoginActivity.this, DashboardClient.class);
                    startActivity(i);
                } else if (textUsername.equals("o")){
                    Intent i = new Intent(UserLoginActivity.this, DashBoardOrganization.class);
                    startActivity(i);
                }*/
                //relaunch(UserLoginActivity.this);

                if (textUsername.length()==0){

                    et_email.setError("Should not be empty");
                    et_email.setAnimation(animShake);

                } else if (password.length() == 0){

                    et_password.setError("Should not be empty");
                    et_password.setAnimation(animShake);

                } else {
                    textUsername = textUsername.trim();

                    if (textUsername.startsWith("03")){
                        textUsername = textUsername.substring(1);
                        textUsername = "92"+textUsername;
                    }

                    String UDID = Prefs.gettUserUDID(UserLoginActivity.this);
                    callLoginService(textUsername, password, API.KEY, UDID);

                Intent startRegisterAsActivity = new Intent(UserLoginActivity.this, DashboardClient.class);
                //Intent startRegisterAsActivity = new Intent(UserLoginActivity.this, DashboardVeterinarian.class);
               // startActivity(startRegisterAsActivity);
            }

            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(UserLoginActivity.this,ForgotPassword.class));

            }
        });


    }

    private void startingRegistrationSelectScreen(){

        tv_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRegisterAsActivity = new Intent(UserLoginActivity.this, SelectRegistration.class);
                //Intent startRegisterAsActivity = new Intent(UserLoginActivity.this, ProfileUpdateForVeterinary.class);
                startActivity(startRegisterAsActivity);
            }
        });

        bt_register_as.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRegisterAsActivity = new Intent(UserLoginActivity.this, SelectRegistration.class);
                //Intent startRegisterAsActivity = new Intent(UserLoginActivity.this, ProfileUpdateForVeterinary.class);
                startActivity(startRegisterAsActivity);
            }
        });


    }

    private void callLoginService(final String text, final String password, final String key, final String UDID){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.Login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Login response Response: " + response.toString());

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
                        Prefs.addPrefsForLogin(getApplicationContext(), user_id, name, username, email, phone, role, password);

                        if (role.equals("veterinarian")){
                            startActivity(new Intent(UserLoginActivity.this, DashboardVeterinarian.class));
                            finish();
                            startService(new Intent(UserLoginActivity.this, UpdateLatLong.class));
                        } else if (role.equals("client")){
                            startActivity(new Intent(UserLoginActivity.this, DashboardClient.class));
                            finish();
                            startService(new Intent(UserLoginActivity.this, UpdateLatLong.class));
                        } else if (role.equals("organization")){
                            startActivity(new Intent(UserLoginActivity.this, DashBoardOrganization.class));
                            finish();
                            startService(new Intent(UserLoginActivity.this, UpdateLatLong.class));
                        }

                        String message = jObj.getString("msg");
                        Toast.makeText(UserLoginActivity.this, message, Toast.LENGTH_SHORT).show();

                    } else {
                        String message = jObj.getString("msg");
                        Toast.makeText(UserLoginActivity.this, message, Toast.LENGTH_SHORT).show();
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


               /* SharedPreferences sharedPreferences  = getSharedPreferences("udid", 0);
                String userUdid = sharedPreferences.getString("udid", "null");*/



               // String mPhone = phone.substring(1);

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", key);
                params.put("text", text);
                params.put("password", password);
                params.put("udid", UDID);

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

    public static boolean emailValidator(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    private void onLoginTextTextFieldChange(){

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable e) {

                String x = e.toString();
                if (x.startsWith("1") || x.startsWith("2")
                        || x.startsWith("3") || x.startsWith("4")
                        || x.startsWith("6") || x.startsWith("5")
                        || x.startsWith("7") || x.startsWith("8")
                        || x.startsWith("9")){

                    Toast.makeText(UserLoginActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("0")){
                    et_email.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
