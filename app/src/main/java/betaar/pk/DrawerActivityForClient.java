package betaar.pk;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import betaar.pk.Adapters.SpinnerListingAdapter;
import betaar.pk.Config.API;
import betaar.pk.Preferences.Prefs;
import betaar.pk.Services.UpdateLatLong;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class DrawerActivityForClient extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    MenuItem navLoginRegister;
    MenuItem navUsername;
    MenuItem navPaymentMethods;
    // MenuItem navChosePlane;
    // MenuItem navFranchiser;
    MenuItem navContactUs;
    MenuItem navViewYourProperties;
    MenuItem navBuyerActvity;
    MenuItem navShowFranchiserList;
    MenuItem navTermsAndCondtion;
    MenuItem navShareApp;
    MenuItem navLiveSupport;
    MenuItem navHelp;
    MenuItem navChatRoom;

    Spinner languageSpinner;

    View view;

    boolean bool = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_for_client);

        view = new View(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        mNavigationView.setItemIconTintList(null);

        // get menu from navigationView
        Menu menu = mNavigationView.getMenu();

        // find MenuItem you want to change
        navUsername = menu.findItem(R.id.nav_item_home);
        navUsername.setTitle(Prefs.getUserFullNameFromPref(getApplicationContext()).toString());
        navPaymentMethods = menu.findItem(R.id.nav_item_help);
        languageSpinner = (Spinner) mDrawerLayout.findViewById(R.id.sp_language);

        SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.languages);
        languageSpinner.setAdapter(subCategory);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), DashboardClient.class);

                if (position == 1) {

                    Log.e("Lang", "English");
                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    bool = false;
                    intent.putExtra("lang", bool);
                    finish();
                    startActivity(intent);

                } else if (position == 2) {

                    Log.e("Lang", "Urdu");
                    Locale locale = new Locale("ur");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    bool = true;
                    intent.putExtra("lang", bool);
                    finish();
                    startActivity(intent);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.nav_item_home) {
                    //home activity
                    //Toast.makeText(getApplicationContext(), "DashBoard", Toast.LENGTH_SHORT).show();
                    showDialogue();

                }

                if (menuItem.getItemId() == R.id.nav_item_my_products) {
                    //my products activity
                    //Toast.makeText(getApplicationContext(), "DashBoard", Toast.LENGTH_SHORT).show();
                    //showDialogue();

                    Intent i = new Intent(DrawerActivityForClient.this, OrganizationMyProducts.class);
                    i.putExtra("Type", "Client");
                    startActivity(i);

                }

                if (menuItem.getItemId() == R.id.nav_item_current_visit) {

                    //current_visit
                    //startActivity(new Intent(DrawerActivityForClient.this, ClientHistory.class));
                    Intent i = new Intent(DrawerActivityForClient.this, ClientHistory.class);
                    i.putExtra("Type", "Current");
                    startActivity(i);

                    //showDialogue();

                }

                if (menuItem.getItemId() == R.id.nav_item_my_orders) {

                    //history
                    //startActivity(new Intent(DrawerActivityForClient.this, ClientHistory.class));
                    Intent i = new Intent(DrawerActivityForClient.this, ClientHistory.class);
                    i.putExtra("Type", "History");
                    startActivity(i);
                    //showDialogue();

                }
                if (menuItem.getItemId() == R.id.nav_item_help) {

                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.betaar.pk/terms-and-conditions"));
                    startActivity(viewIntent);
                    //startActivity(new Intent(DrawerActivityForClient.this, Help.class));
                    //showDialogue();

                }
                if (menuItem.getItemId() == R.id.nav_item_about) {

                    Intent i = new Intent(DrawerActivityForClient.this, AboutUs.class);
                    startActivity(i);

                }
                if (menuItem.getItemId() == R.id.nav_item_contact_us) {

                    Intent i = new Intent(DrawerActivityForClient.this, ContactUs.class);
                    startActivity(i);
                }

                if (menuItem.getItemId() == R.id.logout) {

                    API.logoutService(DrawerActivityForClient.this);
                    finish();

                }

             /*   if (menuItem.getItemId() == R.id.feedback){

                    Intent i = new Intent(BaseActvitvityForDrawer.this, Feedback.class);
                    startActivity(i);

                }*/

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();


    }//end on Create

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String fullname = Prefs.getUserFullNameFromPref(DrawerActivityForClient.this);
        navUsername.setTitle(fullname);

    }


    private void showDialogue() {

        final Dialog dialog = new Dialog(DrawerActivityForClient.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_client_verfication);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(false);
        dialog.show();

        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);

        tv_dialog_title.setText("Sorry for inconvenience!");
        tv_messasge.setText("This service is not available yet. Please wait for the next Update.");

        final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                /*Intent i = new Intent(DrawerActivityForClient.this, DashboardClient.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
            }
        });


    }


}