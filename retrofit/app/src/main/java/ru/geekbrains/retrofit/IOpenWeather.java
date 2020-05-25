package ru.geekbrains.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.geekbrains.retrofit.data.WeatherRequest;

public interface IOpenWeather {
    @GET("weather")
    Call<WeatherRequest> loadWeather(@Query("q") String cityCountry, @Query("units") String metric, @Query("appid") String keyApi);
}
//https://api.openweathermap.org/data/2.5/weather?q=moscow&units=metric&appid=240af58b6f095eb759a3ecd2d282d448