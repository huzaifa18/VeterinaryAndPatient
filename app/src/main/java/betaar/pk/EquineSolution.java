package betaar.pk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EquineSolution extends DrawerActivityForClient {

    TextView tv_title;
    RelativeLayout rl_accessories_equine,rl_feed_equine,rl_animals_equine, rl_vaccinations_equine,
            rl_medicine_products_equine,rl_shed_construction_equine,rl_labs_equine,rl_labour_equine,
            rl_trainer_equine,rl_breeder_equine;
    TextView tv_accessories_equine,tv_feed_equine,tv_animals_equine, tv_vaccinations_equine,
            tv_medicine_products_equine,tv_shed_construction_equine,tv_labs_equine,tv_labour_equine,
            tv_trainer_equine, tv_breeder_equine;

    String category_id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pet_solution);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.custome_equine_solution_screen, null, false);
        mDrawerLayout.addView(contentView, 0);


        init();
        equineSolutioButtonClickHandler();
    }


    private void init(){
        
        tv_title = (TextView) findViewById(R.id.tv_title);
        // bt_accessories = (Button) findViewById(R.id.bt_accessories);


        rl_accessories_equine = (RelativeLayout) findViewById(R.id.rl_accessories_equine);
        rl_feed_equine = (RelativeLayout) findViewById(R.id.rl_feed_equine);
        rl_animals_equine = (RelativeLayout) findViewById(R.id.rl_equine);
        rl_vaccinations_equine = (RelativeLayout) findViewById(R.id.rl_vaccinations_equine);
        rl_medicine_products_equine = (RelativeLayout) findViewById(R.id.rl_medicine_equine);
        rl_shed_construction_equine = (RelativeLayout) findViewById(R.id.rl_shed_construction_equine);
        rl_labs_equine = (RelativeLayout) findViewById(R.id.rl_labs_equine);
        rl_labour_equine = (RelativeLayout) findViewById(R.id.rl_labour_equine);
        rl_trainer_equine = (RelativeLayout) findViewById(R.id.rl_trainer_equine);
        rl_breeder_equine = (RelativeLayout) findViewById(R.id.rl_breeder_equine);

        tv_accessories_equine = (TextView) findViewById(R.id.bt_accessories);
        tv_feed_equine = (TextView) findViewById(R.id.bt_feed);
        tv_animals_equine = (TextView) findViewById(R.id.bt_animals_sale_pur);
        tv_vaccinations_equine = (TextView) findViewById(R.id.bt_veccinatin);
        tv_medicine_products_equine = (TextView) findViewById(R.id.bt_medicine_products);
        tv_shed_construction_equine = (TextView) findViewById(R.id.bt_shed_constructions);
        tv_labs_equine = (TextView) findViewById(R.id.bt_labs);
        tv_labour_equine = (TextView) findViewById(R.id.bt_labor);
        tv_trainer_equine = (TextView) findViewById(R.id.bt_trainer);
        tv_breeder_equine = (TextView) findViewById(R.id.bt_breeder);


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

    private void equineSolutioButtonClickHandler() {

        rl_accessories_equine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EquineSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "accessories");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "1");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_equine_organization,tv_accessories_equine.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_feed_equine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EquineSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "feed");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "2");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_equine_organization,tv_feed_equine.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });


        rl_animals_equine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EquineSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "animals");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "3");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_equine_organization,tv_animals_equine.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_vaccinations_equine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EquineSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "milking_machine");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "4");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_equine_organization,tv_vaccinations_equine.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_medicine_products_equine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EquineSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "medicine_products");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "5");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_equine_organization,tv_medicine_products_equine.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_shed_construction_equine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EquineSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "shed_construction");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "6");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_equine_organization,tv_shed_construction_equine.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_labs_equine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EquineSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "labs");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "7");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_equine_organization,tv_labs_equine.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_labour_equine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EquineSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "labour");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "8");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_equine_organization,tv_labour_equine.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_trainer_equine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EquineSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "land_on_rent");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "9");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_equine_organization,tv_trainer_equine.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_breeder_equine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EquineSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "sprays");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "10");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_equine_organization,tv_breeder_equine.getText().toString()));
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
