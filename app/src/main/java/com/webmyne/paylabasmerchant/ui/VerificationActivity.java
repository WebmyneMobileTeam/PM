package com.webmyne.paylabasmerchant.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.ui.widget.ComplexPreferences;


public class VerificationActivity extends ActionBarActivity {

    private TextView btnFinishSetup,btnNewLogin;
    private AffilateUser affilateUser;
    private boolean isVerify=false;
    private EditText etVerificationCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // overridePendingTransition(R.anim.entry,R.anim.exit);
        setContentView(R.layout.activity_verification);
        etVerificationCode= (EditText) findViewById(R.id.etVerificationCode);
        btnNewLogin= (TextView) findViewById(R.id.btnNewLogin);

        btnFinishSetup= (TextView) findViewById(R.id.btnFinishSetup);
        btnFinishSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVerificationEmpty()){
//                    SnackBar bar = new SnackBar(VerificationActivity.this,"Please enter verification code");
//                    bar.show();
                    Toast.makeText(VerificationActivity.this, "Please enter verification code", Toast.LENGTH_SHORT).show();
                } else {



                    if(affilateUser.VerificationCode.toString().equalsIgnoreCase(etVerificationCode.getText().toString().trim())) {

                        SharedPreferences preferences = getSharedPreferences("verify", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isUserVerify",true);
                        editor.commit();

                        Intent intent =new Intent(VerificationActivity.this,MyDrawerActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
//                        SnackBar bar = new SnackBar(VerificationActivity.this,"Please enter valid verification code");
//                        bar.show();
                        Toast.makeText(VerificationActivity.this, "Please enter valid verification code", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnNewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                preferences.edit().remove("isUserLogin").commit();
                SharedPreferences preferences_verify = getSharedPreferences("verify", MODE_PRIVATE);
                preferences_verify.edit().remove("isUserVerify").commit();
                Intent in = new Intent(VerificationActivity.this, Login.class);
                startActivity(in);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(VerificationActivity.this, "user_pref", 0);
        affilateUser = complexPreferences.getObject("current_user",AffilateUser.class);
        etVerificationCode.setText(affilateUser.VerificationCode.toString().trim());

        isVerify=false;
        SharedPreferences preferences_value = getSharedPreferences("verify", MODE_PRIVATE);
        isVerify=preferences_value.getBoolean("isUserVerify",false);
        if(isVerify){
            Intent intent =new Intent(VerificationActivity.this,MyDrawerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.entry,R.anim.exit);
    }

    public boolean isVerificationEmpty(){

        boolean isEmpty = false;

        if(etVerificationCode.getText() == null || etVerificationCode.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }
}
