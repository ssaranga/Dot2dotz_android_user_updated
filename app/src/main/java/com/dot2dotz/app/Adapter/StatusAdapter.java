package com.dot2dotz.app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.dot2dotz.app.Models.TripStatus;
import com.dot2dotz.app.R;
import com.dot2dotz.app.Utils.MyButton;
import com.dot2dotz.app.Utils.MyTextView;

import java.util.ArrayList;


public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private static final String TAG = "StatusAdapter";
    public StatuAdapterListner statusadapterListener;
    boolean[] selectedService;
    private ArrayList<TripStatus> listModels;
    private Context context;


    public StatusAdapter(ArrayList<TripStatus> listModel, Context context) {
        this.listModels = listModel;
        this.context = context;
    }

    public void setStatusadapterListener(StatuAdapterListner statusadapterListener) {
        this.statusadapterListener = statusadapterListener;
    }

    public void setListModels(ArrayList<TripStatus> listModels) {
        this.listModels = listModels;
    }

    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_status, parent, false);
        return new StatusAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TripStatus status = listModels.get(position);

        if (status.getstatus() != null) {
            if (status.getstatus().equalsIgnoreCase("SEARCHING")) {
                holder.status.setText(R.string.yet_pickup);
                holder.status_img.setImageResource(R.drawable.radio_btn_grey);
                holder.track_btn.setEnabled(false);

            } else if (status.getstatus().equalsIgnoreCase("STARTED") || status.getstatus().equalsIgnoreCase("DROPPED")) {
                holder.status.setText(R.string.pickup);
                holder.status_img.setImageResource(R.drawable.radio_btn_green);
                holder.track_btn.setEnabled(true);

            } else if (status.getstatus().equalsIgnoreCase("COMPLETED")) {
                holder.status.setText(R.string.order_delivered);
                holder.status_img.setImageResource(R.drawable.radio_btn_red);
                holder.track_btn.setEnabled(false);
            } else {
                holder.status.setText("");
            }
        }
        if (status.getdeliveryAddress() != null) {
            holder.destination_address.setText(status.getdeliveryAddress());
        } else {
            holder.destination_address.setText("");
        }
        if (status.getcomments() != null) {

            holder.comments.setText(status.getcomments());
        } else {
            holder.comments.setText("");
        }
        if(status.getOtp() != null && !status.getOtp().isEmpty()) {
            holder.otpText.setText(status.getOtp());
        } else {
            holder.otpText.setText("");
        }

        holder.destination_address.setTag(status);
        holder.status.setTag(status);
        holder.comments.setTag(status);
        holder.track_btn.setTag(status);
        holder.otpText.setTag(status);
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }


    public interface StatuAdapterListner {

        void onTrackbtn(TripStatus statusflow);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MyTextView order, destination_address, comments, status,otpText;
        MyButton track_btn;
        ImageView status_img;
        private ArrayList<TripStatus> listModels;


        public ViewHolder(View itemView) {
            super(itemView);
//            order = itemView.findViewById(R.id.order);
            destination_address = itemView.findViewById(R.id.destination_address);
            comments = itemView.findViewById(R.id.comments);
            status = itemView.findViewById(R.id.status);
            track_btn = itemView.findViewById(R.id.track_btn);
            status_img = itemView.findViewById(R.id.status_img);
            otpText = itemView.findViewById(R.id.otpText);

            track_btn.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            TripStatus status = (TripStatus) view.getTag();
            if (statusadapterListener != null) {
                if (view == track_btn) {
                    statusadapterListener.onTrackbtn(status);
                }
            }

        }

    }

}


