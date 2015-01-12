package com.webmyne.paylabasmerchant.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.webmyne.paylabasmerchant.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link FragmentCombineGC#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCombineGC extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayout linearCombineGiftCode;


    public static FragmentCombineGC newInstance(String param1, String param2) {
        FragmentCombineGC fragment = new FragmentCombineGC();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentCombineGC() {

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

        View convertView = inflater.inflate(R.layout.fragment_combine_gc, container, false);
        init(convertView);
        return convertView;
    }

    private void init(View convertView) {
        linearCombineGiftCode = (LinearLayout)convertView.findViewById(R.id.linearCombineGiftCode);




    }

    @Override
    public void onResume() {
        super.onResume();
    }



}
