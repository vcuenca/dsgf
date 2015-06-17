package es.uv.androidchat;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import es.uv.androidchat.JavaObjects.Config;
import es.uv.androidchat.JavaObjects.GestorDB;
import main.java.Mensaje;

import java.util.ArrayList;
import java.util.Date;

import static es.uv.androidchat.R.id.editText2;


public class ConversationActivity extends ActionBarActivity {

    private EditText conversacion = null;
    private String remitente = "";
    private Button botonEnviar = null;
    private EditText tEnvio = null;
    final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        conversacion = (EditText) findViewById(editText2);
        botonEnviar = (Button)findViewById(R.id.botonEnviar);
        tEnvio = (EditText)findViewById(R.id.tEnvio);

        botonEnviar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Config.facade.sendText(remitente, tEnvio.getText().toString());
                GestorDB.getInstance(activity.getApplicationContext()).insertarMensaje("emisor", remitente, tEnvio.getText().toString(), new Date());
                conversacion.setText(conversacion.getText().toString() + "\n" + tEnvio.getText().toString());
                tEnvio.setText("");
            }
        });


        Bundle bundle = getIntent().getExtras();
        remitente = bundle.getString("remitente");
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
        ArrayList<Mensaje> mensajes = null;
        mensajes = GestorDB.getInstance(getApplicationContext()).obtenerMensajes(remitente);
        String texto = "";

        for (Mensaje mensaje : mensajes) {
            texto += mensaje.getMessage() + "\n";
        }

        conversacion.setText(texto);
    }
}
