package com.eyecare;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class CustomDialogUtil {

    // Remove Ads Dialog
    public static void showRemoveAdsDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_remove_ads, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Find Buttons
        Button btnBuyNow = view.findViewById(R.id.btnBuyNow);
        TextView btnCancel = view.findViewById(R.id.btnCancel);

        // Buy Now Click
        btnBuyNow.setOnClickListener(v -> {
            Toast.makeText(context, "Buy Now clicked", Toast.LENGTH_SHORT).show();
            // Yahan tum purchase logic add kar sakte ho
            dialog.dismiss();
        });

        // Cancel Click
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(
                (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }


    // Rate Us Dialog
    public static void showRateUsDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_rate_us, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Find Views
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        Button btnRateNow = view.findViewById(R.id.btnRateNow);
        TextView btnRemindLater = view.findViewById(R.id.btnRemindLater);

        // Button Listeners
        btnRateNow.setOnClickListener(v -> {
            float rating = ratingBar.getRating(); // yeh rating user ki choice hai
            Toast.makeText(context, "You rated: " + rating + " stars", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnRemindLater.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(
                (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

}
