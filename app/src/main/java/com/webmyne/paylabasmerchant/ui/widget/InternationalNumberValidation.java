package com.webmyne.paylabasmerchant.ui.widget;

import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Created by Android on 28-01-2015.
 */
public class InternationalNumberValidation {


    public static boolean isPossibleNumber(String mobileNumber,String countryCode){
        boolean isValid=false;
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phNumberProto = null;
            phNumberProto = phoneUtil.parse(mobileNumber,countryCode);
            if(phoneUtil.isPossibleNumber(phNumberProto)==true){
                isValid=true;
            } else {
                isValid=false;
            }

        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return isValid;
    }

    public static boolean isValidNumber(String mobileNumber,String countryCode){
        boolean isValid=false;
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phNumberProto = null;
            phNumberProto = phoneUtil.parse(mobileNumber,countryCode);
//            etMobileNumber.setText(phoneUtil.format(phNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL).substring((phoneUtil.format(phNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL).split(" ")[0]).length() + 1));
            isValid = phoneUtil.isValidNumber(phNumberProto);
        } catch (Exception e) {
            Log.e("error",e+"");
        }
        // check if the number is valid

        return isValid;
    }


}
