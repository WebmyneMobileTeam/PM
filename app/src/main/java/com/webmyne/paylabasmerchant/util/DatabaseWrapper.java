package com.webmyne.paylabasmerchant.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.webmyne.paylabasmerchant.model.City;
import com.webmyne.paylabasmerchant.model.Country;
import com.webmyne.paylabasmerchant.model.State;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by krishnakumar on 09-12-2014.
 */
public class DatabaseWrapper extends SQLiteOpenHelper {

    private static String TAG = "DATABASE_WRAPPER";
    private  String DB_PATH = "/data/data/com.webmyne.paylabasmerchant/databases/";
    private static String DB_NAME = "PayLabas.db";
    private SQLiteDatabase myDataBase = null;
    private Context myContext;
    ArrayList<Country> country_obj;
    ArrayList<State> state_obj;

    //private StateManager state = StateManager.getInstance();
    private SQLiteDatabase db;

    public DatabaseWrapper(Context context) {
        super(context, DB_NAME,null,1);
        this.myContext = context;
    }


    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if(dbExist){
           Log.v("log_tag", "database does exist");
        }else{
            Log.v("log_tag", "database does not exist");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException{

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkDataBase(){

        File folder = new File(DB_PATH);
        if(!folder.exists()){
            folder.mkdir();
        }
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        Log.e("mPath", mPath);
        myDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return myDataBase != null;

    }
    @Override
    public synchronized void close()
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public  ArrayList<State> getStateData(int CountryID){

        state_obj = new ArrayList<State>();
        Cursor c = null;
     //   return mDb.rawQuery("SELECT * FROM myTable WHERE column1 = "+ someValue, null);

        String query = "select * from state where CountryID = ";
        c = myDataBase.rawQuery(query+CountryID,null);
        c.moveToFirst();

        if(c.moveToFirst()){
            do {
                int sid= c.getInt(c.getColumnIndex("StateID"));
                String StateN=c.getString(c.getColumnIndex("StateName"));
                int Countrycode=c.getInt(c.getColumnIndex("CountryID"));

                State temp_obj=new State();
                temp_obj.StateID=sid;
                temp_obj.StateName=StateN;
                temp_obj.CountryID=Countrycode;

                state_obj.add(temp_obj);

            }while (c.moveToNext());
        }
        c.close();
        return state_obj;
    }

    public  ArrayList<City> getCityData(int StateID){

       ArrayList<City> cities = new ArrayList<City>();
        Cursor c = null;
        //   return mDb.rawQuery("SELECT * FROM myTable WHERE column1 = "+ someValue, null);

        String query = "select * from city where StateID = ";
        c = myDataBase.rawQuery(query+StateID,null);
        c.moveToFirst();

        if(c.moveToFirst()){
            do {

                City city = new City();
                city.CityID = c.getLong(c.getColumnIndex("CityID"));
                city.CityName = c.getString(c.getColumnIndex("CityName"));
                city.StateID = c.getLong(c.getColumnIndex("StateID"));

                cities.add(city);

            }while (c.moveToNext());
        }
        c.close();

        return cities;
    }

    public void insertCities(ArrayList<City> cities){

        for(City city : cities){

            ContentValues cv = new ContentValues();
            cv.put("CityID",city.CityID);
            cv.put("CityName",city.CityName);
            cv.put("StateID",city.StateID);

            myDataBase.insert("city",null,cv);

        }

    }


    public boolean isAlreadyInDatabase(int stateID){


        boolean isThere = false;

        Cursor c = null;

        String query = "select * from city where StateID ="+stateID;
        c = myDataBase.rawQuery(query,null);

        if(c == null){
            isThere = false;

        }else{
            if(c.getCount()>0){
                isThere = true;
            }
        }
        return isThere;
    }



    public ArrayList<Country> getCountryData(){

      country_obj = new ArrayList<Country>();
      Cursor c = null;

        String query = "select * from country";
        c = myDataBase.rawQuery(query,null);
        c.moveToFirst();

        if(c.moveToFirst()){
            do {
                int cid= c.getInt(c.getColumnIndex("CountryID"));
                String CountryN=c.getString(c.getColumnIndex("CountryName"));
                int CountryC=c.getInt(c.getColumnIndex("CountryCode"));
                String shortC=c.getString(c.getColumnIndex("shortCode"));
                int forTopup=c.getInt(c.getColumnIndex("ForTopUp"));
                String flagc=c.getString(c.getColumnIndex("FlagClass"));
                String cmp_sh_name=c.getString(c.getColumnIndex("CountryShortName"));

                Country datalist=new Country(cid,CountryN,CountryC,shortC,forTopup,flagc,cmp_sh_name);

                country_obj.add(datalist);

            }while (c.moveToNext());
        }
        c.close();

        return country_obj;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    // All the methods for fetching specific data from local Database

}