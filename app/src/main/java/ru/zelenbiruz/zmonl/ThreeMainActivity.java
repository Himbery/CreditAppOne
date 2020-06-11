package ru.zelenbiruz.zmonl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import ru.zelenbiruz.zmonl.Fragment.CardsFragment;

public class ThreeMainActivity extends FragmentActivity implements View.OnClickListener {

    static int PAGE_COUNT = 3;
    public ViewPager pager;
    FragmentPagerAdapter pagerAdapter;
    int currentPage = 0;
    ImageView info;
    String license;
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
    String lic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        preferences = getSharedPreferences("DATA_PIN" , MODE_PRIVATE);

        String pendingCardId  = getIntent().getStringExtra("id");
        String pendingCardPageName = getIntent().getStringExtra("item");

        int pageItemIndex = 0;
        if (pendingCardPageName != null) {
            switch (pendingCardPageName) {
                case "cards_installment":
                    pageItemIndex = 2;
                    break;
                case "cards_credit":
                    pageItemIndex = 0;
                    break;
                case "cards_debit":
                    pageItemIndex = 1;
                    break;
            }
        }

        info = (ImageView) findViewById(R.id.img_info);
        pager = findViewById(R.id.viewpager);
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.pendingCardId = pendingCardId;
        pagerAdapter.pendingPage = pageItemIndex;
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(pageItemIndex, false);
        this.pagerAdapter = pagerAdapter;

        lic = preferences.getString("terms", " ");

        license = getIntent().getStringExtra("license");
        loanBtn = findViewById(R.id.btn_loan);
        cardBtn = findViewById(R.id.btn_card);
        creditBtn = findViewById(R.id.btn_credit);
        visibleCard = getIntent().getStringExtra("card_item");
        visibleLoans = getIntent().getStringExtra("loans_item");
        visibleCredits = getIntent().getStringExtra("credit_item");
        if (!(visibleCredits == null || visibleLoans == null)) {
            if (visibleCredits.equals("0")) {
                creditBtn.setVisibility(View.GONE);
            }
            if (visibleLoans.equals("0")) {
                loanBtn.setVisibility(View.GONE);
            }
        }

        lic = preferences.getString("terms", " ");
        if (lic.isEmpty()){
            lic = license;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("terms", lic);
            editor.apply();
        }



        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        creditBtn.setOnClickListener(this);
        loanBtn.setOnClickListener(this);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(ThreeMainActivity.this, LicenseActivity.class);
                intent.putExtra("name", "Требования к заёмщикам");
                intent.putExtra("license", lic);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_loan:
                Intent intent3 = new Intent(ThreeMainActivity.this, MainActivity.class);
                intent3.putExtra("loans_item", visibleLoans);
                intent3.putExtra("card_item", visibleCard);
                intent3.putExtra("credit_item", visibleCredits);
                startActivity(intent3);
                break;
            case R.id.btn_credit:
                Intent intent2 = new Intent(ThreeMainActivity.this, TwoMainActivity.class);
                intent2.putExtra("license" , lic);
                intent2.putExtra("loans_item", visibleLoans);
                intent2.putExtra("card_item", visibleCard);
                intent2.putExtra("credit_item", visibleCredits);
                startActivity(intent2);
                break;
        }
    }

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        String pendingCardId;
        int pendingPage;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return CardsFragment.newInstance(position, pendingCardId, pendingPage);
        }
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }


}
