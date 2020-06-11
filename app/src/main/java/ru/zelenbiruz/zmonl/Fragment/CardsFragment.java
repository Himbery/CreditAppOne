package ru.zelenbiruz.zmonl.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.zelenbiruz.zmonl.Model.ConfigCardsDebit;
import ru.zelenbiruz.zmonl.Model.ConfigCardsInstallment;

import ru.zelenbiruz.zmonl.CardCreditAdapter;
import ru.zelenbiruz.zmonl.CardDebitAdapter;
import ru.zelenbiruz.zmonl.CardInstallAdapter;
import ru.zelenbiruz.zmonl.Model.ConfigApp;
import ru.zelenbiruz.zmonl.Model.ConfigCardsCredit;
import ru.zelenbiruz.zmonl.Model.DataItem;
import ru.zelenbiruz.zmonl.R;
import ru.zelenbiruz.zmonl.Service.MyApi;
import ru.zelenbiruz.zmonl.ThreeMainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class CardsFragment extends Fragment {
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static final String PENDING_CARD_ID = "pending_card_id";
    static final String PENDING_CARD_PAGE = "pending_card_page";
    int pageNumber;
    View view = null;
    int item;
    String visibleCred;
    String visibleDeb;
    String visibleInst;
    SharedPreferences preferences;
    List <ConfigCardsCredit>cardsList;
    List <ConfigCardsInstallment>cardsInstallList;
    List <ConfigCardsDebit>debitList;
    String license;
    RecyclerView cardCrList;
    RecyclerView cardDList;
    RecyclerView cardIList;
    ArrayList<ConfigCardsCredit> mCCredit;
    ArrayList<ConfigCardsDebit> mCDebit;
    ArrayList<ConfigCardsInstallment> mCInst;
    String pendingCardId;
    int pendingPage;

    public static CardsFragment newInstance(int page, String pendingCardId, int pendingPage) {
        CardsFragment cardsFragment = new CardsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        arguments.putString(PENDING_CARD_ID, pendingCardId);
        arguments.putInt(PENDING_CARD_PAGE, pendingPage);
        cardsFragment.setArguments(arguments);
        return cardsFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        pendingCardId = getArguments().getString(PENDING_CARD_ID);
        pendingPage = getArguments().getInt(PENDING_CARD_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        preferences = getActivity().getSharedPreferences("DATA" , MODE_PRIVATE);

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
                ConfigApp app = dataItem.appConfig;
                visibleInst = app.cards_installment_item;
                visibleDeb = app.cards_debit_item;
                visibleCred = app.cards_credit_item;

            }
                @Override
                public void onFailure(Call<DataItem> call, Throwable t) {
                }
            });

        switch (pageNumber) {
            case 0:
                view = inflater.inflate(R.layout.fragment_0, null);
                item = 0;

                if (cardsList == null) {
                    getCreditCard();
                } else {
                    loadCredit();
                }
                break;
            case 1:
                view = inflater.inflate(R.layout.fragment_1, null);
                item = 1;

                if (debitList == null) {
                    getDebitCard();
                }else {
                    loadDebit();
                }
                break;
            case 2:
                view = inflater.inflate(R.layout.fragment_2, null);
                item = 2;

                if (cardsInstallList == null) {
//
                    getIntCard();
                }else {
                    loadInst();
                }
                break;
        }

        Button cardBtn= view.findViewById(R.id.btn_credit);
        Button debitBtn = view.findViewById(R.id.btn_debit);
        Button iBtn = view.findViewById(R.id.btn_install);

        if (!(visibleInst == null || visibleDeb == null || visibleCred == null) ) {
            if (visibleCred.equals("0")) {
                cardBtn.setVisibility(View.GONE);
            }
            if (!visibleDeb.equals("0")) {
                debitBtn.setVisibility(View.GONE);
            }
            if (!visibleInst.equals("0")) {
                iBtn.setVisibility(View.GONE);
            }
        }


        cardBtn.setOnClickListener(cardBtnListener);
        debitBtn.setOnClickListener(cardBtnListener);
        iBtn.setOnClickListener(cardBtnListener);
        return view;
    }

    public void getCreditCard(){
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
                cardsList = dataItem.cardsCredit;
                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String s = gson.toJson(cardsList);
                editor.putString("CCREDIT_DATA", s);
                editor.putString("License", license);
                editor.apply();

                cardCrList = view.findViewById(R.id.list_cr);
                cardCrList.setLayoutManager(new LinearLayoutManager(getActivity()));
                CardCreditAdapter crAdapter = new CardCreditAdapter(getActivity(), cardsList);
                cardCrList.setAdapter(crAdapter);
                if(pendingCardId != null && pendingPage == pageNumber) {
                    openItem(pendingPage, pendingCardId);
                }

            }
            @Override
            public void onFailure(Call<DataItem> call, Throwable t) {
            }
        });
    }
    public void getDebitCard(){
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
                debitList = dataItem.cardsDebit;
                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String s = gson.toJson(debitList);
                editor.putString("CDEBIT_DATA", s);
                editor.putString("License", license);
                editor.apply();

                cardDList = view.findViewById(R.id.list_d);
                cardDList.setLayoutManager(new LinearLayoutManager(getActivity()));
                CardDebitAdapter crAdapter = new CardDebitAdapter(getActivity(), debitList);
                cardDList.setAdapter(crAdapter);

                if(pendingCardId != null && pendingPage == pageNumber) {
                    openItem(pendingPage, pendingCardId);
                }
            }
            @Override
            public void onFailure(Call<DataItem> call, Throwable t) {
            }
        });
    }
    public void getIntCard(){
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
                cardsInstallList = dataItem.cardsInstallment;
                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String s = gson.toJson(cardsList);
                editor.putString("CINST_DATA", s);
                editor.putString("License", license);
                editor.apply();

                cardIList = view.findViewById(R.id.list_i);
                cardIList.setLayoutManager(new LinearLayoutManager(getActivity()));
                CardInstallAdapter iAdapter = new CardInstallAdapter(getActivity(), cardsInstallList);
                cardIList.setAdapter(iAdapter);

                if(pendingCardId != null && pendingPage == pageNumber) {
                    openItem(pendingPage, pendingCardId);
                }

            }
            @Override
            public void onFailure(Call<DataItem> call, Throwable t) {
            }
        });
    }

    public void loadCredit(){
        String s = preferences.getString("CCREDIT_DATA", "Data is not saved" );
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ConfigCardsCredit>>(){} .getType();
        cardsList = gson.fromJson(s, type);
        cardCrList = view.findViewById(R.id.list_cr);
        cardCrList.setLayoutManager(new LinearLayoutManager(getActivity()));
        CardCreditAdapter crAdapter = new CardCreditAdapter(getActivity(), cardsList);
        cardCrList.setAdapter(crAdapter);

        if(pendingCardId != null && pendingPage == pageNumber) {
            openItem(pendingPage, pendingCardId);
        }
    }
    public void loadDebit(){
        String d = preferences.getString("CDEBIT_DATA", "Data is not saved" );
        Gson gsonD = new Gson();
        Type typeD = new TypeToken<ArrayList<ConfigCardsDebit>>(){} .getType();
        debitList = gsonD.fromJson(d, typeD);
        cardDList = view.findViewById(R.id.list_d);
        cardDList.setLayoutManager(new LinearLayoutManager(getActivity()));
        CardDebitAdapter crAdapter = new CardDebitAdapter(getActivity(), debitList);
        cardDList.setAdapter(crAdapter);

        if(pendingCardId != null && pendingPage == pageNumber) {
            openItem(pendingPage, pendingCardId);
        }
    }
    public void loadInst(){
        String i = preferences.getString("CINST_DATA", "Data is not saved" );
        Gson gsonI = new Gson();
        Type typeI = new TypeToken<ArrayList<ConfigCardsInstallment>>(){} .getType();
        cardsInstallList = gsonI.fromJson(i, typeI);
        cardIList = view.findViewById(R.id.list_i);
        cardIList.setLayoutManager(new LinearLayoutManager(getActivity()));
        CardInstallAdapter iAdapter = new CardInstallAdapter(getActivity(), cardsInstallList);
        cardIList.setAdapter(iAdapter);

        if(pendingCardId != null && pendingPage == pageNumber) {
            openItem(pendingPage, pendingCardId);
        }
    }
    private View.OnClickListener cardBtnListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_credit:
                    ((ThreeMainActivity) getActivity()).pager.setCurrentItem(0, true);
                    break;
                case R.id.btn_debit:
                    ((ThreeMainActivity) getActivity()).pager.setCurrentItem(1, true);
                    break;
                case R.id.btn_install:
                    ((ThreeMainActivity) getActivity()).pager.setCurrentItem(2, true);
                    break;
            }
        }
    };

    public void openItem(int page, String itemId) {
        if (page == 0) {
            int itemIndex = 0;
            for (ConfigCardsCredit credit : cardsList) {
                if (credit.id.equals(itemId)) {
                    itemIndex = cardsList.indexOf(credit);
                }
            }
            final int finalItemIndex = itemIndex;
            cardCrList.scrollToPosition(finalItemIndex);
            cardCrList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    cardCrList.findViewHolderForAdapterPosition(finalItemIndex).itemView.performClick();
                }
            });
        } else if (page == 1) {
            int itemIndex = 0;
            for (ConfigCardsDebit debit : debitList) {
                if (debit.id.equals(itemId)) {
                    itemIndex = debitList.indexOf(debit);
                }
            }
            final int finalItemIndex = itemIndex;
            cardDList.scrollToPosition(finalItemIndex);
            cardDList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    cardDList.findViewHolderForAdapterPosition(finalItemIndex).itemView.performClick();
                }
            });
        } else if(page == 2){
            int itemIndex = 0;
            for (ConfigCardsInstallment installment : cardsInstallList) {
                if (installment.id.equals(itemId)) {
                    itemIndex = cardsInstallList.indexOf(installment);
                }
            }
            final int finalItemIndex = itemIndex;
            cardIList.scrollToPosition(finalItemIndex);
            cardIList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    cardIList.findViewHolderForAdapterPosition(finalItemIndex).itemView.performClick();
                }
            });
        }

    }
}
