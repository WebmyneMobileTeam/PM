package com.webmyne.paylabasmerchant.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.webmyne.paylabasmerchant.model.OTPDialog;
import com.webmyne.paylabasmerchant.model.UnclaimedGCDetail;
import com.webmyne.paylabasmerchant.ui.widget.CallWebService;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import com.webmyne.paylabasmerchant.util.PrefUtils;

import org.json.JSONObject;

import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceRequestActivity extends ActionBarActivity {
    Toolbar toolbar_actionbar;
    UnclaimedGCDetail unclaimedGCDetail;
    private ListView invoiceRequestList;
    private InvoiceRequestAdapter invoiceRequestAdapter;
    private TextView txtTotalAmount;
    private View layoutFrom,layoutTo;
    private TextView txtFromDate,txtFromMonth,txtFromYear;
    private TextView txtToDate,txtToMonth,txtToYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_request);
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

        invoiceRequestList = (ListView)findViewById(R.id.invoiceRequestList);
        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
                /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle("Generated Invoice");
            toolbar_actionbar.setNavigationIcon(R.drawable.icon_back);
            setSupportActionBar(toolbar_actionbar);

        }
        toolbar_actionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> tempList=new ArrayList<String>();
        tempList.add("");
        tempList.add("");
        tempList.add("");
        tempList.add("");
        tempList.add("");
        tempList.add("");
        tempList.add("");
        tempList.add("");
        try{
            AffilateUser user= PrefUtils.getMerchant(InvoiceRequestActivity.this);
            JSONObject userObject = new JSONObject();

            userObject.put("AffiliateID",user.UserID);
            userObject.put("ServiceID","7");


            Log.e("object",userObject.toString());

            final CircleDialog circleDialog = new CircleDialog(InvoiceRequestActivity.this, 0);
            circleDialog.setCancelable(true);
            circleDialog.show();
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.INVOICE_REQUESTS, userObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    circleDialog.dismiss();
                    String response = jobj.toString();
                    Log.e(" Response", "" + response);
                    unclaimedGCDetail=new GsonBuilder().create().fromJson(jobj.toString(), UnclaimedGCDetail.class);
                    txtTotalAmount.setText(unclaimedGCDetail.claimAmount+"");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String dateInString = "01-02-2015";

                    try {

                        Date date = formatter.parse(dateInString);
                        System.out.println(date);
                        System.out.println(formatter.format(date));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        SimpleDateFormat date=new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat dateFormat=new SimpleDateFormat("dd");
                        SimpleDateFormat monthFormat=new SimpleDateFormat("MMM");
                        SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy");

                        Date fromDate=date.parse(unclaimedGCDetail.FromDate);
                        System.out.println(fromDate);
                        System.out.println(formatter.format(fromDate));
                        Date toDate=date.parse(unclaimedGCDetail.ToDate);
                        System.out.println(toDate);
                        System.out.println(formatter.format(toDate));

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



        invoiceRequestAdapter=new InvoiceRequestAdapter(InvoiceRequestActivity.this,tempList);
        invoiceRequestList.setAdapter(invoiceRequestAdapter);
    }

    private class InvoiceRequestAdapter extends BaseAdapter {

        private ArrayList<String> redeemList;
        private Context context;
        private LayoutInflater mInflater;
        private ViewHolder holder;

        public InvoiceRequestAdapter(FragmentActivity activity, ArrayList<String> redeemGiftCodesList) {
            this.context = activity;
            this.redeemList = redeemGiftCodesList;
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

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.item_category_detail_view, parent, false);

                holder = new ViewHolder();

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

    }
}
