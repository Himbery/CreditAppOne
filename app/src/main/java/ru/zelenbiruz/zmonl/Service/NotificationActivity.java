package ru.zelenbiruz.zmonl.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.messaging.RemoteMessage;

import ru.zelenbiruz.zmonl.R;

public class NotificationActivity extends AppCompatActivity {

    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            context.finish();
            return;
        }

        RemoteMessage msg = (RemoteMessage) extras.get("msg");

        if (msg == null) {
            context.finish();
            return;
        }

        RemoteMessage.Notification notification = msg.getNotification();

        if (notification == null) {
            context.finish();
            return;
        }

        String dialogMessage;
        try {
            dialogMessage = notification.getBody();
        } catch (Exception e){
            context.finish();
            return;
        }
        String dialogTitle = notification.getTitle();
        if (dialogTitle == null || dialogTitle.length() == 0) {
            dialogTitle = "";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_AppCompat_Dialog));
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);
        AlertDialog alert = builder.create();
        alert.show();

    }
}
