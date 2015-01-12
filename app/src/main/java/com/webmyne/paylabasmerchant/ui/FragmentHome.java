package com.webmyne.paylabasmerchant.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.webmyne.paylabasmerchant.util.PrefUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.webmyne.paylabasmerchant.util.LogUtils.LOGE;
import static com.webmyne.paylabasmerchant.util.PrefUtils.setLoggedIn;
import static com.webmyne.paylabasmerchant.util.PrefUtils.setMerchant;


public class FragmentHome extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner spPaymentType;
    private LinearLayout gcLayout;
    private TextView btnNext;
    private String mParam1;
    private String mParam2;
    FrameLayout linearTools;
    private Spinner spServiceType;
    private ArrayAdapter<String>  paymentTypeAdapter;
    private ArrayAdapter<String> serviceTypeAdapter;
    private ArrayList<AffilateServices> affilateServicesArrayList;
    private ArrayList<String> affilateServiceNames;
    private ArrayList<String> paymentTypeList;
    private CircleDialog circleDialog;
    private PaymentStep1 paymentStep1;
    private AffilateUser affilateUser;
    private EditText etMobileNumber,etAmount;
    private String paymentType;

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
                //TODO validation

                postPaymentRequest();
            }
        });

        spPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             if(position==2){
                    gcLayout.setVisibility(View.VISIBLE);
                } else {
                    gcLayout.setVisibility(View.GONE);
                }
                paymentType=paymentTypeList.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertview;
    }

    private void initView(View convertview) {
        spPaymentType=(Spinner)convertview.findViewById(R.id.spPaymentType);
        gcLayout=(LinearLayout)convertview.findViewById(R.id.gcLayout);
        btnNext=(TextView)convertview.findViewById(R.id.btnNext);
        spServiceType=(Spinner)convertview.findViewById(R.id.spServiceType);
        etMobileNumber= (EditText)convertview.findViewById(R.id.etMobileNumber);
        etAmount= (EditText)convertview.findViewById(R.id.etAmount);
    }


    @Override
    public void onResume() {
        super.onResume();
        affilateUser=PrefUtils.getMerchant(getActivity());

        paymentTypeAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, paymentTypeList);
        spPaymentType.setAdapter(paymentTypeAdapter);
        serviceTypeAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, affilateServiceNames);
        spServiceType.setAdapter(serviceTypeAdapter);
    }

    private void postPaymentRequest() {

            circleDialog = new CircleDialog(getActivity(), 0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JSONObject requestObject = new JSONObject();
            try {
                requestObject.put("AffiliateID","");
                requestObject.put("Amount", etAmount.getText().toString().trim()+"");
                requestObject.put("GCAmount","");
//                if(paymentType.equalsIgnoreCase(AppConstants.gc)){
//                    requestObject.put("GiftCode", et); //add if gift code select
//                } else {
//                    requestObject.put("GiftCode", ""); //add if gift code select
//                }

                requestObject.put("IsGCUsed", "");
                requestObject.put("IsLowBalance","");
                requestObject.put("LemonwayBal", "");
                if(paymentType.equalsIgnoreCase(AppConstants.gc)){
                    requestObject.put("PaymentVia","GC"); //GC or Wallet
                } else if(paymentType.equalsIgnoreCase(AppConstants.wallet)) {
                    requestObject.put("PaymentVia","Wallet");
                } else {
                    requestObject.put("PaymentVia","");
                }
                requestObject.put("ResponseCode", "");
                requestObject.put("ResponseMsg", "");

                requestObject.put("ServiceUse","");
                requestObject.put("UserCountryCode","");
                requestObject.put("UserID","");
                requestObject.put("UserMobileNo", etMobileNumber.getText().toString().trim()+"");
                requestObject.put("VerificationCode", "");
            } catch (Exception e){
                e.printStackTrace();
            }
            Log.e("object request",requestObject.toString()+"");
//            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.PAYMENT_STEP_1, requestObject, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject jobj) {
//                    circleDialog.dismiss();
//                    LOGE("response: ", jobj.toString() + "");
//                    Log.e("response: ", jobj.toString() + "");
//                    paymentStep1 = new GsonBuilder().create().fromJson(jobj.toString(), PaymentStep1.class);
//                    if(paymentStep1.ResponseCode.equalsIgnoreCase("1")){
//
//                       showVerificationAlert();
//
//                    } else {
//
//                        Toast.makeText(getActivity(), "Network Error\n" + "Please try again", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    circleDialog.dismiss();
//
//
//
//                }
//            });
//
//            req.setRetryPolicy(new DefaultRetryPolicy(0,0,0));
//
//            MyApplication.getInstance().addToRequestQueue(req);


    }

    private void showVerificationAlert() {



        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.custom_alert_dialog, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(promptsView);

        alert.setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();

//                FragmentManager manager = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = manager.beginTransaction();
//                ft.setCustomAnimations(R.anim.entry, R.anim.exit,R.anim.entry, R.anim.exit);
//                ft.replace(R.id.payment_fragment, new FragmentPaymentServiceSelection(), "paymenent_services");
//                ft.addToBackStack("");
//                ft.commit();
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

//end of main class
}
