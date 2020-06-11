package ru.zelenbiruz.zmonl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yandex.metrica.push.firebase.MetricaMessagingService;

import org.jetbrains.annotations.NotNull;
import ru.zelenbiruz.zmonl.Service.MessageResolver;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MessageReceiver extends FirebaseMessagingService {
    public static final String TAG = "myLog";
    public static final String CHANNEL_ID = "my_channel_01";

    private MessageResolver messageResolver = new MessageResolver();
    PendingIntent pi;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.w(TAG, "onNewToken: " + s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {

        Log.i("Recieved remote message", remoteMessage.toString());

        FirebaseInstanceId.getInstance().getToken();

        new MetricaMessagingService().processPush(this, remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String url = remoteMessage.getData().get("url");
        String imageUri = remoteMessage.getData().get("image");
        Bitmap bitmap = getBitmapfromUrl(imageUri);

        pi = this.messageResolver.resolve(this, url);

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
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(sound);
        mBuilder.setSmallIcon(R.drawable.icon);
        mBuilder.setContentIntent(pi);
        notificationManager.notify(String.valueOf(Long.toString(System.currentTimeMillis())), 1, mBuilder.build());


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

