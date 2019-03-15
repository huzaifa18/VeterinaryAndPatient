package betaar.pk;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VerificationActivityForOrganization extends AppCompatActivity {

    Button bt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_for_organization);

        init();
        submitClickListener();
    }


    private void init(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        bt_submit = (Button)findViewById(R.id.bt_submit);

    }


    private void submitClickListener(){

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showingDialog();
                Intent i = new Intent(VerificationActivityForOrganization.this, DashBoardOrganization.class);
                startActivity(i);


            }
        });
    }


    private void showingDialog(){

        final Dialog dialog = new Dialog(VerificationActivityForOrganization.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_for_update_veterinary_profile);
        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);
        RelativeLayout bt_complte_profile = (RelativeLayout) dialog.findViewById(R.id.bt_complte_profile);
        RelativeLayout bt_not_now = (RelativeLayout) dialog.findViewById(R.id.bt_not_now);
        TextView tv_complte_profile = (TextView) dialog.findViewById(R.id.tv_complte_profile);
        TextView tv_second_button_text  = (TextView) dialog.findViewById(R.id.tv_second_button_text);

        tv_complte_profile.setText("Sale Products");
        tv_second_button_text.setText("Offer Jobs");

        //end now button of dialog
        bt_not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        }); //end of not now dialog button

        //bt sale products
        bt_complte_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                Intent updateVetProfile = new Intent(VerificationActivityForOrganization.this, OrganizationSaleProductActivity.class);
                startActivity(updateVetProfile);

            }
        });
        //bt offer jobs
        bt_not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                Intent updateVetProfile = new Intent(VerificationActivityForOrganization.this, OrganizationOfferJobsActivity.class);
                startActivity(updateVetProfile);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.show();
    }
}


