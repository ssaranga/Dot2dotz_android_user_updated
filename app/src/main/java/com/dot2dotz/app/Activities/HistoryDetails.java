package com.dot2dotz.app.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.dot2dotz.app.Adapter.TripAdapter;
import com.dot2dotz.app.Dot2dotzApplicaton;
import com.dot2dotz.app.Helper.ConnectionHelper;
import com.dot2dotz.app.Helper.CustomDialog;
import com.dot2dotz.app.Helper.SharedHelper;
import com.dot2dotz.app.Helper.URLHelper;
import com.dot2dotz.app.Models.Driver;
import com.dot2dotz.app.Models.TripStatus;
import com.dot2dotz.app.R;
import com.dot2dotz.app.Utils.CommonUtils;
import com.dot2dotz.app.Utils.MyBoldTextView;
import com.dot2dotz.app.Utils.MyButton;
import com.dot2dotz.app.Utils.ScreenshotType;
import com.dot2dotz.app.Utils.ScreenshotUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.dot2dotz.app.Dot2dotzApplicaton.trimMessage;

public class HistoryDetails extends AppCompatActivity {

    public JSONObject jsonObject;
    Activity activity;
    Context context;
    Boolean isInternet;
    ConnectionHelper helper;
    CustomDialog customDialog;
    MyBoldTextView tripAmount, tripDate, paymentType, booking_id;
    MyBoldTextView tripComments, tripProviderName, tripSource;
    MyBoldTextView lblTotalPrice, lblBookingID, lblTitle;
    MyBoldTextView lblBasePrice, lblDistancePrice;
    MyBoldTextView waiting_fare, lblDistanceCovered, lblTaxPrice;
    private MyBoldTextView discount;
    private LinearLayout discountLayout;
    MyBoldTextView lblTimeTaken;
    ImageView tripImg, tripProviderImg, paymentTypeImg, backArrow;
    RatingBar tripProviderRating;
    LinearLayout lnrComments, lnrUpcomingLayout;
    LinearLayout parentLayout, profileLayout, lnrInvoice, lnrInvoiceSub;
    String tag = "";
    MyButton btnCancelRide;
    Driver driver;
    String reason = "";
    RecyclerView tripRv;
    TripAdapter tripAdapter;
    ArrayList<TripStatus> tripArrayList = new ArrayList<>();

    Button btnViewInvoice, btnCall, btnClose;
    ImageView image_view, iv_shareInvoice;
    ScrollView sv_parentInvoice;
    RelativeLayout rel_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        CommonUtils.setLanguage(HistoryDetails.this);

        findViewByIdAndInitialize();
        try {
            Intent intent = getIntent();
            String post_details = intent.getStringExtra("post_value");
            tag = intent.getStringExtra("tag");
            jsonObject = new JSONObject(post_details);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = null;
        }

