package com.dot2dotz.app.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class WalletSuccess {

    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("txnid")
    @Expose
    private String txnid;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("surl")
    @Expose
    private String surl;
    @SerializedName("furl")
    @Expose
    private String furl;
    @SerializedName("KEY")
    @Expose
    private String kEY;
    @SerializedName("merchantSalt")
    @Expose
    private String merchantSalt;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getFurl() {
        return furl;
    }

    public void setFurl(String furl) {
        this.furl = furl;
    }

    public String getKEY() {
        return kEY;
    }

    public void setKEY(String kEY) {
        this.kEY = kEY;
    }

    public String getMerchantSalt() {
        return merchantSalt;
    }

    public void setMerchantSalt(String merchantSalt) {
        this.merchantSalt = merchantSalt;
    }
}
