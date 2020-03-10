package com.skirtshot.terminalremoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import adaptador.Comando;
import gerenciador.Monitor;
import gerenciador.ConexaoHandler;
import host.Desktop;
import thread.AguardaMensagem;

public class MainActivity extends AppCompatActivity {

    private String ip = "192.168.0.100";
    private int porta = 9999;
    private String senha = "0000";
    private Socket cliente = null;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        Button buttonConectar = (Button) findViewById(R.id.buttonConectar);
        buttonConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextIp = (EditText) findViewById(R.id.editTextIp);
                EditText editTextPorta = (EditText) findViewById(R.id.editTextPorta);
                EditText editTextSenha = (EditText) findViewById(R.id.editTextSenha);

                ip = editTextIp.getText().toString();
                porta = Integer.parseInt(editTextPorta.getText().toString());
                senha = editTextSenha.getText().toString();

                Thread iniciaSocket = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            cliente = new Socket(ip, porta);
                            if(cliente.isConnected()) {
                                DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());
                                DataInputStream entrada = new DataInputStream(cliente.getInputStream());
                                Monitor monitor = new Monitor();

                                ConexaoHandler.setSocket(cliente);
                                ConexaoHandler.setEntrada(entrada);
                                ConexaoHandler.setSaida(saida);
                                ConexaoHandler.setMonitor(monitor);

                                saida.writeUTF(senha);
                                int resultado = entrada.readInt();
                                if(resultado == 0) {
                                    if(ConexaoHandler.mandarMensagem(Comando.comandoInformacoesSistema())) {
                                        Desktop.configurarInformacoes(entrada.readUTF());
                                    }

                                    Thread aguardaMensagem = new Thread(new AguardaMensagem(monitor));
                                    aguardaMensagem.start();

                                    Intent intent = new Intent(context, ComandoActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Thread thread = new Thread(){
                                        public void run(){
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Toast.makeText(MainActivity.this, "Senha incorreta, tente novamente.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    };
                                    thread.start();
                                }
                            }
                        }
                        catch (IOException e)
                        { e.printStackTrace();  }
                    }
                });
                iniciaSocket.start();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        try
        {
            if(ConexaoHandler.getSocket() != null) {
                ConexaoHandler.getSaida().close();
                ConexaoHandler.getEntrada().close();
                ConexaoHandler.getSocket().close();
            }
        }
        catch (IOException e)
        {   e.printStackTrace();    }
    }
}
