package br.pucpr.jakarta.model.inimigos;

import java.awt.Color;

public class InimigoRapido extends Inimigo {

    private static final long serialVersionUID = 1L;

    private boolean turboAtivo;

    public InimigoRapido(int linhaInicial, int colunaInicial) {
        super("Batedor Rápido", linhaInicial, colunaInicial, 30, 2, 15, 1, new Color(255, 140, 0));
        this.turboAtivo = true;
    }

    @Override
    protected int calcularDanoEfetivo(int danoBruto) {
        return danoBruto;
    }

    public boolean isTurboAtivo() {
        return this.turboAtivo;
    }
}
