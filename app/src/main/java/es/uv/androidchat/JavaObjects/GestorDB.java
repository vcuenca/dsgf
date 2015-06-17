package es.uv.androidchat.JavaObjects;

/**
 * Created by Vic on 25/02/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class GestorDB {

    private QuotesReaderDbHelper helper = null;
    private SQLiteDatabase db = null;
    private static GestorDB gestorDB = null;


    private GestorDB(Context context) {
        helper = new QuotesReaderDbHelper(context);
    }


    public static GestorDB getInstance(Context context) {
        if (gestorDB == null) {
            gestorDB = new GestorDB(context);
        }
        return gestorDB;

    }

    public void insertarPropiedad(String parametro, String valor) {
        db = helper.getWritableDatabase();
        ContentValues nuevoReg = new ContentValues();
        nuevoReg.put("PARAMETRO", parametro);
        nuevoReg.put("VALOR", valor);
        db.insert("SYSTEM", null, nuevoReg);
        //db.close();
    }

    public void actualizarPropiedad(String parametro, String valor) {
        db = helper.getWritableDatabase();
        ContentValues nuevoReg = new ContentValues();
        nuevoReg.put("VALOR", valor);
        db.update("SYSTEM", nuevoReg, "PARAMETRO='" + parametro + "'", null);
        //db.close();
    }

    public String obtenerPropiedad(String codigo) {
        db = helper.getReadableDatabase();
        String valor = "";
        String[] campos = new String[]{"VALOR"};
        String[] args = new String[]{codigo};
        Cursor c = db.query("SYSTEM", campos, "PARAMETRO=?", args, null, null, null);
        try {
            if (c.moveToFirst()) {
                valor = c.getString(0);
            } else {
                Log.i("Consulta Vacia", "La consulta con código " + codigo + " a la tabla SYSTEM no ha devuelto valor.");
            }
        } catch (Exception e) {
            Log.e("BBDD", "Error al obtener propiedad" + e.getLocalizedMessage());
        }
        //c.close();

        return valor;
    }
    public ArrayList<String> obtenerConversaciones(){
        int i = 0;
        ArrayList<String> conversaciones = new ArrayList<String>();
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(Config.RECOVER_CONVERSATIONS, null);
        Log.i("CI","Hay "+ c.getCount() + " columnas.");
        while(c.move(1)){
            i++;
            conversaciones.add(c.getString(0));
        }
        Log.i("CI", "Hay " + i + " resultados.");
        return conversaciones;
    }
    public ArrayList<Mensaje> obtenerMensajes(String contacto){
        ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
        Mensaje m = null;
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  MENSAJE FROM CONVERSATION WHERE (EMISOR = '" + contacto + "') OR (REMITENTE = '" + contacto + "')",null);
        Log.i("CC","He consultado los mensajes.");

        while(c.move(1)){
             m = new Mensaje (c.getString(0), null);
            mensajes.add(m);
        }
        Log.i("CC","He cargado los mensajes.");
        return mensajes;
    }

    public void insertarConversaciones(ArrayList<Conversation> conversaciones) {
        db = helper.getWritableDatabase();
        for (Conversation conversacione : conversaciones) {
            insertarConversacion(conversacione);
        }

    }

    public void insertarConversacion(Conversation conv){
        for (Mensaje mensaje : conv.getMessages()) {
            insertarMensaje(conv.getEmisor(), "NosotrosMismos", mensaje); //Hay que cambiar los nosotros mismo por nuestro propio ID
        }
    }

    public void iniciarConversacion(String userName){
        db = helper.getWritableDatabase();

        ContentValues nuevoReg = new ContentValues();
        nuevoReg.put("EMISOR", "nosotros");
        nuevoReg.put("REMITENTE", userName);
        //Esto hay que mirarlo!!!!!!!!!!!!!!!
        nuevoReg.put("MENSAJE", "");
        nuevoReg.put("FECHA", "");
        db.insert("CONVERSATION", null, nuevoReg);
    }

    public void insertarMensaje(String emisor,String receptor,Mensaje mensaje){
        ContentValues nuevoReg = new ContentValues();
        nuevoReg.put("EMISOR", emisor);
        nuevoReg.put("REMITENTE", receptor);
        nuevoReg.put("MENSAJE", mensaje.getTexto());
        nuevoReg.put("FECHA", String.valueOf(mensaje.getFecha()));
        db.insert("CONVERSATION", null, nuevoReg);
    }
}


