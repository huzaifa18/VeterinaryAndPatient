package betaar.pk.Fragments;

/**
 * Created by Huzaifa Asif on 16-Apr-18.
 */

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import betaar.pk.Adapters.SpinnerListingAdapter;
import betaar.pk.Arrays;
import betaar.pk.CallVetForDairy;
import betaar.pk.WildLifeSolution;
import betaar.pk.R;
import betaar.pk.SaleAnimals;


public class FragmentOther extends Fragment {

    Spinner sp_select_other;
    RelativeLayout bt_call_a_veteriany, bt_farm_solutino, bt_sale_other;
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
        final View v =inflater.inflate(R.layout.fragment_others, container, false);

        init(v);
        callingVeterinaryActivity();
        farmSolutionActivity();
        btSaleDairyClickHanlder();

        return v;
    }

    private void init(View view){

        sp_select_other = (Spinner) view.findViewById(R.id.sp_select_other);
        bt_call_a_veteriany = (RelativeLayout) view.findViewById(R.id.bt_call_a_veteriany);
        bt_farm_solutino = (RelativeLayout) view.findViewById(R.id.bt_farm_solutino);
        bt_sale_other = (RelativeLayout) view.findViewById(R.id.bt_sale_other);
        rl_spiner = (RelativeLayout) view.findViewById(R.id.rl_spiner);


        /*ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.other_categories, R.layout.spinner_dropdown_item);

        //adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_select_other.setAdapter(adapter);*/

        SpinnerListingAdapter category = new SpinnerListingAdapter(getContext(), Arrays.other_categories);
        sp_select_other.setAdapter(category);
        category.notifyDataSetChanged();

    }

    private void callingVeterinaryActivity(){

        bt_call_a_veteriany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

                if (sp_select_other.getSelectedItemPosition() == 0){

                    Toast.makeText(getContext(), "Please Select Wild Life Category", Toast.LENGTH_SHORT).show();
                    rl_spiner.setAnimation(animShake);

                } else {

                    //showDialogue();
                    Intent callVet = new Intent(getActivity(), CallVetForDairy.class);
                    callVet.putExtra("category_id", Arrays.getID(Arrays.product_types, "Wild Life Solution"));
                    callVet.putExtra("sub_category_id", Arrays.other_categories.get(sp_select_other.getSelectedItemPosition()).getId());
                    Log.e("TAG", "Sub Category1: " + Arrays.other_categories.get(sp_select_other.getSelectedItemPosition()).getId());
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
                Intent farmSolution = new Intent(getActivity(), WildLifeSolution.class);
                farmSolution.putExtra("from", "Wild Life");
                farmSolution.putExtra("category_id", Arrays.getID(Arrays.product_types,"Wild Life Solution"));
                getActivity().startActivity(farmSolution);
            }
        });
    }

    private void btSaleDairyClickHanlder(){

        bt_sale_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

                    String selectedItem = sp_select_other.getSelectedItem().toString();
                    Intent i = new Intent(getActivity(), SaleAnimals.class);
                    i.putExtra("type", "Other");
                    i.putExtra("category_id", Arrays.getID(Arrays.product_types,"Wild Life Solution"));
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
