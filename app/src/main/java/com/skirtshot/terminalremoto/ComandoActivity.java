package com.skirtshot.terminalremoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import adaptador.Comando;
import gerenciador.Monitor;
import gerenciador.ConexaoHandler;

public class ComandoActivity extends AppCompatActivity implements View.OnClickListener {
    private String mensagem = "";
    private EditText editTextResultado;
    private Monitor monitor = ConexaoHandler.getMonitor();
    private Context context = this;
    private boolean execucaoComando = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comando);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btEnviar = (Button) findViewById(R.id.buttonEnviar);
        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextComando = (EditText) findViewById(R.id.editTextComando);
                Switch switchMensagemComando = (Switch) findViewById(R.id.switchMensagemComando);
                String aux;

                if(switchMensagemComando.isChecked())
                    aux = Comando.mensagem(editTextComando.getText().toString());
                else
                    aux = Comando.comandoTerminal(editTextComando.getText().toString());

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

        Button btMouse = (Button) findViewById(R.id.buttonMouse);
        btMouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MouseActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onStart(){
        super.onStart();
        execucaoComando = true;
        editTextResultado = (EditText) findViewById(R.id.editTextResultado);
        Thread recebe = new Thread(new Runnable() {
            @Override
            public void run() {
                while(execucaoComando) {
                    if(monitor.haMensagem()) {
                        mensagem = monitor.retirarMensagem();
                        System.out.println(mensagem);
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

    protected void onStop() {
        super.onStop();
        execucaoComando = false;
    }

    public void enviarComando(String comando){
        if(ConexaoHandler.conectado())
            ConexaoHandler.mandarMensagem(comando);
    }

    @Override
    public void onClick(View v) {
        String comando = "";
        switch (v.getId())
        {
            case R.id.buttonVolumeMais:
                comando = "" + Comando.AUMENTAR_VOLUME;
                break;
            case R.id.buttonVolumeMenos:
                comando = ""+ Comando.DIMINUIR_VOLUME;
                break;
            case R.id.buttonVolumeMudo:
                comando = ""+ Comando.MUDO;
                break;
            case R.id.buttonSuspender:
                comando = ""+ Comando.SUSPENDER;
                break;
            case R.id.buttonBloquear:
                comando = ""+ Comando.BLOQUEAR_TELA;
                break;
            case R.id.buttonReiniciar:
                comando = ""+ Comando.REINICIAR;
                break;
            case R.id.buttonDesligar:
                comando = ""+ Comando.DESLIGAR;
                break;
            case R.id.buttonListarDiretorio:
                comando = ""+ Comando.LISTAR_DIRETORIO;
                break;
            case R.id.buttonPwd:
                comando = ""+ Comando.PWD;
                break;
            case R.id.buttonIp:
                comando = ""+ Comando.CONFIGURACAO_IP;
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
