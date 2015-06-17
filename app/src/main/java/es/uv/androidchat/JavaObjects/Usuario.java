package es.uv.androidchat.JavaObjects;

public class Usuario {

    private String user;
    private String password;

    public Usuario(){}

    public Usuario(String _user, String _password){
        setUser(_user);
        setPassword(_password);
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
