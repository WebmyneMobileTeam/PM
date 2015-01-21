package com.webmyne.paylabasmerchant.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.City;
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.model.MobileTopupMain;
import com.webmyne.paylabasmerchant.model.PickUpPoint;
import com.webmyne.paylabasmerchant.ui.widget.CallWebService;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.RegionUtils;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MoneyTransferHomeActivity extends ActionBarActivity {

    Toolbar toolbar_actionbar;
    private Spinner spinner_country;
    private Spinner spinner_city;
   // private Spinner spinner_pickup_points;
   // private ListView list_pickup_points;
    public int selected_cash_pickup = -1;
    private TextView btnSelectCashPickUp;

    private CheckedTextView txtTitlePickUp;
    private TextView txtTitlePickUpSubTitle;
    private TextView txtWeekend;
    private View include_item_pickup;
    private Button btnNextMoneyTransfer;

    private ArrayList<MONEYPOLO_COUNTRY> countries;
    private ArrayList<MONEYPOLO_CITY> cities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer_home);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle(getResources().getString(R.string.money_transfer_title));
            toolbar_actionbar.setNavigationIcon(R.drawable.icon_back);
            setSupportActionBar(toolbar_actionbar);

        }
        toolbar_actionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();

    }

    private void init() {

       spinner_country = (Spinner)findViewById(R.id.spinner_country);
       spinner_city = (Spinner)findViewById(R.id.spinner_city);
       btnSelectCashPickUp = (TextView)findViewById(R.id.btnSelectCashPickUp);
       btnSelectCashPickUp.setOnClickListener(mySelectListner);


        txtTitlePickUp = (CheckedTextView)findViewById(R.id.txtTitlePickUp);
        txtTitlePickUpSubTitle = (TextView)findViewById(R.id.txtTitlePickUpSubTitle);
        txtWeekend = (TextView)findViewById(R.id.txtWeekend);

        include_item_pickup = (View)findViewById(R.id.include_item_pickup);
        include_item_pickup.setVisibility(View.GONE);

        btnNextMoneyTransfer = (Button)findViewById(R.id.btnNextMoneyTransfer);
        btnNextMoneyTransfer.setOnClickListener(nextClickLisnter);


       fetchCountryAndDisplay();
       //addStaticOneCityToSpinner();

       spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(position == 0){

               }else{
                        fetchCityAndDisplay(position);
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){

                }else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private View.OnClickListener mySelectListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            fetchPickUpPointsAndDisplay();

        }
    };

    private View.OnClickListener nextClickLisnter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent i = new Intent(MoneyTransferHomeActivity.this,MoneyTransferFinalActivity.class);
            startActivity(i);


        }
    };



    private void fetchPickUpPointsAndDisplay() {

        final Dialog dialog = new Dialog(MoneyTransferHomeActivity.this,android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.setTitle("SELECT PICKUP POINT");
        dialog.setCancelable(true);

        ArrayList points = new ArrayList();
        PickUpPoint point = new PickUpPoint();
        point.name = "ICICI BANK LIMITED, INDIA";
        point.address = "ICICI BANK TOWERS, BANDRA KURLA COMPLEX, BANDRA(E)";
        point.weekend = "10.00 - 23.00";
        points.add(point);

        point = new PickUpPoint();
        point.name = "ICICI BANK LIMITED, INDIA - CASH PAYMENTS";
        point.address = "ICICI BANK TOWERS, BANDRA KURLA COMPLEX, BANDRA(E)";
        point.weekend = "SA: 07:00-15:00";
        points.add(point);

        point = new PickUpPoint();
        point.name = "MUTHFOOT FINANCE - MAHIM WEST";
        point.address = "61, RAM HALL, OPP. MAHIM RAILWAY STATION (WEST) MAHIM, MUMBAI - 400016";
        point.weekend = "SA: 07:00-15:00";
        points.add(point);

        MobilePickUpPointsAdapter adapter = new MobilePickUpPointsAdapter(MoneyTransferHomeActivity.this,android.R.layout.simple_list_item_single_choice,points);
        View vDialog = getLayoutInflater().inflate(R.layout.item_dialog_pickup,null);
        ListView  list_pickup_points = (ListView)vDialog.findViewById(R.id.list_pickup_points);
        list_pickup_points.setAdapter(adapter);
        dialog.setContentView(vDialog);
        dialog.show();

        list_pickup_points.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();

                fillSelectedPoint();

            }
        });


    }

    private void fillSelectedPoint() {

        if(!include_item_pickup.isShown()){
            include_item_pickup.setVisibility(View.VISIBLE);
        }

        txtTitlePickUp.setText("ICICI BANK LIMITED, INDIA - CASH PAYMENTS");
        txtTitlePickUpSubTitle.setText("ICICI BANK TOWERS, BANDRA KURLA COMPLEX, BANDRA(E)");
        txtWeekend.setText("SA: 07:00-15:00");
    }



    private void fetchCountryAndDisplay() {

        Log.e("Money polo country list ","................in fetch "+AppConstants.GET_MONEYPOLO_COUNTRYLIST);
        final CircleDialog circleDialog=new CircleDialog(MoneyTransferHomeActivity.this,0);
        circleDialog.setCancelable(true);
        circleDialog.show();


        JsonArrayRequest request2 = new JsonArrayRequest(AppConstants.GET_MONEYPOLO_COUNTRYLIST,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jArray) {

                        circleDialog.dismiss();
                        Log.e("Response Country ",jArray.toString());

                        countries = new ArrayList<>();
                        Type listType=new TypeToken<List<MONEYPOLO_COUNTRY>>(){
                        }.getType();
                        countries =  new GsonBuilder().create().fromJson(jArray.toString(),listType);

                        MONEYPOLO_COUNTRY c1 = new MONEYPOLO_COUNTRY();
                        c1.CountryCodeName = "Select Country";
                        countries.add(0,c1);

                        MobileCountryAdapter adapter = new MobileCountryAdapter(MoneyTransferHomeActivity.this,
                                android.R.layout.simple_spinner_item,countries);

                        spinner_country.setAdapter(adapter);


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

                Log.e("Response Country ",""+arg0);
                circleDialog.dismiss();

            }
        });

        int socketTimeout2 = 60000;//60 seconds - change to what you want
        RetryPolicy policy2 = new DefaultRetryPolicy(socketTimeout2, 0, 0);
        request2.setRetryPolicy(policy2);

        MyApplication.getInstance().addToRequestQueue(request2);



    }

    private void fetchCityAndDisplay(int countrycode){

        Log.e("Selected Country ",countries.get(countrycode).CountryCodeName);


       // MobileCityAdapter adapter = new MobileCityAdapter(MoneyTransferHomeActivity.this,android.R.layout.simple_spinner_item,cities);
      //  spinner_city.setAdapter(adapter);


    }

    public class MobilePickUpPointsAdapter extends ArrayAdapter<PickUpPoint> {

        Context context;
        int layoutResourceId;
        ArrayList<PickUpPoint> values;
        // int android.R.Layout.

        public MobilePickUpPointsAdapter(Context context, int resource, ArrayList<PickUpPoint> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item_pickup_points,null);
            }

            CheckedTextView txtTitle = (CheckedTextView) convertView.findViewById(R.id.txtTitlePickUp);
            TextView txtSubTitle = (TextView) convertView.findViewById(R.id.txtTitlePickUpSubTitle);
            TextView txtWeekend = (TextView) convertView.findViewById(R.id.txtWeekend);
            txtSubTitle.setText(values.get(position).address);
            txtTitle.setText(values.get(position).name);
            txtWeekend.setText(values.get(position).weekend);



            return  convertView;

        }
    }


    public class MobileCityAdapter extends ArrayAdapter<MONEYPOLO_CITY> {

        Context context;
        int layoutResourceId;
        ArrayList<MONEYPOLO_CITY> values;
        // int android.R.Layout.

        public MobileCityAdapter(Context context, int resource, ArrayList<MONEYPOLO_CITY> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferHomeActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).Description);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferHomeActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).Description);
            return  txt;
        }
    }


    public class MobileCountryAdapter extends ArrayAdapter<MONEYPOLO_COUNTRY> {

        Context context;
        int layoutResourceId;
        ArrayList<MONEYPOLO_COUNTRY> values;
        // int android.R.Layout.

        public MobileCountryAdapter(Context context, int resource, ArrayList<MONEYPOLO_COUNTRY> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferHomeActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CountryCodeName);
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(MoneyTransferHomeActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setText(values.get(position).CountryCodeName);


            return  txt;
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_money_transfer_home, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MONEYPOLO_COUNTRY{

        @SerializedName("ConvRate")
        public String ConvRate;
        @SerializedName("CountryCodeName")
        public String CountryCodeName;
        @SerializedName("CountryID")
        public int CountryID;
        @SerializedName("Description")
        public String Description;
        @SerializedName("FrmCurrencyCode")
        public String FrmCurrencyCode;
        @SerializedName("RegionsCount")
        public int RegionsCount;
        @SerializedName("ShortCode")
        public String ShortCode;
        @SerializedName("ToCurrencyCode")
        public String ToCurrencyCode;
        public MONEYPOLO_COUNTRY() {
        }
    }

    public static class MONEYPOLO_CITY{

//        "Amount":"String content",
//                "CityID":4294967295,
//                "Description":"String content"

        @SerializedName("Amount")
        public String Amount;
        @SerializedName("CityID")
        public int CityID;
        @SerializedName("Description")
        public String Description;

    }


}
