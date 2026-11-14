package br.pucpr.jakarta.model.partida;

import br.pucpr.jakarta.model.inimigos.Inimigo;
import br.pucpr.jakarta.model.inimigos.InimigoChefe;
import br.pucpr.jakarta.model.inimigos.InimigoRapido;
import br.pucpr.jakarta.model.inimigos.InimigoTanque;
import java.io.Serializable;
import java.util.ArrayList;

public class GeradorOndas implements Serializable {

    private static final long serialVersionUID = 1L;

    public Inimigo criarInimigoPorTipo(int codigoTipo, int linhaInicial, int colunaInicial) {
        Inimigo novoInimigo;
        switch (codigoTipo) {
            case 1:
                novoInimigo = new InimigoRapido(linhaInicial, colunaInicial);
                break;
            case 2:
                novoInimigo = new InimigoTanque(linhaInicial, colunaInicial);
                break;
            default:
                novoInimigo = new InimigoChefe(linhaInicial, colunaInicial);
                break;
        }
        return novoInimigo;
    }

    public ArrayList<Inimigo> gerarOnda(int numeroOnda, Celula celulaInicial) {
        ArrayList<Inimigo> onda = new ArrayList<Inimigo>();
        int l = celulaInicial.getLinha();
        int c = celulaInicial.getColuna();

        int quantidadeBase = numeroOnda + 2;
        for (int i = 0; i < quantidadeBase; i++) {
            int tipo = (i % 2 == 0) ? 1 : 2;
            onda.add(criarInimigoPorTipo(tipo, l, c));
        }

        if (numeroOnda >= 3) {
            onda.add(criarInimigoPorTipo(3, l, c));
        }
        if (numeroOnda == 5) {
            onda.add(criarInimigoPorTipo(3, l, c));
        }

        return onda;
    }
}
