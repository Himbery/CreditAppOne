package ru.zelenbiruz.zmonl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TermsOfUseActivity extends AppCompatActivity implements View.OnClickListener {
    TextView htmlString;
    String terms;
    String license;
    CheckBox check;
    Button okBtn;
    String pin;
    ImageView info;
    Boolean move = false;
    Boolean pinCode = false;
    SharedPreferences pref;
    String CountryID;
    String simCountryIso;
    String visibleCredits;
    String visibleCard;
    String visibleLoans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        getCountryZipCode();
        okBtn = findViewById(R.id.button);
        check = findViewById(R.id.checkBoxBold);
        okBtn.setOnClickListener(this);
        info = (ImageView) findViewById(R.id.img_info);
        htmlString = findViewById(R.id.html_text);
        license = getIntent().getStringExtra("license");
        pin = getIntent().getStringExtra("pin");
        terms = getIntent().getStringExtra("terms");

        visibleCard = getIntent().getStringExtra("card_item");
        visibleCredits = getIntent().getStringExtra("credit_item");
        visibleLoans = getIntent().getStringExtra("loans_item");

        pref = getApplicationContext().getSharedPreferences("DATA_PIN", Context.MODE_PRIVATE);
        move = pref.getBoolean("move", Boolean.parseBoolean(" "));
        pinCode = pref.getBoolean("pincode", Boolean.parseBoolean(" ") );
        if (move && pinCode){
            Intent intent = new Intent(TermsOfUseActivity.this, MainActivity.class);
            intent.putExtra("loans_item", visibleLoans);
            intent.putExtra("card_item", visibleCard);
            intent.putExtra("credit_item", visibleCredits);
            startActivity(intent);
            finish();
        }
        if (move && !pinCode){
            if (pin.equals("1")) {
                Intent intent = new Intent(TermsOfUseActivity.this, PinActivity.class);
                intent.putExtra("country", CountryID);
                intent.putExtra("loans_item", visibleLoans);
                intent.putExtra("card_item", visibleCard);
                intent.putExtra("credit_item", visibleCredits);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(TermsOfUseActivity.this, MainActivity.class);
                intent.putExtra("loans_item", visibleLoans);
                intent.putExtra("card_item", visibleCard);
                intent.putExtra("credit_item", visibleCredits);
                startActivity(intent);
                finish();
            }
        }

        String htmlTaggedString  = "Политикой конфиденциальности";
        Spanned textSpan  =  android.text.Html.fromHtml(htmlTaggedString);
        htmlString.setText(textSpan);
        htmlString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsOfUseActivity.this, LicenseActivity.class);
                intent.putExtra("license", license);
                intent.putExtra("name", "Политика конфиденциальности");
                startActivity(intent);
            }
        });
    }

    private String getCountryZipCode() {

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        return CountryID;
    }

    @Override
    public void onClick(View v) {
        if (check.isChecked()){
            move = true;
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("move", move);
            ed.apply();
            if (pin.equals("1")) {

                Intent intent = new Intent(TermsOfUseActivity.this, PinActivity.class);
                intent.putExtra("country", CountryID);
                intent.putExtra("loans_item", visibleLoans);
                intent.putExtra("card_item", visibleCard);
                intent.putExtra("credit_item", visibleCredits);
                startActivity(intent);

            } else {
                if (visibleLoans.equals("1")) {
                    Intent intent = new Intent(TermsOfUseActivity.this, MainActivity.class);
                    intent.putExtra("card_item", visibleCard);
                    intent.putExtra("credit_item", visibleCredits);
                    intent.putExtra("loans_item", visibleLoans);
                    intent.putExtra("terms", terms);
                    startActivity(intent);


                } else if(visibleLoans.equals("0") ){
                    Intent intent = new Intent(TermsOfUseActivity.this, ThreeMainActivity.class);
                    intent.putExtra("loans_item", visibleLoans);
                    intent.putExtra("credit_item", visibleCredits);
                    intent.putExtra("card_item", visibleCard);
                    startActivity(intent);

                }
            }
            finish();
        } else {
            Toast.makeText(this, "Необходимо принять пользовательское соглашение", Toast.LENGTH_LONG).show();
        }

    }
}
