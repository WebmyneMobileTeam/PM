package com.webmyne.paylabasmerchant.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.paylabasmerchant.R;


public class CurrencyCovertVerification extends Fragment {

    public static CurrencyCovertVerification newInstance(String param1, String param2) {
        CurrencyCovertVerification fragment = new CurrencyCovertVerification();
        return fragment;
    }

    public CurrencyCovertVerification() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currency_covert_verification, container, false);
    }

}
