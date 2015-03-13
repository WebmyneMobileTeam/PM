package com.webmyne.paylabasmerchant.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.GCInvoiceDetail;
import com.webmyne.paylabasmerchant.model.UnclaimedGCDetail;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.LanguageStringUtil;
import com.webmyne.paylabasmerchant.util.PrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceRequestActivity extends ActionBarActivity {
    Toolbar toolbar_actionbar;
    UnclaimedGCDetail unclaimedGCDetail;
    private ListView invoiceRequestList;
    private InvoiceRequestAdapter invoiceRequestAdapter;
    private TextView txtTotalAmount,txtInvoiceRequest;
    private View layoutFrom,layoutTo;
    private TextView txtFromDate,txtFromMonth,txtFromYear;
    private TextView txtToDate,txtToMonth,txtToYear;
    private AffilateUser user;
    private ArrayList<GCInvoiceDetail> tempList;
    private ImageView imgChkButton;
    private boolean isbtnclick=false;

    private Double TotalAmount;
    private ArrayList<String> GCID = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_request);
        init();
                /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle(getString(R.string.GENERATEDINVOICE));
            toolbar_actionbar.setNavigationIcon(R.drawable.icon_back);
            setSupportActionBar(toolbar_actionbar);
        }

        toolbar_actionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtInvoiceRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("GCID Size",""+GCID.size());
                if(unclaimedGCDetail.isAbleToClaim==true) {
                  if(GCID.size()!=0) {
                      claimInvoice();
                  }
                   else{
                      SimpleToast.error(InvoiceRequestActivity.this,getString(R.string.errstring));
                  }
               } else {
                   SimpleToast.error(InvoiceRequestActivity.this,getString(R.string.YOUARENOTABLETOCLAIMINVOICE));
               }
            }
        });
    }




    private void init(){



        txtTotalAmount= (TextView)findViewById(R.id.txtTotalAmount);
        //from date
        layoutFrom= (View)findViewById(R.id.layoutFrom);
        txtFromDate= (TextView)layoutFrom.findViewById(R.id.txtFromDate);
        txtFromMonth= (TextView)layoutFrom.findViewById(R.id.txtFromMonth);
        txtFromYear= (TextView)layoutFrom.findViewById(R.id.txtFromYear);
        //to date
        layoutTo= (View)findViewById(R.id.layoutTo);
        txtToDate= (TextView)layoutTo.findViewById(R.id.txtFromDate);
        txtToMonth= (TextView)layoutTo.findViewById(R.id.txtFromMonth);
        txtToYear= (TextView)layoutTo.findViewById(R.id.txtFromYear);
        txtInvoiceRequest= (TextView)findViewById(R.id.txtInvoiceRequest);
        invoiceRequestList = (ListView)findViewById(R.id.invoiceRequestList);
        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);



        imgChkButton= (ImageView)findViewById(R.id.imgChkButton);
        imgChkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isbtnclick){
                    imgChkButton.setImageResource(R.drawable.checkboxall);
                    invoiceRequestAdapter.setCheckedAll();
                    invoiceRequestAdapter.notifyDataSetChanged();
                    isbtnclick=true;
                }
                else{

                    imgChkButton.setImageResource(R.drawable.checkboxunselect);
                    invoiceRequestAdapter.setUnCheckedAll();

                    invoiceRequestAdapter.notifyDataSetChanged();
                    isbtnclick=false;
                }



            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        user= PrefUtils.getMerchant(InvoiceRequestActivity.this);
        getUnclaimedGCDetail();
    }

    private void setFromAndToDate(){

        try {
            SimpleDateFormat date=new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd");
            SimpleDateFormat monthFormat=new SimpleDateFormat("MMM");
            SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy");

            Date fromDate=date.parse(unclaimedGCDetail.FromDate);
            System.out.println(fromDate);
            System.out.println(date.format(fromDate));
            Date toDate=date.parse(unclaimedGCDetail.ToDate);
            System.out.println(toDate);
            System.out.println(date.format(toDate));

            txtFromDate.setText(dateFormat.format(fromDate) + "");
            txtFromMonth.setText(monthFormat.format(fromDate)+"");
            txtFromYear.setText(yearFormat.format(fromDate)+"");
            txtToDate.setText(dateFormat.format(toDate)+"");
            txtToMonth.setText(monthFormat.format(toDate)+"");
            txtToYear.setText(yearFormat.format(toDate)+"");
        } catch (Exception e){
            e.printStackTrace();
        }

    }



    private void getUnclaimedGCDetail(){

        try{
            JSONObject userObject = new JSONObject();
            userObject.put("AffiliateID",user.UserID);
            userObject.put("ServiceID","7");
            userObject.put("Culture", LanguageStringUtil.CultureString(InvoiceRequestActivity.this));

            Log.e("get request object",userObject.toString());

            final CircleDialog circleDialog = new CircleDialog(InvoiceRequestActivity.this, 0);
            circleDialog.setCancelable(true);
            circleDialog.show();
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.INVOICE_REQUESTS, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e(" Response  List", "" + response);
                    unclaimedGCDetail=new GsonBuilder().create().fromJson(jobj.toString(), UnclaimedGCDetail.class);
                    txtTotalAmount.setText(unclaimedGCDetail.claimAmount+"");
                    setFromAndToDate();

                    TotalAmount = 0.0;

                    tempList=unclaimedGCDetail.gcInvoiceDetails;
                    invoiceRequestAdapter=new InvoiceRequestAdapter(InvoiceRequestActivity.this,tempList);
                    invoiceRequestList.setAdapter(invoiceRequestAdapter);



                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    circleDialog.dismiss();
                    SimpleToast.error(InvoiceRequestActivity.this,getResources().getString(R.string.er_network));

                }
            });
            req.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
            MyApplication.getInstance().addToRequestQueue(req);
        }catch(Exception e){
            Log.e("exception", e.toString());
        }

    }
