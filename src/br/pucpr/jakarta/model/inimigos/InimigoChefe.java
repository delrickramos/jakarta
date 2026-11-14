package br.pucpr.jakarta.model.inimigos;

import br.pucpr.jakarta.model.partida.Celula;
import java.awt.Color;
import java.util.ArrayList;

public class InimigoChefe extends Inimigo {

    private static final long serialVersionUID = 1L;

    private int regeneracaoPorTurno;

    public InimigoChefe(int linhaInicial, int colunaInicial) {
        super("Chefe de Jakarta", linhaInicial, colunaInicial, 140, 1, 60, 5, new Color(138, 43, 226));
        this.regeneracaoPorTurno = 5;
    }

    @Override
    protected int calcularDanoEfetivo(int danoBruto) {
        return (int) Math.max(1, danoBruto * 0.7);
    }

    @Override
    public boolean avancarNoCaminho(ArrayList<Celula> caminho) {
        if (isVivo() && this.vidaAtual < this.vidaMaxima) {
            this.vidaAtual = Math.min(this.vidaMaxima, this.vidaAtual + this.regeneracaoPorTurno);
        }
        return super.avancarNoCaminho(caminho);
    }

    public int getRegeneracaoPorTurno() {
        return this.regeneracaoPorTurno;
    }
}
