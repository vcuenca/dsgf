package es.uv.androidchat.JavaObjects;

import android.util.Log;

import es.uv.androidchat.JavaObjects.Conversation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread{

    private String ipAddress, user, password;
    private int port;
    private boolean authenticated, registered;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private int mode = -1;
    private ArrayList<Conversation> conversations = null;
    private ArrayList<Object> params;
    private ArrayList<String> contacts;

    //mode 0 = get my messages, 1 = register, 2 = message
    //param es realmente el parametro que se utiliza en las consultas
    public ClientThread(String ipAddress, int port, String user, String password, ArrayList<Object> params, int mode){
        this.port = port;
        this.ipAddress = ipAddress;
        this.user = user;
        this.password = password;
        this.mode = mode;
        this.params = params;
    }

    public void run(){
        Socket s = null;
        try {
            s = new Socket(ipAddress, port);
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());

            if (mode == 0){
               authenticateUser(user, password);
                //if (isAuthenticated()) {
                conversations = getMessagesFromServer(s, out, in);
              //  }
            }else if (mode == 1){
                Log.i("ANDROIDCHAT", user + password);
                registerUser(user, password, (String)params.get(0));
            }else if (mode == 2){
                sendText((String)params.get(0), (String)params.get(1));

            }else if (mode == 3){
                searchContacts((String)params.get(0));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void searchContacts(String contactName){
        try {
            out.writeObject(new Integer(3));
            out.writeObject(contactName);
            contacts = (ArrayList)in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getSearchResults(){
        return contacts;
    }

    public boolean isAuthenticated(){
        return authenticated;
    }

    private void authenticateUser(String user, String password){
        //Enviamos el usuario y la password
        try {
            out.writeObject(new Integer(0));
            out.writeObject(user);
            out.writeObject(password);
            //Esperamos respuesta del servidor
            String respuesta = (String)in.readObject();

            if (respuesta.equals("OK")) {
                authenticated = true;
                //Enviamos la ip
                //out.writeObject("10.0.2.2");
                //Enviamos el cloudID
                //out.writeObject("cloudID");

                Log.i("CHAT", "OK");
            }else
                authenticated = false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void registerUser(String user, String password, String cloudID){
        //Enviamos el usuario y la password
        try {
            out.writeObject(new Integer(1));
            out.writeObject(user);
            out.writeObject(password);
            out.writeObject(cloudID);
            //Esperamos respuesta del servidor
            String respuesta = (String)in.readObject();

            if (respuesta.equals("OK")) {
                registered = true;
                Log.i("CHAT", "OK");
            }else
                registered = false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public boolean isRegistered(){
        return this.registered;
    }

    public ArrayList<Conversation> getMessagesFromServer(Socket s, ObjectOutputStream out, ObjectInputStream in){
        conversations = new ArrayList<Conversation>();
        //Leemos el número de mensajes que tenemos pendientes a recibir
        try {
            Integer numberOfMessages = (Integer)in.readObject();
            Log.i("HOLA", numberOfMessages + "");

            for (int i = 0; i < numberOfMessages; i++){
                Conversation conversation = (Conversation)in.readObject();
                conversations.add(conversation);
                //GestorDB.getInstance(getApplicationContext()).insertarConversaciones(conversaciones);
                Log.i("HOLA", "Conversacion recibida");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conversations;
    }

    public ArrayList<Conversation> getMessagesFromLocal(){
        return this.conversations;
    }

    public void sendText(String remitente, String message) throws IOException{
        out.writeObject(new Integer(4));
        out.writeObject(remitente);
        out.writeObject(message);
    }

}


