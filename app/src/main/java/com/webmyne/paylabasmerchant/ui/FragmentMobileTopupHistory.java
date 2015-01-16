package com.webmyne.paylabasmerchant.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.model.AffilateUser;
import com.webmyne.paylabasmerchant.model.AppConstants;
import com.webmyne.paylabasmerchant.model.RechargeHistory;
import com.webmyne.paylabasmerchant.ui.widget.CallWebService;
import com.webmyne.paylabasmerchant.ui.widget.CircleDialog;
import com.webmyne.paylabasmerchant.ui.widget.ComplexPreferences;
import com.webmyne.paylabasmerchant.ui.widget.SimpleToast;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

public class FragmentMobileTopupHistory extends Fragment {

    private ListView listMobileTopup;
    private String[] faq_que;
    private PtrFrameLayout frame;
    ArrayList<RechargeHistory> rechargeHistories;


    public static FragmentMobileTopupHistory newInstance(String param1, String param2) {
        FragmentMobileTopupHistory fragment = new FragmentMobileTopupHistory();
        return fragment;
    }

    public FragmentMobileTopupHistory() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.fragment_mobiletopup_home, container, false);

        listMobileTopup = (ListView)convertView.findViewById(R.id.listMobileTopup);
        listMobileTopup.setEmptyView(convertView.findViewById(R.id.redeemEmptyView));

        frame = (PtrFrameLayout)convertView.findViewById(R.id.material_style_ptr_frame);

        final MaterialHeader header = new MaterialHeader(getActivity());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0,16, 0,16);
        header.setPtrFrameLayout(frame);

        frame.setLoadingMinTime(1000);
        frame.setDurationToCloseHeader(1000);
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);

        frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame){ fetchMobileTopupAndDisplay();


            }
        });

        fetchMobileTopupAndDisplay();


        return convertView;
    }

    private  void fetchMobileTopupAndDisplay(){

        final CircleDialog circleDialog=new CircleDialog(getActivity(),0);
        circleDialog.setCancelable(true);
        circleDialog.show();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        AffilateUser user = complexPreferences.getObject("current_user", AffilateUser.class);

        //GET_MY_MOBILE_TOPUPLIST
        new CallWebService(AppConstants.GET_MY_MOBILE_TOPUPLIST + user.UserID, CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {

                circleDialog.dismiss();

                frame.refreshComplete();

                Log.e("Mobile topup List", response);
                if (!(response == null)) {

                    Type listType = new TypeToken<List<RechargeHistory>>(){}.getType();
                    rechargeHistories = new GsonBuilder().create().fromJson(response, listType);

                    listMobileTopup.setAdapter(new ListMobileTopup(getActivity(),rechargeHistories));

                }

            }

            @Override
            public void error(VolleyError error) {
                frame.refreshComplete();
                circleDialog.dismiss();
//            SnackBar bar = new SnackBar(getActivity(), "Sync Error. Please Try again");
//            bar.show();
                SimpleToast.error(getActivity(),"Sync Error. Please Try again");
            }
        }.start();
    }

    public class ListMobileTopup extends BaseAdapter {

        ArrayList<RechargeHistory> mobiletopuplist1;
        Context context;
        ViewHolder holder;
        LayoutInflater mInflater;
        ListMobileTopup(Context context,ArrayList<RechargeHistory> mobiletopuplist_temp) {
            Log.e("in consrt", String.valueOf(mobiletopuplist_temp.size()));
            this.context=context;
            mobiletopuplist1 = mobiletopuplist_temp;
        }

        @Override
        public int getCount() {
            return mobiletopuplist1.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        class ViewHolder {
            TextView txt_MobileNo, txt_rechardedate, txt_AmountIndolla;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {


           mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_my_mobiletopup_list, parent, false);
                holder = new ViewHolder();
                holder.txt_MobileNo = (TextView) convertView.findViewById(R.id.txt_MobileNo);
                holder.txt_rechardedate = (TextView) convertView.findViewById(R.id.txt_rechardedate);
                holder.txt_AmountIndolla = (TextView) convertView.findViewById(R.id.txt_AmountIndolla);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_MobileNo.setText(mobiletopuplist1.get(position).MobileNo);
            holder.txt_AmountIndolla.setText("€" + mobiletopuplist1.get(position).RechargeAmount);
            holder.txt_rechardedate.setText(mobiletopuplist1.get(position).createdOnString);

            return convertView;

        }

    }






// end of main class
}
