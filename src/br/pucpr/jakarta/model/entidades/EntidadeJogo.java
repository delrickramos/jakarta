package br.pucpr.jakarta.model.entidades;

import br.pucpr.jakarta.model.contratos.Desenhavel;
import java.awt.Color;
import java.io.Serializable;

public abstract class EntidadeJogo implements Serializable, Desenhavel {

    private static final long serialVersionUID = 1L;

    protected int linha;
    protected int coluna;
    protected Color cor;

    public EntidadeJogo(int linha, int coluna, Color cor) {
        this.linha = linha;
        this.coluna = coluna;
        this.cor = cor;
    }

    public double calcularDistancia(EntidadeJogo outra) {
        int deltaLinha = this.linha - outra.getLinha();
        int deltaColuna = this.coluna - outra.getColuna();
        return Math.sqrt((deltaLinha * deltaLinha) + (deltaColuna * deltaColuna));
    }

    public int getLinha() {
        return this.linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return this.coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public Color getCor() {
        return this.cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }
}
