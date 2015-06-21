package es.uv.androidchat;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import es.uv.androidchat.JavaObjects.Config;
import es.uv.androidchat.JavaObjects.GestorDB;
import es.uv.androidchat.JavaObjects.RegisterGCM;

/**
 * Created by Vic on 20/06/2015.
 */
public class IpActivity extends ActionBarActivity
{
    private Context context;
    private TextView ip = null;
    private IpActivity activity = null;
    boolean existe=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ip);
        String val=GestorDB.getInstance(getApplicationContext()).obtenerPropiedad("IP_SERVER");
        ip = (TextView) findViewById(R.id.ip);

        Log.d(Config.TAG, "IP: " +val);

        if(val.equals("")){
            ip.setText(Config.IP_SERVER);

        }else{
            ip.setText(val);
            existe=true;
        }

        activity = this;


        final Button guardar = (Button) findViewById(R.id.guardarButton);
        context = this.getApplicationContext();

        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip.setText("");
            }
        });



        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.IP_SERVER=ip.getText().toString();
                if(existe){
                    GestorDB.getInstance(getApplicationContext()).actualizarPropiedad("IP_SERVER", ip.getText().toString());

                }else{
                    GestorDB.getInstance(getApplicationContext()).insertarPropiedad("IP_SERVER", ip.getText().toString());
                }
                Intent intent = new Intent(activity, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
