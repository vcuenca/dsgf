package es.uv.androidchat.JavaObjects;

import java.util.Date;

/**
 * Created by Adrian on 23/03/2015.
 */
public class Mensaje {

    private String texto = "";
    private Date fecha;

    public Mensaje(String texto,Date fecha){
        this.texto = texto;
        this.fecha=fecha;
    }


    public String getTexto(){
        return texto;
    }

    public Date getFecha() {
        return fecha;
    }
}
