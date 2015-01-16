package com.webmyne.paylabasmerchant.model;

/**
 * Created by Android on 05-12-2014.
 */
public class AppConstants {

    // Base url for the webservice
    public static String BASE_URL = "http://ws-srv-net.in.webmyne.com/Applications/PaylabasWS/";

    public static String USER_LOGIN = BASE_URL + "User.svc/json/UserLogin";

    public static String USER_DETAILS = BASE_URL + "User.svc/json/GetUserDetails/";

    public static String GIFTCODE_LIST = BASE_URL + "GiftCode.svc/json/GiftCodeList/";

    public static String GETCITIES = BASE_URL + "User.svc/json/GetCityList/";

    public static String GETRECEIPIENTS = BASE_URL + "GiftCode.svc/json/GetRecipientList/";

    public static String GENERATE_GC = BASE_URL+"GiftCode.svc/json/GenerateGC";

    public static String USER_REGISTRATION = BASE_URL + "User.svc/json/Registration";

    public static String VERIFY_USER = BASE_URL + "User.svc/json/VerifyUser";

    public static String GETGCDETAIL = BASE_URL + "GiftCode.svc/json/GetGCDetail";

    public static String COMBINE_GC = BASE_URL + "GiftCode.svc/json/CombineGC";

    public static String GET_USER_PROFILE = BASE_URL + "User.svc/json/GetUserProfile/";

    public static String VERIFY_RECIPIENT = BASE_URL + "User.svc/json/VerifyRecipient";

    public static String ADD_RECIPIENT = BASE_URL + "User.svc/json/AddRecipient";

    public static String SERVICE_CHARGE = BASE_URL + "GiftCode.svc/json/ServiceCharge/";

    public static String CREDIT_WALLET = BASE_URL + "MoneyTransfer.svc/json/CreditWallet";

    public static String SEND_MONEY_TO_PAYLABAS_USER = BASE_URL + "/MoneyTransfer.svc/json/SendMoeyToPayLabasUser/";

    public static String SEND_PAYMENT = BASE_URL+"/MoneyTransfer.svc/json/SendPayment";

    public static String GET_PAYMENT_STATUS = BASE_URL + "MoneyTransfer.svc/json/GetPaymentStatus";





    /*********  FTP IP ***********/
    public static final String ftpPath="192.168.1.4";

    /*********  FTP USERNAME ***********/
    public static final String ftpUsername="androidftp";

    /*********  FTP PASSWORD ***********/
    public static final String ftpPassword="1234567890";

    /*********  FTP image download ***********/
    public static final String fileDownloadPath="http://ws-srv-net.in.webmyne.com/applications/Android/RiteWayServices/Images/";


    //Merchant Login
    //Merchant Id 4CF5B52A19
    //Password 123456

    public static final String AFFILATE_LOGIN=BASE_URL+"Affiliate.svc/json/AffiliateLogin";


    //MERCHANT PAYMENT SERVICES
    public static final String creditOnWallet="Credit Own Wallet";
    public static final String generateNewGiftCode="Generate New Gift Code";
    public static final String mobileTopup="Mobile Top Up";

    //MERCHANT PAYMENT TYPES
    public static final String wallet="Paylabas Wallet";
    public static final String gc="Gift Code";
    public static final String cash="Cash";


    //Payment
    public static final String PAYMENT_STEP_1=BASE_URL+"Payment.svc/json/PaymentStep1";

    //Redeem GC
    public static final String REDEEM_GC=BASE_URL+"Payment.svc/json/RedeemGC";

    // Mobile top up web service

    public static String MOBILE_TOPUP = BASE_URL + "Payment.svc/json/MobileTopUp";
    public static String GET_MOBILE_TOPUP_DETAILS = BASE_URL+"MobileTopUp.svc/json/GetIDTInfos";
    /*********  image download for service provider ***********/
    public static final String providerImageURL="http://ws-srv-net.in.webmyne.com/Applications/PayLabas_V02/images/MobileOperators/";
    public static String GET_MY_MOBILE_TOPUPLIST = BASE_URL+"MobileTopUp.svc/json/RechargeHistory/";
}


