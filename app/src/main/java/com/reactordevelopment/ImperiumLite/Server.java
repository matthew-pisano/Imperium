package com.reactordevelopment.ImperiumLite;


import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server extends MultiplayerActivity{
    public static final int SOCKET = 12345;
    private static ServerSocket server;
    private String outIp;
    private static Object received;
    private static Socket inSocket;
    private static Socket outSocket;
    private static ObjectInputStream fromServer;
    private static ObjectOutputStream toServer;
    private static ObjectOutputStream output;
    private boolean isHost;
    private static boolean isReset;
    private boolean launchedGame;

    public Server(String outIp, boolean isHost){
        this.outIp = outIp;
        this.isHost = isHost;
        launchedGame = false;
        new Thread(){
            @Override
            public void run() {
                try {
                    try {
                        server = new ServerSocket(SOCKET, 100); //create socket
                    }catch(BindException e){e.printStackTrace();}
                    connectToServer();
                    Log.i("Server Start", "Connect");
                    waitForConnection();
                    Log.i("Server Start", "Waiting");
                    isReset = false;
                    Log.i("Server Start", "reset1: "+isReset);
                    while (!isInterrupted()) {
                        Log.i("Server Start", "reset2: "+isReset);
                        if(isReset){
                            Log.i("Server Start", "reset3: "+isReset);
                            break;
                        }
                        Log.i("Server Start", "Inloop");
                        //Thread.sleep(1000);
                        try {
                            Log.i("Server Thread", "");
                            processConnection();
                        }catch (EOFException | NullPointerException e) { Log.i("Connectuo", "Server terminated connection"); }
                        catch (IOException ioException) { ioException.printStackTrace(); }
                    }
                }catch (/*InterruptedException |*/ IOException e){e.printStackTrace();}
            }
        }.start();

    }
    public boolean isHost(){return isHost;}

    private void waitForConnection() throws IOException {
        //Log.i("Waiting", "Waiting for Connection");
        inSocket = server.accept();
        //Log.i("ConnectionReceivedFrom", ""+ inSocket.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        output = new ObjectOutputStream(inSocket.getOutputStream());
        output.flush();
        //Log.i("Message Received!", "");
    }

    public void reset(){
        try {
            server.close();
            if(inSocket != null) {
                inSocket.close();
                outSocket.close();
                fromServer.close();
                toServer.close();
                output.close();
            }
        }catch (IOException e){e.printStackTrace();}
        isReset = true;
        received = null;
        server = null;
        inSocket = null;
        outSocket = null;
        fromServer = null;
        toServer = null;
    }
    private void processConnection() throws IOException {
        //Log.i("Connection", "process");
        connectToServer();
        //Log.i("Process", "1");
        getStreams();
        waitForConnection();
       //Log.i("Process", "2");
        fromServer = new ObjectInputStream(inSocket.getInputStream());
        //Log.i("Process", "3");
        //do {
            try {
                //Log.i("Messahe", "Start");
                try {
                    received =  fromServer.readObject();
                }catch (StreamCorruptedException e){ e.printStackTrace(); }
                //Log.i("Messahe", received.toString());
            }
            catch (ClassNotFoundException classNotFoundException) { Log.i("Msg err", "Unknown Message Type"); }
            catch (SocketException e) { Log.i("Reser", "Connection Reset!");}
    }
    public void close(){
        try { outSocket.close(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void connectToServer() {
        try {
            outSocket = new Socket(outIp, Server.SOCKET);}
        catch (IOException ex) { ex.printStackTrace(); }
    }

    public void sendData(String data)  {
        // put any text we write on the network into a stream
        Log.i("Send Data", "saart");
        try {
            toServer = new ObjectOutputStream(outSocket.getOutputStream());
            toServer.writeObject(data);
            toServer.flush(); // sends the message
            Log.i("Connect Message", "Message Sent to server");
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }
    public void sendData(ArrayList<String> data)  {
        // put any text we write on the network into a stream
        /*String list = "Sent List [";
        for(String s : data)
            list += s+", ";
        list += "]";
        Log.i("Sent", list);*/
        try {
            toServer = new ObjectOutputStream(outSocket.getOutputStream());
            toServer.writeObject(data);
            toServer.flush(); // sends the message
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }
    public Object getData(boolean connecting){
        final String hostValue;
        if(isHost) hostValue = "host";
        else hostValue = "client";
        boolean isList = false;
        ArrayList<String> recievedStr = null;
        try{
            recievedStr = (ArrayList) received;
            //ArrayList test = (ArrayList) Server.received;
        }catch (ClassCastException e){/*recievedStr = (String)received;*/}
        if(/*recievedStr != null*/ connecting && recievedStr != null) {
            Log.i("RecievedStr", recievedStr.toString());
            if (recievedStr.get(0).charAt(recievedStr.get(0).length() - 1) == '1' && !recievedStr.get(0).equals(hostValue+1) && !launchedGame) {
                launchedGame = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        connection.setText("Connected!");
                        invisiButton.performClick();
                    }
                });

            }
            if (connection.getText().toString().charAt(0) != ':')
                if (recievedStr.get(0).equals(hostValue+1))
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            connection.setText(":You are a " + hostValue + ", you can't connect to another " + hostValue + "! Please try again:");
                        }
                    });
        }else if(recievedStr != null){
            String list = "Gotten List [";
            for(String s : recievedStr)
                list += s+", ";
            list += "]";
            Log.i("Gotten", list);
        }
        return recievedStr;
    }
    /*public void processConnectionOut() {
        try {
            input = new ObjectInputStream(outSocket.getInputStream());
            String message = (String) input.readObject();
            Log.i("From server Message", ""+message);
        }
        catch (IOException ex) { ex.printStackTrace(); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
    }*/
}