package com.webmyne.paylabasmerchant.util;

import android.content.Context;
/**
 * Created by Android on 10-12-2014.
 */
public class LanguageStringUtil {
    static boolean isEnglisSelected;
    static String newvalue;
    static String cultureString;

    public static String languageString(Context contex,String value) {
        isEnglisSelected = PrefUtils.isEnglishSelected(contex);
        newvalue = value;

        if(isEnglisSelected) {
            // for france
            //ch = ",";
            newvalue = newvalue.replaceAll("\\.", ",");

        }
        else {
            // for english
            //ch = ".";
            newvalue = newvalue.replaceAll("\\,", ".");
        }

        return newvalue;
    }
    public static String CultureString(Context contex) {
        isEnglisSelected = PrefUtils.isEnglishSelected(contex);
        if(isEnglisSelected) {
            // for france
            //ch = ",";
            cultureString="fr-FR";
        }
        else {
            // for english
            //ch = ".";
            cultureString="en-US";

        }

        return cultureString;
    }

}
