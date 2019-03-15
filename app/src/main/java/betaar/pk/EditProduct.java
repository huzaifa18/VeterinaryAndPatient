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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class EditProduct extends AppCompatActivity {

    EditText et_name,et_price,et_description;

    Spinner sp_quantity, sp_unit;

    String name,price,description,quantity,unit,productId;

    RelativeLayout rl_bt_post,rl_spiner_quantity,rl_spiner_price_unit;

    TextView tv_title;

    ImageView progress_logo;
    Animation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        quantity = getIntent().getStringExtra("quantity");
        description = getIntent().getStringExtra("description");
        unit = getIntent().getStringExtra("unit");
        productId = getIntent().getStringExtra("productId");

        Log.e("TAG1","productId: " + productId
                + "\n Price: " + price
                + "\n quantity: " + quantity
                + "\n description: " + description
                + "\n unit: " + unit);

        init();
    }

    private void init(){

        tv_title = (TextView) findViewById(R.id.tv_title);
        et_name = (EditText) findViewById(R.id.et_product_name);
        et_price = (EditText) findViewById(R.id.et_product_price);
        et_description = (EditText) findViewById(R.id.et_product_description);

        sp_quantity = (Spinner) findViewById(R.id.sp_select_quantity);
        sp_unit = (Spinner) findViewById(R.id.sp_select_price_unit);

        rl_bt_post = (RelativeLayout) findViewById(R.id.rl_bt_post);
        rl_spiner_quantity = (RelativeLayout) findViewById(R.id.rl_spiner_quantity);
        rl_spiner_price_unit = (RelativeLayout) findViewById(R.id.rl_spiner_price_unit);

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        SpinnerListingAdapter priceUnit = new SpinnerListingAdapter(getApplicationContext(), Arrays.price_units);
        sp_unit.setAdapter(priceUnit);
        priceUnit.notifyDataSetChanged();

        SpinnerListingAdapter quantityUnit = new SpinnerListingAdapter(getApplicationContext(), Arrays.quantity);
        sp_quantity.setAdapter(quantityUnit);
        priceUnit.notifyDataSetChanged();

        setData();

    }

    private void setData(){

        tv_title.setText(name);

        et_name.setText(name);
        et_price.setText(price);
        et_description.setText(description);

        sp_unit.setSelection(Integer.parseInt(Arrays.getID(Arrays.price_units,unit)));
        sp_quantity.setSelection(Integer.parseInt(Arrays.getID(Arrays.quantity,quantity)));

        postButtonClick();

    }

    private void postButtonClick(){

        rl_bt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                if (et_name.getText().length() == 0){
                    Toast.makeText(EditProduct.this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
                    et_name.setAnimation(animShake);
                    et_name.setError("Should not be empty");
                } else if (et_price.getText().length() == 0){
                    Toast.makeText(EditProduct.this, "Please Enter Product Price", Toast.LENGTH_SHORT).show();
                    et_price.setAnimation(animShake);
                    et_price.setError("Should not be empty");
                } else if (et_description.getText().length() == 0){
                    Toast.makeText(EditProduct.this, "Please Enter Product Description", Toast.LENGTH_SHORT).show();
                    et_description.setAnimation(animShake);
                    et_description.setError("Should not be empty");
                } else if (sp_quantity.getSelectedItemPosition() == 0) {
                    Toast.makeText(EditProduct.this, "Please enter product quantity", Toast.LENGTH_SHORT).show();
                    //((TextView) sp_quantity.getSelectedView()).setError("Please select quantity");
                    rl_spiner_quantity.setAnimation(animShake);
                } else if (sp_unit.getSelectedItemPosition() == 0){
                    Toast.makeText(EditProduct.this, "Please enter price unit", Toast.LENGTH_SHORT).show();
                    //((TextView) sp_unit.getSelectedView()).setError("Please select unit");
                    rl_spiner_price_unit.setAnimation(animShake);
                } else {
                    editProduct(productId,et_name.getText().toString(),et_price.getText().toString(),String.valueOf(Arrays.quantity.get(sp_quantity.getSelectedItemPosition()).getId()),String.valueOf(Arrays.price_units.get(sp_unit.getSelectedItemPosition()).getId()),et_description.getText().toString());
                }

            }
        });

    }

    public void editProduct(final String productId, final String name, final String price, final String quantity, final String unit, final String description) {


        // Tag used to cancel the request
        String cancel_req_tag = "update-product";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
        byte[] data = bos.toByteArray();*/

        //ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");

        //our custom volley request
        StringRequest strReq = new StringRequest(Request.Method.POST, API.Update_product,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        /*progress_logo.clearAnimation();
                        progress_logo.setVisibility(View.GONE);

                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        Log.e("TAG", "Register Response: " + response.toString());

                        progress_logo.clearAnimation();
                        progress_logo.setVisibility(View.GONE);

                        try {

                            JSONObject jObj = new JSONObject(response);
                            Log.e("TAg", "jObj: " + jObj);
                            boolean error = jObj.getBoolean("error");

                            if (!error) {

                                String message = jObj.getString("msg");
                                Log.e("TAG", "the message from server is message: " + message);

                                showDialogue(message);

                            } else {
                                boolean exist = jObj.getBoolean("exist");

                                if (exist){
                                    String message = jObj.getString("msg");
                                    Toast.makeText(EditProduct.this, message, Toast.LENGTH_SHORT).show();
                                } else {

                                    API.logoutService(EditProduct.this);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error","Error Message: "+ error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("key", API.KEY);
                params.put("name", name);
                params.put("user_id", Prefs.getUserIDFromPref(EditProduct.this));
                params.put("quantity", quantity);
                params.put("price", price);
                params.put("unit", unit);
                params.put("description", description);
                params.put("product_id", productId);

                Log.e("TEST","Name: " + name
                        + "\n price: " + price);

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

        final Dialog dialog = new Dialog(EditProduct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_client_verfication);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(false);
        dialog.show();

        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);

        tv_dialog_title.setText("Update Alert!");
        tv_messasge.setText(message);

        final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditProduct.this, DashboardClient.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }

}