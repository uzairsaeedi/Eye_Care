package com.eyetest.eyecare.eyesighttest;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CustomDialogUtil {

    public static void showRemoveAdsDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_remove_ads, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnBuyNow = view.findViewById(R.id.btnBuyNow);
        TextView btnCancel = view.findViewById(R.id.btnCancel);

        btnBuyNow.setOnClickListener(v -> {
            Toast.makeText(context, "Buy Now clicked", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(
                (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }


//    public static void showRateUsDialog(Context context) {
//        Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_rate_us, null);
//        dialog.setContentView(view);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
//        Button btnRateNow = view.findViewById(R.id.btnRateNow);
//        TextView btnRemindLater = view.findViewById(R.id.btnRemindLater);
//
//        btnRateNow.setOnClickListener(v -> {
//            float rating = ratingBar.getRating(); // yeh rating user ki choice hai
//            Toast.makeText(context, "You rated: " + rating + " stars", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        });
//
//        btnRemindLater.setOnClickListener(v -> dialog.dismiss());
//
//        dialog.show();
//        dialog.getWindow().setLayout(
//                (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9),
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//    }

}
