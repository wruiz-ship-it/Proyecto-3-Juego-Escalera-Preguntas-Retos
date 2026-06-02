package com.juego.ui;

import com.juego.model.Pregunta;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class RetoUI extends JDialog {

    private final Pregunta pregunta;
    private final Consumer<Boolean> callback;
    private int seleccion = -1;
    private final JButton[] btnOpciones;
    private final JButton btnConfirmar;

    public RetoUI(JFrame parent, Pregunta pregunta, Consumer<Boolean> callback) {
        super(parent, "❓ Reto — " + pregunta.getDificultad(), true);
        this.pregunta = pregunta;
        this.callback = callback;
        this.btnOpciones = new JButton[4];
        this.btnConfirmar = UITheme.crearBoton("Confirmar", UITheme.ACCENT_BLUE);
        initUI();
    }

    private void initUI() {
        setSize(500, 420);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel root = UITheme.crearPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        setContentPane(root);

        // Dificultad badge
        JLabel badge = new JLabel(pregunta.getDificultad(), SwingConstants.CENTER);
        badge.setFont(new Font("SansSerif", Font.BOLD, 12));
        Color badgeColor = switch (pregunta.getDificultad()) {
            case "FÁCIL"   -> UITheme.ACCENT_GREEN;
            case "MEDIA"   -> UITheme.ACCENT_GOLD;
            default        -> UITheme.ACCENT_RED;
        };
        badge.setForeground(badgeColor);
        badge.setOpaque(true);
        badge.setBackground(badgeColor.darker().darker());
        badge.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(UITheme.BG_DARK);
        topPanel.add(badge);
        root.add(topPanel, BorderLayout.NORTH);

        // Enunciado
        JTextArea enunciado = new JTextArea(pregunta.getEnunciado());
        enunciado.setEditable(false);
        enunciado.setLineWrap(true);
        enunciado.setWrapStyleWord(true);
        enunciado.setFont(new Font("SansSerif", Font.BOLD, 16));
        enunciado.setBackground(UITheme.BG_PANEL);
        enunciado.setForeground(UITheme.TEXT_PRIMARY);
        enunciado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.ACCENT_BLUE, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        root.add(enunciado, BorderLayout.CENTER);

        // Opciones
        String[] opc = pregunta.getOpciones();
        String[] letras = {"A", "B", "C", "D"};
        JPanel opcionesPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        opcionesPanel.setBackground(UITheme.BG_DARK);
        opcionesPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        for (int i = 0; i < opc.length; i++) {
            final int idx = i;
            JButton btn = UITheme.crearBoton(letras[i] + ") " + opc[i], UITheme.BG_CARD);
            btn.setFont(UITheme.FONT_BODY);
            btn.setPreferredSize(new Dimension(200, 48));
            btn.addActionListener(e -> seleccionarOpcion(idx));
            btnOpciones[i] = btn;
            opcionesPanel.add(btn);
        }

        // Confirmar
        btnConfirmar.setEnabled(false);
        btnConfirmar.setPreferredSize(new Dimension(200, 44));
        btnConfirmar.addActionListener(e -> confirmar());

        JPanel sur = new JPanel(new BorderLayout(0, 8));
        sur.setBackground(UITheme.BG_DARK);
        JPanel confirmarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        confirmarPanel.setBackground(UITheme.BG_DARK);
        confirmarPanel.add(btnConfirmar);
        sur.add(opcionesPanel, BorderLayout.NORTH);
        sur.add(confirmarPanel, BorderLayout.CENTER);
        root.add(sur, BorderLayout.SOUTH);
    }

    private void seleccionarOpcion(int idx) {
        seleccion = idx;
        for (int i = 0; i < btnOpciones.length; i++) {
            // Re-paint sin recrear (cambio de color por pintura override en UITheme no disponible)
            // Usamos background visible con opaque
            if (i == idx) {
                btnOpciones[i].setBackground(UITheme.ACCENT_BLUE);
                btnOpciones[i].setForeground(Color.WHITE);
                btnOpciones[i].setOpaque(true);
                btnOpciones[i].setContentAreaFilled(true);
            } else {
                btnOpciones[i].setBackground(UITheme.BG_CARD);
                btnOpciones[i].setOpaque(true);
                btnOpciones[i].setContentAreaFilled(true);
            }
        }
        btnConfirmar.setEnabled(true);
    }

    private void confirmar() {
        if (seleccion < 0) return;
        boolean correcto = pregunta.esCorrecta(seleccion);

        // Mostrar respuesta correcta
        for (int i = 0; i < btnOpciones.length; i++) {
            if (i == pregunta.getRespuestaCorrecta()) {
                btnOpciones[i].setBackground(UITheme.ACCENT_GREEN);
            } else if (i == seleccion && !correcto) {
                btnOpciones[i].setBackground(UITheme.ACCENT_RED);
            }
            btnOpciones[i].setEnabled(false);
        }
        btnConfirmar.setEnabled(false);

        Timer timer = new Timer(1200, e -> {
            dispose();
            callback.accept(correcto);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
