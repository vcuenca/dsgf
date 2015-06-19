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
import main.java.Mensaje;

public class ConversationArrayAdapter extends ArrayAdapter<Mensaje> {

    private final Context context;
    private final ArrayList<Mensaje> mensajes;

    public ConversationArrayAdapter(Context context, ArrayList<Mensaje> mensajes) {
        super(context, R.layout.conversation_layout, mensajes);
        this.context = context;
        this.mensajes = mensajes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.conversation_layout, parent, false);
        TextView tFrom = (TextView) rowView.findViewById(R.id.tFrom);
        TextView tMessage = (TextView) rowView.findViewById(R.id.tMessage);
        TextView tFecha = (TextView) rowView.findViewById(R.id.tFecha);

        Log.d(Config.TAG, "LIST: " + mensajes.get(position).getFrom());
        Log.d(Config.TAG, "LIST: " + mensajes.get(position).getMessage());
        Log.d(Config.TAG, "LIST: " + mensajes.get(position).getFecha());

        Log.d(Config.TAG, "ESTO DEBERIA DAR NULL:" + mensajes.get(position).getFrom());

        //tFrom.setText(mensajes.get(position).getFrom());
        if (mensajes.get(position).getFrom().equals(Config.user.getUser()))
            tFrom.setText("Yo he dicho:");
        else
            tFrom.setText(mensajes.get(position).getFrom() + " ha dicho:");

        tMessage.setText(mensajes.get(position).getMessage());
        //tFecha.setText(mensajes.get(position).getFecha());
        tFecha.setText(mensajes.get(position).getFecha().substring(11,16));
        return rowView;
    }
}