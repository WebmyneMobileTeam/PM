package com.webmyne.paylabasmerchant.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.ComplexPreferences;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.DatabaseWrapper;


import org.json.JSONObject;

public class ConfirmRecipientActivity extends ActionBarActivity {


    private Toolbar toolbar;

    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;

    private ActionBarDrawerToggle drawerToggle;

    private EditText edVerificationCode;

    private TextView btnVerifyRecipient;

    private DatabaseWrapper db_wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_recipient);

        initView();

        if (toolbar != null) {
            toolbar.setTitle("Add Recipient");
            setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ConfirmRecipientActivity.this.finish();
            }
        });

        // fetching the verification code
       /* SharedPreferences preferences = getSharedPreferences("Recipient", MODE_PRIVATE);
        SnackBar bar = new SnackBar(ConfirmRecipientActivity.this, "Your Add Recipient Verification Code is "+ preferences.getString("VerificationCode","vfcode"));
        bar.show();*/

btnVerifyRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmptyField(edVerificationCode)) {
//                    SnackBar bar = new SnackBar(ConfirmRecipientActivity.this,"Please Enter Verification Code");
//                    bar.show();
                    SimpleToast.error(ConfirmRecipientActivity.this,"Please Enter Verification Code");
                }
                else if (checkVerificationcode(edVerificationCode)) {
//                    SnackBar bar = new SnackBar(ConfirmRecipientActivity.this,"Please Enter Correct Verification Code");
//                    bar.show();
                    SimpleToast.error(ConfirmRecipientActivity.this,"Please Enter Correct Verification Code");
                }
                else {
                      processAddRecipient();
                }
            }
        });

        // end of main class
    }

public void processAddRecipient(){


    try {
        ComplexPreferences complexPreferences2 = ComplexPreferences.getComplexPreferences(ConfirmRecipientActivity.this, "user_pref", 0);
        JSONObject userObject =  complexPreferences2.getObject("new-recipient", JSONObject.class);

        Log.e("User id ",String.valueOf(userObject.toString()));

        Log.e("json obj",userObject.toString());
        final CircleDialog circleDialog = new CircleDialog(ConfirmRecipientActivity.this, 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.ADD_RECIPIENT, userObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("Response Addrecipient: ", "" + response);

                try{

                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

                       // User currentUser = new GsonBuilder().create().fromJson(response,User.class);
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ConfirmRecipientActivity.this, "user_pref",0);
                        complexPreferences.remove("new-recipient");
                        complexPreferences.commit();

                        SharedPreferences preferences = getSharedPreferences("Recipient", MODE_PRIVATE);
                        preferences.edit().remove("VerificationCode").commit();

//                        SnackBar bar = new SnackBar(ConfirmRecipientActivity.this,"Recipient Added Sucessfully");
//                        bar.show();
                        SimpleToast.ok(ConfirmRecipientActivity.this,"Recipient Added Sucessfully");

                        Intent verifyRecipient = new Intent( ConfirmRecipientActivity.this ,MyDrawerActivity.class );
                        startActivity(verifyRecipient);
                        finish();
                    }

                    else {
                        if(obj.getString("ResponseCode").equalsIgnoreCase("-2")) {
//                            SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this, "Error occur ");
//                            bar112.show();
                            SimpleToast.error(ConfirmRecipientActivity.this,"Error occur ");
                        }
                        else if(obj.getString("ResponseCode").equalsIgnoreCase("-1")) {
//                            SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this, "Error Occur While adding New Recipient");
//                            bar112.show();
                            SimpleToast.error(ConfirmRecipientActivity.this,"Error Occur While adding New Recipient");
                        }
                        else if(obj.getString("ResponseCode").equalsIgnoreCase("2")) {
//                            SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this, "Mobile No.   already Exist");
//                            bar112.show();
                            SimpleToast.error(ConfirmRecipientActivity.this,"Mobile No.   already Exist");
                        }
                        else if(obj.getString("ResponseCode").equalsIgnoreCase("3")) {
//                            SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this, "Email Id already Exist");
//                            bar112.show();
                            SimpleToast.error(ConfirmRecipientActivity.this,"Email Id already Exist");
                        }
                        else if(obj.getString("ResponseCode").equalsIgnoreCase("4")) {
//                            SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this, "Mobile No. & Email Id already Exist");
//                            bar112.show();
                            SimpleToast.error(ConfirmRecipientActivity.this,"Mobile No. & Email Id already Exist");
                        }
                        else{
//                            SnackBar bar112 = new SnackBar(ConfirmRecipientActivity.this, "Time out, Please Try again.");
//                            bar112.show();
                            SimpleToast.error(ConfirmRecipientActivity.this,"Time out, Please Try again.");
                        }

                    }

                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();
                Log.e("error Addrecipeint: ", error + "");
//                SnackBar bar = new SnackBar(ConfirmRecipientActivity.this, error.getMessage());
//                bar.show();
                SimpleToast.error(ConfirmRecipientActivity.this,error.getMessage());

            }
        });
        MyApplication.getInstance().addToRequestQueue(req);


    } catch (Exception e) {

    }
}



    public boolean checkVerificationcode(EditText param1){
    SharedPreferences preferences = getSharedPreferences("Recipient", MODE_PRIVATE);
    boolean isWrong = false;
    if(!param1.getText().toString().equals(preferences.getString("VerificationCode","vfcode"))){
        isWrong = true;
    }
    return isWrong;
    }
public boolean isEmptyField(EditText param1){
        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }
private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#494949"));
        toolbar.setNavigationIcon(R.drawable.icon_back);


        btnVerifyRecipient = (TextView)findViewById(R.id.btnVerifyRecipient);
        edVerificationCode = (EditText)findViewById(R.id.edVerificationCode);

    }

    public void setToolColor(int color){
        toolbar.setBackgroundColor(color);
    }

    public void setToolTitle(String title){
        toolbar.setTitle(title);
    }
    public void setToolSubTitle(String subTitle){

        toolbar.setSubtitle(subTitle);
    }
}