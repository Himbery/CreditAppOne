package ru.zelenbiruz.zmonl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.yandex.metrica.YandexMetrica;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    CardView cardView;
    ImageView icon, info;
    TextView description;
    Button take;
    TextView title;
    ImageView ya, cash, visa, mir, master, qiwi;
    String yandex, nal, visA, miR, masteR, qiwI;
    String name, information, itemId, gaid;
    String aID;
    String sub1;
    String sub2;
    String sub3;
    String lic;
    SharedPreferences preferences;
    private FirebaseAnalytics mFirebaseAnalytics;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        preferences = getSharedPreferences("DATA_PIN" , MODE_PRIVATE);
        gaid = preferences.getString("gaid", "");
        aID = preferences.getString("androidID", "");
        sub1 = preferences.getString("sub1", "");
        sub2 = preferences.getString("sub2", "");
        sub3 = preferences.getString("sub3", "");
        lic = preferences.getString("terms", " ");

        info = findViewById(R.id.img_info);
        info.setOnClickListener(this);
        name = getIntent().getStringExtra("name");
        itemId = getIntent().getStringExtra("itemId");
        information = getIntent().getStringExtra("license");

         final Map<String, Object> external_link = new HashMap<String, Object>();
        String offer_id = itemId + " " + name;
        external_link.put("offer_id", offer_id);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        final Bundle bundle = new Bundle();

        ya = findViewById(R.id.ya);
        cash = findViewById(R.id.nal);
        visa = findViewById(R.id.visa);
        mir = findViewById(R.id.mir);
        master = findViewById(R.id.master);
        qiwi = findViewById(R.id.qiwi);
        yandex = getIntent().getStringExtra("ya");
        miR = getIntent().getStringExtra("mir");
        masteR = getIntent().getStringExtra("master");
        qiwI = getIntent().getStringExtra("qiwi");
        visA = getIntent().getStringExtra("visa");
        nal = getIntent().getStringExtra("cash");

        cardView = (CardView) findViewById(R.id.cardview);
        icon = findViewById(R.id.logo);
        description = findViewById(R.id.description);
        title = findViewById(R.id.activity_articles_articles);
        take = findViewById(R.id.take_btn);

        int id = getIntent().getIntExtra("id",0);
        String image = getIntent().getStringExtra("icon");
        String text = getIntent().getStringExtra("description");
        String btn = getIntent().getStringExtra("txt_btn");
        final String name = getIntent().getStringExtra("name");
        String order = getIntent().getStringExtra("order");
        final String browserType = getIntent().getStringExtra("browserType");

        String url = order + "&aff_sub1=" + sub1 + "&aff_sub2=" + sub2 + "&aff_sub3=" + sub3 + "&aff_sub4=" + aID
                + "&aff_sub5=" + gaid;

        description.setText(Html.fromHtml(text));
        description.setMovementMethod(new ScrollingMovementMethod());
        Glide.with(this).load(image).into(icon);
        take.setText(btn);
        title.setText(name);

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAnalytics.logEvent("external_link", bundle);
                YandexMetrica.reportEvent("external_link", external_link);
                if (browserType.equals("external")) {
                    if (isOnline()) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(myIntent);
                    } else {
                        Intent myIntent = new Intent(DetailActivity.this, ErrorActivity.class);
                        startActivity(myIntent);
                    }
                } else {
                    Intent intent = new Intent(DetailActivity.this, WebActivity.class);
                    intent.putExtra("order", url);
                    intent.putExtra("browser", browserType);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            }
        });
        if (qiwI.isEmpty()){
            qiwi.setVisibility(View.GONE);
        }
        if (masteR.isEmpty()){
            master.setVisibility(View.GONE);
        }
        if (miR.isEmpty()){
            mir.setVisibility(View.GONE);
        }
        if (visA.isEmpty()){
            visa.setVisibility(View.GONE);
        }
        if (yandex.isEmpty()){
            ya.setVisibility(View.GONE);
        }
        if (nal.isEmpty()){
            cash.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(DetailActivity.this, LicenseActivity.class);
        intent.putExtra("name", "Требования к заёмщикам");
        intent.putExtra("license", lic);
        startActivity(intent);
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        int exitValue = -1;

        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            exitValue = ipProcess.waitFor();
        }
        catch (IOException | InterruptedException ignored) {}
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && exitValue == 0 && netInfo.isConnectedOrConnecting();
    }

}
