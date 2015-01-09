package com.webmyne.paylabasmerchant.ui;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.content.res.Resources;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.ui.widget.SlidingTabLayout;
import com.webmyne.paylabasmerchant.util.AppUtils;


public class ToolsActivity extends ActionBarActivity {

    private static final String ARG_CONFERENCE_DAY_INDEX
            = "com.google.samples.apps.iosched.ARG_CONFERENCE_DAY_INDEX";

    ViewPager mViewPager = null;
    SlidingTabLayout mSlidingTabLayout = null;
    ToolsPagerAdapter mViewPagerAdapter = null;
    Toolbar toolbar_actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);

        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle(getResources().getString(R.string.tools_title));
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

        layoutParams.width = (int) AppUtils.convertDpToPixel(32, ToolsActivity.this);
        layoutParams.height = (int) AppUtils.convertDpToPixel(32, ToolsActivity.this);
        layoutParams.rightMargin = 16;
        /* setting up the toolbar ends*/

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
        if (position == 0) return getResources().getString(R.string.tabstrip_item1);
        else return getResources().getString(R.string.tabstrip_item2);
    }


private class ToolsPagerAdapter extends FragmentPagerAdapter {

        public ToolsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

         if(position==0) {
             CurrencyCoverterFragment frag = new CurrencyCoverterFragment();
           /*  Bundle args = new Bundle();
             args.putInt(ARG_CONFERENCE_DAY_INDEX, position);
             frag.setArguments(args);*/

             return frag;
         }
          else{
               CombineGCFragment frag1 = new CombineGCFragment();
              /* Bundle args1 = new Bundle();
               args1.putInt(ARG_CONFERENCE_DAY_INDEX, position);
               frag1.setArguments(args1);*/
               return frag1;
             }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTittle(position);
        }
    }

}
