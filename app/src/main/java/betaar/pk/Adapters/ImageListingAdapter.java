package betaar.pk.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import betaar.pk.Models.FarmSolutionData;
import betaar.pk.Models.MyProductsGetterSetter;
import betaar.pk.R;

public class ImageListingAdapter extends RecyclerView.Adapter<ImageListingAdapter.MyViewHolder>{

    private ArrayList<String> blogPostList;
    private Activity mContext;
    private String title;


    public ImageListingAdapter(Activity context, ArrayList<String> adList) {

        this.mContext = context;
        this.blogPostList = adList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected ImageView tvItemTitle;

        public MyViewHolder(final View view) {
            super(view);

            tvItemTitle =  (ImageView) view.findViewById(R.id.iv_image);
            //tvItemTitle =  (TextView) view.findViewById(R.id.tv_items_history);

        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_farm_solutin_layout, null);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_image_listing, null);
        MyViewHolder mh = new MyViewHolder(v);
        return mh;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        String ad = blogPostList.get(position);

        //Picasso.with(mContext).load(ad).placeholder(R.drawable.logi_icon).into(holder.tvItemTitle);
        Glide.with(mContext).load(ad).apply(new RequestOptions().placeholder(R.drawable.logi_icon)).into(holder.tvItemTitle);
        Log.e("TAH","AD: " + ad);

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
