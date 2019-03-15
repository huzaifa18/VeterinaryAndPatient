package betaar.pk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import betaar.pk.Fragments.BasicInfoFragment;
import betaar.pk.Fragments.PaymentInfoFragment;
import betaar.pk.Fragments.ShippingInfoFragment;

public class OrderNow extends AppCompatActivity {

    public static ViewPager viewPager;
    RelativeLayout tab_rl_info, tab_rl_shipping, tab_rl_payment;
    android.support.v7.widget.AppCompatImageView tab_iv_info, tab_iv_shipping, tab_iv_payment;
    TextView tab_tv_info, tab_tv_shipping, tab_tv_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_now);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();
        onInfoTabClickListener();
        onShippingTabClickListener();
        onPaymentTabClickListener();
        viewPagerTabChangeListner();

    }

    private void init() {

        tab_rl_info = (RelativeLayout) findViewById(R.id.tab_rl_info);
        tab_rl_shipping = (RelativeLayout) findViewById(R.id.tab_rl_shipping);
        tab_rl_payment = (RelativeLayout) findViewById(R.id.tab_rl_payment);

        tab_iv_info = (android.support.v7.widget.AppCompatImageView) findViewById(R.id.tab_iv_info);
        tab_iv_shipping = (android.support.v7.widget.AppCompatImageView) findViewById(R.id.tab_iv_shipping);
        tab_iv_payment = (android.support.v7.widget.AppCompatImageView) findViewById(R.id.tab_iv_payment);

        tab_tv_info = (TextView) findViewById(R.id.tab_tv_info);
        tab_tv_shipping = (TextView) findViewById(R.id.tab_tv_shipping);
        tab_tv_payment = (TextView) findViewById(R.id.tab_tv_payment);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);

        tab_rl_info.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
        tab_tv_info.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        tab_iv_info.setImageResource(R.drawable.ic_dairyblue);
        viewPager.setCurrentItem(0);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new BasicInfoFragment(), "Info");
        adapter.addFragment(new ShippingInfoFragment(), "Shipping");
        adapter.addFragment(new PaymentInfoFragment(), "Payment");
        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void onInfoTabClickListener() {

        tab_rl_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_rl_info.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                tab_tv_info.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                tab_iv_info.setImageResource(R.drawable.ic_dairyblue);

                tab_rl_shipping.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_shipping.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tab_iv_shipping.setImageResource(R.drawable.ic_pets);

                tab_rl_payment.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_payment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tab_iv_payment.setImageResource(R.drawable.ic_equine);


                viewPager.setCurrentItem(0);

            }
        });
    }

    private void onShippingTabClickListener() {

        tab_rl_shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_rl_info.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_info.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tab_iv_info.setImageResource(R.drawable.ic_dairy);

                tab_rl_shipping.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                tab_tv_shipping.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                tab_iv_shipping.setImageResource(R.drawable.ic_petsblue);

                tab_rl_payment.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_shipping.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tab_iv_shipping.setImageResource(R.drawable.ic_equine);


                viewPager.setCurrentItem(1);

            }
        });
    }

    private void onPaymentTabClickListener() {

        tab_rl_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tab_rl_info.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_info.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tab_iv_info.setImageResource(R.drawable.ic_dairy);

                tab_rl_shipping.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_shipping.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tab_iv_shipping.setImageResource(R.drawable.ic_pets);

                tab_rl_payment.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                tab_tv_payment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                tab_iv_payment.setImageResource(R.drawable.ic_equineblue);

                viewPager.setCurrentItem(2);

            }
        });
    }

    private void viewPagerTabChangeListner() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Log.e("TAG", "the selected page position is: " + position);

                switch (position){

                    case 0:
                        //if (position == 0){
                        tab_rl_info.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                        tab_tv_info.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tab_iv_info.setImageResource(R.drawable.ic_dairyblue);

                        tab_rl_shipping.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_shipping.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_shipping.setImageResource(R.drawable.ic_pets);

                        tab_rl_payment.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_payment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_payment.setImageResource(R.drawable.ic_equine);

                        break;
                    //}
                    case 1:
                        //if (position == 1){
                        tab_rl_info.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_info.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_info.setImageResource(R.drawable.ic_dairy);

                        tab_rl_shipping.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                        tab_tv_shipping.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tab_iv_shipping.setImageResource(R.drawable.ic_petsblue);

                        tab_rl_payment.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_payment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_payment.setImageResource(R.drawable.ic_equine);

                        break;

                    //}
                    case 2:
                        //if (position == 2){
                        tab_rl_info.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_info.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_info.setImageResource(R.drawable.ic_dairy);

                        tab_rl_shipping.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_shipping.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_shipping.setImageResource(R.drawable.ic_pets);

                        tab_rl_payment.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                        tab_tv_payment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        tab_iv_payment.setImageResource(R.drawable.ic_equineblue);

                        break;
                    //}

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}