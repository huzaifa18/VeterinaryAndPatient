package betaar.pk;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import betaar.pk.Adapters.ImageListingAdapter;
import betaar.pk.Adapters.MyProductsAdapter;

public class ProductDetails extends AppCompatActivity {

    private String name = "";
    private String category = "";
    private String sub_category_for_product = "";
    private String internal_sub_category_for_product = "";
    private String quantity = "";
    private String price = "";
    private String unit = "";
    private String description = "";
    private String postedByName = "";
    private String postedByPhone = "";
    private String postedById = "";
    private String type = "";
    RecyclerView rv_images;
    LinearLayoutManager linearLayoutManager;
    ImageListingAdapter dataAdapter;
    public ArrayList<String> myProductsImagesArray;
    ImageView iv_bigImage;
    /*private String image1 = "";
    private String image2 = "";
    private String image3 = "";
    private String image4 = "";*/

    TextView tv_name,tv_category,tv_sub_category,tv_internal_sub_category,tv_quantity,tv_price,tv_unit,tv_description,tv_posted_by,tv_edit;

    RelativeLayout rl_bt_post,rl_bt_contact;

    LinearLayout ll_contact;

    //ImageView iv_image1,iv_image2,iv_image3,iv_image4;

    //RelativeLayout rl_image1,rl_image2,rl_image3,rl_image4;

