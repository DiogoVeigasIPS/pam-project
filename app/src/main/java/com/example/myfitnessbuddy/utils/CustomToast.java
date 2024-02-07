package com.example.myfitnessbuddy.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.myfitnessbuddy.R;

public class CustomToast {

    private static final int errorIcon = R.drawable.ic_error;
    private static final int successIcon = R.drawable.ic_success;

    private static View createToastView(int layoutId, int messageId, int iconId, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, null);

        TextView textView = view.findViewById(R.id.message);
        ImageView iconImageView = view.findViewById(R.id.icon);
        CardView toastCard = view.findViewById(R.id.custom_toast_container);

        textView.setText(context.getString(messageId));
        iconImageView.setImageResource(iconId);

        if (iconId == R.drawable.ic_error) {
            toastCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.danger));
        } else {
            toastCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.success));
        }

        return view;
    }

    private static void showToast(int layoutId, int messageId, int iconId, Context context, int duration) {
        View view = createToastView(layoutId, messageId, iconId, context);

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(view);
        toast.show();
    }

    public static void showSuccessToast(Context context, int messageResId, int duration) {
        showToast(R.layout.custom_toast, messageResId, successIcon, context, duration);
    }

    public static void showErrorToast(Context context, int messageResId, int duration) {
        showToast(R.layout.custom_toast, messageResId, errorIcon, context, duration);
    }
}

