package betaar.pk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FarmSolutionForBirds extends DrawerActivityForClient {

    TextView tv_title;
    RelativeLayout rl_accessories_birds,rl_feed_birds,rl_animals_birds, rl_vaccinations_birds,
            rl_medicine_products_birds,rl_shed_construction_birds,rl_labs_birds,rl_labour_birds,
            rl_egg_birds, rl_shed_for_rent_birds;
    TextView tv_accessories_birds,tv_feed_birds,tv_animals_birds, tv_vaccinations_birds,
            tv_medicine_products_birds,tv_shed_construction_birds,tv_labs_birds,tv_labour_birds,
            tv_egg_birds, tv_shed_for_rent_birds;

    String category_id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_farm_solution_for_birds);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_farm_solution_for_birds, null, false);
        mDrawerLayout.addView(contentView, 0);

        init();
        birdsSolutioButtonClickHandler();

    }

    private void init(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        // bt_accessories = (Button) findViewById(R.id.bt_accessories);

        rl_accessories_birds = (RelativeLayout) findViewById(R.id.rl_accessories_birds);
        rl_feed_birds = (RelativeLayout) findViewById(R.id.rl_feed_birds);
        rl_animals_birds = (RelativeLayout) findViewById(R.id.rl_birds);
        rl_vaccinations_birds = (RelativeLayout) findViewById(R.id.rl_vaccinations_birds);
        rl_medicine_products_birds = (RelativeLayout) findViewById(R.id.rl_medicine_birds);
        rl_shed_construction_birds = (RelativeLayout) findViewById(R.id.rl_shed_construction_birds);
        rl_labs_birds = (RelativeLayout) findViewById(R.id.rl_labs_birds);
        rl_labour_birds = (RelativeLayout) findViewById(R.id.rl_labour_birds);
        rl_egg_birds = (RelativeLayout) findViewById(R.id.rl_egg_birds);
        rl_shed_for_rent_birds = (RelativeLayout) findViewById(R.id.rl_shed_for_rent_birds);

        tv_accessories_birds = (TextView) findViewById(R.id.bt_accessories);
        tv_feed_birds = (TextView) findViewById(R.id.bt_feed);
        tv_animals_birds = (TextView) findViewById(R.id.bt_animals_sale_pur);
        tv_vaccinations_birds = (TextView) findViewById(R.id.bt_veccinatin);
        tv_medicine_products_birds = (TextView) findViewById(R.id.bt_medicine_products);
        tv_shed_construction_birds = (TextView) findViewById(R.id.bt_shed_constructions);
        tv_labs_birds = (TextView) findViewById(R.id.bt_labs);
        tv_labour_birds = (TextView) findViewById(R.id.bt_labor);
        tv_egg_birds = (TextView) findViewById(R.id.bt_egg);
        tv_shed_for_rent_birds = (TextView) findViewById(R.id.bt_shed_for_rent);


        Intent resultIntent = getIntent();
        String result = resultIntent.getExtras().getString("from");
        category_id = resultIntent.getExtras().getString("category_id");

        if (result.equals("birds")){


        }
        else if (result.equals("other")){


        }
        else if (result.equals("pets")){


        }
        else if (result.equals("equine")){


        }


    }

    private void birdsSolutioButtonClickHandler() {

        rl_accessories_birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForBirds.this, FarmSolutionProducts.class);
                i.putExtra("Type", "accessories");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "1");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_bird_organization,tv_accessories_birds.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_feed_birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForBirds.this, FarmSolutionProducts.class);
                i.putExtra("Type", "feed");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "2");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_bird_organization,tv_feed_birds.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });


        rl_animals_birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForBirds.this, FarmSolutionProducts.class);
                i.putExtra("Type", "animals");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "3");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_bird_organization,tv_animals_birds.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_vaccinations_birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForBirds.this, FarmSolutionProducts.class);
                i.putExtra("Type", "milking_machine");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "4");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_bird_organization,tv_vaccinations_birds.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_medicine_products_birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForBirds.this, FarmSolutionProducts.class);
                i.putExtra("Type", "medicine_products");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "5");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_bird_organization,tv_medicine_products_birds.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_shed_construction_birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForBirds.this, FarmSolutionProducts.class);
                i.putExtra("Type", "shed_construction");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "6");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_bird_organization,tv_shed_construction_birds.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_labs_birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForBirds.this, FarmSolutionProducts.class);
                i.putExtra("Type", "labs");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "7");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_bird_organization,tv_labs_birds.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_labour_birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForBirds.this, FarmSolutionProducts.class);
                i.putExtra("Type", "labour");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "8");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_bird_organization,tv_labour_birds.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_egg_birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForBirds.this, FarmSolutionProducts.class);
                i.putExtra("Type", "land_on_rent");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "9");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_bird_organization,tv_egg_birds.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_shed_for_rent_birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FarmSolutionForBirds.this, FarmSolutionProducts.class);
                i.putExtra("Type", "sprays");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "10");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_bird_organization, tv_shed_for_rent_birds.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

    }
    
}
