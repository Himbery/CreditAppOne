package ru.zelenbiruz.zmonl;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_activity);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isOnline()) {
            Intent intent = new Intent(ErrorActivity.this, SplashActivity.class);
            startActivity(intent);
        }
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

}
