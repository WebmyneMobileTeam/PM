package com.webmyne.paylabasmerchant.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.model.LiveCurrency;
import com.webmyne.paylabasmerchant.model.PaymentStep1;
import com.webmyne.paylabasmerchant.model.RedeemGC;
import com.webmyne.paylabasmerchant.ui.widget.CallWebService;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.InternationalNumberValidation;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.DatabaseWrapper;
import com.webmyne.paylabasmerchant.util.PrefUtils;
import com.webmyne.paylabasmerchant.util.RegionUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.webmyne.paylabasmerchant.util.LogUtils.LOGE;


public class FragmentHome extends Fragment {

    private DatabaseWrapper db_wrapper;
    private ArrayList<Country> countries;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //    private Spinner spPaymentType;
    private LinearLayout gcLayout;
    private TextView btnNext, btnReset,txtCurrency,txtOther;
    private String mParam1;
    private String mParam2;
    FrameLayout linearTools;
    //    private Spinner spServiceType;
    private Spinner spCountryCode;
    private ArrayAdapter<String> paymentTypeAdapter;
    private ArrayAdapter<String> serviceTypeAdapter;
    private ArrayList<AffilateServices> affilateServicesArrayList;
    private ArrayList<String> affilateServiceNames;
    private ArrayList<String> paymentTypeList;
    private CircleDialog circleDialog;
    private PaymentStep1 paymentStep1;
    private AffilateUser affilateUser;
    private AffilateUser affilateUserBalance;
    private EditText etMobileNumber, etAmount, etGiftCode;
    private String paymentType;
    private int paymentTypePosition, serviceTypePosition;
    private LinearLayout linearPaymentType;
    //    private String paymentType;
//    private int paymentTypePosition,serviceTypePosition;
    private LinearLayout linerPaymentType;
    public int selectedPaymentType = -1;
    public int selectedServiceType = -1;
    private ArrayList colors_p;
    private LinearLayout linearServiceType, layoutOthers;
    private LinearLayout layoutGenerateGC,layoutTopup,layoutTransfer;
    private LinearLayout layoutCash,layoutGC,layoutWallet;

    private TextView txtTransfer,txtTopup,txtGenerate;
    private TextView txtWallet,txtGC,txtCash,txtConvRate;

    private LinearLayout linearMobileHome;
    private LiveCurrency livCurencyObj;

    public static boolean isFromDetailPage = false;

    final CharSequence[] items = {
            "Electricity Bill", "Gas Bill"
    };

    private Boolean isGenerateGCActive,isMobileTopupActive;

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

        colors_p = new ArrayList();
        colors_p.add(getResources().getColor(R.color.color_giftcode_t));
        colors_p.add(getResources().getColor(R.color.color_mobiletopup_t));
        colors_p.add(getResources().getColor(R.color.color_moneytransfer_t));
        colors_p.add(getResources().getColor(R.color.all_track_color));

