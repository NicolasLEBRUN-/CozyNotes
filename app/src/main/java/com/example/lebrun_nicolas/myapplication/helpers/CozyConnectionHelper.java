package com.example.lebrun_nicolas.myapplication.helpers;

import android.content.Context;
import android.os.Build;
import android.util.Base64;

import com.example.lebrun_nicolas.myapplication.DAL.DaoCredential;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.entity.mime.Header;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;

/**
 * Created by LEBRUN_NICOLAS on 26/10/2016.
 */

public class CozyConnectionHelper {

    public DaoCredential daoCredential;
    private static CozyConnectionHelper cozyInstance;

    public CozyConnectionHelper(Context context) {
        this.daoCredential = DaoCredential.getInstance(context);
    }

    public static CozyConnectionHelper getInstance(Context context) {
        if(cozyInstance == null) {
            cozyInstance = new CozyConnectionHelper(context);
        }
        return cozyInstance;
    }

    public void connectToCozy(String urlConnect, String pwd) throws IOException {

        new Thread(new ConnectionThread(urlConnect, pwd, daoCredential)).start();
    }
}

class ConnectionThread implements Runnable {

    protected String url;
    protected String pwd;
    protected DaoCredential daoCredential;

    public ConnectionThread(String url, String pwd, DaoCredential daoCredential) {
        this.url = url;
        this.pwd = pwd;
        this.daoCredential = daoCredential;
    }

    public void run() {
        String cozyUrlDevice =  this.url+"/device";
        String encryptedPwd = Base64.encodeToString(("owner:"+this.pwd).getBytes(), Base64.DEFAULT);
        String deviceLogin = Build.MODEL+"_CozyNotes";

        InputStream inputStream;

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
            String login = (String) j.get("login");
            String password = (String) j.get("password");
            this.daoCredential.updateParam("login", login);
            this.daoCredential.updateParam("password", password);
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
