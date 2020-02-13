package adaptador;

public enum Comando
{
    DESLIGAR(1), CANCELAR_DESLIGAMENTO(2), REINICIAR(3), SUSPENDER(4), BLOQUEAR_TELA(5),
    AUMENTAR_VOLUME(6), DIMINUIR_VOLUME(7), LISTAR_DIRETORIO(8), CONFIGURACAO_IP(9);

    private final int valor;

    Comando(int id)
    {   valor = id; }

    public int getValor()
    {   return valor;   }
}
