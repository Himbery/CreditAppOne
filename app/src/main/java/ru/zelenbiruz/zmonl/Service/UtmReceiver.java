package ru.zelenbiruz.zmonl.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.analytics.CampaignTrackingReceiver;

public class UtmReceiver  extends CampaignTrackingReceiver {
    String utm_source,utm_medium,utm_term,utm_content,utm_campaign;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle extra = intent.getExtras();
            if(extra!=null) {
                String refer= extra.getString("referrer");
                if(refer!=null){
                    String referrer=refer.substring(0,refer.length());
                    String referSet[]=referrer.split("&");
                    if(referSet.length>0){
                        for(String item:referSet){
                            String itemSet[]=item.split("=");
                            if(itemSet.length==2){
                                String key=itemSet[0];
                                String val=itemSet[1];
                                if(key!=null){
                                    switch (key){
                                        case "utm_source":
                                            utm_source=val;
                                            break;
                                        case "utm_medium":
                                            utm_medium=val;
                                            break;
                                        case "utm_term":
                                            utm_term=val;
                                            break;
                                        case "utm_content":
                                            utm_content=val;
                                            break;
                                        case "utm_campaign":
                                            utm_campaign=val;
                                            break;
                                    }

                                    //Saving values in shared preference
                                    //for fetching later
                                    saveUtmValues(context,key,val);
                                }
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void saveUtmValues(Context context, String key, String val){
        try{
            SharedPreferences preference = context.getSharedPreferences("utm_campaign", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preference.edit();
            editor.putString(key, val);
            editor.apply();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
