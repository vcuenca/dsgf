package es.uv.androidchat;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import es.uv.androidchat.JavaObjects.Config;
import es.uv.androidchat.JavaObjects.GestorDB;
import main.java.Mensaje;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.internal.ms;
import com.google.gson.Gson;

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
                //Esto es una librería de google que transforma Json en objetos
                Gson gson=new Gson();
                //Le pasamos el String que recibimos(Json) y la clase a la que queramos que lo comparta.
                Mensaje obj2 = gson.fromJson(String.valueOf(extras.get(Config.MESSAGE_KEY)), Mensaje.class);

                if (!GestorDB.getInstance(this.getApplicationContext()).existeConversacion(obj2.getFrom())) {
                    GestorDB.getInstance(this.getApplicationContext()).iniciarConversacion(obj2.getFrom());
                    Log.d(Config.TAG, "Conversacion añadida");
                }

                int idConversacion = GestorDB.getInstance(this.getApplicationContext()).obtenerIdConversacion(obj2.getFrom());
                GestorDB.getInstance(this.getApplicationContext()).insertarMensaje(idConversacion, obj2);
                //Config.facade.sendConfirmation(obj2.getId());
                Log.d(Config.TAG, "Mensaje añadido");

                sendNotification(obj2.getFrom() + ": " + obj2.getMessage());
                Log.i(TAG, "Received: " + extras.toString());

                //Reseteamos los mensajes pendientes si se cumple la condicion

                if (Config.facade.conversacionActual != null && Config.facade.conversacionActual.getRemitente().equals(obj2.getFrom()))
                    GestorDB.getInstance(this.getApplicationContext()).resetMensajesPendientes(obj2.getFrom());

                if (Config.facade.conversacionActual != null) {
                    Config.facade.conversacionActual.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Config.facade.conversacionActual.cargarMensajes();
                        }
                    });
                }

            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        //Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);


        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), ContactsActivity.class), 0);

        //Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//        v.vibrate(5000);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.abc_btn_check_to_on_mtrl_000)
                .setContentTitle("Nuevo mensaje")
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