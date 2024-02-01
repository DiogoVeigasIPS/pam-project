package com.example.myfitnessbuddy.main_fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.panel.UpdateDetailsActivity;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.models.Day;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPanel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPanel extends Fragment {

    public FragmentPanel() {
        // Required empty public constructor
    }

    public static FragmentPanel newInstance() {
        FragmentPanel fragment = new FragmentPanel();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton calorieButton = getView().findViewById(R.id.mbt_calorie_add);
        ImageButton weightButton = getView().findViewById(R.id.mbt_weight_add);
        AppCompatButton personalDetailsButton = getView().findViewById(R.id.bt_personal_details);

        calorieButton.setOnClickListener(v -> {

        });

        weightButton.setOnClickListener(v -> {
            WeightDialogFragment weightDialogFragment = new WeightDialogFragment();
            weightDialogFragment.show(getChildFragmentManager(), "WeightDialogFragmentTag");
        });

        personalDetailsButton.setOnClickListener(v -> {
            Intent caloryIntent = new Intent(getActivity(), UpdateDetailsActivity.class);
            startActivity(caloryIntent);
        });

    }

    private static void updateUI(){

    }

    public static class WeightDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_weight, null);

            builder.setView(dialogView)
                    .setPositiveButton(R.string.update, null) // Set a null click listener initially
                    .setNegativeButton(R.string.cancel, (dialog, id) -> {
                        WeightDialogFragment.this.getDialog().cancel();
                    });

            AlertDialog dialog = builder.create();

            // Override the onShow method to customize the positive button's behavior
            dialog.setOnShowListener(dialogInterface -> {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(view -> {
                    // Your custom logic here
                    EditText weightInput = dialogView.findViewById(R.id.weight_input);
                    String weightStr = weightInput.getText().toString();

                    if(weightStr.equals("")){
                        Toast.makeText(getActivity(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            int weight = Integer.parseInt(weightStr);
                            DatabaseHelper.executeInBackground(() -> {
                                Day today = DatabaseHelper.DayHelper.getToday();
                                today.setWeight(weight);
                                DatabaseHelper.DayHelper.updateDay(today);

                                requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(getActivity(), R.string.weight_updated, Toast.LENGTH_SHORT).show();
                                    updateUI();
                                    dialog.dismiss();
                                });
                            });

                        } catch (NumberFormatException numberFormatException){
                            Log.e("WeightDialogFragment", "Error parsing weight", numberFormatException);
                            Toast.makeText(getActivity(), R.string.invalid_weight_format, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            });

            return dialog;
        }
    }
}