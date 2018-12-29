package com.dot2dotz.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dot2dotz.app.Dot2dotzApplicaton;
import com.dot2dotz.app.Helper.URLHelper;
import com.dot2dotz.app.Models.AccessDetails;
import com.dot2dotz.app.R;
import com.dot2dotz.app.Utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.dot2dotz.app.Dot2dotzApplicaton.trimMessage;


public class AccessKeyActivity extends AppCompatActivity {

    EditText txtAccessKey, txtUserName;

    FloatingActionButton btnAccessKey;

    LinearLayout lnrAccessLogin, lnrAccessLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_begin);
        CommonUtils.setLanguage(AccessKeyActivity.this);
        initView();
    }

    private void initView() {
        txtAccessKey = findViewById(R.id.txtAccessKey);
        txtUserName = findViewById(R.id.txtUserName);
        lnrAccessLogin = findViewById(R.id.lnrAccessLogin);
        lnrAccessLoading = findViewById(R.id.lnrAccessLoading);
        btnAccessKey = findViewById(R.id.btnAccessKey);

        btnAccessKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtUserName.getText().toString().equalsIgnoreCase("")) {
                    displayMessage(getResources().getString(R.string.enter_username));
                } else if (txtAccessKey.getText().toString().equalsIgnoreCase("")) {
                    displayMessage(getResources().getString(R.string.enter_access_key));
                } else {
                    accessKeyAPI();
                }
            }
        });
    }

    public void accessKeyAPI() {

        JSONObject object = new JSONObject();
        try {
            object.put("username", txtUserName.getText().toString());
            object.put("accesskey", txtAccessKey.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadingVisibility();

        Log.e("URL: ", URLHelper.login);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.access_login, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response: ", response.toString());
                loginVisibility();
                processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                String Message;
                loginVisibility();
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {

                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                displayMessage(getString(R.string.something_went_wrong));
                            }
                        } else if (response.statusCode == 401) {
                            displayMessage(errorObj.optString("message"));
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
                            if (json != null && !json.equalsIgnoreCase("")) {
                                displayMessage(json);
                            } else {
                                displayMessage(getString(R.string.please_try_again));
                            }
                        } else if (response.statusCode == 503) {
                            displayMessage(getString(R.string.server_down));
                        } else {
                            displayMessage(getString(R.string.please_try_again));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        displayMessage(getString(R.string.something_went_wrong));
                    }

                } else {
                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        accessKeyAPI();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                return headers;
            }
        };

        Dot2dotzApplicaton.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void processResponse(JSONObject response) {
        AccessDetails accessDetails = new AccessDetails();
        accessDetails.status = response.optBoolean("status");
        if (accessDetails.status) {
            JSONArray jsonArrayData = response.optJSONArray("data");
            JSONObject jsonObjectData = jsonArrayData.optJSONObject(0);
            accessDetails.id = jsonObjectData.optInt("id");
            accessDetails.clientName = jsonObjectData.optString("id");
            accessDetails.email = jsonObjectData.optString("id");
            accessDetails.product = jsonObjectData.optString("id");
            accessDetails.username = jsonObjectData.optString("id");
            accessDetails.password = jsonObjectData.optString("id");
            accessDetails.passport = jsonObjectData.optString("id");
            accessDetails.clientid = jsonObjectData.optInt("id");
            accessDetails.serviceurl = jsonObjectData.optString("id");
            accessDetails.isActive = jsonObjectData.optInt("id");
            accessDetails.createdAt = jsonObjectData.optString("id");
            accessDetails.updatedAt = jsonObjectData.optString("id");

        }
        JSONArray jsonArraySettings = response.optJSONArray("setting");
        if (jsonArraySettings.length() > 0) {
            JSONObject jsonObjectSettings = jsonArraySettings.optJSONObject(0);
            accessDetails.siteTitle = jsonObjectSettings.optString("site_title");
            accessDetails.siteLogo = jsonObjectSettings.optString("site_logo");
            accessDetails.siteEmailLogo = jsonObjectSettings.optString("site_email_logo");
            accessDetails.siteIcon = jsonObjectSettings.optString("site_icon");
            accessDetails.siteCopyright = jsonObjectSettings.optString("site_copyright");

            accessDetails.providerSelectTimeout = jsonObjectSettings.optString("provider_select_timeout");
            accessDetails.providerSearchRadius = jsonObjectSettings.optString("provider_search_radius");
            accessDetails.basePrice = jsonObjectSettings.optString("base_price");
            accessDetails.pricePerMinute = jsonObjectSettings.optString("price_per_minute");
            accessDetails.taxPercentage = jsonObjectSettings.optString("tax_percentage");
            accessDetails.stripeSecretKey = jsonObjectSettings.optString("stripe_secret_key");
            accessDetails.stripePublishableKey = jsonObjectSettings.optString("stripe_publishable_key");
            accessDetails.cASH = jsonObjectSettings.optString("CASH");
            accessDetails.cARD = jsonObjectSettings.optString("CARD");
            accessDetails.manualRequest = jsonObjectSettings.optString("manual_request");
            accessDetails.defaultLang = jsonObjectSettings.optString("default_lang");
            accessDetails.currency = jsonObjectSettings.optString("currency");
            accessDetails.distance = jsonObjectSettings.optString("distance");
            accessDetails.scheduledCancelTimeExceed = jsonObjectSettings.optString("scheduled_cancel_time_exceed");
            accessDetails.pricePerKilometer = jsonObjectSettings.optString("price_per_kilometer");
            accessDetails.commissionPercentage = jsonObjectSettings.optString("commission_percentage");
            accessDetails.storeLinkAndroid = jsonObjectSettings.optString("store_link_android");
            accessDetails.storeLinkIos = jsonObjectSettings.optString("store_link_ios");
            accessDetails.dailyTarget = jsonObjectSettings.optString("daily_target");
            accessDetails.surgePercentage = jsonObjectSettings.optString("surge_percentage");
            accessDetails.surgeTrigger = jsonObjectSettings.optString("surge_trigger");
            accessDetails.demoMode = jsonObjectSettings.optString("demo_mode");
            accessDetails.bookingPrefix = jsonObjectSettings.optString("booking_prefix");
            accessDetails.sosNumber = jsonObjectSettings.optString("sos_number");
            accessDetails.contactNumber = jsonObjectSettings.optString("contact_number");
            accessDetails.contactEmail = jsonObjectSettings.optString("contact_email");
            accessDetails.socialLogin = jsonObjectSettings.optString("social_login");
        }

        GoToBeginActivity();
    }

    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(AccessKeyActivity.this, SignIn.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void loadingVisibility() {
        lnrAccessLogin.setVisibility(View.GONE);
        lnrAccessLoading.setVisibility(View.VISIBLE);
    }

    private void loginVisibility() {
        lnrAccessLogin.setVisibility(View.VISIBLE);
        lnrAccessLoading.setVisibility(View.GONE);
    }

    public void displayMessage(String toastString) {
        try {
            Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } catch (Exception e) {
            try {
                Toast.makeText(AccessKeyActivity.this, "" + toastString, Toast.LENGTH_SHORT).show();
            } catch (Exception ee) {
                e.printStackTrace();
            }
        }
    }
}
