package betaar.pk.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class Prefs {


    public static void addingUserUDID(Context context, final String userUDID){
        SharedPreferences userUDIDPref = context.getSharedPreferences("betaar_udid", 0);
        SharedPreferences.Editor editor = userUDIDPref.edit();
        editor.putString("user_udid", userUDID);
        editor.commit();

    }

    public static String gettUserUDID(Context context){

        SharedPreferences userUDIDPref = context.getSharedPreferences("betaar_udid", 0);
        String UDID = userUDIDPref.getString("user_udid", "-1");
        return UDID;
    }

    public static void addPrefsForLogin(Context context, final String user_id,
                                 final String name, final String username,
                                 final String  email, final String phone, final String role, final String password){

        SharedPreferences userLoginPref  = context.getSharedPreferences("betaar_user", 0);
        SharedPreferences.Editor editor = userLoginPref.edit();
        editor.putString("user_id", user_id);
        editor.putString("name", name);
        editor.putString("username", username);
        editor.putString("email", email);
        editor.putString("phone", phone);
        editor.putString("role", role);
        editor.putString("password", password);
        editor.commit();

    }

    //getting user id
    public static String getUserIDFromPref(Context context){
        SharedPreferences userLoginPref  = context.getSharedPreferences("betaar_user", 0);
        String userId = userLoginPref.getString("user_id", "");
        return userId;
    }

    //gettitng user role
    public static String getUserRoleFromPref(Context context){
        SharedPreferences userRolePref  = context.getSharedPreferences("betaar_user", 0);
        String userId = userRolePref.getString("role", "-1");
        return userId;
    }

    //gettitng full name
    public static String getUserFullNameFromPref(Context context){
        SharedPreferences preUserFullName  = context.getSharedPreferences("betaar_user", 0);
        String fullName = preUserFullName.getString("name", "-1");
        return fullName;
    }

    //gettitng user name
    public static String getUserNameFromPref(Context context){
        SharedPreferences preUserName  = context.getSharedPreferences("betaar_user", 0);
        String username = preUserName.getString("username", "-1");
        return username;
    }

    //gettitng user name
    public static String getEmailFromPref(Context context){
        SharedPreferences preUserEmail  = context.getSharedPreferences("betaar_user", 0);
        String email = preUserEmail.getString("email", "-1");
        return email;
    }

    //gettitng password
    public static String getPasswordFromPref(Context context){
        SharedPreferences prepassword  = context.getSharedPreferences("betaar_user", 0);
        String password = prepassword.getString("password", "-1");
        return password;
    }


    public static ArrayList<String> getAllUserValueFromPref(Context context){

        ArrayList<String> arrayList = new ArrayList<>();
        SharedPreferences userLoginPref  = context.getSharedPreferences("betaar_user", 0);
        String userId = userLoginPref.getString("user_id", "");
        String name = userLoginPref.getString("name", "");
        String username = userLoginPref.getString("username", "");
        String email = userLoginPref.getString("email", "");
        String phone = userLoginPref.getString("phone", "");
        String password = userLoginPref.getString("password", "");
        String role = userLoginPref.getString("role", "");
        arrayList.add(userId);
        arrayList.add(name);
        arrayList.add(username);
        arrayList.add(email);
        arrayList.add(phone);
        arrayList.add(password);
        arrayList.add(role);

        return arrayList;
    }

    public static void clearPrefData(Context context){
        SharedPreferences userLoginPref  = context.getSharedPreferences("betaar_user", 0);
        SharedPreferences.Editor editor = userLoginPref.edit();
        editor.clear();
        editor.commit();
    }
}
