package com.webmyne.paylabasmerchant.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.ComplexPreferences;

import org.json.JSONObject;


public class Login extends ActionBarActivity {

    private TextView btnLoginNext;
    private EditText etMerchantId,etSecretId;
    private CircleDialog circleDialog;
    private AffilateUser affilateUser;
    private boolean isLogin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // overridePendingTransition(R.anim.entry,R.anim.exit);
        setContentView(R.layout.activity_launcher);
       initView();
    }

    private void initView(){
        etMerchantId= (EditText) findViewById(R.id.etMerchantId);
        etSecretId= (EditText) findViewById(R.id.etSecretId);
        btnLoginNext= (TextView) findViewById(R.id.btnLoginNext);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogin=false;
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        isLogin=preferences.getBoolean("isUserLogin",false);
        if(isLogin==true){
            Intent intent =new Intent(Login.this,VerificationActivity.class);
            startActivity(intent);
            finish();
        }
        etMerchantId.setText("4CF5B52A19");
        etSecretId.setText("123456");
        btnLoginNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMerchantEmpty() || isSecretIdEmpty()){
//                    SnackBar bar = new SnackBar(Login.this,"Please enter merchant id and secret id");
//                    bar.show();
                    Toast.makeText(Login.this,"Please enter merchant id and Password",Toast.LENGTH_SHORT).show();
                } else {
                    checkMerchentLogin();
                }

            }
        });
    }

    public boolean isMerchantEmpty(){

        boolean isEmpty = false;

        if(etMerchantId.getText() == null || etMerchantId.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isSecretIdEmpty(){

        boolean isEmpty = false;

        if(etSecretId.getText() == null || etSecretId.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.entry,R.anim.exit);
    }


    private void checkMerchentLogin() {

        circleDialog = new CircleDialog(Login.this, 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("MerchantID", etMerchantId.getText().toString().trim() + "");
            requestObject.put("Password", etSecretId.getText().toString().trim() + "");
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.AFFILATE_LOGIN, requestObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                circleDialog.dismiss();
                Log.e("response: ",jobj.toString()+"");
                affilateUser = new GsonBuilder().create().fromJson(jobj.toString(), AffilateUser.class);
                if(affilateUser.ResponseCode.equalsIgnoreCase("1")){
                    //store current user and domain in shared preferences
                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(Login.this, "user_pref", 0);
                    complexPreferences.putObject("current_user", affilateUser);
                    complexPreferences.commit();

                    // set login true

                    SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isUserLogin",true);
                    editor.commit();

                    Intent intent =new Intent(Login.this,VerificationActivity.class);
                    startActivity(intent);
                    finish();

                    } else {


//                    SnackBar bar = new SnackBar(Login.this, "Network Error\n" +
//                            "Please try again");
//                    bar.show();
                    Toast.makeText(Login.this,"Network Error\n" +
                            "Please try again",Toast.LENGTH_SHORT).show();
                    }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();



            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(0,0,0));

        MyApplication.getInstance().addToRequestQueue(req);

    }

}
