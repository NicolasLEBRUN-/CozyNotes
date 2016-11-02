package com.example.lebrun_nicolas.myapplication.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by LEBRUN_NICOLAS on 26/10/2016.
 */

public class CozyConnectionHelper {

    private static CozyConnectionHelper cozyInstance;
    private static SharedPreferences preferences;

    public CozyConnectionHelper(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static CozyConnectionHelper getInstance(Context context) {
        if(cozyInstance == null) {
            cozyInstance = new CozyConnectionHelper(context);
        }
        return cozyInstance;
    }

    public void connectToCozy(String urlConnect, String pwd) throws IOException {

        new Thread(new ConnectionThread(urlConnect, pwd, this.preferences)).start();
    }
}

class ConnectionThread implements Runnable {

    protected String url;
    protected String pwd;
    protected SharedPreferences preferences;

    public ConnectionThread(String url, String pwd, SharedPreferences preferences) {
        this.url = url;
        this.pwd = pwd;
        this.preferences = preferences;
    }

    public void run() {
        String cozyUrlDevice =  this.url+"/device";
        String encryptedPwd = Base64.encodeToString(("owner:"+this.pwd).getBytes(), Base64.DEFAULT);
        String deviceLogin = Build.MODEL+"_CozyNotes";

        InputStream inputStream;



       if(this.preferences.getString("token", null) == null) {
           try {
               HttpsURLConnection c = (HttpsURLConnection) new URL(cozyUrlDevice).openConnection();
               c.setRequestMethod("POST");
               c.setDoOutput(true);
               c.setRequestProperty("Content-Type", "application/json");
               c.setRequestProperty("Authorization", "Basic "+encryptedPwd);

               String str =  "{\"" +
                       "login\":\""+deviceLogin+"\","+
                       "\"permissions\": " +
                       "{\"note\": " +
                       "{\"description\": \"Synchronize notes\"}" +
                       "}" +
                       "}";
               byte[] outputInBytes = str.getBytes("UTF-8");

               OutputStream os = c.getOutputStream();

               os.write( outputInBytes );
               os.close();

               try {
                   inputStream = c.getInputStream();
               } catch (IOException e) {
                   inputStream = c.getErrorStream();
               }
               InputStreamReader isr = new InputStreamReader(inputStream);
               BufferedReader br = new BufferedReader(isr);
               String data = br.readLine();
               JSONObject j = new JSONObject(data);
               String password = (String) j.get("password");

               SharedPreferences.Editor editor = this.preferences.edit();
               editor.putString("token", password);
               editor.commit();

               inputStream.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }


    }
}
