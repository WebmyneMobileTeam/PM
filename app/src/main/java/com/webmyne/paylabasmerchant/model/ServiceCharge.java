package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 08-12-2014.
 */
public class ServiceCharge {

    @SerializedName("AffiliateID")
    public String AffiliateID;

    @SerializedName("AffiliatePerCharge")
    public String AffiliatePerCharge;

    @SerializedName("PaylabasFixCharge")
    public String PaylabasFixCharge;

    @SerializedName("PaylabasPerCharge")
    public String PaylabasPerCharge;

    @SerializedName("MaxLimit")
    public float MaxLimit;

    @SerializedName("MinLimit")
    public float MinLimit;



}
