package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 10-12-2014.
 */
public class Receipient {

    @SerializedName("Country")
    public long Country;
    @SerializedName("CountryCode")
    public String CountryCode;
    @SerializedName("FirstName")
    public String FirstName;
    @SerializedName("LastName")
    public String LastName;
    @SerializedName("MobileNo")
    public String MobileNo;
    @SerializedName("RecipientID")
    public long RecipientID;
    @SerializedName("EmailId")
    public String EmailId;
    @SerializedName("City")
    public long City;
    @SerializedName("State")
    public long State;

    @SerializedName("Address")
    public String Address;
    @SerializedName("ZipCode")
    public String ZipCode;



}
