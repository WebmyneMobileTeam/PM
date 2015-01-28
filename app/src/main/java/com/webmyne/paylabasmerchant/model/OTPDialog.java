package com.webmyne.paylabasmerchant.model;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.webmyne.paylabasmerchant.R;


/**
 * Created by Android on 27-01-2015.
 */
public class OTPDialog extends Dialog{

    private View convertView;
    private OnConfirmListner listner;
    private EditText edOTP;
    private Button btnConfirm;
    private ImageView btnBack;

    public OTPDialog(final Context context, int theme, final String checkOTP) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = inflater.inflate(R.layout.item_otp_dialog,null);
        setContentView(convertView);
        btnBack = (ImageView)convertView.findViewById(R.id.btnBack);
        edOTP = (EditText)convertView.findViewById(R.id.edOTP);
        btnConfirm = (Button)convertView.findViewById(R.id.btnFinishOTP);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edOTP.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context, "Please enter One Time Password", Toast.LENGTH_SHORT).show();
                }else{
                     if(edOTP.getText().toString().equalsIgnoreCase(checkOTP)){
                         listner.onComplete();
                         dismiss();
                     }else{
                         Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show();
                     }


                }
            }
        });

        this.show();

    }

    public void setColor(String btnColor){
        btnConfirm.setBackgroundColor(Color.parseColor(btnColor));
    }


    public void setOnConfirmListner(OnConfirmListner listner){
        this.listner = listner;
    }

    public static interface OnConfirmListner{
        public void onComplete();
    }



}
