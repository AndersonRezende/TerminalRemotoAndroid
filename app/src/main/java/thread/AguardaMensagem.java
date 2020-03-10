package thread;

import java.io.DataInputStream;
import java.io.IOException;

import gerenciador.ConexaoHandler;
import gerenciador.Monitor;

public class AguardaMensagem implements Runnable{
    private Monitor monitor;
    private DataInputStream entrada;

    public AguardaMensagem(Monitor monitor){
        this.monitor = monitor;
        this.entrada = ConexaoHandler.getEntrada();
    }

    @Override
    public void run() {
        while(true)
        {
            try
            {
                String recebido = entrada.readUTF();
                System.out.println("Recebido do servidor: "+ recebido);
                monitor.adicionarMensagem(recebido);
            }
            catch (IOException e)
            {
                System.err.println("Conexão encerrada.");
                e.printStackTrace();
                break;
            }
        }
    }
}
