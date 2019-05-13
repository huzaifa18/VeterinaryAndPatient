package betaar.pk;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import betaar.pk.Preferences.Prefs;

public class DashboardVeterinarian extends DrawerActivityForVeterinarian {

    TextView tv_name;
    RelativeLayout rl_checkup_requests, rl_find_jobs;
    /*RelativeLayout rl_offer_jobs, rl_sale_products;*/

    int backPressCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dashboard_veterinarian);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.custome_layout_for_vet_dashboard,null,false);
        mDrawerLayout.addView(contentView, 0);
        toolbar.setTitle(R.string.app_name);

        init();
        onClickLitenerForCheckupRequests();
        onClickLitenerForFindJobs();
        onClickLitenerForOfferJobs();
        onClickLitenerForSaleProducts();

    }//end of onCreate

    private void init(){

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(Prefs.getUserFullNameFromPref(getApplicationContext()).toString());
        rl_checkup_requests = (RelativeLayout) findViewById(R.id.rl_checkup_requests);
        rl_find_jobs = (RelativeLayout) findViewById(R.id.rl_find_jobs);
        /*rl_offer_jobs = (RelativeLayout) findViewById(R.id.rl_offer_jobs);
        rl_sale_products = (RelativeLayout) findViewById(R.id.rl_sale_products);*/

    }

    private void onClickLitenerForCheckupRequests(){

        rl_checkup_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showDialogue();
                Intent i = new Intent(DashboardVeterinarian.this, CheckupRequestsListing.class);
                startActivity(i);

            }
        });
    }
    private void onClickLitenerForFindJobs(){

        rl_find_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showDialogue();
                Intent i = new Intent(DashboardVeterinarian.this, SearchJobCategory.class);
                startActivity(i);

            }
        });
    }

    private void onClickLitenerForOfferJobs(){

        /*rl_offer_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DashboardVeterinarian.this, OrganizationOfferJobsActivity.class);
                startActivity(i);

            }
        });*/
    }
    private void onClickLitenerForSaleProducts(){

        /*rl_sale_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DashboardVeterinarian.this, OrganizationSaleProductActivity.class);
                startActivity(i);
            }
        });*/
    }

    private void showDialogue(){

        final Dialog dialog = new Dialog(DashboardVeterinarian.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_client_verfication);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(false);
        dialog.show();

        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);

        tv_dialog_title.setText("Sorry for inconvenience!");
        tv_messasge.setText("This service is not available yet. Please wait for the next Update.");

        final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                /*Intent i = new Intent(DashboardVeterinarian.this, DashboardVeterinarian.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
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
