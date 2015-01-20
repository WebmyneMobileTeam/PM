package com.webmyne.paylabasmerchant.ui;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.RegionUtils;

import java.util.ArrayList;

public class FragmentCashOUT extends Fragment {

    private EditText edMobileNumber,edCashInAmount,edFormId,edPin;
    private TextView btnNext;
    private Spinner spCountry;
    private ArrayList<Country> countries;


    public static FragmentCashOUT newInstance(String param1, String param2) {
        FragmentCashOUT fragment = new FragmentCashOUT();
        return fragment;
    }

    public FragmentCashOUT() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_cash_out, container, false);

        initView(convertView);
        //edRechargeMobileNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());



        btnNext.setOnClickListener(new View.OnClickListener() {
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
                else if(isEmptyField(edPin)){
                    SimpleToast.error(getActivity(), "Please Enter Pin");
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
        edPin= (EditText)convertView.findViewById(R.id.edPin);

        btnNext = (TextView)convertView.findViewById(R.id.btnNext);
        spCountry= (Spinner)convertView.findViewById(R.id.spCountry);

    }

    @Override
    public void onResume() {
        super.onResume();
        setCountryCode();

    }

    private void processPay(){

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
