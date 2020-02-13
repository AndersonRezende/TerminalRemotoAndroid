package com.skirtshot.terminalremoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import adaptador.Comando;
import gerenciador.Monitor;
import gerenciador.SocketHandler;
import thread.AguardaMensagem;

public class ComandoActivity extends AppCompatActivity implements View.OnClickListener {

    private String IP = "192.168.0.100";
    private int PORTA = 9999;
    private String SENHA = "0000";

    private String mensagem = "";
    private EditText editTextResultado;
    private Monitor monitor;
    private Socket cliente = null;
    private DataOutputStream saida = null;
    private DataInputStream entrada = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comando);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configurarComunicacao();
        ativarRecebimentoMensagem();



        Button btEnviar = (Button) findViewById(R.id.buttonEnviar);
        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextComando = (EditText) findViewById(R.id.editTextComando);
                final String comando = editTextComando.getText().toString();
                editTextComando.setText("");

                final String finalComando = comando;
                Thread enviar = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        enviarComando(finalComando);
                    }
                });
                enviar.start();
            }
        });

        editTextResultado = (EditText) findViewById(R.id.editTextResultado);
        Thread recebe = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if(monitor.haMensagem()) {
                        mensagem = monitor.retirarMensagem();
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                editTextResultado.setText(mensagem);
                                editTextResultado.setSelection(editTextResultado.getText().length());
                            }
                        });
                    }
                }
            }
        });
        recebe.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        fecharConexao();
    }

    private void ativarRecebimentoMensagem() {
        Thread aguardaMensagem = new Thread(new AguardaMensagem(monitor, entrada));
        aguardaMensagem.start();
    }

    private void configurarComunicacao() {
        monitor = new Monitor();
        cliente = SocketHandler.getSocket();
        try {
            saida = new DataOutputStream(cliente.getOutputStream());
            entrada = new DataInputStream(cliente.getInputStream());
        }
        catch (IOException e)
        {   e.printStackTrace();    }
    }

    public void enviarComando(String comando){
        try
        {
            if(cliente != null && saida != null)
                saida.writeUTF(comando);
        }
        catch (IOException e)
        {   e.printStackTrace();    }
    }

    public void fecharConexao() {
        try {
            if (cliente != null && cliente.isConnected())
            {
                entrada.close();
                saida.close();
                cliente.close();
            }
        }
        catch (IOException e)
        {   e.printStackTrace();    }

    }



    @Override
    public void onClick(View v) {
        String comando = "";
        switch (v.getId())
        {
            case R.id.buttonVolumeMais:
                comando = ""+ Comando.AUMENTAR_VOLUME.getValor();
                break;
            case R.id.buttonVolumeMenos:
                comando = ""+Comando.DIMINUIR_VOLUME.getValor();
                break;
            case R.id.buttonSuspender:
                comando = ""+Comando.SUSPENDER.getValor();
                break;
            case R.id.buttonBloquear:
                comando = ""+Comando.BLOQUEAR_TELA.getValor();
                break;
            case R.id.buttonReiniciar:
                comando = ""+Comando.REINICIAR.getValor();
                break;
            case R.id.buttonDesligar:
                comando = ""+Comando.DESLIGAR.getValor();
                break;
        }

        final String finalComando = comando;
        Thread enviar = new Thread(new Runnable() {
            @Override
            public void run() {
                enviarComando(finalComando);
            }
        });
        enviar.start();
    }


}
