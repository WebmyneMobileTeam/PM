package com.webmyne.paylabasmerchant.ui;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.paylabasmerchant.R;


public class FragmentCurrencyConvert extends Fragment {

    public static FragmentCurrencyConvert newInstance(String param1, String param2) {
        FragmentCurrencyConvert fragment = new FragmentCurrencyConvert();
        return fragment;
    }

    public FragmentCurrencyConvert() {
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
        View convertView= inflater.inflate(R.layout.fragment_currency_convert, container, false);
        return convertView;
    }

}
