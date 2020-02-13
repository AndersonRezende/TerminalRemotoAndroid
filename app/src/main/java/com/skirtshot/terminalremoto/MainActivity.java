package com.skirtshot.terminalremoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import gerenciador.SocketHandler;

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
                                SocketHandler.setSocket(cliente);
                                DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());
                                saida.writeUTF(senha);
                                Intent intent = new Intent(context, ComandoActivity.class);
                                startActivity(intent);
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
}
