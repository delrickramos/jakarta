package br.pucpr.jakarta.model.partida;

import java.io.Serializable;
import java.util.ArrayList;

public class Mapa implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int LINHAS = 6;
    public static final int COLUNAS = 10;

    private Celula[][] grade;
    private ArrayList<Celula> caminhoInimigos;

    public Mapa() {
        this.grade = new Celula[LINHAS][COLUNAS];
        this.caminhoInimigos = new ArrayList<Celula>();
        inicializarMapaPadrao();
    }

    private void inicializarMapaPadrao() {
        for (int l = 0; l < LINHAS; l++) {
            for (int c = 0; c < COLUNAS; c++) {
                this.grade[l][c] = new Celula(l, c, Celula.TIPO_GRAMA);
            }
        }

        int[][] coordenadasCaminho = {
            {1, 0}, {1, 1}, {1, 2}, {1, 3},
            {2, 3}, {3, 3}, {4, 3},
            {4, 4}, {4, 5}, {4, 6},
            {3, 6}, {2, 6},
            {2, 7}, {2, 8}, {2, 9}
        };

        for (int i = 0; i < coordenadasCaminho.length; i++) {
            int l = coordenadasCaminho[i][0];
            int c = coordenadasCaminho[i][1];
            Celula celula = this.grade[l][c];
            if (i == coordenadasCaminho.length - 1) {
                celula.setTipo(Celula.TIPO_BASE);
            } else {
                celula.setTipo(Celula.TIPO_CAMINHO);
            }
            this.caminhoInimigos.add(celula);
        }
    }

    public boolean coordenadaValida(int linha, int coluna) {
        return linha >= 0 && linha < LINHAS && coluna >= 0 && coluna < COLUNAS;
    }

    public Celula getCelula(int linha, int coluna) {
        if (!coordenadaValida(linha, coluna)) {
            return null;
        }
        return this.grade[linha][coluna];
    }

    public ArrayList<Celula> getCaminhoInimigos() {
        return this.caminhoInimigos;
    }
}
