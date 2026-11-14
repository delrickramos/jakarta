package br.pucpr.jakarta.persistence;

import br.pucpr.jakarta.model.partida.Partida;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GerenciadorPersistencia {

    public static final String ARQUIVO_SAVE_PADRAO = "saves/partida_jakarta.dat";
    public static final String ARQUIVO_RANKING = "saves/ranking_jakarta.txt";

    public static void salvarPartida(Partida partida, String caminhoArquivo) throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(partida);
        }
    }

    public static Partida carregarPartida(String caminhoArquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(caminhoArquivo))) {
            return (Partida) ois.readObject();
        }
    }

    public static void salvarNoRanking(String nomeJogador, int pontuacao, int ondaAlcancada) throws IOException {
        File arquivo = new File(ARQUIVO_RANKING);
        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo, true))) {
            bw.write("Jogador: " + nomeJogador + " | Pontos: " + pontuacao + " | Onda: " + ondaAlcancada);
            bw.newLine();
        }
    }

    public static ArrayList<String> lerRanking() throws IOException {
        ArrayList<String> linhas = new ArrayList<String>();
        File arquivo = new File(ARQUIVO_RANKING);
        if (!arquivo.exists()) {
            return linhas;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        }
        return linhas;
    }
}
