package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 07-01-2015.
 */
public class LiveCurrency {
    @SerializedName("VerificationCode")
    public String VerificationCode;

    @SerializedName("FromCurrency")
    public String FromCurrency;

    @SerializedName("LiveRate")
    public String LiveRate;
    @SerializedName("Tocurrency")
    public String Tocurrency;

}
