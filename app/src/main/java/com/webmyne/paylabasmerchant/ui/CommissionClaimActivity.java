package com.webmyne.paylabasmerchant.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.webmyne.paylabasmerchant.R;

import java.util.ArrayList;

public class CommissionClaimActivity extends ActionBarActivity {
    Toolbar toolbar_actionbar;
    private ListView commossionClainList;
    private CommissionClaimAdapter commissionClaimAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_claim);
        commossionClainList = (ListView)findViewById(R.id.commossionClainList);
        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
                /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle(getString(R.string.code_commisionclaimactivity_TITLECOMISSIONCLIAM));
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



        commissionClaimAdapter=new CommissionClaimAdapter(CommissionClaimActivity.this,tempList);
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
        private ImageView detailList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.item_commission_category, parent, false);

                holder = new ViewHolder();
                holder.detailList= (ImageView) convertView.findViewById(R.id.detailList);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.detailList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent=new Intent(CommissionClaimActivity.this,CommissionClaimCategoryDetailActivity.class);
                    startActivity(intent);
                }
            });
            return convertView;
        }

    }
}
