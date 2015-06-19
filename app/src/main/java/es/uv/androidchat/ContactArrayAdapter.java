package es.uv.androidchat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import es.uv.androidchat.JavaObjects.Config;
import es.uv.androidchat.JavaObjects.GestorDB;
import main.java.Mensaje;

public class ContactArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> conversaciones;

    public ContactArrayAdapter(Context context, ArrayList<String> conversaciones) {
        super(context, R.layout.pending_messages, conversaciones);
        this.context = context;
        this.conversaciones = conversaciones;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.pending_messages, parent, false);
        TextView tConversacion = (TextView) rowView.findViewById(R.id.tConversacion);
        TextView tNumeroMensajes = (TextView) rowView.findViewById(R.id.tNumeroMensajes);

        tConversacion.setText(conversaciones.get(position));
        tNumeroMensajes.setText(GestorDB.getInstance(this.getContext()).obtenerMensajesPendientes(conversaciones.get(position)) + " mensajes pendientes sin leer");
        return rowView;
    }
}