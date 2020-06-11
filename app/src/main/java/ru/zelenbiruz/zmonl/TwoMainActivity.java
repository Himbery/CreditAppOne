package ru.zelenbiruz.zmonl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.zelenbiruz.zmonl.Model.DataItem;

import ru.zelenbiruz.zmonl.Model.ConfigCredit;
import ru.zelenbiruz.zmonl.Service.MyApi;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwoMainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView creditsList;
    ImageView info;
    String license;
    CreditsAdapter cAdapter;
    ConfigCredit configCredit;
    Button loanBtn;
    Button cardBtn;
    Button creditBtn;
    String visibleCredits;
    String visibleCard;
    String visibleLoans;
    SharedPreferences preferences;
    ArrayList<ConfigCredit> mCredit;
    List<ConfigCredit> creditList;
    String pendingCreditId;
    String lic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        preferences = getSharedPreferences("DATA_PIN", MODE_PRIVATE);

        pendingCreditId = getIntent().getStringExtra("id");
        String text = getIntent().getStringExtra("license");

        lic= preferences.getString("terms", " ");
        loanBtn = findViewById(R.id.btn_loan);
        cardBtn = findViewById(R.id.btn_card);
        creditBtn = findViewById(R.id.btn_credit);

        visibleCard = getIntent().getStringExtra("card_item");
        visibleLoans = getIntent().getStringExtra("loans_item");
        visibleCredits = getIntent().getStringExtra("credit_item");
        if (!(visibleCard == null || visibleLoans == null)) {
            if (visibleCard.equals("0")) {
                cardBtn.setVisibility(View.GONE);
            }
            if (visibleLoans.equals("0")) {
                loanBtn.setVisibility(View.GONE);
            }
        }
        info = (ImageView) findViewById(R.id.img_info);
        creditsList = findViewById(R.id.list);
        creditsList.setLayoutManager(new LinearLayoutManager(this));

//        String s = preferences.getString("CREDIT_DATA", "Data is not saved" );
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<ConfigCredit>>(){} .getType();
//        mCredit = gson.fromJson(s, type);

            if (mCredit == null) {
                getResult();
            } else {
                logData();
            }
            loanBtn.setOnClickListener(this);
            cardBtn.setOnClickListener(this);

            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TwoMainActivity.this, LicenseActivity.class);
                    intent.putExtra("name", "Требования к заёмщикам");
                    intent.putExtra("license", lic);
                    startActivity(intent);
                }
            });
    }

    public void getResult() {
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
                String initL = dataItem.initLicenseTerm;
                creditList = dataItem.credits;
                license = dataItem.licenseTerm;

                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String s = gson.toJson(creditList);
                editor.putString("CREDIT_DATA", s);
                editor.putString("License", license);
                editor.apply();

                cAdapter = new CreditsAdapter(TwoMainActivity.this, creditList);
                creditsList.setAdapter(cAdapter);
                cAdapter.notifyDataSetChanged();

                if(pendingCreditId != null) {
                    openItem(pendingCreditId);
                }
            }

            @Override
            public void onFailure(Call<DataItem> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_card:
                Intent intent2 = new Intent(TwoMainActivity.this, ThreeMainActivity.class);
                intent2.putExtra("license", lic);
                intent2.putExtra("card_item", visibleCard);
                intent2.putExtra("loans_item", visibleLoans);
                intent2.putExtra("credit_item", visibleCredits);
                startActivity(intent2);
                break;
            case R.id.btn_loan:
                Intent intent3 = new Intent(TwoMainActivity.this, MainActivity.class);
                intent3.putExtra("loans_item", visibleLoans);
                intent3.putExtra("card_item", visibleCard);
                intent3.putExtra("credit_item", visibleCredits);
                startActivity(intent3);
                break;
        }
    }

    public void logData() {
        String s = preferences.getString("CREDIT_DATA", "Data is not saved");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ConfigCredit>>() {
        }.getType();
        mCredit = gson.fromJson(s, type);
        cAdapter = new CreditsAdapter(TwoMainActivity.this, mCredit);
        creditsList.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();

        if(pendingCreditId != null) {
            openItem(pendingCreditId);
        }
    }

    public void openItem(String id) {
        int itemIndex = 0;
        for (ConfigCredit credit : creditList) {
            if (credit.id.equals(id)) {
                itemIndex = creditList.indexOf(credit);
            }
        }

        final int finalItemIndex = itemIndex;
        creditsList.scrollToPosition(finalItemIndex);
        creditsList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                creditsList.findViewHolderForAdapterPosition(finalItemIndex).itemView.performClick();
            }
        });
    }
}
