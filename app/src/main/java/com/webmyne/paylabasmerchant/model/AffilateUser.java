package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 07-01-2015.
 */
public class AffilateUser {

    @SerializedName("AllowCashIN")
    public boolean AllowCashIN;
    @SerializedName("AllowCashOut")
    public boolean AllowCashOut;
    @SerializedName("AllowGC")
    public boolean AllowGC;
    @SerializedName("AllowMobileTopUp")
    public boolean AllowMobileTopUp;
    @SerializedName("CashOutPointName")
    public String CashOutPointName;
    @SerializedName("CityID")
    public int CityID;
    @SerializedName("CountryID")
    public int CountryID;
    @SerializedName("DeviceType")
    public String DeviceType;
    @SerializedName("EmailID")
    public String EmailID;
    @SerializedName("FName")
    public String FName;
    @SerializedName("IsDeleted")
    public boolean IsDeleted;
    @SerializedName("LName")
    public String LName;
    @SerializedName("LemonwayBal")
    public String LemonwayBal;
    @SerializedName("MerchantID")
    public String MerchantID;
    @SerializedName("MobileCountryCode")
    public String MobileCountryCode;
    @SerializedName("MobileNo")
    public String MobileNo;
    @SerializedName("ResponseCode")
    public String ResponseCode;
    @SerializedName("ResponseMsg")
    public String ResponseMsg;
    @SerializedName("StateID")
    public int StateID;
    @SerializedName("UserID")
    public int UserID;
    @SerializedName("VerificationCode")
    public String VerificationCode;


}
