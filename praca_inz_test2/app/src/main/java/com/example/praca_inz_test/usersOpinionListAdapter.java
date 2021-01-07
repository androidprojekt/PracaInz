package com.example.praca_inz_test;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class usersOpinionListAdapter extends ArrayAdapter<userOpinion> {
    private Context mContext;
    int mResource;
    private static final String TAG = "BeaconListAdapter";

    public usersOpinionListAdapter(Context context, int resource, List<userOpinion> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String nick= getItem(position).getNickname();
        String opinion = getItem(position).getOpinion();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvOpinions = convertView.findViewById(R.id.opinionDatabaseId);
        TextView tvNick = convertView.findViewById(R.id.nickDatabaseId);

        tvNick.setText(nick);
        tvOpinions.setText(opinion);

        return convertView;
    }




}


