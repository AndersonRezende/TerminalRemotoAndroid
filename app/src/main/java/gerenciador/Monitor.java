package gerenciador;

import java.util.ArrayList;

public class Monitor
{
    private ArrayList<String> mensagens;

    public Monitor()
    {   mensagens = new ArrayList<>();  }

    public void adicionarMensagem(String mensagem)
    {   this.mensagens.add(mensagem);   }

    public String retirarMensagem()
    {
        String saida = "";
        if(mensagens.size() > 0)
            saida = mensagens.remove(0);
        return saida;
    }

    public boolean haMensagem()
    {   return !mensagens.isEmpty(); }
}
