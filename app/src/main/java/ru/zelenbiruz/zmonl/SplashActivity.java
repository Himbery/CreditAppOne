package ru.zelenbiruz.zmonl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import ru.zelenbiruz.zmonl.Model.ConfigApp;
import ru.zelenbiruz.zmonl.Model.DataItem;

import ru.zelenbiruz.zmonl.Service.MessageResolver;
import ru.zelenbiruz.zmonl.Service.MyApi;
import ru.zelenbiruz.zmonl.Service.ReferrerReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity{

    ProgressBar pBar;
    private static final String TAG = "myLogs";
    String visibleLoans;
    String visibleCredits;
    String visibleCard;
    SharedPreferences preferences;
    String CountryID;
    String dateNow;
    String dateJson;
    InstallReferrerClient referrerClient;
    String aff_sub1, sub1;
    String aff_sub2, sub2;
    String aff_sub3, sub3;
    String aff_sub4;
    String aff_sub5;
    String androidID;
    String advertId;
    String urlIns;
    String gaid;
    ReferrerReceiver rr;
    MessageResolver messageReceiver = new MessageResolver();
    public static final String CHANNEL_ID = "my_channel_01";
    String utmValues;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("sentFriendRequest");
        AppLinkData.fetchDeferredAppLinkData(this, getString(R.string.facebook_app_id),
                new AppLinkData.CompletionHandler() {
                    @Override
                    public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                        if (appLinkData != null) {
                            Bundle bundle = appLinkData.getArgumentBundle();
                            Log.i("DEBUG_FACEBOOK_SDK", bundle.toString());
                        } else {
                            Log.i("DEBUG_FACEBOOK_SDK", "AppLinkData is Null");
                        }
                    }
                });

        getCountryZipCode();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                    }
                });
        preferences = getSharedPreferences("DATA_PIN" , MODE_PRIVATE);

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                advertId = null;
                try{
                    advertId = idInfo.getId();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                return advertId;
            }

            @Override
            protected void onPostExecute(String advertId) {
                preferences = getSharedPreferences("DATA_PIN" , MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("gaid", advertId);
                editor.apply();
//                Toast.makeText(getApplicationContext(), advertId, Toast.LENGTH_LONG).show();
            }

        };
        task.execute();

        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("androidID", androidID);
        editor.apply();

        referrerClient = InstallReferrerClient.newBuilder (this).build ();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished (int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:// Соединение установлено.
                        try {
                            ReferrerDetails response = referrerClient.getInstallReferrer();
                            String referrerUrl = response.getInstallReferrer();
                            long referrerClickTime = response.getReferrerClickTimestampSeconds();
                            long appInstallTime = response.getInstallBeginTimestampSeconds();
                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();


                            if (referrerUrl.contains("gclid")){
                                aff_sub1 = "google";
                                aff_sub2 = "cpc";
                                aff_sub3 = "gclid";
                                preferences = getSharedPreferences("DATA_PIN" , MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("sub1", aff_sub1);
                                editor.putString("sub2", aff_sub2);
                                editor.putString("sub3", aff_sub3);
                                editor.apply();
                            } else if (!referrerUrl.contains("gclid")){

                                String[] params = referrerUrl.split("&");
                                for (String p : params) {
                                    if (p.startsWith("utm_content=")) {
                                        final String content = p.substring("utm_content=".length());
                                        Log.i("ReferrerReceiver", content);
                                        break;
                                    }
                                    if (p.startsWith("utm_source=")){
                                        aff_sub1 = p.substring("utm_source=".length());
                                        Log.i("ReferrerReceiver", aff_sub1);
                                    }
                                    if (p.startsWith("utm_campaign=")){
                                        aff_sub2 = p.substring("utm_campaign=".length());
                                        Log.i("ReferrerReceiver", aff_sub2);
                                    }
                                }
                                aff_sub3 = "";
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("sub1", aff_sub1);
                                editor.putString("sub2", aff_sub2);
                                editor.putString("sub3", aff_sub3);
                                editor.apply();

                            }

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        break;
                }
            }
            @Override
            public void onInstallReferrerServiceDisconnected() {
            }
        });


        pBar = findViewById(R.id.progressbar);
        if (isOnline() && (CountryID.equals("RU")|| CountryID.equals("UA"))) {

            if (getIntent().getExtras() == null) {
                getResult();
            } else {
                for (String key : getIntent().getExtras().keySet()) {
                    Object value = getIntent().getExtras().get(key);
                }
                String title = getIntent().getExtras().getString("title");
                String body = getIntent().getExtras().getString("body");
                String url = getIntent().getExtras().getString("url");
                String imageUri = getIntent().getExtras().getString("image");
                Bitmap bitmap = getBitmapfromUrl(imageUri);

                if (url != null) {
                Uri uri = Uri.parse(url);
                Path path = Paths.get(uri.getPath());
                String parameter = path.getFileName().toString();
                String screenName = path.getName(path.getNameCount() - 2).toString();
                if (screenName.equals("credits")) {
                    Intent intentOpen = new Intent(this, TwoMainActivity.class);
                    intentOpen.putExtra("id", parameter);
                    startActivity(intentOpen);
                    finish();
                } else if (screenName.equals("cards_debit")) {
                    Intent intentOpen = new Intent(this, ThreeMainActivity.class);
                    intentOpen.putExtra("id", parameter);
                    intentOpen.putExtra("item", screenName);
                    startActivity(intentOpen);
                    finish();
                } else if (screenName.equals("cards_installment")) {
                    Intent intentOpen = new Intent(this, ThreeMainActivity.class);
                    intentOpen.putExtra("id", parameter);
                    intentOpen.putExtra("item", screenName);
                    startActivity(intentOpen);
                    finish();
                } else if (screenName.equals("card_credits")) {
                    Intent intentOpen = new Intent(this, ThreeMainActivity.class);
                    intentOpen.putExtra("id", parameter);
                    intentOpen.putExtra("item", screenName);
                    startActivity(intentOpen);
                    finish();
                } else if (screenName.equals("loans")) {
                    Intent intentOpen = new Intent(this, MainActivity.class);
                    intentOpen.putExtra("id", parameter);
                    startActivity(intentOpen);
                    finish();
                } else if (screenName.equals("offer_item") && parameter.equals("credits")) {
                    Intent intentOpen = new Intent(this, TwoMainActivity.class);
                    startActivity(intentOpen);
                    finish();
                } else if (screenName.equals("offer_item") && parameter.equals("loans")) {
                    Intent intentOpen = new Intent(this, MainActivity.class);
                    startActivity(intentOpen);
                    finish();
                } else if (screenName.equals("offer_item") && parameter.equals("cards")) {
                    Intent intentOpen = new Intent(this, ThreeMainActivity.class);
                    startActivity(intentOpen);
                    finish();
                }

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, title, NotificationManager.IMPORTANCE_HIGH);
                    mChannel.setDescription("EZ");
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notificationManager.createNotificationChannel(mChannel);
                    mBuilder.setChannelId(CHANNEL_ID);
                }
                mBuilder.setContentTitle(title);
                mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
                mBuilder.setContentText(body);
                mBuilder.setAutoCancel(true);
                mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                mBuilder.setSmallIcon(R.drawable.icon);
                notificationManager.notify(String.valueOf(Long.toString(System.currentTimeMillis())), 1, mBuilder.build());

                } else {
                    getResult();
                }
            }
        } else if (!isOnline()){
            Intent intent = new Intent(SplashActivity.this, ErrorActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, TermsOfUseActivity.class);
            intent.putExtra("pin", "0");
            intent.putExtra("loans_item", visibleLoans);
            intent.putExtra("card_item", visibleCard);
            intent.putExtra("credit_item", visibleCredits);
            intent.putExtra("url_install", urlIns);
            startActivity(intent);
        }
    }

    public void getResult() {
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
                String initL = dataItem.initLicenseTerm;
                List cardsList = dataItem.cards;
                List creditList = dataItem.credits;
                List debitList = dataItem.cardsDebit;
                List loansList = dataItem.loans;
                List creditCardsList = dataItem.cardsCredit;
                List cardsInstallList = dataItem.cardsInstallment;
                ConfigApp app = dataItem.appConfig;
                String agree = dataItem.hideInitAgreement;
                String showDocs = dataItem.showDocs;
                String terms = dataItem.licenseTerm;
                visibleLoans = app.loans_item;
                visibleCard = app.cards_item;
                visibleCredits = app.credits_item;

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("terms", terms);
                editor.apply();

                if (agree.equals("0")) {
                    Intent intent = new Intent(SplashActivity.this, TermsOfUseActivity.class);
                    intent.putExtra("license", initL);
                    intent.putExtra("terms", terms);
                    intent.putExtra("pin", app.hide_order_offer);
                    intent.putExtra("loans_item", visibleLoans);
                    intent.putExtra("card_item", visibleCard);
                    intent.putExtra("credit_item", visibleCredits);
                    intent.putExtra("date", dateJson);
                    intent.putExtra("date_now", dateNow);
                    intent.putExtra("re", utmValues);
                    intent.putExtra("url_install", urlIns);
                    startActivity(intent);

                    finish();

                } else if (agree.equals("1")){
                    if (visibleLoans.equals("1")) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("card_item", visibleCard);
                            intent.putExtra("credit_item", visibleCredits);
                            intent.putExtra("loans_item", visibleLoans);
                            intent.putExtra("date", dateJson);
                            intent.putExtra("date_now", dateNow);
                            intent.putExtra("url_install", urlIns);
                            intent.putExtra("terms", terms);
                            startActivity(intent);

                            finish();

                    } else if(visibleLoans.equals("0") ){
                        Intent intent = new Intent(SplashActivity.this, ThreeMainActivity.class);
                        intent.putExtra("loans_item", visibleLoans);
                        intent.putExtra("credit_item", visibleCredits);
                        intent.putExtra("card_item", visibleCard);
                        intent.putExtra("date", dateJson);
                        intent.putExtra("date_now", dateNow);
                        intent.putExtra("url_install", urlIns);
                        startActivity(intent);

                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataItem> call, Throwable t) {
                Log.d( TAG,"failure " + t);
            }
        });
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        int exitValue = -1;

        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            exitValue = ipProcess.waitFor();
        }
        catch (IOException | InterruptedException ignored) {}

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && exitValue == 0 && netInfo.isConnectedOrConnecting();
    }

    private String getCountryZipCode() {

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        CountryID = manager.getSimCountryIso().toUpperCase();
        return CountryID;
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }
}
