package com.webmyne.paylabasmerchant.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
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
import android.widget.LinearLayout;
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
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.model.GCCountry;
import com.webmyne.paylabasmerchant.model.Receipient;
import com.webmyne.paylabasmerchant.ui.widget.CallWebService;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.ComplexPreferences;
import com.webmyne.paylabasmerchant.ui.widget.FormValidator;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.DatabaseWrapper;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 26-01-2015.
 */
public class GenerateGCActivity extends ActionBarActivity implements View.OnClickListener,TextWatcher{



    private AlertDialog alertDialogBuilder;

    private EditText edMobileNumberGenerateGC;
    private EditText edAmountGenerateGC;

//    private Spinner spRecipients;
//    private Spinner spCountry;

    private TextView btnResetGenerateGC;
    private TextView btnGenerateGCGenerateGC;

    private AffilateUser user;
    private Spinner spinnerRecipientContactGenerateGc;
    private Spinner spinnerCountryGenerateGc;

    private ArrayList<Receipient> receipients;
    private ArrayList<Country> countries;
    private DatabaseWrapper db_wrapper;
    private TextView txtCCGenerateGC;
    DecimalFormat df = new DecimalFormat("#.00");
    private ServiceCharge charge;
    private LinearLayout mainLinear;
    private View viewService;
    private int selected_country_id = 0;
    int temp_posCountrySpinner;
    ArrayList<GCCountry> arrCheckCountries;
//    ArrayList<Country> finalCountries;
   // ArrayList<Country> finalCountries;
    double selected_amount = 0;

    @Override
    public void onResume() {
        super.onResume();


    }

    private void getGCCountries() {
        final CircleDialog d = new CircleDialog(this, 0);
        d.setCancelable(true);
        d.show();

        new CallWebService(AppConstants.GET_GC_COUNTRY, CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {

                d.dismiss();

                Type listType = new TypeToken<List<GCCountry>>() {
                }.getType();
                arrCheckCountries = new GsonBuilder().create().fromJson(response, listType);

                for (int i = 0; i < arrCheckCountries.size(); i++) {
                    Log.e("", arrCheckCountries.get(i).CountryName + "");
                }

                CountryAdapter countryAdapter = new CountryAdapter(GenerateGCActivity.this, R.layout.spinner_country, arrCheckCountries);
                spinnerCountryGenerateGc.setAdapter(countryAdapter);

            }

            @Override
            public void error(VolleyError error) {

                d.dismiss();
            }
        }.start();
    }

    private void processCountrySelection(int position) {

        selected_country_id = position;
        temp_posCountrySpinner=position;

        txtCCGenerateGC.setText(String.format("+%s",arrCheckCountries.get(position).CountryCode));

    }

    private void processSelectionWholeReceipient(int position) {

        Receipient resp = receipients.get(position);
        try{

            int toSelection = 0;

            for(int i=0;i<arrCheckCountries.size();i++){

                if(arrCheckCountries.get(i).CountryId == resp.Country){
                    toSelection = i;
                    break;
                }else{
                    continue;
                }

            }
            spinnerCountryGenerateGc.setSelection(toSelection);
        }catch(Exception e){
                e.printStackTrace();
        }
        edMobileNumberGenerateGC.setText(resp.MobileNo);

    }

//    private void fetchCountryAndDisplay() {
//
//
//        new AsyncTask<Void,Void,Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                db_wrapper = new DatabaseWrapper(getActivity());
//                try {
//                    db_wrapper.openDataBase();
//                    countries= db_wrapper.getCountryData();
//                    db_wrapper.close();
//
//
//
//
//                }catch(Exception e){e.printStackTrace();}
//
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                finalCountries = new ArrayList<Country>();
//
//               for(Country country : countries){
//
//                    if(arrCheckCountries.contains(country.CountryName.toString().trim())){
//                        finalCountries.add(country);
//                    }
//
//                }
//
//
//                CountryAdapter countryAdapter = new CountryAdapter(getActivity(),R.layout.spinner_country, finalCountries);
//                spinnerCountryGenerateGc.setAdapter(countryAdapter);
//
//            }
//        }.execute();
//
//    }

