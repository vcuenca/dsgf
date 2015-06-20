package es.uv.androidchat;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
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
import main.java.Conversation;
import main.java.Mensaje;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.internal.ms;
import com.google.gson.Gson;

public class GCMNotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;


    int icono_v=R.drawable.gcmlogo;
    int icono_r=R.drawable.gcmlogo;


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
                sendNotification("Send error: " + extras.toString(),0,"");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString(),0,"");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                //Esto es una librería de google que transforma Json en objetos
                Gson gson=new Gson();
                //Le pasamos el String que recibimos(Json) y la clase a la que queramos que lo comparta.


                /// GUARDAR MENSAJE EN LA BBDD :)
                Mensaje obj2 = gson.fromJson(String.valueOf(extras.get(Config.MESSAGE_KEY)), Mensaje.class);

                //Miramos si existe conversación , y si no la creamos.
                if (!GestorDB.getInstance(this.getApplicationContext()).existeConversacion(obj2.getFrom())) {
                    GestorDB.getInstance(this.getApplicationContext()).iniciarConversacion(obj2.getFrom());
                    Log.d(Config.TAG, "Conversacion añadida");
                }

                int idConversacion = GestorDB.getInstance(this.getApplicationContext()).obtenerIdConversacion(obj2.getFrom());

                //Insertamos el mensaje en la conversación
                GestorDB.getInstance(this.getApplicationContext()).insertarMensaje(idConversacion, obj2);
                //Config.facade.sendConfirmation(obj2.getId());
                Log.d(Config.TAG, "Mensaje añadido");

               sendNotification(obj2.getMessage(), icono_r, obj2.getFrom());

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
                int maxId = 0;

                //Obtenemos el id de la conversacion


                        maxId = obj2.getId();

                Config.facade.sendConfirmation(maxId);
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }







    private void sendNotification(String msg,int icono,String remitente) {
        //Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

    Intent i=new Intent(getApplicationContext(), ConversationActivity.class);
        i.putExtra("remitente",remitente);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                i, 0);


        //Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//        v.vibrate(5000);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(icono)
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