        filterService();
        paymentTypeList();


    }

    private void paymentTypeList() {

        paymentTypeList = new ArrayList<String>();
        paymentTypeList.add("Select Payment Type");
        paymentTypeList.add(AppConstants.wallet);
        paymentTypeList.add(AppConstants.gc);
        paymentTypeList.add(AppConstants.cash);

    }

    private void filterService() {
        //filter services
        affilateServicesArrayList = new ArrayList<AffilateServices>();
        affilateServicesArrayList = PrefUtils.getMerchant(getActivity()).affilateServicesArrayList;
        affilateServiceNames = new ArrayList<String>();
        affilateServiceNames.add("Select Service Type");

        for (AffilateServices affilateServices : affilateServicesArrayList) {
            Log.e("service name",""+affilateServices.ServiceName);
            Log.e("sis active",""+affilateServices.IsActive);

            if (affilateServices.IsActive == true) {
                affilateServiceNames.add(affilateServices.ServiceName.toString().trim());

            }


        }

        Log.e("service name size",""+affilateServiceNames.size());
        for(int i=0;i<affilateServiceNames.size();i++)
        {
            Log.e("service name",affilateServiceNames.get(i));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertview = inflater.inflate(R.layout.fragment_home, container, false);

        initView(convertview);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMobileNumberEmpty()) {
                    SimpleToast.error(getActivity(), getResources().getString(R.string.validation_empty_mobile_number));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.validation_empty_mobile_number), Toast.LENGTH_SHORT).show();
                } else if(InternationalNumberValidation.isPossibleNumber(etMobileNumber.getText().toString().toString(), countries.get(spCountryCode.getSelectedItemPosition()).ShortCode.toString().trim())==false){
                    SimpleToast.error(getActivity(), "Please Enter Valid Mobile Number");
                }else if(InternationalNumberValidation.isValidNumber(etMobileNumber.getText().toString().toString(), countries.get(spCountryCode.getSelectedItemPosition()).ShortCode.toString().trim())==false){
                    SimpleToast.error(getActivity(), "Please Enter Valid Mobile Number");
                }else if (selectedPaymentType == -1) {
                    SimpleToast.error(getActivity(), getResources().getString(R.string.validation_empty_payment_type_selection));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.validation_empty_payment_type_selection), Toast.LENGTH_SHORT).show();
                } else if (selectedPaymentType == 1 && isGiftCodeEmpty()) {
                    SimpleToast.error(getActivity(), getResources().getString(R.string.validation_empty_gift_code));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.validation_empty_gift_code), Toast.LENGTH_SHORT).show();
                } else if (selectedServiceType == -1) {
                    SimpleToast.error(getActivity(), getResources().getString(R.string.validation_empty_service_type));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.validation_empty_service_type), Toast.LENGTH_SHORT).show();
                } else if (isAmountEmpty()) {
                    SimpleToast.error(getActivity(), getResources().getString(R.string.validation_empty_amount));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.validation_empty_amount), Toast.LENGTH_SHORT).show();
                } else {

                    if ((selectedPaymentType == 2)) {

                        if (selectedServiceType == 0) {

                            //TODO
                            affilateUser.tempAmount = etAmount.getText().toString().trim();
                            Country countryObject = (Country) spCountryCode.getSelectedItem();
                            affilateUser.tempCountryCode = countryObject.CountryCode + "";
                            affilateUser.tempMobileNumber = etMobileNumber.getText().toString();

                            affilateUser.tempPaymentVia = "Cash";

                            PrefUtils.setMerchant(getActivity(), affilateUser);
                            Intent intent = new Intent(getActivity(), MoneyTransferHomeActivity.class);
                            intent.putExtra("edamount",etAmount.getText().toString());
                            startActivity(intent);

                        } else if (selectedServiceType == 1) {
                            //TODO
                            affilateUser.tempAmount = etAmount.getText().toString().trim();
                            Country countryObject = (Country) spCountryCode.getSelectedItem();
                            affilateUser.tempCountryCode = countryObject.CountryCode + "";
                            affilateUser.tempMobileNumber = etMobileNumber.getText().toString();
                            affilateUser.tempPaymentVia = "Cash";
                            PrefUtils.setMerchant(getActivity(), affilateUser);
                            Intent intent = new Intent(getActivity(), MobileTopupActivity.class);
                            startActivity(intent);



                        } else if (selectedServiceType == 2) {
                            //TODO
                            affilateUser.tempAmount = etAmount.getText().toString().trim();
                            Country countryObject = (Country) spCountryCode.getSelectedItem();
                            affilateUser.tempCountryCode = countryObject.CountryCode + "";
                            affilateUser.tempMobileNumber = etMobileNumber.getText().toString();
                            affilateUser.tempPaymentVia = "Cash";
                            PrefUtils.setMerchant(getActivity(), affilateUser);
                            Intent intent = new Intent(getActivity(), NewGenerateGCActivity.class);
                            intent.putExtra("payment_via", selectedPaymentType);
                            startActivity(intent);
                        }

                    } else {
                        postPaymentRequest();
                    }
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetAll();

            }
        });

        setCountryCode();

        return convertview;
    }

    private void resetAll() {
        etAmount.setText("");
        etMobileNumber.setText("");
        etGiftCode.setText("");

        layoutTransfer.setVisibility(View.VISIBLE);
        layoutTopup.setVisibility(View.VISIBLE);
        layoutGenerateGC.setVisibility(View.VISIBLE);
        layoutOthers.setVisibility(View.VISIBLE);
        layoutCash.setVisibility(View.VISIBLE);
        layoutWallet.setVisibility(View.VISIBLE);
        layoutGC.setVisibility(View.VISIBLE);

        txtTransfer.setVisibility(View.VISIBLE);
        txtTopup.setVisibility(View.VISIBLE);
        txtGenerate.setVisibility(View.VISIBLE);
        txtWallet.setVisibility(View.VISIBLE);
        txtCash.setVisibility(View.VISIBLE);
        txtGC.setVisibility(View.VISIBLE);

        resetPaymentLinear();
        resetServiceLinear();

        spCountryCode.setSelection(0);

    }

    private void setCountryCode() {


        new RegionUtils() {

            @Override
            public void response(ArrayList response) {
                countries = response;

                CountryCodeAdapter countryAdapter = new CountryCodeAdapter(getActivity(), R.layout.spinner_country, countries);
                spCountryCode.setAdapter(countryAdapter);
            }
        }.fetchCountry(getActivity());
    }

    private void initView(View convertview) {
        
        gcLayout = (LinearLayout) convertview.findViewById(R.id.gcLayout);
        btnNext = (TextView) convertview.findViewById(R.id.btnNext);
        btnReset = (TextView) convertview.findViewById(R.id.btnReset);
        etMobileNumber = (EditText) convertview.findViewById(R.id.etMobileNumber);
        etAmount = (EditText) convertview.findViewById(R.id.etAmount);
        etGiftCode = (EditText) convertview.findViewById(R.id.etGiftCode);
        linearPaymentType = (LinearLayout) convertview.findViewById(R.id.linearPaymentType);
        linearServiceType = (LinearLayout) convertview.findViewById(R.id.linearServiceType);
        layoutOthers = (LinearLayout) convertview.findViewById(R.id.layoutOthers);
        spCountryCode = (Spinner) convertview.findViewById(R.id.spCountryCode);

        layoutCash =(LinearLayout) convertview.findViewById(R.id.layoutCash);
        layoutGC = (LinearLayout) convertview.findViewById(R.id.layoutGC);
        layoutGenerateGC = (LinearLayout) convertview.findViewById(R.id.layoutGenerateGC);
        layoutTopup = (LinearLayout) convertview.findViewById(R.id.layoutTopUp);
        layoutTransfer = (LinearLayout) convertview.findViewById(R.id.layoutTransfer);
        layoutWallet = (LinearLayout) convertview.findViewById(R.id.layoutWallet);


        txtOther = (TextView)convertview.findViewById(R.id.txtOther);
        txtCash = (TextView)convertview.findViewById(R.id.txtCash);
        txtGC = (TextView)convertview.findViewById(R.id.txtGC);
        txtGenerate = (TextView)convertview.findViewById(R.id.txtGenerate);
        txtTopup = (TextView)convertview.findViewById(R.id.txtTopup);
        txtTransfer = (TextView)convertview.findViewById(R.id.txtTransfer);
        txtWallet = (TextView)convertview.findViewById(R.id.txtWallet);
        txtCurrency= (TextView)convertview.findViewById(R.id.txtCurrency);
        txtConvRate= (TextView)convertview.findViewById(R.id.txtConvRate);
        linearMobileHome = (LinearLayout)convertview.findViewById(R.id.linearMobileHome);


        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()==0) {
                    txtConvRate.setText("");
                    txtConvRate.setVisibility(View.GONE);
                    }

                else {

                    if(selectedServiceType == 3){
                        // for reddem gc no live conversion rate is not display
                    }
                    else {
                        MyApplication.getInstance().cancelAll();
                        getLiveCurrencyRate();
                    }

                }
            }
        });

    }
    


    @Override
    public void onResume() {
        super.onResume();
        getBalanceAndDisplay();
        affilateUser= PrefUtils.getMerchant(getActivity());
        // String str = affilateUser.affilateServicesArrayList.get(0).ServiceName.toString();
        // position 2 is for Cash in service, 0 - for generate gidt code, 1 - fro mobile top only

        isGenerateGCActive = affilateUser.affilateServicesArrayList.get(0).IsActive;
        isMobileTopupActive= affilateUser.affilateServicesArrayList.get(1).IsActive;



        if (isFromDetailPage == true) {
            resetAll();
        }
        PrefUtils.ClearLiveRate(getActivity());
        txtConvRate.setVisibility(View.GONE);
        txtOther.setText("Other Services");
        //getting the currency object
        String LocalCurrency = PrefUtils.getAffilateCurrency(getActivity());
        txtCurrency.setText(LocalCurrency);


        paymentTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, paymentTypeList);
