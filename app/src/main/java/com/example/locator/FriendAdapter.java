package com.example.locator;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private List<FriendItem> list = new ArrayList<>();
    private Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendItem item = list.get(position);
        holder.name.setText(item.user.getName());
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String countryName = "", stateName = "", cityName = "";
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(item.user.getLatitude(), item.user.getLongitude(), 1);
            cityName = addresses.get(0).getAddressLine(0);
            stateName = addresses.get(0).getAddressLine(1);
            countryName = addresses.get(0).getAddressLine(2);
        } catch (IOException e) {
            e.printStackTrace();
        }


        holder.location.setText(cityName);
        holder.points.setText(String.valueOf(item.user.getPoints()));
        if (item.user.getProfilePicture() != null && !item.user.getProfilePicture().isEmpty())
            holder.image.setImageBitmap(Tools.StringToBitMap(item.user.getProfilePicture()));
        else
            holder.image.setImageDrawable(context.getDrawable(R.drawable.ic_place_holder));

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public FriendAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<User> list) {
        this.list.clear();
        for (User user : list) {
            this.list.add(new FriendItem(user));
        }
        notifyDataSetChanged();
    }

    public void addData(User user) {
        this.list.add(0, new FriendItem(user));
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, location, points;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            points = itemView.findViewById(R.id.ok);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            image = itemView.findViewById(R.id.image);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("friend_id", list.get(position).user.getId());
            context.startActivity(intent);
        }
    }
}
