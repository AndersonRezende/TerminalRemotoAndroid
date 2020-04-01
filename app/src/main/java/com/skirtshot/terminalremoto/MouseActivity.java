package com.skirtshot.terminalremoto;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import adaptador.Comando;
import gerenciador.ConexaoHandler;
import gerenciador.Monitor;
import host.Desktop;

public class MouseActivity extends AppCompatActivity implements View.OnClickListener {

    private Monitor monitor;
    private int x = 0, y = 0;
    private int larguraDesktop = Desktop.largura, alturaDesktop = Desktop.altura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Touchpad");

        monitor = ConexaoHandler.getMonitor();
        LinearLayout LinearLayoutClique = (LinearLayout) findViewById(R.id.LinearLayoutClique);
        LinearLayoutClique.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int larguraAndroid = v.getWidth();
                int alturaAndroid = v.getHeight();
                x = (int) (event.getX() - 0) * (larguraDesktop - 0) / (larguraAndroid - 0);
                y = (int) (event.getY() - 0) * (alturaDesktop - 0) / (alturaAndroid - 0);
                final String comando = Comando.moverMouse(x, y);

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
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        String comando = "";
        switch (v.getId())
        {
            case R.id.buttonEsquerdo:
                comando += Comando.clicarMouse(Comando.BOTAO_ESQUERDO_MOUSE);
                break;
            case R.id.buttonDireito:
                comando += Comando.clicarMouse(Comando.BOTAO_DIREITO_MOUSE);
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
