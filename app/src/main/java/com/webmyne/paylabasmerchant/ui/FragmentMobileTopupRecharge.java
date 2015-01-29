package com.webmyne.paylabasmerchant.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.MobileTopUpProducts;
import com.webmyne.paylabasmerchant.model.MobileTopupMain;
import com.webmyne.paylabasmerchant.model.MobileTopupRechargeService;
import com.webmyne.paylabasmerchant.ui.widget.CallWebService;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.ComplexPreferences;
import com.webmyne.paylabasmerchant.ui.widget.InternationalNumberValidation;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.PrefUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FragmentMobileTopupRecharge extends Fragment {

    private EditText edRechargeMobileNumber;
    private TextView amountPay,recipeintAmountGET;
    private TextView btnRecharge;
    private Spinner spCountry;
    private Spinner spServiceProvider;
    private Spinner spRechargeAmount;
    private ImageView ProviderImg;
    ArrayList<MobileTopupMain> mobileTopupList;
    ArrayList<MobileTopUpProducts> mobileTopUpProductsArrayList;
    ArrayList<MobileTopupRechargeService> mobileTopupRechargeServiceList;
    String roundup_total;

    public static FragmentMobileTopupRecharge newInstance(String param1, String param2) {
        FragmentMobileTopupRecharge fragment = new FragmentMobileTopupRecharge();
        return fragment;
    }

    public FragmentMobileTopupRecharge() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_mobiletopup_recharge, container, false);

        initView(convertView);
        //edRechargeMobileNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //setting the mobile top carriren name
                mobileTopUpProductsArrayList = mobileTopupList.get(position).TopUpProducts;
                MobileTopUpProductsAdapter TopupProductsadpater = new MobileTopUpProductsAdapter(getActivity(),R.layout.spinner_country, mobileTopUpProductsArrayList);
                spServiceProvider.setAdapter(TopupProductsadpater);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spServiceProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //setting the recharge amount
                mobileTopupRechargeServiceList = mobileTopUpProductsArrayList.get(position).RechargeService;
                MobileTopUpRechargeServiceAdapter RechargeServiceadpater = new MobileTopUpRechargeServiceAdapter(getActivity(),R.layout.spinner_country, mobileTopupRechargeServiceList);
                spRechargeAmount.setAdapter(RechargeServiceadpater);
                String temp= mobileTopUpProductsArrayList.get(position).carrierName.toString();
                Log.e("Item Select",temp);
                SetProviderImage(temp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRechargeAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CalculateRechargePrice(position,spServiceProvider.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AffilateUser user= PrefUtils.getMerchant(getActivity());

                if(isEmptyField(edRechargeMobileNumber)){
//                    SnackBar bar = new SnackBar(getActivity(),"Please Enter Mobile Number");
//                    bar.show();
                    SimpleToast.error(getActivity(), "Please Enter Mobile Number");

                }   else if(InternationalNumberValidation.isPossibleNumber(edRechargeMobileNumber.getText().toString().toString(), mobileTopupList.get(spCountry.getSelectedItemPosition()).shortCode.toString().trim())==false){
                    SimpleToast.error(getActivity(), "Please Enter Valid Mobile Number");
                }else if(InternationalNumberValidation.isValidNumber(edRechargeMobileNumber.getText().toString().toString(), mobileTopupList.get(spCountry.getSelectedItemPosition()).shortCode.toString().trim())==false){
                    SimpleToast.error(getActivity(), "Please Enter Valid Mobile Number");
                } else {
                    processRecharge();
                }
            }
        });
        return convertView;
    }

    private void initView(View convertView){
        edRechargeMobileNumber = (EditText)convertView.findViewById(R.id.edRechargeMobileNumber);
        amountPay = (TextView)convertView.findViewById(R.id.amountPay);
        recipeintAmountGET = (TextView)convertView.findViewById(R.id.recipeintAmountGET);
        btnRecharge = (TextView)convertView.findViewById(R.id.btnRecharge);
        spCountry= (Spinner)convertView.findViewById(R.id.spCountryRecharge);
        spServiceProvider= (Spinner)convertView.findViewById(R.id.spServiceProvider);
        spRechargeAmount= (Spinner)convertView.findViewById(R.id.spRechargeAmount);
        ProviderImg= (ImageView)convertView.findViewById(R.id.ProviderImg);
    }

    @Override
    public void onResume() {
        super.onResume();
        //  fetching all  details of rechrge;
        fetchMobileTopupDetials();
    }

    private void SetProviderImage(String  imageName){

        try {

            Log.e("full path",String.valueOf(AppConstants.providerImageURL+imageName.replaceAll(" ","")+".png"));
            Picasso.with(getActivity().getBaseContext()).load(AppConstants.providerImageURL + imageName.replaceAll(" ", "") + ".png").into(ProviderImg);

        }
        catch(Exception e){
            Log.e("Execpetion occurs loading provider image",e.toString());
        }
    }

    private void fetchMobileTopupDetials(){

        final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        new CallWebService(AppConstants.GET_MOBILE_TOPUP_DETAILS,CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {
                Log.e("mob top response",response);
                circleDialog.dismiss();

                Type listType=new TypeToken<List<MobileTopupMain>>(){
                }.getType();
                mobileTopupList =  new GsonBuilder().create().fromJson(response, listType);
                Log.e("size of mobile top list", String.valueOf(mobileTopupList.size()));

                //setting the mobile top company
                MobileTopUpCountryAdapter countryadpater = new MobileTopUpCountryAdapter(getActivity(),R.layout.spinner_country, mobileTopupList);
                spCountry.setAdapter(countryadpater);

            }

            @Override
            public void error(VolleyError error) {
//            SnackBar bar = new SnackBar(getActivity(),"Server Error. Please Try Again");
//            bar.show();
                SimpleToast.error(getActivity(), "Server Error. Please Try Again");
                circleDialog.dismiss();
            }
        }.start();

        Log.e("mobile topup","web service end");

    }

    private void CalculateRechargePrice(int rechargeAmountPosition,int serviceProviderPosition){

        String rechargePrice = String.valueOf(mobileTopupRechargeServiceList.get(rechargeAmountPosition).rechargePrice);
        Float EuroRate = mobileTopupList.get(serviceProviderPosition).USDtoEuro;

        Float charges = (Float.parseFloat(rechargePrice)/100);
        Log.e(" charges",String.valueOf(charges));

        Float Total = charges*EuroRate;
        roundup_total = String.format("%.2f", Total);

        Log.e("Total",String.valueOf(roundup_total));
        amountPay.setText("You have to Pay € " + String.valueOf(roundup_total));
        recipeintAmountGET.setText("Your recipient gets "+ mobileTopupRechargeServiceList.get(rechargeAmountPosition).currency+" "+String.valueOf(mobileTopupRechargeServiceList.get(rechargeAmountPosition).LocalPrice));

    }

    public void processRecharge(){

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Recharge");
        alert.setMessage("Are sure to Continue ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                try{

                    CalculateRechargePrice(spRechargeAmount.getSelectedItemPosition(),spServiceProvider.getSelectedItemPosition());

                    JSONObject userObject = new JSONObject();
                    AffilateUser user= PrefUtils.getMerchant(getActivity());

                    userObject.put("AffiliateID",user.UserID+"");
                    userObject.put("UserCountryCode",user.tempCountryCode+"");
                    userObject.put("UserMobileNo",user.tempMobileNumber+"");
                    userObject.put("PaymentVia",user.tempPaymentVia+"");
                    userObject.put("Amount",String.valueOf(roundup_total)+"");
                    if(user.tempPaymentVia.equalsIgnoreCase("GC")){
                        userObject.put("GiftCode",user.tempGiftCode+"");
                    }
                    userObject.put("topupCode",mobileTopUpProductsArrayList.get(spServiceProvider.getSelectedItemPosition()).carrierCode+"");
                    userObject.put("countryCode",mobileTopupList.get(spCountry.getSelectedItemPosition()).shortCode.trim()+"");//IN
                    userObject.put("LiveConAmt", mobileTopupList.get(spServiceProvider.getSelectedItemPosition()).USDtoEuro+"");
                    userObject.put("rechargeAmount",mobileTopupRechargeServiceList.get(spRechargeAmount.getSelectedItemPosition()).rechargePrice+"");
                    userObject.put("mobileNo",edRechargeMobileNumber.getText().toString().trim());

                    Log.e("recharge post object",userObject.toString());

                    final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
                    circleDialog.setCancelable(true);
                    circleDialog.show();

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.MOBILE_TOPUP, userObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject jobj) {
                            circleDialog.dismiss();
                            String response = jobj.toString();
                            Log.e("Recharge Response", "" + response);


                            try{
                                JSONObject obj = new JSONObject(response);
                                if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

//                                   SnackBar bar = new SnackBar(getActivity(),"Recharge Done");
//                                   bar.show();
                                    SimpleToast.ok(getActivity(), "Recharge Done");
                                }

                                else {
                                    if(obj.getString("ResponseCode").equalsIgnoreCase("-2")) {
//                                       SnackBar bar112 = new SnackBar(getActivity(), "Payment deduction Fail");
//                                       bar112.show();
                                        SimpleToast.error(getActivity(), "Payment deduction Fail");
                                    }
                                    else {
//                                       SnackBar bar112 = new SnackBar(getActivity(), "Recharge Failed. Please Try again !!!");
//                                       bar112.show();
                                        SimpleToast.error(getActivity(), "Recharge Failed. Please Try again !!!");
                                    }
                                }

                                CountDownTimer countDownTimer;
                                countDownTimer = new MyCountDownTimer(3000, 1000); // 1000 = 1s
                                countDownTimer.start();

                            } catch (Exception e) {
                                Log.e("error response recharge1: ", e.toString() + "");
                            }


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            circleDialog.dismiss();
                            Log.e("error response recharge2: ", error + "");
                            SimpleToast.error(getActivity(), "Network Error. Please Try Again");
//                           SnackBar bar = new SnackBar(getActivity(),"Network Error. Please Try Again");
//                           bar.show();

                        }
                    });


                    req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
                    MyApplication.getInstance().addToRequestQueue(req);

                    // end of main try block
                } catch(Exception e){
                    Log.e("error in recharge",e.toString());
                }
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();

    }


    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            Log.e("counter","Time's up!");
            FragmentHome.isFromDetailPage=true;
                getActivity().finish();
