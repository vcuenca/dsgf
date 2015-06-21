package es.uv.androidchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import es.uv.androidchat.JavaObjects.Config;
import main.java.Conversation;
import es.uv.androidchat.JavaObjects.GestorDB;
import main.java.Mensaje;

import java.util.ArrayList;


public class ContactsActivity extends Activity {
    private ListView contacts = null;
    private ArrayList<String> conv = null;
    private ArrayAdapter<String> cnv = null;
    private String remitente = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Config.TAG, "Hola");
        boolean usuarioRegistrado = true;

        /*
         * Antes de continuar se comprueba si tenemos conexión a internet,
         * y en caso contrario se redirige a ajustes del sistema
         * para activar una conexión.
         *
         * Creo que si una vez te sale la pagina de ajustes le das a atrás la comprobación tre la saltas,
         * igual sería mejor poner un while...
         *
         *  También sería interesante comprobar si podemos conectarnos con el servidor.
         */

        ConnectivityManager conMan = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State internet_movil = conMan.getNetworkInfo(0).getState();
        NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();

        if (!(((internet_movil != NetworkInfo.State.CONNECTED) || (internet_movil != NetworkInfo.State.CONNECTING))
                ||
                ((wifi != NetworkInfo.State.CONNECTED) || (wifi != NetworkInfo.State.CONNECTING)))) {

            Toast toast = Toast.makeText(getApplicationContext(), "No tiene conexión a internet activada, por favor actívela.", Toast.LENGTH_LONG);
            toast.show();
            startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
        }
        else{
            Log.d(Config.TAG, "Tienes internet");
        }

        if (GestorDB.getInstance(this.getApplicationContext()).obtenerPropiedad("user").equals("")) {
            Log.d(Config.TAG, "HolaHOLAHOLA");


            Log.d(Config.TAG, "USUARIO: " + Config.user.getUser());

            usuarioRegistrado = false;
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }else{
            Config.user.setUser(GestorDB.getInstance(this.getApplicationContext()).obtenerPropiedad("user"));
            Config.user.setPassword(GestorDB.getInstance(this.getApplicationContext()).obtenerPropiedad("pass"));
        }
        String val=GestorDB.getInstance(getApplicationContext()).obtenerPropiedad("IP_SERVER");

        if(!val.equals(""))
         Config.IP_SERVER=val;

        Log.d(Config.TAG, "PEPE");
        setContentView(R.layout.activity_contacts);
        final Activity activity = this;
        contacts = (ListView)findViewById(R.id.listView);
        ImageView iv = (ImageView)findViewById(R.id.imageView);


        //---------//

        if (usuarioRegistrado)
            cargarConversaciones();

        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FindContactActivity.class);
                startActivity(intent);
            }
        });

        contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ConversationActivity.class);
                remitente = (String) contacts.getItemAtPosition(position);
                Log.i("CC", "El remitente de la conversación seleccionada es: " + remitente);
                intent.putExtra("remitente",remitente);
                startActivity(intent);
            }
        });


    }

    private void cargarConversaciones() {
        Log.i("CI", "Cargo la info");
        ArrayList<Conversation> conversaciones = Config.facade.getMessages();

        //Insertamos las conversaciones en la BD
        if (conversaciones != null) {
            for (Conversation c : conversaciones) {
                //Miramos si ya existe la conversacion, de ser asi solo añadimos los mensajes, si no existe la creamos
                String remitente = c.getUser();
                int maxId = 0;

                if (!GestorDB.getInstance(this.getApplicationContext()).existeConversacion(remitente)) {
                    GestorDB.getInstance(this.getApplicationContext()).iniciarConversacion(remitente);
                }

                //Obtenemos el id de la conversacion
                int idConversacion = GestorDB.getInstance(this.getApplicationContext()).obtenerIdConversacion(remitente);

                for (Mensaje m : c.getMessages()) {
                    Log.d(Config.TAG, "ID MENSAJE:" + m.getId() + "");

                    if (m.getId() > maxId)
                        maxId = m.getId();

                    GestorDB.getInstance(this.getApplicationContext()).insertarMensaje(idConversacion, m);
                }

                Config.facade.sendConfirmation(maxId);
            }
        }

        conv = GestorDB.getInstance(getApplicationContext()).obtenerConversaciones();

        cnv = new ContactArrayAdapter(this, conv);

        Log.d(Config.TAG, "Hay:" + conv.size() + "conversaciones");

        contacts.setAdapter(cnv);
    }


    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        conv = GestorDB.getInstance(getApplicationContext()).obtenerConversaciones();

        cnv = new ContactArrayAdapter(this, conv);

        Log.d(Config.TAG, "Hay:" + conv.size() + "conversaciones");

        contacts.setAdapter(cnv);

        Config.facade.conversacionActual = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
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

    public void addConversation(String conversation){
        conv.add(conversation);
        cnv.notifyDataSetChanged();
    }
}
