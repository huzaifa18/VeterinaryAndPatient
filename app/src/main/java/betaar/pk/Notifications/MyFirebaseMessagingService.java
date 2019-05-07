package betaar.pk.Notifications;

/**
 * Created by Huzaifa Asif on 14-Feb-18.
 */
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import betaar.pk.BillConfirmation;
import betaar.pk.ClientHistory;
import betaar.pk.DashboardClient;
import betaar.pk.DashboardVeterinarian;
import betaar.pk.Models.MedData;
import betaar.pk.Models.MedGroups;
import betaar.pk.Preferences.Prefs;
import betaar.pk.R;
import betaar.pk.RequestDetail;
import betaar.pk.Services.UpdateLatLong;
import betaar.pk.SplashScreen;
import betaar.pk.VisitDetail;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    JSONObject obj = null;
    String visit_id = null;
    String immediate_visit= null;
    String date_of_visit= null;
    String time_of_visit= null;
    String user_address= null;
    String user_lat= null;
    String vet_lat= null;
    String user_lng= null;
    String vet_lng= null;
    String reason_of_visit= null;
    String no_of_animals= null;
    String protocol= "No Protocol";
    String name= null;
    String phone= null;
    String category= null;
    String sub_category= null;
    String bill= null;
    ArrayList<MedGroups> group;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.e(TAG, "The message is " + remoteMessage.getData().get("title") );
        // Log.e(TAG, "From: " + remoteMessage.getFrom());
        //Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody().toString());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getData().get("body"));
        Log.e(TAG, "Notification Message type: " + remoteMessage.getData().get("type"));
        Log.e(TAG, "Notification Message detail: " + remoteMessage.getData().get("detail"));

        if (remoteMessage.getData().get("type").equals("request_received_to_vet")) {
            getVetDetail(remoteMessage);
        } else if (remoteMessage.getData().get("type").equals("vet_started_visit")) {
            getVetDetail(remoteMessage);
        } else if (remoteMessage.getData().get("type").equals("vet_cancelled_visit")) {
            getVetDetail(remoteMessage);
        } else if (remoteMessage.getData().get("type").equals("vet_completed_visit")){
            getVetDetail(remoteMessage);
        } else if (remoteMessage.getData().get("type").equals("confirm_finish_visit")){
            getVetDetail(remoteMessage);
        } else if (remoteMessage.getData().get("type").equals("visit_finished")){
            getVetDetail(remoteMessage);
        } else if (remoteMessage.getData().get("type").equals("admin_cancelled_visit")){
            getVetDetail(remoteMessage);
        }

    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String title, String messageBody, String type, String lat, String lng, String visit_id,
                                  String immediate_visit,String date_of_visit,String time_of_visit,String user_address,
                                  String reason_of_visit,String no_of_animals,String protocol,String name, String bill ) {

        PendingIntent pendingIntent;

        if (type.equals("request_received_to_vet")){

            Intent intent = new Intent(this, RequestDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            intent.putExtra("visit_id",visit_id);
            intent.putExtra("immediate_visit",immediate_visit);
            intent.putExtra("date_of_visit",date_of_visit);
            intent.putExtra("time_of_visit",time_of_visit);
            intent.putExtra("user_address",user_address);
            intent.putExtra("reason_of_visit",reason_of_visit);
            intent.putExtra("no_of_animals",no_of_animals);
            intent.putExtra("protocol",protocol);
            intent.putExtra("category",category);
            intent.putExtra("sub_category",sub_category);
            intent.putExtra("request_type","pending");
            intent.putExtra("type","sender");
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        } else if (type.equals("vet_started_visit")){

            Intent intent = new Intent(this, RequestDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("lat",vet_lat);
            intent.putExtra("lng",vet_lng);
            intent.putExtra("visit_id",visit_id);
            intent.putExtra("immediate_visit",immediate_visit);
            intent.putExtra("date_of_visit",date_of_visit);
            intent.putExtra("time_of_visit",time_of_visit);
            intent.putExtra("user_address",user_address);
            intent.putExtra("reason_of_visit",reason_of_visit);
            intent.putExtra("no_of_animals",no_of_animals);
            intent.putExtra("protocol",protocol);
            intent.putExtra("category",category);
            intent.putExtra("sub_category",sub_category);
            intent.putExtra("request_type","accepted");
            intent.putExtra("type","receiver");
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        } else if (type.equals("vet_cancelled_visit")){

            Intent intent = new Intent(this, RequestDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            intent.putExtra("visit_id",visit_id);
            intent.putExtra("immediate_visit",immediate_visit);
            intent.putExtra("date_of_visit",date_of_visit);
            intent.putExtra("time_of_visit",time_of_visit);
            intent.putExtra("user_address",user_address);
            intent.putExtra("reason_of_visit",reason_of_visit);
            intent.putExtra("no_of_animals",no_of_animals);
            intent.putExtra("protocol",protocol);
            intent.putExtra("category",category);
            intent.putExtra("sub_category",sub_category);
            intent.putExtra("request_type","cancelled");
            intent.putExtra("type","receiver");
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        } else if (type.equals("vet_completed_visit")){

            Intent intent = new Intent(this, RequestDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            intent.putExtra("visit_id",visit_id);
            intent.putExtra("immediate_visit",immediate_visit);
            intent.putExtra("date_of_visit",date_of_visit);
            intent.putExtra("time_of_visit",time_of_visit);
            intent.putExtra("user_address",user_address);
            intent.putExtra("reason_of_visit",reason_of_visit);
            intent.putExtra("no_of_animals",no_of_animals);
            intent.putExtra("protocol",protocol);
            intent.putExtra("category",category);
            intent.putExtra("sub_category",sub_category);
            intent.putExtra("request_type","completed");
            intent.putExtra("type","receiver");
            intent.putExtra("bill",bill);
            intent.putExtra("array",group);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        } else if (type.equals("confirm_finish_visit")){

            Intent intent = new Intent(this, VisitDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            intent.putExtra("visit_id",visit_id);
            intent.putExtra("immediate_visit",immediate_visit);
            intent.putExtra("date_of_visit",date_of_visit);
            intent.putExtra("time_of_visit",time_of_visit);
            intent.putExtra("user_address",user_address);
            intent.putExtra("reason_of_visit",reason_of_visit);
            intent.putExtra("no_of_animals",no_of_animals);
            intent.putExtra("protocol",protocol);
            intent.putExtra("category",category);
            intent.putExtra("sub_category",sub_category);
            intent.putExtra("request_type","completed");
            intent.putExtra("type","receiver");
            intent.putExtra("bill",bill);
            Log.e("TAG","Bill: " + bill);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        } else if (type.equals("visit_finished")){

            Intent intent = new Intent(this, VisitDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            intent.putExtra("visit_id",visit_id);
            intent.putExtra("immediate_visit",immediate_visit);
            intent.putExtra("date_of_visit",date_of_visit);
            intent.putExtra("time_of_visit",time_of_visit);
            intent.putExtra("user_address",user_address);
            intent.putExtra("reason_of_visit",reason_of_visit);
            intent.putExtra("no_of_animals",no_of_animals);
            intent.putExtra("protocol",protocol);
            intent.putExtra("category",category);
            intent.putExtra("sub_category",sub_category);
            intent.putExtra("request_type","finished");
            intent.putExtra("type","receiver");
            intent.putExtra("user_id", Prefs.getUserIDFromPref(this));
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        } else if (type.equals("admin_cancelled_visit")){

            Intent intent = new Intent(this, RequestDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            intent.putExtra("visit_id",visit_id);
            intent.putExtra("immediate_visit",immediate_visit);
            intent.putExtra("date_of_visit",date_of_visit);
            intent.putExtra("time_of_visit",time_of_visit);
            intent.putExtra("user_address",user_address);
            intent.putExtra("reason_of_visit",reason_of_visit);
            intent.putExtra("no_of_animals",no_of_animals);
            intent.putExtra("protocol",protocol);
            intent.putExtra("category",category);
            intent.putExtra("sub_category",sub_category);
            intent.putExtra("request_type","cancelled");
            intent.putExtra("type","receiver");
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        } else {

            Intent intent = new Intent(this, SplashScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);


        }

        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.ic_launcher);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                // .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(DEFAULT_VIBRATE_PATTERN)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void getVetDetail(RemoteMessage remoteMessage){

        try {

            Log.e(TAG, "Notification: " + remoteMessage.getData().toString());

            obj = new JSONObject(remoteMessage.getData().get("detail").toString());

            visit_id = obj.getString("visit_id");

            immediate_visit = obj.getString("immediate_visit");

            date_of_visit = obj.getString("date_of_visit");

            time_of_visit = obj.getString("time_of_visit");

            user_address = obj.getString("user_address");
            user_lat = obj.getString("user_lat");
            user_lng = obj.getString("user_lng");
            reason_of_visit = obj.getString("reason_of_visit");
            no_of_animals = obj.getString("no_of_animals");

            Log.e(TAG, "Test Okay ");

            String protocol_id = String.valueOf(obj.getInt("protocol_id"));
            String speciality_id;
            speciality_id = obj.getString("speciality_id");
            String visit_started_at;
            visit_started_at = obj.getString("visit_started_at");
            String visit_ended_at;
            visit_ended_at = obj.getString("visit_ended_at");
            String created_at;
            created_at = obj.getString("created_at");

            if (remoteMessage.getData().get("type").equals("request_received_to_vet")) {

                JSONObject obj1 = obj.getJSONObject("sender");

                name = obj1.getString("name");

                phone = obj1.getString("phone");

            } else if (remoteMessage.getData().get("type").equals("vet_completed_visit")){

                bill = obj.getString("bill");

            } else {

                JSONObject obj1 = obj.getJSONObject("receiver");

                name = obj1.getString("name");

                phone = obj1.getString("phone");

                vet_lat = obj1.getString("lat");

                vet_lng = obj1.getString("lng");

            }

            String status = obj.getString("status");

            if (remoteMessage.getData().get("type").equals("confirm_finish_visit") && status.equals("awaiting_for_client_bill_confirmation")){

                ArrayList<MedData> meds = new ArrayList<>();

                group = new ArrayList<>();

                bill = obj.getString("bill");

                JSONArray medArr = obj.getJSONArray("medicines");

                if (medArr.length() > 0) {

                    Log.e("Tag","Medicines");

                    String groupId = null;
                    String groupName = null;

                    for (int i = 0; i < medArr.length(); i++) {

                        JSONObject jObjmed = medArr.getJSONObject(i);

                        String med_id = jObjmed.getString("medicine_id");
                        String quantity = jObjmed.getString("quantity");
                        String unit_price = jObjmed.getString("unit_price");
                        String unit = jObjmed.getString("unit");
                        JSONObject medobj = jObjmed.getJSONObject("medicine");
                        String med_name = medobj.getString("name");

                        JSONObject medGroupObj = medobj.getJSONObject("group");
                        groupId = medGroupObj.getString("id");
                        groupName = medGroupObj.getString("name");

                        quantity = quantity + " " + unit;

                        meds.add(new MedData(med_id, med_name, groupId, unit_price, quantity));

                        group.add(new MedGroups(groupId, groupName, meds));

                    }

                } else {

                    Log.e("Tag","Semens");

                }

                /*Intent i = new Intent(this,BillConfirmation.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("bill",bill);
                i.putExtra("array",group);
                i.putExtra("visit_id",visit_id);
                //startActivity(i);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
                        PendingIntent.FLAG_ONE_SHOT);
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.ic_launcher);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        // .setLargeIcon(icon)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("body"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setVibrate(DEFAULT_VIBRATE_PATTERN)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, notificationBuilder.build());*/


            } else {

                /*Intent i = new Intent(this, ClientHistory.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("Type", "Current");
                //startActivity(i);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
                        PendingIntent.FLAG_ONE_SHOT);
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.ic_launcher);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        // .setLargeIcon(icon)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("body"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setVibrate(DEFAULT_VIBRATE_PATTERN)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, notificationBuilder.build());*/

            }

            Log.e(TAG, "Test NotOkay ");

            JSONObject obj2 = obj.getJSONObject("category");

            category = obj2.getString("name");

            JSONObject obj3 = obj.getJSONObject("sub_category");

            sub_category = obj3.getString("name");

            Log.e(TAG, "Check");

            if (protocol_id.equals("0")){

            } else {

                JSONObject obj4 = obj.getJSONObject("protocol");

                protocol = obj4.getString("type");

            }

            Log.e(TAG, "Notification Message detail Immidiate: " + immediate_visit);

            //Calling method to generate notification
            sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"),remoteMessage.getData().get("type"),
                    user_lat,user_lng,visit_id,immediate_visit,date_of_visit,time_of_visit,user_address,reason_of_visit,no_of_animals,protocol,
                    name,bill);

        } catch (JSONException e) {

            e.printStackTrace();

        }
    }

}