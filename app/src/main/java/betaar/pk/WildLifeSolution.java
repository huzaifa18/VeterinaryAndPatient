package betaar.pk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WildLifeSolution extends DrawerActivityForClient {

    TextView tv_title;
    RelativeLayout rl_accessories_wildlife,rl_feed_wildlife,rl_animals_wildlife, rl_vaccinations_wildlife,
            rl_medicine_products_wildlife,rl_shed_construction_wildlife,rl_labs_wildlife,rl_labour_wildlife;
    TextView tv_accessories_wildlife,tv_feed_wildlife,tv_animals_wildlife, tv_vaccinations_wildlife,
            tv_medicine_products_wildlife,tv_shed_construction_wildlife,tv_labs_wildlife,tv_labour_wildlife;

    String category_id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pet_solution);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.custome_screen_for_other_tab, null, false);
        mDrawerLayout.addView(contentView, 0);


        init();
        wildlifeolutioButtonClickHandler();
    }


    private void init(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        // bt_accessories = (Button) findViewById(R.id.bt_accessories);

        rl_accessories_wildlife = (RelativeLayout) findViewById(R.id.rl_accessories_wildlife);
        rl_feed_wildlife = (RelativeLayout) findViewById(R.id.rl_feed_wildlife);
        rl_animals_wildlife = (RelativeLayout) findViewById(R.id.rl_animals_wildlife);
        rl_vaccinations_wildlife = (RelativeLayout) findViewById(R.id.rl_vaccinations_wildlife);
        rl_medicine_products_wildlife = (RelativeLayout) findViewById(R.id.rl_medicine_products_wildlife);
        rl_shed_construction_wildlife = (RelativeLayout) findViewById(R.id.rl_shed_construction_wildlife);
        rl_labs_wildlife = (RelativeLayout) findViewById(R.id.rl_labs_wildlife);
        rl_labour_wildlife = (RelativeLayout) findViewById(R.id.rl_labour_wildlife);

        tv_accessories_wildlife = (TextView) findViewById(R.id.bt_accessories);
        tv_feed_wildlife = (TextView) findViewById(R.id.bt_feed);
        tv_animals_wildlife = (TextView) findViewById(R.id.bt_animals_sale_pur);
        tv_vaccinations_wildlife = (TextView) findViewById(R.id.bt_veccinatin);
        tv_medicine_products_wildlife = (TextView) findViewById(R.id.bt_medicine_products);
        tv_shed_construction_wildlife = (TextView) findViewById(R.id.bt_shed_constructions);
        tv_labs_wildlife = (TextView) findViewById(R.id.bt_labs);
        tv_labour_wildlife = (TextView) findViewById(R.id.bt_labor);


        Intent resultIntent = getIntent();
        String result = resultIntent.getExtras().getString("from");
        category_id = resultIntent.getExtras().getString("category_id");
        
        if (result.equals("dairy")){


        }
        else if (result.equals("other")){


        }
        else if (result.equals("wildlife")){


        }
        else if (result.equals("equine")){


        }


    }

    private void wildlifeolutioButtonClickHandler() {

        rl_accessories_wildlife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(WildLifeSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "accessories");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "1");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_wildLife_organization,tv_accessories_wildlife.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_feed_wildlife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(WildLifeSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "feed");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "2");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_wildLife_organization,tv_feed_wildlife.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });


        rl_animals_wildlife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(WildLifeSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "animals");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "3");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_wildLife_organization,tv_animals_wildlife.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_vaccinations_wildlife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(WildLifeSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "milking_machine");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "4");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_wildLife_organization,tv_vaccinations_wildlife.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_medicine_products_wildlife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(WildLifeSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "medicine_products");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "5");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_wildLife_organization,tv_medicine_products_wildlife.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_shed_construction_wildlife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(WildLifeSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "shed_construction");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "6");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_wildLife_organization,tv_shed_construction_wildlife.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_labs_wildlife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(WildLifeSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "labs");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "7");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_wildLife_organization,tv_labs_wildlife.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_labour_wildlife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(WildLifeSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "labour");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "8");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_wildLife_organization,tv_labour_wildlife.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
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
