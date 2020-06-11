package ru.zelenbiruz.zmonl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.push.YandexMetricaPush;

public class SilentPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD);
        StringBuilder sb = new StringBuilder("Silent push received.");
        if (!TextUtils.isEmpty(payload)) {
            sb.append("\nPayload: ").append(payload);
        }
        YandexMetrica.reportEvent("Silent push");
        Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
    }
}
