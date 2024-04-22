package com.example.dkt_group_beta.activities;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dkt_group_beta.R;

import java.util.ArrayList;
import java.util.List;

public class PopUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pop_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_pop_up);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*0.8), (int) (height*0.6));
    }
    private void loadImages() {
        List<ImageView> imageViews = new ArrayList<>();
        LinearLayout container = findViewById(R.id.imageContainer);

        //for (String imageName : IMAGE_NAMES) {
          //  int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            //Bitmap bitmap = decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200);

            //ImageView imageView = new ImageView(this);
            //imageView.setLayoutParams(new LinearLayout.LayoutParams(
              //      LinearLayout.LayoutParams.MATCH_PARENT,
                //    LinearLayout.LayoutParams.WRAP_CONTENT
            //));
            //imageView.setImageBitmap(bitmap);
            //container.addView(imageView);
        }
    }
}