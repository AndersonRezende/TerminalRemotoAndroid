package com.skirtshot.terminalremoto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private String IP = "192.168.0.100";
    private int PORTA = 9999;
    private String SENHA = "0000";
    private Socket cliente = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
