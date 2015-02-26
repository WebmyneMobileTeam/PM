package com.webmyne.paylabasmerchant.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
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
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.model.FormDetail;
import com.webmyne.paylabasmerchant.model.LiveCurrency;
import com.webmyne.paylabasmerchant.model.MobileTopUpProducts;
import com.webmyne.paylabasmerchant.model.MobileTopupMain;
import com.webmyne.paylabasmerchant.model.MobileTopupRechargeService;
import com.webmyne.paylabasmerchant.model.OTPDialog;
import com.webmyne.paylabasmerchant.ui.widget.CallWebService;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.InternationalNumberValidation;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.LanguageStringUtil;
import com.webmyne.paylabasmerchant.util.PrefUtils;
import com.webmyne.paylabasmerchant.util.RegionUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentCashIN extends Fragment {

    private EditText edMobileNumber,edCashInAmount,edFormId,edMobileNumberConfirm;
    private TextView btnPay,txtCurrency,txtConvRate;
    private Spinner spCountry,spIdentityProof;
    private ArrayList<Country> countries;
    ArrayList<String> identityProofTypesList;
    private LiveCurrency livCurencyObj;

    boolean isEnglisSelected;
    CharSequence ch=".";
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
                   SimpleToast.error(getActivity(), getResources().getString(R.string.code_combinegcfragment_PLEASEENTERMOBILE));
                }else if(InternationalNumberValidation.isPossibleNumber(edMobileNumber.getText().toString().toString(), countries.get(spCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.code_combinegcfragment_ERROENTERVALIDMONILENUMBER));
                }else if(InternationalNumberValidation.isValidNumber(edMobileNumber.getText().toString().toString(), countries.get(spCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.code_combinegcfragment_ERROENTERVALIDMONILENUMBER));
                } else   if(isEmptyField(edMobileNumberConfirm)){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.code_fragment_cashiin_ENTERCONIFRMMOBILBENO));
                }else if(InternationalNumberValidation.isPossibleNumber(edMobileNumberConfirm.getText().toString().toString(), countries.get(spCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.code_fragment_cashiin_ENTERVALIDCONIFRMMOBILBENO));
                }else if(InternationalNumberValidation.isValidNumber(edMobileNumberConfirm.getText().toString().toString(), countries.get(spCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.code_fragment_cashiin_ENTERVALIDCONIFRMMOBILBENO));
                }else   if(!edMobileNumberConfirm.getText().toString().equalsIgnoreCase(edMobileNumber.getText().toString())){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.code_fragment_cashiin_ENTERCORRECTCONIFRMMOBILBENO));
                }
                else if(edCashInAmount.length()<6){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.code_fragment_cashiin_ENTERCASHINAMOUNT));
                }
                else if(isEmptyField(edFormId)){
                    SimpleToast.error(getActivity(), getResources().getString(R.string.code_fragment_cashiin_ENTERFORMID));
                }

                else{
                    processOTP();
                }
            }
        });
        return convertView;
    }
    private void processOTP(){
        try{
            AffilateUser user= PrefUtils.getMerchant(getActivity());
            JSONObject userObject = new JSONObject();

            String newvalue= edCashInAmount.getText().toString().trim();
            newvalue = newvalue.replaceAll("\\,", ".");

            userObject.put("Amount",newvalue);
            userObject.put("UserCountryCode",String.valueOf(user.MobileCountryCode));
            userObject.put("UserID",String.valueOf(user.UserID));
            userObject.put("UserMobileNo", user.MobileNo);
            userObject.put("Culture", LanguageStringUtil.CultureString(getActivity()));
            Log.e("cash in  object",userObject.toString());

            final CircleDialog circleDialog = new CircleDialog(getActivity(), 0);
            circleDialog.setCancelable(true);
            circleDialog.show();
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SEND_OTP, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("cash in   Response", "" + response);
                    OTP otpobj= new GsonBuilder().create().fromJson(jobj.toString(), OTP.class);

                    try{
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

                            OTPDialog otpDialog = new OTPDialog(getActivity(),0,otpobj.VerificationCode);
                            otpDialog.setOnConfirmListner(new OTPDialog.OnConfirmListner() {
                                @Override
                                public void onComplete() {
                                    processPay();
                                }
                            });


                        }

                        else {
                            SimpleToast.error(getActivity(),obj.getString("ResponseMsg"));
                        }

                    } catch (Exception e) {
                        Log.e("error response recharge1: ", e.toString() + "");
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error response live curreency: ", error + "");
                    SimpleToast.error(getActivity(),getResources().getString(R.string.er_network));

                }
            });


            req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
            MyApplication.getInstance().addToRequestQueue(req);
        }catch(Exception e){
            Log.e("exception",e.toString());
        }

    }
    private void initView(View convertView){
        edMobileNumber = (EditText)convertView.findViewById(R.id.edMobileNumber);
        edMobileNumberConfirm= (EditText)convertView.findViewById(R.id.edMobileNumberConfirm);
        edFormId= (EditText)convertView.findViewById(R.id.edFormId);
        edCashInAmount= (EditText)convertView.findViewById(R.id.edCashInAmount);
        txtCurrency= (TextView)convertView.findViewById(R.id.txtCurrency);
        btnPay = (TextView)convertView.findViewById(R.id.btnPay);
        spCountry= (Spinner)convertView.findViewById(R.id.spCountry);
        spIdentityProof= (Spinner)convertView.findViewById(R.id.spIdentityProof);
        txtConvRate= (TextView)convertView.findViewById(R.id.txtConvRate);

        edCashInAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//original pattern
//if(!s.toString().matches("^\\ (\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$"))
                if(!s.toString().matches("^\\ (\\d{1,3}(\\d{3})*|(\\d+))(\\"+ch+"\\d{2})?$"))
                {
                    //original pattern
                    //String userInput= ""+s.toString().replaceAll("[^\\d]", "");
                    String userInput= ""+s.toString().replaceAll("[^\\d]+", "");

                    StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                    while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                        cashAmountBuilder.deleteCharAt(0);
                    }
                    while (cashAmountBuilder.length() < 3) {
                        cashAmountBuilder.insert(0, '0');
                    }
                    cashAmountBuilder.insert(cashAmountBuilder.length()-2, ch);
                    cashAmountBuilder.insert(0, ' ');

                    edCashInAmount.setText(cashAmountBuilder.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(edCashInAmount.getText(), cashAmountBuilder.toString().length());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()==0) {
                    txtConvRate.setText("");
                    txtConvRate.setVisibility(View.GONE);
                }

                else if(edCashInAmount.getText().toString().trim().length()>=5) {
                        MyApplication.getInstance().cancelAll();
                        getLiveCurrencyRate();

                }
            }
        });



    }
    private void getLiveCurrencyRate(){
        try{
            JSONObject userObject = new JSONObject();

            // Log.e("user local currency",user.LocalCurrency);
            final String LocalCurrency = PrefUtils.getAffilateCurrency(getActivity());
            userObject.put("FromCurrency","EUR");
            userObject.put("Tocurrency",LocalCurrency);
            userObject.put("Culture", LanguageStringUtil.CultureString(getActivity()));

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

                    String newvalue1= edCashInAmount.getText().toString().trim();
                    newvalue1 = newvalue1.replaceAll("\\,", ".");



                    float finalamt = Float.valueOf(newvalue1)/ Float.valueOf(livCurencyObj.LiveRate.toString());

                    PrefUtils.settLiveRate(getActivity(),livCurencyObj.LiveRate.toString());

                    double newValue=0.0d;
                    DecimalFormat df = new DecimalFormat("#.##");
                    newValue = Double.valueOf(df.format(finalamt));
                    txtConvRate.setVisibility(View.VISIBLE);
                    txtConvRate.setText(edCashInAmount.getText().toString()+" "+ livCurencyObj.Tocurrency +" = "+ LanguageStringUtil.languageString(getActivity(), String.valueOf(newValue))+" EUR");


                    //    txtConvRate.setText(etAmount.getText().toString()+" EUR"+" = "+ String.valueOf(newValue)+" "+livCurencyObj.Tocurrency);
                    // LiveRate.setText("1 EUR = "+cashoutobj.LiveRate+" "+cashoutobj.Tocurrency);


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error response live curreency: ", error + "");
                    SimpleToast.error(getActivity(), getResources().getString(R.string.er_network));
                }
            });
            req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
            MyApplication.getInstance().addToRequestQueue(req);

        }
        catch(Exception e){
            Log.e("error in getlive currency out ",e.toString());
        }

    }




    @Override
    public void onResume() {
        super.onResume();
        setCountryCode();

        String LocalCurrency = PrefUtils.getAffilateCurrency(getActivity());
        txtCurrency.setText(LocalCurrency);

        isEnglisSelected= PrefUtils.isEnglishSelected(getActivity());

        if(isEnglisSelected)
            ch=",";
        else
            ch=".";


        txtConvRate.setVisibility(View.GONE);
        edCashInAmount.setText("");

        identityProofTypesList=new ArrayList<String>();

        identityProofTypesList.add(getResources().getString(R.string.NATIONAID));
        identityProofTypesList.add(getResources().getString(R.string.PASSPORT));
        identityProofTypesList.add(getResources().getString(R.string.DRIVINGLICENSE));
        identityProofTypesList.add(getResources().getString(R.string.SOCIALSECURITYNO));
        spIdentityProof.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,identityProofTypesList));

    }

