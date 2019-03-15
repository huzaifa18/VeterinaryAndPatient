package betaar.pk.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import betaar.pk.Adapters.SpinnerListingAdapter;
import betaar.pk.Arrays;
import betaar.pk.CallVetForDairy;
import betaar.pk.ChatActivityMain;
import betaar.pk.DashboardClient;
import betaar.pk.DashboardVeterinarian;
import betaar.pk.DrawerActivityForClient;
import betaar.pk.FarmSolutionForDairy;
import betaar.pk.Models.DataIds;
import betaar.pk.ProductDetails;
import betaar.pk.R;
import betaar.pk.SaleAnimals;
import betaar.pk.SignInActivity;


public class FragmentDairy extends Fragment {

    Spinner sp_select_dairy_category;
    TextView tv_spinner;
    RelativeLayout bt_call_a_veteriany, bt_farm_solutino, bt_sale_dairy;
    RelativeLayout rl_spiner;
    String category_id = "0";
    String item = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =inflater.inflate(R.layout.dairy_fragment, container, false);

        init(v);
        callingVeterinaryActivity();
        farmSolutionActivity();
        btSaleDairyClickHanlder();


        return v;
    }

    private void init(View view){

        sp_select_dairy_category = (Spinner) view.findViewById(R.id.sp_select_dairy_category);
        bt_call_a_veteriany = (RelativeLayout) view.findViewById(R.id.bt_call_a_veteriany);
        bt_farm_solutino = (RelativeLayout) view.findViewById(R.id.bt_farm_solutino);
        bt_sale_dairy = (RelativeLayout) view.findViewById(R.id.bt_sale_dairy);
        rl_spiner = (RelativeLayout) view.findViewById(R.id.rl_spiner);


        /*ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.dairy_categories, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_select_dairy_category.setAdapter(adapter);

        Log.e("Tag", "Array: "+ Arrays.dairy_categories.size());*/

        SpinnerListingAdapter category = new SpinnerListingAdapter(getContext(), Arrays.dairy_categories);
        sp_select_dairy_category.setAdapter(category);
        category.notifyDataSetChanged();

        spinnerClickListener();

    }

    private void spinnerClickListener() {

        sp_select_dairy_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //tv_spinner.setTextColor(Color.WHITE);
            }
        });

    }

    private void callingVeterinaryActivity(){

        bt_call_a_veteriany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // showDialogue();

                /*getActivity().startActivity(new Intent(getActivity(), ChatActivityMain.class));
                getActivity().finish();*/

                final Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

                if (sp_select_dairy_category.getSelectedItemPosition() == 0){

                    Toast.makeText(getContext(), "Please Select Dairy Category", Toast.LENGTH_SHORT).show();
                    rl_spiner.setAnimation(animShake);

                } else {

                    Intent callVet = new Intent(getActivity(), CallVetForDairy.class);
                    callVet.putExtra("category_id", Arrays.getID(Arrays.product_types, "Dairy Solution"));
                    callVet.putExtra("sub_category_id", Arrays.dairy_categories.get(sp_select_dairy_category.getSelectedItemPosition()).getId());
                    Log.e("TAG", "Sub Category1: " + Arrays.dairy_categories.get(sp_select_dairy_category.getSelectedItemPosition()).getId());
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
                Intent farmSolution = new Intent(getActivity(), FarmSolutionForDairy.class);
                farmSolution.putExtra("from", "dairy");
                farmSolution.putExtra("category_id", Arrays.getID(Arrays.product_types,"Dairy Solution"));
                getActivity().startActivity(farmSolution);
            }
        });
    }

    private void btSaleDairyClickHanlder(){

        bt_sale_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

           /*     int item_position = sp_select_dairy_category.getSelectedItemPosition();
                if (item_position == 0){
                    ((TextView)sp_select_dairy_category.getSelectedView()).setError("Please Select Category");
                    rl_spiner.setAnimation(animShake);

                }else {*/

                   // String selectedItem = sp_select_dairy_category.getSelectedItem().toString();
                    Intent i = new Intent(getActivity(), SaleAnimals.class);
                    i.putExtra("type", "Dairy");
                    i.putExtra("category_id", Arrays.getID(Arrays.product_types,"Dairy Solution"));
                    //i.putExtra("item", selectedItem);
                    startActivity(i);

                //}
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
