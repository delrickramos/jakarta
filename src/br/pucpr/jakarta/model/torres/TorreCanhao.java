package br.pucpr.jakarta.model.torres;

import br.pucpr.jakarta.model.inimigos.Inimigo;
import java.awt.Color;
import java.util.ArrayList;

public class TorreCanhao extends Torre {

    private static final long serialVersionUID = 1L;

    private int bonusExplosao;

    public TorreCanhao(int linha, int coluna) {
        super("Torre Canhão", linha, coluna, 80, 18, 2, new Color(178, 34, 34));
        this.bonusExplosao = 4;
    }

    @Override
    public String atacar(ArrayList<Inimigo> inimigos) {
        int atingidos = 0;
        int danoTotal = this.dano + this.bonusExplosao;

        for (Inimigo inimigo : inimigos) {
            if (inimigo.isVivo() && estaNoAlcance(inimigo)) {
                inimigo.receberDano(danoTotal);
                atingidos++;
            }
        }

        if (atingidos > 0) {
            return this.nome + " (Nv" + this.nivel + ") causou explosão em área em "
                    + atingidos + " inimigo(s) (-" + danoTotal + " HP bruto)";
        }
        return "";
    }

    public int getBonusExplosao() {
        return this.bonusExplosao;
    }
}
