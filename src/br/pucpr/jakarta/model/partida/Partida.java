package br.pucpr.jakarta.model.partida;

import br.pucpr.jakarta.exception.AcaoInvalidaException;
import br.pucpr.jakarta.exception.PosicaoInvalidaException;
import br.pucpr.jakarta.exception.SaldoInsuficienteException;
import br.pucpr.jakarta.model.inimigos.Inimigo;
import br.pucpr.jakarta.model.torres.Torre;
import java.io.Serializable;
import java.util.ArrayList;

public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int TOTAL_ONDAS = 5;

    private String nomeJogador;
    private int moedas;
    private int vidaBase;
    private int ondaAtual;
    private int pontuacao;

    private Mapa mapa;
    private GeradorOndas geradorOndas;
    private ArrayList<Torre> torres;
    private ArrayList<Inimigo> inimigosAtivos;
    private ArrayList<String> historicoCombate;

    public Partida(String nomeJogador) {
        this.nomeJogador = (nomeJogador == null || nomeJogador.trim().isEmpty()) ? "Defensor" : nomeJogador.trim();
        this.moedas = 150;
        this.vidaBase = 20;
        this.ondaAtual = 0;
        this.pontuacao = 0;

        this.mapa = new Mapa();
        this.geradorOndas = new GeradorOndas();
        this.torres = new ArrayList<Torre>();
        this.inimigosAtivos = new ArrayList<Inimigo>();
        this.historicoCombate = new ArrayList<String>();
        registrarLog("Partida iniciada para o jogador: " + this.nomeJogador);
    }

    public void construirTorre(Torre novaTorre) throws PosicaoInvalidaException, SaldoInsuficienteException {
        int l = novaTorre.getLinha();
        int c = novaTorre.getColuna();

        if (!this.mapa.coordenadaValida(l, c)) {
            throw new PosicaoInvalidaException("Coordenada (" + l + ", " + c + ") está fora dos limites do mapa!");
        }

        Celula celula = this.mapa.getCelula(l, c);
        if (!celula.podeConstruir()) {
            throw new PosicaoInvalidaException("Não é possível construir em (" + l + ", " + c + "). Escolha uma célula de grama livre!");
        }

        if (this.moedas < novaTorre.getCusto()) {
            throw new SaldoInsuficienteException("Saldo insuficiente! Custo da torre: $" + novaTorre.getCusto() + " | Suas moedas: $" + this.moedas);
        }

        this.moedas -= novaTorre.getCusto();
        celula.setTorreInstalada(novaTorre);
        this.torres.add(novaTorre);
        registrarLog("Construída " + novaTorre.getNome() + " na posição (" + l + ", " + c + ").");
    }

    public void melhorarTorre(int linha, int coluna) throws PosicaoInvalidaException, AcaoInvalidaException, SaldoInsuficienteException {
        if (!this.mapa.coordenadaValida(linha, coluna)) {
            throw new PosicaoInvalidaException("Coordenada inválida!");
        }

        Celula celula = this.mapa.getCelula(linha, coluna);
        Torre torre = celula.getTorreInstalada();
        if (torre == null) {
            throw new AcaoInvalidaException("Não existe nenhuma torre instalada na posição (" + linha + ", " + coluna + ")!");
        }

        if (torre.getNivel() >= 3) {
            throw new AcaoInvalidaException("A " + torre.getNome() + " já está no nível máximo (Nv 3)!");
        }

        int custoUpgrade = torre.getCustoMelhoria();
        if (this.moedas < custoUpgrade) {
            throw new SaldoInsuficienteException("Moedas insuficientes para melhorar! Custo: $" + custoUpgrade);
        }

        this.moedas -= custoUpgrade;
        torre.melhorar();
        registrarLog(torre.getNome() + " em (" + linha + ", " + coluna + ") subiu para o Nível " + torre.getNivel() + "!");
    }

    public void venderTorre(int linha, int coluna) throws PosicaoInvalidaException, AcaoInvalidaException {
        if (!this.mapa.coordenadaValida(linha, coluna)) {
            throw new PosicaoInvalidaException("Coordenada inválida!");
        }

        Celula celula = this.mapa.getCelula(linha, coluna);
        Torre torre = celula.getTorreInstalada();
        if (torre == null) {
            throw new AcaoInvalidaException("Não há torre para vender na posição (" + linha + ", " + coluna + ")!");
        }

        int reembolso = torre.getCusto() / 2;
        this.moedas += reembolso;
        this.torres.remove(torre);
        celula.setTorreInstalada(null);
        registrarLog(torre.getNome() + " vendida por $" + reembolso + ".");
    }

    public void iniciarProximaOnda() throws AcaoInvalidaException {
        if (isGameOver() || isVitoria()) {
            throw new AcaoInvalidaException("A partida já foi encerrada!");
        }
        if (!this.inimigosAtivos.isEmpty()) {
            throw new AcaoInvalidaException("Aguarde todos os inimigos da onda atual serem derrotados!");
        }
        if (this.ondaAtual >= TOTAL_ONDAS) {
            throw new AcaoInvalidaException("Todas as ondas já foram concluídas!");
        }

        this.ondaAtual++;
        Celula inicio = this.mapa.getCaminhoInimigos().get(0);
        this.inimigosAtivos = this.geradorOndas.gerarOnda(this.ondaAtual, inicio);
        registrarLog("=== Onda " + this.ondaAtual + " iniciada com " + this.inimigosAtivos.size() + " inimigos! ===");
    }

    public void executarTurnoCombate() {
        if (this.inimigosAtivos.isEmpty() || isGameOver()) {
            return;
        }

        for (Torre torre : this.torres) {
            String logAtaque = torre.atacar(this.inimigosAtivos);
            if (logAtaque != null && !logAtaque.isEmpty()) {
                registrarLog(logAtaque);
            }
        }

        ArrayList<Inimigo> derrotados = new ArrayList<Inimigo>();
        for (Inimigo inimigo : this.inimigosAtivos) {
            if (!inimigo.isVivo()) {
                this.moedas += inimigo.getRecompensaMoedas();
                this.pontuacao += inimigo.getRecompensaMoedas() * 10;
                derrotados.add(inimigo);
                registrarLog(inimigo.getNome() + " derrotado! (+" + inimigo.getRecompensaMoedas() + " moedas)");
            }
        }
        this.inimigosAtivos.removeAll(derrotados);

        ArrayList<Inimigo> invasoresBase = new ArrayList<Inimigo>();
        for (Inimigo inimigo : this.inimigosAtivos) {
            boolean chegouNaBase = inimigo.avancarNoCaminho(this.mapa.getCaminhoInimigos());
            if (chegouNaBase) {
                this.vidaBase -= inimigo.getDanoBase();
                if (this.vidaBase < 0) {
                    this.vidaBase = 0;
                }
                invasoresBase.add(inimigo);
                registrarLog("ALERTA: " + inimigo.getNome() + " invadiu a Base de Jakarta! (-" + inimigo.getDanoBase() + " vida)");
            }
        }
        this.inimigosAtivos.removeAll(invasoresBase);
    }

    public void registrarLog(String mensagem) {
        this.historicoCombate.add(mensagem);
    }

    public boolean isGameOver() {
        return this.vidaBase <= 0;
    }

    public boolean isVitoria() {
        return this.vidaBase > 0 && this.ondaAtual >= TOTAL_ONDAS && this.inimigosAtivos.isEmpty();
    }

    public boolean isOndaEmAndamento() {
        return !this.inimigosAtivos.isEmpty();
    }

    public String getNomeJogador() {
        return this.nomeJogador;
    }

    public int getMoedas() {
        return this.moedas;
    }

    public int getVidaBase() {
        return this.vidaBase;
    }

    public int getOndaAtual() {
        return this.ondaAtual;
    }

    public int getPontuacao() {
        return this.pontuacao;
    }

    public Mapa getMapa() {
        return this.mapa;
    }

    public ArrayList<Torre> getTorres() {
        return this.torres;
    }

    public ArrayList<Inimigo> getInimigosAtivos() {
        return this.inimigosAtivos;
    }

    public ArrayList<String> getHistoricoCombate() {
        return this.historicoCombate;
    }
}
