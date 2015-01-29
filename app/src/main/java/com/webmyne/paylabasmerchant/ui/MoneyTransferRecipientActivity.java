package com.webmyne.paylabasmerchant.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.City;
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.model.Receipient;
import com.webmyne.paylabasmerchant.model.State;
import com.webmyne.paylabasmerchant.ui.widget.CallWebService;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.InternationalNumberValidation;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.DatabaseWrapper;
import com.webmyne.paylabasmerchant.util.PrefUtils;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class MoneyTransferRecipientActivity extends ActionBarActivity {

    Toolbar toolbar_actionbar;
    TextView btnAddRecipient;
    TextView txtSelectRecipient;
    Spinner spCountry,spState,spCity;
    EditText edFirstname,edLastname,edEmail,edAddress,edZipcode,edCountryCode,edMobileno;

    private ArrayList<Receipient> receipients;

    private AffilateUser user;

    ArrayList<Country> countrylist;
    ArrayList<State> statelist;
    ArrayList<City> cityList;
    int temp_CountryID;
    int temp_CountryID1;
    int temp_StateID;
    int temp_CityID;
    int getCountryID;
    int RecipientId;
    private DatabaseWrapper db_wrapper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer_recipient);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle("MoneyTransfer");
            toolbar_actionbar.setNavigationIcon(R.drawable.icon_back);
            toolbar_actionbar.setBackgroundColor(getResources().getColor(R.color.theme_primary));

            setSupportActionBar(toolbar_actionbar);

        }
        toolbar_actionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  finish();
            }
        });

        intView();



    }

private void intView(){
    btnAddRecipient = (TextView)findViewById(R.id.btnAddRecipient);

    edFirstname = (EditText)findViewById(R.id.edFirstname);
    edLastname = (EditText)findViewById(R.id.edLastname);
    edAddress = (EditText)findViewById(R.id.edAddress);
    edZipcode  = (EditText)findViewById(R.id.edZipcode);
    edCountryCode  = (EditText)findViewById(R.id.edCountryCode);
    edEmail = (EditText)findViewById(R.id.edEmail);
    edMobileno = (EditText)findViewById(R.id.edMobileno);

    spCountry = (Spinner)findViewById(R.id.spCountry);
    spState = (Spinner)findViewById(R.id.spState);
    spCity = (Spinner)findViewById(R.id.spCity);





    btnAddRecipient.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (isEmptyField(edFirstname)) {
                SimpleToast.error(MoneyTransferRecipientActivity.this, "Please Enter First Name !!!");

            } else if (isEmptyField(edLastname)) {
                SimpleToast.error(MoneyTransferRecipientActivity.this, "Please Enter Last Name !!!");
            } else if (isEmptyField(edAddress)) {
                SimpleToast.error(MoneyTransferRecipientActivity.this, "Please Enter Street Address !!!");

            } else if (isEmptyField(edZipcode)) {
                SimpleToast.error(MoneyTransferRecipientActivity.this, "Please Enter Zipcode !!!");

            } else if (!isZipcodeMatch(edZipcode)) {
                SimpleToast.error(MoneyTransferRecipientActivity.this, "Please Enter Valid Zipcode !!!");


            } else if (isMobileMatch(edMobileno)) {
                SimpleToast.error(MoneyTransferRecipientActivity.this, "Please Enter Valid Mobile Number !!!");


            }else if(InternationalNumberValidation.isPossibleNumber(edMobileno.getText().toString().toString(), countrylist.get(spCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){
                SimpleToast.error(MoneyTransferRecipientActivity.this, "Please Enter Valid Mobile Number");
            }else if(InternationalNumberValidation.isValidNumber(edMobileno.getText().toString().toString(), countrylist.get(spCountry.getSelectedItemPosition()).ShortCode.toString().trim())==false){
                SimpleToast.error(MoneyTransferRecipientActivity.this, "Please Enter Valid Mobile Number");
            }

            else {


                if(getIntent().getExtras().get("ObjectValue").toString().equals("Recipient")){

                    MoneyTransferFinalActivity.recObj = new Receipient();
                    MoneyTransferFinalActivity.recObj.FirstName = edFirstname.getText().toString();
                    MoneyTransferFinalActivity.recObj.LastName = edLastname.getText().toString();
                    MoneyTransferFinalActivity.recObj.EmailId = edEmail.getText().toString();
                    MoneyTransferFinalActivity.recObj.Address = edAddress.getText().toString();
                    MoneyTransferFinalActivity.recObj.ZipCode = edZipcode.getText().toString();
                    MoneyTransferFinalActivity.recObj.MobileNo = edMobileno.getText().toString();

                    MoneyTransferFinalActivity.recObj.Country = countrylist.get(spCountry.getSelectedItemPosition()).CountryID;
                    MoneyTransferFinalActivity.recObj.City = cityList.get(spCity.getSelectedItemPosition()).CityID;
                    MoneyTransferFinalActivity.recObj.State = statelist.get(spState.getSelectedItemPosition()).StateID;

                    finish();
                }
                else{
                    MoneyTransferFinalActivity.senObj = new Receipient();
                    MoneyTransferFinalActivity.senObj.FirstName = edFirstname.getText().toString();
                    MoneyTransferFinalActivity.senObj.LastName = edLastname.getText().toString();
                    MoneyTransferFinalActivity.senObj.EmailId = edEmail.getText().toString();
                    MoneyTransferFinalActivity.senObj.Address = edAddress.getText().toString();
                    MoneyTransferFinalActivity.senObj.ZipCode = edZipcode.getText().toString();
                    MoneyTransferFinalActivity.senObj.MobileNo = edMobileno.getText().toString();
                    MoneyTransferFinalActivity.senObj.MobileCountryCode = edCountryCode.getText().toString();


                    MoneyTransferFinalActivity.senObj.Country = countrylist.get(spCountry.getSelectedItemPosition()).CountryID;
                    MoneyTransferFinalActivity.senObj.City = cityList.get(spCity.getSelectedItemPosition()).CityID;
                    MoneyTransferFinalActivity.senObj.State = statelist.get(spState.getSelectedItemPosition()).StateID;

                    finish();
                }


            }
        }
    });



    spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            fetchStateAndDisplay(position+1);
            temp_CountryID1=position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });

    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            temp_CityID=position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });



}

    @Override
    protected void onResume() {
        super.onResume();
        fetchCountryAndDisplay();

    }


