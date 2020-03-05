package gerenciador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConexaoHandler
{
    private static Socket socket;
    private static DataInputStream entrada;
    private static DataOutputStream saida;
    private static Monitor monitor;

    public static synchronized Socket getSocket()
    {   return socket;  }

    public static synchronized void setSocket(Socket socket)
    {   ConexaoHandler.socket = socket;  }

    public static synchronized DataInputStream getEntrada()
    {   return entrada;  }

    public static synchronized void setEntrada(DataInputStream entrada)
    {   ConexaoHandler.entrada = entrada;  }

    public static synchronized DataOutputStream getSaida()
    {   return saida;  }

    public static synchronized void setSaida(DataOutputStream saida)
    {   ConexaoHandler.saida = saida;  }

    public static synchronized Monitor getMonitor()
    {   return monitor;  }

    public static synchronized void setMonitor(Monitor monitor)
    {   ConexaoHandler.monitor = monitor;  }

    public static boolean conectado()
    {
        boolean conectado = false;
        if(socket != null && saida != null)
            conectado = true;
        return conectado;
    }

    public static synchronized boolean mandarMensagem(String mensagem)
    {
        boolean enviou = false;
        if(conectado())
        {
            try
            {
                saida.writeUTF(mensagem);
                enviou = true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return enviou;
    }
}
