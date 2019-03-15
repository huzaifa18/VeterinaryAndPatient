package betaar.pk.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import betaar.pk.R;

public class CustomProgressDialog extends Dialog {

    private ImageView imageview;
    Animation rotate;

    public CustomProgressDialog(Context context, int resourceIdOfImage) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams windowmanger = getWindow().getAttributes();
        windowmanger .gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(windowmanger );
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(150, 150);
        imageview = new ImageView(context);
        imageview.setBackgroundResource(R.drawable.progress_animation);
        layout.addView(imageview, params);
        addContentView(layout, params);
        rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
    }

    @Override
    public void show() {
        super.show();
        AnimationDrawable frameAnimation = (AnimationDrawable)imageview.getBackground();
        frameAnimation.start();
        imageview.setAnimation(rotate);

    }

}

