package br.pucpr.jakarta.model.inimigos;

import java.awt.Color;

public class InimigoTanque extends Inimigo {

    private static final long serialVersionUID = 1L;

    private int armadura;

    public InimigoTanque(int linhaInicial, int colunaInicial) {
        super("Golias Blindado", linhaInicial, colunaInicial, 70, 1, 25, 2, new Color(105, 105, 105));
        this.armadura = 4;
    }

    @Override
    protected int calcularDanoEfetivo(int danoBruto) {
        int liquido = danoBruto - this.armadura;
        return Math.max(1, liquido);
    }

    public int getArmadura() {
        return this.armadura;
    }
}
