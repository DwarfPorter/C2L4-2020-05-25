package ru.geekbrains.okhttp;

import android.os.Handler;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.geekbrains.okhttp.data.WeatherRequest;
import ru.geekbrains.okhttp.model.Weather;

public class OkHttpRequester {

    private final OnResponseCompleted listener;

    public OkHttpRequester(OnResponseCompleted listener){
        this.listener = listener;
    }

    public void run(String url){
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            // Этот хендлер нужен для синхронизации потоков: если его
            // сконструировать, он запомнит поток и в дальнейшем будет с ним
            // синхронизироваться
            final Handler handler = new Handler();
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String answer = response.body().string();
                Gson gson = new Gson();
                WeatherRequest weatherRequest = gson.fromJson(answer, WeatherRequest.class);
                final Weather weather = new Weather();
                weather.setTemperature((int) weatherRequest.getMain().getTemp());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onCompleted(weather);
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    // Интерфейс обратного вызова; метод onCompleted вызывается по окончании
    // загрузки страницы
    public interface OnResponseCompleted {
        void onCompleted(Weather content);
    }
}
