package ru.zelenbiruz.zmonl.Service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import ru.zelenbiruz.zmonl.MainActivity;
import ru.zelenbiruz.zmonl.ThreeMainActivity;
import ru.zelenbiruz.zmonl.TwoMainActivity;
import ru.zelenbiruz.zmonl.WebActivity;

import java.nio.file.Path;
import java.nio.file.Paths;


public class MessageResolver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PendingIntent resolve(Context context, String url) {
        Uri uri = Uri.parse(url);
        Path path = Paths.get(uri.getPath());
        String parameter = path.getFileName().toString();
        String screenName = path.getName(path.getNameCount() - 2).toString();

        PendingIntent pendingIntent;

        if (screenName.equals("credits")) {
            Intent intentOpen = new Intent(context, TwoMainActivity.class);
            intentOpen.putExtra("id", parameter);
            pendingIntent = PendingIntent.getActivity(context, 0, intentOpen, PendingIntent.FLAG_ONE_SHOT);
        } else if (screenName.equals("cards_debit")) {
            Intent intentOpen = new Intent(context, ThreeMainActivity.class);
            intentOpen.putExtra("id", parameter);
            intentOpen.putExtra("item", screenName);
            pendingIntent = PendingIntent.getActivity(context, 0, intentOpen, PendingIntent.FLAG_ONE_SHOT);
        } else if (screenName.equals("cards_installment") ){
            Intent intentOpen = new Intent(context, ThreeMainActivity.class);
            intentOpen.putExtra("id", parameter);
            intentOpen.putExtra("item", screenName);
            pendingIntent = PendingIntent.getActivity(context, 0, intentOpen, PendingIntent.FLAG_ONE_SHOT);
        } else if (screenName.equals("card_credits")) {
            Intent intentOpen = new Intent(context, ThreeMainActivity.class);
            intentOpen.putExtra("id", parameter);
            intentOpen.putExtra("item", screenName);
            pendingIntent = PendingIntent.getActivity(context, 0, intentOpen, PendingIntent.FLAG_ONE_SHOT);
        } else if (screenName.equals("loans")) {
            Intent intentOpen = new Intent(context, MainActivity.class);
            intentOpen.putExtra("id", parameter);
            pendingIntent = PendingIntent.getActivity(context, 0, intentOpen, PendingIntent.FLAG_ONE_SHOT);
        } else if (screenName.equals("offer_item") && parameter.equals("credits")) {
            Intent intentOpen = new Intent(context, TwoMainActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intentOpen, PendingIntent.FLAG_ONE_SHOT);
        } else if (screenName.equals("offer_item") && parameter.equals("loans")) {
            Intent intentOpen = new Intent(context, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intentOpen, PendingIntent.FLAG_ONE_SHOT);
        } else if (screenName.equals("offer_item") && parameter.equals("cards")) {
            Intent intentOpen = new Intent(context, ThreeMainActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intentOpen, PendingIntent.FLAG_ONE_SHOT);

        } else {
            Intent intentOpen = new Intent(context, WebActivity.class);
            intentOpen.putExtra("url", url);
            intentOpen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intentOpen, PendingIntent.FLAG_ONE_SHOT);
        }

        return pendingIntent;

    }
}
