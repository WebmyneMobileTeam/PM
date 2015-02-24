package com.webmyne.paylabasmerchant.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.util.AppUtils;


public class SettingsActivity extends ActionBarActivity {

    private static final String ARG_CONFERENCE_DAY_INDEX
            = "com.google.samples.apps.iosched.ARG_CONFERENCE_DAY_INDEX";

    Toolbar toolbar_actionbar;
    FrameLayout frame_container;
    TextView txtchangelanguagae;
    ImageView imgUS,imgFrance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);

        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle(getString(R.string.code_TITLESETTINGS));
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

        layoutParams.width = (int) AppUtils.convertDpToPixel(32, SettingsActivity.this);
        layoutParams.height = (int) AppUtils.convertDpToPixel(32, SettingsActivity.this);
        layoutParams.rightMargin = 16;
        /* setting up the toolbar ends*/

        intView();
    }

    private void intView(){
        frame_container = (FrameLayout)findViewById(R.id.frame_container);
        txtchangelanguagae = (TextView)findViewById(R.id.txtchangelanguagae);
        imgUS= (ImageView)findViewById(R.id.imgUS);
        imgFrance= (ImageView)findViewById(R.id.imgFrance);

    }


}
