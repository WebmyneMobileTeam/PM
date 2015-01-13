package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 12-01-2015.
 */
public class PaymentStep1 {

    @SerializedName("AffiliateID")
    public String AffiliateID;
    @SerializedName("Amount")
    public String Amount;
    @SerializedName("GCAmount")
    public long GCAmount;
    @SerializedName("GiftCode")
    public String GiftCode;
    @SerializedName("IsGCUsed")
    public boolean IsGCUsed;
    @SerializedName("IsLowBalance")
    public boolean IsLowBalance;
    @SerializedName("LemonwayBal")
    public String LemonwayBal;
    @SerializedName("PaymentVia")
    public String PaymentVia;
    @SerializedName("ResponseCode")
    public String ResponseCode;
    @SerializedName("ResponseMsg")
    public String ResponseMsg;
    @SerializedName("ServiceUse")
    public String ServiceUse;
    @SerializedName("UserCountryCode")
    public String UserCountryCode;
    @SerializedName("UserID")
    public int UserID;
    @SerializedName("UserMobileNo")
    public String UserMobileNo;
    @SerializedName("VerificationCode")
    public String VerificationCode;




}
