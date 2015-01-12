package com.webmyne.paylabasmerchant.ui;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateServices;
import com.webmyne.paylabasmerchant.util.PrefUtils;

import java.util.ArrayList;

public class FragmentPaymentServiceSelection extends Fragment {

    private Spinner spServiceType;
    private ArrayAdapter<String> serviceTypeAdapter;
    private ArrayList<AffilateServices> affilateServicesArrayList;
    private ArrayList<String> affilateServiceNames;

    public static FragmentPaymentServiceSelection newInstance(String param1, String param2) {
        FragmentPaymentServiceSelection fragment = new FragmentPaymentServiceSelection();

        return fragment;
    }

    public FragmentPaymentServiceSelection() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        affilateServicesArrayList=new ArrayList<AffilateServices>();
        affilateServicesArrayList= PrefUtils.getMerchant(getActivity()).affilateServicesArrayList;
        affilateServiceNames=new ArrayList<String>();
        for(AffilateServices affilateServices: affilateServicesArrayList){
            if(affilateServices.IsActive==true){
                affilateServiceNames.add(affilateServices.ServiceName.toString().trim());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView= inflater.inflate(R.layout.fragment_payment_services, container, false);
        spServiceType=(Spinner)convertView.findViewById(R.id.spServiceType);
        serviceTypeAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, affilateServiceNames);
        spServiceType.setAdapter(serviceTypeAdapter);
        return convertView;
    }
}
