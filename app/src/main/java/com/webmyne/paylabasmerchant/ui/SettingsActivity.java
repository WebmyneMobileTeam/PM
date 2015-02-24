package com.webmyne.paylabasmerchant.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.util.AppUtils;
import com.webmyne.paylabasmerchant.util.PrefUtils;

import java.util.Locale;


public class SettingsActivity extends ActionBarActivity {

    private static final String ARG_CONFERENCE_DAY_INDEX
            = "com.google.samples.apps.iosched.ARG_CONFERENCE_DAY_INDEX";

    Toolbar toolbar_actionbar;
    FrameLayout frame_container;
    TextView txtchangelanguagae;
    ImageView imgUS,imgFrance;
   boolean isEnglisSelected;

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
                Intent intent = new Intent(SettingsActivity.this, MyDrawerActivity.class);
                startActivity(intent);
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

        imgUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(SettingsActivity.this);
                if(isEnglisSelected){
                    showLanguageAlert("en");
                }

            }
        });

        imgFrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(SettingsActivity.this);
                if(!isEnglisSelected){
                    showLanguageAlert("fr");
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLanguage();
        imgUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(SettingsActivity.this);
                if(isEnglisSelected){
                    showLanguageAlert("en");
                }

            }
        });

        imgFrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEnglisSelected= PrefUtils.isEnglishSelected(SettingsActivity.this);
                if(!isEnglisSelected){
                    showLanguageAlert("fr");
                }

            }
        });
    }

    private void setLanguage() {

        isEnglisSelected= PrefUtils.isEnglishSelected(SettingsActivity.this);
        if(PrefUtils.isEnglishSelected(SettingsActivity.this)){
            imgUS.setColorFilter(Color.argb(128, 0, 0, 0));
            Configuration config = new Configuration();
            config.locale = Locale.FRANCE;
            getResources().updateConfiguration(config, null);
           /* etMerchantId.setHint("Merchant ID");
            etSecretId.setHint("Password");
            btnLoginNext.setText("NEXT");*/
            txtchangelanguagae.setText("Changer de langue");

        } else {
            imgFrance.setColorFilter(Color.argb(128, 0, 0, 0));
            Configuration config = new Configuration();
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, null);
            /*etMerchantId.setHint("Merchant ID");
            etSecretId.setHint("Password");
            btnLoginNext.setText("NEXT");*/

            txtchangelanguagae.setText("Change Language");


        }


       /* if(isLoggedIn(LoginActivity.this)){
            Intent intent =new Intent(LoginActivity.this,VerificationActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    private void showLanguageAlert(final String languageType){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Change Language");
        if(languageType.equalsIgnoreCase("en")){
            alert.setMessage("Are you sure, you want to change language to English");
        } else {
            alert.setMessage("Are you sure, yo want to change language to French");
        }
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if(languageType.equalsIgnoreCase("en")){

                    PrefUtils.setEnglishSelected(SettingsActivity.this, false);
                    imgUS.clearColorFilter();
                    imgFrance.setColorFilter(Color.argb(128, 0, 0, 0));


                } else {
                    PrefUtils.setEnglishSelected(SettingsActivity.this,true);
                    imgFrance.clearColorFilter();
                    imgUS.setColorFilter(Color.argb(128, 0, 0, 0));


                }
                changeLanguage(languageType);
                dialog.dismiss();

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();

    }

    private void changeLanguage(String languageType){

        if(languageType.equalsIgnoreCase("en")){
            Log.e("eng", "eng");
            Configuration config = new Configuration();
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, null);

            txtchangelanguagae.setText("Change Language");

        } else {
            Log.e("french","french");
            Configuration config = new Configuration();
            config.locale = Locale.FRANCE;
            getResources().updateConfiguration(config, null);

            txtchangelanguagae.setText("Changer de langue");
        }

    }
    
    
    //end of main classs
}
