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
import org.jetbrains.annotations.NotNull;
import ru.zelenbiruz.zmonl.Model.ConfigLoan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

class LoansAdapter extends RecyclerView.Adapter<LoansAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<ConfigLoan> loans = new ArrayList<>();

    LoansAdapter(Context context, List<ConfigLoan> loans) {
        this.loans = loans;
        this.inflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public LoansAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final LoansAdapter.ViewHolder holder, int position) {

        final ConfigLoan loan = loans.get(position);
        Glide.with(holder.itemView.getContext()).load(loan.screen).into(holder.imageView);
        if (loan.name!= null){
            holder.nameView.setText(loan.name);
        }
        if (!loan.termMin.isEmpty() || loan.termMin != null || loan.termMin != "" ){
            holder.termsView.setText("Срок " + loan.termPrefix + " " +loan.termMin + " " +loan.termMid + " " +loan.termMax + loan.termPostfix);
        } else {
            holder.termsView.setVisibility(View.GONE);
        }

        holder.sumView.setText("Сумма " + loan.summPrefix + " " + loan.summMin +" " + loan.summMid + " " +loan.summMax + loan.summPostfix);
        holder.stavkaView.setText("Ставка " + loan.percentPrefix + " " + loan.percent + " " + loan.percentPostfix);
        holder.rating.setRating(loan.getScore());
        if (loan.qiwi.isEmpty()){
            holder.qiwi.setVisibility(View.GONE);
        }
        if (loan.mastercard.isEmpty()){
            holder.master.setVisibility(View.GONE);
        }
        if (loan.mir.isEmpty()){
            holder.mir.setVisibility(View.GONE);
        }
        if (loan.visa.isEmpty()){
            holder.visa.setVisibility(View.GONE);
        }
        if (loan.yandex.isEmpty()){
            holder.ya.setVisibility(View.GONE);
        }
        if (loan.cash.isEmpty()){
            holder.cash.setVisibility(View.GONE);
        }
        final Map<String, Object> external_link = new HashMap<String, Object>();
        String offer_id = loan.itemId + " " + loan.name;
        external_link.put("offer_id", offer_id);

        holder.detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                openDetailsActivity(context, loan);
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

                String url = loan.order + "&aff_sub1=" + sub1 + "&aff_sub2=" + sub2 + "&aff_sub3=" + sub3 + "&aff_sub4=" + aID
                        + "&aff_sub5=" + gaid;

                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                Bundle bundle = new Bundle();
                bundle.putString("external_link", "external_link");
                YandexMetrica.reportEvent("offer_id", external_link);
                mFirebaseAnalytics.logEvent("external_link", bundle);
                if (loan.browserType.equals("external")){
                    if (isOnline()) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(myIntent);
                    } else {
                        Intent myIntent = new Intent(context, ErrorActivity.class);
                        context.startActivity(myIntent);
                    }
                } else {

                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("order", url);
                        intent.putExtra("browser", loan.browserType);
                        intent.putExtra("name", loan.name);
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
                openDetailsActivity(context, loan);
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
        return loans.size();
    }

    public void openDetailsActivity(Context context, ConfigLoan loan) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("itemId", loan.itemId);
        intent.putExtra("icon", loan.screen);
        intent.putExtra("description", loan.description);
        intent.putExtra("txt_btn", loan.orderButtonText);
        intent.putExtra("name", loan.name);
        intent.putExtra("order", loan.order);
        intent.putExtra("browserType", loan.browserType);
        intent.putExtra("qiwi", loan.qiwi);
        intent.putExtra("visa", loan.visa);
        intent.putExtra("cash", loan.cash);
        intent.putExtra("ya", loan.yandex);
        intent.putExtra("master", loan.mastercard);
        intent.putExtra("mir", loan.mir);
        context.startActivity(intent);
    }
}
