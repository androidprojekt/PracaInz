package com.example.praca_inz_test;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private Context mContext;
    private List<Exhibit> mData;

    public RecyclerViewAdapter(Context mContext, List<Exhibit> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_exhibit,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tvExhibitName.setText(mData.get(position).getExhibitName());
        holder.imgExhibitThumbnail.setImageResource(mData.get(position).getThumbnail());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ExhibitActivity.class);
                intent.putExtra("name",mData.get(position).getExhibitName());
                intent.putExtra("description",mData.get(position).getDescription());
                intent.putExtra("thumbnail",mData.get(position).getThumbnail());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvExhibitName;
        ImageView imgExhibitThumbnail;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);

            tvExhibitName= itemView.findViewById(R.id.exhibit_name_id);
            imgExhibitThumbnail =  itemView.findViewById(R.id.exhibit_image_id);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }

}
