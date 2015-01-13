package com.webmyne.paylabasmerchant.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateServices;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.PaymentStep1;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.PrefUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.webmyne.paylabasmerchant.util.LogUtils.LOGE;


public class FragmentHome extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//    private Spinner spPaymentType;
    private LinearLayout gcLayout;
    private TextView btnNext;
    private String mParam1;
    private String mParam2;
    FrameLayout linearTools;
//    private Spinner spServiceType;
    private Spinner spCountryCode;
    private ArrayAdapter<String>  paymentTypeAdapter;
    private ArrayAdapter<String> serviceTypeAdapter;
    private ArrayList<AffilateServices> affilateServicesArrayList;
    private ArrayList<String> affilateServiceNames;
    private ArrayList<String> paymentTypeList;
    private CircleDialog circleDialog;
    private PaymentStep1 paymentStep1;
    private AffilateUser affilateUser;
    private EditText etMobileNumber,etAmount,etGiftCode;
    private String paymentType;
    private int paymentTypePosition,serviceTypePosition;
    private LinearLayout linerPaymentType;
    public int selectedPaymentType = -1;
    public int selectedServiceType = -1;
    private LinearLayout linearServiceType;

    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        filterService();

        paymentTypeList();
    }

    private void paymentTypeList() {
        paymentTypeList=new ArrayList<String>();
        paymentTypeList.add("Select Payment Type");
        paymentTypeList.add(AppConstants.wallet);
        paymentTypeList.add(AppConstants.gc);
        paymentTypeList.add(AppConstants.cash);
    }

    private void filterService() {
        //filter services
        affilateServicesArrayList=new ArrayList<AffilateServices>();
        affilateServicesArrayList= PrefUtils.getMerchant(getActivity()).affilateServicesArrayList;
        affilateServiceNames=new ArrayList<String>();
        affilateServiceNames.add("Select Service Type");
        for(AffilateServices affilateServices: affilateServicesArrayList){
            if(affilateServices.IsActive==true){
                affilateServiceNames.add(affilateServices.ServiceName.toString().trim());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertview = inflater.inflate(R.layout.fragment_home, container, false);

        initView(convertview);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isMobileNumberEmpty()){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.validation_empty_mobile_number));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.validation_empty_mobile_number), Toast.LENGTH_SHORT).show();
                } else if(paymentTypePosition==0){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.validation_empty_payment_type_selection));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.validation_empty_payment_type_selection), Toast.LENGTH_SHORT).show();
                } else if(paymentTypePosition==2 && isGiftCodeEmpty()){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.validation_empty_gift_code));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.validation_empty_gift_code), Toast.LENGTH_SHORT).show();
                } else if(serviceTypePosition==0){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.validation_empty_service_type));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.validation_empty_service_type), Toast.LENGTH_SHORT).show();
                }else if(isAmountEmpty()) {
                    SimpleToast.error(getActivity(), getResources().getString(R.string.validation_empty_amount));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.validation_empty_amount), Toast.LENGTH_SHORT).show();
                } else {
                    postPaymentRequest();
                }

            }
        });


        return convertview;
    }

    private void initView(View convertview) {
//        spPaymentType=(Spinner)convertview.findViewById(R.id.spPaymentType);
        gcLayout=(LinearLayout)convertview.findViewById(R.id.gcLayout);

        btnNext=(TextView)convertview.findViewById(R.id.btnNext);
//        spServiceType=(Spinner)convertview.findViewById(R.id.spServiceType);
        etMobileNumber= (EditText)convertview.findViewById(R.id.etMobileNumber);
        etAmount= (EditText)convertview.findViewById(R.id.etAmount);
        etGiftCode= (EditText)convertview.findViewById(R.id.etGiftCode);
        linerPaymentType = (LinearLayout)convertview.findViewById(R.id.linearPaymentType);
        linearServiceType = (LinearLayout)convertview.findViewById(R.id.linearServiceType);
        spCountryCode=(Spinner)convertview.findViewById(R.id.spCountryCode);
    }


    @Override
    public void onResume() {
        super.onResume();
        affilateUser=PrefUtils.getMerchant(getActivity());

        paymentTypeAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, paymentTypeList);
//        spPaymentType.setAdapter(paymentTypeAdapter);
        serviceTypeAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, affilateServiceNames);
