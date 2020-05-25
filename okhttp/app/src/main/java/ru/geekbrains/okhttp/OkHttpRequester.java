package ru.geekbrains.okhttp;

import android.os.Handler;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onCompleted(answer);
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
        void onCompleted(String content);
    }
}
