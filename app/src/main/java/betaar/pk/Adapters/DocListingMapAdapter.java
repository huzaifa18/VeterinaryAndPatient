package betaar.pk.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import betaar.pk.Models.MapGetterSetter;
import betaar.pk.R;
import betaar.pk.VetDetail;

public class DocListingMapAdapter extends RecyclerView.Adapter<DocListingMapAdapter.DataHolder> {

        public static List<MapGetterSetter> infoList;
        Context context;
        DocListingMapAdapter.OnitemClickListener onitemClickListener;
        int lastPosition = -1;
        String specialization;
        String noOfAnimals;
        String protocol;
        String category_id;
        String sub_category_id;

        public DocListingMapAdapter(Context context1, ArrayList<MapGetterSetter> list, String specializationn, String NoOfAnimals, String Protocol, String Category_id, String Sub_category_id) {
            context = context1;
            infoList = list;
            specialization = specializationn;
            noOfAnimals = NoOfAnimals;
            protocol = Protocol;
            category_id = Category_id;
            sub_category_id = Sub_category_id;
        }

        public interface OnitemClickListener {
            void onItemClick(LinearLayout b, View v, MapGetterSetter obj, int position);
        }

        public void setOnitemClickListener(DocListingMapAdapter.OnitemClickListener onitemClickListener) {
            this.onitemClickListener = onitemClickListener;
        }

        @Override
        public DocListingMapAdapter.DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_doc_map_listing, null);
            return new DocListingMapAdapter.DataHolder(view);
        }



        @Override
        public void onBindViewHolder(DocListingMapAdapter.DataHolder dataHolder, int i) {
            final MapGetterSetter c = infoList.get(i);

            dataHolder.title.setText(c.getTitle());
            dataHolder.diploma.setText(c.getDiploma_type());
            dataHolder.distance.setText(c.getDistance());

            if (lastPosition > i) {

                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_to_left);
                dataHolder.itemView.startAnimation(animation);

            } else if (lastPosition < i) {

                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_to_right);
                dataHolder.itemView.startAnimation(animation);

            }

            /*Animation animation = AnimationUtils.loadAnimation(context,
                    (i > lastPosition) ? R.anim.slide_to_right : R.anim.slide_to_left);
            dataHolder.itemView.startAnimation(animation);*/

            lastPosition = i;

        }

    @Override
    public void onViewDetachedFromWindow(DataHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
        public int getItemCount() {
            return infoList.size();
        }

        public class DataHolder extends RecyclerView.ViewHolder {

            TextView title,diploma,distance;

            public DataHolder(View itemView) {
                super(itemView);

                title = (TextView) itemView.findViewById(R.id.tv_line_1);
                diploma = (TextView) itemView.findViewById(R.id.tv_line_2);
                distance = (TextView) itemView.findViewById(R.id.tv_line_3);

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    itemView.setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View view, DragEvent dragEvent) {

                            return false;
                        }
                    });
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemView.setOnScrollChangeListener( new RecyclerView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                        }
                    });
                }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context,""+infoList.get(getAdapterPosition()).getName(),Toast.LENGTH_LONG).show();

                    Intent myIntent = new Intent(context, VetDetail.class);
                    myIntent.putExtra("user_id",infoList.get(getAdapterPosition()).getUser_id());
                    myIntent.putExtra("name",infoList.get(getAdapterPosition()).getTitle());
                    myIntent.putExtra("diploma",infoList.get(getAdapterPosition()).getDiploma_type());
                    myIntent.putExtra("distance",infoList.get(getAdapterPosition()).getDistance());
                    myIntent.putExtra("specialization_id",specialization);
                    myIntent.putExtra("no_of_animals",noOfAnimals);
                    myIntent.putExtra("category_id",category_id);
                    myIntent.putExtra("sub_category_id",sub_category_id);
                    Log.e("TAG","purpose: " + protocol);
                    myIntent.putExtra("purpose",protocol);
                    context.startActivity(myIntent);

                }
            });

            }
        }

}