//      spPaymentType.setAdapter(paymentTypeAdapter);
        serviceTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, affilateServiceNames);
//      spServiceType.setAdapter(serviceTypeAdapter);

        resetPaymentLinear();
        resetServiceLinear();
        setupPaymentLinear();
        setupServiceLinear();


     /*   final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);
    */
      /*  ((MyDrawerActivity)getActivity()).setToolTitle("Hi, User!");
        ((MyDrawerActivity)getActivity()).setToolSubTitle("Balance $0.00");
        ((MyDrawerActivity)getActivity()).setToolColor(Color.parseColor("#2977AC"));
*/

     

    }
    private void getBalanceAndDisplay() {
        affilateUserBalance= PrefUtils.getMerchant(getActivity());
        new CallWebService(AppConstants.USER_DETAILS+affilateUserBalance.UserID,CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String jobj) {

                Log.e("on resume response k: ", jobj.toString() + "");

                affilateUserBalance = new GsonBuilder().create().fromJson(jobj.toString(), AffilateUser.class);

              //  AffilateUser user1= PrefUtils.getMerchant(getActivity());
                try{
                    ((MyDrawerActivity)getActivity()).setToolTitle("Hi, "+affilateUserBalance.FName);

                    ((MyDrawerActivity)getActivity()).setToolSubTitle("Balance "+getResources().getString(R.string.euro)+" "+affilateUserBalance.LemonwayBal);
                    ((MyDrawerActivity)getActivity()).hideToolLoading();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void error(VolleyError error) {
                try{
                    ((MyDrawerActivity)getActivity()).hideToolLoading();
                }catch(Exception e){}

            }
        }.start();

    }



    private void getLiveCurrencyRate(){
        try{
            JSONObject userObject = new JSONObject();
            AffilateUser user= PrefUtils.getMerchant(getActivity());

            // Log.e("user local currency",user.LocalCurrency);
            final String LocalCurrency = PrefUtils.getAffilateCurrency(getActivity());
            userObject.put("FromCurrency","EUR");
            userObject.put("Tocurrency",LocalCurrency);

            final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
            circleDialog.setCancelable(true);
            circleDialog.show();
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.GET_LIVE_CURRENCY, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("live currency  Response", "" + response);
                    livCurencyObj = new GsonBuilder().create().fromJson(jobj.toString(), LiveCurrency.class);
                    float finalamt = Float.valueOf(etAmount.getText().toString())/ Float.valueOf(livCurencyObj.LiveRate.toString());

                    PrefUtils.settLiveRate(getActivity(),livCurencyObj.LiveRate.toString());

                    double newValue=0.0d;
                    DecimalFormat df = new DecimalFormat("#.##");
                    newValue = Double.valueOf(df.format(finalamt));
                    txtConvRate.setVisibility(View.VISIBLE);
                    txtConvRate.setText(etAmount.getText().toString()+" "+ livCurencyObj.Tocurrency +" = "+ String.valueOf(newValue)+" EUR");


                //    txtConvRate.setText(etAmount.getText().toString()+" EUR"+" = "+ String.valueOf(newValue)+" "+livCurencyObj.Tocurrency);
                   // LiveRate.setText("1 EUR = "+cashoutobj.LiveRate+" "+cashoutobj.Tocurrency);


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error curreency: ", error + "");
                    SimpleToast.error(getActivity(), getResources().getString(R.string.er_network));
                }
            });
            req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
            MyApplication.getInstance().addToRequestQueue(req);

        }
        catch(Exception e){
            Log.e("error currency out ",e.toString());
        }

    }
    public void refreshBalance(){

        ((MyDrawerActivity)getActivity()).setToolTitle("Hi, "+affilateUser.FName);

        new CallWebService(AppConstants.USER_DETAILS+affilateUser.UserID,CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {

                Log.e("Response User Details ",response);

                try{

                    JSONObject obj = new JSONObject(response);
                    try{
                        ((MyDrawerActivity)getActivity()).setToolSubTitle("Balance "+getResources().getString(R.string.euro)+" "+obj.getString("LemonwayBal"));
                    }catch(Exception e){

                    }

                }catch(Exception e){

                }

            }

            @Override
            public void error(VolleyError error) {


            }
        }.start();


    }


    public void resetPaymentLinear() {
        selectedPaymentType = -1;


        for (int i = 0; i < linearPaymentType.getChildCount(); i++) {
            int k = i;
            LinearLayout linear = (LinearLayout) linearPaymentType.getChildAt(i);
            ImageView img = (ImageView) linear.getChildAt(0);
            linear.setBackgroundResource(R.drawable.circle_border_focused);
            linear.getBackground().setColorFilter((int) colors_p.get(k), PorterDuff.Mode.SRC_ATOP);
            img.setColorFilter((int) colors_p.get(k), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void resetServiceLinear() {
        selectedServiceType = -1;
        for (int i = 0; i < linearServiceType.getChildCount(); i++) {
            int k = i;
            LinearLayout linear = (LinearLayout) linearServiceType.getChildAt(i);
            ImageView img = (ImageView) linear.getChildAt(0);
            linear.setBackgroundResource(R.drawable.circle_border_focused);
            linear.getBackground().setColorFilter((int) colors_p.get(k), PorterDuff.Mode.SRC_ATOP);
            img.setColorFilter((int) colors_p.get(k), PorterDuff.Mode.SRC_ATOP);
        }

        if(isMobileTopupActive == false){
            layoutTopup.setVisibility(View.GONE);
            txtTopup.setVisibility(View.GONE);
        }
        if(isGenerateGCActive == false){
            layoutGenerateGC.setVisibility(View.GONE);
            txtGenerate.setVisibility(View.GONE);
        }

        txtOther.setText("Other Services");
        txtOther.setVisibility(View.VISIBLE);
    }

    private void setupPaymentLinear() {

        for (int i = 0; i < linearPaymentType.getChildCount(); i++) {
            LinearLayout linearChild = (LinearLayout) linearPaymentType.getChildAt(i);
            linearChild.setOnClickListener(linearPaymentListner);
        }


    }

    private void setupServiceLinear() {
        
        for (int i = 0; i <4; i++) {
            LinearLayout linearChild = (LinearLayout) linearServiceType.getChildAt(i);
            linearChild.setOnClickListener(linearServiceListner);
        }

        if(isMobileTopupActive == false){
            layoutTopup.setVisibility(View.GONE);
            txtTopup.setVisibility(View.GONE);
        }

        if(isGenerateGCActive == false){
            layoutGenerateGC.setVisibility(View.GONE);
            txtGenerate.setVisibility(View.GONE);
        }
          
        
    }

    View.OnClickListener linearPaymentListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            
            if(selectedServiceType == -1){
                Toast.makeText(getActivity(), "Select Service First", Toast.LENGTH_SHORT).show();
            }else{
                LinearLayout linearChild = (LinearLayout) v;
                selectedPaymentType = linearPaymentType.indexOfChild(linearChild);
                setPaymentSelection(selectedPaymentType);
            }



        }
    };

    View.OnClickListener linearServiceListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            resetAll();
            
            LinearLayout linearChild = (LinearLayout) v;
            selectedServiceType = linearServiceType.indexOfChild(linearChild);
            setServiceSelection(selectedServiceType);

        }
    };

    private void setServiceSelection(int selectedServiceType) {

        for (int i = 0; i < 4; i++) {

            LinearLayout linear = (LinearLayout) linearServiceType.getChildAt(i);
            ImageView iv = (ImageView) linear.getChildAt(0);
            int z = i;
            if (z == selectedServiceType) {
                iv.setColorFilter(Color.WHITE);
                linear.setBackgroundResource(R.drawable.circle_mask);
                linear.getBackground().setColorFilter((int) colors_p.get(selectedServiceType), PorterDuff.Mode.SRC_ATOP);
            } else {
                iv.setColorFilter((int) colors_p.get(z), PorterDuff.Mode.SRC_ATOP);
                linear.setBackgroundResource(R.drawable.circle_border_focused);
                linear.getBackground().setColorFilter((int) colors_p.get(z), PorterDuff.Mode.SRC_ATOP);
            }
        }

        switch (selectedServiceType){

            case 0:

                // transfer
                layoutWallet.setVisibility(View.VISIBLE);
                layoutGC.setVisibility(View.GONE);
                layoutCash.setVisibility(View.VISIBLE);

                txtWallet.setVisibility(View.VISIBLE);
                txtGC.setVisibility(View.GONE);
                txtCash.setVisibility(View.VISIBLE);
                linearMobileHome.setVisibility(View.VISIBLE);

                etAmount.setText("");
                txtOther.setText("Other Services");
                gcLayout.setVisibility(View.GONE);
                break;

            case 1:

                //topup

                layoutWallet.setVisibility(View.VISIBLE);
                layoutGC.setVisibility(View.GONE);
                layoutCash.setVisibility(View.VISIBLE);

                txtWallet.setVisibility(View.VISIBLE);
                txtGC.setVisibility(View.GONE);
                txtCash.setVisibility(View.VISIBLE);

                linearMobileHome.setVisibility(View.GONE);

                etAmount.setText("");
                txtOther.setText("Other Services");
                gcLayout.setVisibility(View.GONE);
                break;

            case 2:

                //generate
                layoutWallet.setVisibility(View.VISIBLE);
                layoutGC.setVisibility(View.GONE);
                layoutCash.setVisibility(View.VISIBLE);

                txtWallet.setVisibility(View.VISIBLE);
                txtGC.setVisibility(View.GONE);
                txtCash.setVisibility(View.VISIBLE);
                linearMobileHome.setVisibility(View.VISIBLE);

                etAmount.setText("");
                txtOther.setText("Other Services");
                gcLayout.setVisibility(View.GONE);
                break;

            case 3:
                //others

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Other Service");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        //   mDoneButton.setText(items[item]);
                        txtOther.setText(items[item]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                layoutWallet.setVisibility(View.GONE);
                layoutGC.setVisibility(View.VISIBLE);
                layoutCash.setVisibility(View.GONE);
                gcLayout.setVisibility(View.VISIBLE);
                txtWallet.setVisibility(View.GONE);
                txtGC.setVisibility(View.VISIBLE);
                txtCash.setVisibility(View.GONE);
                linearMobileHome.setVisibility(View.VISIBLE);

                etAmount.setText("");
                break;

        }


    }

    private void setPaymentSelection(int selectedPaymentType) {



        for (int i = 0; i < linearPaymentType.getChildCount(); i++) {

            LinearLayout linear = (LinearLayout) linearPaymentType.getChildAt(i);
            ImageView iv = (ImageView) linear.getChildAt(0);
            int z = i;
            if (z == selectedPaymentType) {
                iv.setColorFilter(Color.WHITE);
                linear.setBackgroundResource(R.drawable.circle_mask);
                linear.getBackground().setColorFilter((int) colors_p.get(selectedPaymentType), PorterDuff.Mode.SRC_ATOP);

            } else {

                iv.setColorFilter((int) colors_p.get(z), PorterDuff.Mode.SRC_ATOP);
                linear.setBackgroundResource(R.drawable.circle_border_focused);
                linear.getBackground().setColorFilter((int) colors_p.get(z), PorterDuff.Mode.SRC_ATOP);

            }
        }


      /*  switch (selectedPaymentType){

            case 0:
                    // for wallet
                layoutTransfer.setVisibility(View.VISIBLE);
                txtTransfer.setVisibility(View.VISIBLE);

                if(isMobileTopupActive == false){
                    layoutTopup.setVisibility(View.GONE);
                    txtTopup.setVisibility(View.GONE);
                }
                else{
                    layoutTopup.setVisibility(View.VISIBLE);
                    txtTopup.setVisibility(View.VISIBLE);
                }

                if(isGenerateGCActive == false){
                    layoutGenerateGC.setVisibility(View.GONE);
                    txtGenerate.setVisibility(View.GONE);
                }
                else{
                    layoutGenerateGC.setVisibility(View.VISIBLE);
                    txtGenerate.setVisibility(View.VISIBLE);
                }
                layoutTransfer.setVisibility(View.VISIBLE);
                txtTransfer.setVisibility(View.VISIBLE);

                //hide
                layoutOthers.setVisibility(View.GONE);
                txtOther.setVisibility(View.GONE);
                gcLayout.setVisibility(View.GONE);

                break;

            case 1:
                // for Giftcode
                //display
                layoutOthers.setVisibility(View.VISIBLE);
                txtOther.setVisibility(View.VISIBLE);

                gcLayout.setVisibility(View.VISIBLE);

                //hide
                layoutTopup.setVisibility(View.GONE);
                txtTopup.setVisibility(View.GONE);
                layoutGenerateGC.setVisibility(View.GONE);
                txtGenerate.setVisibility(View.GONE);
                layoutTransfer.setVisibility(View.GONE);
                txtTransfer.setVisibility(View.GONE);

                break;

            case 2:
            // for Cash
                layoutTransfer.setVisibility(View.VISIBLE);
                txtTransfer.setVisibility(View.VISIBLE);

            if(isMobileTopupActive == false){
                layoutTopup.setVisibility(View.GONE);
                txtTopup.setVisibility(View.GONE);
            }
            else{
                layoutTopup.setVisibility(View.VISIBLE);
                txtTopup.setVisibility(View.VISIBLE);
            }

            if(isGenerateGCActive == false){
                layoutGenerateGC.setVisibility(View.GONE);
                txtGenerate.setVisibility(View.GONE);
            }

            else{
                layoutGenerateGC.setVisibility(View.VISIBLE);
                txtGenerate.setVisibility(View.VISIBLE);
            }

            //hide
            layoutOthers.setVisibility(View.GONE);
            txtOther.setVisibility(View.GONE);
                gcLayout.setVisibility(View.GONE);


        }*/

      /*  if(isMobileTopupActive == false){
            layoutTopup.setVisibility(View.GONE);
            txtTopup.setVisibility(View.GONE);
        }
        if(isGenerateGCActive == false){
            layoutGenerateGC.setVisibility(View.GONE);
            txtGenerate.setVisibility(View.GONE);
        }*/

/*
        if (selectedPaymentType == 0) {
            gcLayout.setVisibility(View.VISIBLE);
        } else {
            gcLayout.setVisibility(View.GONE);
        }




        if (selectedPaymentType == 1) {
            gcLayout.setVisibility(View.VISIBLE);
        } else {
            gcLayout.setVisibility(View.GONE);
        }

        if (selectedPaymentType == 2) {
            layoutOthers.setVisibility(View.GONE);
            txtOther.setVisibility(View.GONE);
        } else {
            layoutOthers.setVisibility(View.VISIBLE);
        }*/

    }


    private void postPaymentRequest() {

        circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JSONObject requestObject = new JSONObject();
        try {

            requestObject.put("AffiliateID", affilateUser.UserID + "");
            //  String LiveRate=PrefUtils.getLiveRate(getActivity());
            // Log.e("using live rate",LiveRate);
            // float finalamt = Float.valueOf(etAmount.getText().toString().trim()) * Float.valueOf(LiveRate);
            // double newPayableAMount=0.0d;
            // DecimalFormat df = new DecimalFormat("#.##");
            // newPayableAMount = Double.valueOf(df.format(finalamt));

            //     requestObject.put("Amount", String.valueOf(newPayableAMount));


            if (selectedServiceType == 1) {
                requestObject.put("Amount", "1");
            }
            else{
                requestObject.put("Amount", etAmount.getText().toString().trim() + "");
            }



            if (selectedPaymentType == 1) {
                requestObject.put("GiftCode", etGiftCode.getText().toString()); //add if gift code select
            }




            if (selectedPaymentType == 1) {
                requestObject.put("PaymentVia", "GC"); //GC or Wallet
            } else if (selectedPaymentType == 0) {
                requestObject.put("PaymentVia", "Wallet");
            }
            Country countryObject = (Country) spCountryCode.getSelectedItem();
            requestObject.put("UserCountryCode", countryObject.CountryCode);
            requestObject.put("UserMobileNo", etMobileNumber.getText().toString().trim() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("object payment 1", requestObject.toString() + "");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.PAYMENT_STEP_1, requestObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                circleDialog.dismiss();

                Log.e("response: ", jobj.toString() + "");
                paymentStep1 = new GsonBuilder().create().fromJson(jobj.toString(), PaymentStep1.class);
                try {
                    if (paymentStep1.ResponseCode.equalsIgnoreCase("1")) {

                        SimpleToast.ok(getActivity(), getResources().getString(R.string.PaymentStep1_1));
                        showVerificationAlert();


                    } else if (paymentStep1.ResponseCode.equalsIgnoreCase("2")) {
                        SimpleToast.error(getActivity(), getResources().getString(R.string.PaymentStep1_2));


                    } else if (paymentStep1.ResponseCode.equalsIgnoreCase("2")) {
                        SimpleToast.error(getActivity(), getResources().getString(R.string.PaymentStep1_2));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_2), Toast.LENGTH_SHORT).show();
                    } else if (paymentStep1.ResponseCode.equalsIgnoreCase("-2")) {
                        SimpleToast.error(getActivity(), getResources().getString(R.string.PaymentStep1_m2));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m2), Toast.LENGTH_SHORT).show();
                    } else if (paymentStep1.ResponseCode.equalsIgnoreCase("-3")) {
                        SimpleToast.error(getActivity(), getResources().getString(R.string.PaymentStep1_m3));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m3), Toast.LENGTH_SHORT).show();
                    } else if (paymentStep1.ResponseCode.equalsIgnoreCase("-4")) {
                        SimpleToast.error(getActivity(), getResources().getString(R.string.PaymentStep1_m4));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m4), Toast.LENGTH_SHORT).show();
                    }
                    else if (paymentStep1.ResponseCode.equalsIgnoreCase("-6")) {
                        SimpleToast.error(getActivity(), paymentStep1.ResponseMsg);
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m4), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SimpleToast.error(getActivity(), getResources().getString(R.string.PaymentStep1_m5));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m5), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SimpleToast.error(getActivity(), "Error");
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                circleDialog.dismiss();
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        MyApplication.getInstance().addToRequestQueue(req);

    }

    private void showVerificationAlert() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.custom_alert_dialog, null);
        final EditText etVerificationCode = (EditText) promptsView.findViewById(R.id.etVerificationCode);
        etVerificationCode.setText(paymentStep1.VerificationCode + "");
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(promptsView);

        alert.setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (paymentStep1.VerificationCode.equalsIgnoreCase(etVerificationCode.getText().toString().trim())) {
                    // TODO goto next screen

//                  Intent i =  new Intent(getActivity(),MobileTopupActivity.class);
//                  startActivity(i);

                    dialog.dismiss();
                    if (isRedeemGC()) { // Redeem GC
                        processRedeemGC();

                    } else if (selectedServiceType == 0) { // Money Transfer
                        //TODO
                        affilateUser.tempAmount = etAmount.getText().toString().trim();
                        Country countryObject = (Country) spCountryCode.getSelectedItem();
                        affilateUser.tempCountryCode = countryObject.CountryCode + "";
                        affilateUser.tempMobileNumber = etMobileNumber.getText().toString();
                        if (selectedPaymentType == 1) {
                            affilateUser.tempPaymentVia = "GC";
                            affilateUser.tempGiftCode = etGiftCode.getText().toString().trim();
                        } else {
                            affilateUser.tempPaymentVia = "Wallet";
                        }
                        PrefUtils.setMerchant(getActivity(), affilateUser);
                        Intent intent = new Intent(getActivity(), MoneyTransferHomeActivity.class);
                        intent.putExtra("edamount",etAmount.getText().toString());
                        startActivity(intent);

                    } else if (selectedServiceType == 1) { // Mobile Topup
                        //TODO
                        affilateUser.tempAmount = etAmount.getText().toString().trim();
                        Country countryObject = (Country) spCountryCode.getSelectedItem();
                        affilateUser.tempCountryCode = countryObject.CountryCode + "";
                        affilateUser.tempMobileNumber = etMobileNumber.getText().toString();
                        if (selectedPaymentType == 1) {
                            affilateUser.tempPaymentVia = "GC";
                            affilateUser.tempGiftCode = etGiftCode.getText().toString().trim();
                        } else if (selectedPaymentType == 0) {
                            affilateUser.tempPaymentVia = "Wallet";
                        } else if (selectedPaymentType == 2) {
                            affilateUser.tempPaymentVia = "Cash";
                        }
                        PrefUtils.setMerchant(getActivity(), affilateUser);
                        Intent intent = new Intent(getActivity(), MobileTopupActivity.class);
                        startActivity(intent);
                    } else if (selectedServiceType == 2) { //Generate GC
                        //TODO
                        affilateUser.tempAmount = etAmount.getText().toString().trim();
                        Country countryObject = (Country) spCountryCode.getSelectedItem();
                        affilateUser.tempCountryCode = countryObject.CountryCode + "";
                        affilateUser.tempMobileNumber = etMobileNumber.getText().toString();
                        if (selectedPaymentType == 1) {
                            affilateUser.tempPaymentVia = "GC";
                            affilateUser.tempGiftCode = etGiftCode.getText().toString().trim();
                        } else if (selectedPaymentType == 0) {
                            affilateUser.tempPaymentVia = "Wallet";
                        } else if (selectedPaymentType == 2) {
                            affilateUser.tempPaymentVia = "Cash";
                        }
                        PrefUtils.setMerchant(getActivity(), affilateUser);
                        Intent intent = new Intent(getActivity(), NewGenerateGCActivity.class);
                        intent.putExtra("payment_via", selectedPaymentType);
                        startActivity(intent);

                    }


                } else {
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


    /*
       Retreive image from assests and return in Bitmap format
    */
    private Bitmap getBitmapFromAsset(String strName) {
        AssetManager assetManager = getActivity().getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }

    private void processRedeemGC() {

        circleDialog = new CircleDialog(getActivity(), 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JSONObject requestObject = new JSONObject();
        try {

          //  String LiveRate=PrefUtils.getLiveRate(getActivity());
           // Log.e("using live rate",LiveRate);
           // float finalamt = Float.valueOf(etAmount.getText().toString().trim()) * Float.valueOf(LiveRate);
           // double newPayableAMount=0.0d;
           // DecimalFormat df = new DecimalFormat("#.##");
           // newPayableAMount = Double.valueOf(df.format(finalamt));

       //     requestObject.put("Amount", String.valueOf(newPayableAMount));

            requestObject.put("AffiliateID", affilateUser.UserID + "");
            requestObject.put("Amount", etAmount.getText().toString().trim() + "");
            requestObject.put("ServiceUse", getServiceName(selectedServiceType) + "");
            requestObject.put("GiftCode", etGiftCode.getText().toString().trim() + "");
            Country countryObject = (Country) spCountryCode.getSelectedItem();
            requestObject.put("UserCountryCode", countryObject.CountryCode + "");
            requestObject.put("UserMobileNo", etMobileNumber.getText().toString().trim() + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("object request", requestObject.toString() + "");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.REDEEM_GC, requestObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                circleDialog.dismiss();
                LOGE("response: ", jobj.toString() + "");
                Log.e("response redeem gc: ", jobj.toString() + "");
                RedeemGC redeemGC = new GsonBuilder().create().fromJson(jobj.toString(), RedeemGC.class);

                PrefUtils.ClearLiveRate(getActivity());

                if (redeemGC.ResponseCode.equalsIgnoreCase("1")) {
                    SimpleToast.ok(getActivity(), getResources().getString(R.string.RedeemGC1_1));
                } else if (redeemGC.ResponseCode.equalsIgnoreCase("2")) {
                    SimpleToast.ok(getActivity(), getResources().getString(R.string.RedeemGC1_2));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_2), Toast.LENGTH_SHORT).show();
                } else if (redeemGC.ResponseCode.equalsIgnoreCase("-1")) {
                    SimpleToast.error(getActivity(), getResources().getString(R.string.RedeemGC1_m1));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_2), Toast.LENGTH_SHORT).show();
                } else if (redeemGC.ResponseCode.equalsIgnoreCase("-2")) {
                    SimpleToast.error(getActivity(), getResources().getString(R.string.RedeemGC1_m2));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m2), Toast.LENGTH_SHORT).show();
                } else if (redeemGC.ResponseCode.equalsIgnoreCase("-3")) {
                    SimpleToast.error(getActivity(), getResources().getString(R.string.RedeemGC1_m3));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m3), Toast.LENGTH_SHORT).show();
                } else if (redeemGC.ResponseCode.equalsIgnoreCase("-4")) {
                    Log.e("inside","-4");
                    SimpleToast.error(getActivity(), getResources().getString(R.string.RedeemGC1_m4));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m4), Toast.LENGTH_SHORT).show();
                }
                else if (redeemGC.ResponseCode.equalsIgnoreCase("-6")) {
                    Log.e("inside","-6");
                    SimpleToast.error(getActivity(), redeemGC.ResponseMsg);
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m4), Toast.LENGTH_SHORT).show();
                }else {
                    SimpleToast.error(getActivity(), getResources().getString(R.string.RedeemGC1_m5));
//                        Toast.makeText(getActivity(), getResources().getString(R.string.PaymentStep1_m5), Toast.LENGTH_SHORT).show();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                circleDialog.dismiss();
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        MyApplication.getInstance().addToRequestQueue(req);
    }
    private boolean isRedeemGC() {
        boolean isAvailable = false;
        if (selectedPaymentType == 1 && selectedServiceType == 3) {
            isAvailable = true;
        }
        return isAvailable;
    }


    public boolean isMobileNumberEmpty() {

        boolean isEmpty = false;

        if (etMobileNumber.getText() == null || etMobileNumber.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isGiftCodeEmpty() {

        boolean isEmpty = false;

        if (etGiftCode.getText() == null || etGiftCode.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isAmountEmpty() {

        boolean isEmpty = false;

        if(selectedServiceType == 1){
            isEmpty = false;
            return isEmpty;
        }else{
            if (etAmount.getText() == null || etAmount.getText().toString().equalsIgnoreCase("")) {
                isEmpty = true;
            }
        }


        return isEmpty;
    }


    public class CountryCodeAdapter extends ArrayAdapter<Country> {
        Context context;
        int layoutResourceId;
        ArrayList<Country> values;

        // int android.R.Layout.
        public CountryCodeAdapter(Context context, int resource, ArrayList<Country> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16, 16, 16, 16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryName + " +" + String.valueOf(values.get(position).CountryCode));

            txt.setText(values.get(position).CountryName + " +" + String.valueOf(values.get(position).CountryCode));

            if (values.get(position).ShortCode == null || values.get(position).ShortCode.equalsIgnoreCase("") || values.get(position).ShortCode.equalsIgnoreCase("NULL")) {
            } else {
                try {
                  /*  Class res = R.drawable.class;
                    Field field = res.getField(values.get(position).ShortCode.toLowerCase().toString()+".png");
                    int drawableId = field.getInt(null);*/
                    int idd = getResources().getIdentifier("com.webmyne.paylabasmerchant:drawable/" + values.get(position).ShortCode.toString().trim().toLowerCase(), null, null);
                    txt.setCompoundDrawablesWithIntrinsicBounds(idd, 0, 0, 0);
                } catch (Exception e) {
                    Log.e("MyTag", "Failure to get drawable id.", e);
                }


            }
            return txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16, 16, 16, 16);
            txt.setText("+" + String.valueOf(values.get(position).CountryCode));


            return txt;
        }
    }

    private String getServiceName(int selectedServiceType) {
        String serviceType;
        if (selectedServiceType == 0) {
            serviceType = "";
        } else if (selectedServiceType == 1) {
            serviceType = "";
        } else if (selectedServiceType == 2) {
            serviceType = "";
        } else {
            serviceType = "Other";
        }
        return serviceType;
    }
//end of main class
}
