package ru.zelenbiruz.zmonl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.andexert.library.RippleView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.yandex.metrica.YandexMetrica;

import java.util.Random;

public class PinActivity extends AppCompatActivity {

    EditText phone;
    EditText code;
    Button codeBtn;
    Button okBtn;
    int finalI;
    String country;
    String codeNumber;
    String numberPhone;
    String visibleCredits;
    String visibleCard;
    String visibleLoans;
    RippleView rippleViewCode;
    RippleView rippleViewOk;
    Boolean pinCode;
    SharedPreferences pref;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_activity);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        final Bundle bundle = new Bundle();

        pref = getApplicationContext().getSharedPreferences("DATA_PIN", Context.MODE_PRIVATE);

        country = getIntent().getStringExtra("country");
        if (country.equals("RU")) {
            codeNumber = "+7";
        }else if(country.equals("UA")){
            codeNumber = "+380";
        }

        if (isEmulator() || country == null){
            codeNumber = "+1";
        }

        visibleCard = getIntent().getStringExtra("card_item");
        visibleCredits = getIntent().getStringExtra("credit_item");
        visibleLoans = getIntent().getStringExtra("loans_item");

        codeBtn = findViewById(R.id.code1);
        okBtn = findViewById(R.id.ok_btn1);
        phone = findViewById(R.id.phone_edit);
        code = findViewById(R.id.code_edit);
        rippleViewCode = (RippleView) findViewById(R.id.code);
        rippleViewOk = (RippleView) findViewById(R.id.ok_btn);

        int min = 1001;
        int max = 9999;
        int diff = max - min;
        final Random random = new Random();
        int i = random.nextInt(diff + 1);
        i += min;
        finalI = i;
        if (country.equals("RU") ||country.equals("UA")) {

            phone.setText(codeNumber);
            Selection.setSelection(phone.getText(), phone.getText().length());

            phone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().startsWith(codeNumber))
                    {
                        phone.setText(codeNumber);
                        Selection.setSelection(phone.getText(), phone.getText().length());
                    }
                }
            });

            numberPhone = phone.getText().toString();

            rippleViewCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (phone.getText().toString().length()> 10) {
                        sendSMSMessage();
                    } else {
                        Toast.makeText(getApplicationContext(), "Необходимо ввести номер телефона", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (country == null){
            Toast.makeText(getApplicationContext(), "Ошибка..", Toast.LENGTH_LONG).show();
        }

        rippleViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!code.getText().toString().isEmpty()){
                    pinCode = true;
                    SharedPreferences.Editor ed = pref.edit();
                    ed.putBoolean("pincode", pinCode);
                    ed.apply();

                    if (visibleLoans.equals("1")) {
                        Intent intent = new Intent(PinActivity.this, MainActivity.class);
                        intent.putExtra("card_item", visibleCard);
                        intent.putExtra("credit_item", visibleCredits);
                        intent.putExtra("loans_item", visibleLoans);
                        startActivity(intent);
                        mFirebaseAnalytics.logEvent("pin_accepted", bundle);
                        YandexMetrica.reportEvent("pin_accepted");

                        finish();

                    } else if(visibleLoans.equals("0") ){
                        Intent intent = new Intent(PinActivity.this, ThreeMainActivity.class);
                        intent.putExtra("loans_item", visibleLoans);
                        intent.putExtra("credit_item", visibleCredits);
                        intent.putExtra("card_item", visibleCard);
                        startActivity(intent);
                        mFirebaseAnalytics.logEvent("pin_accepted", bundle);
                        YandexMetrica.reportEvent("pin_accepted");

                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Необходимо ввести код", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    protected void sendSMSMessage() {

        final String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(String.valueOf(finalI));

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

        Intent deleteIntent = new Intent(this, PinActivity.class);
        PendingIntent deletePendingIntent = PendingIntent.getService(this, 0, deleteIntent, 0);

        builder.addAction(android.R.drawable.ic_delete, "Удалить", deletePendingIntent);
        builder.setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My channel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My channel description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private boolean isEmulator(){
        return "google_sdk".equals( Build.PRODUCT ) || "sdk".equals( Build.PRODUCT );
    }
}
