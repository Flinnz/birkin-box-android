package ru.birkin.team.android;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    public boolean deleteMode = false;
    private final List<ClothesWithLaundryRules> clothesList;
    private BiConsumer<Integer, ClothesAdapter> onAdd;

    public ClothesAdapter(LayoutInflater inflater,
                          List<ClothesWithLaundryRules> clothes) {
        this.inflater = inflater;
        this.clothesList = clothes;
        this.onAdd = null;
    }

    public ClothesAdapter(LayoutInflater inflater,
                          List<ClothesWithLaundryRules> clothes,
                          BiConsumer<Integer, ClothesAdapter> onAdd) {
        this(inflater, clothes);
        this.onAdd = onAdd;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.clothes_item, parent, false);
        return new ViewHolder(view, onAdd);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClothesWithLaundryRules clothes = clothesList.get(position);
        if (clothes.clothes != null) {
            if (clothes.clothes.clothesPhoto != null) {
                if (clothes.clothes.clothesPhoto.bitmap != null) {
                    holder.photoView.setImageBitmap(clothes.clothes.clothesPhoto.bitmap);
                }
            }
        }
        holder.nameView.setText(clothes.clothes.name);
        holder.descriptionView.setText(clothes.clothes.description);
        holder.clothesPosition = position;
        holder.clothesAdapter = this;
        holder.deleteMode = deleteMode;
        if (deleteMode) {
            holder.button.setVisibility(View.VISIBLE);
        } else {
            holder.button.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return clothesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView photoView;
        final TextView nameView;
        final TextView descriptionView;
        final Button button;
        final View.OnClickListener viewClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClothesWithLaundryRules clothesWithLaundryRules = clothesAdapter.clothesList.get(clothesPosition);
                Context context = v.getContext();
                Intent intent = new Intent(context, GarmentActivity.class);
                int[] drawables = new int[clothesWithLaundryRules.laundryRules.size()];
                String[] descs = new String[clothesWithLaundryRules.laundryRules.size()];
                intent.putExtra("drawables", drawables);
                intent.putExtra("descs", descs);
                intent.putExtra("clothesId", clothesWithLaundryRules.clothes.clothesId);
                intent.putExtra("labelPhoto", clothesWithLaundryRules.clothes.labelPhoto.path);
                intent.putExtra("clothesPhoto", clothesWithLaundryRules.clothes.clothesPhoto.path);

                context.startActivity(intent);
            }
        };
        final View.OnClickListener addViewClick = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                onAdd.accept(clothesPosition, clothesAdapter);
            }
        };

        final View.OnClickListener deleteClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClothesWithLaundryRules clothesWithLaundryRules = clothesAdapter.clothesList.get(clothesPosition);
                BirkinBoxApplication
                        .getInstance()
                        .getDatabase()
                        .clothesDao()
                        .remove(clothesWithLaundryRules.clothes)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe();

                BirkinBoxApplication
                        .getInstance()
                        .getDatabase()
                        .clothesWithLaundryRulesDao()
                        .removeClothesReferences(clothesWithLaundryRules.clothes.clothesId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe();
                File file = new File(clothesWithLaundryRules.clothes.clothesPhoto.path);
                file.delete();
            }
        };

        private int clothesPosition;
        private ClothesAdapter clothesAdapter;
        private boolean deleteMode;
        private BiConsumer<Integer, ClothesAdapter> onAdd = null;
        public ViewHolder(@NonNull View itemView) {
            this(itemView, null);
        }
        public ViewHolder(@NonNull View itemView, BiConsumer<Integer, ClothesAdapter> onAdd) {
            super(itemView);
            if (onAdd == null) {
                itemView.setOnClickListener(viewClick);
            }
            this.photoView = (ImageView)itemView.findViewById(R.id.clothes_photo_preview);
            this.nameView = (TextView) itemView.findViewById(R.id.clothes_name);
            this.descriptionView = (TextView)itemView.findViewById(R.id.clothes_description);
            button = (Button) itemView.findViewById(R.id.delete_item);
            button.setOnClickListener(deleteClick);
            this.onAdd = onAdd;
            if (onAdd != null) {
                itemView.setOnClickListener(addViewClick);
            }
        }
    }
}
