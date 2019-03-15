package betaar.pk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import betaar.pk.Preferences.Prefs;

public class DashBoardOrganization extends DrawerActvityForOrganization {

    TextView tv_name;
    RelativeLayout rl_my_products;
    RelativeLayout rl_offer_jobs, rl_sale_products;

    int backPressCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dash_board_organization);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_dash_board_organization, null, false);
        mDrawerLayout.addView(contentView, 0);
        toolbar.setTitle("Organization");

        init();
        onClickLitenerForCheckupRequests();
        onClickLitenerForOfferJobs();
        onClickLitenerForSaleProducts();

    }//end of onCreate

    private void init(){

        getIntent();

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(Prefs.getUserFullNameFromPref(getApplicationContext()).toString());
        rl_my_products = (RelativeLayout) findViewById(R.id.rl_my_products);

        rl_offer_jobs = (RelativeLayout) findViewById(R.id.rl_offer_jobs);
        rl_sale_products = (RelativeLayout) findViewById(R.id.rl_sale_products);
    }

    private void onClickLitenerForCheckupRequests(){

        rl_my_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DashBoardOrganization.this, OrganizationMyProducts.class);
                startActivity(i);


            }
        });
    }


    private void onClickLitenerForOfferJobs(){

        rl_offer_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DashBoardOrganization.this, OrganizationOfferJobsActivity.class);
                startActivity(i);

            }
        });
    }
    private void onClickLitenerForSaleProducts(){

        rl_sale_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DashBoardOrganization.this, OrganizationSaleProductActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (backPressCount == 0){
            backPressCount++;
        } else {
            finish();
        }
    }

}
