package com.webmyne.paylabasmerchant.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.ui.widget.SlidingTabLayout;
import com.webmyne.paylabasmerchant.util.AppUtils;


public class ReportsActivity extends ActionBarActivity {

    private static final String ARG_CONFERENCE_DAY_INDEX
            = "com.google.samples.apps.iosched.ARG_CONFERENCE_DAY_INDEX";

    Toolbar toolbar_actionbar;
    FrameLayout frame_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);

        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle(getResources().getString(R.string.reports_title));
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

        layoutParams.width = (int) AppUtils.convertDpToPixel(32, ReportsActivity.this);
        layoutParams.height = (int) AppUtils.convertDpToPixel(32, ReportsActivity.this);
        layoutParams.rightMargin = 16;
        /* setting up the toolbar ends*/

        intView();
    }

    private void intView(){
        frame_container = (FrameLayout)findViewById(R.id.frame_container);

    }


}
