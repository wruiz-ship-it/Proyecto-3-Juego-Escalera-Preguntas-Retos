package com.juego.ui;

import com.juego.service.TableroService;

import javax.swing.*;
import java.awt.*;

public class RegistroJugadoresUI extends JFrame {

    private final JFrame parent;
    private final JSpinner spinnerJugadores;
    private final JTextField[] campos;
    private final JPanel panelCampos;

    public RegistroJugadoresUI(JFrame parent) {
        this.parent = parent;
        this.spinnerJugadores = new JSpinner(new SpinnerNumberModel(2, 2, 4, 1));
        this.campos = new JTextField[4];
        this.panelCampos = new JPanel();

        setTitle("Registrar Jugadores");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        // ── Root con BoxLayout vertical ───────────────────────────────────
        JPanel root = UITheme.crearPanel(null);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        setContentPane(root);

        // Título
        JLabel titulo = UITheme.crearTitulo("👥 Registro de Jugadores");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        root.add(titulo);
        root.add(Box.createVerticalStrut(20));

        // Fila: número de jugadores
        JPanel numPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        numPanel.setBackground(UITheme.BG_DARK);
        numPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel lblNum = new JLabel("Número de jugadores:");
        lblNum.setForeground(UITheme.TEXT_PRIMARY);
        lblNum.setFont(UITheme.FONT_BODY);
        spinnerJugadores.setFont(UITheme.FONT_BODY);
        spinnerJugadores.setPreferredSize(new Dimension(60, 32));
        spinnerJugadores.addChangeListener(e -> actualizarCampos());
        numPanel.add(lblNum);
        numPanel.add(spinnerJugadores);
        root.add(numPanel);
        root.add(Box.createVerticalStrut(14));

        // Panel de campos (BoxLayout vertical — crece y encoge con el nº de jugadores)
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBackground(UITheme.BG_DARK);
        panelCampos.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < 4; i++) {
            campos[i] = new JTextField("Jugador " + (i + 1));
            campos[i].setFont(UITheme.FONT_BODY);
            campos[i].setBackground(UITheme.BG_CARD);
            campos[i].setForeground(UITheme.TEXT_PRIMARY);
            campos[i].setCaretColor(UITheme.TEXT_PRIMARY);
            campos[i].setBorder(UITheme.crearBorde(UITheme.JUGADOR_COLORS[i]));
            campos[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        }

        root.add(panelCampos);
        root.add(Box.createVerticalStrut(20));

        // Botones — siempre al final, nunca se cortan
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        btnPanel.setBackground(UITheme.BG_DARK);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        JButton btnVolver  = UITheme.crearBoton("← Volver",       new Color(80, 80, 100));
        JButton btnIniciar = UITheme.crearBoton("▶  Iniciar Juego", UITheme.ACCENT_GREEN);
        btnVolver .addActionListener(e -> { parent.setVisible(true); dispose(); });
        btnIniciar.addActionListener(e -> iniciarJuego());
        btnPanel.add(btnVolver);
        btnPanel.add(btnIniciar);
        root.add(btnPanel);

        actualizarCampos();
    }

    private void actualizarCampos() {
        int num = (int) spinnerJugadores.getValue();
        panelCampos.removeAll();
        for (int i = 0; i < num; i++) {
            panelCampos.add(campos[i]);
            if (i < num - 1) panelCampos.add(Box.createVerticalStrut(10));
        }
        panelCampos.revalidate();
        panelCampos.repaint();
        // Re-ajustar tamaño de ventana cuando cambia el número de campos
        pack();
    }

    private void iniciarJuego() {
        int num = (int) spinnerJugadores.getValue();
        TableroService service = new TableroService();
        for (int i = 0; i < num; i++) {
            String nombre = campos[i].getText().trim();
            if (nombre.isEmpty()) nombre = "Jugador " + (i + 1);
            service.registrarJugador(nombre, UITheme.JUGADOR_COLORS[i]);
        }
        JuegoUI juego = new JuegoUI(service);
        juego.setVisible(true);
        dispose();
    }
}
