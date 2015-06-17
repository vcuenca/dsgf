package es.uv.androidchat.JavaObjects;

import android.util.Log;

import java.util.ArrayList;

public class Facade {

    public Facade(){}

    //Busca en el servidor los contactos que coinciden con el parametro de entrada
    public ArrayList<String> getContacts(String contactName){
        Log.d(Config.TAG, "llego a facade");
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(contactName);
        ClientThread client = new ClientThread(Config.IP_SERVER, Config.PORT, "ivan", "ramon", params, 3);
        client.start();
        try {
            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return client.getSearchResults();
        /*
        ArrayList<String> a = new ArrayList<String>();
        a.add("contacto1");
        a.add("contacto2");
        a.add("contacto3");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");
        a.add("contacto4");

        return a;
        */
    }

    public void sendText(String remitente, String text){
        Log.d(Config.TAG, text  + "textp");
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(remitente);
        params.add(text);

        ClientThread client = new ClientThread(Config.IP_SERVER, Config.PORT, "ivan", "ramon", params, 2);
        client.start();
    }

}

