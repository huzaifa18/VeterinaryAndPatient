package betaar.pk.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import betaar.pk.Arrays;
import betaar.pk.FarmSolutionListing;
import betaar.pk.FarmSolutionProducts;
import betaar.pk.Models.DataIds;
import betaar.pk.Models.FarmSolutionData;
import betaar.pk.OrganizationMyProducts;
import betaar.pk.R;

/**
 * Created by Huzaifa Asif on 16-Apr-18.
 */

public class ListingDataApaterForFarmSolutions extends RecyclerView.Adapter<ListingDataApaterForFarmSolutions.MyViewHolder>{

private ArrayList<FarmSolutionData> blogPostList;
private ArrayList<DataIds> subList;
private Activity mContext;
private String category_id = "0";
private String sub_category_id = "0";


    /*public ListingDataApaterForFarmSolutions(Activity context, ArrayList<FarmSolutionData> adList) {

        this.mContext = context;
        this.blogPostList = adList;
    }*/

    public ListingDataApaterForFarmSolutions(Activity context, ArrayList<DataIds> adList) {

        this.mContext = context;
        this.subList = adList;
    }

    public ListingDataApaterForFarmSolutions(Activity mContext, ArrayList<DataIds> subList, String category_id, String sub_category_id) {
        this.subList = subList;
        this.mContext = mContext;
        this.category_id = category_id;
        this.sub_category_id = sub_category_id;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

    protected TextView tvItemTitle;
    protected ImageView imItemImage;
    protected TextView sample1;
    protected TextView sample2;
    protected TextView sample3;
    protected TextView bt_dairy_subcategory;


    public MyViewHolder(final View view) {
        super(view);

        //tvItemTitle =  (TextView) view.findViewById(R.id.tv_item_title);
        //sample1 =  (TextView) view.findViewById(R.id.tv_line_1);
        //sample2 =  (TextView) view.findViewById(R.id.tv_line_2);
        //sample3 =  (TextView) view.findViewById(R.id.tv_line_3);
        bt_dairy_subcategory =  (TextView) view.findViewById(R.id.bt_dairy_subcategory);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(mContext,FarmSolutionProducts.class);
                i.putExtra("category_id", category_id);
                i.putExtra("sub_category_id", sub_category_id);
                //i.putExtra("internal_sub_category_id", subList.get(getAdapterPosition()).getId()+1);
                i.putExtra("internal_sub_category_id", Arrays.getID(subList,subList.get(getAdapterPosition()+1).getName()));

                //Log.e("Adapter","Category: " + category_id + "\n Sub Category: " + sub_category_id + "\n Internal Sub Category: " + subList.get(getAdapterPosition()).getId());

                mContext.startActivity(i);
            }
        });


    }

}


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_farm_solutin_layout, null);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dairy_organization_sub, null);
        MyViewHolder mh = new MyViewHolder(v);
        return mh;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            //Log.e("TAG", "the array size is: " + blogPostList.size());

            if (position == subList.size()-1){

            } else {
                position++;
            }

            holder.bt_dairy_subcategory.setText(subList.get(position).getName());

            /*FarmSolutionData ad = blogPostList.get(position);



                    holder.tvItemTitle.setText(ad.getTitle());
                    holder.sample1.setText(ad.getSample1());
                    holder.sample2.setText(ad.getSample2());
                    holder.sample3.setText(ad.getSample3());*/

        }
    }


    @Override
    public int getItemCount() {

        return subList.size()-1;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        setFadeAnimation(holder.itemView);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2000);
        view.startAnimation(anim);
    }

}
