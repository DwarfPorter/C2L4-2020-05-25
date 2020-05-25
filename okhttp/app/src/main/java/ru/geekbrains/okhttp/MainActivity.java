package ru.geekbrains.okhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.Locale;

import ru.geekbrains.okhttp.model.Weather;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView temperature = findViewById(R.id.textTemp);

        OkHttpRequester requester = new OkHttpRequester(new OkHttpRequester.OnResponseCompleted() {
            @Override
            public void onCompleted(Weather content) {
                temperature.setText(String.format(Locale.getDefault(),"%d", content.getTemperature()));
            }
        });

        requester.run("https://api.openweathermap.org/data/2.5/weather?q=moscow&units=metric&appid=240af58b6f095eb759a3ecd2d282d448");
    }
}
