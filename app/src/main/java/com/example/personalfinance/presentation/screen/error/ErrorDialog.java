package com.example.personalfinance.presentation.screen.error;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.personalfinance.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ErrorDialog extends DialogFragment {

    private static final String ARG_MESSAGE = "error_message";
    private static final String ARG_CODE = "error_code";

    public static ErrorDialog newInstance(String message, String code) {
        ErrorDialog dialog = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_CODE, code);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_error, null);

        TextView tvMessage = view.findViewById(R.id.tvErrorMessage);
        TextView tvCode = view.findViewById(R.id.tvErrorCode);
        Button btnOk = view.findViewById(R.id.btnOk);

        Bundle args = getArguments();
        if (args != null) {
            tvMessage.setText(args.getString(ARG_MESSAGE, "Произошла ошибка"));
            tvCode.setText(args.getString(ARG_CODE, "Код: неизвестно"));
        }

        btnOk.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }
}