package br.pucpr.jakarta;

import br.pucpr.jakarta.view.JanelaPrincipal;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JanelaPrincipal());
    }
}
