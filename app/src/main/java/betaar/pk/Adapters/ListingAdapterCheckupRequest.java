package betaar.pk.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import betaar.pk.Config.API;
import betaar.pk.DashboardVeterinarian;
import betaar.pk.Helpers.CustomProgressDialog;
import betaar.pk.Models.RequestModelVet;
import betaar.pk.OrganizationSaleProductActivity;
import betaar.pk.PathDrawActivity;
import betaar.pk.Preferences.Prefs;
import betaar.pk.R;
import betaar.pk.RequestDetail;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

/**
 * Created by Huzaifa Asif on 18-Apr-18.
 */

public class ListingAdapterCheckupRequest extends RecyclerView.Adapter<ListingAdapterCheckupRequest.MyViewHolder>{

    private ArrayList<RequestModelVet> blogPostList;
    private Activity mContext;
    private String recievedFrom;

    CustomProgressDialog progressDialog;


    public ListingAdapterCheckupRequest(Activity context, ArrayList<RequestModelVet> adList) {

        this.mContext = context;
        this.blogPostList = adList;

        progressDialog=new CustomProgressDialog(context, 1);
    }

    public ListingAdapterCheckupRequest(Activity mContext, ArrayList<RequestModelVet> blogPostList, String recievedFrom) {
        this.blogPostList = blogPostList;
        this.mContext = mContext;
        this.recievedFrom = recievedFrom;
        progressDialog=new CustomProgressDialog(mContext, 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvItemTitle;
        protected ImageView imItemImage;
        protected TextView sample1;
        protected TextView sample2;
        protected TextView sample3;
        protected TextView tv_viwe_more;

        protected RelativeLayout rl_main_layout;
        protected RelativeLayout bt_item_detail;


        public MyViewHolder(final View view) {
            super(view);

            tvItemTitle =  (TextView) view.findViewById(R.id.tv_item_title);
            sample1 =  (TextView) view.findViewById(R.id.tv_line_1);
            sample2 =  (TextView) view.findViewById(R.id.tv_line_2);
            sample3 =  (TextView) view.findViewById(R.id.tv_line_3);
            rl_main_layout = (RelativeLayout) view.findViewById(R.id.rl_main_layout);
            bt_item_detail = (RelativeLayout) view.findViewById(R.id.bt_item_detail);
            tv_viwe_more = (TextView) view.findViewById(R.id.tv_viwe_more);
            tv_viwe_more.bringToFront();

        }

    }

    // visit cancel webservice
    public void cancelRequest(final String visit_id){

        // Tag used to cancel the request
        String cancel_req_tag = "visit cancel";

        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, API.Respond_to_request, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "visit cancel response: " + response.toString());
                //hideDialog();
                progressDialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String msg = jObj.getString("msg");

                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mContext,DashboardVeterinarian.class);
                        mContext.startActivity(i);
                        mContext.finish();

                    } else {

                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(mContext);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "visit cancel Error: " + error.getMessage());
                Toast.makeText(mContext, "Service Not working", Toast.LENGTH_LONG).show();
                //hideDialog();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("key", API.KEY);
                params.put("visit_id", visit_id);
                params.put("status", "cancelled");
                params.put("user_id", Prefs.getUserIDFromPref(mContext));

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(mContext).addToRequestQueue(strReq, cancel_req_tag);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_farm_solutin_layout, null);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_layout_checkup_request, null);
        MyViewHolder mh = new MyViewHolder(v);
        return mh;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            Log.e("TAG", "the array size is: " + blogPostList.size());

            RequestModelVet ad = blogPostList.get(position);


            holder.tvItemTitle.setText(ad.getName());
            holder.sample1.setText(ad.getProtocol());

            if (ad.getImmediate_visit().toString().equals("1")){

                holder.sample2.setText("Immediate Request");
                holder.sample3.setVisibility(View.GONE);

            } else {

                holder.sample2.setText(ad.getTime_of_visit());
                holder.sample3.setText(ad.getDate_of_visit());

            }

            holder.bt_item_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (recievedFrom.equals("client")) {

                        Intent intent = new Intent(mContext, RequestDetail.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("name",blogPostList.get(holder.getAdapterPosition()).getName());
                        intent.putExtra("phone",blogPostList.get(holder.getAdapterPosition()).getPhone());
                        intent.putExtra("lat",blogPostList.get(holder.getAdapterPosition()).getUser_lat());
                        intent.putExtra("lng",blogPostList.get(holder.getAdapterPosition()).getUser_lng());
                        intent.putExtra("visit_id",blogPostList.get(holder.getAdapterPosition()).getVisit_id());
                        intent.putExtra("immediate_visit",blogPostList.get(holder.getAdapterPosition()).getImmediate_visit());
                        intent.putExtra("date_of_visit",blogPostList.get(holder.getAdapterPosition()).getDate_of_visit());
                        intent.putExtra("time_of_visit",blogPostList.get(holder.getAdapterPosition()).getTime_of_visit());
                        intent.putExtra("user_address",blogPostList.get(holder.getAdapterPosition()).getUser_address());
                        intent.putExtra("reason_of_visit",blogPostList.get(holder.getAdapterPosition()).getReason_of_visit());
                        intent.putExtra("no_of_animals",blogPostList.get(holder.getAdapterPosition()).getNo_of_animals());
                        intent.putExtra("protocol",blogPostList.get(holder.getAdapterPosition()).getProtocol());
                        intent.putExtra("category",blogPostList.get(holder.getAdapterPosition()).getCategory());
                        intent.putExtra("sub_category",blogPostList.get(holder.getAdapterPosition()).getSubcategory());
                        intent.putExtra("request_type","completed");
                        intent.putExtra("type", recievedFrom);
                        intent.putExtra("status", "history");
                        mContext.startActivity(intent);

                    } else if (recievedFrom.equals("current")) {

                        Intent intent = new Intent(mContext, RequestDetail.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("name",blogPostList.get(holder.getAdapterPosition()).getName());
                        intent.putExtra("phone",blogPostList.get(holder.getAdapterPosition()).getPhone());
                        intent.putExtra("lat",blogPostList.get(holder.getAdapterPosition()).getUser_lat());
                        intent.putExtra("lng",blogPostList.get(holder.getAdapterPosition()).getUser_lng());
                        intent.putExtra("visit_id",blogPostList.get(holder.getAdapterPosition()).getVisit_id());
                        intent.putExtra("immediate_visit",blogPostList.get(holder.getAdapterPosition()).getImmediate_visit());
                        intent.putExtra("date_of_visit",blogPostList.get(holder.getAdapterPosition()).getDate_of_visit());
                        intent.putExtra("time_of_visit",blogPostList.get(holder.getAdapterPosition()).getTime_of_visit());
                        intent.putExtra("user_address",blogPostList.get(holder.getAdapterPosition()).getUser_address());
                        intent.putExtra("reason_of_visit",blogPostList.get(holder.getAdapterPosition()).getReason_of_visit());
                        intent.putExtra("no_of_animals",blogPostList.get(holder.getAdapterPosition()).getNo_of_animals());
                        intent.putExtra("protocol",blogPostList.get(holder.getAdapterPosition()).getProtocol());
                        intent.putExtra("category",blogPostList.get(holder.getAdapterPosition()).getCategory());
                        intent.putExtra("sub_category",blogPostList.get(holder.getAdapterPosition()).getSubcategory());
                        intent.putExtra("request_type","current");
                        intent.putExtra("status", "accepted");
                        intent.putExtra("type", recievedFrom);
                        mContext.startActivity(intent);

                    } else {

                                /*Intent i = new Intent(mContext, PathDrawActivity.class);
                                i.putExtra("lat", blogPostList.get(holder.getAdapterPosition()).getUser_lat());
                                i.putExtra("lng", blogPostList.get(holder.getAdapterPosition()).getUser_lng());
                                i.putExtra("name", blogPostList.get(holder.getAdapterPosition()).getName());
                                i.putExtra("address", blogPostList.get(holder.getAdapterPosition()).getUser_address());
                                i.putExtra("phone", blogPostList.get(holder.getAdapterPosition()).getPhone());
                                i.putExtra("visit_id", blogPostList.get(holder.getAdapterPosition()).getVisit_id());
                                i.putExtra("status", "accepted");
                                i.putExtra("type", recievedFrom);
                                mContext.startActivity(i);*/

                        Intent intent = new Intent(mContext, RequestDetail.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("name",blogPostList.get(holder.getAdapterPosition()).getName());
                        intent.putExtra("phone",blogPostList.get(holder.getAdapterPosition()).getPhone());
                        intent.putExtra("lat",blogPostList.get(holder.getAdapterPosition()).getUser_lat());
                        intent.putExtra("lng",blogPostList.get(holder.getAdapterPosition()).getUser_lng());
                        intent.putExtra("visit_id",blogPostList.get(holder.getAdapterPosition()).getVisit_id());
                        intent.putExtra("immediate_visit",blogPostList.get(holder.getAdapterPosition()).getImmediate_visit());
                        intent.putExtra("date_of_visit",blogPostList.get(holder.getAdapterPosition()).getDate_of_visit());
                        intent.putExtra("time_of_visit",blogPostList.get(holder.getAdapterPosition()).getTime_of_visit());
                        intent.putExtra("user_address",blogPostList.get(holder.getAdapterPosition()).getUser_address());
                        intent.putExtra("reason_of_visit",blogPostList.get(holder.getAdapterPosition()).getReason_of_visit());
                        intent.putExtra("no_of_animals",blogPostList.get(holder.getAdapterPosition()).getNo_of_animals());
                        intent.putExtra("protocol",blogPostList.get(holder.getAdapterPosition()).getProtocol());
                        intent.putExtra("category",blogPostList.get(holder.getAdapterPosition()).getCategory());
                        intent.putExtra("sub_category",blogPostList.get(holder.getAdapterPosition()).getSubcategory());
                        intent.putExtra("request_type","pending");
                        intent.putExtra("status", "accepted");
                        intent.putExtra("type", "sender");
                        mContext.startActivity(intent);

                    }

                    /*Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_request_detial);
                    ImageView imageView2 = (ImageView) dialog.findViewById(R.id.imageView2);
                    TextView tv_line_1 = (TextView) dialog.findViewById(R.id.tv_line_1);
                    TextView tv_line_2 = (TextView) dialog.findViewById(R.id.tv_line_2);
                    TextView tv_line_3 = (TextView) dialog.findViewById(R.id.tv_line_3);
                    Button bt_dialog_accpet = (Button) dialog.findViewById(R.id.bt_dialog_accpet);
                    Button bt_dialog_ignor = (Button) dialog.findViewById(R.id.bt_dialog_ignor);

                    tv_line_1.setText(blogPostList.get(holder.getAdapterPosition()).getNo_of_animals());
                    tv_line_2.setText(blogPostList.get(holder.getAdapterPosition()).getUser_address());
                    tv_line_3.setText(blogPostList.get(holder.getAdapterPosition()).getReason_of_visit());

                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                    dialog.show();

                    if (recievedFrom.equals("receiver") || recievedFrom.equals("Current")){
                        bt_dialog_accpet.setText("Detail");
                        bt_dialog_ignor.setVisibility(view.GONE);
                    }

                    bt_dialog_accpet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                        }
                    });

                    bt_dialog_ignor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            
                            cancelRequest(blogPostList.get(holder.getAdapterPosition()).getVisit_id());

                        }
                    });*/


                }
            });


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
