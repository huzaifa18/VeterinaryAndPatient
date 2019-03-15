package betaar.pk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import betaar.pk.Adapters.MyProductsAdapter;
import betaar.pk.Config.API;
import betaar.pk.Models.MyProductsGetterSetter;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class FarmSolutionProducts extends DrawerActivityForClient {

    RecyclerView rv_checkup_requests;
    LinearLayoutManager linearLayoutManager;
    MyProductsAdapter dataAdapter;
    public ArrayList<MyProductsGetterSetter> myProductsArray;
    public ArrayList<String> myProductsImagesArray;

    ImageView progress_logo;
    Animation rotate;

    String category_id = "0";
    String sub_category_id = "0";
    String internal_sub_category_id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_farm_solution_products);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_farm_solution_products, null, false);
        mDrawerLayout.addView(contentView, 0);

        Intent i = getIntent();
        category_id = i.getStringExtra("category_id");
        sub_category_id = i.getStringExtra("sub_category_id");
        internal_sub_category_id = i.getStringExtra("internal_sub_category_id");
        Log.e("Adapter","Category: " + category_id + "\n Sub Category: " + sub_category_id + "\n Internal Sub Category: " + internal_sub_category_id);

        ini();
        Log.e("TAG","User ID: " + Prefs.getUserIDFromPref(FarmSolutionProducts.this));
        //myProducts(Prefs.getUserIDFromPref(OrganizationMyProducts.this));
        getProducts(Prefs.getUserIDFromPref(FarmSolutionProducts.this),category_id,sub_category_id,internal_sub_category_id);

    }

    private void ini() {

        rv_checkup_requests = (RecyclerView) findViewById(R.id.rv_checkup_requests);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL, false);
        rv_checkup_requests.setLayoutManager(linearLayoutManager);
        /*DividerItemDecoration itemDecorator = new DividerItemDecoration(FarmSolutionListing.this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(FarmSolutionListing.this, R.drawable.list_item_divider));
        rv_farm_solution.addItemDecoration(itemDecorator);*/

        myProductsArray = new ArrayList<MyProductsGetterSetter>();
        myProductsImagesArray = new ArrayList<String>();

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        /*for (int i = 0; i<=5; i++) {
            myProductsArray.add(new MyProductsGetterSetter("id "+ i, "name " + i, "category_id "+ i, "sub_category_for_product_id "+ i
                    , "internal_sub_category_for_product_id "+ i, "price "+ i, "unit "+ i, "description "+ i, "categoryName "+ i, "subCategoryName "+ i));
        }*/


        dataAdapter = new MyProductsAdapter(FarmSolutionProducts.this,  myProductsArray, "type");
        rv_checkup_requests.setAdapter(dataAdapter);

    }

    private void getProducts(final String user_id , final String category_id , final String sub_category_id, final String internal_sub_category_id){

        // Tag used to cancel the request
        String cancel_req_tag = "Get Products";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.Get_products, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Login response Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONArray productsArray = jObj.getJSONArray("products");

                        for (int i = 0 ; i < productsArray.length() ; i++){

                            Log.e("Tag","Array Length: " + productsArray.length());

                            String image = null;
                            String image2 = null;
                            String image3 = null;
                            String image4 = null;
                            JSONObject obj = productsArray.getJSONObject(i);

                            String id = obj.getString("id");
                            String name = obj.getString("name");
                            String category_id = obj.getString("category_id");
                            String sub_category_for_product_id = obj.getString("sub_category_for_product_id");
                            String internal_su_category_for_product_id = obj.getString("internal_sub_category_for_product_id");
                            String quantity = obj.getString("quantity");
                            String price = obj.getString("price");
                            JSONObject unitObj = obj.getJSONObject("unit");
                            String unit = unitObj.getString("unit");

                            Log.e("tag","unit: " + unit);
                            String description = obj.getString("description");

                            JSONObject objCategory = obj.getJSONObject("category");
                            String categoryName = objCategory.getString("name");

                            JSONObject objSub_category = obj.getJSONObject("sub_category");
                            String subCategoryName =  objSub_category.getString("name");

                            String internalSubCategoryName = "Null";

                            if (!internal_su_category_for_product_id.equals("0")){

                                Log.e("Service","Not Okay!");
                                JSONObject objInternal_sub_category = obj.getJSONObject("internal_sub_category");
                                internalSubCategoryName = objInternal_sub_category.getString("name");

                            } else {
                                Log.e("Service","Internal Sub Category: " + internal_su_category_for_product_id);
                            }

                            Log.e("Service","Okay!");

                            JSONArray imagesArray = obj.getJSONArray("images");

                            myProductsImagesArray = new ArrayList<String>();

                            for (int j = 0 ; j < imagesArray.length(); j++){

                                JSONObject obj1 = imagesArray.getJSONObject(j);

                                if (j == 0) {
                                    image = obj1.getString("photo_path");
                                }

                                if (j == 1) {
                                    image2 = obj1.getString("photo_path");
                                }

                                if (j == 2) {
                                    image3 = obj1.getString("photo_path");
                                }

                                if (j == 3) {
                                    image4 = obj1.getString("photo_path");
                                }

                                myProductsImagesArray.add(obj1.getString("photo_path"));

                            }

                            JSONObject postedByObj = obj.getJSONObject("posted_by");

                            String posted_by_id = postedByObj.getString("id");

                            String posted_by_name = postedByObj.getString("name");

                            String posted_by_phone = postedByObj.getString("phone");

                            myProductsArray.add(new MyProductsGetterSetter(id, name, category_id, sub_category_for_product_id,internal_su_category_for_product_id,quantity,price,unit,description,categoryName,subCategoryName,internalSubCategoryName,image,myProductsImagesArray, posted_by_id, posted_by_name, posted_by_phone));

                            Log.e("TAG","\nMy Products: " +
                                    "\n Id: " + id +
                                    "\n Name: " + name +
                                    "\n Category Id: " + category_id +
                                    "\n sub_Category Id: " + sub_category_for_product_id +
                                    "\ninternal sub category: " + internal_su_category_for_product_id +
                                    "\n Price: " + price +
                                    "\n Description: " + description +
                                    "\n Category Name: " + categoryName +
                                    "\n Sub Category Name: " + subCategoryName +
                                    "\n Internal Sub Category Name: " + internalSubCategoryName +
                                    "\n Image1: " + image +
                                    "\n Image2: " + image2 +
                                    "\n Image3: " + image3 +
                                    "\n Image4: " + image4 +
                                    "\n Posted By Name: " + posted_by_name +
                                    "\n Posted By Phone: " + posted_by_phone );


                        }

                        /*String user_id = infoObject.getString("user_id");
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
                        } else if (role.equals("client")){
                            startActivity(new Intent(UserLoginActivity.this, DashboardClient.class));
                            finish();
                        } else if (role.equals("organization")){
                            startActivity(new Intent(UserLoginActivity.this, DashBoardOrganization.class));
                            finish();
                        }*/

                        String message = jObj.getString("msg");
                        //Toast.makeText(FarmSolutionProducts.this, message, Toast.LENGTH_SHORT).show();

                    } else {
                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(FarmSolutionProducts.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(FarmSolutionProducts.this);

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
                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


                SharedPreferences sharedPreferences  = getSharedPreferences("udid", 0);
                String userUdid = sharedPreferences.getString("udid", "null");

                // String mPhone = phone.substring(1);

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", user_id);
                params.put("category_id", category_id);
                params.put("sub_category_id", sub_category_id);
                params.put("internal_sub_category_id", internal_sub_category_id);

                Log.e("getProducts","UserID: " + user_id + "\nCategoryId: " + category_id + "\nSubCategoryId: " + sub_category_id +
                "\nInternalSubCategoryId: " + internal_sub_category_id);

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
