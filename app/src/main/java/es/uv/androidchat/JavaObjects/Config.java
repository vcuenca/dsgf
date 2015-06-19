package es.uv.androidchat.JavaObjects;

import es.uv.androidchat.ConversationActivity;

public interface Config {

    // used to share GCM regId with application server - using php app server
    //static final String APP_SERVER_URL = "http://192.168.1.17/gcm/gcm.php?shareRegId=1";

    // GCM server using java
    static Usuario user = new Usuario();
    // Google Project Number
    static final String GOOGLE_PROJECT_ID = "492347066058";
    static final String MESSAGE_KEY = "message";
    static final String GCM_CODE = "GCM_CODE";
    static final String VERSION = "VERSION";

    static final String DATABASE_NAME = "SQLDB";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_CONVERSATION_TABLE = "CREATE TABLE CONVERSATION (ID INTEGER PRIMARY KEY AUTOINCREMENT, EMISOR TEXT,REMITENTE TEXT)";

    static final String CREATE_MESSAGES_TABLE = "CREATE TABLE MESSAGES (ID_MENSAJE INTEGER PRIMARY KEY AUTOINCREMENT, ID_CONVERSATION INTEGER, MESSAGE TEXT, FECHA TEXT, PARA TEXT, DE TEXT)";
    static final String CREATE_SYSTEM_TABLE = "CREATE TABLE SYSTEM (PARAMETRO VARCHAR(30), VALOR VARCHAR(50))";
    static final String RECOVER_CONVERSATIONS = "SELECT DISTINCT REMITENTE FROM CONVERSATION";
    static final String TAG = "DEBUG";
    //static final String IP_SERVER = "127.0.2.2";
    static final String IP_SERVER = "10.0.2.2";
    //static final String IP_SERVER = "192.168.1.130";
    static final int PORT = 1235;
    static Facade facade = new Facade();
}