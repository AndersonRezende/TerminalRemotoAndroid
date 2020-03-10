package host;

public class Desktop {
    public static final String ALTURA = "altura-";
    public static final String LARGURA = "largura-";

    public static int altura;
    public static int largura;

    public static void configurarInformacoes(String info) {
        info = info.replace("info-", "");
        String informacoes[] = info.split(";");
        String aux = "";
        for(int index = 0; index < informacoes.length; index++) {
            if(informacoes[index].contains(ALTURA)) {
                aux = informacoes[index].replace(ALTURA, "");
                altura = (int) Float.parseFloat(aux);
            }
            if(informacoes[index].contains(LARGURA)) {
                aux = informacoes[index].replace(LARGURA, "");
                largura = (int) Float.parseFloat(aux);
            }
        }
    }
}
