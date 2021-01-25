package com.example.iormatrix;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Socket_Connect{

    // Adresse IP de la raspberry où le script "server.js" est déjà executé (L'android doit être connecté dans le meme réseau local)
    // A MODIFIER SELON VOTRE RESEAU
    public static String SERVER_STATIC_IP = "192.168.0.27";

    // Le port écouté par le serveur dans le script "server.js", normalement 8080
    public static int SERVER_PORT = 8080;


    private Socket client = null;
    private BufferedReader br;
    private volatile PrintStream bw;
    private String previous_msg = null;


    // Constructeur par défaut
    public Socket_Connect(){
        Log.d("debug","Socket_Connect() : BP 0");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client = new Socket(SERVER_STATIC_IP, SERVER_PORT);
                    Log.d("debug","Socket_Connect() : OK");
                    bw = new PrintStream(client.getOutputStream());
                    Log.d("debug","Socket_Connect() : BP bw="+bw);
                } catch (IOException e) {
                    Log.d("debug", "Socket_Connect() : " + e.getMessage());
                }
            }
        }).start();
        Log.d("debug","Socket_Connect() : BP 1");
    }

    public String getMessage(){
        String str = null;
        try {
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));

            str = br.readLine();
            if(str == null)
                str = previous_msg;
            Log.d("debug", "received response from server : " + str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        previous_msg = str;
        return str;
    }

    public void sendMessage(final String message){
        Log.d("debug","sending message : " + message + " from " + bw);
        new Thread(new Runnable() {
            @Override
            public void run() {
                bw.println(message);
            }
        }).start();

        Log.d("debug","sent message : " + message);
    }


    public void Socket_Connect_Destroy(){
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
