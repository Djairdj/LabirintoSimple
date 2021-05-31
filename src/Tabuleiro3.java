public class Tabuleiro3 {
    private static final int QNT_LINHAS = 15;
    private static final int QNT_COLUNAS = 10;
    private static final String TETO = " ▂";
    private static final String PISO = /* "▔"; */ "█";
    private static final String PAREDE = "█";
    private static final String OBSTACULO = "⬕";
    private static final String VAZIO = "";// "✔";
    private static final String SEM_SAIDA = "X";
    private static final String START = "⛹";
    private static final String DESTINO = "Φ";
    private static final float PROBABILIDADE = 0.7f;
    private static String subindo = "▲";
    private static String descendo = "▼";
    private static String direita = "►";
    private static String esquerda = "◄";

    private static int linhaInicio;
    private static int colunaInicio;
    private static int linhaDestino;
    private static int colunaDestino;
    private static int recursiva = 0;

    private static String[][] tabuleiro;
    private static int delay = 800;

    private static void inicializarMatriz() {
        criaTabuleiro(tabuleiro);
        preencheTabuleiro(tabuleiro, OBSTACULO);
        imprimir();
        definePontos();
    }

    public static void criaTabuleiro(String[][] tabuleiro) {
        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro[i].length; j++) {
                tabuleiro[0][j] = ajuste(TETO);
                tabuleiro[tabuleiro.length - 1][j] = PISO + PISO;
            }
            tabuleiro[i][0] = ajuste(PAREDE);
            tabuleiro[i][tabuleiro[i].length - 1] = ajuste(PAREDE);
        }
        tabuleiro[tabuleiro.length - 1][0] = ajuste(PISO);
        tabuleiro[tabuleiro.length - 1][tabuleiro[0].length - 1] = ajuste(PISO + PISO);
    }

    public static void preencheTabuleiro(String[][] tabuleiro, String obstaculo) {
        for (int i = 1; i < tabuleiro.length - 1; i++) {
            for (int j = 1; j < tabuleiro[i].length - 1; j++) {
                if (Math.random() > PROBABILIDADE) {
                    tabuleiro[i][j] = ajuste(obstaculo);
                } else {
                    tabuleiro[i][j] = ajuste(VAZIO);
                }
            }
        }
    }

    private static String ajuste(String valor) {
        return String.format("%2s", valor);
    }

    private static void definePontos() {
        linhaInicio = gerarNumero(1, QNT_LINHAS - 2);
        colunaInicio = gerarNumero(1, QNT_COLUNAS - 2);
        while (!(tabuleiro[linhaInicio][colunaInicio].equals(ajuste(VAZIO)))) {
            linhaInicio = gerarNumero(1, QNT_LINHAS - 2);
            colunaInicio = gerarNumero(1, QNT_COLUNAS - 2);
        }
        tabuleiro[linhaInicio][colunaInicio] = ajuste(START);

        linhaDestino = gerarNumero(1, QNT_LINHAS - 2);
        colunaDestino = gerarNumero(1, QNT_COLUNAS - 2);
        while (!(tabuleiro[linhaDestino][colunaDestino].equals(ajuste(VAZIO)))) {
            linhaDestino = gerarNumero(1, QNT_LINHAS - 2);
            colunaDestino = gerarNumero(1, QNT_COLUNAS - 2);
        }
        tabuleiro[linhaDestino][colunaDestino] = ajuste(DESTINO);
    }

    private static int gerarNumero(int menor, int maior) {
        return (int) Math.round(Math.random() * (maior - menor)) + menor;
    }

    private static void cleanMatriz(String[][] mtz) {
        for (int y = 1; y < mtz.length - 1; y++) {
            for (int i = 1; i < mtz[y].length - 1; i++) {
                if (!mtz[y][i].equals(ajuste(OBSTACULO))) {
                    mtz[y][i] = ajuste(VAZIO);
                }
            }
        }
    }

    public static void imprimir() {
        String intersec = "\n      ";
        String setas = "      ";
        for (int i = 0; i < tabuleiro[0].length; i++) {
            intersec += ajuste(String.valueOf(i));
        }
        System.out.println(intersec);
        for (int w = 0; w < tabuleiro[0].length; w++) {
            // setas += String.format("%2s ", "⇩");
            setas += ajuste("⇩");
        }
        System.out.println(setas);
        for (int i = 0; i < QNT_LINHAS; i++) {
            System.out.printf("%2d  ⇨ ", i);
            for (int j = 0; j < QNT_COLUNAS; j++) {
                System.out.print(tabuleiro[i][j]);
            }
            System.out.println();
        }
    }

    private static boolean procurarCaminho(int linhaAtual, int colunaAtual) {
        // System.out.printf("Procura Caminho está na : Linha %d e coluna %d\n",
        // linhaAtual, colunaAtual);
        int proxLinha;
        int proxColuna;
        boolean achou = false;

        // tentando subir
        proxLinha = linhaAtual - 1;
        proxColuna = colunaAtual;
        tabuleiro[linhaAtual][colunaAtual] = ajuste(subindo);
        achou = tentarCaminho(proxLinha, proxColuna, ajuste(subindo));

        if (!achou) {
            // tentando descer
            proxLinha = linhaAtual + 1;
            proxColuna = colunaAtual;
            tabuleiro[linhaAtual][colunaAtual] = ajuste(descendo);
            achou = tentarCaminho(proxLinha, proxColuna, ajuste(descendo));
        }
        // tentando a esquerda
        if (!achou) {
            proxLinha = linhaAtual;
            proxColuna = colunaAtual - 1;
            tabuleiro[linhaAtual][colunaAtual] = ajuste(esquerda);
            achou = tentarCaminho(proxLinha, proxColuna, ajuste(esquerda));
        }
        if (!achou) {
            // tentando a direita
            proxLinha = linhaAtual;
            proxColuna = colunaAtual + 1;
            tabuleiro[linhaAtual][colunaAtual] = ajuste(direita);
            achou = tentarCaminho(proxLinha, proxColuna, ajuste(direita));
        }
        return achou;
    }

    private static boolean tentarCaminho(int proxLinha, int proxColuna, String simbolo) {
        // System.out.printf("Tentar Caminho: Linha %d e coluna %d\n", proxLinha,
        // proxColuna);
        recursiva++;
        boolean deuCerto = false;

        if (tabuleiro[proxLinha][proxColuna].equals(ajuste(DESTINO))) {
            tabuleiro[proxLinha][proxColuna] = ajuste("✔"); // ajuste("۞");
            deuCerto = true;
        } else if (posicaoVazia(proxLinha, proxColuna)) {
            // marcar
            // tabuleiro[proxLinha][proxColuna] = CAMINHO;
            // tabuleiro[proxLinha][proxColuna] = ajuste(simbolo);
            tabuleiro[proxLinha][proxColuna] = ajuste(START);
            imprimir();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            deuCerto = procurarCaminho(proxLinha, proxColuna);
            if (!deuCerto) {
                // System.out.printf("Retorno SEM SAÍDA: Linha %d e coluna %d\n", proxLinha,
                // proxColuna);
                tabuleiro[proxLinha][proxColuna] = ajuste(SEM_SAIDA); // Aqui é onde aparece o X no tabuleiro
            }
        }
        return deuCerto;
    }

    public static boolean posicaoVazia(int linha, int coluna) {
        boolean vazio = false;
        if (linha >= 0 && coluna >= 0 && linha < QNT_LINHAS && coluna < QNT_COLUNAS) {
            vazio = (tabuleiro[linha][coluna].equals(ajuste(VAZIO)));
        }
        return vazio;
    }

    private static void start() {
        tabuleiro = new String[QNT_LINHAS][QNT_COLUNAS];
        inicializarMatriz();
        imprimir();

        System.out.println("\n- Procurando solução -\n");
        boolean achou;
        boolean ctrl = true;
        while (ctrl) {
            achou = procurarCaminho(linhaInicio, colunaInicio);
            imprimir();
            if (achou) {
                System.out.println("ACHOU O CAMINHO!");
                ctrl = false;
            } else {
                System.out.println("Não tem caminho!");
                System.out.println("Definindo novos pontos.");
                try {
                    Thread.sleep(2000);
                    cleanMatriz(tabuleiro);
                    definePontos();
                    imprimir();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] a) {
        start();
        System.out.printf("Ocorreram %d recursões.\t", recursiva);
    }
}
