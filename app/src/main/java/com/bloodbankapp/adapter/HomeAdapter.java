package com.bloodbankapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodbankapp.R;
import com.bloodbankapp.models.Doner;
import com.bloodbankapp.ui.UserDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    List<Doner> donorList;

    public HomeAdapter(List<Doner> donorList) {
        this.donorList = donorList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_donor_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final Doner doner = donorList.get(position);

        holder.bindData(doner);

        // Open Detail Screen here
        holder.ivSeeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.itemView.getContext();
                Intent detailIntent = new Intent(context, UserDetailActivity.class);
                Bundle donerDetailBundle = new Bundle();
                donerDetailBundle.putSerializable("donor", doner);
                detailIntent.putExtras(donerDetailBundle);
                context.startActivity(detailIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivSeeDetail;
        public CircleImageView civProfile;
        public TextView tvName;
        public TextView tvBloodGroup;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvBloodGroup = itemView.findViewById(R.id.tvBloodGroup);
            ivSeeDetail = itemView.findViewById(R.id.ivOpenDetail);
            civProfile = itemView.findViewById(R.id.civProfile);
        }

        public void bindData(Doner doner) {
            tvName.setText(doner.getName());
            tvBloodGroup.setText(doner.getBloodGroup());
            Picasso.with(itemView.getContext())
                    .load("https://picsum.photos/200")
                    .placeholder(R.drawable.ic_user)
                    .into(civProfile);

        }
    }

}
