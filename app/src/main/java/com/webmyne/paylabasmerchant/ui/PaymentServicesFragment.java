package com.webmyne.paylabasmerchant.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.paylabasmerchant.R;

public class PaymentServicesFragment extends Fragment {

    public static PaymentServicesFragment newInstance(String param1, String param2) {
        PaymentServicesFragment fragment = new PaymentServicesFragment();

        return fragment;
    }

    public PaymentServicesFragment() {
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
        View convertView= inflater.inflate(R.layout.fragment_payment_services, container, false);

        return convertView;
    }

}
