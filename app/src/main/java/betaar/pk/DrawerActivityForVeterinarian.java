package betaar.pk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import betaar.pk.Config.API;
import betaar.pk.Preferences.Prefs;
import betaar.pk.Services.UpdateLatLong;

public class DrawerActivityForVeterinarian extends AppCompatActivity {

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

    View view;
    Toolbar toolbar;

    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_for_veterinarian);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fa = this;

        view = new View(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;
        mNavigationView.setItemIconTintList(null);

        // get menu from navigationView
        Menu menu = mNavigationView.getMenu();

        // find MenuItem you want to change
        navUsername = menu.findItem(R.id.nav_item_home);
        navUsername.setTitle(Prefs.getUserFullNameFromPref(getApplicationContext()).toString());
        navPaymentMethods = menu.findItem(R.id.nav_item_help);


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.nav_item_home) {
                    //home activity
                    //showDialogue();
                   startActivity(new Intent(DrawerActivityForVeterinarian.this, ProfileUpdateForVeterinary.class));
                    finish();

                }

                if (menuItem.getItemId() == R.id.nav_item_my_orders) {
                    //history
                    //showDialogue();
                    Intent i = new Intent(DrawerActivityForVeterinarian.this, CheckUp.class);
                    i.putExtra("Type","Veterinarian");
                    startActivity(i);
                    finish();

                }
                if (menuItem.getItemId() == R.id.nav_item_help){

                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.betaar.pk/terms-and-conditions"));
                    startActivity(viewIntent);
                    finish();
                    //startActivity(new Intent(DrawerActivityForVeterinarian.this, Help.class));
                    //showDialogue();

                }
                if (menuItem.getItemId() == R.id.nav_item_about){

                    Intent i = new Intent(DrawerActivityForVeterinarian.this, AboutUs.class);
                    startActivity(i);
                    finish();

                }
                if (menuItem.getItemId() == R.id.nav_item_contact_us){

                    Intent i = new Intent(DrawerActivityForVeterinarian.this, ContactUs.class);
                    startActivity(i);
                    finish();
                }

                if (menuItem.getItemId() == R.id.logout){

                    API.logoutService(DrawerActivityForVeterinarian.this);
                    //finish();

                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
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

        String fullName = Prefs.getUserFullNameFromPref(getApplicationContext());
        navUsername.setTitle(fullName);

    }


    private void showDialogue(){

        final Dialog dialog = new Dialog(DrawerActivityForVeterinarian.this);
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
                /*Intent i = new Intent(DrawerActivityForVeterinarian.this, DashboardVeterinarian.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
            }
        });


    }




}