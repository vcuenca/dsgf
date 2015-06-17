package es.uv.androidchat.JavaObjects;

public interface Config {

    // used to share GCM regId with application server - using php app server
    //static final String APP_SERVER_URL = "http://192.168.1.17/gcm/gcm.php?shareRegId=1";

    // GCM server using java
    String APP_SERVER_URL = "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";
    Usuario user = new Usuario();
    // Google Project Number
    String GOOGLE_PROJECT_ID = "492347066058";
    String MESSAGE_KEY = "message";
    String GCM_CODE = "GCM_CODE";
    String VERSION = "VERSION";

    String DATABASE_NAME = "SQLDB";
    int DATABASE_VERSION = 1;
   String CREATE_CONVERSATION_TABLE = "CREATE TABLE CONVERSATION (" +
            "EMISOR TEXT,REMITENTE TEXT, MENSAJE TEXT, FECHA DATE)";

    String CREATE_SYSTEM_TABLE = "CREATE TABLE SYSTEM (PARAMETRO VARCHAR(30), VALOR VARCHAR(50))";
    String RECOVER_CONVERSATIONS = "SELECT DISTINCT remitente FROM Conversation";
    String TAG = "DEBUG";
    String IP_SERVER = "10.0.2.2";
    int PORT = 1235;
    Facade facade = new Facade();
}