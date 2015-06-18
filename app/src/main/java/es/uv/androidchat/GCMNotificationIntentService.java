package es.uv.androidchat;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import es.uv.androidchat.JavaObjects.Config;
import es.uv.androidchat.JavaObjects.Conversation;
import es.uv.androidchat.JavaObjects.GestorDB;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;

public class GCMNotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "TAG";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        Log.d(Config.TAG, "RECIBIENDO");

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR   //Esto estaba aquÃ­ , lo dejo por filtrado ,por si hay problemas luego con GCM.
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                //Se supone que este es el caso en el que no habrÃ­a problemas en la recepciÃ³n del mensaje, con lo que deberemos
                //realizar una consulta al servidor para recuperar los mensajes y guardarlos en la BD.

                //Aqui recuperamos los mensajes del servidor

                //ArrayList<Conversation> conversaciones= (ArrayList<Conversation>) extras.get(Config.MESSAGE_KEY);

                //GestorDB.getInstance(getApplicationContext()).insertarConversaciones(conversaciones);

                //Mostramos la notificación .

                sendNotification("Tiene un nuevo mensaje de "+ "ivan" + "contactos");
                Log.i(TAG, "Received: " + extras.toString());
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        //Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class), 0);

        //Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//        v.vibrate(5000);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.abc_btn_check_to_on_mtrl_000)
                .setContentTitle("GCM Notification")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setLights(Color.RED, 1, 2)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setSubText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());

        //Log.d(TAG, "Notification sent successfully.");
    }
}