package com.webmyne.paylabasmerchant.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.webmyne.paylabasmerchant.R;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner spPaymentType;
    private LinearLayout gcLayout;
    private TextView btnNext;
    private String mParam1;
    private String mParam2;
    FrameLayout linearTools;


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertview = inflater.inflate(R.layout.fragment_home, container, false);
        spPaymentType=(Spinner)convertview.findViewById(R.id.spPaymentType);
        gcLayout=(LinearLayout)convertview.findViewById(R.id.gcLayout);
        btnNext=(TextView)convertview.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVerificationAlert();
            }


        });
        spPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    gcLayout.setVisibility(View.VISIBLE);
                } else {
                    gcLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return convertview;
    }

    private void showVerificationAlert() {



        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.custom_alert_dialog, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(promptsView);

        alert.setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.setCustomAnimations(R.anim.entry, R.anim.exit,R.anim.entry, R.anim.exit);
                ft.replace(R.id.payment_fragment, new PaymentServicesFragment(), "paymenent_services");
                ft.addToBackStack("");
                ft.commit();
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
            }
        });

        alert.show();
    }

//end of main class
}