//            FragmentManager manager = getActivity().getSupportFragmentManager();
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.setCustomAnimations(R.anim.entry, R.anim.exit,R.anim.entry, R.anim.exit);
//            ft.replace(R.id.payment_fragment, new FragmentHome(), "payment_home");
//            ft.commit();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

    }

    public boolean isEmptyField(EditText param1){

        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isMobilenoMatch(EditText param1,EditText param2){
        boolean isMatch = false;
        if(param1.getText().toString().equals(param2.getText().toString())){
            isMatch = true;
        }
        return isMatch;
    }

    public class MobileTopUpRechargeServiceAdapter extends ArrayAdapter<MobileTopupRechargeService> {

        Context context;
        int layoutResourceId;
        ArrayList<MobileTopupRechargeService> values;
        // int android.R.Layout.

        public MobileTopUpRechargeServiceAdapter(Context context, int resource, ArrayList<MobileTopupRechargeService> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);

            txt.setText(values.get(position).LocalPrice);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).LocalPrice);
            return  txt;
        }
    }

    public class MobileTopUpProductsAdapter extends ArrayAdapter<MobileTopUpProducts> {

        Context context;
        int layoutResourceId;
        ArrayList<MobileTopUpProducts> values;
        // int android.R.Layout.

        public MobileTopUpProductsAdapter(Context context, int resource, ArrayList<MobileTopUpProducts> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).carrierName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).carrierName);
            return  txt;
        }
    }

    public class MobileTopUpCountryAdapter extends ArrayAdapter<MobileTopupMain> {

        Context context;
        int layoutResourceId;
        ArrayList<MobileTopupMain> values;
        // int android.R.Layout.

        public MobileTopUpCountryAdapter(Context context, int resource, ArrayList<MobileTopupMain> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).countryName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).countryName);
            return  txt;
        }
    }

    // end of main class
}
