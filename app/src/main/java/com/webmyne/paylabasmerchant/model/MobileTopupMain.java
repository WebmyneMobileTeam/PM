package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 10-12-2014.
 */
public class MobileTopupMain {

    @SerializedName("IsTop")
    public boolean IsTop;

    @SerializedName("TopUpProducts")
    public ArrayList<MobileTopUpProducts> TopUpProducts;

    @SerializedName("USDtoEuro")
    public float USDtoEuro;

    @SerializedName("countryCode")
    public int countryCode;

    @SerializedName("countryId")
    public long countryId;

    @SerializedName("countryName")
    public String countryName;

    @SerializedName("flagclass")
    public String flagclass;

    @SerializedName("shortCode")
    public String shortCode;

}
