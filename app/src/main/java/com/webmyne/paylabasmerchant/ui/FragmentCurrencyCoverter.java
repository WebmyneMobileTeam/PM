package com.webmyne.paylabasmerchant.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.model.GCCountry;
import com.webmyne.paylabasmerchant.ui.widget.CallWebService;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.ComplexPreferences;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.RegionUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class FragmentCurrencyCoverter extends Fragment {

   TextView btnConvert;
    private ArrayList<Country> countries;
     private EditText edUserMobile,edEnterGiftCode;
    private AffilateUser user;
    private ArrayList<String> combine_giftcode_list;
    private ArrayList<GCCountry> countryList;
    private TextView Giftamount,FromAmount,ToAmount;
    private Spinner spGCCountry,spCountry;
    private JSONObject responseObject;
    private GCCountryAdapter gcCountryAdapter;
    private LinearLayout convertContainer;
    public static FragmentCurrencyCoverter newInstance(String param1, String param2) {
        FragmentCurrencyCoverter fragment = new FragmentCurrencyCoverter();

        return fragment;
    }

    public FragmentCurrencyCoverter() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView= inflater.inflate(R.layout.fragment_currency_coverter, container, false);
        btnConvert = (TextView)convertView.findViewById(R.id.btnConvert);
        spCountry = (Spinner) convertView.findViewById(R.id.spCountry);
        spGCCountry = (Spinner) convertView.findViewById(R.id.spGCCountry);
        edUserMobile=(EditText)convertView.findViewById(R.id.etMobileNumber);
        edEnterGiftCode=(EditText)convertView.findViewById(R.id.etGiftCode);
        Giftamount = (TextView)convertView.findViewById(R.id.Giftamount);
        FromAmount = (TextView)convertView.findViewById(R.id.FromAmount);
        ToAmount = (TextView)convertView.findViewById(R.id.ToAmount);
        convertContainer = (LinearLayout)convertView.findViewById(R.id.convertContainer);


        edEnterGiftCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() == 9) {

                     if(edUserMobile.getText().toString().trim().length()==0 || edUserMobile.getText().toString().trim().length()<9){
                        edEnterGiftCode.setText("");
                        edUserMobile.setError("Please Enter Mobile");
                        edUserMobile.requestFocus();
                    }else {
                        processFetchValue(edEnterGiftCode.getText().toString());
                    }



                }
            }
        });

        spGCCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){

                }else {
                    convertContainer.setVisibility(View.VISIBLE);
                    ToAmount.setText(countryList.get(position).CurrencyName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return convertView;
   }

    @Override
    public void onResume() {
        super.onResume();
        convertContainer.setVisibility(View.GONE);
        fetchCountries();
        getGCCountries();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", AffilateUser.class);


    }



    private void getGCCountries() {
        final CircleDialog d = new CircleDialog(getActivity(), 0);
        d.setCancelable(true);
        d.show();

        new CallWebService(AppConstants.GET_GC_COUNTRY, CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {

                d.dismiss();

                Type listType = new TypeToken<List<GCCountry>>() {
                }.getType();
                countryList = new GsonBuilder().create().fromJson(response, listType);
                GCCountry newobj =  new GCCountry();
                newobj.CountryName="Select Country";
                countryList.add(0,newobj);
                for (int i = 0; i < countryList.size(); i++) {
                    Log.e("", countryList.get(i).CountryName + "");
                }

                gcCountryAdapter = new GCCountryAdapter(getActivity(), R.layout.spinner_country, countryList);
                spGCCountry.setAdapter(gcCountryAdapter);

            }

            @Override
            public void error(VolleyError error) {

                d.dismiss();
            }
        }.start();
    }

    private void fetchCountries(){
        new RegionUtils() {

            @Override
            public void response(ArrayList response) {
                countries = response;

                CountryCodeAdapter countryAdapter = new CountryCodeAdapter(getActivity(), R.layout.spinner_country, countries);
                spCountry.setAdapter(countryAdapter);
            }
        }.fetchCountry(getActivity());
    }


    private void processFetchValue(String code) {

        try {

            JSONObject generateObject = new JSONObject();
            generateObject.put("GCText", code);
            generateObject.put("SenderID", 0);
            generateObject.put("UserCountryCode",countries.get(spCountry.getSelectedItemPosition()).CountryCode + "");
            generateObject.put("UserMobileNo", edUserMobile.getText().toString().trim());
            Log.e(" FetchGC detail GC: ", "" + generateObject);
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.GETGCDETAIL, generateObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {

                    responseObject=jobj;
                    String response = jobj.toString();

                    Log.e("Response FetchGC detail GC: ", "" + response);

                    try {

                        // todo
                        JSONObject obj = new JSONObject(response);

                        String responseCode = obj.getString("ResponseCode");

                        if (responseCode.equalsIgnoreCase("1")) {

                            Giftamount.setText(jobj.getString("LocalValueReceived") + " " + jobj.getString("LocalValueReceivedCurrancy"));
                            FromAmount.setText(jobj.getString("LocalValueReceivedCurrancy"));
                            GCCountry selectedCountry = countryList.get(spGCCountry.getSelectedItemPosition());
                            double oldValue = Double.parseDouble(jobj.getString("LocalValueReceived"));
                            double newValue=0.0d;
                            if(selectedCountry.CurrencyName.toString().equalsIgnoreCase(jobj.getString("LocalValueReceivedCurrancy"))){
                                newValue = oldValue ;
                            } else {
                                newValue = oldValue * selectedCountry.LiveRate;
                            }
                            DecimalFormat df = new DecimalFormat("#.##");
                            newValue = Double.valueOf(df.format(newValue));
                            //newText.setText("" + newValue + " " + selectedCountry.CurrencyName);


                        } else {

                          //  ed.setText("");
                          //  ed.setError(jobj.getString("ResponseMsg"));
                          /*  SnackBar bar = new SnackBar(getActivity(),jobj.getString("ResponseMsg"));
                            bar.show();*/

                        }

                    } catch (Exception e) {

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {


//                    SnackBar bar = new SnackBar(getActivity(), error.getMessage());
//                    bar.show();
                    SimpleToast.error(getActivity(), error.getMessage() + "");

                }
            });

            req.setRetryPolicy(
                    new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            MyApplication.getInstance().addToRequestQueue(req);

        } catch (Exception e) {

        }
    }



    public class GCCountryAdapter extends ArrayAdapter<GCCountry> {
        Context context;
        int layoutResourceId;
        ArrayList<GCCountry> values;
        // int android.R.Layout.
        public GCCountryAdapter(Context context, int resource, ArrayList<GCCountry> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
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
            TextView txt = new TextView(getActivity());

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

    private Bitmap getBitmapFromAsset(String strName) {
        AssetManager assetManager = getActivity().getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }
}
