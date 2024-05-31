package com.example.dkt_group_beta.dialogues;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dkt_group_beta.R;

public class CheatDialogFragment extends DialogFragment {

    public interface OnInputListener {
        void sendCheatValue(int input);
    }

    public OnInputListener inputListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cheat_dialog, container, false);

        EditText etNumberInput = view.findViewById(R.id.etNumberInput);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            String input = etNumberInput.getText().toString();
            if (!input.isEmpty()) {
                inputListener.sendCheatValue(Integer.parseInt(input));
                dismiss();
            } else {
                Toast.makeText(getContext(), "Please enter a number", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
