package es.uv.androidchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import es.uv.androidchat.JavaObjects.ClientThread;
import es.uv.androidchat.JavaObjects.Config;
import es.uv.androidchat.JavaObjects.Conversation;
import es.uv.androidchat.JavaObjects.GestorDB;
import es.uv.androidchat.JavaObjects.Mensaje;
import es.uv.androidchat.JavaObjects.StableArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {

    private static ClientThread clienteThread = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GestorDB.getInstance(getApplicationContext());
        setContentView(R.layout.activity_register);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());



            //ClientThread c = new ClientThread("10.0.2.2", 1234, imei, ip);
        //c.start();
        ListView listaContactos = (ListView)findViewById(R.id.listView);

        final ArrayList<String> list = new ArrayList<String>();

       // Obtenemos las conversaciones
       ArrayList<Conversation> conversations = (ArrayList<Conversation>) getIntent().getSerializableExtra("conversations");

        //ArrayList<Mensaje> messages = conversations.get


        for (int i = 0; i < conversations.size(); ++i) {
            list.add(conversations.get(i).getEmisor());
            Log.i(Config.TAG,conversations.get(i).getEmisor());

        }
       final ArrayAdapter adapter = new StableArrayAdapter(this,
               android.R.layout.simple_list_item_1, list);
        listaContactos.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
