package com.webmyne.paylabasmerchant.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 07-01-2015.
 */
public class AffilateUser {

    @SerializedName("AffiliateServices")
    public ArrayList<AffilateServices> affilateServicesArrayList =new ArrayList<AffilateServices>();

    @SerializedName("GCRedeemPerposes")
    public ArrayList<GCRedeemPerposes> gcredeemPerposesArrayList =new ArrayList<GCRedeemPerposes>();


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
    @SerializedName("LocalCurrency")
    public String LocalCurrency;



    //Mobile Topup Service
    public boolean isMobiletopupServiceAvailable(){
        boolean available=false;
        for(AffilateServices affilateServices: affilateServicesArrayList){

            if(affilateServices.ServiceName.equalsIgnoreCase(AppConstants.mobileTopup) && affilateServices.IsActive==true){
                available=true;
                return  available;
            }
        }
        return  available;
    }

   // Generate new gift code service
   public boolean isGenerateNewGcServiceAvailable(){
       boolean available=false;
       for(AffilateServices affilateServices: affilateServicesArrayList){

           if(affilateServices.ServiceName.equalsIgnoreCase(AppConstants.generateNewGiftCode) && affilateServices.IsActive==true){
               available=true;
               return  available;
           }
       }
       return  available;
   }

    //credit On Wallet service
    public boolean isCreditOnWalletServiceAvailable(){
        boolean available=false;
        for(AffilateServices affilateServices: affilateServicesArrayList){

            if(affilateServices.ServiceName.equalsIgnoreCase(AppConstants.creditOnWallet) && affilateServices.IsActive==true){
                available=true;
                return  available;
            }
        }
        return  available;
    }

    public String tempCountryCode;
    public String tempMobileNumber;
    public String tempPaymentVia;
    public String tempAmount;
    public String tempGiftCode;



}
