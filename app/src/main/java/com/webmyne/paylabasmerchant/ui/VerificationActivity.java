package com.webmyne.paylabasmerchant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.util.LogUtils;
import com.webmyne.paylabasmerchant.util.PrefUtils;


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
                    Toast.makeText(VerificationActivity.this, "Please enter verification code", Toast.LENGTH_SHORT).show();
                } else {
                    if(affilateUser.VerificationCode.toString().equalsIgnoreCase(etVerificationCode.getText().toString().trim())) {

                        PrefUtils.setVerified(VerificationActivity.this, true);

                        Intent intent =new Intent(VerificationActivity.this,MyDrawerActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(VerificationActivity.this, "Please enter valid verification code", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnNewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PrefUtils.clearLogin(VerificationActivity.this);
                PrefUtils.clearVerify(VerificationActivity.this);

                Intent in = new Intent(VerificationActivity.this, LoginActivity.class);
                startActivity(in);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        affilateUser = PrefUtils.getMerchant(VerificationActivity.this);
        etVerificationCode.setText(affilateUser.VerificationCode.toString().trim());
        isVerify=PrefUtils.isVerified(VerificationActivity.this);
        if(isVerify){
            Intent intent =new Intent(VerificationActivity.this,MyDrawerActivity.class);
            startActivity(intent);
            finish();
        }

        SmsRadar.initializeSmsRadarService(VerificationActivity.this, new SmsListener() {
            @Override
            public void onSmsSent(Sms sms) {

//                Toast.makeText(VerificationActivity.this,sms.getAddress()+"\n"+sms.getMsg()+"\n"+sms.getDate()+"\n"+sms.getType(), Toast.LENGTH_SHORT).show();
//                Log.e("sms",sms.getAddress()+"\n"+sms.getMsg()+"\n"+sms.getDate()+"\n"+sms.getType()+"");
                LogUtils.LOGE("sms",sms.getAddress()+"\n"+sms.getMsg()+"\n"+sms.getDate()+"\n"+sms.getType()+"");
            }

            @Override
            public void onSmsReceived(Sms sms) {
//                Toast.makeText(VerificationActivity.this, sms.getAddress()+"\n"+sms.getMsg()+"\n"+sms.getDate()+"\n"+sms.getType(), Toast.LENGTH_SHORT).show();
//                Log.e("sms",sms.getAddress()+"\n"+sms.getMsg()+"\n"+sms.getDate()+"\n"+sms.getType()+"");
                LogUtils.LOGE("sms",sms.getAddress()+"\n"+sms.getMsg()+"\n"+sms.getDate()+"\n"+sms.getType()+"");
                if(sms.getMsg().toString().contains(affilateUser.VerificationCode.toString())){
                    etVerificationCode.setText(affilateUser.VerificationCode.toString().trim());
                    PrefUtils.setVerified(VerificationActivity.this, true);
                    Intent intent =new Intent(VerificationActivity.this,MyDrawerActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(R.anim.entry,R.anim.exit);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SmsRadar.stopSmsRadarService(VerificationActivity.this);
    }

    public boolean isVerificationEmpty(){

        boolean isEmpty = false;

        if(etVerificationCode.getText() == null || etVerificationCode.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }
}
