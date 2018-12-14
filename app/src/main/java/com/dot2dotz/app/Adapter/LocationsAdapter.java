package com.dot2dotz.app.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dot2dotz.app.Activities.MainActivity;
import com.dot2dotz.app.Models.Locations;
import com.dot2dotz.app.R;
import com.dot2dotz.app.Utils.Utilities;

import java.util.ArrayList;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.ViewHolder> {

    private static final String TAG = "LocationsAdapter";
    boolean[] selectedService;
    android.app.AlertDialog confirmation_dialogue;
    private ArrayList<Locations> listModels;
    private Context context;
    private LocationsListener locationsListener;
    public int contact_position;
    String receiver_phone_number = "";

    Intent intent;

    public LocationsAdapter(ArrayList<Locations> listModel, Context context) {
        this.listModels = listModel;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_list_item, parent, false);
        return new ViewHolder(v);
    }

    public void setLocationsListener(LocationsListener locationsListener) {
        this.locationsListener = locationsListener;
    }

    public void setListModels(ArrayList<Locations> listModels) {
        this.listModels = listModels;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        Locations locations = listModels.get(position);

        if (position == 0) {
            holder.closeImg.setVisibility(View.GONE);
        } else {
            holder.closeImg.setVisibility(View.VISIBLE);
            holder.srcTxt.setClickable(false);
        }

        if (locations.getsAddress() != null && !locations.getsAddress().equalsIgnoreCase("null") &&
                locations.getsAddress().length() > 0) {
            holder.srcTxt.setText(locations.getsAddress());
        } else if (locations.getsLatitude() != null && locations.getsLongitude() != null) {
            Utilities.getAddressUsingLatLng("source", holder.srcTxt, context, locations.getsLatitude(), locations.getsLongitude());
        } else {
            holder.srcTxt.setText("");
        }

        if (locations.getdAddress() != null) {
            holder.destTxt.setText(locations.getdAddress());
        } else {
            holder.destTxt.setText("");
        }
        if (locations.getGoods() != null) {
            holder.goodTxt.setText(locations.getGoods());
        } else {
            holder.goodTxt.setText("");
        }
        if (locations.getUserName() != null) {
            holder.userName.setText(locations.getUserName());
        } else {
            holder.userName.setText("");
        }
        if (locations.getUserMobile() != null) {
            holder.userMobile.setText(locations.getUserMobile());
        }
        else if (receiver_phone_number != "" && position == Utilities.address_position)
        {
            Utilities.address_position = -1;
            holder.userMobile.setText(receiver_phone_number.replace(" ", ""));
            locations.setUserMobile(holder.userMobile.getText().toString());
        } /*else {
            holder.userMobile.setText("");
        }*/

        holder.destTxt.setTag(locations);
        holder.srcTxt.setTag(locations);
        holder.goodTxt.setTag(locations);
        holder.userName.setTag(locations);
        holder.userMobile.setTag(locations);
        holder.closeImg.setTag(locations);
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public void showGoodsDialog(final Locations locations, final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getResources().getString(R.string.app_name));
        alert.setIcon(R.mipmap.ic_launcher);

        View custom = LayoutInflater.from(context).inflate(R.layout.custom_edit, null);
        final EditText input = custom.findViewById(R.id.desc);
        if (position == 0) {
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            if (locations.getGoods() != null && !locations.getGoods().isEmpty()) {
                String goods = locations.getGoods();
                if (goods != null && !goods.equalsIgnoreCase("null") && goods.length() > 0) {
                    input.setText(goods);
                }
            } else {
                input.setHint(R.string.enter_your_goods);
            }
        } else if (position == 1) {
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            if (locations.getUserName() != null && !locations.getUserName().isEmpty()) {
                String userName = locations.getUserName();
                if (userName != null && !userName.equalsIgnoreCase("null") && userName.length() > 0) {
                    input.setText(userName);
                }
            } else {
                input.setHint(R.string.enter_receiver_name);
            }
        } else if (position == 2) {
            //input.setText(holder.usermobile);
            input.setInputType(InputType.TYPE_CLASS_PHONE);
            if (locations.getUserMobile() != null && !locations.getUserMobile().isEmpty()) {
                String userMobile = locations.getUserMobile();
                if (userMobile != null && !userMobile.equalsIgnoreCase("null") && userMobile.length() > 0) {

                    input.setText(userMobile);

                }
            } else {
                input.setHint(R.string.enter_receiver_mobile);
            }
        }
        alert.setView(custom);
        alert.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (position == 0) {
                    if (input.getText().length() > 0) {
                        locations.setGoods(input.getText().toString());
                        locationsListener.onGoodsClick(locations);
                    }
                } else if (position == 1) {
                    if (input.getText().length() > 0) {
                        locations.setUserName(input.getText().toString());
                        locationsListener.onUserNameClick(locations);
                    }
                } else if (position == 2) {
                    if (input.getText().length() > 0) {

                        if (input.getText().length() == 10) {
                            locations.setUserMobile(input.getText().toString());
                            locationsListener.onUserMobileClick(locations);
                        } else {
                            Toast.makeText(context, R.string.enter_receiver_mobile_validation, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dialog.cancel();

            }
        });
        alert.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();

        Button buttonbackground = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonbackground.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

        Button buttonbackground1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public interface LocationsListener {
        void onCloseClick(Locations locations);

        void onSrcClick(Locations locations);

        void onDestClick(Locations locations);

        void onGoodsClick(Locations locations);

        void onUserNameClick(Locations locations);

        void onUserMobileClick(Locations locations);

        void onPickContact(int position, TextView userMobile_pick);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView srcTxt, destTxt, goodTxt, userName, userMobile;
        ImageView closeImg, userMobile_pick;

        public ViewHolder(View itemView) {
            super(itemView);
            srcTxt = itemView.findViewById(R.id.src_txt);
            destTxt = itemView.findViewById(R.id.dest_txt);
            goodTxt = itemView.findViewById(R.id.good_txt);
            closeImg = itemView.findViewById(R.id.close_img);
            userMobile_pick = itemView.findViewById(R.id.userMobile_pick);
            userName = itemView.findViewById(R.id.userName);
            userMobile = itemView.findViewById(R.id.userMobile);

            srcTxt.setOnClickListener(this);
            destTxt.setOnClickListener(this);
            goodTxt.setOnClickListener(this);
            closeImg.setOnClickListener(this);
            userMobile_pick.setOnClickListener(this);
            userName.setOnClickListener(this);
            userMobile.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (locationsListener != null) {
                final Locations locations = (Locations) v.getTag();
                if (v == closeImg) {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    builder.setTitle(context.getResources().getString(R.string.app_name))
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage(context.getResources().getString(R.string.alert));
                    builder.setCancelable(false);
                    builder.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            locationsListener.onCloseClick(locations);
                        }
                    });
                    builder.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    confirmation_dialogue = builder.create();
                    confirmation_dialogue.show();


                } else if (v == srcTxt) {
                    locationsListener.onSrcClick(locations);
                } else if (v == destTxt) {
                    locationsListener.onDestClick(locations);
                } else if (v == goodTxt) {
                    showGoodsDialog(locations, 0);
                } else if (v == userName) {
                    showGoodsDialog(locations, 1);
                } else if (v == userMobile) {
                    showGoodsDialog(locations, 2);
                } else if (v == userMobile_pick) {

                    locationsListener.onPickContact(getLayoutPosition(), userMobile);
                    Utilities.address_position = getLayoutPosition();
                    if (!receiver_phone_number.equalsIgnoreCase("")) {
                        userMobile.setText(receiver_phone_number);
                        locations.setUserMobile(receiver_phone_number);
                    }
                }
            }
        }
    }

    public void updateAdapter(String phone_number) {

        // update adapter element like NAME, EMAIL e.t.c.userMobile_pick here
        receiver_phone_number = phone_number;
        // then in order to refresh the views notify the RecyclerView
        notifyDataSetChanged();
    }
}
