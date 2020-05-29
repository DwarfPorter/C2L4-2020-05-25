package ru.geekbrains.picasso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);
        Picasso.get()
                .load("https://c1.staticflickr.com/1/186/31520440226_175445c41a_b.jpg")
                .transform(new CircleTransformation())
                .rotate(90)
                .into(imageView);

        ImageView imageView2 = findViewById(R.id.imageView2);
        int id = getResources().getIdentifier("geekbrains", "drawable", getPackageName());
        Picasso.get()
                .load(id)
                .into(imageView2);

        ImageView imageView3 = findViewById(R.id.imageView3);
        Glide.with(this)
                .load("https://c1.staticflickr.com/1/186/31520440226_175445c41a_b.jpg")
                .into(imageView3);

        ImageView imageView4 = findViewById(R.id.imageView4);
        Glide.with(this)
                .load(id)
                .into(imageView4);
    }
}
