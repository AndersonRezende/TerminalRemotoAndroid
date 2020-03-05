package com.skirtshot.terminalremoto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import adaptador.Comando;
import gerenciador.Monitor;
import gerenciador.ConexaoHandler;

public class MouseActivity extends AppCompatActivity implements View.OnClickListener {

    private int x = 0, y = 0;
    private int widthDesktop = 1920, heightDesktop = 1080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout LinearLayoutClique = (LinearLayout) findViewById(R.id.LinearLayoutClique);
        LinearLayoutClique.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int xMobile = (int) event.getX();
                int yMobile = (int) event.getY();
                int widthMobile = v.getWidth();
                int heightMobile = v.getHeight();
                x = (xMobile * widthDesktop) / widthMobile;
                y = (yMobile * heightDesktop) / heightMobile;

                final String comando = "robot-mover " + x + " " + y;

                Thread enviar = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ConexaoHandler.mandarMensagem(comando);
                    }
                });
                enviar.start();
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        String comando = "robot-mover ";
        switch (v.getId())
        {
            case R.id.buttonEsquerdo:
                comando += Comando.cliqueMouse(Comando.BOTAO_ESQUERDO_MOUSE);
                break;
            case R.id.buttonDireito:
                comando += Comando.cliqueMouse(Comando.BOTAO_DIREITO_MOUSE);
                break;
        }

        final String finalComando = comando;
        Thread enviar = new Thread(new Runnable() {
            @Override
            public void run() {
                ConexaoHandler.mandarMensagem(finalComando);
            }
        });
        enviar.start();
    }
}
