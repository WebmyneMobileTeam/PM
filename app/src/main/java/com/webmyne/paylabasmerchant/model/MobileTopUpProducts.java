package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 10-12-2014.
 */
public class MobileTopUpProducts {

    @SerializedName("LocalPrice")
    public float LocalPrice;

    @SerializedName("RechargeService")
    public ArrayList<MobileTopupRechargeService> RechargeService;

    @SerializedName("carrierCode")
    public String carrierCode;

    @SerializedName("carrierName")
    public String carrierName;

    @SerializedName("commission")
    public float commission;

    @SerializedName("currency")
    public String currency;

    @SerializedName("productName")
    public String productName;

    @SerializedName("rechargePrice")
    public String rechargePrice;

}
