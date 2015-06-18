package es.uv.androidchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import es.uv.androidchat.JavaObjects.Config;
import main.java.Conversation;
import es.uv.androidchat.JavaObjects.GestorDB;

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

        if (GestorDB.getInstance(this.getApplicationContext()).obtenerPropiedad("user").equals("")) {
            Log.d(Config.TAG, "Hola");
            Config.user.setUser(GestorDB.getInstance(this.getApplicationContext()).obtenerPropiedad("user"));
            Config.user.setPassword(GestorDB.getInstance(this.getApplicationContext()).obtenerPropiedad("pass"));
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        Log.d(Config.TAG, "PEPE");
        Log.d(Config.TAG, "PEPE");
        setContentView(R.layout.activity_contacts);
        final Activity activity = this;
        contacts = (ListView)findViewById(R.id.listView);
        ImageView iv = (ImageView)findViewById(R.id.imageView);
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
        /*
        contacts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ConversationActivity.class);
                remitente = (String) contacts.getSelectedItem();
                Log.i("CC", "El remitente de la conversación seleccionada es: " + remitente);
                intent.putExtra("remitente",remitente);
                startActivity(intent);
            }
        });*/

    }

    private void cargarConversaciones() {
        Log.i("CI", "Cargo la info");
        Config.facade.getMessages();


        conv = GestorDB.getInstance(getApplicationContext()).obtenerConversaciones();

        cnv = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, conv);

        Log.d(Config.TAG, "Hay:" + conv.size() + "conversaciones");

        contacts.setAdapter(cnv);
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
