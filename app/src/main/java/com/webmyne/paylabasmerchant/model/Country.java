package com.webmyne.paylabasmerchant.model;

/**
 * Created by Android on 08-12-2014.
 */
public class Country {

    public int CountryID;

    public String CountryName;

    public int CountryCode;

    public String ShortCode;

    public int ForTopUp;

    public String FlagClass;

    public String CountryShortName;

    Country(){    }

public Country(int cid,String cname,int ccode,String shcode,int ftopup,String flagc,String cshname){
    this.CountryID=cid;
    this.CountryName=cname;
    this.CountryCode=ccode;
    this.ShortCode=shcode;
    this.ForTopUp=ftopup;
    this.FlagClass=flagc;
    this.CountryShortName=cshname;
}

}
