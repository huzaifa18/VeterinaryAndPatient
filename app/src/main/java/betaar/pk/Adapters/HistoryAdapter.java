package betaar.pk.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import java.util.ArrayList;

import betaar.pk.Models.FarmSolutionData;
import betaar.pk.R;

/**
 * Created by User-10 on 18-Apr-18.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{

    private ArrayList<FarmSolutionData> blogPostList;
    private Activity mContext;
    private String title;


    public HistoryAdapter(Activity context, ArrayList<FarmSolutionData> adList) {

        this.mContext = context;
        this.blogPostList = adList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvItemTitle;

        public MyViewHolder(final View view) {
            super(view);

            tvItemTitle =  (TextView) view.findViewById(R.id.tv_item_title);
            //tvItemTitle =  (TextView) view.findViewById(R.id.tv_items_history);

        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_farm_solutin_layout, null);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_farm_solutin_layout, null);
        MyViewHolder mh = new MyViewHolder(v);
        return mh;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Log.e("TAG", "the array size is: " + blogPostList.size());

        FarmSolutionData ad = blogPostList.get(position);

        holder.tvItemTitle.setText(ad.getTitle());

        if (holder instanceof MyViewHolder) {

        }
    }


    @Override
    public int getItemCount() {

        return blogPostList.size();
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

