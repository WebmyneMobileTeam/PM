package com.webmyne.paylabasmerchant.util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.City;
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.model.State;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;

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

 private ArrayList<State> fetchStateAndDisplay(final Context context,final int CountryID) {
        statelist = new ArrayList<State>();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db_wrapper = new DatabaseWrapper(context);
                try {
                    db_wrapper.openDataBase();
                    statelist = db_wrapper.getStateData(CountryID);
                    db_wrapper.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                }
        }.execute();

    return  statelist;
 }

 private ArrayList<City> fetchAndDisplayCity(final Context context,final int stateID) {
        cityList = new ArrayList<City>();
        boolean isAlreadyThere = false;
        db_wrapper = new DatabaseWrapper(context);
        try {
            db_wrapper.openDataBase();
            isAlreadyThere = db_wrapper.isAlreadyInDatabase(stateID);
            db_wrapper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isAlreadyThere == true) {

            System.out.println("Cities are already in database");
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    db_wrapper = new DatabaseWrapper(context);
                    try {
                        db_wrapper.openDataBase();
                        cityList = db_wrapper.getCityData(stateID);
                        db_wrapper.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                  /*  CityAdapter cityAdapter = new CityAdapter(SignUpActivity.this, R.layout.spinner_country, cityList);
                    spCity.setAdapter(cityAdapter);*/

                }
            }.execute();


        } else {

            final CircleDialog circleDialog = new CircleDialog(context, 0);
            circleDialog.setCancelable(true);
            circleDialog.show();


            System.out.println("Cities are not there");

            /*new CallWebService(AppConstants.GETCITIES + stateID, CallWebService.TYPE_JSONARRAY) {

                @Override
                public void response(String response) {

                    circleDialog.dismiss();
                    Type listType = new TypeToken<List<City>>() {
                    }.getType();
                    cityList = new GsonBuilder().create().fromJson(response, listType);
                    CityAdapter cityAdapter = new CityAdapter(SignUpActivity.this, R.layout.spinner_country, cityList);
                    spCity.setAdapter(cityAdapter);

                    db_wrapper = new DatabaseWrapper(SignUpActivity.this);
                    try {
                        db_wrapper.openDataBase();
                        db_wrapper.insertCities(cityList);
                        db_wrapper.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void error(VolleyError error) {

                    circleDialog.dismiss();
                }
            }.start();
*/
        }



        return cityList;
    }



    //end of main class
}
