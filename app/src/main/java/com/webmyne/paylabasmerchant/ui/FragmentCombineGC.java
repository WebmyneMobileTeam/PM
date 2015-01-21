package com.webmyne.paylabasmerchant.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.PrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link FragmentCombineGC#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCombineGC extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayout linearCombineGiftCode;
    private TextView btnCombine;
    private TextView btnAdd;

    private AffilateUser user;
    private ArrayList<String> combine_giftcode_list;


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
        btnAdd = (TextView)convertView.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnCombine = (TextView)convertView.findViewById(R.id.btnCombine);
        btnCombine.setOnClickListener(this);



    }

    @Override
    public void onResume() {
        super.onResume();
      /*  ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        user = complexPreferences.getObject("current_user", User.class);
*/
        user= PrefUtils.getMerchant(getActivity());

        try{
            linearCombineGiftCode.removeAllViews();
        }catch(Exception e){

        }


        addCombineStrip(false);
        addCombineStrip(false);

    }

    private void addCombineStrip(boolean isDeleteVisible) {

        View vStrip = getActivity().getLayoutInflater().inflate(R.layout.item_combinegiftcode,null);
        vStrip.setTag(linearCombineGiftCode.getChildCount());
        TextView txtDelete = (TextView)vStrip.findViewById(R.id.btnDeleteCombineGiftCode);
        if(isDeleteVisible == false){
            txtDelete.setVisibility(View.INVISIBLE);
        }else{
            txtDelete.setVisibility(View.VISIBLE);
        }

        txtDelete.setOnClickListener(deleteListner);
        final EditText edEnterGiftCode = (EditText)vStrip.findViewById(R.id.entergiftcode_combinegiftcode);
        edEnterGiftCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().length() == 9){



                    LinearLayout first = (LinearLayout)edEnterGiftCode.getParent().getParent();
                    TextView ed = (TextView)first.findViewById(R.id.txtAmountGCCombineGC);
                    processFetchValue(edEnterGiftCode.getText().toString(),ed,edEnterGiftCode);


                }
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearCombineGiftCode.addView(vStrip,params);
        linearCombineGiftCode.invalidate();

    }

    boolean duplicates(final ArrayList<String> arr)
    {
        Set<String> lump = new HashSet<String>();
        for (String i : arr)
        {
            if (lump.contains(i)) return true;
            lump.add(i);
        }
        return false;
    }

    private void processFetchValue(String code, final TextView index, final EditText ed) {

        try {

            JSONObject generateObject = new JSONObject();
            generateObject.put("GCText", code);
            generateObject.put("SenderID", user.UserID);
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.GETGCDETAIL, generateObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {

                    String response = jobj.toString();

                    Log.e("Response FetchGC detail GC: ", "" + response);

                    try {
                        JSONObject obj = new JSONObject(response);

                        String responseCode = obj.getString("ResponseCode");

                        if(responseCode.equalsIgnoreCase("1")){
                            index.setText(getResources().getString(R.string.euro)+" "+jobj.getString("GCAmount"));
                        }else{

                            ed.setText("");
                            ed.setError(jobj.getString("ResponseMsg"));
                          /*  SnackBar bar = new SnackBar(getActivity(),jobj.getString("ResponseMsg"));
                            bar.show();*/

                        }

                    } catch (Exception e) {

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    SimpleToast.error(getActivity(),error.getMessage());

                }
            });

            req.setRetryPolicy(
                    new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            MyApplication.getInstance().addToRequestQueue(req);

        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnAdd:
                processAddCombineStrips();
                break;
            case R.id.btnCombine:
                processCombine();
                break;

        }
    }

    private void processCombine() {

        if(isPassedFromValidationProcess()) {

            try {

                JSONObject jMain = new JSONObject();
                JSONArray arr = new JSONArray();

                for (int i = 0; i < linearCombineGiftCode.getChildCount(); i++) {
                    LinearLayout layout = (LinearLayout) linearCombineGiftCode.getChildAt(i);
                    EditText ed = (EditText) layout.findViewById(R.id.entergiftcode_combinegiftcode);
                    JSONObject jobj = new JSONObject();
                    jobj.put("GiftCode", ed.getText().toString());
                    arr.put(jobj);
                }
                jMain.put("GiftCode",arr);
                jMain.put("SenderID",user.UserID);

                Log.e("----------------- jMAIN ",""+jMain.toString());

                try{


                    final CircleDialog d=new CircleDialog(getActivity(),0);
                    d.setCancelable(true);
                    d.show();

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.COMBINE_GC, jMain, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject jobj) {

                            try {
                                String response = jobj.toString();
                                Log.e("Response : ", "" + response);
                                if (jobj.getString("ResponseCode").equalsIgnoreCase("1")) {
                                    SimpleToast.ok(getActivity(), "Gift Code Combined");
                                    clearAll();

                                    CountDownTimer countDownTimer;
                                    countDownTimer = new MyCountDownTimer(3000, 1000); // 1000 = 1s
                                    countDownTimer.start();
                                  /*  FragmentManager manager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction ft = manager.beginTransaction();
                                    ft.replace(R.id.main_container,new MyAccountFragment());
                                    ft.commit();*/

                                } else {
                                    SimpleToast.error(getActivity(),jobj.getString("ResponseMsg"));

                                }

                                d.dismiss();

                            }catch(Exception e){

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            d.dismiss();
                            Log.e("error responsegg: ", error + "");
                            SimpleToast.error(getActivity(),error.getMessage());

                        }
                    });

                    req.setRetryPolicy(new DefaultRetryPolicy(0,0,0));
                    MyApplication.getInstance().addToRequestQueue(req);

                }catch (Exception e){

                }


            }catch(Exception e){

            }

        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            Log.e("counter","Time's up!");
            FragmentHome.isFromDetailPage=true;
            getActivity().finish();

        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

    }

    public boolean isPassedFromValidationProcess() {

        boolean isPassed = false;
        ArrayList<String> gcs = new ArrayList<String>();

        for(int i=0;i<linearCombineGiftCode.getChildCount();i++){

            LinearLayout layout = (LinearLayout)linearCombineGiftCode.getChildAt(i);
            EditText ed = (EditText)layout.findViewById(R.id.entergiftcode_combinegiftcode);
            gcs.add(ed.getText().toString());

            if(ed.getText().toString().equalsIgnoreCase("")){
                ed.setError("Enter GC");
                isPassed = false;
                return isPassed;

            }else if(duplicates(gcs) == true) {
                SimpleToast.error(getActivity(),"Can not combine same gift codes");
                isPassed = false;
                return  isPassed;

            }else{

                if(ed.getText().toString().length() == 9){
                    isPassed = true;
                    continue;

                }else{
                    ed.setError("Enter Valid GC");
                    isPassed = false;
                    return isPassed;
                }
            }
        }

        return isPassed;
    }

    private boolean matchPreviousGCS(String s, ArrayList<String> gcs) {

        boolean isMatch = false;

        for(String sgc : gcs){

            if(s.equals(sgc)){

                isMatch = true;
                return isMatch;
            }
        }

        return isMatch;
    }

    private void clearAll(){

        for(int i=0;i<linearCombineGiftCode.getChildCount();i++){

            LinearLayout layout = (LinearLayout)linearCombineGiftCode.getChildAt(i);
            EditText ed = (EditText)layout.findViewById(R.id.entergiftcode_combinegiftcode);
            ed.setText("");
        }
    }

    private View.OnClickListener deleteListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            FrameLayout fp = (FrameLayout)v.getParent();
            LinearLayout second = (LinearLayout)fp.getParent();
            LinearLayout first = (LinearLayout)second.getParent();
            linearCombineGiftCode.removeViewAt(linearCombineGiftCode.indexOfChild(first));
            linearCombineGiftCode.invalidate();
        }
    };

    private void processAddCombineStrips() {

        if(linearCombineGiftCode.getChildCount() == 5){

        }else{
            addCombineStrip(true);
        }


    }
}
