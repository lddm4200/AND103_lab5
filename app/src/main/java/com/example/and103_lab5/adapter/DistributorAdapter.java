package com.example.and103_lab5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.and103_lab5.databinding.ItemDistributorBinding;
import com.example.and103_lab5.model.CongTy;

import java.util.ArrayList;

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.ViewHolder> {
    private ArrayList<CongTy> list;
    private Context context;
    private DistributorClick distributorClick;

    public DistributorAdapter(ArrayList<CongTy> list, Context context, DistributorClick distributorClick) {
        this.list = list;
        this.context = context;
        this.distributorClick = distributorClick;
    }

    public interface DistributorClick {
        void delete(CongTy congTy);
        void edit(CongTy congTy);
    }

    @NonNull
    @Override
    public DistributorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDistributorBinding binding = ItemDistributorBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorAdapter.ViewHolder holder, int position) {
        CongTy congTy = list.get(position);
        holder.binding.tvName.setText(congTy.getName());
        holder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distributorClick.delete(congTy);
            }
        });

        holder.binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distributorClick.edit(congTy);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDistributorBinding binding;
        public ViewHolder(ItemDistributorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