/*    void claimInvoice2(){

        //  GCID.add(getPosition,""+redeemList.get(getPosition).id);

        Log.e("GCID Size",""+GCID.size());

        for (int ii = 0; ii < GCID.size(); ii++) {
            Log.e("GCID ID",""+GCID.get(ii));
        }



        try{

            JSONArray arr = new JSONArray();


            for (int i = 0; i < 3; i++) {
                JSONObject jobj = new JSONObject();
                jobj.put("GiftCode", ""+i);
                arr.put(jobj);
            }


            JSONObject userObject = new JSONObject();

            userObject.put("AffiliateID", user.UserID);
            userObject.put("ServiceID","7");
            userObject.put("GiftCode", arr);
            userObject.put("Culture", LanguageStringUtil.CultureString(InvoiceRequestActivity.this));

            Log.e("get request object",userObject.toString());
        }catch (Exception e){
            Log.e("Exc",e.toString());
        }

    }*/


    private void claimInvoice(){
        try{

            JSONArray arr = new JSONArray();
            for (int i = 0; i < GCID.size(); i++) {
                JSONObject jobj = new JSONObject();
                jobj.put("Id",Integer.valueOf(GCID.get(i)));
                arr.put(jobj);
            }


            JSONObject userObject = new JSONObject();
            userObject.put("AffiliateID",user.UserID);
            userObject.put("ClaimAmt",Float.valueOf(txtTotalAmount.getText().toString().trim()));
            userObject.put("ServiceID",7);
            userObject.put("GCID",arr);
            userObject.put("Culture",LanguageStringUtil.CultureString(InvoiceRequestActivity.this));

            Log.e("post object claim",userObject.toString());

            final CircleDialog circleDialog = new CircleDialog(InvoiceRequestActivity.this, 0);
            circleDialog.setCancelable(true);
            circleDialog.show();
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.CLAIM_INVOICE, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e(" Response ClaimInvoice", "" + response);
                    try {
                            if(jobj.getString("ResponseCode").toString().equalsIgnoreCase("1")){
                                SimpleToast.ok(InvoiceRequestActivity.this,jobj.getString("ResponseMsg").toString());
                            } else {
                                SimpleToast.error(InvoiceRequestActivity.this, jobj.getString("ResponseMsg").toString());
                            }

                      } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    circleDialog.dismiss();
                    SimpleToast.error(InvoiceRequestActivity.this,getResources().getString(R.string.er_network));

                }
            });
            req.setRetryPolicy(  new DefaultRetryPolicy(0,0,0));
            MyApplication.getInstance().addToRequestQueue(req);
        }catch(Exception e){
            Log.e("exception",e.toString());
        }
    }

    private class InvoiceRequestAdapter extends BaseAdapter {


        private int FianlPos;

        private ArrayList<GCInvoiceDetail> redeemList;
        private Context context;
        private LayoutInflater mInflater;
        private ViewHolder holder;
        private boolean checkedAll;

        public InvoiceRequestAdapter(FragmentActivity activity, ArrayList<GCInvoiceDetail> redeemGiftCodesList) {
            this.context = activity;
            this.redeemList = redeemGiftCodesList;
            FianlPos=0;


        }

        void setCheckedAll ( ){
            checkedAll=true;
            TotalAmount=0.0;
           GCID = new ArrayList<String>();
            for (int i = 0; i < redeemList.size(); i++) {
                    TotalAmount  = TotalAmount + redeemList.get(i).GCAmount;
                    txtTotalAmount.setText(""+TotalAmount);
                     redeemList.get(i).isinvocieSelected=true;
                    redeemList.get(i).setSelectedALL(checkedAll);
                    GCID.add(String.valueOf(redeemList.get(i).id));

            }
            notifyDataSetChanged();
        }

        void setUnCheckedAll (){

            checkedAll=false;

            TotalAmount=0.0;
            txtTotalAmount.setText(""+TotalAmount);
            GCID = new ArrayList<String>();

                for (int i = 0; i < redeemList.size(); i++) {
                    redeemList.get(i).isinvocieSelected=false;
                    redeemList.get(i).setSelectedALL(checkedAll);

            }


        }


        @Override
        public int getCount() {

            return redeemList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            private TextView name,mobile,amount,date,txtlocalAmount;
            ImageView chkbox;

        }



        @Override
        public View getView( final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflator =(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = inflator.inflate(R.layout.item_category_detail_view, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.txtName);
                viewHolder.mobile= (TextView) convertView.findViewById(R.id.txtMobile);
                viewHolder.amount= (TextView)convertView. findViewById(R.id.txtAmount);
                viewHolder.txtlocalAmount= (TextView)convertView. findViewById(R.id.txtlocalAmount);
                viewHolder.date= (TextView) convertView.findViewById(R.id.txtDate);

                viewHolder.chkbox = (ImageView) convertView.findViewById(R.id.chkBox);

                convertView.setTag(viewHolder);
                convertView.setTag(R.id.txtName, viewHolder.name);
                convertView.setTag(R.id.txtMobile, viewHolder.mobile);
                convertView.setTag(R.id.txtAmount, viewHolder.amount);
                convertView.setTag(R.id.txtlocalAmount, viewHolder.txtlocalAmount);
                convertView.setTag(R.id.txtDate, viewHolder.date);

                convertView.setTag(R.id.chkBox, viewHolder.chkbox);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.chkbox.setTag(position); // This line is important.
            viewHolder.name.setText(tempList.get(position).SendTo + "");
            viewHolder.mobile.setText(tempList.get(position).ReceiverMob + "");
            viewHolder.amount.setText("EUR "+tempList.get(position).GCAmount + "");
            viewHolder.date.setText(tempList.get(position).WithdrawalDateString + "");

            viewHolder.txtlocalAmount.setText(tempList.get(position).LocalValueReceivedCurrancy+" "+tempList.get(position).LocalValueReceived + "");

            if (redeemList.get(position).isinvocieSelected==true) {
                viewHolder.chkbox.setImageResource(R.drawable.checkboxall);
                viewHolder.chkbox.setTag("active");
            } else {
                viewHolder.chkbox.setImageResource(R.drawable.checkboxunselect);
                viewHolder.chkbox.setTag("deactive");
            }



            viewHolder.chkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tag = viewHolder.chkbox.getTag().toString();
                    if (tag.equalsIgnoreCase("active")) { // uncheck checkbox, add

                        GCID.remove(String.valueOf(redeemList.get(position).id));

                        TotalAmount  = TotalAmount - redeemList.get(position).GCAmount;
                        txtTotalAmount.setText(""+TotalAmount);


                        redeemList.get(position).isinvocieSelected = false;
                        viewHolder.chkbox.setImageResource(R.drawable.checkboxunselect);
                        viewHolder.chkbox.setTag("deactive");
                    } else { // check checkbox, remove

                        GCID.add(String.valueOf(redeemList.get(position).id));

                        TotalAmount  = TotalAmount + redeemList.get(position).GCAmount;
                        txtTotalAmount.setText(""+TotalAmount);



                        viewHolder.chkbox.setImageResource(R.drawable.checkboxall);
                        viewHolder.chkbox.setTag("active");
                        redeemList.get(position).isinvocieSelected = true;
                    }


                }
            });


            return convertView;
        }

    }

}
