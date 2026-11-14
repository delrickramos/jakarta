package br.pucpr.jakarta.view;

import br.pucpr.jakarta.exception.AcaoInvalidaException;
import br.pucpr.jakarta.exception.PosicaoInvalidaException;
import br.pucpr.jakarta.exception.SaldoInsuficienteException;
import br.pucpr.jakarta.model.partida.Partida;
import br.pucpr.jakarta.model.torres.Torre;
import br.pucpr.jakarta.model.torres.TorreArqueiro;
import br.pucpr.jakarta.model.torres.TorreCanhao;
import br.pucpr.jakarta.model.torres.TorreGelo;
import br.pucpr.jakarta.persistence.GerenciadorPersistencia;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

public class JanelaPrincipal extends JFrame {

    private Partida partida;
    private PainelTabuleiro painelTabuleiro;
    private Timer timerOnda;

    private JLabel lblStatus;
    private JRadioButton rbArqueiro;
    private JRadioButton rbCanhao;
    private JRadioButton rbGelo;
    private JTextField txtLinha;
    private JTextField txtColuna;
    private JTextArea areaLog;

    public JanelaPrincipal() {
        super("Jakarta Tower Defense");
        String nome = JOptionPane.showInputDialog(
                this,
                "Digite o nome do Defensor:",
                "Jakarta Tower Defense",
                JOptionPane.QUESTION_MESSAGE
        );
        this.partida = new Partida(nome);

        configurarMenu();
        configurarComponentes();
        configurarTimer();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        atualizarTela();
    }

