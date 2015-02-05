/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webmyne.paylabasmerchant.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.ui.widget.ComplexPreferences;

import static com.webmyne.paylabasmerchant.util.LogUtils.LOGD;
import static com.webmyne.paylabasmerchant.util.LogUtils.makeLogTag;

/**
 * Utilities and constants related to app preferences.
 */
public class PrefUtils  {

    private static final String TAG = makeLogTag("PrefUtils");

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("isLogin", false);
    }

    public static void setLoggedIn(final Context context, final boolean isLoggedIn) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("isLogin", isLoggedIn).commit();
    }

    public static void clearLogin(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().remove("isLogin").commit();
    }

    public static boolean isVerified(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("isUserVerify", false);
    }

    public static void setVerified(final Context context, final boolean isVerified) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("isUserVerify", isVerified).commit();
    }

    public static void clearVerify(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().remove("isUserVerify").commit();
    }

    public static boolean isEnglishSelected(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("isEnglishSelected", false);
    }

    public static void setEnglishSelected(final Context context, final boolean isEnglishSelected) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("isEnglishSelected", isEnglishSelected).commit();
    }

    public static AffilateUser getMerchant(final Context context){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "user_pref", 0);
        return complexPreferences.getObject("current_user", AffilateUser.class);
    }
    public static void setMerchant(final Context context, final AffilateUser affilateUser) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "user_pref", 0);
        complexPreferences.putObject("current_user", affilateUser);
        complexPreferences.commit();
    }


    public static String getAffilateCurrency(final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("AffilateCurrency", "");
    }

    public static void setAffilateCurrency(final Context context,String Value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("AffilateCurrency", Value).commit();
    }


    public static String getLiveRate(final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("get live rate",sp.getString("LiveRate", ""));
        return sp.getString("LiveRate", "");
    }

    public static void settLiveRate(final Context context,String Value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("set live rate",Value);
        sp.edit().putString("LiveRate", Value).commit();
    }
    public static void ClearLiveRate(final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("clear live rate","done");
        sp.edit().remove("LiveRate").commit();
    }

}
