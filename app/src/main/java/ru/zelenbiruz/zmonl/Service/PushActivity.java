package ru.zelenbiruz.zmonl.Service;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import ru.zelenbiruz.zmonl.MainActivity;
import ru.zelenbiruz.zmonl.R;
import ru.zelenbiruz.zmonl.TwoMainActivity;
import ru.zelenbiruz.zmonl.ThreeMainActivity;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PushActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        String url = getIntent().getStringExtra("url_push");

//        Intent intentData = getIntent();
        if (url != null){
        Uri uri = Uri.parse(url);
//        if (uri != null && !TextUtils.isEmpty(uri.getHost())) {
            Path path = Paths.get(uri.getPath());
            String parameter = path.getFileName().toString();
            String screenName = path.getName(path.getNameCount() - 2).toString();


            if (screenName.equals("credits")) {
                Intent intentOpen = new Intent(this, TwoMainActivity.class);
                intentOpen.putExtra("id", parameter);
                startActivity(intentOpen);
            } else if (screenName.equals("cards_debit")) {
                Intent intentOpen = new Intent(this, ThreeMainActivity.class);
                intentOpen.putExtra("id", parameter);
                intentOpen.putExtra("item", screenName);
                startActivity(intentOpen);
            } else if (screenName.equals("cards_installment")) {
                Intent intentOpen = new Intent(this, ThreeMainActivity.class);
                intentOpen.putExtra("id", parameter);
                intentOpen.putExtra("item", screenName);
                startActivity(intentOpen);
            } else if (screenName.equals("card_credits")) {
                Intent intentOpen = new Intent(this, ThreeMainActivity.class);
                intentOpen.putExtra("id", parameter);
                intentOpen.putExtra("item", screenName);
                startActivity(intentOpen);
            } else if (screenName.equals("loans")) {
                Intent intentOpen = new Intent(this, MainActivity.class);
                intentOpen.putExtra("id", parameter);
                startActivity(intentOpen);
            } else if (screenName.equals("offer_item") && parameter.equals("credits")) {
                Intent intentOpen = new Intent(this, TwoMainActivity.class);
                startActivity(intentOpen);
            } else if (screenName.equals("offer_item") && parameter.equals("loans")) {
                Intent intentOpen = new Intent(this, MainActivity.class);
                startActivity(intentOpen);
            } else if (screenName.equals("offer_item") && parameter.equals("cards")) {
                Intent intentOpen = new Intent(this, ThreeMainActivity.class);
                startActivity(intentOpen);

            }
        }

    }
}
