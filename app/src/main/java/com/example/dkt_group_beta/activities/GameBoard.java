package com.example.dkt_group_beta.activities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dkt_group_beta.R;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends AppCompatActivity {
    private static final int NUMBER_OF_FIELDS = 32;
    private List<ImageView> imageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.imageViews = new ArrayList<>();
        runOnUiThread(() -> {
            for (int i = 1; i <= NUMBER_OF_FIELDS; i++) {
                int resourceId = this.getResources()
                        .getIdentifier("field" + i, "id", this.getPackageName());
                imageViews.add(findViewById(resourceId));

                resourceId = this.getResources()
                        .getIdentifier("field" + i, "drawable", this.getPackageName());


                ImageView imageView = imageViews.get(i-1);
                if (imageView != null && resourceId != 0) {
                        imageView.setImageBitmap(
                                decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
                }
            }
        });
        for (int i = 1; i <= NUMBER_OF_FIELDS; i++) {
            int resourceId = this.getResources().getIdentifier("field" + i, "id", this.getPackageName());
            ImageView imageView = findViewById(resourceId);
            if (imageView != null) {
                imageViews.add(imageView);
                String resourceName = getResources().getResourceEntryName(imageView.getId());
                // enable clicking on an image view and opening pop-up
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ImageView", "ImageView ID: " + imageView.getId());
                        showCard(v, resourceName);
                    }
                });
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public void my_button_onClick_working(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_pop_up, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void showCard(View view, String viewID) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_cards_layout, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        ImageView popupImageView = popupView.findViewById(R.id.popUpCards); // load specific image into pop-up that belongs to the field
        int imageResourceId = getResources().getIdentifier("card" + viewID, "drawable", getPackageName());
        popupImageView.setImageResource(imageResourceId);
    }
}