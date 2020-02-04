package com.example.locator;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.ViewHolder> {


    private List<Quest> questList;
    private Dialog dialog;
    private Context context;
    private int questType;


    public QuestAdapter(Quest quest, Context context, int type) {
        questList = new ArrayList<>();
        questList.add(quest);
        questType = type;
        this.context = context;
    }

    public QuestAdapter(List<Quest> quest, Context context, int type) {
        questList = quest;
        questType = type;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quest, parent, false);

        return new ViewHolder(view);

    }

    public void addQuest(Quest quest) {
        questList.add(quest);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Quest quest = questList.get(position);

        holder.txtName.setText(quest.getName());
        String status = quest.getItemsFound() + "/" + quest.getItems().size() + " question answered";
        holder.txtStatus.setText(status);

    }


    @Override
    public int getItemCount() {
        if (questList == null)
            return 0;
        return questList.size();
    }

    public void removeQuest(Quest quest) {
        for (int i = 0; i < questList.size(); i++) {
            if (questList.get(i).getId().equals(quest.getId())) {
                questList.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtName, txtStatus;
        public ImageView img1, img2, img3;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtName = itemView.findViewById(R.id.title);
            txtStatus = itemView.findViewById(R.id.count);
            img1 = itemView.findViewById(R.id.thumbnail);
            img2 = itemView.findViewById(R.id.thumbnail2);
            img3 = itemView.findViewById(R.id.thumbnail3);

        }

        public void openTakeDialog(Quest quest) {
            dialog = new LovelyStandardDialog(context)
                .setCancelable(false)
                .setTitle("Take quest")
                .setMessage("Do you want to take the quest?")
                .setPositiveButton("Take", v -> {
                    LocatorData.getInstance().takeQuest(quest);
                    LocatorData.getInstance().feedQuests.remove(quest);
                    questList.remove(quest);
                    notifyDataSetChanged();
                    Intent intent = new Intent(context, ActivityQuestProgress.class);
                    intent.putExtra("quest", quest);
                    context.startActivity(intent);
                })
                .setNegativeButton("Cancel", v -> {
                    dialog.dismiss();
                }).show();

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Quest q = questList.get(position);
            switch (questType) {
                case Constants.QuestType.FEED:
                    openTakeDialog(q);
                    break;
                case Constants.QuestType.FINISHED:
                case Constants.QuestType.ACTIVE:
                    Intent intent = new Intent(context, ActivityQuestProgress.class);
                    intent.putExtra("quest", q);
                    context.startActivity(intent);
                    break;
                case Constants.QuestType.ADDED:
                    break;
            }
        }
    }
}