//        spServiceType.setAdapter(serviceTypeAdapter);
        setupPaymentLinear();
        setupServiceLinear();
    }

    private void setupPaymentLinear() {

        for(int i=0;i<linerPaymentType.getChildCount();i++){
            LinearLayout linearChild = (LinearLayout)linerPaymentType.getChildAt(i);
            linearChild.setOnClickListener(linearPaymentListner);
        }
    }

    private void setupServiceLinear() {

        for(int i=0;i<linearServiceType.getChildCount();i++){
            LinearLayout linearChild = (LinearLayout)linearServiceType.getChildAt(i);
            linearChild.setOnClickListener(linearServiceListner);
        }
    }

    View.OnClickListener linearPaymentListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          LinearLayout linearChild = (LinearLayout)v;
          selectedPaymentType = linerPaymentType.indexOfChild(linearChild);
          setPaymentSelection(selectedPaymentType);

        }
    };

    View.OnClickListener linearServiceListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            LinearLayout linearChild = (LinearLayout)v;
            selectedServiceType = linearServiceType.indexOfChild(linearChild);
            setServiceSelection(selectedServiceType);

        }
    };

    private void setServiceSelection(int selectedServiceType) {

        for(int i=0;i<linearServiceType.getChildCount();i++){

            LinearLayout linear = (LinearLayout)linearServiceType.getChildAt(i);
            ImageView iv = (ImageView)linear.getChildAt(0);

            int z = i;
            if(z == selectedServiceType){
                iv.setColorFilter(Color.WHITE);
                linear.setBackgroundResource(R.drawable.circle_mask);
            }else{

                iv.setColorFilter(getResources().getColor(R.color.theme_primary));
                linear.setBackgroundResource(R.drawable.circle_border_focused);
            }
        }
    }

    private void setPaymentSelection(int selectedPaymentType) {

        for(int i=0;i<linerPaymentType.getChildCount();i++){

            LinearLayout linear = (LinearLayout)linerPaymentType.getChildAt(i);
            ImageView iv = (ImageView)linear.getChildAt(0);

            int z = i;
            if(z == selectedPaymentType){
                iv.setColorFilter(Color.WHITE);
                linear.setBackgroundResource(R.drawable.circle_mask);
            }else{

                iv.setColorFilter(getResources().getColor(R.color.theme_primary));
                linear.setBackgroundResource(R.drawable.circle_border_focused);
            }
        }
    }


    private void postPaymentRequest() {

        circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("AffiliateID",affilateUser.MerchantID+"");
            requestObject.put("Amount", etAmount.getText().toString().trim()+"");
            if(paymentType.equalsIgnoreCase(AppConstants.gc)){
                requestObject.put("GiftCode", etGiftCode.getText().toString()); //add if gift code select
            }
            if(paymentType.equalsIgnoreCase(AppConstants.gc)){
                requestObject.put("PaymentVia","GC"); //GC or Wallet
            } else if(paymentType.equalsIgnoreCase(AppConstants.wallet)) {
                requestObject.put("PaymentVia","Wallet");
            } else {
                requestObject.put("PaymentVia","Cash");
            }
            requestObject.put("UserCountryCode","91");
            requestObject.put("UserMobileNo", etMobileNumber.getText().toString().trim()+"");
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.e("object request",requestObject.toString()+"");
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.PAYMENT_STEP_1, requestObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    LOGE("response: ", jobj.toString() + "");
                    Log.e("response: ", jobj.toString() + "");
                    paymentStep1 = new GsonBuilder().create().fromJson(jobj.toString(), PaymentStep1.class);
                    if(paymentStep1.ResponseCode.equalsIgnoreCase("1")){

                        if(!paymentType.equalsIgnoreCase(AppConstants.cash)) {
                            showVerificationAlert();
                        }else {

                            //TODO Direct redirect to next screen

                        }

                    } else if(paymentStep1.ResponseCode.equalsIgnoreCase("2")){
                        SimpleToast.error(getActivity(), getResources().getString(R.string.PaymentStep1_2));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_2), Toast.LENGTH_SHORT).show();
                    }   else if(paymentStep1.ResponseCode.equalsIgnoreCase("-2")){
                        SimpleToast.error(getActivity(), getResources().getString(R.string.PaymentStep1_m2));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m2), Toast.LENGTH_SHORT).show();
                    } else if(paymentStep1.ResponseCode.equalsIgnoreCase("-3")){
                        SimpleToast.error(getActivity(), getResources().getString(R.string.PaymentStep1_m3));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m3), Toast.LENGTH_SHORT).show();
                    } else if(paymentStep1.ResponseCode.equalsIgnoreCase("-4")){
                        SimpleToast.error(getActivity(), getResources().getString(R.string.PaymentStep1_m4));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m4), Toast.LENGTH_SHORT).show();
                    } else {
                        SimpleToast.error(getActivity(), getResources().getString(R.string.PaymentStep1_m5));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m5), Toast.LENGTH_SHORT).show();
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

    private void showVerificationAlert() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.custom_alert_dialog, null);
       final  EditText etVerificationCode=(EditText)promptsView.findViewById(R.id.etVerificationCode);
        etVerificationCode.setText(paymentStep1.VerificationCode+"");
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(promptsView);

        alert.setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(paymentStep1.VerificationCode.equalsIgnoreCase(etVerificationCode.getText().toString().trim())){
                    // TODO goto next screen
//                    if(spServiceType.getSelectedItemPosition()==3){
//
//                        Intent i =  new Intent(getActivity(),MobileTopupActivity.class);
//                        startActivity(i);
//                    }

                    dialog.dismiss();

                    /*if(isRedeemGC()){
                        processRedeemGC();
                    }
*/





                } else{
                    SimpleToast.error(getActivity(), getResources().getString(R.string.validation_empty_verification_code));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.validation_empty_verification_code), Toast.LENGTH_SHORT).show();
                }


            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void processRedeemGC() {



    }

    private boolean isRedeemGC() {

        return true;
    }

    public boolean isMobileNumberEmpty(){

        boolean isEmpty = false;

        if(etMobileNumber.getText() == null || etMobileNumber.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isGiftCodeEmpty(){

        boolean isEmpty = false;

        if(etGiftCode.getText() == null || etGiftCode.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isAmountEmpty(){

        boolean isEmpty = false;

        if(etAmount.getText() == null || etAmount.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }
//end of main class
}
