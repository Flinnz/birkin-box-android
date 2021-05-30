package ru.birkin.team.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LaundryRuleAdapter extends RecyclerView.Adapter<LaundryRuleAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<LaundryRule> ruleList;

    public LaundryRuleAdapter(LayoutInflater inflater, List<LaundryRule> ruleList) {
        this.inflater = inflater;
        this.ruleList = ruleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.laundry_rule, parent, false);
        return new LaundryRuleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LaundryRule laundryRule = ruleList.get(position);
        holder.photoView.setImageResource(laundryRule.drawableId);
        holder.nameView.setText(laundryRule.displayName);
    }

    @Override
    public int getItemCount() {
        return ruleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView photoView;
        final TextView nameView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.photoView = (ImageView)itemView.findViewById(R.id.rule_icon);
            this.nameView = (TextView) itemView.findViewById(R.id.rule_name);
        }
    }
}
