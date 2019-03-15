package betaar.pk.Notifications;


/**
 * Created by Huzaifa Asif on 14-Feb-18.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import betaar.pk.Preferences.Prefs;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        //Displaying token on logcat
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        Prefs.addingUserUDID(getApplicationContext(), refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }

}
