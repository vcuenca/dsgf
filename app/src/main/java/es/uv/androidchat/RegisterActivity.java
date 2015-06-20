package es.uv.androidchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import es.uv.androidchat.JavaObjects.ClientThread;
import es.uv.androidchat.JavaObjects.Config;
import es.uv.androidchat.JavaObjects.GestorDB;
import es.uv.androidchat.JavaObjects.RegisterGCM;


public class RegisterActivity extends ActionBarActivity {

    private Context context;
    private TextView correo = null, password = null;
    private RegisterActivity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        activity = this;
        correo = (TextView) findViewById(R.id.correo);
        password = (TextView) findViewById(R.id.password);
        final Button registerButton = (Button) findViewById(R.id.registerButton);
        context = this.getApplicationContext();

        correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo.setText("");
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Registramos el correo en el servidor
                RegisterGCM regGCM = new RegisterGCM(context, activity);
                regGCM.execute(null, null, null);
            }
        });


    }

    public void registerUser(String cloudID) {
        String user = correo.getText().toString();
        String pass = password.getText().toString();

        Log.d(Config.TAG, "USUARIO: " + user);
        Log.d(Config.TAG, "PASSWORD: " + pass);


        ArrayList<Object> params = new ArrayList<Object>();
        params.add(cloudID);
        String ip=GestorDB.getInstance(getApplicationContext()).obtenerPropiedad("IP_SERVER");

        ClientThread client = new ClientThread(Config.IP_SERVER, Config.PORT ,user, pass, params, 1);
        client.start();
        try {
            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GestorDB.getInstance(this.getApplicationContext()).insertarPropiedad("user", user);
        GestorDB.getInstance(this.getApplicationContext()).insertarPropiedad("pass", pass);

        Log.d(Config.TAG, "USUARIO: " + GestorDB.getInstance(this.getApplicationContext()).obtenerPropiedad("user"));
        Log.d(Config.TAG, "PASS: " + GestorDB.getInstance(this.getApplicationContext()).obtenerPropiedad("pass"));

        Config.user.setUser(GestorDB.getInstance(this.getApplicationContext()).obtenerPropiedad("user"));
        Config.user.setPassword(GestorDB.getInstance(this.getApplicationContext()).obtenerPropiedad("pass"));
        //if (ConnectionManager.getClientThread().isRegistered()){
        Intent intent = new Intent(activity, ContactsActivity.class);
        startActivity(intent);
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
            Intent intent = new Intent(activity, IpActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
