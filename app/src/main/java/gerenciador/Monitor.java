package gerenciador;

import java.util.ArrayList;

public class Monitor
{
    private ArrayList<String> mensagens;

    public Monitor()
    {   mensagens = new ArrayList<>();  }

    public synchronized void adicionarMensagem(String mensagem)
    {   this.mensagens.add(mensagem);   }

    public synchronized String retirarMensagem()
    {
        String saida = "";
        if(mensagens.size() > 0)
            saida = mensagens.remove(0);
        return saida;
    }

    public synchronized boolean haMensagem()
    {   return !mensagens.isEmpty(); }
}
