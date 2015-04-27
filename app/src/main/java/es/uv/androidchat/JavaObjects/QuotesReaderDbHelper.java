package es.uv.androidchat.JavaObjects;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Adrian on 11/02/2015.
 */
public class QuotesReaderDbHelper extends SQLiteOpenHelper {

    public QuotesReaderDbHelper(Context context){
        super(context,
                Config.DATABASE_NAME,//String name
                null,//factory
                Config.DATABASE_VERSION//int version
        );

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            db.execSQL(Config.CREATE_CONVERSATION_TABLE);
            db.execSQL(Config.CREATE_SYSTEM_TABLE);
            db.execSQL("INSERT INTO CONVERSATION VALUES ('IVAN','JCFVKJFH','FECHA')");
            db.execSQL("INSERT INTO CONVERSATION VALUES ('VICTOR','JCFVKJFH(2)','FECHA')");
            // db.close();
        }catch(Exception e){Log.i("BD","Error al crear las tablas. " + e.getMessage());}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "MY_USSER");
        db.execSQL("DROP TABLE IF EXISTS " + "SYSTEM");
        db.execSQL("DROP TABLE IF EXISTS " + "CONVERSATION");

    }

}
