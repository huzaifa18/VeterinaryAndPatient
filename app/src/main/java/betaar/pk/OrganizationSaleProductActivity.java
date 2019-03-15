package betaar.pk;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import betaar.pk.Adapters.SpinnerListingAdapter;
import betaar.pk.Config.API;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.VolleyMultipartRequest;
import betaar.pk.utils.Permissions;

public class OrganizationSaleProductActivity extends AppCompatActivity {

    RelativeLayout rl_spiner_product_type, rl_spiner_product_category, rl_spiner_product_sub_category, rl_spinner_price_unit;
    Spinner sp_select_category, sp_select_sub_category, sp_select_internal_sub_category, sp_select_price_unit;
    RelativeLayout rl_image_1, rl_image_2, rl_image_3, rl_image_4;
    ImageView tv_image_1, tv_image_2 , tv_image_3, tv_image_4;
    TextView tx_image_1, tx_image_2, tx_image_3, tx_image_4;
    RelativeLayout rl_bt_post;
    EditText et_product_name, et_product_price;
    EditText et_product_description;

    String imageUri1 = "Image 1";
    String imageUri2 = "Image 2";
    String imageUri3 = "Image 3";
    String imageUri4 = "Image 4";

    TextView tvTitle;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Uri imageUri = null;
    int INDICATOR = -1;

    ImageView progress_logo;
    Animation rotate;

