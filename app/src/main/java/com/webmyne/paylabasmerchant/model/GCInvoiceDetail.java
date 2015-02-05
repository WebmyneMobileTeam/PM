package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

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
    @SerializedName("WithdrawalDate")
    public String WithdrawalDate;
    @SerializedName("WithdrawalDateString")
    public String WithdrawalDateString;
    @SerializedName("id")
    public int id;


}
