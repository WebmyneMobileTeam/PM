package com.webmyne.paylabasmerchant.ui;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.ui.widget.SlidingTabLayout;
import com.webmyne.paylabasmerchant.util.AppUtils;
import com.webmyne.paylabasmerchant.util.PrefUtils;

public class CashInOutActivity extends ActionBarActivity {
    private static final String ARG_CONFERENCE_DAY_INDEX
            = "com.google.samples.apps.iosched.ARG_CONFERENCE_DAY_INDEX";

    ViewPager mViewPager = null;
    SlidingTabLayout mSlidingTabLayout = null;
    ToolsPagerAdapter mViewPagerAdapter = null;
    Toolbar toolbar_actionbar;
    private AffilateUser affilateUser;
    Boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);

        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle(getResources().getString(R.string.cashinout_title));
            toolbar_actionbar.setNavigationIcon(R.drawable.icon_back);
            setSupportActionBar(toolbar_actionbar);

        }

        toolbar_actionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.RIGHT);

        layoutParams.width = (int) AppUtils.convertDpToPixel(32, CashInOutActivity.this);
        layoutParams.height = (int) AppUtils.convertDpToPixel(32, CashInOutActivity.this);
        layoutParams.rightMargin = 16;
        /* setting up the toolbar ends*/

        affilateUser= PrefUtils.getMerchant(CashInOutActivity.this);
        // String str = affilateUser.affilateServicesArrayList.get(0).ServiceName.toString();
        // position 2 is for Cash in service, 0 - for generate gidt code, 1 - fro mobile top only
        isActive = affilateUser.affilateServicesArrayList.get(2).IsActive;

        intView();





    }

    private void intView(){
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        setSlidingTabLayoutContentDescriptions();

        mViewPagerAdapter = new ToolsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mViewPagerAdapter);

        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(res.getColor(R.color.tab_selected_strip));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    private void setSlidingTabLayoutContentDescriptions() {


        for (int i = 0; i < 2; i++) {
            mSlidingTabLayout.setContentDescription(i,getString(R.string.my_schedule_tab_desc_a11y, getTittle(i)));
        }
    }

    private String getTittle(int position) {
        if (position == 0) return getResources().getString(R.string.cashinout_tabstrip1);
        else if (position == 1) return getResources().getString(R.string.cashinout_tabstrip2);
        else return getResources().getString(R.string.cashinout_tabstrip2);
    }

    private class ToolsPagerAdapter extends FragmentPagerAdapter {
        public ToolsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(!isActive) {
                    FragmentCashOUT frag = new FragmentCashOUT();
                    return frag;
            }

            else{
                    if(position==0) {
                        FragmentCashIN frag = new FragmentCashIN();

                        return frag;
                    }
                    else if(position==1) {
                        FragmentCashOUT frag = new FragmentCashOUT();

                        return frag;
                    }
                    else{
                        FragmentCashIN frag = new FragmentCashIN();
                        return frag;
                    }
            }

        }

        @Override
        public int getCount() {

            int positionStart;
            Log.e("value of isactive - ",""+isActive);

            if(!isActive) {
                Log.e("inside if","return 1");
                return 1;
            }
            else {
                return 2;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {

            if(!isActive) {
               return getTittle(1);
            }
            else {
                return getTittle(position);
            }

        }
    }

}
