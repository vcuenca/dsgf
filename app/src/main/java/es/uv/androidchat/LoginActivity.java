package es.uv.androidchat;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import es.uv.androidchat.JavaObjects.ClientThread;
import es.uv.androidchat.JavaObjects.Config;
import es.uv.androidchat.JavaObjects.Conversation;
import es.uv.androidchat.JavaObjects.GestorDB;

import java.util.ArrayList;


public class LoginActivity extends Activity {

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Activity activity = this;
        final TextView tUsuario = (TextView)findViewById(R.id.usuario);
        final TextView tPassword = (TextView)findViewById(R.id.password);
        final Button botonAutenticar = (Button)findViewById(R.id.botonAutenticar);
        final TextView registerLabel = (TextView)findViewById(R.id.registerLabel);

        tUsuario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tUsuario.setText("");
            }
        });

        tPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               tPassword.setText("");
            }
        });

        botonAutenticar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usuario = "", password = "";

                usuario = tUsuario.getText().toString();
                password = tPassword.getText().toString();

                Log.d(Config.TAG, "hola");
                Config.user.setUser(usuario);
                Config.user.setPassword(password);

                GestorDB.getInstance(activity.getApplicationContext()).insertarPropiedad("user", usuario);
                GestorDB.getInstance(activity.getApplicationContext()).insertarPropiedad("pass", password);


                Log.d(Config.TAG, GestorDB.getInstance(activity.getApplicationContext()).obtenerPropiedad("usersaved"));

                ClientThread client = new ClientThread("10.0.2.2", 1235, tUsuario.getText().toString(), tPassword.getText().toString(), null, 0);

               client.start();
                Log.d(Config.TAG, "DESPUES DE CLIENT THREAD");

                try {
                    client.join();
                    if (client.isAuthenticated()){
                        GestorDB.getInstance(activity.getApplicationContext()).insertarPropiedad("usersaved", "s");
                        //fa
                        //Abrimos la lista de contactos, pantalla principal
                        Intent intent = new Intent(activity, MainActivity.class);
                        //intent.putExtra(EXTRA_MESSAGE, message);
                        //Obtenemos las conversaciones pendientes
                        ArrayList<Conversation> conversations = client.getMessagesFromLocal();
                        if (conversations == null){
                            Log.i("HOLA", null);
                        }



                        Bundle informacion = new Bundle();
                        informacion.putSerializable("conversations", conversations);
                        intent.putExtras(informacion);
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        registerLabel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
