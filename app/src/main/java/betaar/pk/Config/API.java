package betaar.pk.Config;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
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

import betaar.pk.CheckupRequestsListing;
import betaar.pk.DrawerActivityForClient;
import betaar.pk.DrawerActivityForVeterinarian;
import betaar.pk.Helpers.CustomProgressDialog;
import betaar.pk.Preferences.Prefs;
import betaar.pk.R;
import betaar.pk.RequestDetail;
import betaar.pk.Services.UpdateLatLong;
import betaar.pk.UserLoginActivity;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

import static betaar.pk.DrawerActivityForVeterinarian.fa;

/**
 * Created by Huzaifa Asif on 16-Apr-18.
 */

public class API {

    static CustomProgressDialog progressDialog;

    public static final String KEY = "!44zpt$Y@QC2Udm$nmBr6XVkyUbkNA3X?k*pr8kUb*%gG";
    public static final String BASE_URL = "http://ranglerz.net/betaar/betaar-my-api/";
    //public static final String BASE_URL = "http://betaar.pk/betaar-my-api/";
    public static final String Login = BASE_URL+"login";
    public static final String Logout = BASE_URL+"logout";
    public static final String ForgotPassword = BASE_URL+"forgot-password";
    public static final String ResetPassword = BASE_URL+"reset-password";
    public static final String ResendCode = BASE_URL+"resend-code";
    public static final String VerifyUser = BASE_URL+"verify-user";
    public static final String Register_veterinarian = BASE_URL+"register-veterinarian";
    public static final String Register_client = BASE_URL+"register-client";
    public static final String Register_organization = BASE_URL+"register-organization";
    public static final String Post_product = BASE_URL+"post-product";
    public static final String Update_product = BASE_URL+"update-product";
    public static final String Post_job = BASE_URL+"post-job";
    public static final String My_products = BASE_URL+"my-products";
    public static final String Get_products = BASE_URL+"get-products";
    public static final String Get_jobs = BASE_URL+"get-jobs";
    public static final String Update_vet = BASE_URL+"update-veterinarian";
    public static final String UPDATE_LAT_LNG = BASE_URL+"update-lat-lng";
    public static final String Get_vets = BASE_URL+"get-veterinarians";
    public static final String Send_Request_to_vets = BASE_URL+"send-request-to-veterinarian";
    public static final String Get_requests = BASE_URL+"get-requests";
    public static final String Respond_to_request = BASE_URL+"respond-to-request";
    public static final String CheckVisit = BASE_URL+"check-visit";
    public static final String Get_protocols = BASE_URL+"get-protocols";
    public static final String Get_bill = BASE_URL+"get-bill";
    public static final String Confirm_end_visit = BASE_URL+"confirm-end-visit";
    public static final String Confirm_bill = BASE_URL+"confirm-bill";

    //http://ranglerz.net/betaar/betaar-my-api/register-client?key=!44zpt$Y@QC2Udm$nmBr6XVkyUbkNA3X?k*pr8kUb*%gG&cnic=45541-4564564-4&address=122 B Sector C&name=Huzaifa Asif&username=shoaib&email=usman@ranglerz.com&phone=923124578456&password=123456&lat=31.45&lng=56&udid=123

    public static void showDialogue(Context context){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_client_verfication);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(false);
        dialog.show();

        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);

        tv_dialog_title.setText("Sorry for inconvenience!");
        tv_messasge.setText("This service is not available yet. Please wait for the next Update.");

        final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                /*Intent i = new Intent(getContext(), DashboardClient.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
            }
        });


    }

    public static void showLogoutDialogue(final Context context){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_client_verfication);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(false);
        dialog.show();

        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);

        tv_dialog_title.setText("Logging Out!");
        tv_messasge.setText("All your pending requests will be cancelled!");

        final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Prefs.clearPrefData(context);
                Intent i = new Intent(context, UserLoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);
                context.stopService(new Intent(context, UpdateLatLong.class));
                DrawerActivityForVeterinarian.fa.finish();
            }
        });


    }

    public static void logoutService(final Context context){

        // Tag used to cancel the request
        String cancel_req_tag = "logout";
        //show pregress here

        progressDialog = new CustomProgressDialog(context, 1);

        //progressDialog.show();
        
        StringRequest strReq = new StringRequest(Request.Method.POST, API.Logout, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Logout response Response: " + response.toString());

                //progressDialog.dismiss();

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("msg");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "message", Toast.LENGTH_SHORT).show();

                        showLogoutDialogue(context);

                    } else {
                        String message = jObj.getString("msg");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){

                        } else {

                            showLogoutDialogue(context);

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Log out Error: " + error.getMessage());
                Toast.makeText(context,"Server Connection Fail", Toast.LENGTH_LONG).show();
                //hid pregress here
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", Prefs.getUserIDFromPref(context));

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(context).addToRequestQueue(strReq, cancel_req_tag);
    }

}
