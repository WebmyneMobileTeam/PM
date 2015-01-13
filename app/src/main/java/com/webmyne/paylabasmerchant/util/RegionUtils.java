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
public abstract class RegionUtils implements IService{

    public abstract void response(ArrayList response);
    static ArrayList<Country> countrylist;
    static ArrayList<State> statelist;
    static ArrayList<City> cityList;

 public synchronized final RegionUtils fetchCountry(final Context ctx){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseWrapper  db_wrapper = new DatabaseWrapper(ctx);
                try {
                    db_wrapper.openDataBase();
                     countrylist = db_wrapper.getCountryData();
                   // response(countrylist);
                    db_wrapper.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                response(countrylist);
            }
        }.execute();
        return this;
    }

 public synchronized final RegionUtils fetchState(final Context ctx,final int CountryID){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseWrapper  db_wrapper = new DatabaseWrapper(ctx);
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
                response(statelist);
            }
        }.execute();
        return this;
    }

/*
private void fetchAndDisplayCity(final int stateID) {
        cityList = new ArrayList<City>();
        boolean isAlreadyThere = false;
         DatabaseWrapper db_wrapper = new DatabaseWrapper(SignUpActivity.this);
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
                    db_wrapper = new DatabaseWrapper(SignUpActivity.this);
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

                    CityAdapter cityAdapter = new CityAdapter(SignUpActivity.this, R.layout.spinner_country, cityList);
                    spCity.setAdapter(cityAdapter);

                }
            }.execute();


        } else {

            final CircleDialog circleDialog = new CircleDialog(SignUpActivity.this, 0);
            circleDialog.setCancelable(true);
            circleDialog.show();


            System.out.println("Cities are not there");
            new CallWebService(AppConstants.GETCITIES + stateID, CallWebService.TYPE_JSONARRAY) {

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

        }

    }

*/



}
