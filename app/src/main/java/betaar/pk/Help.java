package betaar.pk;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        /*Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.betaar.pk/terms-and-conditions"));
        startActivity(viewIntent);*/
    }
}
