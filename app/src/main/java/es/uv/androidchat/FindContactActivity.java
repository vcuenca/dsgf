package es.uv.androidchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import es.uv.androidchat.JavaObjects.Config;
import es.uv.androidchat.JavaObjects.GestorDB;
import es.uv.androidchat.JavaObjects.StableArrayAdapter;

import java.util.ArrayList;


public class FindContactActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_contact);

        final Context context = this.getApplicationContext();
        final Button botonBuscar = (Button)findViewById(R.id.button);
        final EditText eText = (EditText)findViewById(R.id.editText3);
        final ListView contactList = (ListView)findViewById(R.id.listView2);
        final Activity activity = this;
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> result = Config.facade.getContacts(eText.getText().toString());

                if (result != null) {
                    final ArrayAdapter adapter = new StableArrayAdapter(context, R.layout.list_item_layout, result);
                    contactList.setAdapter(adapter);
                }
            }
        });

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ConversationActivity.class);
                String userName = (String)contactList.getItemAtPosition(position);
                GestorDB.getInstance(activity.getApplicationContext()).iniciarConversacion(userName);
                intent.putExtra("remitente", userName);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_contact, menu);
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
