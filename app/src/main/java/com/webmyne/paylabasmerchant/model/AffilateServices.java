package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 09-01-2015.
 */
public class AffilateServices {

    @SerializedName("RemainingBal")
    public int RemainingBal;
    @SerializedName("ServiceName")
    public String ServiceName;
    @SerializedName("SetLimit")
    public int SetLimit;
    @SerializedName("ServiceID")
    public int ServiceID ;
    @SerializedName("IsActive")
    public boolean IsActive;
}
