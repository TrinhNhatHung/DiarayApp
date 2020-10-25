package com.example.diaryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diaryapp.model.PostMessage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private ArrayList<PostMessage> mPostMessage;

    public DiaryAdapter(ArrayList<PostMessage> mPostMessage) {
        this.mPostMessage = mPostMessage;
        Collections.sort(mPostMessage);
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent,false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        holder.tvTitle.setText(mPostMessage.get(position).getTitle());
        holder.tvContent.setText(mPostMessage.get(position).getContent());
        holder.cvItem.setCardBackgroundColor(mPostMessage.get(position).getColor());
        long time = mPostMessage.get(position).getTimestamp();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        String AM_PM = calendar.get(Calendar.AM_PM) == 1 ? "PM" : "AM";
        holder.tvTime.setText(hour + ":" + minute + " " + AM_PM);
        Calendar now = Calendar.getInstance();
        long timeAgoMilis = now.getTimeInMillis() - calendar.getTimeInMillis();
        long dayAgo = timeAgoMilis / 86400000;
        if (dayAgo != 0){
            holder.tvDateNote.setText(dayAgo + " days ago");
        } else {
            long hourAgo =  timeAgoMilis / 3600000;
            holder.tvDateNote.setText(hourAgo + " hours ago");
        }

    }

    @Override
    public int getItemCount() {
        return mPostMessage.size();
    }

    public class DiaryViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView tvDateNote;
        private TextView tvTime;
        private TextView tvTitle;
        private TextView tvContent;
        private CardView cvItem;
        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.tvDateNote = view.findViewById(R.id.tv_date_note);
            this.tvTitle = view.findViewById(R.id.tv_title);
            this.tvContent = view.findViewById(R.id.tv_content);
            this.cvItem = view.findViewById(R.id.cv_item);
            this.tvTime = view.findViewById(R.id.tv_time);
        }
    }
}