    String categoryId;
    String subCategoryId;
    String internalSubCategoryId;

    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_sale_product);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //getSupportActionBar().setTitle(R.string.title_sale_products);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(OrganizationSaleProductActivity.this ,R.color.colorBlue)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        Permissions.requestAppPermissions(OrganizationSaleProductActivity.this);

        getImage1();
        getImage2();
        getImage3();
        getImage4();

        postButtonClickHandler();
        onProdcutTypeSelectListner();
        spCategorySelectorHandler();
        spInternalCategorySelectorHandler();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void init(){

        rl_spiner_product_type = (RelativeLayout) findViewById(R.id.rl_spiner_product_type);
        rl_spiner_product_category = (RelativeLayout) findViewById(R.id.rl_spiner_product_category);
        rl_spiner_product_sub_category = (RelativeLayout) findViewById(R.id.rl_spiner_product_sub_category);
        rl_spinner_price_unit = (RelativeLayout) findViewById(R.id.rl_spiner_price_unit);


        sp_select_category = (Spinner) findViewById(R.id.sp_select_product_type);
        SpinnerListingAdapter category = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_types);
        sp_select_category.setAdapter(category);
        category.notifyDataSetChanged();

        sp_select_sub_category = (Spinner) findViewById(R.id.sp_select_product_category);
        /*ArrayAdapter adapterProductCategory = ArrayAdapter.createFromResource(OrganizationSaleProductActivity.this,
                R.array.product_categories_for_dairy, R.layout.spinner_item);
        adapterProductCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_select_sub_category.setAdapter(adapterProductCategory);
*/

        sp_select_internal_sub_category = (Spinner) findViewById(R.id.sp_select_product_sub_category);
        /*ArrayAdapter adapterProductSubCategory = ArrayAdapter.createFromResource(OrganizationSaleProductActivity.this,
                R.array.product_sub_categories_for_dairy_accesories, R.layout.spinner_item);
        adapterProductSubCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_select_internal_sub_category.setAdapter(adapterProductSubCategory);*/

        sp_select_price_unit = (Spinner) findViewById(R.id.sp_select_price_unit);
        SpinnerListingAdapter priceUnit = new SpinnerListingAdapter(getApplicationContext(), Arrays.price_units);
        sp_select_price_unit.setAdapter(priceUnit);
        priceUnit.notifyDataSetChanged();


        rl_image_1 = (RelativeLayout) findViewById(R.id.rl_image_1);
        rl_image_2 = (RelativeLayout) findViewById(R.id.rl_image_2);
        rl_image_3 = (RelativeLayout) findViewById(R.id.rl_image_3);
        rl_image_4 = (RelativeLayout) findViewById(R.id.rl_image_4);

        tv_image_1 = (ImageView) findViewById(R.id.tv_image_1);
        tv_image_2 = (ImageView) findViewById(R.id.tv_image_2);
        tv_image_3 = (ImageView) findViewById(R.id.tv_image_3);
        tv_image_4 = (ImageView) findViewById(R.id.tv_image_4);
        tx_image_1 = (TextView) findViewById(R.id.tx_image_1);
        tx_image_2 = (TextView) findViewById(R.id.tx_image_2);
        tx_image_3 = (TextView) findViewById(R.id.tx_image_3);
        tx_image_4 = (TextView) findViewById(R.id.tx_image_4);

        rl_bt_post = (RelativeLayout) findViewById(R.id.rl_bt_post);
        et_product_name = (EditText) findViewById(R.id.et_product_name);
        et_product_price = (EditText) findViewById(R.id.et_product_price);
        et_product_description = (EditText) findViewById(R.id.et_product_description);


        rl_spiner_product_category.setVisibility(View.GONE);
        rl_spiner_product_sub_category.setVisibility(View.GONE);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_sale_products);

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

    }

    private void getImage1(){
        rl_image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gettingImagesDialog(1);


            }
        });
    }

    private void getImage2(){
        rl_image_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gettingImagesDialog(2);

            }
        });
    }
    private void getImage3(){
        rl_image_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gettingImagesDialog(3);
            }
        });
    }
    private void getImage4(){
        rl_image_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gettingImagesDialog(4);

            }
        });
    }


    private void gettingImagesDialog(final int imageIndicator) {

        boolean result =  Permissions.requestAppPermissions(OrganizationSaleProductActivity.this);
        if (result) {

            final Dialog dialog = new Dialog(OrganizationSaleProductActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custome_dialog_for_getting_photo);

            Button bt_dialog_from_gallary = (Button) dialog.findViewById(R.id.bt_dialog_from_gallary);
            Button bt_dialog_from_camera = (Button) dialog.findViewById(R.id.bt_dialog_from_camera);

            bt_dialog_from_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    cameraIntent(imageIndicator); //to detect that the view is from top document button view

                }
            });

            bt_dialog_from_gallary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    galleryIntent(imageIndicator);
                }
            });


            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
            dialog.show();


        }
    }

    private void cameraIntent(int imageIndicator)
    {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        String fileName = getFileName(imageUri);
        Log.e("TAG", "the upload file name is: " + fileName);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);

        INDICATOR = imageIndicator;
    }

    private void galleryIntent(int imageIndicator)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
        INDICATOR = imageIndicator;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("TAg", "The Request code is: " + requestCode);

        //  if (resultCode == Activity.RESULT_OK) {
        if (requestCode == SELECT_FILE) {

            if (data!=null) {
                try {
                imageUri = data.getData();
                String fileName = getFileName(imageUri);
                Log.e("TAG", "the upload file name is: " + fileName);

                if(INDICATOR == 1){

                    Picasso.with(this).load(imageUri).into(tv_image_1);
                    imageUri1 = imageUri.toString();

                        //tv_image_1.bringToFront();
                        tx_image_1.setText(imageUri.toString());
                        tx_image_1.setVisibility(View.GONE);

                }
                if(INDICATOR == 2){

                    Picasso.with(this).load(imageUri).into(tv_image_2);
                    imageUri2 = imageUri.toString();

                    //tv_image_2.setImageURI(imageUri);
                    //tv_image_2.bringToFront();
                    tx_image_2.setText(imageUri.toString());
                    tx_image_2.setVisibility(View.GONE);
                }
                if(INDICATOR == 3){

                    Picasso.with(this).load(imageUri).into(tv_image_3);
                    imageUri3 = imageUri.toString();

                    //tv_image_3.setImageURI(imageUri);
                    //tv_image_3.bringToFront();
                    tx_image_3.setText(imageUri.toString());
                    tx_image_3.setVisibility(View.GONE);
                }
                if(INDICATOR == 4){

                    Picasso.with(this).load(imageUri).into(tv_image_4);
                    imageUri4 = imageUri.toString();

                    //tv_image_4.setImageURI(imageUri);
                    //tv_image_4.bringToFront();
                    tx_image_4.setText(imageUri.toString());
                    tx_image_4.setVisibility(View.GONE);
                }
                }catch (OutOfMemoryError e){
                    e.printStackTrace();
                    System.gc();
                }

            }
        }
        else if (requestCode == REQUEST_CAMERA) {
            if (data!=null || imageUri!=null) {
                try{
                String fileName = getFileName(imageUri);
                if(INDICATOR == 1){

                    Picasso.with(this).load(imageUri).into(tv_image_1);
                    imageUri1 = imageUri.toString();

                    //tv_image_1.bringToFront();
                    tx_image_1.setText(imageUri.toString());
                    tx_image_1.setVisibility(View.GONE);
                }
                if(INDICATOR == 2){

                    Picasso.with(this).load(imageUri).into(tv_image_2);
                    imageUri2 = imageUri.toString();

                    //tv_image_2.setImageURI(imageUri);
                    //tv_image_2.bringToFront();
                    tx_image_2.setText(imageUri.toString());
                    tx_image_2.setVisibility(View.GONE);
                }
                if(INDICATOR == 3){

                    Picasso.with(this).load(imageUri).into(tv_image_3);
                    imageUri3 = imageUri.toString();

                    //tv_image_3.setImageURI(imageUri);
                    //tv_image_3.bringToFront();
                    tx_image_3.setText(imageUri.toString());
                    tx_image_3.setVisibility(View.GONE);
                }
                if(INDICATOR == 4){

                    Picasso.with(this).load(imageUri).into(tv_image_4);
                    imageUri4 = imageUri.toString();

                    //tv_image_4.setImageURI(imageUri);
                    //tv_image_4.bringToFront();
                    tx_image_4.setText(imageUri.toString());
                    tx_image_4.setVisibility(View.GONE);
                }
                }catch (OutOfMemoryError e){
                    e.printStackTrace();
                    System.gc();
                }
            }
        }

    }
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void postButtonClickHandler(){

        rl_bt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String spProductType = sp_select_category.getSelectedItem().toString();
                String spProdcutCategory = sp_select_sub_category.getSelectedItem().toString();
                String spProdcutSubCategory = sp_select_internal_sub_category.getSelectedItem().toString();


                int spCategoryPosition = sp_select_category.getSelectedItemPosition();
                int spSubCategoryPosition = sp_select_sub_category.getSelectedItemPosition();
                int SpInternalSubCategoryPosition  = sp_select_internal_sub_category.getSelectedItemPosition();
                int SpPriceUnitPosition  = sp_select_price_unit.getSelectedItemPosition();
                String productName = et_product_name.getText().toString();
                String productPrice = et_product_price.getText().toString();
                String productDescription = et_product_description.getText().toString();

                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);


                if (spCategoryPosition == 0){
                    Toast.makeText(OrganizationSaleProductActivity.this, "Please Select Product Type", Toast.LENGTH_SHORT).show();
                    //((TextView) sp_select_category.getSelectedView()).setError("Please Select Prodcut Type");
                    rl_spiner_product_type.setAnimation(animShake);
                }
                else if (spSubCategoryPosition == 0){
                    Toast.makeText(OrganizationSaleProductActivity.this, "Please Select Product Category", Toast.LENGTH_SHORT).show();
                    //((TextView) sp_select_sub_category.getSelectedView()).setError("Please Select Category");
                    rl_spiner_product_category.setAnimation(animShake);
                } else if (SpInternalSubCategoryPosition == 0 && spCategoryPosition == 1){
                    if (rl_spiner_product_sub_category.getVisibility() == View.GONE){

                    } else {
                        Toast.makeText(OrganizationSaleProductActivity.this, "Please Select Product Sub-Category", Toast.LENGTH_SHORT).show();
                        //((TextView) sp_select_internal_sub_category.getSelectedView()).setError("Please Select Sub Category");
                        rl_spiner_product_sub_category.setAnimation(animShake);
                    }
                }else if (SpPriceUnitPosition == 0){
                    Toast.makeText(OrganizationSaleProductActivity.this, "Please Select Price Unit", Toast.LENGTH_SHORT).show();
                    //((TextView) sp_select_price_unit.getSelectedView()).setError("Please Select Price Unit");
                    rl_spinner_price_unit.setAnimation(animShake);
                } else if (productName.length()==0){
                    Toast.makeText(OrganizationSaleProductActivity.this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
                    et_product_name.setAnimation(animShake);
                    et_product_name.setError("should not empty");
                } else if (productPrice.length()==0){
                    Toast.makeText(OrganizationSaleProductActivity.this, "Please Enter Product Price", Toast.LENGTH_SHORT).show();
                    et_product_price.setAnimation(animShake);
                    et_product_price.setError("should not empty");
                } else if (imageUri1.equals("Image 1")){
                    Toast.makeText(OrganizationSaleProductActivity.this, "Please Upload Prodcut Image", Toast.LENGTH_SHORT).show();
                    tx_image_1.setError("Upload Image");
                    rl_image_1.setAnimation(animShake);
                } else if (productDescription.length() == 0){
                    Toast.makeText(OrganizationSaleProductActivity.this, "Please Enter Short Description about product", Toast.LENGTH_SHORT).show();
                    et_product_description.setAnimation(animShake);
                    et_product_description.setError("should not empty");
                } else {

                    if (categoryId == null) {
                        categoryId = "0";
                    }

                    if (subCategoryId == null) {
                        subCategoryId = "0";
                    }

                    if (internalSubCategoryId == null) {
                        internalSubCategoryId = "0";
                    }


                    Log.e("TAG", "to send data on server selected Category: " + categoryId);
                    Log.e("TAG", "to send data on server selected Sub Category: " + subCategoryId);
                    Log.e("TAG", "to send data on server selected Internal Sub Cateogry: " + internalSubCategoryId);
                    Log.e("TAG", "to send data on server selected product name: " + productName);
                    Log.e("TAG", "to send data on server selected Product price: " + productPrice);
                    Log.e("TAG", "to send data on server selected Product image 1: " + imageUri1);
                    Log.e("TAG", "to send data on server selected Product image 2: " + imageUri2);
                    Log.e("TAG", "to send data on server selected Product image 3: " + imageUri3);
                    Log.e("TAG", "to send data on server selected Product image 4: " + imageUri4);
                    Log.e("TAG", "to send data on server selected Product description: " + productDescription);

                    try {
                        // bm = BitmapFactory.decodeResource(getResources(),
                        // R.drawable.forest);
                        //bm = BitmapFactory.decodeFile(imageUri1);
                       // BitmapFactory.Options options = new BitmapFactory.Options();
                        //options.inJustDecodeBounds = true;

                        Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imageUri1));
                        Bitmap bitmap2 = null;
                        Bitmap bitmap3 = null;
                        Bitmap bitmap4 = null;
                        if (tx_image_2.getText().toString().equals("Image 2")) {

                        } else {
                            bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imageUri2));
                        }

                        if (tx_image_3.getText().toString().equals("Image 3")) {

                        } else {
                            bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imageUri3));
                        }

                        if (tx_image_4.getText().toString().equals("Image 4")) {

                        } else {
                            bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imageUri4));
                        }

                        String sp_price = String.valueOf(Arrays.price_units.get(SpPriceUnitPosition).getId());

                        Log.e("TAG","User ID: " + Prefs.getUserIDFromPref(OrganizationSaleProductActivity.this));
                        addProduct(productName , categoryId , subCategoryId , internalSubCategoryId , Prefs.getUserIDFromPref(OrganizationSaleProductActivity.this) , productPrice, sp_price , productDescription, bitmap1, bitmap2, bitmap3, bitmap4);
                    } catch (Exception e) {
                        Log.e(e.getClass().getName(), e.getMessage());
                    }

                }
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void addProduct(final String productName, final String category, final String subCategory, final String internalSubCategory, final String user_id, final String price, final String sp_price, final String description, final Bitmap image1, final Bitmap image2, final Bitmap image3, final Bitmap image4) {


        // Tag used to cancel the request
        String cancel_req_tag = "post-product";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
        byte[] data = bos.toByteArray();*/

        //ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, API.Post_product,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

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

                            JSONObject jObj = new JSONObject(new String(response.data));
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
                                    Toast.makeText(OrganizationSaleProductActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else {

                                    API.logoutService(OrganizationSaleProductActivity.this);

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

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("key", API.KEY);
                params.put("name", productName);
                params.put("category", category);
                params.put("subcategory", subCategory);
                params.put("internalsubcategory",internalSubCategory);
                params.put("user_id", user_id);
                params.put("price", price);
                params.put("unit", sp_price);
                params.put("description", description);
                params.put("photo_type", "file");

                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();

                //Log.e("Tag","Image: "+ new DataPart(imagename + ".png",getFileDataFromDrawable(image1)));

                Log.e("TAG","Image 1: param " + imageUri1);
                params.put("photo1", new DataPart(imagename + ".png",getFileDataFromDrawable(image1)));

                if (imageUri2.equals("Image 2")) {
                    Log.e("TAG","Image 2: " + imageUri2);
                } else {
                    Log.e("TAG","Image 2: param " + imageUri2);
                    params.put("photo2", new DataPart(imagename + ".png", getFileDataFromDrawable(image2)));
                }

                if (imageUri3.equals("Image 3")) {
                    Log.e("TAG","Image 3: " + imageUri3);
                } else {
                    Log.e("TAG","Image 3: param " + imageUri3);
                    params.put("photo3", new DataPart(imagename + ".png", getFileDataFromDrawable(image3)));
                }

                if (imageUri4.equals("Image 4")) {
                    Log.e("TAG","Image 4: " + imageUri4);
                } else {
                    Log.e("TAG","Image 4: param " + imageUri4);
                    params.put("photo4", new DataPart(imagename + ".png", getFileDataFromDrawable(image4)));
                }

                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    private void onProdcutTypeSelectListner() {

        sp_select_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                categoryId = String.valueOf(Arrays.product_types.get(i).getId());

                //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_types.get(i).getId(), Toast.LENGTH_LONG).show();

                if (i == 0){
                    rl_spiner_product_category.setVisibility(View.GONE);
                    rl_spiner_product_sub_category.setVisibility(View.GONE);
                }
                if (i==1){

                    sp_select_sub_category = (Spinner) findViewById(R.id.sp_select_product_category);
                    SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_categories_for_dairy_organization);
                    sp_select_sub_category.setAdapter(subCategory);
                    subCategory.notifyDataSetChanged();
                    rl_spiner_product_category.setVisibility(View.VISIBLE);
                    rl_spiner_product_sub_category.setVisibility(View.GONE);

                }
                if (i==2){

                    /*sp_select_sub_category  = (Spinner) findViewById(R.id.sp_select_sub_category);
                    ArrayAdapter adapterProductCategory = ArrayAdapter.createFromResource(OrganizationSaleProductActivity.this,
                            R.array.product_categories_for_pets_organization, R.layout.spinner_item);
                    adapterProductCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    sp_select_sub_category.setAdapter(adapterProductCategory);
                    rl_spiner_product_category.setVisibility(View.VISIBLE);
                    rl_spiner_product_sub_category.setVisibility(View.GONE);*/

                    sp_select_sub_category = (Spinner) findViewById(R.id.sp_select_product_category);
                    SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_categories_for_pets_organization);
                    sp_select_sub_category.setAdapter(subCategory);
                    subCategory.notifyDataSetChanged();
                    rl_spiner_product_category.setVisibility(View.VISIBLE);
                    rl_spiner_product_sub_category.setVisibility(View.GONE);

                }

                if (i==3){

                    sp_select_sub_category = (Spinner) findViewById(R.id.sp_select_product_category);
                    SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_categories_for_equine_organization);
                    sp_select_sub_category.setAdapter(subCategory);
                    subCategory.notifyDataSetChanged();
                    rl_spiner_product_category.setVisibility(View.VISIBLE);
                    rl_spiner_product_sub_category.setVisibility(View.GONE);

                }

                if (i==4){

                    sp_select_sub_category = (Spinner) findViewById(R.id.sp_select_product_category);
                    SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_categories_for_bird_organization);
                    sp_select_sub_category.setAdapter(subCategory);
                    subCategory.notifyDataSetChanged();
                    rl_spiner_product_category.setVisibility(View.VISIBLE);
                    rl_spiner_product_sub_category.setVisibility(View.GONE);

                }

                if (i==5){

                    sp_select_sub_category = (Spinner) findViewById(R.id.sp_select_product_category);
                    SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_categories_for_wildLife_organization);
                    sp_select_sub_category.setAdapter(subCategory);
                    subCategory.notifyDataSetChanged();
                    rl_spiner_product_category.setVisibility(View.VISIBLE);
                    rl_spiner_product_sub_category.setVisibility(View.GONE);

                }

                Log.e("TAG","Category: " + categoryId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void spCategorySelectorHandler(){
        sp_select_sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (sp_select_category.getSelectedItemId() == 1){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_dairy_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                    subCategoryId = String.valueOf(Arrays.product_categories_for_dairy_organization.get(i).getId());

                if (i == 0){

                } else {
                    rl_spiner_product_sub_category.setVisibility(View.VISIBLE);
                }

                if (i == 1){

                    SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_accesories);
                    sp_select_internal_sub_category.setAdapter(subCategory);
                    subCategory.notifyDataSetChanged();

                }
                if (i == 2){

                    SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_feed);
                    sp_select_internal_sub_category.setAdapter(subCategory);
                    subCategory.notifyDataSetChanged();

                }
                if (i == 3){

                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_dairy_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                    SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_animals);
                    sp_select_internal_sub_category.setAdapter(subCategory);
                    subCategory.notifyDataSetChanged();

                }
                if (i == 4){

                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_dairy_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                    SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_milking_parlour);
                    sp_select_internal_sub_category.setAdapter(subCategory);
                    subCategory.notifyDataSetChanged();

                }
                if (i == 5){
                    /*rl_spiner_product_sub_category.setVisibility(View.VISIBLE);
                    ArrayAdapter adapterProductSubCategory = ArrayAdapter.createFromResource(OrganizationSaleProductActivity.this,
                            R.array.product_sub_categories_for_dairy_medicine_products, R.layout.spinner_item);
                    adapterProductSubCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    sp_select_internal_sub_category.setAdapter(adapterProductSubCategory);*/
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_dairy_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                    SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_medicine_products);
                    sp_select_internal_sub_category.setAdapter(subCategory);
                    subCategory.notifyDataSetChanged();

                }
                if (i == 6){

                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_dairy_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                    SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_sheed_construction);
                    sp_select_internal_sub_category.setAdapter(subCategory);
                    subCategory.notifyDataSetChanged();

                }
                if (i == 7){

                        //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_dairy_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                        SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_labs);
                        sp_select_internal_sub_category.setAdapter(subCategory);
                        subCategory.notifyDataSetChanged();

                    }
                    if (i == 8){

                        //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_dairy_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                        SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_labour);
                        sp_select_internal_sub_category.setAdapter(subCategory);
                        subCategory.notifyDataSetChanged();

                    }
                    if (i == 9){

                        //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_dairy_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                        SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_land_on_rent);
                        sp_select_internal_sub_category.setAdapter(subCategory);
                        subCategory.notifyDataSetChanged();

                    }
                    if (i == 10){

                        //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_dairy_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                        SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_sprays);
                        sp_select_internal_sub_category.setAdapter(subCategory);
                        subCategory.notifyDataSetChanged();

                    }
                    if (i == 11){

                        SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_milk_sale_pur);
                        sp_select_internal_sub_category.setAdapter(subCategory);
                        subCategory.notifyDataSetChanged();

                    }
                    if (i == 12){

                        //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_dairy_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                        SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.product_sub_categories_for_dairy_crops_seed);
                        sp_select_internal_sub_category.setAdapter(subCategory);
                        subCategory.notifyDataSetChanged();

                    }
                }
                if (sp_select_category.getSelectedItemPosition() == 2){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_pets_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                    subCategoryId = String.valueOf(Arrays.product_categories_for_pets_organization.get(i).getId());
                }
                if (sp_select_category.getSelectedItemPosition() == 3){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_equine_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                    subCategoryId = String.valueOf(Arrays.product_categories_for_equine_organization.get(i).getId());
                }
                if (sp_select_category.getSelectedItemPosition() == 4){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_bird_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                    subCategoryId = String.valueOf(Arrays.product_categories_for_bird_organization.get(i).getId());
                }
                if (sp_select_category.getSelectedItemPosition() == 5){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_categories_for_wildLife_organization.get(i).getId(), Toast.LENGTH_LONG).show();
                    subCategoryId = String.valueOf(Arrays.product_categories_for_wildLife_organization.get(i).getId());
                }

                Log.e("TAG","Sub Category: " + subCategoryId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void spInternalCategorySelectorHandler(){

        sp_select_internal_sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (sp_select_sub_category.getSelectedItemPosition() == 1){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_accesories.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_accesories.get(i).getId());
                    //internalSubCategoryId = Arrays.getID(Arrays.product_sub_categories_for_dairy_accesories,Arrays.product_sub_categories_for_dairy_accesories.get(i).getName());
                }if (sp_select_sub_category.getSelectedItemPosition() == 2){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_feed.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_feed.get(i).getId());
                }if (sp_select_sub_category.getSelectedItemPosition() == 3){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_animals.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_animals.get(i).getId());
                }if (sp_select_sub_category.getSelectedItemPosition() == 4){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_milking_parlour.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_milking_parlour.get(i).getId());
                }if (sp_select_sub_category.getSelectedItemPosition() == 5){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_medicine_products.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_medicine_products.get(i).getId());
                }if (sp_select_sub_category.getSelectedItemPosition() == 6){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_sheed_construction.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_sheed_construction.get(i).getId());
                }if (sp_select_sub_category.getSelectedItemPosition() == 7){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_labs.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_labs.get(i).getId());
                }if (sp_select_sub_category.getSelectedItemPosition() == 8){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_labour.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_labour.get(i).getId());
                }if (sp_select_sub_category.getSelectedItemPosition() == 9){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_land_on_rent.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_land_on_rent.get(i).getId());
                }if (sp_select_sub_category.getSelectedItemPosition() == 10){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_sprays.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_sprays.get(i).getId());
                }if (sp_select_sub_category.getSelectedItemPosition() == 11){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_milk_sale_pur.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_milk_sale_pur.get(i).getId());
                }if (sp_select_sub_category.getSelectedItemPosition() == 12){
                    //Toast.makeText(OrganizationSaleProductActivity.this, "Id: " + Arrays.product_sub_categories_for_dairy_crops_seed.get(i).getId(), Toast.LENGTH_LONG).show();
                    internalSubCategoryId = String.valueOf(Arrays.product_sub_categories_for_dairy_crops_seed.get(i).getId());
                }

                Log.e("TAG","Internal Sub Category: " + internalSubCategoryId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

            });
    }

    private void showDialogue(String message){

        final Dialog dialog = new Dialog(OrganizationSaleProductActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_client_verfication);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(false);
        dialog.show();

        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);

        tv_dialog_title.setText("Sale Product");
        tv_messasge.setText(message);

        final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrganizationSaleProductActivity.this, DashBoardOrganization.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


    }

}
