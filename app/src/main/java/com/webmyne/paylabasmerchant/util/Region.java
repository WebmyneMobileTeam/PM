package com.webmyne.paylabasmerchant.util;

import android.content.Context;
import android.os.AsyncTask;

import com.webmyne.paylabasmerchant.model.City;
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.model.State;

import java.util.ArrayList;

/**
 * Created by Android on 12-01-2015.
 */
public class Region {
    ArrayList<Country> countrylist;
    ArrayList<State> statelist;
    ArrayList<City> cityList;
    DatabaseWrapper db_wrapper;

 private ArrayList<Country> fetchCountryAndDisplay(final Context context) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                db_wrapper = new DatabaseWrapper(context);
                try {
                    db_wrapper.openDataBase();
                    countrylist = db_wrapper.getCountryData();
                    db_wrapper.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                /*CountryAdapter countryAdapter = new CountryAdapter(SignUpActivity.this, R.layout.spinner_country, countrylist);
                spCountry.setAdapter(countryAdapter);*/
            }
        }.execute();
        return countrylist;
    }



    //end of main class
}
