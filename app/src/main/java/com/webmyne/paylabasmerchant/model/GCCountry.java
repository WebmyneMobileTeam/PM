package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 23-01-2015.
 */
public class GCCountry {

    @SerializedName("CountryId")
    public int CountryId;

    @SerializedName("CountryName")
    public String CountryName;

    @SerializedName("CurrencyName")
    public String CurrencyName;

    @SerializedName("LiveRate")
    public double LiveRate;

    @SerializedName("ShortCode")
    public String ShortCode;

    @SerializedName("CountryCode")
    public String CountryCode;
}
