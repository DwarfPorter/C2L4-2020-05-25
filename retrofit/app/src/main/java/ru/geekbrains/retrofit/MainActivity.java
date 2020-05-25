package ru.geekbrains.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.geekbrains.retrofit.data.WeatherRequest;

public class MainActivity extends AppCompatActivity {

    private IOpenWeather openWeather;
    private TextView textTemp;
    private TextInputEditText editCity;
    private TextInputEditText editApiKey;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRetrofit();
        initGui();
        initPreferences();
        initEvents();
    }

    // Создаём обработку клика кнопки
    private void initEvents() {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();            // Сохраняем настройки
                requestRetrofit(editCity.getText().toString(), editApiKey.getText().toString());
            }
        });
    }

    private void requestRetrofit(String city, String keyApi) {
        openWeather.loadWeather(city, "metric", keyApi)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            float result = response.body().getMain().getTemp();
                            textTemp.setText(String.format(Locale.getDefault(), "%f.2", result));
                        }
                        if (!response.isSuccessful() && response.errorBody() != null){
                            try {
                                JSONObject jsonError = new JSONObject(response.errorBody().string());
                                String error = jsonError.getString("message");
                                textTemp.setText(error);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        textTemp.setText("Error");
                    }
                });
    }

    // Сохраняем настройки
    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("apiKey", editApiKey.getText().toString());
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savePreferences();
    }

    private void initPreferences() {
        sharedPref = getPreferences(MODE_PRIVATE);
        loadPreferences();                   // Загружаем настройки
    }

    // Загружаем настройки
    private void loadPreferences() {
        String loadedApiKey = sharedPref.getString("apiKey", "240af58b6f095eb759a3ecd2d282d448");
        editApiKey.setText(loadedApiKey);
    }

    private void initGui() {
        textTemp = findViewById(R.id.textTemp);
        editApiKey = findViewById(R.id.editApiKey);
        editCity = findViewById(R.id.editCity);
    }

    private void initRetrofit() {
        Retrofit retrofit = MyApplication.getRetrofitInstance();
        openWeather = retrofit.create(IOpenWeather.class);
    }
}
