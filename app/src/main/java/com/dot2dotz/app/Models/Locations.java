package com.dot2dotz.app.Models;

import java.io.Serializable;



public class Locations extends Throwable implements Serializable{

    String sAddress;
    String dAddress;
    String sLatitude;
    String dLatitude;
    String sLongitude;
    String dLongitude;
    String goods;
    String userName;
    String userMobile;

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getdAddress() {
        return dAddress;
    }

    public void setdAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public String getsLatitude() {
        return sLatitude;
    }

    public void setsLatitude(String sLatitude) {
        this.sLatitude = sLatitude;
    }

    public String getdLatitude() {
        return dLatitude;
    }

    public void setdLatitude(String dLatitude) {
        this.dLatitude = dLatitude;
    }

    public String getsLongitude() {
        return sLongitude;
    }

    public void setsLongitude(String sLongitude) {
        this.sLongitude = sLongitude;
    }

    public String getdLongitude() {
        return dLongitude;
    }

    public void setdLongitude(String dLongitude) {
        this.dLongitude = dLongitude;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }


    @Override
    public String toString() {
        return "Locations{" +
                "sAddress='" + sAddress + '\'' +
                ", dAddress='" + dAddress + '\'' +
                ", sLatitude='" + sLatitude + '\'' +
                ", dLatitude='" + dLatitude + '\'' +
                ", sLongitude='" + sLongitude + '\'' +
                ", dLongitude='" + dLongitude + '\'' +
                ", goods='" + goods + '\'' +
                ", user_name='" + userName + '\'' +
                ", user_mobile='" + userMobile + '\'' +
                '}';
    }
}
