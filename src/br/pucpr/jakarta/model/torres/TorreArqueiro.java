package br.pucpr.jakarta.model.torres;

import br.pucpr.jakarta.model.inimigos.Inimigo;
import java.awt.Color;
import java.util.ArrayList;

public class TorreArqueiro extends Torre {

    private static final long serialVersionUID = 1L;

    private int flechasPorTurno;

    public TorreArqueiro(int linha, int coluna) {
        super("Torre Arqueiro", linha, coluna, 50, 12, 3, new Color(34, 139, 34));
        this.flechasPorTurno = 2;
    }

    @Override
    public String atacar(ArrayList<Inimigo> inimigos) {
        int disparos = 0;
        StringBuilder log = new StringBuilder();

        for (Inimigo inimigo : inimigos) {
            if (inimigo.isVivo() && estaNoAlcance(inimigo)) {
                inimigo.receberDano(this.dano);
                disparos++;
                if (log.length() > 0) {
                    log.append(" | ");
                }
                log.append(this.nome).append(" (Nv").append(this.nivel)
                   .append(") disparou flecha em ").append(inimigo.getNome())
                   .append(" (-").append(this.dano).append(" HP)");

                if (disparos >= this.flechasPorTurno) {
                    break;
                }
            }
        }
        return log.toString();
    }

    public int getFlechasPorTurno() {
        return this.flechasPorTurno;
    }
}
