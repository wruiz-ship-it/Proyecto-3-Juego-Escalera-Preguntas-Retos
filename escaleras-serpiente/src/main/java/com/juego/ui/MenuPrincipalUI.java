package com.juego.ui;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalUI extends JFrame {

    public MenuPrincipalUI() {
        setTitle("Escaleras, Serpientes y Retos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, UITheme.BG_DARK, 0, getHeight(), new Color(30, 20, 60)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        // Header
        JPanel header = new JPanel(new GridLayout(3, 1, 0, 8));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(50, 40, 20, 40));

        JLabel emoji = new JLabel("🎲", SwingConstants.CENTER);
        emoji.setFont(new Font("SansSerif", Font.PLAIN, 60));
        JLabel titulo = UITheme.crearTitulo("Escaleras, Serpientes");
        JLabel subtitulo = new JLabel("y Retos", SwingConstants.CENTER);
        subtitulo.setFont(UITheme.FONT_H2);
        subtitulo.setForeground(UITheme.ACCENT_BLUE);

        header.add(emoji);
        header.add(titulo);
        header.add(subtitulo);
        root.add(header, BorderLayout.NORTH);

        // Botones
        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 0, 16));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JButton btnJugar = UITheme.crearBoton("🎮  Nuevo Juego", UITheme.ACCENT_BLUE);
        JButton btnReglas = UITheme.crearBoton("📋  Reglas", new Color(100, 80, 180));
        JButton btnSalir = UITheme.crearBoton("🚪  Salir", UITheme.ACCENT_RED);

        btnJugar.setPreferredSize(new Dimension(280, 52));
        btnReglas.setPreferredSize(new Dimension(280, 52));
        btnSalir.setPreferredSize(new Dimension(280, 52));

        btnJugar.addActionListener(e -> abrirRegistro());
        btnReglas.addActionListener(e -> mostrarReglas());
        btnSalir.addActionListener(e -> System.exit(0));

        btnPanel.add(btnJugar);
        btnPanel.add(btnReglas);
        btnPanel.add(btnSalir);
        root.add(btnPanel, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("Proyecto Universitario — Estructuras de Datos", SwingConstants.CENTER);
        footer.setFont(UITheme.FONT_SMALL);
        footer.setForeground(UITheme.TEXT_MUTED);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        root.add(footer, BorderLayout.SOUTH);
    }

    private void abrirRegistro() {
        RegistroJugadoresUI reg = new RegistroJugadoresUI(this);
        reg.setVisible(true);
        setVisible(false);
    }

    private void mostrarReglas() {
        String reglas = """
                ════════════ REGLAS ════════════

                • 2 a 4 jugadores.
                • Meta exacta: casilla 50.
                • Si el dado supera la meta, no avanza.

                🪜 ESCALERAS: suben al jugador.
                   3→12  |  8→18  |  20→32

                🐍 SERPIENTES: bajan al jugador.
                   25→10  |  40→22

                ❓ RETOS: responde una pregunta.
                   ✅ Correcto → acierto + turno extra.
                   ❌ Incorrecto → error + pierde turno.

                Dificultad por zona:
                  1-15 = Fácil
                  16-35 = Media
                  36-50 = Difícil
                """;
        JTextArea ta = new JTextArea(reglas);
        ta.setEditable(false);
        ta.setFont(UITheme.FONT_BODY);
        ta.setBackground(UITheme.BG_DARK);
        ta.setForeground(UITheme.TEXT_PRIMARY);
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Reglas del Juego",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
