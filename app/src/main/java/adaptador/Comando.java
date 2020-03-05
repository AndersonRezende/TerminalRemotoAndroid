package adaptador;

public class Comando {
    public static final int BOTAO_ESQUERDO_MOUSE = 0;
    public static final int BOTAO_DIREITO_MOUSE = 1;

    public static final int DESLIGAR = 1;
    public static final int CANCELAR_DESLIGAMENTO = 2;
    public static final int REINICIAR = 3;
    public static final int SUSPENDER = 4;
    public static final int BLOQUEAR_TELA = 5;
    public static final int AUMENTAR_VOLUME = 6;
    public static final int DIMINUIR_VOLUME = 7;
    public static final int MUDO = 8;
    public static final int CONFIGURACAO_IP = 9;
    public static final int PWD = 11;
    public static final int DATE = 12;
    public static final int HISTORY = 13;
    public static final int LISTAR_DIRETORIO = 14;

    public static String moverMouse(int x, int y) {
        String comando = "robot-mover " + x + " " + y;
        return comando;
    }

    public static String clicarMouse(int botao) {
        String comando = "robot-clicar " + botao;
        return comando;
    }

    public static String comandoTerminal(String comando) {
        String comandoTerminal = "comando-"+comando;
        return comandoTerminal;
    }

    public static String mensagem(String mensagem) {
        String echo = "comando-echo " + mensagem;
        return echo;
    }

    public static String comandoSistema(String comando) {
        String comandoSistema = "system-"+comando;
        return comandoSistema;
    }
}
