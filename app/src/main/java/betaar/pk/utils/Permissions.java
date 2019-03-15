package betaar.pk.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Permissions {

    public static boolean requestAppPermissions(Activity context) {
        boolean ispermissionGrated = false;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ispermissionGrated =  true;
        }

        if (hasReadPermissions(context) && hasWritePermissions(context)) {

            ispermissionGrated =  true;
            return ispermissionGrated;
        }

        ActivityCompat.requestPermissions(context,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 111); // your request code

        return ispermissionGrated;
    }

    public static boolean hasReadPermissions(Activity activity) {
        return (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean hasWritePermissions(Activity activity) {
        return (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
}
