package es.uv.androidchat;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import es.uv.androidchat.JavaObjects.Config;
import es.uv.androidchat.JavaObjects.GestorDB;
import es.uv.androidchat.JavaObjects.StableArrayAdapter;
import main.java.Mensaje;

import java.util.ArrayList;
import java.util.Date;

import static es.uv.androidchat.R.id.listView3;


public class ConversationActivity extends ActionBarActivity {

    private ListView conversacion = null;
    private String remitente = "";
    private Button botonEnviar = null;
    private EditText tEnvio = null;
    final Activity activity = this;
    private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        conversacion = (ListView) findViewById(listView3);
        botonEnviar = (Button)findViewById(R.id.botonEnviar);
        tEnvio = (EditText)findViewById(R.id.tEnvio);
        TextView tituloRemitente = (TextView) findViewById(R.id.titRemitente);
        //Creamos el arraylist que contendrá los mensajes

        Config.facade.conversacionActual = (ConversationActivity)activity;

        botonEnviar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Config.facade.sendText(remitente, tEnvio.getText().toString());

                int idConversacion = GestorDB.getInstance(activity.getApplicationContext()).obtenerIdConversacion(remitente);
                Mensaje m = new Mensaje();
                m.setMessage(tEnvio.getText().toString());
                m.setFecha(String.valueOf(new Date()));
                m.setReceiver(remitente);
                m.setFrom(Config.user.getUser());
                Log.d(Config.TAG, "Mensaje insertado desde: " + m.getFrom());
                GestorDB.getInstance(activity.getApplicationContext()).insertarMensaje(idConversacion, m);
                tEnvio.setText("");
                mensajes.add(m);
                conversacion.setSelection(conversacion.getAdapter().getCount()-1);
            }
        });


        Bundle bundle = getIntent().getExtras();
        remitente = bundle.getString("remitente");
        tituloRemitente.setText(remitente);
        cargarMensajes();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conversation, menu);
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

    public void cargarMensajes(){
        mensajes.clear();
        mensajes = GestorDB.getInstance(getApplicationContext()).obtenerMensajes(remitente);
        String texto = "";

        final ConversationArrayAdapter adapter = new ConversationArrayAdapter(this.getApplicationContext(), mensajes);
        conversacion.setAdapter(adapter);
        conversacion.setSelection(conversacion.getAdapter().getCount()-1);
    }
}
