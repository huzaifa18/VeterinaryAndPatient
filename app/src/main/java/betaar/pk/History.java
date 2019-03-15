package betaar.pk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class History extends DrawerActivityForVeterinarian {

    RelativeLayout bt_checkup;
    RelativeLayout bt_product_sale;
    RelativeLayout bt_product_request;
    RelativeLayout bt_job_offer;
    TextView tv_check_up;
    TextView tv_sale_product;
    TextView tv_sale_request;
    TextView tv_offer_job;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.history_test, null, false);
        mDrawerLayout.addView(contentView, 0);

        Intent i = getIntent();
        type = i.getStringExtra("Type");

        initialization();
        clickListeners();

    }

    private void initialization() {

        bt_checkup = (RelativeLayout) findViewById(R.id.rl_checkup_requests);
        bt_product_sale = (RelativeLayout) findViewById(R.id.rl_find_jobs);
        bt_product_request = (RelativeLayout) findViewById(R.id.rl_sale_products_history);
        bt_job_offer = (RelativeLayout) findViewById(R.id.rl_offer_jobs);

        tv_check_up = (TextView) findViewById(R.id.tv_check_up);
        tv_sale_product = (TextView) findViewById(R.id.tv_sale_product);
        tv_sale_request = (TextView) findViewById(R.id.tv_sale_request);
        tv_offer_job = (TextView) findViewById(R.id.tv_offer_job);

        checkType();

    }

    private void clickListeners() {

        bt_checkup.setOnClickListener(bt_checkupOnClickListener);
        bt_product_sale.setOnClickListener(bt_product_saleOnClickListener);
        bt_product_request.setOnClickListener(bt_product_requestOnClickListener);
        bt_job_offer.setOnClickListener(bt_job_offerOnClickListener);

    }

    private View.OnClickListener bt_checkupOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(History.this, CheckUp.class);
            i.putExtra("Title",tv_check_up.getText());
            startActivity(i);
        }
    };

    private View.OnClickListener bt_product_saleOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(History.this, CheckUp.class);
            i.putExtra("Title",tv_sale_product.getText());
            startActivity(i);
        }
    };

    private View.OnClickListener bt_product_requestOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(History.this, CheckUp.class);
            i.putExtra("Title",tv_sale_request.getText());
            startActivity(i);
        }
    };

    private View.OnClickListener bt_job_offerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(History.this, CheckUp.class);
            i.putExtra("Title",tv_offer_job.getText());
            startActivity(i);
        }
    };

    private void checkType(){

        if (type.equals("Veterinarian")){

        } else if (type.equals("Client")){

            tv_check_up.setText("Call Vet History");
            tv_sale_request.setText("Buying History");
            bt_job_offer.setVisibility(View.GONE);

        } else if (type.equals("Organization")){

            bt_checkup.setVisibility(View.GONE);
            bt_product_request.setVisibility(View.GONE);

        }

    }

}
