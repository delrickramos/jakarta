package br.pucpr.jakarta.model.torres;

import br.pucpr.jakarta.model.inimigos.Inimigo;
import java.awt.Color;
import java.util.ArrayList;

public class TorreGelo extends Torre {

    private static final long serialVersionUID = 1L;

    private boolean congelaEmArea;

    public TorreGelo(int linha, int coluna) {
        super("Torre de Gelo", linha, coluna, 60, 8, 2, new Color(30, 144, 255));
        this.congelaEmArea = false;
    }

    @Override
    public void melhorar() {
        super.melhorar();
        if (this.nivel >= 2) {
            this.congelaEmArea = true;
        }
    }

    @Override
    public String atacar(ArrayList<Inimigo> inimigos) {
        StringBuilder log = new StringBuilder();

        for (Inimigo inimigo : inimigos) {
            if (inimigo.isVivo() && estaNoAlcance(inimigo)) {
                inimigo.receberDano(this.dano);
                inimigo.setLento(true);

                if (log.length() > 0) {
                    log.append(" | ");
                }
                log.append(this.nome).append(" (Nv").append(this.nivel)
                   .append(") congelou ").append(inimigo.getNome())
                   .append(" (-").append(this.dano).append(" HP + Lentidão)");

                if (!this.congelaEmArea) {
                    break;
                }
            }
        }
        return log.toString();
    }

    public boolean isCongelaEmArea() {
        return this.congelaEmArea;
    }
}
