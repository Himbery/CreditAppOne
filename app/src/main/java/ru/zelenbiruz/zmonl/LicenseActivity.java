package ru.zelenbiruz.zmonl;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import ru.zelenbiruz.zmonl.Model.DataItem;
import ru.zelenbiruz.zmonl.Service.MyApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LicenseActivity extends AppCompatActivity {

    TextView license;
    TextView title;
    String lic;
    String text;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_activity);
        preferences = getSharedPreferences("DATA_PIN", MODE_PRIVATE);

        text = getIntent().getStringExtra("license");
        String name = getIntent().getStringExtra("name");
        title = findViewById(R.id.activity_articles_articles);
        license = findViewById(R.id.license);

        if (text.equals(" ")) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://zaimdozarplaty.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            MyApi api = retrofit.create(MyApi.class);

            Call<DataItem> call = api.data();
            call.enqueue(new Callback<DataItem>() {
                @Override
                public void onResponse(Call<DataItem> call, Response<DataItem> response) {

                    DataItem dataItem = response.body();
                    lic = dataItem.licenseTerm;

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("License", lic);
                    editor.apply();

                    license.setText(Html.fromHtml(lic));
                    license.setMovementMethod(new ScrollingMovementMethod());

                }

                @Override
                public void onFailure(Call<DataItem> call, Throwable t) {
                }
            });
        }

        title.setText(name);
        license.setText(Html.fromHtml(text));
        license.setMovementMethod(new ScrollingMovementMethod());


    }

}
