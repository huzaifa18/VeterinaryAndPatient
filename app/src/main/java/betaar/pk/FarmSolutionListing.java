package betaar.pk;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import betaar.pk.Adapters.ListingDataApaterForFarmSolutions;
import betaar.pk.Models.DataIds;
import betaar.pk.Models.FarmSolutionData;

public class FarmSolutionListing extends DrawerActivityForClient {

    RecyclerView rv_farm_solution;
    LinearLayoutManager linearLayoutManager;
    ListingDataApaterForFarmSolutions dataAdapter;
    TextView headingTv;
    public ArrayList<FarmSolutionData> farmData;
    String type;
    String category_id = "0";
    String sub_category_id = "0";
    ArrayList<DataIds> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_farm_solution_listing);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_farm_solution_listing, null, false);
        mDrawerLayout.addView(contentView, 0);

        Intent i = getIntent();
        type = i.getStringExtra("Type");
        category_id = i.getStringExtra("category_id");
        sub_category_id = i.getStringExtra("sub_category_id");

        Log.e("TEST","SubCategory ID: " + sub_category_id);

        ini();
        setArray();
    }

    private void ini(){
        rv_farm_solution = (RecyclerView) findViewById(R.id.rv_farm_solution);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL, false);
        rv_farm_solution.setLayoutManager(linearLayoutManager);
        headingTv = (TextView) findViewById(R.id.tv_heading);

        /*DividerItemDecoration itemDecorator = new DividerItemDecoration(FarmSolutionListing.this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(FarmSolutionListing.this, R.drawable.list_item_divider));
        rv_farm_solution.addItemDecoration(itemDecorator);*/
        /*requestData = new ArrayList<FarmSolutionData>();

        for (int i = 0; i<=5; i++) {
            requestData.add(new FarmSolutionData("This is Title "+ i, "Sample " + i, "Sample "+ i, "Sample "+ i));
        }*/

    }

    private void setArray() {

        if (type.equals("accessories")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_accesories);
            arr = Arrays.product_sub_categories_for_dairy_accesories;
            headingTv.setText("Accessories");

        } else if (type.equals("feed")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_feed);
            arr = Arrays.product_sub_categories_for_dairy_feed;
            headingTv.setText("Feed");

        }else if (type.equals("animals")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_animals);
            arr = Arrays.product_sub_categories_for_dairy_animals;
            headingTv.setText("Animals (sale & pur)");

        }else if (type.equals("milking_machine")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_milking_parlour);
            arr = Arrays.product_sub_categories_for_dairy_milking_parlour;
            headingTv.setText("Milking Machine");

        }else if (type.equals("medicine_products")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_medicine_products);
            arr = Arrays.product_sub_categories_for_dairy_medicine_products;
            headingTv.setText("Medicine Products");

        }else if (type.equals("shed_construction")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_sheed_construction);
            arr = Arrays.product_sub_categories_for_dairy_sheed_construction;
            headingTv.setText("Shed Construction");

        }else if (type.equals("labs")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_labs);
            arr = Arrays.product_sub_categories_for_dairy_labs;
            headingTv.setText("Labs");

        }else if (type.equals("labour")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_labour);
            arr = Arrays.product_sub_categories_for_dairy_labour;
            headingTv.setText("Labour");

        }else if (type.equals("land_on_rent")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_land_on_rent);
            arr = Arrays.product_sub_categories_for_dairy_land_on_rent;
            headingTv.setText("Land On Rent");

        }else if (type.equals("sprays")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_sprays);
            arr = Arrays.product_sub_categories_for_dairy_sprays;
            headingTv.setText("Sprays");

        }else if (type.equals("milk")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_milk_sale_pur);
            arr = Arrays.product_sub_categories_for_dairy_milk_sale_pur;
            headingTv.setText("Milk");

        }else if (type.equals("dairy_crops_seed")){

            //arr = getResources().getStringArray(R.array.product_sub_categories_for_dairy_crops_seed);
            arr = Arrays.product_sub_categories_for_dairy_crops_seed;
            headingTv.setText("Dairy Crops Seed");

        }

        dataAdapter = new ListingDataApaterForFarmSolutions(FarmSolutionListing.this,  arr, category_id, sub_category_id);
        rv_farm_solution.setAdapter(dataAdapter);

    }


}





