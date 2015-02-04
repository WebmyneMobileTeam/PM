package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 04-02-2015.
 */
public class GCInvoiceDetail {

    @SerializedName("GCAmount")
    public double GCAmount;
    @SerializedName("ReceiverMob")
    public String ReceiverMob;
    @SerializedName("SendTo")
    public String SendTo;
    @SerializedName("WithdrawalDateString")
    public String WithdrawalDate;
    @SerializedName("id")
    public int id;


}
