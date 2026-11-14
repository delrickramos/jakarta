package br.pucpr.jakarta.view;

import br.pucpr.jakarta.model.inimigos.Inimigo;
import br.pucpr.jakarta.model.partida.Celula;
import br.pucpr.jakarta.model.partida.Mapa;
import br.pucpr.jakarta.model.partida.Partida;
import br.pucpr.jakarta.model.torres.Torre;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class PainelTabuleiro extends JPanel {

    public static final int TAMANHO_CELULA = 64;

    private Partida partida;
    private int linhaSelecionada;
    private int colunaSelecionada;

    public PainelTabuleiro(Partida partida) {
        this.partida = partida;
        this.linhaSelecionada = 0;
        this.colunaSelecionada = 0;
        int largura = Mapa.COLUNAS * TAMANHO_CELULA;
        int altura = Mapa.LINHAS * TAMANHO_CELULA;
        setPreferredSize(new Dimension(largura, altura));
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
        repaint();
    }

    public void setCelulaSelecionada(int linha, int coluna) {
        this.linhaSelecionada = linha;
        this.colunaSelecionada = coluna;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Mapa mapa = this.partida.getMapa();

        for (int l = 0; l < Mapa.LINHAS; l++) {
            for (int c = 0; c < Mapa.COLUNAS; c++) {
                Celula celula = mapa.getCelula(l, c);
                int x = c * TAMANHO_CELULA;
                int y = l * TAMANHO_CELULA;

                if (Celula.TIPO_CAMINHO.equals(celula.getTipo())) {
                    g2d.setColor(new Color(210, 180, 140));
                } else if (Celula.TIPO_BASE.equals(celula.getTipo())) {
                    g2d.setColor(new Color(65, 105, 225));
                } else {
                    g2d.setColor(new Color(144, 238, 144));
                }
                g2d.fillRect(x, y, TAMANHO_CELULA, TAMANHO_CELULA);

                g2d.setColor(new Color(100, 130, 100));
                g2d.drawRect(x, y, TAMANHO_CELULA, TAMANHO_CELULA);

                g2d.setColor(Color.DARK_GRAY);
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2d.drawString(l + "," + c, x + 4, y + 12);

                if (Celula.TIPO_BASE.equals(celula.getTipo())) {
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
                    g2d.drawString("BASE", x + 15, y + 38);
                }
            }
        }

        if (mapa.coordenadaValida(this.linhaSelecionada, this.colunaSelecionada)) {
            int sx = this.colunaSelecionada * TAMANHO_CELULA;
            int sy = this.linhaSelecionada * TAMANHO_CELULA;
            g2d.setColor(new Color(255, 215, 0));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(sx + 1, sy + 1, TAMANHO_CELULA - 3, TAMANHO_CELULA - 3);
            g2d.setStroke(new BasicStroke(1));
        }

        for (Torre torre : this.partida.getTorres()) {
            torre.desenhar(g2d, TAMANHO_CELULA);
        }

        for (Inimigo inimigo : this.partida.getInimigosAtivos()) {
            inimigo.desenhar(g2d, TAMANHO_CELULA);
        }
    }
}