    private void fetchReceipientsAndDisplay() {

        new CallWebService(AppConstants.GETRECEIPIENTS +user.UserID,CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {

                Log.e("Receipients List",response);
                if(response == null){

                }else{

                    Type listType=new TypeToken<List<Receipient>>(){
                    }.getType();
                    receipients =  new GsonBuilder().create().fromJson(response, listType);

                    Receipient receipient = new Receipient();
                    receipient.FirstName = "Select";
                    receipient.LastName = "Receipient";
                    receipients.add(0,receipient);

                    ReceipientAdapter countryAdapter = new ReceipientAdapter(GenerateGCActivity.this,R.layout.spinner_country, receipients);
                    spinnerRecipientContactGenerateGc.setAdapter(countryAdapter);
                }

            }

            @Override
            public void error(VolleyError error) {

            }
        }.start();

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_new_generate_gc);
        Log.e("nirav","nsdfnsdklf");
        init();
        getGCCountries();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, "user_pref", 0);
        user = complexPreferences.getObject("current_user", AffilateUser.class);

        receipients = new ArrayList<Receipient>();
        countries = new ArrayList<Country>();

        fetchReceipientsAndDisplay();
//        fetchCountryAndDisplay();


        spinnerCountryGenerateGc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){

                }else{
                    processCountrySelection(position);


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerRecipientContactGenerateGc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){

                }else{

                    processSelectionWholeReceipient(position);


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void init() {

        mainLinear = (LinearLayout)findViewById(R.id.mainlineargenerategc);
        viewService = (View)findViewById(R.id.linearService);
        viewService.setVisibility(View.GONE);
        edAmountGenerateGC = (EditText)findViewById(R.id.edAmountGenerateGC);
        edMobileNumberGenerateGC = (EditText)findViewById(R.id.edMobileNumberGenerateGC);
        edMobileNumberGenerateGC.addTextChangedListener(this);
        edAmountGenerateGC.addTextChangedListener(this);
        btnResetGenerateGC = (TextView)findViewById(R.id.btnResetGenerateGC);
        btnGenerateGCGenerateGC = (TextView)findViewById(R.id.btnGenerateGCGenerateGC);
        btnGenerateGCGenerateGC.setOnClickListener(this);
        btnResetGenerateGC.setOnClickListener(this);
        spinnerRecipientContactGenerateGc = (Spinner)findViewById(R.id.spinnerRecipientContactGenerateGc);
        spinnerCountryGenerateGc = (Spinner)findViewById(R.id.spinnerCountryGenerateGc);
        txtCCGenerateGC = (TextView)findViewById(R.id.txtCCGenerateGC);

/*
        edAmountGenerateGC.addTextChangedListener(new TextWatcher() {

            private String current = "";


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


             */
/*  if(!s.toString().equals(current)){

                    edAmountGenerateGC.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[,.]", "");
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE);
                    String currencySymbol = formatter.getCurrency().getSymbol();
                    cleanString = cleanString.replace(currencySymbol,"");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(Locale.FRANCE).format((parsed/100));

                    current = formatted;
                    edAmountGenerateGC.setText(formatted);

                    edAmountGenerateGC.setSelection(formatted.length());
                    edAmountGenerateGC.addTextChangedListener(this);
                }*//*


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

*/



    }




    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {

        activeReset();
    }

    public void activeReset(){

        btnResetGenerateGC.setEnabled(true);
        btnResetGenerateGC.setBackgroundColor(getResources().getColor(R.color.blue));
    }
    public void passiveReset(){

        btnResetGenerateGC.setEnabled(false);
        btnResetGenerateGC.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnGenerateGCGenerateGC:

                if(viewService.isShown()){

                    processGenerate();

                }else{

                    checkProcess();

                  /*  int indexCountry = spinnerCountryGenerateGc.getSelectedItemPosition();

                    if(indexCountry == 0){
                        SnackBar bar = new SnackBar(getActivity(),"Please Select Country");
                        bar.show();

                    }else{
                        checkProcess();
                    }*/
                }

                break;

            case R.id.btnResetGenerateGC:

                if(viewService.isShown()){
                    setupMain();
                }else{
                    resetAll();
                }
                break;
        }
    }

    private void checkProcess() {

        ArrayList<View> arr = new ArrayList<>();
        arr.add(edMobileNumberGenerateGC);
        arr.add(edAmountGenerateGC);

        new FormValidator(new FormValidator.ResultValidationListner() {
            @Override
            public void complete() {

                if(edMobileNumberGenerateGC.getText().length() == 9 || edMobileNumberGenerateGC.getText().length() == 10 ){

                    final CircleDialog circleDialog=new CircleDialog(GenerateGCActivity.this,0);
                    circleDialog.setCancelable(true);
                    circleDialog.show();
                    String postfix = user.UserID+"/"+edAmountGenerateGC.getText().toString()+"/"+arrCheckCountries.get(selected_country_id).CountryId;
                    Log.e("Pre Service charge link ",AppConstants.SERVICE_CHARGE+postfix);

                    new CallWebService(AppConstants.SERVICE_CHARGE+postfix, CallWebService.TYPE_JSONOBJECT) {

                        @Override
                        public void response(String response) {
                            circleDialog.dismiss();
                            Log.e("---- Service Charge Response ",response);

                            charge = new GsonBuilder().create().fromJson(response,ServiceCharge.class);

                            if(charge.ResponseCode.equalsIgnoreCase("1")){
                                if(validateChagresAndDisplay() == true){
                                    processDialog();
                                }
                            }else{
//                                            SnackBar bar = new SnackBar(getActivity(),charge.ResponseMsg);
//                                            bar.show();
                                SimpleToast.ok(GenerateGCActivity.this,charge.ResponseMsg);
                            }
                        }

                        @Override
                        public void error(VolleyError error) {
                            circleDialog.dismiss();
//                                        SnackBar bar = new SnackBar(getActivity(),"Error");
//                                        bar.show();
                            SimpleToast.error(GenerateGCActivity.this,"Error");
                        }
                    }.start();

                }else{
//                                SnackBar bar = new SnackBar(getActivity(),"Enter valid number");
//                                bar.show();
                    SimpleToast.error(GenerateGCActivity.this,"Enter valid number");

                }

            }

            @Override
            public void error(String error) {

//                            SnackBar bar = new SnackBar(getActivity(),String.format("Please enter %s",error));
//                            bar.show();
                SimpleToast.error(GenerateGCActivity.this,String.format("Please enter %s",error));
            }
        }).validate(arr);
    }

    private void processDialog() {

      /*  Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.item_dialog_generategc,null);
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();*/

        setupService();


    }

    private boolean validateChagresAndDisplay(){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(GenerateGCActivity.this, "user_pref", 0);
        user = complexPreferences.getObject("current_user", AffilateUser.class);
        boolean isComplete = false;
        double value = Double.parseDouble(edAmountGenerateGC.getText().toString());
        double user_value = Double.parseDouble(user.LemonwayBal);

        if(value<charge.MinLimit){

            isComplete = false;
            edAmountGenerateGC.setError("Minimum Amount is € "+charge.MinLimit+" For This Service");

        }else if(value > charge.MaxLimit){

            isComplete = false;
            edAmountGenerateGC.setError("Maximum Amount is € "+charge.MaxLimit+" For This Service");


        }else if(value>user_value){

            isComplete = false;
            edAmountGenerateGC.setError("Insufficient balance");

        }else{
            isComplete = true;
        }

        return isComplete;
    }


    private void setupMain(){

        mainLinear.setVisibility(View.VISIBLE);
        viewService.setVisibility(View.GONE);
        btnResetGenerateGC.setText("Reset");
        btnGenerateGCGenerateGC.setText("Check Price");
        refreshBalance();
    }

    public void refreshBalance(){

//        ((MyDrawerActivity)getActivity()).setToolTitle("Hi, "+user.FName);
//        ((MyDrawerActivity)getActivity()).showToolLoading();

        new CallWebService(AppConstants.USER_DETAILS+user.UserID,CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {

                Log.e("Response User Details ",response);
                try{

                    JSONObject obj = new JSONObject(response);
                    try{
//                        ((MyDrawerActivity)getActivity()).setToolSubTitle("Balance "+getResources().getString(R.string.euro)+" "+obj.getString("LemonwayBal"));
//                        ((MyDrawerActivity)getActivity()).hideToolLoading();
                    }catch(Exception e){

                    }

                }catch(Exception e){

                }

            }

            @Override
            public void error(VolleyError error) {
//                ((MyDrawerActivity)getActivity()).hideToolLoading();

            }
        }.start();


    }

    private void setupService(){

        mainLinear.setVisibility(View.GONE);
        viewService.setVisibility(View.VISIBLE);
        btnResetGenerateGC.setText("Back");
        btnGenerateGCGenerateGC.setText("Generate GC");

        TextView txtMobileGenerateGCService = (TextView)viewService.findViewById(R.id.txtMobileGenerateGCService);
        TextView txtAmountGenerateGCService = (TextView)viewService.findViewById(R.id.txtAmountGenerateGCService);
        TextView txtPayableAmountGenerateGCService = (TextView)viewService.findViewById(R.id.txtPayableAmountGenerateGCService);

        TextView txtPaylabasChargeGenerateGCService = (TextView)viewService.findViewById(R.id.txtPaylabasChargeGenerateGCService);
        TextView txtTransactionChargeGenerateGCService = (TextView)viewService.findViewById(R.id.txtTransactionChargeGenerateGCService);

        TextView txtReceipientGets = (TextView)viewService.findViewById(R.id.txtReceipientGets);
        TextView txtExchangeRate = (TextView)viewService.findViewById(R.id.txtExchangeRate);

        double percentageCharge = charge.PercentageCharge;
        double amount = Double.parseDouble(edAmountGenerateGC.getText().toString());
        double displayPercentageCharge = (amount*percentageCharge)/100;
        DecimalFormat df = new DecimalFormat("#.##");
        txtTransactionChargeGenerateGCService.setText(getResources().getString(R.string.euro)+" "+String.format("%1$.2f",displayPercentageCharge));
        txtMobileGenerateGCService.setText(edMobileNumberGenerateGC.getText().toString());
        txtAmountGenerateGCService.setText(getResources().getString(R.string.euro)+" "+edAmountGenerateGC.getText().toString());

        txtPayableAmountGenerateGCService.setText(getResources().getString(R.string.euro)+" "+charge.PayableAmount);

        txtPaylabasChargeGenerateGCService.setText(getResources().getString(R.string.euro)+" "+charge.FixCharge);


        txtExchangeRate.setText(String.format("Exchange rate :\n1 EUR = %s",charge.LiveRate));

        double totalreceipientget = Double.parseDouble(edAmountGenerateGC.getText().toString()) * Double.parseDouble(charge.LiveRate.split(" ")[0].toString());

        double x= Double.parseDouble(charge.LiveRate.split(" ")[0].toString());
        String dx=df.format(totalreceipientget);
        totalreceipientget=Double.valueOf(dx);
        txtReceipientGets.setText(""+totalreceipientget+" "+charge.LiveRate.split(" ")[1].toString());
    }

    private Bitmap getBitmapFromAsset(String strName)
    {
        AssetManager assetManager = GenerateGCActivity.this.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }

    private void processGenerate() {
        try{

            JSONObject generateObject = new JSONObject();

     /*             "CountryCode":"String content",
                    "GCAmount":12678967.543233,
                    "MobileNo":"String content",
                    "ResponseCode":"String content",
                    "ResponseMsg":"String content",
                    "SenderID":9223372036854775807    */
//            generateObject.put("CountryCode", 223+"");
            generateObject.put("CountryCode", txtCCGenerateGC.getText().toString().replace("+","").trim());
            generateObject.put("GCAmount",edAmountGenerateGC.getText().toString().trim());
            generateObject.put("MobileNo",edMobileNumberGenerateGC.getText().toString().trim());
            generateObject.put("ResponseCode","");
            generateObject.put("ResponseMsg","");
            generateObject.put("SenderID",user.UserID);
            generateObject.put("CountryID",arrCheckCountries.get(spinnerCountryGenerateGc.getSelectedItemPosition()).CountryId);
            double newLocalValue=arrCheckCountries.get(spinnerCountryGenerateGc.getSelectedItemPosition()).LiveRate*Double.parseDouble(edAmountGenerateGC.getText().toString().trim());
            DecimalFormat df = new DecimalFormat("#.##");
            newLocalValue = Double.valueOf(df.format(newLocalValue));
            generateObject.put("LocalValueReceived",newLocalValue);
            generateObject.put("LocalValueReceivedCurrancy",arrCheckCountries.get(spinnerCountryGenerateGc.getSelectedItemPosition()).CurrencyName);

            Log.e("gc json obj",generateObject.toString());

            final CircleDialog circleDialog=new CircleDialog(GenerateGCActivity.this,0);
            circleDialog.setCancelable(true);
            circleDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.GENERATE_GC, generateObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {

                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response Generate GC: ", "" + response);
                    try{
                        JSONObject obj = new JSONObject(response);
                        String responsecode = obj.getString("ResponseCode");

                        if(responsecode.equalsIgnoreCase("1")){
                            //TODO
//                            SnackBar bar = new SnackBar(getActivity(),"Gift code generated Successfully");
//                            bar.show();
                            SimpleToast.ok(GenerateGCActivity.this,"Gift code generated Successfully");
//                            processCheckMobileExists();
                            //resetAll();
//                            FragmentManager manager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction ft = manager.beginTransaction();
//                            ft.replace(R.id.main_gc_view,new MyAccountFragment());
//                            ft.commit();
                            finish();

                       /*     FragmentManager fm = getActivity().getSupportFragmentManager();
                            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                fm.popBackStack();
                            }*/

                            //   setupMain();
/*
                            try {
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                MyAccountFragment frag = (MyAccountFragment) manager.findFragmentByTag("MA");
                                if (frag != null) {
                                    frag.refreshBalance();
                                }
                            }catch (Exception e){};*/

                        }else{

                            String errorMSG = "";
                            if(responsecode.equalsIgnoreCase("-2")){
                                errorMSG = "Error In While Generating GiftCode";
                            }else if(responsecode.equalsIgnoreCase("-1")){
                                errorMSG = "Error";
                            }else if(responsecode.equalsIgnoreCase("2")){
                                processCheckMobileExists();
                                errorMSG = "You cannot send money to yourself";
                            }else if(responsecode.equalsIgnoreCase("3")){
                                errorMSG = "User will blocked for next 24 hours";
                            }else if(responsecode.equalsIgnoreCase("4")){
                                errorMSG = "User Deleted";
                            }else if(responsecode.equalsIgnoreCase("5")){
                                errorMSG = "User is not verified";
                            }else{
                                errorMSG = "Network Error\nPlease try again";
                            }
//                            SnackBar bar = new SnackBar(getActivity(),errorMSG);
//                            bar.show();
                            SimpleToast.error(GenerateGCActivity.this,errorMSG+"");

                            setupMain();
                            //  resetAll();
                        }

                    }catch(Exception e){

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    circleDialog.dismiss();

//                    SnackBar bar = new SnackBar(getActivity(),"Network Error");
//                    bar.show();
                    SimpleToast.error(GenerateGCActivity.this,"Network Error");


                }
            });

            req.setRetryPolicy(
                    new DefaultRetryPolicy(0,0,0));

            MyApplication.getInstance().addToRequestQueue(req);

        }catch (Exception e){

        }
    }
    private void processCheckMobileExists(){


        if(checkIfExistsOrNot()){

        }else{


            AlertDialog.Builder alert = new AlertDialog.Builder(GenerateGCActivity.this);
            alert.setTitle("Add Recipient");
            alert.setMessage("Would you like to add this contact as your Recipient ?");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Intent i = new Intent(GenerateGCActivity.this, AddRecipientActivity.class);
//
//                    i.putExtra("CountryID",(int)arrCheckCountries.get(temp_posCountrySpinner).CountryId);
//                    i.putExtra("Mobileno",edMobileNumberGenerateGC.getText().toString());
//                    startActivity(i);


                }
            });
            alert.show();
        }
    }

    private boolean checkIfExistsOrNot() {

        boolean isExists = false;
        Log.e("mobile  ",edMobileNumberGenerateGC.getText().toString());
        Log.e("size of recipient  ",String.valueOf(receipients.size()));
        String mob=edMobileNumberGenerateGC.getText().toString();

        for(int i=1;i<receipients.size();i++){
            String temp = receipients.get(i).MobileNo;
            if(temp.equals(mob))
            {
                isExists = true;
                return isExists;
            }
            else{
                isExists = false;
            }
        }

       /* for(Receipient receipient:receipients){

            Log.e("values  ",receipient.MobileNo);

            if(receipient.MobileNo.equalsIgnoreCase(edMobileNumberGenerateGC.getText().toString())){
                Log.e("if block ","if block");
                isExists = true;
                return isExists;

            }else{
                isExists = false;
                Log.e("else block ","else block");
            }

        }*/
        return isExists;

    }

    private void resetAll() {

        edAmountGenerateGC.setText("");
        edMobileNumberGenerateGC.setText("");
        spinnerCountryGenerateGc.setSelection(0);
        spinnerRecipientContactGenerateGc.setSelection(0);
        passiveReset();

    }

    public class ReceipientAdapter extends ArrayAdapter<Receipient> {
        Context context;
        int layoutResourceId;
        ArrayList<Receipient> values;
        // int android.R.Layout.
        public ReceipientAdapter(Context context, int resource, ArrayList<Receipient> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(GenerateGCActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            if(position == 0){
                txt.setText(values.get(position).FirstName +" "+values.get(position).LastName);

            }else{
                txt.setText(values.get(position).FirstName +" "+values.get(position).LastName + String.format("(+%s %s)",values.get(position).CountryCode,values.get(position).MobileNo));

            }
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(GenerateGCActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            if(position == 0){
                txt.setText(values.get(position).FirstName +" "+values.get(position).LastName);

            }else{
                txt.setText(values.get(position).FirstName +" "+values.get(position).LastName + String.format("( +%s %s)",values.get(position).CountryCode,values.get(position).MobileNo));

            }

            return  txt;
        }
    }


    public class CountryAdapter extends ArrayAdapter<GCCountry> {
        Context context;
        int layoutResourceId;
        ArrayList<GCCountry> values;
        // int android.R.Layout.
        public CountryAdapter(Context context, int resource, ArrayList<GCCountry> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(GenerateGCActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryName);

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 16;
            LinearLayout.LayoutParams params_image = new LinearLayout.LayoutParams(56,32);

            ImageView img = new ImageView(context);
            try{
                img.setImageBitmap(getBitmapFromAsset(values.get(position).CountryName.toString().trim()+"-flag.png"));
            }catch(Exception e){

            }
            img.setImageBitmap(getBitmapFromAsset(values.get(position).CountryName.toString().trim()+"-flag.png"));

            layout.addView(img,params_image);
            layout.addView(txt,params);


            return  layout;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(GenerateGCActivity.this);

            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryName);

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 16;
            LinearLayout.LayoutParams params_image = new LinearLayout.LayoutParams(56,32);

            ImageView img = new ImageView(context);

            try{
                img.setImageBitmap(getBitmapFromAsset(values.get(position).CountryName.toString().trim()+"-flag.png"));
            }catch(Exception e){

            }

            layout.addView(img,params_image);
            layout.addView(txt,params);
            return  layout;
        }
    }

    public static class ServiceCharge{

        @SerializedName("FixCharge")
        public double FixCharge;
        @SerializedName("LemonwayBal")
        public String LemonwayBal;
        @SerializedName("MaxLimit")
        public double MaxLimit;
        @SerializedName("MinLimit")
        public double MinLimit;
        @SerializedName("PayableAmount")
        public double PayableAmount;
        @SerializedName("PercentageCharge")
        public double PercentageCharge;
        @SerializedName("ResponseCode")
        public String ResponseCode;
        @SerializedName("ResponseMsg")
        public String ResponseMsg;
        @SerializedName("LiveRate")
        public String LiveRate;

    }


}
