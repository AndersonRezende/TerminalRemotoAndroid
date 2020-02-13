package thread;

import java.io.DataInputStream;
import java.io.IOException;

import gerenciador.Monitor;

public class AguardaMensagem implements Runnable{
    private Monitor monitor;
    private DataInputStream entrada;

    public AguardaMensagem(Monitor monitor, DataInputStream entrada){
        this.monitor = monitor;
        this.entrada = entrada;
    }

    @Override
    public void run() {
        while(true)
        {
            try
            {
                String recebido = entrada.readUTF();
                System.out.println(recebido);
                monitor.adicionarMensagem(recebido);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
