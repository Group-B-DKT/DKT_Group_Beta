package com.example.dkt_group_beta.activities.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.model.Player;

import java.util.List;

public class PlayerItemAdapter extends RecyclerView.Adapter<PlayerItemAdapter.ViewHolder> {
    private final List<Player> data;

    public PlayerItemAdapter(List<Player> data) {
        this.data = data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView playerName;
        TextView isOnTurn;
        TextView money;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.txt_playerName);
            isOnTurn = itemView.findViewById(R.id.txt_isOnTurn);
            money = itemView.findViewById(R.id.txt_money);
        }
    }

    @NonNull
    @Override
    public PlayerItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.player_stat_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerItemAdapter.ViewHolder holder, int position) {
        holder.playerName.setText(data.get(position).getUsername());
        holder.isOnTurn.setText(data.get(position).isOnTurn() ? "< current playing" : "");
        holder.money.setText(data.get(position).getMoney());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
