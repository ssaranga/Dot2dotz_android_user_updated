package com.dot2dotz.app.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dot2dotz.app.Adapter.LocationsAdapter;
import com.dot2dotz.app.Helper.SharedHelper;
import com.dot2dotz.app.Models.Locations;
import com.dot2dotz.app.Models.PlacePredictions;
import com.dot2dotz.app.R;
import com.dot2dotz.app.Utils.CommonUtils;
import com.dot2dotz.app.Utils.MySingleton;

import java.util.ArrayList;

public class LocationAndGoodsActivity extends AppCompatActivity implements LocationsAdapter.LocationsListener {

    private static final String TAG = "LocationAndGoodsActivit";

    RecyclerView recyclerView;
    Button submitBtn;
    FloatingActionButton addFab;
    LocationsAdapter locationsAdapter;
    ArrayList<Locations> locationsArrayList = new ArrayList<>();
    ImageView backArrow;

    private Context context = LocationAndGoodsActivity.this;
    private Activity activity = LocationAndGoodsActivity.this;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST = 18945;

    Locations locations;
    public boolean isPickup;
    String sAddress;
    public static final int RequestPermissionCode = 1;
    static final int PICK_CONTACT = 1;
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;

    String phone_number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_and_goods);

        CommonUtils.setLanguage(LocationAndGoodsActivity.this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            //getContacts();
        } else {
            requestLocationPermission();
        }
        findViewsById();
        locationsArrayList.add(getLocation());
        setupRecyclerView();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                boolean isvalid;
                for (int i = 0; i < locationsArrayList.size(); i++) {

                    if (locationsArrayList.get(i).getGoods() == null) {
                        isvalid = true;
                        Toast.makeText(context, context.getResources().getString(R.string.empty_goods), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (locationsArrayList.get(i).getdAddress() == null) {
                        isvalid = true;
                        Toast.makeText(context, context.getResources().getString(R.string.empty_dest), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (locationsArrayList.get(i).getUserName() == null) {
                        isvalid = true;
                        Toast.makeText(context, context.getResources().getString(R.string.empty_user_name), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (locationsArrayList.get(i).getUserMobile() == null) {
                        isvalid = true;
                        Toast.makeText(context, context.getResources().getString(R.string.empty_user_mobile), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (locationsArrayList.get(0) != null) {
                    int i = 1;
                    for (Locations locations : locationsArrayList) {
                        intent.putExtra("Location Address" + i + "", locations);
                        i++;
                    }
                    intent.putExtra("Location size", locationsArrayList.size());
                    Log.e(TAG, "onClick: ", locationsArrayList.get(0));
                    intent.putExtra("pick_lo" +
                            "cation", "no");
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySingleton.getHomeFragment() != null) {
                    MySingleton.getHomeFragment().setVisibility();
                }
                finish();
            }
        });

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationsAdapter.getItemCount() < 3) {
                    locationsArrayList.add(getLocation());
                    refreshAdapter();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.can_not_add_loc), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void refreshAdapter() {
        locationsAdapter.setListModels(locationsArrayList);
        locationsAdapter.notifyDataSetChanged();
    }

    private Locations getLocation() {
        Locations locations = new Locations();
        locations.setsLatitude(SharedHelper.getKey(context, "curr_lat"));
        locations.setsLongitude(SharedHelper.getKey(context, "curr_lng"));
        locations.setdAddress(sAddress);
        locations.setdLatitude(null);
        locations.setdLongitude(null);
        return locations;
    }


    private void setupRecyclerView() {
        locationsAdapter = new LocationsAdapter(locationsArrayList, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        locationsAdapter.setLocationsListener(this);
        recyclerView.setAdapter(locationsAdapter);
    }

    private void findViewsById() {
        recyclerView = findViewById(R.id.recyclerView);
        submitBtn = findViewById(R.id.submit_btn);
        addFab = findViewById(R.id.add_fab);
        backArrow = findViewById(R.id.backArrow);
    }

    @Override
    public void onCloseClick(Locations locations) {
        locationsArrayList.remove(locations);
        locationsAdapter.setListModels(locationsArrayList);
        locationsAdapter.notifyDataSetChanged();
    }

    public void goToSearch()
    {
        Intent intent = new Intent(this,CustomGooglePlacesSearch.class);
        if (isPickup)
            intent.putExtra("cursor", "source");
        else
            intent.putExtra("cursor", "destination");
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
    }

    @Override
    public void onSrcClick(Locations locations) {
        isPickup = true;
        this.locations = locations;
        goToSearch();
    }

    @Override
    public void onDestClick(Locations locations) {
        isPickup = false;
        this.locations = locations;
        goToSearch();
    }

    @Override
    public void onGoodsClick(Locations locations) {
        this.locations = locations;
        refreshAdapter();
    }

    @Override
    public void onUserNameClick(Locations locations) {
        this.locations = locations;
        refreshAdapter();
    }

    @Override
    public void onUserMobileClick(Locations locations) {
        this.locations = locations;
        refreshAdapter();
    }

    @Override
    public void onPickContact(int position, TextView userMobile_pick) {

        /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            getContacts();
        } else {
            requestLocationPermission();
        }*/
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);

        userMobile_pick.setText(phone_number);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data.getSerializableExtra("Location Address") != null) {
            PlacePredictions placePredictions = (PlacePredictions) data.getSerializableExtra("Location Address");
            Log.e(TAG, "onActivityResult: " + placePredictions.toString());
            if (placePredictions.strDestLatitude != null && placePredictions.strDestLongitude != null) {
                if (!placePredictions.strDestLongitude.equalsIgnoreCase(locations.getsLongitude()) &&
                        !placePredictions.strDestLatitude.equalsIgnoreCase(locations.getsLatitude())) {
                    if (isPickup) {
                        for (Locations loc : locationsArrayList) {
                            loc.setsLatitude(placePredictions.strDestLatitude);
                            loc.setsLongitude(placePredictions.strDestLongitude);
                            loc.setsAddress(placePredictions.strDestAddress);
                        }
                        SharedHelper.putKey(context, "curr_lat", placePredictions.strDestLatitude + "");
                        SharedHelper.putKey(context, "curr_lng", placePredictions.strDestLongitude + "");
                        sAddress = placePredictions.strDestAddress;
                        refreshAdapter();
                    } else {
                        locations.setdLatitude(placePredictions.strDestLatitude);
                        locations.setdLongitude(placePredictions.strDestLongitude);
                        locations.setdAddress(placePredictions.strDestAddress);
                        refreshAdapter();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.src_and_dest_same_loc), Toast.LENGTH_SHORT).show();
                    goToSearch();
                }
            }
        } else if (requestCode == PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    try {
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:" + cNumber);

                            phone_number = cNumber;
                            // txtphno.setText("Phone Number is: " + cNumber);
                            if (locationsAdapter == null) {
                                locationsAdapter = new LocationsAdapter(locationsArrayList, context);
                                locationsAdapter.updateAdapter(cNumber);
                            } else {
                                locationsAdapter.updateAdapter(cNumber);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

        }
    }

    protected void requestLocationPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    RequestPermissionCode);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case RequestPermissionCode: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //getContacts();
                } else {
                    // permission denied,Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(LocationAndGoodsActivity.this, "Permission is required", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    /*public void getContacts() {

        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);
                    }
                    phoneCursor.close();
                }
                output.append("\n");
            }
            *//*if (locationsAdapter == null) {
                locationsAdapter = new LocationsAdapter(locationsArrayList, context);
                locationsAdapter.updateAdapter(phoneNumber);
            } else {
                locationsAdapter.updateAdapter(phoneNumber);
            }*//*
            System.out.println(output.toString());
        }
    }
*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (MySingleton.getHomeFragment() != null) {
            MySingleton.getHomeFragment().setVisibility();
        }

    }
}