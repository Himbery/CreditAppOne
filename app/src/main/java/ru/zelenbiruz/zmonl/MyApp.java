package ru.zelenbiruz.zmonl;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;
import com.yandex.metrica.push.YandexMetricaPush;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(getString(R.string.api_key)).build();
        YandexMetrica.activate(this, config);
        YandexMetrica.enableActivityAutoTracking(this);
        YandexMetricaPush.init(this);

    }
}
