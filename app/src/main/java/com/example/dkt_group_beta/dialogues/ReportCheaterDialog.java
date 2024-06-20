package com.example.dkt_group_beta.dialogues;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.model.Player;

import java.util.List;

public class ReportCheaterDialog extends DialogFragment {

    private List<Player> players;
    private OnPlayerSelectedListener listener;
    private Player selectedPlayer;

    public interface OnPlayerSelectedListener {
        void onPlayerSelected(Player player);
        void onPlayerConfirmed(Player player);
    }


    public ReportCheaterDialog(List<Player> players, OnPlayerSelectedListener listener) {
        this.players = players;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_report_cheater_dialog, null);
        builder.setView(view);

        ListView listView = view.findViewById(R.id.player_list_view);
        Button confirmButton = view.findViewById(R.id.button_confirm);

        String[] playerNames = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playerNames[i] = players.get(i).getUsername();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_single_choice, playerNames);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            selectedPlayer = players.get(position);
            confirmButton.setEnabled(true);
        });

        confirmButton.setOnClickListener(v -> {
            if (listener != null && selectedPlayer != null) {
                listener.onPlayerSelected(selectedPlayer);
            }
            dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }
}
