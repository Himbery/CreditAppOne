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
import ru.zelenbiruz.zmonl.Model.ConfigDate;
import ru.zelenbiruz.zmonl.Model.DataItem;

import ru.zelenbiruz.zmonl.Model.ConfigLoan;
import ru.zelenbiruz.zmonl.Service.MyApi;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView listLoans;
    List <ConfigLoan>loansList;
    ImageView info;
    String license;
    LoansAdapter lAdapter;
    String visibleCredits;
    String visibleCard;
    String visibleLoans;
    String visibleCred;
    String visibleDeb;
    String visibleInst;
    Button loanBtn;
    Button cardBtn;
    Button creditBtn;
    SharedPreferences preferences;
    List mLoan;
    String dateNow;
    String dateJson;
    Date date2;
    Date date1;
    String pendingCreditId;
    String terms_text;
    String gaid;
    String aID;
    String sub1;
    String sub2;
    String lic;
    String sub3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (ImageView) findViewById(R.id.img_info);
        listLoans = findViewById(R.id.list);
        listLoans.setLayoutManager(new LinearLayoutManager(this));
        terms_text= getIntent().getStringExtra("terms");

        preferences = getSharedPreferences("DATA_PIN" , MODE_PRIVATE);

        gaid = preferences.getString("gaid", "");
        aID = preferences.getString("androidID", "");
        sub1 = preferences.getString("sub1", "");
        sub2 = preferences.getString("sub2", "");
        sub3 = preferences.getString("sub3", "");


//        dateNow = preferences.getString("date", " Date is not saved" );
        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");

//        if (dateNow.equals(" Date is not saved")) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://zaimdozarplaty.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            MyApi api = retrofit.create(MyApi.class);

            Call<ConfigDate> call = api.date();
            call.enqueue(new Callback<ConfigDate>() {
                @Override
                public void onResponse(Call<ConfigDate> call, Response<ConfigDate> response) {

                    ConfigDate configDate = response.body();
                    dateJson = configDate.date;
                    date2 = new Date();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("date1", dateJson);
                    editor.apply();
                    try {
                        date2 = sdf.parse(dateJson);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    dateNow = preferences.getString("date1", " Date is not saved" );
                    try {
                        date1 = sdf.parse(dateNow);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                    if (date2.after(date1) || dateNow.equals(" Date is not saved")) {
                        getResult();
//                    } else {
//                        logData();
//                    }
                }

                @Override
                public void onFailure(Call<ConfigDate> call, Throwable t) {
                }
            });
        visibleCard = getIntent().getStringExtra("card_item");
        visibleCredits = getIntent().getStringExtra("credit_item");
        visibleLoans = getIntent().getStringExtra("loans_item");

        loanBtn = findViewById(R.id.btn_loan);
        cardBtn = findViewById(R.id.btn_card);
        creditBtn = findViewById(R.id.btn_credit);


        creditBtn.setOnClickListener(this);
        cardBtn.setOnClickListener(this);
        lic = preferences.getString("terms", " ");
        if (lic.isEmpty()){
            lic = license;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("terms", lic);
            editor.apply();
        }

        if (!(visibleCredits == null || visibleCard == null || visibleLoans == null)) {
            if (visibleCard.equals("0")) {
                cardBtn.setVisibility(View.GONE);
            }
            if (visibleCredits.equals("0")) {
                creditBtn.setVisibility(View.GONE);
            }
        }
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LicenseActivity.class);
                intent.putExtra("name", "Требования к заёмщикам");
                intent.putExtra("license", lic);
                startActivity(intent);
            }
        });
        pendingCreditId = getIntent().getStringExtra("id");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_card:
                        Intent intent2 = new Intent(MainActivity.this, ThreeMainActivity.class);
                        intent2.putExtra("license" , license);
                        intent2.putExtra("name", "Требования к заёмщикам");
                        intent2.putExtra("card_item", visibleCard);
                        intent2.putExtra("loans_item", visibleLoans);
                        intent2.putExtra("credit_item", visibleCredits);
                        startActivity(intent2);
                        break;
            case R.id.btn_credit:
                        Intent intent3 = new Intent(MainActivity.this, TwoMainActivity.class);
                        intent3.putExtra("license" , license);
                        intent3.putExtra("name", "Требования к заёмщикам");
                        intent3.putExtra("loans_item", visibleLoans);
                        intent3.putExtra("card_item", visibleCard);
                        intent3.putExtra("credit_item", visibleCredits);
                        startActivity(intent3);
                        break;
        }
    }

//    public void logData() {
//        String s = preferences.getString("LOAN_DATA", " ");
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<ConfigLoan>>(){} .getType();
//        mLoan = gson.fromJson(s, type);
//        lAdapter = new LoansAdapter(MainActivity.this, mLoan);
//        listLoans.setAdapter(lAdapter);
//        lAdapter.notifyDataSetChanged();
//
//        if(pendingCreditId != null) {
//            openItem(pendingCreditId);
//        }
//    }
    public void getResult(){
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

                loansList = dataItem.loans;
                license = dataItem.licenseTerm;

                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String s = gson.toJson(loansList);
                editor.putString("LOAN_DATA", s);
                editor.putString("License", license);
                editor.apply();

                lAdapter = new LoansAdapter(MainActivity.this, loansList);
                listLoans.setAdapter(lAdapter);
                lAdapter.notifyDataSetChanged();

                if(pendingCreditId != null) {
                    openItem(pendingCreditId);
                }
            }

            @Override
            public void onFailure(Call<DataItem> call, Throwable t) {
            }
        });
    }
    public void openItem(String id) {
        int itemIndex = 0;
        for (ConfigLoan loan : loansList) {
            if (loan.id.equals(id)) {
                itemIndex = loansList.indexOf(loan);
            }
        }

        final int finalItemIndex = itemIndex;
        listLoans.scrollToPosition(finalItemIndex);
        listLoans.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                listLoans.findViewHolderForAdapterPosition(finalItemIndex).itemView.performClick();
            }
        });
    }
}
