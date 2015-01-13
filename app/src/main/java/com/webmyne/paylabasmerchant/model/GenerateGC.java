package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 13-01-2015.
 */
public class GenerateGC {

    @SerializedName("AffiliateID")
    public String AffiliateID;
    @SerializedName("Amount")
    public String Amount;
    @SerializedName("ServiceUse")
    public String ServiceUse;
    @SerializedName("GCAmount")
    public long GCAmount;
    @SerializedName("GiftCode")
    public String GiftCode;
    @SerializedName("UserCountryCode")
    public String UserCountryCode;
    @SerializedName("UserMobileNo")
    public String UserMobileNo;
    @SerializedName("IsGCUsed")
    public boolean IsGCUsed ;
    @SerializedName("IsLowBalance")
    public boolean IsLowBalance;
    @SerializedName("LemonwayBal")
    public String LemonwayBal;
    @SerializedName("PaymentStatus")
    public String PaymentStatus;
    @SerializedName("PaymentVia")
    public String PaymentVia;
    @SerializedName("ReceiverCountryCode")
    public String ReceiverCountryCode;
    @SerializedName("ReceiverMobileNo")
    public String ReceiverMobileNo;
    @SerializedName("ResponseCode")
    public String ResponseCode;
    @SerializedName("ResponseMsg")
    public String ResponseMsg;
    @SerializedName("UserID")
    public int UserID;
    @SerializedName("VerificationCode")
    public String VerificationCode;
}
