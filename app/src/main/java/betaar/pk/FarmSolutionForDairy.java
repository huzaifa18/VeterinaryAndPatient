package betaar.pk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FarmSolutionForDairy extends DrawerActivityForClient {

    TextView tv_title;
    RelativeLayout rl_accessories_dairy,rl_feed_dairy,rl_animals_dairy,rl_milking_machine_dairy,
            rl_medicine_products_dairy,rl_shed_construction_dairy,rl_labs_dairy,rl_labour_dairy,
            rl_land_on_rent_dairy,rl_sprays_dairy,rl_milk_dairy,rl_dairy_crops_seed_dairy;
    TextView tv_accessories_dairy,tv_feed_dairy,tv_animals_dairy,tv_milking_machine_dairy,
            tv_medicine_products_dairy,tv_shed_construction_dairy,tv_labs_dairy,tv_labour_dairy,
            tv_land_on_rent_dairy,tv_sprays_dairy,tv_milk_dairy,tv_dairy_crops_seed_dairy;
    //Button bt_accessories;
    String category_id = "0";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_farm_solution_for_dairy);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.custome_form_solution_screen, null, false);
        mDrawerLayout.addView(contentView, 0);


        init();
        //
        farmSolutionButtonClickHandler();
    }

    private void init(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_accessories_dairy = (RelativeLayout) findViewById(R.id.rl_accessories_dairy);
        rl_feed_dairy = (RelativeLayout) findViewById(R.id.rl_feed_dairy);
        rl_animals_dairy = (RelativeLayout) findViewById(R.id.rl_animals_dairy);
        rl_milking_machine_dairy = (RelativeLayout) findViewById(R.id.rl_milking_machine_dairy);
        rl_medicine_products_dairy = (RelativeLayout) findViewById(R.id.rl_medicine_products_dairy);
        rl_shed_construction_dairy = (RelativeLayout) findViewById(R.id.rl_shed_construction_dairy);
        rl_labs_dairy = (RelativeLayout) findViewById(R.id.rl_labs_dairy);
        rl_labour_dairy = (RelativeLayout) findViewById(R.id.rl_labour_dairy);
        rl_land_on_rent_dairy = (RelativeLayout) findViewById(R.id.rl_land_on_rent_dairy);
        rl_sprays_dairy = (RelativeLayout) findViewById(R.id.rl_sprays_dairy);
        rl_milk_dairy = (RelativeLayout) findViewById(R.id.rl_milk_dairy);
        rl_dairy_crops_seed_dairy = (RelativeLayout) findViewById(R.id.rl_dairy_crops_seed_dairy);
        tv_accessories_dairy = (TextView) findViewById(R.id.bt_accessories);
        tv_feed_dairy = (TextView) findViewById(R.id.bt_feed);
        tv_animals_dairy = (TextView) findViewById(R.id.bt_animals_sale_pur);
        tv_milking_machine_dairy = (TextView) findViewById(R.id.bt_milking_machine);
        tv_medicine_products_dairy = (TextView) findViewById(R.id.bt_medicine_products);
        tv_shed_construction_dairy = (TextView) findViewById(R.id.bt_shed_constructions);
        tv_labs_dairy = (TextView) findViewById(R.id.bt_labs);
        tv_labour_dairy = (TextView) findViewById(R.id.bt_labor);
        tv_land_on_rent_dairy = (TextView) findViewById(R.id.bt_land);
        tv_sprays_dairy = (TextView) findViewById(R.id.bt_pesticide);
        tv_milk_dairy = (TextView) findViewById(R.id.bt_milk_sale_purchase);
        tv_dairy_crops_seed_dairy = (TextView) findViewById(R.id.bt_dairy_crops_seed);

       // bt_accessories = (Button) findViewById(R.id.bt_accessories);


        Intent resultIntent = getIntent();
        String result = resultIntent.getExtras().getString("from");
        category_id = resultIntent.getExtras().getString("category_id");
        if (result.equals("dairy")){


        }
        else if (result.equals("other")){


        }
        else if (result.equals("pets")){


        }
        else if (result.equals("equine")){


        }


    }

    private void farmSolutionButtonClickHandler(){

        rl_accessories_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "accessories");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "1");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_accessories_dairy.getText().toString()));
                startActivity(i);

            }
        });

        rl_feed_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "feed");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "2");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_feed_dairy.getText().toString()));
                startActivity(i);

            }
        });


        rl_animals_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "animals");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "3");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_animals_dairy.getText().toString()));
                Log.e("TEST","TEXT: " + tv_animals_dairy.getText().toString());
                Log.e("TEST","SubCategory ID: " + Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_animals_dairy.getText().toString()));
                startActivity(i);

            }
        });
        rl_milking_machine_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "milking_machine");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "4");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_milking_machine_dairy.getText().toString()));
                startActivity(i);

            }
        });

        rl_medicine_products_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "medicine_products");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "5");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_medicine_products_dairy.getText().toString()));
                startActivity(i);

            }
        });

        rl_shed_construction_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "shed_construction");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "6");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_shed_construction_dairy.getText().toString()));
                startActivity(i);

            }
        });

        rl_labs_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "labs");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "7");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_labs_dairy.getText().toString()));
                startActivity(i);

            }
        });

        rl_labour_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "labour");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "8");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_labour_dairy.getText().toString()));
                startActivity(i);

            }
        });

        rl_land_on_rent_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "land_on_rent");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "9");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_land_on_rent_dairy.getText().toString()));
                startActivity(i);

            }
        });

        rl_sprays_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "sprays");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "10");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_sprays_dairy.getText().toString()));
                startActivity(i);

            }
        });

        rl_milk_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "milk");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "11");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_milk_dairy.getText().toString()));
                Log.e("TEST","SubCategory ID: " + Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_milk_dairy.getText().toString()));
                startActivity(i);

            }
        });

        rl_dairy_crops_seed_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                i.putExtra("Type", "dairy_crops_seed");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "12");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_dairy_organization,tv_dairy_crops_seed_dairy.getText().toString()));
                startActivity(i);

            }
        });

    }

  /*  private void farmSolutionButtonClickHandler(){
        bt_accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForDairy.this, FarmSolutionListing.class);
                startActivity(i);

            }
        });
    }*/
}
