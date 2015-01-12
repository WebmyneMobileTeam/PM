package com.webmyne.paylabasmerchant.model;

/**
 * Created by Android on 08-12-2014.
 */
public class State {

    public int StateID;

    public String StateName ;

    public int CountryID;

 public State() {
    }
State(int sid,String sname,int countryc){
    this.StateID=sid;
    this.StateName=sname;
    this.CountryID=countryc;
}
}