private void processPay(){

        try{

            JSONObject userObject = new JSONObject();
            AffilateUser user= PrefUtils.getMerchant(getActivity());


            DecimalFormat df = new DecimalFormat("#.##");
            String LiveRate = PrefUtils.getLiveRate(getActivity());

            String newvalue= edCashInAmount.getText().toString().trim();
            newvalue = newvalue.replaceAll("\\,", ".");

            double finalamt = Double.parseDouble(newvalue)/ Float.valueOf(LiveRate);
            double newAmount=0.0d;
            newAmount = Double.valueOf(df.format(finalamt));

            userObject.put("Amount",String.valueOf(newAmount));


            userObject.put("AffiliateID",user.UserID+"");
          //  userObject.put("Amount",edCashInAmount.getText().toString());
            userObject.put("Currency","EUR");
            userObject.put("FormDetail",edFormId.getText().toString());
            userObject.put("FormDetailType",spIdentityProof.getSelectedItemPosition()+1);
            userObject.put("UserCountryCode",countries.get(spCountry.getSelectedItemPosition()).CountryCode);
            userObject.put("UserMobileNo",edMobileNumber.getText().toString());
            userObject.put("Culture", LanguageStringUtil.CultureString(getActivity()));
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

                    PrefUtils.ClearLiveRate(getActivity());

                    try{
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("ResponseCode").equalsIgnoreCase("1")){
                            SimpleToast.ok(getActivity(), getResources().getString(R.string.code_fragmentcashiin_CASHINDONE));

                        }
                        else {
                            SimpleToast.error(getActivity(), obj.getString("ResponseMsg"));

                        }
                        getActivity().finish();
                    } catch (Exception e) {
                        Log.e("error response cashi in1: ", e.toString() + "");
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();
                    Log.e("error response cash in2: ", error + "");
                    SimpleToast.error(getActivity(), getResources().getString(R.string.code_fragmentcashiin_NWERROR));
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

                CountryCodeAdapter countryAdapter = new CountryCodeAdapter(getActivity(),R.layout.spinner_country, countries);
                spCountry.setAdapter(countryAdapter);

            }
        }.fetchCountry(getActivity());
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
    private class OTP{
        @SerializedName("VerificationCode")
        public String VerificationCode;
    }



    // end of main class
}
