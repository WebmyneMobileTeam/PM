package com.webmyne.paylabasmerchant.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
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
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.model.MobileTopUpProducts;
import com.webmyne.paylabasmerchant.model.MobileTopupMain;
import com.webmyne.paylabasmerchant.model.MobileTopupRechargeService;
import com.webmyne.paylabasmerchant.ui.widget.CallWebService;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.PrefUtils;
import com.webmyne.paylabasmerchant.util.RegionUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FragmentCashIN extends Fragment {

    private EditText edMobileNumber,edCashInAmount,edFormId;
    private TextView btnPay;
    private Spinner spCountry;
    private ArrayList<Country> countries;


    public static FragmentCashIN newInstance(String param1, String param2) {
        FragmentCashIN fragment = new FragmentCashIN();
        return fragment;
    }

    public FragmentCashIN() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_cash_in, container, false);

        initView(convertView);
        //edRechargeMobileNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());



        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmptyField(edMobileNumber)){
                   SimpleToast.error(getActivity(), "Please Enter Mobile Number");
                }
                else if(isEmptyField(edCashInAmount)){
                    SimpleToast.error(getActivity(), "Please Enter Cash In Amount");
                }
                else if(isEmptyField(edFormId)){
                    SimpleToast.error(getActivity(), "Please Enter FormID");
                }

                else{
                    processPay();
                }
            }
        });
        return convertView;
    }

    private void initView(View convertView){
        edMobileNumber = (EditText)convertView.findViewById(R.id.edMobileNumber);
        edFormId= (EditText)convertView.findViewById(R.id.edFormId);
        edCashInAmount= (EditText)convertView.findViewById(R.id.edCashInAmount);

        btnPay = (TextView)convertView.findViewById(R.id.btnPay);
        spCountry= (Spinner)convertView.findViewById(R.id.spCountry);

    }

    @Override
    public void onResume() {
        super.onResume();
        setCountryCode();

    }

private void processPay(){

        try{

            JSONObject userObject = new JSONObject();
            AffilateUser user= PrefUtils.getMerchant(getActivity());

            userObject.put("AffiliateID",user.UserID+"");
            userObject.put("Amount",edCashInAmount.getText().toString());
            userObject.put("Currency","EUR");
            userObject.put("FormDetail",edFormId.getText().toString());

            userObject.put("UserCountryCode",countries.get(spCountry.getSelectedItemPosition()).CountryCode);
            userObject.put("UserMobileNo",edMobileNumber.getText().toString());

            Log.e("cash in post object",userObject.toString());

            final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
            circleDialog.setCancelable(true);
            circleDialog.show();
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.CASH_IN, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("cash in  Response", "" + response);


                    try{
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("ResponseCode").equalsIgnoreCase("1")){
                            SimpleToast.ok(getActivity(), "Cash In Done");

                            CountDownTimer countDownTimer;
                            countDownTimer = new MyCountDownTimer(3000, 1000); // 1000 = 1s
                            countDownTimer.start();
                        }
                        else {
                            if(obj.getString("ResponseCode").equalsIgnoreCase("2")) {
                                SimpleToast.error(getActivity(), "Invalid User Details !!!");
                            }
                            else {
                            SimpleToast.error(getActivity(), "Cash In Failed. Please Try again !!!");
                            }
                        }

                    } catch (Exception e) {
                        Log.e("error response cashi in1: ", e.toString() + "");
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error response cash in2: ", error + "");
                    SimpleToast.error(getActivity(), "Network Error. Please Try Again");
                }
            });


            req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
            MyApplication.getInstance().addToRequestQueue(req);

        }
        catch(Exception e){
            Log.e("error in cash in ",e.toString());
        }
    }


    private void setCountryCode() {
        new RegionUtils() {

            @Override
            public void response(ArrayList response) {
                countries=response;
                for(int i=0;i<countries.size();i++){
                    Log.e("country names:",countries.get(i).CountryCode+"");
                }

                CountryCodeAdapter countryAdapter = new CountryCodeAdapter(getActivity(),R.layout.spinner_country, countries);
                spCountry.setAdapter(countryAdapter);
            }
        }.fetchCountry(getActivity());
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

        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

    }

    public class CountryCodeAdapter extends ArrayAdapter<Country> {
        Context context;
        int layoutResourceId;
        ArrayList<Country> values;
        // int android.R.Layout.
        public CountryCodeAdapter(Context context, int resource, ArrayList<Country> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryName+" +"+String.valueOf(values.get(position).CountryCode));

            txt.setText(values.get(position).CountryName+" +"+String.valueOf(values.get(position).CountryCode));
            if(values.get(position).ShortCode == null || values.get(position).ShortCode.equalsIgnoreCase("") ||values.get(position).ShortCode.equalsIgnoreCase("NULL") ){
            }else{
                try {
                  /*  Class res = R.drawable.class;
                    Field field = res.getField(values.get(position).ShortCode.toLowerCase().toString()+".png");
                    int drawableId = field.getInt(null);*/
                    int idd = getResources().getIdentifier("com.webmyne.paylabasmerchant:drawable/" + values.get(position).ShortCode.toString().trim().toLowerCase(), null, null);
                    txt.setCompoundDrawablesWithIntrinsicBounds(idd,0,0,0);
                }
                catch (Exception e) {
                    Log.e("MyTag", "Failure to get drawable id.", e);
                }



            }
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText("+"+String.valueOf(values.get(position).CountryCode)+" "+values.get(position).CountryName);


            return  txt;
        }
    }




 public boolean isEmptyField(EditText param1){

        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }




    // end of main class
}
