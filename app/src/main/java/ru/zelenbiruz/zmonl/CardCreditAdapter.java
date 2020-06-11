package ru.zelenbiruz.zmonl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.yandex.metrica.YandexMetrica;
import ru.zelenbiruz.zmonl.Model.ConfigCardsCredit;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class CardCreditAdapter extends RecyclerView.Adapter<CardCreditAdapter.ViewHolder>{
    private LayoutInflater inflater;
    private List<ConfigCardsCredit> credits = new ArrayList<>();

    public CardCreditAdapter(Context context, List<ConfigCardsCredit> credits) {
        this.credits = credits;
        this.inflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public CardCreditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new CardCreditAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ConfigCardsCredit credit = credits.get(position);
        Glide.with(holder.itemView.getContext()).load(credit.screen).into(holder.imageView);
        holder.nameView.setText(credit.name);
        holder.sumView.setText("Сумма " + credit.summPrefix + " " + credit.summMin +" " + credit.summMid + " " +credit.summMax + credit.summPostfix);
        holder.stavkaView.setText("Ставка " + credit.percentPrefix + " " + credit.percent + " " + credit.percentPostfix);
        holder.termsView.setText("Срок " + credit.termPrefix + " " +credit.termMin + " " +credit.termMid + " " +credit.termMax + credit.termPostfix);
        holder.rating.setRating(credit.getScore());
        if (credit.qiwi.isEmpty()){
            holder.qiwi.setVisibility(View.GONE);
        }
        if (credit.mastercard.isEmpty()){
            holder.master.setVisibility(View.GONE);
        }
        if (credit.mir.isEmpty()){
            holder.mir.setVisibility(View.GONE);
        }
        if (credit.visa.isEmpty()){
            holder.visa.setVisibility(View.GONE);
        }
        if (credit.yandex.isEmpty()){
            holder.ya.setVisibility(View.GONE);
        }
        if (credit.cash.isEmpty()){
            holder.cash.setVisibility(View.GONE);
        }
        final Map<String, Object> external_link = new HashMap<String, Object>();
        String offer_id = credit.itemId + " " + credit.name;
        external_link.put("offer_id", offer_id);

        holder.detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                openDetailsActivity(context, credit);
            }
        });

        holder.get_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();

                String aID;
                String sub1;
                String sub2;
                String sub3;
                String gaid;
                SharedPreferences preferences;
                preferences = context.getSharedPreferences("DATA_PIN" , MODE_PRIVATE);
                gaid = preferences.getString("gaid", "");
                aID = preferences.getString("androidID", "");
                sub1 = preferences.getString("sub1", "");
                sub2 = preferences.getString("sub2", "");
                sub3 = preferences.getString("sub3", "");

                String url = credit.order + "&aff_sub1=" + sub1 + "&aff_sub2=" + sub2 + "&aff_sub3=" + sub3 + "&aff_sub4=" + aID
                        + "&aff_sub5=" + gaid;

//                Uri.Builder builder = new Uri.Builder();
//                builder.scheme(credit.order)
//                        .appendQueryParameter("aff_sub1", sub1)
//                        .appendQueryParameter("aff_sub2", sub2)
//                        .appendQueryParameter("aff_sub3", sub3)
//                        .appendQueryParameter("aff_sub4", aID)
//                        .appendQueryParameter("aff_sub5" ,gaid);
//                String url = builder.build().toString();

                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                Bundle bundle = new Bundle();
                bundle.putString("external_link", "external_link");
                YandexMetrica.reportEvent("offer_id", external_link);
                mFirebaseAnalytics.logEvent("external_link", bundle);
                if (credit.browserType.equals("external")){
                    if (isOnline()) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(myIntent);
                    } else {
                        Intent myIntent = new Intent(context, ErrorActivity.class);
                        context.startActivity(myIntent);
                    }
                } else {
//                    if (loan.browserType.equals("iternal")) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("order", url);
                    intent.putExtra("browser", credit.browserType);
                    intent.putExtra("name", credit.name);
                    context.startActivity(intent);
                }
            }
            public boolean isOnline() {
                Context context = holder.itemView.getContext();;
                Runtime runtime = Runtime.getRuntime();
                int exitValue = -1;

                try {
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                    exitValue = ipProcess.waitFor();
                }
                catch (IOException | InterruptedException ignored) {}
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                return netInfo != null && exitValue == 0 && netInfo.isConnectedOrConnecting();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                openDetailsActivity(context, credit);
            }
        });


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView c;
        final ImageView imageView, ya, cash, visa, mir, master, qiwi;
        final TextView nameView, sumView, stavkaView, termsView;
        RatingBar rating;
        Button detail_btn, get_btn;
        ViewHolder(View view){
            super(view);
            c = (CardView) view.findViewById(R.id.body);
            imageView = (ImageView)view.findViewById(R.id.icon);
            nameView = (TextView) view.findViewById(R.id.title);
            sumView = (TextView) view.findViewById(R.id.sum);
            stavkaView = (TextView)view.findViewById(R.id.stavka);
            termsView = (TextView) view.findViewById(R.id.terms);
            rating = (RatingBar) view.findViewById(R.id.score);
            detail_btn = (Button) view.findViewById(R.id.detail);
            get_btn = (Button) view.findViewById(R.id.get_btn);
            ya = view.findViewById(R.id.ya);
            cash = view.findViewById(R.id.nal);
            visa = view.findViewById(R.id.visa);
            mir = view.findViewById(R.id.mir);
            master = view.findViewById(R.id.master);
            qiwi = view.findViewById(R.id.qiwi);
        }
    }
    @Override
    public int getItemCount() {
        return credits.size();
    }

    public void openDetailsActivity(Context context, ConfigCardsCredit credit) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("itemId", credit.itemId);
        intent.putExtra("icon", credit.screen);
        intent.putExtra("description", credit.description);
        intent.putExtra("txt_btn", credit.orderButtonText);
        intent.putExtra("name", credit.name);
        intent.putExtra("order", credit.order);
        intent.putExtra("browserType", credit.browserType);
        intent.putExtra("qiwi", credit.qiwi);
        intent.putExtra("visa", credit.visa);
        intent.putExtra("cash", credit.cash);
        intent.putExtra("ya", credit.yandex);
        intent.putExtra("master", credit.mastercard);
        intent.putExtra("mir", credit.mir);
        context.startActivity(intent);
    }

}
