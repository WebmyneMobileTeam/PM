package com.webmyne.paylabasmerchant.model;

/**
 * Created by Android on 29-01-2015.
 */
public class FormDetail {


    // selected type- passport
    public String FormDetailType;

    //i.e Passport Number
    public String FormDetail;

    public FormDetail(String formDetailType, String formDetail) {
        FormDetailType = formDetailType;
        FormDetail = formDetail;
    }
}
