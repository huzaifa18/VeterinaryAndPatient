package betaar.pk.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import betaar.pk.Adapters.SpinnerListingAdapter;
import betaar.pk.Arrays;
import betaar.pk.CallVetForDairy;
import betaar.pk.DashboardClient;
import betaar.pk.FarmSolutionForBirds;
import betaar.pk.FarmSolutionForDairy;
import betaar.pk.OrganizationOfferJobsActivity;
import betaar.pk.R;
import betaar.pk.SaleAnimals;

public class Birds extends Fragment {

    Spinner sp_select_bird;
    RelativeLayout bt_call_a_veteriany, bt_farm_solutino, bt_sale_birds;
    RelativeLayout rl_spiner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =inflater.inflate(R.layout.fragment_birds, container, false);

        init(v);
        callingVeterinaryActivity();
        farmSolutionActivity();
        btSaleBirdClickHanlder();

        return v;
    }

    private void init(View view){

sp_select_bird = (Spinner) view.findViewById(R.id.sp_select_bird);
        bt_call_a_veteriany = (RelativeLayout) view.findViewById(R.id.bt_call_a_veteriany);
        bt_farm_solutino = (RelativeLayout) view.findViewById(R.id.bt_farm_solutino);
        bt_sale_birds = (RelativeLayout) view.findViewById(R.id.bt_sale_birds);

        rl_spiner = (RelativeLayout) view.findViewById(R.id.rl_spiner);

        /*ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.birds_categories, R.layout.spinner_dropdown_item);

        //adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_select_bird.setAdapter(adapter);*/

        SpinnerListingAdapter category = new SpinnerListingAdapter(getContext(), Arrays.birds_categories);
        sp_select_bird.setAdapter(category);
        category.notifyDataSetChanged();

    }

    private void callingVeterinaryActivity(){

        bt_call_a_veteriany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

                if (sp_select_bird.getSelectedItemPosition() == 0){

                    Toast.makeText(getContext(), "Please Select Bird Category", Toast.LENGTH_SHORT).show();
                    rl_spiner.setAnimation(animShake);

                } else {

                    //showDialogue();
                    Intent callVet = new Intent(getActivity(), CallVetForDairy.class);
                    callVet.putExtra("category_id", Arrays.getID(Arrays.product_types, "Birds Solution"));
                    callVet.putExtra("sub_category_id", Arrays.birds_categories.get(sp_select_bird.getSelectedItemPosition()).getId());
                    Log.e("TAG", "Sub Category1: " + Arrays.birds_categories.get(sp_select_bird.getSelectedItemPosition()).getId());
                    getActivity().startActivity(callVet);

                }
            }
        });
    }

    private void farmSolutionActivity(){

        bt_farm_solutino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showDialogue();
                Intent farmSolution = new Intent(getActivity(), FarmSolutionForBirds.class);
                farmSolution.putExtra("from", "Birds");
                farmSolution.putExtra("category_id", Arrays.getID(Arrays.product_types,"Birds Solution"));
                getActivity().startActivity(farmSolution);
            }
        });
    }

    private void btSaleBirdClickHanlder(){

        bt_sale_birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

                String selectedItem = sp_select_bird.getSelectedItem().toString();
                Intent i = new Intent(getActivity(), SaleAnimals.class);
                i.putExtra("type", "Bird");
                i.putExtra("category_id", Arrays.getID(Arrays.product_types,"Birds Solution"));
                startActivity(i);


            }
        });
    }

    private void showDialogue(){

        final Dialog dialog = new Dialog(getContext());
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
                /*Intent i = new Intent(getContext(), DashboardClient.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
            }
        });


    }

}
