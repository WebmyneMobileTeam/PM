package com.webmyne.paylabasmerchant.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.Receipient;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.PrefUtils;

import org.json.JSONObject;

import java.util.ArrayList;


public class MoneyTransferFinalActivity extends ActionBarActivity {

    Toolbar toolbar_actionbar;
    public static TextView txtSelectRecipient;
    public static Receipient recObj;
    private  TextView btnAddRecipient;

    Float FinalPayableAmount;
    AffilateUser user;

    TextView txtSendAmount,txtFees,txtamountPayable,txtamountRecipientGET,txtExchangerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer_final);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle("Money Transfer");
            toolbar_actionbar.setNavigationIcon(R.drawable.icon_back);
            toolbar_actionbar.setBackgroundColor(getResources().getColor(R.color.theme_primary));

            setSupportActionBar(toolbar_actionbar);

        }
        toolbar_actionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        intView();

        fillDestails();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(recObj!=null){
            Log.e("object", "object not null");
            txtSelectRecipient.setText(recObj.FirstName.toString()+" "+recObj.LastName.toString());
        }
        else {
            Log.e("object", "object is null");
            txtSelectRecipient.setText("Select Recipient");
        }

         user= PrefUtils.getMerchant(MoneyTransferFinalActivity.this);
     /*   ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(MoneyTransferFinalActivity.this, "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);
*/
    }

    private void fillDestails(){

        float commision = MoneyTransferHomeActivity.bankobj.ApproxComm;

        String percentage = MoneyTransferHomeActivity.bankobj.Perccharge;
        String fixedprice = MoneyTransferHomeActivity.bankobj.Fixedcharge;

        Float Percentage = Float.valueOf(percentage)/100;

        Float amount1 = MoneyTransferHomeActivity.bankobj.Amount * Percentage ;
        Log.e("amount1",String.valueOf(amount1));

        Float FinalFeesAmount = Float.valueOf(MoneyTransferHomeActivity.bankobj.ApproxComm)+amount1+Float.valueOf(fixedprice);

        FinalPayableAmount = Float.valueOf(MoneyTransferHomeActivity.bankobj.Amount)+FinalFeesAmount;

        txtSendAmount.setText("€ "+String.valueOf(MoneyTransferHomeActivity.bankobj.Amount));

        txtFees.setText("Fees:  € "+String.valueOf(FinalFeesAmount));
        txtamountPayable.setText("Total Payable Amount: € "+String.valueOf(FinalPayableAmount));

        txtamountRecipientGET.setText("Recipient Gets: "+String.valueOf(MoneyTransferHomeActivity.bankobj.RecipientGet)+" "+ MoneyTransferHomeActivity.bankobj.Currencies);
        txtExchangerate.setText("Exchnage Rate:  € 1 = "+String.valueOf(MoneyTransferHomeActivity.bankobj.ConvRate)+" "+ MoneyTransferHomeActivity.bankobj.Currencies);

    }
    private void intView(){

    txtSelectRecipient = (TextView)findViewById(R.id.txtSelectRecipient);

      txtSendAmount = (TextView)findViewById(R.id.txtSendAmount);
      txtFees = (TextView)findViewById(R.id.txtFees);
      txtamountPayable = (TextView)findViewById(R.id.txtamountPayable);
      txtamountRecipientGET = (TextView)findViewById(R.id.txtamountRecipientGET);
      txtExchangerate     = (TextView)findViewById(R.id.txtExchangerate);

       btnAddRecipient = (TextView)findViewById(R.id.btnAddRecipient);

    txtSelectRecipient.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MoneyTransferFinalActivity.this,MoneyTransferRecipientActivity.class);
            startActivity(i);
        }
    });


     btnAddRecipient.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             if(recObj==null){
                   SimpleToast.error(MoneyTransferFinalActivity.this, "Please Select Recipient First !!!");
             }
             else {
                processMoney();
             }
         }
     });

}


private void processMoney(){

    final CircleDialog circleDialog=new CircleDialog(MoneyTransferFinalActivity.this,0);
    circleDialog.setCancelable(true);
    circleDialog.show();

    /*"Amount":"String content",
            "BankID":"String content",

            "PayableAmt":"String content",

            "ReceiverAddress":"String content",
            "ReceiverCity":"String content",
            "ReceiverCountry":"String content",

            "ReceiverEmailAddress":"String content",
            "ReceiverFirstName":"String content",
            "ReceiverLastName":"String content",
            "ReceiverMobileNo":"String content",
            "ReceiverState":"String content",
            "ReceiverZip":"String content",
            "ShortCode":"String content",

            "UserID":9223372036854775807*/


    try{
        JSONObject userObject = new JSONObject();

        userObject.put("Amount",String.valueOf(MoneyTransferHomeActivity.bankobj.Amount));
        userObject.put("BankID",String.valueOf(MoneyTransferHomeActivity.bankobj.BankID));
        userObject.put("PayableAmt",String.valueOf(FinalPayableAmount));

        userObject.put("ReceiverAddress",String.valueOf(recObj.Address));
        userObject.put("ReceiverCity",String.valueOf(recObj.City));
        userObject.put("ReceiverCountry",String.valueOf(recObj.Country));

        userObject.put("ReceiverEmailAddress",String.valueOf(recObj.EmailId));
        userObject.put("ReceiverFirstName",String.valueOf(recObj.FirstName));
        userObject.put("ReceiverLastName",String.valueOf(recObj.LastName));

        userObject.put("ReceiverMobileNo",String.valueOf(recObj.MobileNo));
        userObject.put("ReceiverState",String.valueOf(recObj.State));
        userObject.put("ReceiverZip",String.valueOf(recObj.ZipCode));
        //userObject.put("ShortCode",String.valueOf(FinalPayableAmount));




        userObject.put("UserID",user.UserID);


        Log.e("obj of mone transfer--",userObject.toString());
        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstants.MoONEY_CASH_PICKUP, userObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {

                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("Response money transfer : ", "" + response);
                try {

                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("ResponseCode").equalsIgnoreCase("1")){
                        SimpleToast.ok(MoneyTransferFinalActivity.this, "MoneyTransfer Done");
                    }

                    else {
                        SimpleToast.error(MoneyTransferFinalActivity.this, "MoneyTransfer Fail, Your Money is refunded !!!");
                    }





                } catch (Exception e) {
                    Log.e("error responsegg1: ", e.toString() + "");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();
                Log.e("error responsegg: ", error + "");
                SimpleToast.error(MoneyTransferFinalActivity.this, error.getMessage());
            }
        });


        req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
        MyApplication.getInstance().addToRequestQueue(req);




    }catch (Exception e){
        Log.e("Exception in money transfer",e.toString());
    }
}



//end of main class
}