private void clearall(){

    fetchCountryAndDisplay();

    edFirstname.setText("");
    edLastname.setText("");
    edEmail.setText("");
    edMobileno.setText("");
    edCountryCode.setText("");
    edAddress.setText("");
    edZipcode.setText("");

}

    public boolean isMobileMatch(EditText param1) {

        boolean isEmpty = false;
        if ((param1.getText() == null || param1.getText().toString().equalsIgnoreCase(""))) {
            isEmpty = true;
        } else if (param1.getText().toString().length() < 9 || param1.getText().toString().length() > 10) {
            isEmpty = true;
        }


        return isEmpty;

    }
 public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }
    public boolean isEmailMatch(EditText param1) {
        // boolean isMatch = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }

    public boolean isZipcodeMatch(EditText param1) {
        boolean isMatch = false;
        if (param1.getText().toString().matches("[a-zA-Z0-9]*")) {
            isMatch = true;
        }
        return isMatch;
    }


private void fetchCountryAndDisplay() {

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(MoneyTransferRecipientActivity.this);
                try {
                    db_wrapper.openDataBase();
                    countrylist= db_wrapper.getCountryData();
                    db_wrapper.close();
                }catch(Exception e){e.printStackTrace();}

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                CountryAdapter countryAdapter = new CountryAdapter(MoneyTransferRecipientActivity.this,R.layout.spinner_country, countrylist);
                spCountry.setAdapter(countryAdapter);
                spCountry.setSelection(getCountryID-1);
            }
        }.execute();

    }
    private void fetchStateAndDisplay(int CountryID) {

        statelist = new ArrayList<State>();
        edCountryCode.setText(String.valueOf(countrylist.get(spCountry.getSelectedItemPosition()).CountryCode));
        temp_CountryID=CountryID;

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(MoneyTransferRecipientActivity.this);
                try {
                    db_wrapper.openDataBase();
                    statelist= db_wrapper.getStateData(temp_CountryID);
                    db_wrapper.close();
                }catch(Exception e){e.printStackTrace();}

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                StateAdapter stateAdapter = new StateAdapter(MoneyTransferRecipientActivity.this,R.layout.spinner_state, statelist);
                spState.setAdapter(stateAdapter);



                spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        temp_StateID=position;
                        fetchAndDisplayCity(statelist.get(position).StateID);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }
        }.execute();
    }

    private void fetchAndDisplayCity(final int stateID) {
        cityList = new ArrayList<City>();
        boolean isAlreadyThere = false;
        db_wrapper = new DatabaseWrapper(MoneyTransferRecipientActivity.this);
        try {
            db_wrapper.openDataBase();
            isAlreadyThere = db_wrapper.isAlreadyInDatabase(stateID);
            db_wrapper.close();
        }catch(Exception e){e.printStackTrace();}

        if(isAlreadyThere == true){

            System.out.println("Cities are already in database");
            new AsyncTask<Void,Void,Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    db_wrapper = new DatabaseWrapper(MoneyTransferRecipientActivity.this);
                    try {
                        db_wrapper.openDataBase();
                        cityList = db_wrapper.getCityData(stateID);
                        db_wrapper.close();
                    }catch(Exception e){e.printStackTrace();}
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    CityAdapter cityAdapter = new CityAdapter(MoneyTransferRecipientActivity.this,R.layout.spinner_country, cityList);
                    spCity.setAdapter(cityAdapter);


                }
            }.execute();


        }else{

            final CircleDialog circleDialog=new CircleDialog(MoneyTransferRecipientActivity.this,0);
            circleDialog.setCancelable(true);
            circleDialog.show();


            System.out.println("Cities are not there");
            new CallWebService(AppConstants.GETCITIES +stateID,CallWebService.TYPE_JSONARRAY) {

                @Override
                public void response(String response) {

                    circleDialog.dismiss();
                    Type listType=new TypeToken<List<City>>(){
                    }.getType();
                    cityList =  new GsonBuilder().create().fromJson(response, listType);
                    CityAdapter cityAdapter = new CityAdapter(MoneyTransferRecipientActivity.this,R.layout.spinner_country, cityList);
                    spCity.setAdapter(cityAdapter);

                    db_wrapper = new DatabaseWrapper(MoneyTransferRecipientActivity.this);
                    try {
                        db_wrapper.openDataBase();
                        db_wrapper.insertCities(cityList);
                        db_wrapper.close();
                    }catch(Exception e){e.printStackTrace();}


                }

                @Override
                public void error(VolleyError error) {

                    circleDialog.dismiss();
                }
            }.start();

        }

    }


    public class CityAdapter extends ArrayAdapter<City> {

        Context context;
        int layoutResourceId;
        ArrayList<City> values;
        // int android.R.Layout.

        public CityAdapter(Context context, int resource, ArrayList<City> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CityName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).CityName);
            return  txt;
        }
    }
    public class StateAdapter extends ArrayAdapter<State>{

        Context context;
        int layoutResourceId;
        ArrayList<State> values;
        // int android.R.Layout.

        public StateAdapter(Context context, int resource, ArrayList<State> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).StateName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).StateName);
            return  txt;
        }
    }
    public class CountryAdapter extends ArrayAdapter<Country> {
        Context context;
        int layoutResourceId;
        ArrayList<Country> values;

        // int android.R.Layout.
        public CountryAdapter(Context context, int resource, ArrayList<Country> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryName);
            return txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16, 16, 16, 16);
            txt.setText(values.get(position).CountryName);
            return txt;
        }
    }

    public class RecipientAdapter extends ArrayAdapter<Receipient> {

        Context context;
        int layoutResourceId;
        ArrayList<Receipient> values;
        // int android.R.Layout.

        public RecipientAdapter(Context context, int resource, ArrayList<Receipient> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).FirstName+" "+values.get(position).LastName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferRecipientActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).FirstName+" "+values.get(position).LastName);
            return  txt;
        }
    }



    //end of main class
}
