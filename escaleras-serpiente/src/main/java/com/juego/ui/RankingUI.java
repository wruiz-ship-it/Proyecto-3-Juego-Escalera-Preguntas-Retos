package com.juego.ui;

import com.juego.model.Jugador;
import com.juego.service.TableroService;

import javax.swing.*;
import java.awt.*;

public class RankingUI extends JFrame {

    private final TableroService service;

    public RankingUI(TableroService service) {
        this.service = service;
        setTitle("🏆 Resultado Final");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 540);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel root = UITheme.crearPanel(new BorderLayout(0, 16));
        root.setBorder(BorderFactory.createEmptyBorder(30, 36, 24, 36));
        setContentPane(root);

        // Header
        JPanel header = new JPanel(new GridLayout(3, 1, 0, 6));
        header.setBackground(UITheme.BG_DARK);
        JLabel trofeo = new JLabel("🏆", SwingConstants.CENTER);
        trofeo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        JLabel titulo = UITheme.crearTitulo("¡Fin del Juego!");

        Jugador ganador = service.getGanador();
        String textoGanador = ganador != null ? "Ganador: " + ganador.getNombre() : "Juego terminado";
        JLabel lblGanador = new JLabel(textoGanador, SwingConstants.CENTER);
        lblGanador.setFont(UITheme.FONT_H2);
        lblGanador.setForeground(UITheme.ACCENT_GREEN);
        header.add(trofeo);
        header.add(titulo);
        header.add(lblGanador);
        root.add(header, BorderLayout.NORTH);

        // Ranking table
        Jugador[] ranking = service.getRanking();
        JPanel rankPanel = new JPanel(new GridLayout(0, 1, 0, 8));
        rankPanel.setBackground(UITheme.BG_DARK);
        rankPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));

        String[] medallas = {"🥇", "🥈", "🥉", "4️⃣"};
        for (int i = 0; i < ranking.length; i++) {
            Jugador j = ranking[i];
            if (j == null) continue;
            JPanel card = new JPanel(new BorderLayout(12, 0));
            Color color = UITheme.JUGADOR_COLORS[Math.min(i, UITheme.JUGADOR_COLORS.length - 1)];
            card.setBackground(UITheme.BG_CARD);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 2),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)));

            JLabel medalla = new JLabel(i < medallas.length ? medallas[i] : (i + 1) + ".");
            medalla.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

            JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
            info.setBackground(UITheme.BG_CARD);
            JLabel nombre = new JLabel(j.getNombre());
            nombre.setFont(new Font("SansSerif", Font.BOLD, 15));
            nombre.setForeground(UITheme.TEXT_PRIMARY);

            JLabel stats = new JLabel("Posición: " + j.getPosicion() +
                    " | Aciertos: " + j.getAciertos() +
                    " | Errores: " + j.getErrores() +
                    " | Puntaje: " + j.getPuntaje());
            stats.setFont(UITheme.FONT_SMALL);
            stats.setForeground(UITheme.TEXT_MUTED);
            info.add(nombre);
            info.add(stats);

            JLabel pts = new JLabel(j.getPuntaje() + " pts", SwingConstants.RIGHT);
            pts.setFont(new Font("SansSerif", Font.BOLD, 18));
            pts.setForeground(UITheme.ACCENT_GOLD);

            card.add(medalla, BorderLayout.WEST);
            card.add(info, BorderLayout.CENTER);
            card.add(pts, BorderLayout.EAST);
            rankPanel.add(card);
        }

        JScrollPane scroll = new JScrollPane(rankPanel);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.ACCENT_BLUE, 1));
        scroll.setBackground(UITheme.BG_DARK);
        root.add(scroll, BorderLayout.CENTER);

        // Botones
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        btnPanel.setBackground(UITheme.BG_DARK);
        JButton btnNuevo = UITheme.crearBoton("🎮  Nuevo Juego", UITheme.ACCENT_BLUE);
        JButton btnSalir = UITheme.crearBoton("🚪  Salir", UITheme.ACCENT_RED);
        btnNuevo.addActionListener(e -> { new MenuPrincipalUI().setVisible(true); dispose(); });
        btnSalir.addActionListener(e -> System.exit(0));
        btnPanel.add(btnNuevo);
        btnPanel.add(btnSalir);
        root.add(btnPanel, BorderLayout.SOUTH);
    }
}
