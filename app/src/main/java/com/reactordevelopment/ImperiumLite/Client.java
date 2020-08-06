package com.reactordevelopment.ImperiumLite;


import android.util.Log;

import java.io.*;
import java.net.*;

public class Client {

    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String host;
    private int id;

    public Client(String host, int id){

        this.id = id;
        new Thread() {
            @Override
            public void run() {
                    connectToServer();
                    sendData("Hello There: "+android.os.Build.MODEL);
                    processConnection();
            }
        }.start();

    }
    public int getId(){return id;}
    public void close(){
        try { client.close(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void connectToServer() {
        try { if(host.equals("localhost") || host.equals("Socket")) client = new Socket(InetAddress.getByName("localhost"), Server.SOCKET);
        else client = new Socket(host, Server.SOCKET);}
        catch (IOException ex) { ex.printStackTrace(); }
    }

    public void sendData(String data)  {
        // put any text we write on the network into a stream
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(data);
            output.flush(); // sends the message
            Log.i("Connect Message", "Message Sent to server");
        }
        catch (IOException ex) { ex.printStackTrace(); }
    }

    public void processConnection() {
        try {
            input = new ObjectInputStream(client.getInputStream());
            String message = (String) input.readObject();
            Log.i("From server Message", ""+message);
        }
        catch (IOException ex) { ex.printStackTrace(); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
    }
}