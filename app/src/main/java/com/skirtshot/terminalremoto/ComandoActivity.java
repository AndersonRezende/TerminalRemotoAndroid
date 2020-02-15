package com.skirtshot.terminalremoto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import adaptador.Comando;
import gerenciador.Monitor;
import gerenciador.SocketHandler;
import thread.AguardaMensagem;

public class ComandoActivity extends AppCompatActivity implements View.OnClickListener {
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
                Switch switchMensagemComando = (Switch) findViewById(R.id.switchMensagemComando);
                String aux = editTextComando.getText().toString();

                if(switchMensagemComando.isChecked())
                    aux = "echo \""+editTextComando.getText().toString()+"\"";

                final String comando = aux;
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
            case R.id.buttonVolumeMudo:
                comando = ""+Comando.MUDO.getValor();
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
            case R.id.buttonListarDiretorio:
                comando = ""+Comando.LISTAR_DIRETORIO.getValor();
                break;
            case R.id.buttonPwd:
                comando = ""+Comando.PWD.getValor();
                break;
            case R.id.buttonIp:
                comando = ""+Comando.CONFIGURACAO_IP.getValor();
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
