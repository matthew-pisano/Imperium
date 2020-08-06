package com.reactordevelopment.ImperiumLite;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MultiplayerActivity extends AppCompatActivity {
    private ImageButton hostButton;
    private ImageButton clientButton;
    private ImageButton resetButton;
    protected static Button invisiButton;
    protected static TextView connection;
    private EditText serverIp;
    private static Server server;
    private static Client playerClient;
    private String ip;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        hostButton = findViewById(R.id.host);
        clientButton = findViewById(R.id.client);
        resetButton = findViewById(R.id.reset);
        serverIp = findViewById(R.id.clientId);
        connection = findViewById(R.id.connection);
        context = this;
        invisiButton = new Button(context);
        makeButtons();
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();

        ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        ((TextView)findViewById(R.id.ipAddress)).setText(ip);
        serverIp.setText(ip.substring(0, ip.indexOf('.', ip.indexOf('.', ip.indexOf('.')+1)+1)+1));
    }
    @Override
    public void onBackPressed(){
        GameActivity.resetHost();
        finish();
    }
    public void makeConnection() {
        //Intent intent = new Intent().setClassName(MultiplayerActivity.this, "com.reactordevelopment.ImperiumLite.GameActivity");
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("players", 2);
        Log.i("build", "added " + 2 + " players");
        intent.putExtra("mapId", 0);
        if(GameActivity.server.isHost())intent.putExtra("types", new int[]{0, 0});
        else intent.putExtra("types", new int[]{2, 0});
        intent.putExtra("tag", "new");
        //context.startActivity(intent);
        startActivity(intent);
        finish();
    }
    private void makeButtons() {
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(serverIp.getText().toString().equals(ip)){
                    connection.setText("Can't connect to self! Try again");
                    return;
                }
                connection.setText("Connecting...");
                GameActivity.createHost(serverIp.getText().toString(), true);
            }
        });
        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(serverIp.getText().toString().equals(ip)){
                    connection.setText("Can't connect to self! Try again");
                    return;
                }
                connection.setText("Connecting...");
                GameActivity.createHost(serverIp.getText().toString(), false);
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                GameActivity.resetHost();
                connection.setText("Connection Reset! Chose a host or client");
            }
        });

        invisiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeConnection();
            }
        });
    }

}