    LinearLayout ll_image_3_4,ll_internal_sub_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        getSupportActionBar().setTitle("Product Detail");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ProductDetails.this ,R.color.colorBlue)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = getIntent().getStringExtra("name");
        category = getIntent().getStringExtra("category");
        sub_category_for_product = getIntent().getStringExtra("sub_category");
        internal_sub_category_for_product = getIntent().getStringExtra("internal_sub_category");
        quantity = getIntent().getStringExtra("quantity");
        price = getIntent().getStringExtra("price");
        unit = getIntent().getStringExtra("unit");
        description = getIntent().getStringExtra("description");
        myProductsImagesArray = getIntent().getStringArrayListExtra("images");
        postedById = getIntent().getStringExtra("postedById");
        postedByName = getIntent().getStringExtra("postedByName");
        postedByPhone = getIntent().getStringExtra("postedByPhone");
        type = getIntent().getStringExtra("type");

        Log.e("TEST", "productID:" + postedById);

        //image1 = getIntent().getStringExtra("image1");
        //Log.e("Images","Image 1: " + image1);
        //image2 = getIntent().getStringExtra("image2");
        //Log.e("Images","Image 2: " + image2);
        //image3 = getIntent().getStringExtra("image3");
        //Log.e("Images","Image 3: " + image3);
        //image4 = getIntent().getStringExtra("image4");
        //Log.e("Images","Image 4: " + image4);

        initialise();

    }

    private void initialise() {

        tv_name = (TextView) findViewById(R.id.tv_product_name);
        tv_category = (TextView) findViewById(R.id.tv_category);
        tv_sub_category = (TextView) findViewById(R.id.tv_sub_category);
        tv_internal_sub_category = (TextView) findViewById(R.id.tv_internal_sub_category);
        tv_quantity = (TextView) findViewById(R.id.tv_quantity);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_unit = (TextView) findViewById(R.id.tv_unit);
        tv_description = (TextView) findViewById(R.id.et_product_description);
        rv_images = (RecyclerView) findViewById(R.id.rv_images);
        iv_bigImage = (ImageView) findViewById(R.id.iv_bigimage);
        rl_bt_post = (RelativeLayout) findViewById(R.id.rl_bt_post);
        tv_posted_by = (TextView) findViewById(R.id.tv_posted_by_name);
        rl_bt_contact = (RelativeLayout) findViewById(R.id.rl_bt_contact);

        ll_contact = (LinearLayout) findViewById(R.id.ll_contact);
        tv_edit = (TextView) findViewById(R.id.tv_edit);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.HORIZONTAL, false);
        rv_images.setLayoutManager(linearLayoutManager);
        //myProductsImagesArray = new ArrayList<String>();
        dataAdapter = new ImageListingAdapter(ProductDetails.this,  myProductsImagesArray);
        rv_images.setAdapter(dataAdapter);

        /*iv_image1 = (ImageView) findViewById(R.id.tv_image_1);
        iv_image2 = (ImageView) findViewById(R.id.tv_image_2);
        iv_image3 = (ImageView) findViewById(R.id.tv_image_3);
        iv_image4 = (ImageView) findViewById(R.id.tv_image_4);

        rl_image1 = (RelativeLayout) findViewById(R.id.rl_image_1);
        rl_image2 = (RelativeLayout) findViewById(R.id.rl_image_2);
        rl_image3 = (RelativeLayout) findViewById(R.id.rl_image_3);
        rl_image4 = (RelativeLayout) findViewById(R.id.rl_image_4);*/

        //ll_image_3_4 = (LinearLayout) findViewById(R.id.ll_image_3_4);
        ll_internal_sub_category = (LinearLayout) findViewById(R.id.ll_internal_sub_category);

        if (type.equals("Client")){
            ll_contact.setVisibility(View.GONE);
            tv_edit.setVisibility(View.VISIBLE);
        } else {
            ll_contact.setVisibility(View.VISIBLE);
            tv_edit.setVisibility(View.GONE);
        }

        setData();
        postButtonListener();
        contactButtonListener();

    }

    private void postButtonListener() {

        rl_bt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialogue();

            }
        });

    }

    private void contactButtonListener() {

        rl_bt_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //API.showDialogue(ProductDetails.this);
                /*Intent i = new Intent(ProductDetails.this,SignInActivity.class);
                startActivity(i);*/

                if (type.equals("Client")){
                    Intent intent = new Intent(ProductDetails.this, EditProduct.class);
                    intent.putExtra("name",name);
                    intent.putExtra("price",price);
                    intent.putExtra("quantity",quantity);
                    intent.putExtra("description",description);
                    intent.putExtra("unit",unit);
                    intent.putExtra("productId",postedById);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + postedByPhone.trim()));
                    startActivity(intent);
                }

            }
        });

    }

    private void setData() {

        tv_name.setText(name);
        tv_posted_by.setText(postedByName);
        tv_category.setText(category);
        tv_sub_category.setText(sub_category_for_product);

        if (internal_sub_category_for_product.equals("Null")){
            ll_internal_sub_category.setVisibility(View.GONE);
        }

        tv_internal_sub_category.setText(internal_sub_category_for_product);
        tv_quantity.setText(quantity);
        tv_price.setText(price);
        tv_unit.setText(unit);
        tv_description.setText(description);

        dataAdapter = new ImageListingAdapter(ProductDetails.this,  myProductsImagesArray);
        rv_images.setAdapter(dataAdapter);

        rv_images.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.e("TAG","ID: " + rv.getId());
                rv.getId();
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        /*Picasso.with(ProductDetails.this).load(image1).placeholder(R.drawable.logi_icon).into(iv_image1);
        Picasso.with(ProductDetails.this).load(image2).placeholder(R.drawable.logi_icon).into(iv_image2);
        Picasso.with(ProductDetails.this).load(image3).placeholder(R.drawable.logi_icon).into(iv_image3);
        Picasso.with(ProductDetails.this).load(image4).placeholder(R.drawable.logi_icon).into(iv_image4);*/

        //Log.e("Product Details","Image 2:" + image2);

        /*if (image1.equals("") || image1.isEmpty()){

        }*/

        /*if (image2.equals("") || image2.isEmpty()){

            rl_image2.setVisibility(View.GONE);

        }

        if (image3.equals("") || image3.isEmpty()){

            rl_image3.setVisibility(View.GONE);

        }

        if (image4.equals("") || image4.isEmpty()){

            rl_image4.setVisibility(View.GONE);

        }*/

        /*if (image3.equals(null) && image4.equals(null)){

            ll_image_3_4.setVisibility(View.GONE);

        }*/

    }

    private void showDialogue(){

        final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        final Dialog dialog = new Dialog(ProductDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogue_layout_quantity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        final EditText et_quantity = (EditText) dialog.findViewById(R.id.et_quantity);
        ImageView close = (ImageView) dialog.findViewById(R.id.close);

        final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_quantity.length() == 0){
                    et_quantity.setError("Should not be empty");
                    et_quantity.setAnimation(animShake);
                } else if (Integer.parseInt(quantity) < Integer.parseInt(String.valueOf(et_quantity.getText()))){
                    et_quantity.setError("Max Products Available: " + quantity);
                    et_quantity.setAnimation(animShake);
                } else {
                    Intent i = new Intent(ProductDetails.this, OrderNow.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

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

}
