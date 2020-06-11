package ru.zelenbiruz.zmonl.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;


public class ReferrerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action != null && TextUtils.equals(action, "com.android.vending.INSTALL_REFERRER")) {
            try {
                final String referrer = intent.getStringExtra("referrer");

                // Parse parameters
                String[] params = referrer.split("&");
                for (String p : params) {
                    if (p.startsWith("utm_content=")) {
                        final String content = p.substring("utm_content=".length());

                        /**
                         * USE HERE YOUR CONTENT (i.e. configure the app based on the link the user clicked)
                         */
                        Log.i("ReferrerReceiver", content);

                        break;
                    }
                    if (p.startsWith("utm_source=")){
                        final String source = p.substring("utm_source=".length());
                        Log.i("ReferrerReceiver", source);
                    }
                }
                Map<String, String> getParams = getMap(referrer);
                String sourc0e = getParams.get("utm_campaign");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    private Map<String, String> getMap(String paramString) {
        String[] arrayOfString1 = paramString.split("&");
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        for (String pair : arrayOfString1) {
            String[] entry = pair.split("=");
            if (entry.length != 2) {
                break;
            }
            localHashMap.put(entry[0], entry[1]);
        }
        return localHashMap;
    }
}

