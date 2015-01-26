package com.webmyne.paylabasmerchant.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.City;
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.model.State;
import com.webmyne.paylabasmerchant.ui.widget.CallWebService;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.ComplexPreferences;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.DatabaseWrapper;


import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AddRecipientActivity extends ActionBarActivity {


    private Toolbar toolbar;

    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private  String CountryName;
    private  String getMobileno;
    private ActionBarDrawerToggle drawerToggle;
    private EditText edFirstName;
    private EditText edLastName;
    private EditText edEmail;
    private EditText edMobileno;
    private EditText edCountryCode;
    private Spinner spCountry;
    private Spinner spState;
    private Spinner spCity;
    private TextView btnAddRecipient;

    ArrayList<Country> countrylist;
    ArrayList<State> statelist;
    ArrayList<City> cityList;
    int temp_CountryID;
    int temp_CountryID1;
    int temp_StateID;
    int temp_CityID;
    int getCountryID;
    private DatabaseWrapper db_wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipient);

        getCountryID = getIntent().getIntExtra("CountryID",0);

        initView();

        getMobileno = getIntent().getStringExtra("Mobileno");
        edMobileno.setText(getMobileno);

        if (toolbar != null) {
            toolbar.setTitle("Add Recipient");
            setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AddRecipientActivity.this.finish();
            }
        });



        btnAddRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmptyField(edFirstName)){
//                    SnackBar bar = new SnackBar(AddRecipientActivity.this,"Please Enter First Name");
//                    bar.show();
                    SimpleToast.error(AddRecipientActivity.this,"Please Enter First Name");
                }
                else if(isEmptyField(edLastName)){
//                    SnackBar bar = new SnackBar(AddRecipientActivity.this,"Please Enter Last Name");
//                    bar.show();
                    SimpleToast.error(AddRecipientActivity.this,"Please Enter Last Name");
                }
                else if(isEmptyField(edEmail)){
//                    SnackBar bar = new SnackBar(AddRecipientActivity.this,"Please Enter Email Address");
//                    bar.show();
                    SimpleToast.error(AddRecipientActivity.this,"Please Enter Email Address");
                }
                else if(!isEmailMatch(edEmail)){
//                    SnackBar bar = new SnackBar(AddRecipientActivity.this,"Please Enter Valid Email Address");
//                    bar.show();
                    SimpleToast.error(AddRecipientActivity.this,"Please Enter Valid Email Address");
                }
                else {
                    processVerifyRecipient();
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
        // end of main class
    }


public void processVerifyRecipient(){
    try {

        ComplexPreferences complexPreferences2 = ComplexPreferences.getComplexPreferences(AddRecipientActivity.this, "user_pref", 0);
        AffilateUser user = complexPreferences2.getObject("current_user", AffilateUser.class);

        final JSONObject userObject = new JSONObject();

        Log.e("User id ",String.valueOf(user.UserID));

        userObject.put("EmailID", edEmail.getText().toString().trim());
        userObject.put("MobileNo", edMobileno.getText().toString().trim());
        userObject.put("MobileCountryCode", edCountryCode.getText().toString().trim());
        userObject.put("UserID", user.UserID);

        final long tempUserID= user.UserID;
        final String tempUserEmailID=user.EmailID;

        Log.e("json obj",userObject.toString());
        final CircleDialog circleDialog = new CircleDialog(AddRecipientActivity.this, 0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.VERIFY_RECIPIENT, userObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                circleDialog.dismiss();
                String response = jobj.toString();
                Log.e("Response Addrecipient: ", "" + response);
                try{

                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("ResponseCode").equalsIgnoreCase("1")){

                        AffilateUser currentUser = new GsonBuilder().create().fromJson(response,AffilateUser.class);

                        final JSONObject newRecipientobj = new JSONObject();

                        newRecipientobj.put("FirstName", edFirstName.getText().toString().trim());
                        newRecipientobj.put("LastName", edLastName.getText().toString().trim());


                        newRecipientobj.put("EmailID", edEmail.getText().toString().trim());
                        newRecipientobj.put("MobileNo", edMobileno.getText().toString().trim());
                        newRecipientobj.put("MobileCountryCode", edCountryCode.getText().toString().trim());

                        newRecipientobj.put("CityID", cityList.get(spCity.getSelectedItemPosition()).CityID);
                        newRecipientobj.put("CountryID", countrylist.get(spCountry.getSelectedItemPosition()).CountryID);
                        newRecipientobj.put("StateID", statelist.get(spState.getSelectedItemPosition()).StateID);

                        newRecipientobj.put("RecipientID",0);

                        newRecipientobj.put("UserEmailID",tempUserEmailID);
                        newRecipientobj.put("UserID",tempUserID);


                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddRecipientActivity.this, "user_pref",0);
                        complexPreferences.putObject("new-recipient", newRecipientobj);
                        complexPreferences.commit();

                        // set verification code true
                        SharedPreferences preferences = getSharedPreferences("Recipient", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("VerificationCode", currentUser.VerificationCode.toString());
                        editor.commit();

                        Intent verfiyRecipient = new Intent( AddRecipientActivity.this ,ConfirmRecipientActivity.class );
                        startActivity(verfiyRecipient);
                        finish();

                    }

                    else {
                        if(obj.getString("ResponseCode").equalsIgnoreCase("-2")) {
//                            SnackBar bar112 = new SnackBar(AddRecipientActivity.this, "Error occur ");
//                            bar112.show();
                           SimpleToast.error(AddRecipientActivity.this,"Error occur ");
                        }
                        else if(obj.getString("ResponseCode").equalsIgnoreCase("-1")) {
//                            SnackBar bar112 = new SnackBar(AddRecipientActivity.this, "Error Occur While adding New Recipient");
//                            bar112.show();
                            SimpleToast.error(AddRecipientActivity.this,"Error Occur While adding New Recipient");
                        }
                        else if(obj.getString("ResponseCode").equalsIgnoreCase("2")) {
//                            SnackBar bar112 = new SnackBar(AddRecipientActivity.this, "Mobile No.   already Exist");
//                            bar112.show();
                            SimpleToast.error(AddRecipientActivity.this,"Mobile No.   already Exist");
                        }
                        else if(obj.getString("ResponseCode").equalsIgnoreCase("3")) {
//                            SnackBar bar112 = new SnackBar(AddRecipientActivity.this, "Email Id already Exist");
//                            bar112.show();
                            SimpleToast.error(AddRecipientActivity.this,"Email Id already Exist");
                        }
                        else if(obj.getString("ResponseCode").equalsIgnoreCase("4")) {
//                            SnackBar bar112 = new SnackBar(AddRecipientActivity.this, "Mobile No. & Email Id already Exist");
//                            bar112.show();
                            SimpleToast.error(AddRecipientActivity.this,"Mobile No. & Email Id already Exist");
                        }
                        else{
//                            SnackBar bar112 = new SnackBar(AddRecipientActivity.this, "Time out, Please Try again.");
//                            bar112.show();
                            SimpleToast.error(AddRecipientActivity.this,"Time out, Please Try again.");
                        }

                    }

                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                circleDialog.dismiss();
                Log.e("error Addrecipeint: ", error + "");
//                SnackBar bar = new SnackBar(AddRecipientActivity.this, error.getMessage());
//                bar.show();
                SimpleToast.error(AddRecipientActivity.this,error.getMessage());

            }
        });
        MyApplication.getInstance().addToRequestQueue(req);

    } catch (Exception e) {

    }
}

