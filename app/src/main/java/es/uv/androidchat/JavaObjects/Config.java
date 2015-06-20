package es.uv.androidchat.JavaObjects;

import es.uv.androidchat.ConversationActivity;

public  class Config {

    // used to share GCM regId with application server - using php app server
    //static final String APP_SERVER_URL = "http://192.168.1.17/gcm/gcm.php?shareRegId=1";

    // GCM server using java
    public static  String IP_SERVER = "10.0.2.2";
    public static Usuario user = new Usuario();
    // Google Project Number
    public static final String GOOGLE_PROJECT_ID = "492347066058";
    public static final String MESSAGE_KEY = "message";
    public static final String GCM_CODE = "GCM_CODE";
    public static final String VERSION = "VERSION";

    public static final String DATABASE_NAME = "SQLDB";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_CONVERSATION_TABLE = "CREATE TABLE CONVERSATION (ID INTEGER PRIMARY KEY AUTOINCREMENT, EMISOR TEXT,REMITENTE TEXT, MENSAJES_PENDIENTES INTEGER)";

    public static final String CREATE_MESSAGES_TABLE = "CREATE TABLE MESSAGES (ID_MENSAJE INTEGER PRIMARY KEY AUTOINCREMENT, ID_CONVERSATION INTEGER, MESSAGE TEXT, FECHA TEXT, PARA TEXT, DE TEXT)";
    public static final String CREATE_SYSTEM_TABLE = "CREATE TABLE SYSTEM (PARAMETRO VARCHAR(30), VALOR VARCHAR(50))";
    public static final String RECOVER_CONVERSATIONS = "SELECT DISTINCT REMITENTE FROM CONVERSATION";
    public static final String TAG = "DEBUG";
    //static final String IP_SERVER = "192.168.1.134";
    //static final String IP_SERVER = "192.168.1.130";
    public static final int PORT = 1235;
    public static Facade facade = new Facade();


}