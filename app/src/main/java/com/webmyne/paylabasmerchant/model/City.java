package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 10-12-2014.
 */
public class City {

    @SerializedName("CityID")
    public long CityID;
    @SerializedName("CityName")
    public String CityName;
    @SerializedName("StateID")
    public long StateID;


}
