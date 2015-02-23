package com.webmyne.paylabasmerchant.ui;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.FrameLayout;
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
import com.webmyne.paylabasmerchant.ui.widget.InternationalNumberValidation;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.RegionUtils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link CombineGCFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CombineGCFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ArrayList<Country> countries;
    private LinearLayout linearCombineGiftCode;
    private TextView btnAddCombineGiftCode;
    private TextView btnCombineGcCombineGc;
    private EditText edUserMobile;
    private AffilateUser user;
    private ArrayList<String> combine_giftcode_list;
    private ArrayList<GCCountry> countryList;
    private Spinner spGCCountry,spCountry;
    private JSONObject responseObject;
    private GCCountryAdapter gcCountryAdapter;
    private double localOldtextValue;

    public static CombineGCFragment newInstance(String param1, String param2) {
        CombineGCFragment fragment = new CombineGCFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CombineGCFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.fragment_combine_gc, container, false);
        init(convertView);
        return convertView;
    }

    private void init(View convertView) {
        linearCombineGiftCode = (LinearLayout) convertView.findViewById(R.id.linearCombineGiftCode);
        btnAddCombineGiftCode = (TextView) convertView.findViewById(R.id.btnAddCombineGiftCode);
        btnAddCombineGiftCode.setOnClickListener(this);
        btnCombineGcCombineGc = (TextView) convertView.findViewById(R.id.btnCombineGcCombineGc);
        btnCombineGcCombineGc.setOnClickListener(this);
        spGCCountry = (Spinner) convertView.findViewById(R.id.spGCCountry);
        spCountry = (Spinner) convertView.findViewById(R.id.spCountry);

        edUserMobile=(EditText)convertView.findViewById(R.id.edUserMobile);
        spGCCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                processCountrySelection(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void processCountrySelection(int position) {

        for (int i = 0; i < linearCombineGiftCode.getChildCount(); i++) {

            LinearLayout layout = (LinearLayout) linearCombineGiftCode.getChildAt(i);
            EditText ed = (EditText) layout.findViewById(R.id.entergiftcode_combinegiftcode);
            TextView oldText = (TextView) layout.findViewById(R.id.txtAmountGCCombineGC);
            TextView newText = (TextView) layout.findViewById(R.id.txtNewAmountGCCombineGC);

            try {
                double oldValue = Double.parseDouble(oldText.getText().toString().split(" ")[0]);
//                double newValue = oldValue * countryList.get(position).LiveRate;
                GCCountry selectedCountry = countryList.get(spGCCountry.getSelectedItemPosition());
                double newValue=0.0d;
                if(selectedCountry.CurrencyName.toString().trim().equalsIgnoreCase(responseObject.getString("LocalValueReceivedCurrancy").trim())){
                    newValue = localOldtextValue ;
                } else {
                    newValue = oldValue * selectedCountry.LiveRate;
                }
                DecimalFormat df = new DecimalFormat("#.##");
                newValue = Double.valueOf(df.format(newValue));
                newText.setText("" + newValue + " " + countryList.get(position).CurrencyName);

            } catch (Exception e) {

            }
           /*if(TextUtils.isDigitsOnly(oldText.getText().toString().split(" ")[0])){

               double oldValue = Double.parseDouble(oldText.getText().toString().split(" ")[0]);
               double newValue = oldValue * countryList.get(position).LiveRate;
               DecimalFormat df = new DecimalFormat("#.##");
               newValue = Double.valueOf(df.format(newValue));
               newText.setText(""+newValue+" "+countryList.get(position).CurrencyName);


           }
*/

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        fetchCountries();
        getGCCountries();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", AffilateUser.class);
        try {
            linearCombineGiftCode.removeAllViews();
        } catch (Exception e) {

        }


        addCombineStrip(false);
        addCombineStrip(false);

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

                for (int i = 0; i < countryList.size(); i++) {
                    Log.e("", countryList.get(i).CountryName + "");
                    Log.e("", countryList.get(i).LiveRate + "");
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

    private void addCombineStrip(boolean isDeleteVisible) {

        View vStrip = getActivity().getLayoutInflater().inflate(R.layout.item_combinegiftcode, null);
        vStrip.setTag(linearCombineGiftCode.getChildCount());
        TextView txtDelete = (TextView) vStrip.findViewById(R.id.btnDeleteCombineGiftCode);
        if (isDeleteVisible == false) {
            txtDelete.setVisibility(View.INVISIBLE);
        } else {
            txtDelete.setVisibility(View.VISIBLE);
        }

        txtDelete.setOnClickListener(deleteListner);
        final EditText edEnterGiftCode = (EditText) vStrip.findViewById(R.id.entergiftcode_combinegiftcode);
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

                    LinearLayout first = (LinearLayout) edEnterGiftCode.getParent().getParent();
                    TextView ed = (TextView) first.findViewById(R.id.txtAmountGCCombineGC);
                    TextView txtNewAmountGCCombineGC = (TextView) first.findViewById(R.id.txtNewAmountGCCombineGC);
                    if(edUserMobile.getText().toString().trim().length()==0){
                        edEnterGiftCode.setText("");
                        edUserMobile.setError(getResources().getString(R.string.code_combinegcfragment_PLEASEENTERMOBILE));
                        edUserMobile.requestFocus();
                    }else {
                        processFetchValue(edEnterGiftCode.getText().toString(), ed, edEnterGiftCode, txtNewAmountGCCombineGC);
                    }



                }
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearCombineGiftCode.addView(vStrip, params);
        linearCombineGiftCode.invalidate();

    }

    boolean duplicates(final ArrayList<String> arr) {
        Set<String> lump = new HashSet<String>();
        for (String i : arr) {
            if (lump.contains(i)) return true;
            lump.add(i);
        }
        return false;
    }

    private void processFetchValue(String code, final TextView index, final EditText ed, final TextView newText) {

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

                            index.setText(jobj.getString("LocalValueReceived") + " " + jobj.getString("LocalValueReceivedCurrancy"));

                            GCCountry selectedCountry = countryList.get(spGCCountry.getSelectedItemPosition());
                            double oldValue = Double.parseDouble(jobj.getString("GCAmount"));
                            double localoldvalue =  Double.parseDouble(jobj.getString("LocalValueReceived"));
                            double newValue=0.0d;
                            localOldtextValue=localoldvalue;
                            if(selectedCountry.CurrencyName.toString().trim().equalsIgnoreCase(jobj.getString("LocalValueReceivedCurrancy").trim())){
                                newValue = localoldvalue ;
                            } else {

                                Log.e("old value",oldValue+"");
                                Log.e("live rate",selectedCountry.LiveRate+"");

                                newValue = oldValue * selectedCountry.LiveRate;
                                Log.e("new value",newValue+"");
                            }
                            DecimalFormat df = new DecimalFormat("#.##");
                            newValue = Double.valueOf(df.format(newValue));
                            newText.setText("" + Double.valueOf(df.format(newValue)) + " " + selectedCountry.CurrencyName);


                        } else {

                            ed.setText("");
                            ed.setError(jobj.getString("ResponseMsg"));
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
                    SimpleToast.error(getActivity(),error.getMessage()+"");

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAddCombineGiftCode:
                processAddCombineStrips();
                break;
            case R.id.btnCombineGcCombineGc:
                if(InternationalNumberValidation.isPossibleNumber(edUserMobile.getText().toString().toString(), countryList.get(spGCCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){
                SimpleToast.error(getActivity(), getResources().getString(R.string.code_combinegcfragment_ERRORENTERMOBILENO));
            }else if(InternationalNumberValidation.isValidNumber(edUserMobile.getText().toString().toString(), countryList.get(spGCCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){
                SimpleToast.error(getActivity(), getResources().getString(R.string.code_combinegcfragment_ERROENTERVALIDMONILENUMBER));
            } else {
                    processCombine();
                }
                break;

        }
    }

    private void processCombine() {

        if (isPassedFromValidationProcess()) {

            try {

                JSONObject jMain = new JSONObject();
                JSONArray arr = new JSONArray();
                double newLocalValue= 0.0d;

                for (int i = 0; i < linearCombineGiftCode.getChildCount(); i++) {
                    LinearLayout layout = (LinearLayout) linearCombineGiftCode.getChildAt(i);
                    EditText ed = (EditText) layout.findViewById(R.id.entergiftcode_combinegiftcode);
                    TextView newText = (TextView) layout.findViewById(R.id.txtNewAmountGCCombineGC);
                    newLocalValue = newLocalValue + Double.parseDouble(newText.getText().toString().split(" ")[0]);
                    JSONObject jobj = new JSONObject();
                    jobj.put("GiftCode", ed.getText().toString());
                    arr.put(jobj);
                }

                //todo change service and values
                jMain.put("UserMobileNo", edUserMobile.getText().toString().trim());

                jMain.put("UserCountryCode",countries.get(spCountry.getSelectedItemPosition()).CountryCode + "");

                jMain.put("AffiliateID", user.UserID);
                jMain.put("GiftCode", arr);
                jMain.put("SenderID", user.UserID);
                DecimalFormat df = new DecimalFormat("#.##");
                newLocalValue = Double.valueOf(df.format(newLocalValue));
                jMain.put("NewLocalValueReceived", newLocalValue + "");
                jMain.put("NewLocalValueReceivedCurrancy", countryList.get(spGCCountry.getSelectedItemPosition()).CurrencyName + "");
                Log.e("----------------- jMAIN ", "" + jMain.toString());

                try {

                    final CircleDialog d = new CircleDialog(getActivity(), 0);
                    d.setCancelable(true);
                    d.show();

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.COMBINE_GC, jMain, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject jobj) {

                            try {
                                String response = jobj.toString();
                                Log.e("Response : ", "" + response);
                                if (jobj.getString("ResponseCode").equalsIgnoreCase("1")) {
//                                    SnackBar bar = new SnackBar(getActivity(), "Gift Code Combined");
//                                    bar.show();
                                    SimpleToast.ok(getActivity(), getResources().getString(R.string.code_combinegcfragment_GIFTCODECOMBINED));
                                    clearAll();

                                    getActivity().finish();

                                } else {
//                                    SnackBar bar = new SnackBar(getActivity(), jobj.getString("ResponseMsg"));
//                                    bar.show();
                                    SimpleToast.error(getActivity(),jobj.getString("ResponseMsg")+"error ");
                                }

                                d.dismiss();


                            } catch (Exception e) {

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            d.dismiss();
                            Log.e("error responsegg: ", error + "");
//                            SnackBar bar = new SnackBar(getActivity(), error.getMessage());
//                            bar.show();
                            SimpleToast.error(getActivity(),error.getMessage());

                        }
                    });
                    req.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));

                    MyApplication.getInstance().addToRequestQueue(req);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



    public boolean isPassedFromValidationProcess() {

        boolean isPassed = false;
        ArrayList<String> gcs = new ArrayList<String>();

        for (int i = 0; i < linearCombineGiftCode.getChildCount(); i++) {

            LinearLayout layout = (LinearLayout) linearCombineGiftCode.getChildAt(i);
            EditText ed = (EditText) layout.findViewById(R.id.entergiftcode_combinegiftcode);
            gcs.add(ed.getText().toString());

            if (ed.getText().toString().equalsIgnoreCase("")) {
                ed.setError(getResources().getString(R.string.code_combinegcfragment_ENTERGC));
                isPassed = false;
                return isPassed;

            } else if (duplicates(gcs) == true) {

//                SnackBar bar = new SnackBar(getActivity(), "Can not combine same gift codes");
//                bar.show();
                SimpleToast.error(getActivity(), getResources().getString(R.string.code_combinegcfragment_CANNOTCOMBINESAMEGIFTCODES));
                isPassed = false;
                return isPassed;

            } else {

                if (ed.getText().toString().length() == 9) {
                    isPassed = true;
                    continue;

                } else {
                    ed.setError(getResources().getString(R.string.code_combinegcfragment_ENTERVALIDGC));
                    isPassed = false;
                    return isPassed;
                }
            }
        }

        return isPassed;
    }

    private boolean matchPreviousGCS(String s, ArrayList<String> gcs) {

        boolean isMatch = false;

        for (String sgc : gcs) {

            if (s.equals(sgc)) {

                isMatch = true;
                return isMatch;
            }
        }

        return isMatch;
    }

    private void clearAll() {


        for (int i = 0; i < linearCombineGiftCode.getChildCount(); i++) {

            LinearLayout layout = (LinearLayout) linearCombineGiftCode.getChildAt(i);
            EditText ed = (EditText) layout.findViewById(R.id.entergiftcode_combinegiftcode);
            ed.setText("");
        }
    }

    private View.OnClickListener deleteListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout fp = (LinearLayout) v.getParent();
            LinearLayout second = (LinearLayout) fp.getParent();
            LinearLayout first = (LinearLayout) second.getParent();
            LinearLayout third = (LinearLayout) first.getParent();
            linearCombineGiftCode.removeViewAt(linearCombineGiftCode.indexOfChild(third));
            linearCombineGiftCode.invalidate();
        }
    };

    private void processAddCombineStrips() {

        if (linearCombineGiftCode.getChildCount() == 5) {

        } else {
            addCombineStrip(true);
        }


    }

   /* public class GCCountryAdapter extends ArrayAdapter<GCCountry> {
        Context context;
        int layoutResourceId;
        ArrayList<GCCountry> values = new ArrayList<GCCountry>();

        // int android.R.Layout.
        public GCCountryAdapter(Context context, int resource, ArrayList<GCCountry> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values.clear();
            this.values.addAll(objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setPadding(16, 16, 16, 16);
            txt.setGravity(Gravity.CENTER_VERTICAL);

            txt.setText(values.get(position).CountryName + "");

            return txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16, 16, 16, 16);

            txt.setText(values.get(position).CountryName + "");


            return txt;
        }
    }*/

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




//    public class GCCountryAdapter extends ArrayAdapter<GCCountry> {
//        Context context;
//        int layoutResourceId;
//        ArrayList<GCCountry> values;
//
//        // int android.R.Layout.
//        public GCCountryAdapter(Context context, int resource, ArrayList<GCCountry> objects) {
//            super(context, resource, objects);
//            this.context = context;
//            this.values = objects;
//        }
//
//        @Override
//        public View getDropDownView(int position, View convertView, ViewGroup parent) {
//
//            TextView txt = new TextView(getActivity());
//            txt.setPadding(16, 16, 16, 16);
//            txt.setGravity(Gravity.CENTER_VERTICAL);
//            txt.setText(values.get(position).CountryName);
//
//            LinearLayout layout = new LinearLayout(context);
//            layout.setOrientation(LinearLayout.HORIZONTAL);
//            layout.setGravity(Gravity.CENTER_VERTICAL);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.leftMargin = 16;
//            LinearLayout.LayoutParams params_image = new LinearLayout.LayoutParams(56, 32);
//
//            ImageView img = new ImageView(context);
//            try {
//                img.setImageBitmap(getBitmapFromAsset(values.get(position).CountryName.toString().trim() + "-flag.png"));
//            } catch (Exception e) {
//
//            }
//            img.setImageBitmap(getBitmapFromAsset(values.get(position).CountryName.toString().trim() + "-flag.png"));
//
//            layout.addView(img, params_image);
//            layout.addView(txt, params);
//
//
//            return layout;
//        }
//
//
//    }

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

