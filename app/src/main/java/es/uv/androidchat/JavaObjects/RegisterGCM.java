package es.uv.androidchat.JavaObjects;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import es.uv.androidchat.RegisterActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class RegisterGCM extends AsyncTask<Object, Void, Object>{

    GoogleCloudMessaging gcm;
    String regId;

    private RegisterActivity registerActivity;
    static final String TAG = "Register Activity";
    private Context context;

    public RegisterGCM(Context context, RegisterActivity registerActivity) {
        this.context = context;
        this.registerActivity = registerActivity;
    }


    private String getRegistrationId(Context context) {
        //GestorDB.getInstance(context).insertarPropiedad(Config.GCM_CODE,"1234");
        String registrationId = GestorDB.getInstance(context).obtenerPropiedad(Config.GCM_CODE);
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        int registeredVersion = Integer.valueOf(GestorDB.getInstance(context).obtenerPropiedad(Config.VERSION));
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.d("RegisterGCM",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }



    private void storeRegistrationId(Context context, String regId) {
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);

        GestorDB.getInstance(context).insertarPropiedad(Config.GCM_CODE,regId);
        GestorDB.getInstance(context).insertarPropiedad(Config.VERSION,String.valueOf(appVersion));

    }

    protected Object doInBackground(Object[] params) {
        String msg = "";
        regId = getRegistrationId(context);
        if (regId == "") {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regId = gcm.register(Config.GOOGLE_PROJECT_ID);
                Log.d(Config.TAG, regId);

                storeRegistrationId(context, regId);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                Log.d("RegisterGCM", "Error: " + msg);
            }
        }
        Log.d("RegisterGCM", "AsyncTask completed: " + msg);
        return regId;
    }

    protected void onPostExecute(Object msg) {
        Log.d(Config.TAG, "Registrado: " + msg.toString());
        registerActivity.registerUser(msg.toString());
    }
}