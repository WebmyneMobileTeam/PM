package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 04-02-2015.
 */
public class UnclaimedGCDetail {

    @SerializedName("FromDateString")
    public String FromDate;
    @SerializedName("ToDateString")
    public String ToDate;
    @SerializedName("claimAmount")
    public double claimAmount;
    @SerializedName("isAbleToClaim")
    public boolean isAbleToClaim;
    @SerializedName("GCInvoiceDetail")
    public ArrayList<GCInvoiceDetail> gcInvoiceDetails;


}