public boolean isEmptyField(EditText param1){
        boolean isEmpty = false;
        if(param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }
        return isEmpty;
    }
public boolean isEmailMatch(EditText param1){
        // boolean isMatch = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }


private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#494949"));
        toolbar.setNavigationIcon(R.drawable.icon_back);


        btnAddRecipient = (TextView)findViewById(R.id.btnAddRecipient);
        edFirstName = (EditText)findViewById(R.id.edFirstname);
        edLastName = (EditText)findViewById(R.id.edLastname);
        edEmail  = (EditText)findViewById(R.id.edEmail);
        edMobileno = (EditText)findViewById(R.id.edMobileno);
        edCountryCode = (EditText)findViewById(R.id.edCountryCode);
        spCountry = (Spinner)findViewById(R.id.spCountry);
        spState = (Spinner)findViewById(R.id.spState);
        spCity = (Spinner)findViewById(R.id.spCity);

        fetchCountryAndDisplay();
    }

    private void fetchCountryAndDisplay() {

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(AddRecipientActivity.this);
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

                CountryAdapter countryAdapter = new CountryAdapter(AddRecipientActivity.this,R.layout.spinner_country, countrylist);
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

                db_wrapper = new DatabaseWrapper(AddRecipientActivity.this);
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
                StateAdapter stateAdapter = new StateAdapter(AddRecipientActivity.this,R.layout.spinner_state, statelist);
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
        db_wrapper = new DatabaseWrapper(AddRecipientActivity.this);
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
                    db_wrapper = new DatabaseWrapper(AddRecipientActivity.this);
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

                    CityAdapter cityAdapter = new CityAdapter(AddRecipientActivity.this,R.layout.spinner_country, cityList);
                    spCity.setAdapter(cityAdapter);

                }
            }.execute();


        }else{

            final CircleDialog circleDialog=new CircleDialog(AddRecipientActivity.this,0);
            circleDialog.setCancelable(true);
            circleDialog.show();


            System.out.println("Cities are not there");
            new CallWebService(AppConstants.GETCITIES +stateID, CallWebService.TYPE_JSONARRAY) {

                @Override
                public void response(String response) {

                    circleDialog.dismiss();
                    Type listType=new TypeToken<List<City>>(){
                    }.getType();
                    cityList =  new GsonBuilder().create().fromJson(response, listType);
                    CityAdapter cityAdapter = new CityAdapter(AddRecipientActivity.this,R.layout.spinner_country, cityList);
                    spCity.setAdapter(cityAdapter);

                    db_wrapper = new DatabaseWrapper(AddRecipientActivity.this);
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


    public class CityAdapter extends ArrayAdapter<City>{

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

            TextView txt = new TextView(AddRecipientActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CityName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(AddRecipientActivity.this);
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

            TextView txt = new TextView(AddRecipientActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).StateName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(AddRecipientActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).StateName);
            return  txt;
        }
    }
    public class CountryAdapter extends ArrayAdapter<Country>{
        Context context;
        int layoutResourceId;
        ArrayList<Country> values;
        // int android.R.Layout.
        public CountryAdapter(Context context, int resource, ArrayList<Country> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(AddRecipientActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(AddRecipientActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).CountryName);
            return  txt;
        }
    }

    public void setToolColor(int color){
        toolbar.setBackgroundColor(color);
    }

    public void setToolTitle(String title){
        toolbar.setTitle(title);
    }
    public void setToolSubTitle(String subTitle){

        toolbar.setSubtitle(subTitle);
    }
}