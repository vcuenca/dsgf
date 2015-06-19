package es.uv.androidchat.JavaObjects;

import android.util.Log;

import java.util.ArrayList;

import es.uv.androidchat.ConversationActivity;
import main.java.Conversation;

public class Facade {

    public static ConversationActivity conversacionActual = null;

    public Facade(){}

    //Busca en el servidor los contactos que coinciden con el parametro de entrada
    public ArrayList<String> getContacts(String contactName){
        Log.d(Config.TAG, "llego a facade");
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(contactName);
        ClientThread client = new ClientThread(Config.IP_SERVER, Config.PORT, Config.user.getUser(), Config.user.getPassword(), params, 3);
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
        Log.d(Config.TAG, text + "textp");
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(remitente);
        params.add(text);

        ClientThread client = new ClientThread(Config.IP_SERVER, Config.PORT, Config.user.getUser(), Config.user.getPassword(), params, 2);
        client.start();
    }
    //Recibe las conversaciones
    public ArrayList<Conversation> getMessages(){
        ClientThread client = new ClientThread(Config.IP_SERVER, Config.PORT, Config.user.getUser(), Config.user.getPassword(), null, 0);
        client.start();
        try {
            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return client.getMessagesFromLocal();
    }

    public void sendConfirmation(int maxId){
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(new Integer(maxId));
        ClientThread client = new ClientThread(Config.IP_SERVER, Config.PORT, Config.user.getUser(), Config.user.getPassword(), params, 4);
        client.start();
    }

}

