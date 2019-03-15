package betaar.pk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VetDetail extends AppCompatActivity {

    RelativeLayout request_btn;
    TextView tv_name;
    TextView tv_diploma;
    TextView tv_distance;
    TextView tv_specialization;
    TextView tv_noOfAnimals;
    TextView tv_protocol;
    TextView tv_category;
    TextView tv_sub_category;

    String user_id;
    String name;
    String diploma;
    String distance;
    String specialization_id;
    String noOfAnimals;
    String protocol;
    String category_id;
    String sub_category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_detail);

        user_id = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");
        diploma = getIntent().getStringExtra("diploma");
        distance = getIntent().getStringExtra("distance");
        specialization_id = getIntent().getStringExtra("specialization_id");
        noOfAnimals = getIntent().getStringExtra("no_of_animals");
        protocol = getIntent().getStringExtra("purpose");
        category_id = getIntent().getStringExtra("category_id");
        sub_category_id = getIntent().getStringExtra("sub_category_id");

        Log.e("VetDetails","User_Id: " + user_id +
                "\n name: " + name +
                "\n diploma: " + diploma +
                "\n distance: " + distance +
                "\n specialization_id: " + specialization_id +
                "\n noOfAnimals: " + noOfAnimals +
                "\n protocol: " + protocol +
                "\n category_id: " + category_id +
                "\n sub_category_id: " + sub_category_id
        );

        initialize();

    }

    private void initialize() {

        request_btn = (RelativeLayout) findViewById(R.id.rl_bt_request);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_diploma = (TextView) findViewById(R.id.tv_diploma);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_specialization = (TextView) findViewById(R.id.tv_specialization);
        tv_noOfAnimals = (TextView) findViewById(R.id.tv_no_of_animals);
        tv_protocol = (TextView) findViewById(R.id.tv_protocol);
        tv_category = (TextView) findViewById(R.id.tv_category);
        tv_sub_category = (TextView) findViewById(R.id.tv_sub_category);

        tv_name.setText(name);
        tv_diploma.setText(diploma);
        tv_distance.setText(distance);
        tv_specialization.setText(Arrays.specialities.get(Integer.parseInt(specialization_id)).getName());
        tv_noOfAnimals.setText(noOfAnimals);
        tv_protocol.setText(protocol);
        tv_category.setText(Arrays.product_types.get(Integer.parseInt(category_id)).getName());

        if (category_id.equals("1")){

            tv_sub_category.setText(Arrays.dairy_categories.get(Integer.parseInt(sub_category_id)).getName());

        } else if (category_id.equals("2")){

            tv_sub_category.setText(Arrays.getName(Arrays.pets_categories,sub_category_id));

        } else if (category_id.equals("3")){

            tv_sub_category.setText(Arrays.getName(Arrays.equine_categories,sub_category_id));

        } else if (category_id.equals("4")){

            tv_sub_category.setText(Arrays.getName(Arrays.birds_categories,sub_category_id));

        } else if (category_id.equals("5")){

            tv_sub_category.setText(Arrays.getName(Arrays.other_categories,sub_category_id));

        }

        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(VetDetail.this,RequestForm.class);
                i.putExtra("user_id_reciever",user_id);
                i.putExtra("speciality_id",specialization_id);
                i.putExtra("no_of_animals",noOfAnimals);
                i.putExtra("protocol",protocol);
                i.putExtra("category_id",category_id);
                i.putExtra("sub_category_id",sub_category_id);
                startActivity(i);

                Log.e("TEG","Protocol: " + protocol);

            }
        });

    }
}