        if (jsonObject != null) {

            if (tag.equalsIgnoreCase("past_trips")) {
                btnCancelRide.setVisibility(View.GONE);
                lnrComments.setVisibility(View.VISIBLE);
                lnrUpcomingLayout.setVisibility(View.GONE);
                getRequestDetails();
                lblTitle.setText(context.getResources().getString(R.string.past_trips));
            } else {
                lnrUpcomingLayout.setVisibility(View.VISIBLE);
                btnViewInvoice.setVisibility(View.GONE);
                btnCancelRide.setVisibility(View.VISIBLE);
                lnrComments.setVisibility(View.GONE);
                getUpcomingDetails();
                lblTitle.setText(context.getResources().getString(R.string.upcoming_trips));
            }
        }
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryDetails.this, ShowProfile.class);
                intent.putExtra("driver", driver);
                startActivity(intent);
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //setup recycler view
        tripArrayList = new ArrayList<>();
        tripRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        tripAdapter = new TripAdapter(tripArrayList, context);
        tripRv.setAdapter(tripAdapter);
    }

    public void findViewByIdAndInitialize() {
        activity = HistoryDetails.this;
        context = HistoryDetails.this;
        helper = new ConnectionHelper(activity);
        isInternet = helper.isConnectingToInternet();
        parentLayout = findViewById(R.id.parentLayout);
        profileLayout = findViewById(R.id.profile_detail_layout);
        sv_parentInvoice = findViewById(R.id.sv_parentInvoice);
        lnrInvoice = findViewById(R.id.lnrInvoice);
        lnrInvoiceSub = findViewById(R.id.lnrInvoiceSub);
        rel_header = findViewById(R.id.rel_header);
        parentLayout.setVisibility(View.GONE);
        backArrow = findViewById(R.id.backArrow);
        image_view = findViewById(R.id.image_view);
        tripAmount = findViewById(R.id.tripAmount);
        tripDate = findViewById(R.id.tripDate);
        paymentType = findViewById(R.id.paymentType);
        booking_id = findViewById(R.id.booking_id);
        paymentTypeImg = findViewById(R.id.paymentTypeImg);
        tripProviderImg = findViewById(R.id.tripProviderImg);
        tripImg = findViewById(R.id.tripImg);
        tripComments = findViewById(R.id.tripComments);
        tripProviderName = findViewById(R.id.tripProviderName);
        tripProviderRating = findViewById(R.id.tripProviderRating);
        tripSource = findViewById(R.id.tripSource);
        lblBookingID = findViewById(R.id.lblBookingID);
        lblBasePrice = findViewById(R.id.lblBasePrice);
        lblDistanceCovered = findViewById(R.id.lblDistanceCovered);
        lblTaxPrice = findViewById(R.id.lblTaxPrice);
        discount = findViewById(R.id.discount);
        discountLayout = findViewById(R.id.discountLayout);
        lblDistancePrice = findViewById(R.id.lblDistancePrice);
        waiting_fare = findViewById(R.id.waiting_fare);
        lblTimeTaken = findViewById(R.id.lblTimeTaken);
        lblTotalPrice = findViewById(R.id.lblTotalPrice);
        lblTitle = findViewById(R.id.lblTitle);
        btnCancelRide = findViewById(R.id.btnCancelRide);
        lnrComments = findViewById(R.id.lnrComments);
        tripRv = findViewById(R.id.trip_rv);
        lnrUpcomingLayout = findViewById(R.id.lnrUpcomingLayout);
        btnViewInvoice = findViewById(R.id.btnViewInvoice);
        iv_shareInvoice = findViewById(R.id.iv_shareInvoice);
        btnCall = findViewById(R.id.btnCall);
        btnClose = findViewById(R.id.btnClose);

        btnCancelRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.app_name)
                        .setMessage(getString(R.string.cencel_request))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                showreasonDialog();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnViewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnrInvoice.setVisibility(View.VISIBLE);
            }
        });

        iv_shareInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot(ScreenshotType.CUSTOM);
            }
        });

        lnrInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnrInvoice.setVisibility(View.GONE);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnrInvoice.setVisibility(View.GONE);
            }
        });

        lnrInvoiceSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (driver.getMobile() != null && !driver.getMobile().equalsIgnoreCase("null") && driver.getMobile().length() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
                    } else {
                        Intent intentCall = new Intent(Intent.ACTION_CALL);
                        intentCall.setData(Uri.parse("tel:" + driver.getMobile()));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intentCall);
                    }
                } else {
                    displayMessage(getString(R.string.user_no_mobile));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            //Toast.makeText(SignInActivity.this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + driver.getMobile()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }
    }

    private void showreasonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.cancel_dialog, null);
        final EditText reasonEtxt = (EditText) view.findViewById(R.id.reason_etxt);
        Button submitBtn = (Button) view.findViewById(R.id.submit_btn);
        builder.setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.app_name)
                .setView(view)
                .setCancelable(true);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = reasonEtxt.getText().toString();
                cancelRequest();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void getRequestDetails() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();

        Log.e("URL: ", URLHelper.GET_HISTORY_DETAILS_API);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.GET_HISTORY_DETAILS_API + "?request_id=" + jsonObject.optString("id"), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    Log.v("HistoryDetails", response.toString());
                    if (response != null && response.length() > 0) {
                        if (response.optJSONObject(0) != null) {
                            Glide.with(activity).load(response.optJSONObject(0).optString("static_map")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(tripImg);
                            Log.e("History Details", "onResponse: Currency" + SharedHelper.getKey(context, "currency"));
                            JSONObject providerObj = response.optJSONObject(0).optJSONObject("provider");
                            if (providerObj != null) {
                                driver = new Driver();
                                driver.setFname(providerObj.optString("first_name"));
                                driver.setLname(providerObj.optString("last_name"));
                                driver.setMobile(providerObj.optString("mobile"));
                                driver.setEmail(providerObj.optString("email"));
                                driver.setImg(providerObj.optString("avatar"));
                                driver.setRating(providerObj.optString("rating"));
                            }

                            try {
                                JSONArray userdrop = response.optJSONObject(0).getJSONArray("userdrop");
                                if (userdrop != null) {
                                    tripArrayList.clear();
                                    for (int i = 0; i < userdrop.length(); i++) {
                                        TripStatus flows = new TripStatus();
                                        flows.setdeliveryAddress(userdrop.getJSONObject(i).optString("d_address"));
                                        flows.setcomments(userdrop.getJSONObject(i).optString("service_items"));
                                        flows.setstatus(userdrop.getJSONObject(i).optString("status"));
                                        flows.setD_lat(userdrop.getJSONObject(i).optString("d_latitude"));
                                        flows.setD_long(userdrop.getJSONObject(i).optString("d_longitude"));
                                        tripArrayList.add(flows);
                                    }
                                    tripAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (response.optJSONObject(0).optString("booking_id") != null &&
                                    !response.optJSONObject(0).optString("booking_id").equalsIgnoreCase("")) {
                                booking_id.setText(response.optJSONObject(0).optString("booking_id"));
                                lblBookingID.setText(response.optJSONObject(0).optString("booking_id"));
                            }
                            if (response.optJSONObject(0).optInt("distance") != 0) {
                                lblDistanceCovered.setText(response.optJSONObject(0).optInt("distance") + " mi");
                            }
                            String form;
                            if (tag.equalsIgnoreCase("past_trips")) {
                                form = response.optJSONObject(0).optString("assigned_at");
                            } else {
                                form = response.optJSONObject(0).optString("schedule_at");
                            }
                            if (response.optJSONObject(0).optJSONObject("payment") != null && response.optJSONObject(0).optJSONObject("payment").optInt("discount") != 0) {
                                discountLayout.setVisibility(View.VISIBLE);
                                discount.setText(SharedHelper.getKey(context, "currency") + "" +
                                        response.optJSONObject(0).optJSONObject("payment").optInt("discount"));
                            } else {
                                discountLayout.setVisibility(View.GONE);
                            }
                            if (response.optJSONObject(0).optJSONObject("payment") != null && response.optJSONObject(0).optJSONObject("payment").optString("payable") != null &&
                                    !response.optJSONObject(0).optJSONObject("payment").optString("payable").equalsIgnoreCase("")) {
                                tripAmount.setText(SharedHelper.getKey(context, "currency") + "" + response.optJSONObject(0).optJSONObject("payment").optString("payable"));
                                response.optJSONObject(0).optJSONObject("payment");

                                Float Estimatedfare = Float.valueOf(response.optJSONObject(0).optJSONObject("payment").optString("fixed")) + Float.valueOf(response.optJSONObject(0).optJSONObject("payment").optString("distance"));

//                            lblBasePrice.setText((SharedHelper.getKey(context, "currency") + "" + response.optJSONObject(0).optJSONObject("payment").optString("fixed")));
                                lblBasePrice.setText((SharedHelper.getKey(context, "currency") + "" + Estimatedfare));
                                lblDistancePrice.setText((SharedHelper.getKey(context, "currency") + ""
                                        + response.optJSONObject(0).optJSONObject("payment").optString("distance")));
                                waiting_fare.setText(SharedHelper.getKey(context, "currency") + "" + response.optJSONObject(0).optJSONObject("payment").optString("waiting_charge"));
                                if (response.optJSONObject(0).optString("travel_time") != null &&
                                        !response.optJSONObject(0).optString("travel_time").isEmpty()) {
                                    lblTimeTaken.setText("" + response.optJSONObject(0).optString("travel_time") + " " + getResources().getString(R.string.mins));
                                } else {
                                    lblTimeTaken.setText("" + "0" + " " + getResources().getString(R.string.mins));
                                }
                                lblTaxPrice.setText((SharedHelper.getKey(context, "currency") + ""
                                        + response.optJSONObject(0).optJSONObject("payment").optString("tax")));
                                lblTotalPrice.setText((SharedHelper.getKey(context, "currency") + ""
                                        + response.optJSONObject(0).optJSONObject("payment").optString("payable" + "")));
                            } else {
                                tripAmount.setVisibility(View.GONE);
                            }
                            try {
                                tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + getYear(form) + "\n" + getTime(form));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            paymentType.setText(response.optJSONObject(0).optString("payment_mode"));
                            if (response.optJSONObject(0).optString("payment_mode").equalsIgnoreCase("CASH")) {
                                paymentTypeImg.setImageResource(R.drawable.money_icon);
                            } else {
                                paymentTypeImg.setImageResource(R.drawable.visa);
                            }
                            Glide.with(activity).load(URLHelper.base + "storage/" + response.optJSONObject(0).optJSONObject("provider").optString("avatar"))
                                    .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).dontAnimate().into(tripProviderImg);
                            if (response.optJSONObject(0).optJSONObject("rating") != null && !response.optJSONObject(0).optJSONObject("rating").optString("provider_comment").equalsIgnoreCase("") && !response.optJSONObject(0).optJSONObject("rating").optString("provider_comment").equalsIgnoreCase("null")) {
                                tripComments.setText(response.optJSONObject(0).optJSONObject("rating").optString("provider_comment", ""));
                            } else {
                                tripComments.setText(getString(R.string.no_comments));
                            }
                            if (response.optJSONObject(0).optJSONObject("rating").optString("provider_rating") != null && !response.optJSONObject(0).optJSONObject("rating").optString("provider_rating").equalsIgnoreCase("")) {
                                tripProviderRating.setRating(Float.parseFloat(response.optJSONObject(0).optJSONObject("rating").optString("provider_rating")));
                            } else {
                                tripProviderRating.setRating(0);
                            }
                            tripProviderName.setText(response.optJSONObject(0).optJSONObject("provider").optString("first_name") + " " + response.optJSONObject(0).optJSONObject("provider").optString("last_name"));
                            if (response.optJSONObject(0).optString("s_address") == null || response.optJSONObject(0).optString("d_address") == null || response.optJSONObject(0).optString("d_address").equals("") || response.optJSONObject(0).optString("s_address").equals("")) {
//                            viewLayout.setVisibility(View.GONE);
                            } else {
                                tripSource.setText(response.optJSONObject(0).optString("s_address"));
                            }

                        }
                    }
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    parentLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    String json = null;
                    String Message;
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
                                refreshAccessToken("PAST_TRIPS");
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
                        displayMessage(getString(R.string.please_try_again));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        Dot2dotzApplicaton.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void getUpcomingDetails() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();

        Log.e("URL: ", URLHelper.UPCOMING_TRIP_DETAILS);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.UPCOMING_TRIP_DETAILS + "?request_id=" + jsonObject.optString("id"), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.v("HistoryDetails", response.toString());
                    if (response != null && response.length() > 0) {
                        if (response.optJSONObject(0) != null) {
                            Glide.with(activity).load(response.optJSONObject(0).optString("static_map")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(tripImg);
//                    tripDate.setText(response.optJSONObject(0).optString("assigned_at"));
                            paymentType.setText(response.optJSONObject(0).optString("payment_mode"));
                            String form = response.optJSONObject(0).optString("schedule_at");
                            JSONObject providerObj = response.optJSONObject(0).optJSONObject("provider");

                            if (response.optJSONObject(0).optString("booking_id") != null &&
                                    !response.optJSONObject(0).optString("booking_id").equalsIgnoreCase("")) {
                                booking_id.setText(response.optJSONObject(0).optString("booking_id"));
                            }

                            if (providerObj != null) {
                                driver = new Driver();
                                driver.setFname(providerObj.optString("first_name"));
                                driver.setLname(providerObj.optString("last_name"));
                                driver.setMobile(providerObj.optString("mobile"));
                                driver.setEmail(providerObj.optString("email"));
                                driver.setImg(providerObj.optString("avatar"));
                                driver.setRating(providerObj.optString("rating"));
                            }

                            try {
                                JSONArray userdrop = response.optJSONObject(0).getJSONArray("userdrop");
                                if (userdrop != null) {
                                    tripArrayList.clear();
                                    for (int i = 0; i < userdrop.length(); i++) {
                                        TripStatus flows = new TripStatus();
                                        flows.setdeliveryAddress(userdrop.getJSONObject(i).optString("d_address"));
                                        flows.setcomments(userdrop.getJSONObject(i).optString("service_items"));
                                        flows.setstatus(userdrop.getJSONObject(i).optString("status"));
                                        flows.setD_lat(userdrop.getJSONObject(i).optString("d_latitude"));
                                        flows.setD_long(userdrop.getJSONObject(i).optString("d_longitude"));
                                        tripArrayList.add(flows);
                                    }
                                    tripAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + getYear(form) + "\n" + getTime(form));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (response.optJSONObject(0).optString("payment_mode").equalsIgnoreCase("CASH")) {
                                paymentTypeImg.setImageResource(R.drawable.money_icon);
                            } else {
                                paymentTypeImg.setImageResource(R.drawable.visa);
                            }

                            if (response.optJSONObject(0).optJSONObject("provider").optString("avatar") != null)
                                Glide.with(activity).load(URLHelper.base + "storage/" + response.optJSONObject(0).optJSONObject("provider").optString("avatar"))
                                        .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).dontAnimate().into(tripProviderImg);

                            tripProviderRating.setRating(Float.parseFloat(response.optJSONObject(0).optJSONObject("provider").optString("rating")));
                            tripProviderName.setText(response.optJSONObject(0).optJSONObject("provider").optString("first_name") + " " + response.optJSONObject(0).optJSONObject("provider").optString("last_name"));

                            if (response.optJSONObject(0).optString("s_address") == null || response.optJSONObject(0).optString("d_address") == null || response.optJSONObject(0).optString("d_address").equals("") || response.optJSONObject(0).optString("s_address").equals("")) {
//                            viewLayout.setVisibility(View.GONE);
                            } else {
                                tripSource.setText(response.optJSONObject(0).optString("s_address"));
                            }

                            try {
                                JSONObject serviceObj = response.optJSONObject(0).optJSONObject("service_type");
                                if (serviceObj != null) {
//                            holder.car_name.setText(serviceObj.optString("name"));
                                    if (tag.equalsIgnoreCase("past_trips")) {
                                        tripAmount.setText(SharedHelper.getKey(context, "currency") + serviceObj.optString("price"));
                                    } else {
                                        tripAmount.setVisibility(View.GONE);
                                    }
                                    Glide.with(activity).load(serviceObj.optString("image"))
                                            .placeholder(R.drawable.loading).error(R.drawable.loading)
                                            .dontAnimate().into(tripProviderImg);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if ((customDialog != null) && (customDialog.isShowing()))
                            customDialog.dismiss();
                        parentLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    String json = null;
                    String Message;
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
                                refreshAccessToken("UPCOMING_TRIPS");
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
                        displayMessage(getString(R.string.please_try_again));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        Dot2dotzApplicaton.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    private void refreshAccessToken(final String tag) {


        JSONObject object = new JSONObject();
        try {
            object.put("grant_type", "refresh_token");
            object.put("client_id", URLHelper.client_id);
            object.put("client_secret", URLHelper.client_secret);
            object.put("refresh_token", SharedHelper.getKey(context, "refresh_token"));
            object.put("scope", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("URL: ", URLHelper.login);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.login, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.v("SignUpResponse", response.toString());
                    SharedHelper.putKey(context, "access_token", response.optString("access_token"));
                    SharedHelper.putKey(context, "refresh_token", response.optString("refresh_token"));
                    SharedHelper.putKey(context, "token_type", response.optString("token_type"));
                    if (tag.equalsIgnoreCase("PAST_TRIPS")) {
                        getRequestDetails();
                    } else if (tag.equalsIgnoreCase("UPCOMING_TRIPS")) {
                        getUpcomingDetails();
                    } else if (tag.equalsIgnoreCase("CANCEL_REQUEST")) {
                        cancelRequest();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String json = null;
                    String Message;
                    NetworkResponse response = error.networkResponse;

                    if (response != null && response.data != null) {
                        SharedHelper.putKey(context, "loggedIn", getString(R.string.False));
                        GoToBeginActivity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                return headers;
            }
        };

        Dot2dotzApplicaton.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void displayMessage(String toastString) {
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(activity, SignIn.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        activity.finish();
    }

    @Override
    public void onBackPressed() {
        if (lnrInvoice.getVisibility() == View.VISIBLE) {
            lnrInvoice.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public void cancelRequest() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("request_id", jsonObject.optString("id"));
            object.put("cancel_reason", reason);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("URL: ", URLHelper.CANCEL_REQUEST_API);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CANCEL_REQUEST_API, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.v("CancelRequestResponse", response.toString());
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    String json = null;
                    String Message;
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
                                refreshAccessToken("CANCEL_REQUEST");
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
                        displayMessage(getString(R.string.please_try_again));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        Dot2dotzApplicaton.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        return monthName;
    }

    private String getDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String dateName = new SimpleDateFormat("dd").format(cal.getTime());
        return dateName;
    }

    private String getYear(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String yearName = new SimpleDateFormat("yyyy").format(cal.getTime());
        return yearName;
    }

    private String getTime(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String timeName = new SimpleDateFormat("hh:mm a").format(cal.getTime());
        return timeName;
    }

    /*  Method which will take screenshot on Basis of Screenshot Type ENUM  */
    private void takeScreenshot(ScreenshotType screenshotType) {
        Bitmap b = null;
        switch (screenshotType) {
            case FULL:
                //If Screenshot type is FULL take full page screenshot i.e our root content.
                b = ScreenshotUtils.getScreenShot(sv_parentInvoice);
                break;
            case CUSTOM:
                //If Screenshot type is CUSTOM

                rel_header.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of first button
                btnViewInvoice.setVisibility(View.INVISIBLE);//set the visibility to VISIBLE of hidden text

                b = ScreenshotUtils.getScreenShot(sv_parentInvoice);

                //After taking screenshot reset the button and view again
                rel_header.setVisibility(View.VISIBLE);//set the visibility to VISIBLE of first button again
                btnViewInvoice.setVisibility(View.VISIBLE);//set the visibility to INVISIBLE of hidden text

                //NOTE:  You need to use visibility INVISIBLE instead of GONE to remove the view from frame else it wont consider the view in frame and you will not get screenshot as you required.
                break;
        }

        //If bitmap is not null
        if (b != null) {
            //showScreenShotImage(b);//show bitmap over imageview

            File saveFile = ScreenshotUtils.getMainDirectoryName(this);//get the path to save screenshot
            File file = ScreenshotUtils.store(b, "screenshot" + screenshotType + ".jpg", saveFile);//save the screenshot to selected path
            shareScreenshot(file);//finally share screenshot
        } else
            //If bitmap is null show toast message
            Toast.makeText(this, R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show();
    }

    /*   *//*  Show screenshot Bitmap *//*
    private void showScreenShotImage(Bitmap b) {
        imageView.setImageBitmap(b);
    }*/

    /*  Share Screenshot  */
    private void shareScreenshot(File file) {
        Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.sharing_text));
        intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

}