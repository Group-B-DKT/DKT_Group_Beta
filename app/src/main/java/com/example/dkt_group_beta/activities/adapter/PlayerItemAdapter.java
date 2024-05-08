package com.example.dkt_group_beta.activities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
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
    private LayoutInflater inflater;

    public PlayerItemAdapter(Context context, List<Player> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
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
        View view = inflater.inflate(R.layout.player_stat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerItemAdapter.ViewHolder holder, int position) {
        holder.playerName.setText(data.get(position).getUsername());
        holder.isOnTurn.setText(data.get(position).isOnTurn() ? "current playing" : "");
        holder.money.setText(""+data.get(position).getMoney());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
