package betaar.pk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class SignUpHelp extends AppCompatActivity {

    TextView tv_helpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_help);

        init();
    }

    private void init() {

        tv_helpText = (TextView) findViewById(R.id.tv_helpText);

        tv_helpText.setText(Html.fromHtml(getString(R.string.signup_help)));

    }

}
