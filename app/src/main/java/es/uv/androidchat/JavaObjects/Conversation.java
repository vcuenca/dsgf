package es.uv.androidchat.JavaObjects;

import java.io.Serializable;
import java.util.ArrayList;

public class Conversation implements Serializable{

    private ArrayList<Mensaje> messages;
    private String emisor;
    public Conversation(ArrayList<Mensaje> messages,String emisor){
        this.messages = messages;
        this.emisor=emisor;
    }


    public ArrayList<Mensaje> getMessages(){
        return messages;
    }

    public void addMessage(Mensaje message){
        this.messages.add(message);
    }

    public String getEmisor() {
        return emisor;
    }

}