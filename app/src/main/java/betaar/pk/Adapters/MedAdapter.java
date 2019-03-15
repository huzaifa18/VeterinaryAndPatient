package betaar.pk.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import betaar.pk.Models.MedGroups;
import betaar.pk.R;

public class MedAdapter extends RecyclerView.Adapter<MedAdapter.MyViewHolder>{

    private ArrayList<MedGroups> medList;
    private Activity mContext;

    public MedAdapter(ArrayList<MedGroups> medList, Activity mContext) {
        this.medList = medList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_bill_confirmation_screen, null);
        MyViewHolder mh = new MyViewHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvItemTitle.setText(medList.get(position).getMedData().get(position).getName());
        holder.sample1.setText(medList.get(position).getName());
        holder.sample2.setText(medList.get(position).getMedData().get(position).getUnit_price());
        holder.sample3.setText(medList.get(position).getMedData().get(position).getUnit());

    }

    @Override
    public int getItemCount() {
        return medList.size();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

    protected TextView tvItemTitle;
    protected TextView sample1;
    protected TextView sample2;
    protected TextView sample3;
    protected TextView sample4;
    protected TextView tv_viwe_more;

    protected RelativeLayout rl_main_layout;
    protected RelativeLayout bt_item_detail;


    public MyViewHolder(final View view) {
        super(view);

        tvItemTitle =  (TextView) view.findViewById(R.id.tv_item_title);
        sample1 =  (TextView) view.findViewById(R.id.tv_line_1);
        sample2 =  (TextView) view.findViewById(R.id.tv_line_2);
        sample3 =  (TextView) view.findViewById(R.id.tv_line_3);
        sample4 =  (TextView) view.findViewById(R.id.tv_line_4);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }

}

}
