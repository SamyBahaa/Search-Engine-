package com.example.searchengine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private ArrayList<ResultData> mResultData;
    private Context context;


    public ResultAdapter(ArrayList<ResultData> mResultData, Context context) {
        this.mResultData = mResultData;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_result_list,parent,false);
        return new ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        ResultData currentItem = mResultData.get(position);

        holder.urlTextView.setText(currentItem.Id);
        holder.nameTextView.setText(currentItem.Name);
    }

    @Override
    public int getItemCount() {
        return mResultData.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView urlTextView;
        public TextView nameTextView;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.result_Iv);
            urlTextView = itemView.findViewById(R.id.url_tv);
            nameTextView = itemView.findViewById(R.id.name_tv);


        }
    }
}
