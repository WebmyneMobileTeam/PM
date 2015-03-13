package com.webmyne.paylabasmerchant.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.webmyne.paylabasmerchant.R;
import com.webmyne.paylabasmerchant.util.AppUtils;


public class InvoiceRequestMain extends ActionBarActivity implements View.OnClickListener{
    Toolbar toolbar_actionbar;
    FrameLayout frame_container;
    private TextView txtInvoiceRequest,txtClashClaim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_request_main);

        toolbar_actionbar = (Toolbar)findViewById(R.id.toolbar_actionbar);

        txtInvoiceRequest= (TextView)findViewById(R.id.txtInvoiceRequest);
    //    txtClashClaim= (TextView)findViewById(R.id.txtClashClaim);
        txtInvoiceRequest.setOnClickListener(this);
     //   txtClashClaim.setOnClickListener(this);
        /* setting up the toolbar starts*/
        if (toolbar_actionbar != null) {
            toolbar_actionbar.setTitle(getString(R.string.INVOICEREQUEST));
            toolbar_actionbar.setNavigationIcon(R.drawable.icon_back);
            setSupportActionBar(toolbar_actionbar);

        }

        toolbar_actionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.RIGHT);

        layoutParams.width = (int) AppUtils.convertDpToPixel(32, InvoiceRequestMain.this);
        layoutParams.height = (int) AppUtils.convertDpToPixel(32, InvoiceRequestMain.this);
        layoutParams.rightMargin = 16;
        /* setting up the toolbar ends*/

        intView();
    }

    private void intView(){
        frame_container = (FrameLayout)findViewById(R.id.frame_container);

    }

    @Override
    public void onClick(View v) {

      switch (v.getId()){

          case R.id.txtInvoiceRequest:

              Intent intent=new Intent(InvoiceRequestMain.this,InvoiceRequestActivity.class);
              startActivity(intent);

              break;

         /* case R.id.txtClashClaim:

              Intent intentClaim=new Intent(InvoiceRequestMain.this,CommissionClaimActivity.class);
              startActivity(intentClaim);
              break;*/
      }


    }
}
