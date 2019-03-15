package betaar.pk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PetSolution extends DrawerActivityForClient {

    TextView tv_title;
    RelativeLayout rl_accessories_pets,rl_feed_pets,rl_animals_pets, rl_vaccinations_pets,
            rl_medicine_products_pets,rl_shed_construction_pets,rl_labs_pets,rl_labour_pets,
            rl_trainer_pets,rl_breeder_pets;
    TextView tv_accessories_pets,tv_feed_pets,tv_animals_pets, tv_vaccinations_pets,
            tv_medicine_products_pets,tv_shed_construction_pets,tv_labs_pets,tv_labour_pets,
            tv_trainer_pets, tv_breeder_pets;
    
    String category_id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pet_solution);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.custome_pet_solution_screen, null, false);
        mDrawerLayout.addView(contentView, 0);


        init();
        petSolutioButtonClickHandler();
    }

    private void init(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        // bt_accessories = (Button) findViewById(R.id.bt_accessories);

        rl_accessories_pets = (RelativeLayout) findViewById(R.id.rl_accessories_pets);
        rl_feed_pets = (RelativeLayout) findViewById(R.id.rl_feed_pets);
        rl_animals_pets = (RelativeLayout) findViewById(R.id.rl_animals_pets);
        rl_vaccinations_pets = (RelativeLayout) findViewById(R.id.rl_vaccinations_pets);
        rl_medicine_products_pets = (RelativeLayout) findViewById(R.id.rl_medicine_products_pets);
        rl_shed_construction_pets = (RelativeLayout) findViewById(R.id.rl_shed_construction_pets);
        rl_labs_pets = (RelativeLayout) findViewById(R.id.rl_labs_pets);
        rl_labour_pets = (RelativeLayout) findViewById(R.id.rl_labor_pets);
        rl_trainer_pets = (RelativeLayout) findViewById(R.id.rl_trainer_pets);
        rl_breeder_pets = (RelativeLayout) findViewById(R.id.rl_breeder_pets);

        tv_accessories_pets = (TextView) findViewById(R.id.bt_accessories);
        tv_feed_pets = (TextView) findViewById(R.id.bt_feed);
        tv_animals_pets = (TextView) findViewById(R.id.bt_animals_sale_pur);
        tv_vaccinations_pets = (TextView) findViewById(R.id.bt_veccinatin);
        tv_medicine_products_pets = (TextView) findViewById(R.id.bt_medicine_products);
        tv_shed_construction_pets = (TextView) findViewById(R.id.bt_shed_constructions);
        tv_labs_pets = (TextView) findViewById(R.id.bt_labs);
        tv_labour_pets = (TextView) findViewById(R.id.bt_labor);
        tv_trainer_pets = (TextView) findViewById(R.id.bt_trainer);
        tv_breeder_pets = (TextView) findViewById(R.id.bt_breeder);


        Intent resultIntent = getIntent();
        String result = resultIntent.getExtras().getString("from");
        category_id = resultIntent.getExtras().getString("category_id");

        if (result.equals("pets")){


        }
        else if (result.equals("other")){


        }
        else if (result.equals("pets")){


        }
        else if (result.equals("equine")){


        }


    }

    private void petSolutioButtonClickHandler() {

        rl_accessories_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PetSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "accessories");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "1");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_pets_organization,tv_accessories_pets.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_feed_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PetSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "feed");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "2");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_pets_organization,tv_feed_pets.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });


        rl_animals_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PetSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "animals");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "3");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_pets_organization,tv_animals_pets.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_vaccinations_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PetSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "milking_machine");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "4");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_pets_organization,tv_vaccinations_pets.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_medicine_products_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PetSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "medicine_products");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "5");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_pets_organization,tv_medicine_products_pets.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_shed_construction_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PetSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "shed_construction");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "6");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_pets_organization,tv_shed_construction_pets.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_labs_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PetSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "labs");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "7");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_pets_organization,tv_labs_pets.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_labour_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PetSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "labour");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "8");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_pets_organization,tv_labour_pets.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_trainer_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PetSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "land_on_rent");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "9");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_pets_organization,tv_trainer_pets.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

        rl_breeder_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PetSolution.this, FarmSolutionProducts.class);
                i.putExtra("Type", "sprays");
                i.putExtra("category_id", ""+category_id);
                //i.putExtra("subcategory_id", "10");
                i.putExtra("sub_category_id", Arrays.getID(Arrays.product_categories_for_pets_organization,tv_breeder_pets.getText().toString()));
                i.putExtra("internal_sub_category_id", "0");
                startActivity(i);

            }
        });

    }

  /*  private void farmSolutionButtonClickHandler(){
        bt_accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PetSolution.this, FarmSolutionProducts.class);
                startActivity(i);

            }
        });
    }*/
}
