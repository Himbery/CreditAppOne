package ru.zelenbiruz.zmonl.Service;

import ru.zelenbiruz.zmonl.Model.ConfigDate;
import ru.zelenbiruz.zmonl.Model.DataItem;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyApi {
    @GET("db.json")
    Call<DataItem> data();

    @GET("date.json")
    Call<ConfigDate> date();

}
