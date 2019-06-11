package com.example.locator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.ViewHolder> {


    private List<Quest> questList;
    private Context context;


    public QuestAdapter(List<Quest>questL, Context context)
    {
        questList =questL;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quest,parent,false);

        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Quest quest= questList.get(position);

        holder.txtName.setText(quest.getName());
        String status=quest.getItemsFound()+"/"+quest.getItems().size()+" items found";
        holder.txtStatus.setText(status);
        ArrayList<Item> items=quest.getItems();
        if(items.get(0).getCapturedImage()!=null)
            holder.img1.setImageBitmap(items.get(0).getCapturedImage());
        if(items.get(1).getCapturedImage()!=null)
            holder.img2.setImageBitmap(items.get(1).getCapturedImage());
        if(items.get(2).getCapturedImage()!=null)
            holder.img2.setImageBitmap(items.get(2).getCapturedImage());

    }


    @Override
    public int getItemCount() {
        return questList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtName,txtStatus;
        public ImageView img1, img2, img3;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtName=(TextView)itemView.findViewById(R.id.title);
            txtStatus=(TextView)itemView.findViewById(R.id.count);
            img1= itemView.findViewById(R.id.thumbnail);
            img2= itemView.findViewById(R.id.thumbnail2);
            img3= itemView.findViewById(R.id.thumbnail3);


        }

        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();
            Quest q= questList.get(position);

            Intent i=new Intent(context, Quest.class);
            i.putExtra("Quest",q);
            context.startActivity(i);


        }
    }
}
