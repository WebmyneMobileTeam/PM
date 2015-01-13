package com.webmyne.paylabasmerchant.util;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Android on 13-01-2015.
 */
public abstract class CountryUtils implements IService{

    public abstract void response(ArrayList response);

    public synchronized final CountryUtils fetch(final Context ctx){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseWrapper  db_wrapper = new DatabaseWrapper(ctx);
                try {
                    db_wrapper.openDataBase();
                    ArrayList countrylist = db_wrapper.getCountryData();
                    response(countrylist);
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


        return this;

    }

}
