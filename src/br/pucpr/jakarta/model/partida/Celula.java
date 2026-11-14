package br.pucpr.jakarta.model.partida;

import br.pucpr.jakarta.model.torres.Torre;
import java.io.Serializable;

public class Celula implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TIPO_GRAMA = "GRAMA";
    public static final String TIPO_CAMINHO = "CAMINHO";
    public static final String TIPO_BASE = "BASE";

    private int linha;
    private int coluna;
    private String tipo;
    private Torre torreInstalada;

    public Celula(int linha, int coluna, String tipo) {
        this.linha = linha;
        this.coluna = coluna;
        this.tipo = tipo;
        this.torreInstalada = null;
    }

    public boolean isOcupada() {
        return this.torreInstalada != null;
    }

    public boolean podeConstruir() {
        return TIPO_GRAMA.equals(this.tipo) && !isOcupada();
    }

    public int getLinha() {
        return this.linha;
    }

    public int getColuna() {
        return this.coluna;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Torre getTorreInstalada() {
        return this.torreInstalada;
    }

    public void setTorreInstalada(Torre torreInstalada) {
        this.torreInstalada = torreInstalada;
    }
}
