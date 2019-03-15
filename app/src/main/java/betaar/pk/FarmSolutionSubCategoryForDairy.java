package betaar.pk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class FarmSolutionSubCategoryForDairy extends DrawerActivityForClient {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_farm_solution_sub_category_for_dairy);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_farm_solution_sub_category_for_dairy, null, false);
        mDrawerLayout.addView(contentView, 0);

    }
}
