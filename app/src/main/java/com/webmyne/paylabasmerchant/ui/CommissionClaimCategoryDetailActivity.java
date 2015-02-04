package com.webmyne.paylabasmerchant.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.webmyne.paylabasmerchant.R;

import java.util.ArrayList;

public class CommissionClaimCategoryDetailActivity extends ActionBarActivity {

    private ListView commossionClainList;
    private CommissionClaimAdapter commissionClaimAdapter;
    private Toolbar toolbar_actionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_claim_category_detail);
        commossionClainList = (ListView)findViewById(R.id.commossionClainDetailList);
        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
                /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle("Commission Clain");
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

        commissionClaimAdapter=new CommissionClaimAdapter(CommissionClaimCategoryDetailActivity.this,tempList);
        commossionClainList.setAdapter(commissionClaimAdapter);
    }

    private class CommissionClaimAdapter extends BaseAdapter {

        private ArrayList<String> redeemList;
        private Context context;
        private LayoutInflater mInflater;
        private ViewHolder holder;

        public CommissionClaimAdapter(FragmentActivity activity, ArrayList<String> redeemGiftCodesList) {
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
