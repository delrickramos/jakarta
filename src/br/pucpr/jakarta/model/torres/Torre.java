package br.pucpr.jakarta.model.torres;

import br.pucpr.jakarta.model.contratos.Melhoravel;
import br.pucpr.jakarta.model.entidades.EntidadeJogo;
import br.pucpr.jakarta.model.inimigos.Inimigo;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class Torre extends EntidadeJogo implements Melhoravel {

    private static final long serialVersionUID = 1L;

    protected String nome;
    protected int custo;
    protected int dano;
    protected int alcance;
    protected int nivel;

    public Torre(String nome, int linha, int coluna, int custo, int dano, int alcance, Color cor) {
        super(linha, coluna, cor);
        this.nome = nome;
        this.custo = custo;
        this.dano = dano;
        this.alcance = alcance;
        this.nivel = 1;
    }

    public boolean estaNoAlcance(Inimigo inimigo) {
        return calcularDistancia(inimigo) <= this.alcance;
    }

    public abstract String atacar(ArrayList<Inimigo> inimigos);

    @Override
    public void melhorar() {
        this.nivel++;
        this.dano += 6;
    }

    @Override
    public int getCustoMelhoria() {
        return (this.custo / 2) * this.nivel;
    }

    @Override
    public int getNivel() {
        return this.nivel;
    }

    @Override
    public void desenhar(Graphics2D g2d, int tamanhoCelula) {
        int x = this.coluna * tamanhoCelula;
        int y = this.linha * tamanhoCelula;

        g2d.setColor(this.cor);
        g2d.fillRect(x + 10, y + 10, tamanhoCelula - 20, tamanhoCelula - 20);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x + 10, y + 10, tamanhoCelula - 20, tamanhoCelula - 20);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.drawString("Nv" + this.nivel, x + 20, y + 36);
    }

    public String getNome() {
        return this.nome;
    }

    public int getCusto() {
        return this.custo;
    }

    public int getDano() {
        return this.dano;
    }

    public int getAlcance() {
        return this.alcance;
    }
}
