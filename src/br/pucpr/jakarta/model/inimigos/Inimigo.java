package br.pucpr.jakarta.model.inimigos;

import br.pucpr.jakarta.model.entidades.EntidadeJogo;
import br.pucpr.jakarta.model.partida.Celula;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class Inimigo extends EntidadeJogo {

    private static final long serialVersionUID = 1L;

    protected String nome;
    protected int vidaAtual;
    protected int vidaMaxima;
    protected int velocidade;
    protected int recompensaMoedas;
    protected int danoBase;
    protected boolean lento;
    protected int indiceCaminhoAtual;

    public Inimigo(String nome, int linha, int coluna, int vidaMaxima, int velocidade,
                   int recompensaMoedas, int danoBase, Color cor) {
        super(linha, coluna, cor);
        this.nome = nome;
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.velocidade = velocidade;
        this.recompensaMoedas = recompensaMoedas;
        this.danoBase = danoBase;
        this.lento = false;
        this.indiceCaminhoAtual = 0;
    }

    public void receberDano(int danoBruto) {
        int danoReal = calcularDanoEfetivo(danoBruto);
        this.vidaAtual -= danoReal;
        if (this.vidaAtual < 0) {
            this.vidaAtual = 0;
        }
    }

    protected abstract int calcularDanoEfetivo(int danoBruto);

    public boolean avancarNoCaminho(ArrayList<Celula> caminho) {
        if (caminho == null || caminho.isEmpty()) {
            return false;
        }

        int passos = this.lento ? 1 : this.velocidade;
        this.lento = false;

        this.indiceCaminhoAtual += passos;
        if (this.indiceCaminhoAtual >= caminho.size() - 1) {
            this.indiceCaminhoAtual = caminho.size() - 1;
            Celula destino = caminho.get(this.indiceCaminhoAtual);
            this.linha = destino.getLinha();
            this.coluna = destino.getColuna();
            return true;
        }

        Celula novaPosicao = caminho.get(this.indiceCaminhoAtual);
        this.linha = novaPosicao.getLinha();
        this.coluna = novaPosicao.getColuna();
        return false;
    }

    public boolean isVivo() {
        return this.vidaAtual > 0;
    }

    @Override
    public void desenhar(Graphics2D g2d, int tamanhoCelula) {
        int x = this.coluna * tamanhoCelula;
        int y = this.linha * tamanhoCelula;

        g2d.setColor(this.cor);
        g2d.fillOval(x + 14, y + 16, tamanhoCelula - 28, tamanhoCelula - 28);

        if (this.lento) {
            g2d.setColor(Color.CYAN);
        } else {
            g2d.setColor(Color.BLACK);
        }
        g2d.drawOval(x + 14, y + 16, tamanhoCelula - 28, tamanhoCelula - 28);

        // Barra de vida acima do inimigo
        int larguraBarra = tamanhoCelula - 16;
        int vidaProporcional = (int) ((double) this.vidaAtual / this.vidaMaxima * larguraBarra);

        g2d.setColor(Color.RED);
        g2d.fillRect(x + 8, y + 6, larguraBarra, 6);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(x + 8, y + 6, Math.max(0, vidaProporcional), 6);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x + 8, y + 6, larguraBarra, 6);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 10));
        g2d.drawString(String.valueOf(this.vidaAtual), x + 22, y + 38);
    }

    public String getNome() {
        return this.nome;
    }

    public int getVidaAtual() {
        return this.vidaAtual;
    }

    public int getVidaMaxima() {
        return this.vidaMaxima;
    }

    public int getVelocidade() {
        return this.velocidade;
    }

    public int getRecompensaMoedas() {
        return this.recompensaMoedas;
    }

    public int getDanoBase() {
        return this.danoBase;
    }

    public boolean isLento() {
        return this.lento;
    }

    public void setLento(boolean lento) {
        this.lento = lento;
    }
}