    private void configurarMenu() {
        JMenuBar barraMenu = new JMenuBar();
        JMenu menuJogo = new JMenu("Jogo");

        JMenuItem itemNovo = new JMenuItem("Novo Jogo");
        itemNovo.addActionListener(e -> iniciarNovoJogo());

        JMenuItem itemSalvar = new JMenuItem("Salvar Jogo");
        itemSalvar.addActionListener(e -> salvarJogoAtual());

        JMenuItem itemCarregar = new JMenuItem("Carregar Jogo");
        itemCarregar.addActionListener(e -> carregarJogoSalvo());

        JMenuItem itemRanking = new JMenuItem("Ver Ranking");
        itemRanking.addActionListener(e -> exibirRanking());

        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));

        menuJogo.add(itemNovo);
        menuJogo.add(itemSalvar);
        menuJogo.add(itemCarregar);
        menuJogo.add(itemRanking);
        menuJogo.addSeparator();
        menuJogo.add(itemSair);

        barraMenu.add(menuJogo);
        setJMenuBar(barraMenu);
    }

    private void configurarComponentes() {
        setLayout(new BorderLayout(8, 8));

        JPanel painelTopo = new JPanel();
        painelTopo.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        this.lblStatus = new JLabel();
        this.lblStatus.setFont(new Font("SansSerif", Font.BOLD, 14));
        painelTopo.add(this.lblStatus);
        add(painelTopo, BorderLayout.NORTH);

        this.painelTabuleiro = new PainelTabuleiro(this.partida);
        this.painelTabuleiro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / PainelTabuleiro.TAMANHO_CELULA;
                int lin = e.getY() / PainelTabuleiro.TAMANHO_CELULA;
                txtLinha.setText(String.valueOf(lin));
                txtColuna.setText(String.valueOf(col));
                painelTabuleiro.setCelulaSelecionada(lin, col);
            }
        });
        add(this.painelTabuleiro, BorderLayout.CENTER);

        JPanel painelLateral = new JPanel(new GridLayout(11, 1, 5, 5));
        painelLateral.setBorder(BorderFactory.createTitledBorder("Loja e Controle"));

        this.rbArqueiro = new JRadioButton("Torre Arqueiro ($50)", true);
        this.rbCanhao = new JRadioButton("Torre Canhão ($80)");
        this.rbGelo = new JRadioButton("Torre de Gelo ($60)");

        ButtonGroup grupoTorres = new ButtonGroup();
        grupoTorres.add(this.rbArqueiro);
        grupoTorres.add(this.rbCanhao);
        grupoTorres.add(this.rbGelo);

        JPanel painelCoords = new JPanel(new GridLayout(1, 4, 4, 4));
        painelCoords.add(new JLabel("Linha:"));
        this.txtLinha = new JTextField("0", 3);
        painelCoords.add(this.txtLinha);
        painelCoords.add(new JLabel("Col:"));
        this.txtColuna = new JTextField("0", 3);
        painelCoords.add(this.txtColuna);

        JButton btnConstruir = new JButton("Construir Torre");
        btnConstruir.addActionListener(e -> acaoConstruirTorre());

        JButton btnMelhorar = new JButton("Melhorar Torre (Upgrade)");
        btnMelhorar.addActionListener(e -> acaoMelhorarTorre());

        JButton btnVender = new JButton("Vender Torre (50%)");
        btnVender.addActionListener(e -> acaoVenderTorre());

        JButton btnIniciarOnda = new JButton("Iniciar Próxima Onda");
        btnIniciarOnda.addActionListener(e -> acaoIniciarOnda());

        JButton btnSalvar = new JButton("Salvar Jogo");
        btnSalvar.addActionListener(e -> salvarJogoAtual());

        JButton btnCarregar = new JButton("Carregar Jogo");
        btnCarregar.addActionListener(e -> carregarJogoSalvo());

        painelLateral.add(new JLabel("Selecione a Torre (ou clique no mapa):"));
        painelLateral.add(this.rbArqueiro);
        painelLateral.add(this.rbCanhao);
        painelLateral.add(this.rbGelo);
        painelLateral.add(painelCoords);
        painelLateral.add(btnConstruir);
        painelLateral.add(btnMelhorar);
        painelLateral.add(btnVender);
        painelLateral.add(btnIniciarOnda);
        painelLateral.add(btnSalvar);
        painelLateral.add(btnCarregar);

        add(painelLateral, BorderLayout.EAST);

        this.areaLog = new JTextArea(6, 50);
        this.areaLog.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(this.areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Histórico de Combate"));
        add(scrollLog, BorderLayout.SOUTH);
    }

    private void configurarTimer() {
        this.timerOnda = new Timer(650, e -> {
            if (this.partida.isOndaEmAndamento()) {
                this.partida.executarTurnoCombate();
                atualizarTela();
                verificarFimDePartida();
            } else {
                this.timerOnda.stop();
                atualizarTela();
                verificarFimDePartida();
            }
        });
    }

    private void acaoConstruirTorre() {
        try {
            int linha = Integer.parseInt(this.txtLinha.getText().trim());
            int coluna = Integer.parseInt(this.txtColuna.getText().trim());

            Torre novaTorre;
            if (this.rbArqueiro.isSelected()) {
                novaTorre = new TorreArqueiro(linha, coluna);
            } else if (this.rbCanhao.isSelected()) {
                novaTorre = new TorreCanhao(linha, coluna);
            } else {
                novaTorre = new TorreGelo(linha, coluna);
            }

            this.partida.construirTorre(novaTorre);
            this.painelTabuleiro.setCelulaSelecionada(linha, coluna);
            atualizarTela();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite números inteiros válidos para Linha e Coluna!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (PosicaoInvalidaException | SaldoInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Ação Inválida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void acaoMelhorarTorre() {
        try {
            int linha = Integer.parseInt(this.txtLinha.getText().trim());
            int coluna = Integer.parseInt(this.txtColuna.getText().trim());
            this.partida.melhorarTorre(linha, coluna);
            atualizarTela();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite números inteiros válidos para Linha e Coluna!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (PosicaoInvalidaException | AcaoInvalidaException | SaldoInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void acaoVenderTorre() {
        try {
            int linha = Integer.parseInt(this.txtLinha.getText().trim());
            int coluna = Integer.parseInt(this.txtColuna.getText().trim());
            this.partida.venderTorre(linha, coluna);
            atualizarTela();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite números inteiros válidos para Linha e Coluna!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (PosicaoInvalidaException | AcaoInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void acaoIniciarOnda() {
        try {
            this.partida.iniciarProximaOnda();
            atualizarTela();
            this.timerOnda.start();
        } catch (AcaoInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void iniciarNovoJogo() {
        this.timerOnda.stop();
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do Defensor:", "Novo Jogo", JOptionPane.QUESTION_MESSAGE);
        this.partida = new Partida(nome);
        this.painelTabuleiro.setPartida(this.partida);
        atualizarTela();
    }

    private void salvarJogoAtual() {
        try {
            GerenciadorPersistencia.salvarPartida(this.partida, GerenciadorPersistencia.ARQUIVO_SAVE_PADRAO);
            JOptionPane.showMessageDialog(this, "Partida salva com sucesso em: " + GerenciadorPersistencia.ARQUIVO_SAVE_PADRAO);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar partida: " + ex.getMessage(), "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarJogoSalvo() {
        try {
            this.timerOnda.stop();
            this.partida = GerenciadorPersistencia.carregarPartida(GerenciadorPersistencia.ARQUIVO_SAVE_PADRAO);
            this.painelTabuleiro.setPartida(this.partida);
            atualizarTela();
            if (this.partida.isOndaEmAndamento()) {
                this.timerOnda.start();
            }
            JOptionPane.showMessageDialog(this, "Partida carregada com sucesso!");
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível carregar o jogo salvo: " + ex.getMessage(), "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirRanking() {
        try {
            ArrayList<String> ranking = GerenciadorPersistencia.lerRanking();
            if (ranking.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma pontuação registrada no ranking ainda.");
            } else {
                StringBuilder sb = new StringBuilder("=== Ranking de Defensores de Jakarta ===\n\n");
                for (String linha : ranking) {
                    sb.append(linha).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao ler ranking: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verificarFimDePartida() {
        if (this.partida.isGameOver()) {
            this.timerOnda.stop();
            salvarPontuacaoFinal();
            JOptionPane.showMessageDialog(this, "Fim de Jogo! A Base de Jakarta foi destruída.\nPontuação Final: " + this.partida.getPontuacao(), "Derrota", JOptionPane.ERROR_MESSAGE);
        } else if (this.partida.isVitoria()) {
            this.timerOnda.stop();
            salvarPontuacaoFinal();
            JOptionPane.showMessageDialog(this, "Parabéns, " + this.partida.getNomeJogador() + "! Você defendeu Jakarta em todas as 5 ondas!\nPontuação Final: " + this.partida.getPontuacao(), "Vitória!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void salvarPontuacaoFinal() {
        try {
            GerenciadorPersistencia.salvarNoRanking(this.partida.getNomeJogador(), this.partida.getPontuacao(), this.partida.getOndaAtual());
        } catch (IOException ex) {
            // Falha silenciosa ao gravar ranking
        }
    }

    private void atualizarTela() {
        this.lblStatus.setText(String.format(
                "Defensor: %s   |   Moedas: $%d   |   Vida da Base: %d   |   Onda: %d/%d   |   Pontos: %d",
                this.partida.getNomeJogador(),
                this.partida.getMoedas(),
                this.partida.getVidaBase(),
                this.partida.getOndaAtual(),
                Partida.TOTAL_ONDAS,
                this.partida.getPontuacao()
        ));

        StringBuilder sb = new StringBuilder();
        ArrayList<String> logs = this.partida.getHistoricoCombate();
        int inicio = Math.max(0, logs.size() - 12);
        for (int i = inicio; i < logs.size(); i++) {
            sb.append(logs.get(i)).append("\n");
        }
        this.areaLog.setText(sb.toString());
        this.painelTabuleiro.repaint();
    }
}
