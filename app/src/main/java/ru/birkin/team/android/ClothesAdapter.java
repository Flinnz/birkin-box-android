package ru.birkin.team.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<ClothesWithLaundryRules> clothesList;

    public ClothesAdapter(LayoutInflater inflater, List<ClothesWithLaundryRules> clothes) {
        this.inflater = inflater;
        this.clothesList = clothes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.clothes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClothesWithLaundryRules clothes = clothesList.get(position);
        holder.photoView.setImageBitmap(clothes.clothes.clothesPhoto.bitmap);
        holder.nameView.setText(clothes.clothes.name);
        holder.descriptionView.setText(clothes.clothes.description);
    }

    @Override
    public int getItemCount() {
        return clothesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView photoView;
        final TextView nameView;
        final TextView descriptionView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.photoView = (ImageView)itemView.findViewById(R.id.clothes_photo_preview);
            this.nameView = (TextView) itemView.findViewById(R.id.clothes_name);
            this.descriptionView = (TextView)itemView.findViewById(R.id.clothes_description);
        }
    }
}
