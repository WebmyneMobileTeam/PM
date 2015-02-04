package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 09-01-2015.
 */
public class AffilateServices {

    @SerializedName("RemainingBal")
    public double RemainingBal;
    @SerializedName("ServiceName")
    public String ServiceName;
    @SerializedName("SetLimit")
    public double SetLimit;
    @SerializedName("ServiceID")
    public int ServiceID ;
    @SerializedName("IsActive")
    public boolean IsActive;

}
