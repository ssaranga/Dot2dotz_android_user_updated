//-----------------------------------com.dot2dotz.app.Models.AccessDetails.java-----------------------------------

package com.dot2dotz.app.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessDetails {

    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("client_name")
    @Expose
    public String clientName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("product")
    @Expose
    public String product;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("passport")
    @Expose
    public String passport;
    @SerializedName("clientid")
    @Expose
    public Integer clientid;
    @SerializedName("serviceurl")
    @Expose
    public String serviceurl;
    @SerializedName("is_active")
    @Expose
    public Integer isActive;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    @SerializedName("site_title")
    @Expose
    public String siteTitle;
    @SerializedName("site_logo")
    @Expose
    public String siteLogo;
    @SerializedName("site_email_logo")
    @Expose
    public String siteEmailLogo;
    @SerializedName("site_icon")
    @Expose
    public String siteIcon;
    @SerializedName("site_copyright")
    @Expose
    public String siteCopyright;
    @SerializedName("provider_select_timeout")
    @Expose
    public String providerSelectTimeout;
    @SerializedName("provider_search_radius")
    @Expose
    public String providerSearchRadius;
    @SerializedName("base_price")
    @Expose
    public String basePrice;
    @SerializedName("price_per_minute")
    @Expose
    public String pricePerMinute;
    @SerializedName("tax_percentage")
    @Expose
    public String taxPercentage;
    @SerializedName("stripe_secret_key")
    @Expose
    public String stripeSecretKey;
    @SerializedName("stripe_publishable_key")
    @Expose
    public String stripePublishableKey;
    @SerializedName("CASH")
    @Expose
    public String cASH;
    @SerializedName("CARD")
    @Expose
    public String cARD;
    @SerializedName("manual_request")
    @Expose
    public String manualRequest;
    @SerializedName("default_lang")
    @Expose
    public String defaultLang;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("distance")
    @Expose
    public String distance;
    @SerializedName("scheduled_cancel_time_exceed")
    @Expose
    public String scheduledCancelTimeExceed;
    @SerializedName("price_per_kilometer")
    @Expose
    public String pricePerKilometer;
    @SerializedName("commission_percentage")
    @Expose
    public String commissionPercentage;
    @SerializedName("store_link_android")
    @Expose
    public String storeLinkAndroid;
    @SerializedName("store_link_ios")
    @Expose
    public String storeLinkIos;
    @SerializedName("daily_target")
    @Expose
    public String dailyTarget;
    @SerializedName("surge_percentage")
    @Expose
    public String surgePercentage;
    @SerializedName("surge_trigger")
    @Expose
    public String surgeTrigger;
    @SerializedName("demo_mode")
    @Expose
    public String demoMode;
    @SerializedName("booking_prefix")
    @Expose
    public String bookingPrefix;
    @SerializedName("sos_number")
    @Expose
    public String sosNumber;
    @SerializedName("contact_number")
    @Expose
    public String contactNumber;
    @SerializedName("contact_email")
    @Expose
    public String contactEmail;
    @SerializedName("social_login")
    @Expose
    public String socialLogin;

}