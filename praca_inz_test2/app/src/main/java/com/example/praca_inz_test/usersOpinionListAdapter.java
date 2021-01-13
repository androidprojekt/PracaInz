package com.example.praca_inz_test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class usersOpinionListAdapter extends ArrayAdapter<userOpinion> {
    private Context mContext;
    int mResource;

    public usersOpinionListAdapter(Context context, int resource, List<userOpinion> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }


    @SuppressLint("ViewHolder")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        String nick= Objects.requireNonNull(getItem(position)).getNickname();
        String opinion = Objects.requireNonNull(getItem(position)).getOpinion();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvOpinions = convertView.findViewById(R.id.opinionDatabaseId);
        TextView tvNick = convertView.findViewById(R.id.nickDatabaseId);

        tvNick.setText(nick);
        tvOpinions.setText(opinion);

        return convertView;
    }




}


