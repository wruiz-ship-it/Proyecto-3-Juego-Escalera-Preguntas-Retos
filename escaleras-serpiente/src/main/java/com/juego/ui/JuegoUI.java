package com.juego.ui;

import com.juego.model.Jugador;
import com.juego.model.Movimiento;
import com.juego.service.TableroService;
import com.juego.service.TableroService.ResultadoMovimiento;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JuegoUI extends JFrame {

    private final TableroService service;
    private TableroPanel tableroPanel;
    private JLabel lblTurno;
    private JLabel lblDado;
    private JButton btnDado;
    private JTextArea areaHistorial;
    private JPanel panelJugadores;

    public JuegoUI(TableroService service) {
        this.service = service;
        setTitle("Escaleras, Serpientes y Retos");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { confirmarSalida(); }
        });
        setResizable(false);
        initUI();
        pack();
        setLocationRelativeTo(null);
        actualizarInfoJugadores();
    }

    private void initUI() {
        JPanel root = UITheme.crearPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(root);

        tableroPanel = new TableroPanel(service);
        tableroPanel.setBorder(BorderFactory.createLineBorder(UITheme.ACCENT_BLUE, 2));
        root.add(tableroPanel, BorderLayout.CENTER);

        // Panel derecho
        JPanel derecho = new JPanel(new BorderLayout(0, 8));
        derecho.setBackground(UITheme.BG_DARK);
        derecho.setPreferredSize(new Dimension(240, 0));

        panelJugadores = new JPanel(new GridLayout(0, 1, 0, 6));
        panelJugadores.setBackground(UITheme.BG_PANEL);
        panelJugadores.setBorder(crearTitledBorder("Jugadores"));

        areaHistorial = new JTextArea(10, 20);
        areaHistorial.setEditable(false);
        areaHistorial.setFont(UITheme.FONT_SMALL);
        areaHistorial.setBackground(UITheme.BG_CARD);
        areaHistorial.setForeground(UITheme.TEXT_PRIMARY);
        areaHistorial.setLineWrap(true);
        areaHistorial.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaHistorial);
        scroll.setBorder(crearTitledBorder("Historial"));

        derecho.add(panelJugadores, BorderLayout.NORTH);
        derecho.add(scroll, BorderLayout.CENTER);
        root.add(derecho, BorderLayout.EAST);

        // Panel inferior
        JPanel inferior = new JPanel(new BorderLayout(12, 0));
        inferior.setBackground(UITheme.BG_DARK);
        inferior.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        lblTurno = new JLabel("Turno: ...", SwingConstants.LEFT);
        lblTurno.setFont(UITheme.FONT_H2);
        lblTurno.setForeground(UITheme.ACCENT_GOLD);

        lblDado = new JLabel("🎲", SwingConstants.CENTER);
        lblDado.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 44));

        btnDado = UITheme.crearBoton("🎲  Lanzar Dado", UITheme.ACCENT_BLUE);
        btnDado.setPreferredSize(new Dimension(200, 50));
        btnDado.addActionListener(e -> lanzarDado());

        JPanel dadoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        dadoPanel.setBackground(UITheme.BG_DARK);
        dadoPanel.add(lblDado);
        dadoPanel.add(btnDado);

        inferior.add(lblTurno, BorderLayout.CENTER);
        inferior.add(dadoPanel, BorderLayout.EAST);
        root.add(inferior, BorderLayout.SOUTH);

        actualizarTurnoLabel();
    }

    // ── Dado con animación mejorada ────────────────────────────────────────

    private void lanzarDado() {
        btnDado.setEnabled(false);
        Jugador actual = service.getJugadorActual();
        if (actual == null) { btnDado.setEnabled(true); return; }

        String[] caras = {"⚀","⚁","⚂","⚃","⚄","⚅"};
        final int[] frame = {0};
        final int[] delays = {60, 60, 80, 80, 100, 120, 140, 160}; // acelera→desacelera

        // Usamos Timer encadenados para dar efecto de inercia
        scheduleNextFrame(caras, frame, delays, () -> ejecutarMovimiento());
    }

    private void scheduleNextFrame(String[] caras, int[] frame, int[] delays, Runnable onDone) {
        if (frame[0] >= delays.length) {
            onDone.run();
            return;
        }
        int delay = delays[frame[0]];
        lblDado.setText(caras[(int)(Math.random() * 6)]);
        frame[0]++;
        new Timer(delay, e -> {
            ((Timer) e.getSource()).stop();
            scheduleNextFrame(caras, frame, delays, onDone);
        }) {{ setRepeats(false); start(); }};
    }

    private void ejecutarMovimiento() {
        int dado = service.lanzarDado();
        String[] caras = {"⚀","⚁","⚂","⚃","⚄","⚅"};
        lblDado.setText(caras[dado - 1]);

        // Identificar índice del jugador actual en la cola
        Jugador actual = service.getJugadorActual();
        Jugador[] todos = service.getCola().toArray();
        int jugadorIdx = 0;
        for (int i = 0; i < todos.length; i++) {
            if (todos[i] == actual) { jugadorIdx = i; break; }
        }

        int posAntes = actual.getPosicion();
        ResultadoMovimiento res = service.moverJugadorActual(dado);
        if (res == null) { btnDado.setEnabled(true); return; }

        int posDespues = res.posNueva;
        final int idx = jugadorIdx;

        // ── Animar desplazamiento ──────────────────────────────────────────
        if (posAntes != posDespues && posDespues > 0) {
            tableroPanel.animarMovimiento(idx, posAntes == 0 ? 1 : posAntes, posDespues, () -> {
                postMovimiento(res);
            });
        } else {
            postMovimiento(res);
        }
    }

    private void postMovimiento(ResultadoMovimiento res) {
        tableroPanel.repaint();
        actualizarHistorial();
        actualizarInfoJugadores();

        switch (res.tipo) {
            case ESCALERA -> mostrarMensaje("🪜 ¡Escalera!",
                    res.jugador.getNombre() + " sube de " + res.posAnterior + " a " + res.posNueva,
                    UITheme.ACCENT_GREEN);
            case SERPIENTE -> mostrarMensaje("🐍 ¡Serpiente!",
                    res.jugador.getNombre() + " baja de " + res.posAnterior + " a " + res.posNueva,
                    UITheme.ACCENT_RED);
            case PIERDE_TURNO -> mostrarMensaje("⏭ Turno perdido",
                    res.jugador.getNombre() + " pierde este turno.", UITheme.TEXT_MUTED);
            case EXCEDE -> mostrarMensaje("🚫 Sin avance",
                    res.jugador.getNombre() + " necesita " + (50 - res.posAnterior) + " o menos.",
                    UITheme.ACCENT_RED);
            case RETO -> {
                if (res.pregunta != null) { mostrarReto(res); return; }
            }
            case GANA -> { mostrarGanador(); return; }
            default -> {}
        }

        actualizarTurnoLabel();
        btnDado.setEnabled(true);
    }

    private void mostrarReto(ResultadoMovimiento res) {
        RetoUI retoUI = new RetoUI(this, res.pregunta, correcto -> {
            service.procesarRespuesta(correcto);
            tableroPanel.repaint();
            actualizarHistorial();
            actualizarInfoJugadores();
            actualizarTurnoLabel();
            if (correcto)
                mostrarMensaje("✅ ¡Correcto!", "¡Tienes turno extra!", UITheme.ACCENT_GREEN);
            else
                mostrarMensaje("❌ Incorrecto", "Pierdes el siguiente turno.", UITheme.ACCENT_RED);
            btnDado.setEnabled(true);
        });
        retoUI.setVisible(true);
    }

    private void mostrarGanador() {
        btnDado.setEnabled(false);
        tableroPanel.repaint();
        Timer delay = new Timer(500, e -> {
            RankingUI ranking = new RankingUI(service);
            ranking.setVisible(true);
            dispose();
        });
        delay.setRepeats(false);
        delay.start();
    }

    private void actualizarTurnoLabel() {
        Jugador j = service.getJugadorActual();
        if (j != null) lblTurno.setText("Turno: " + j.getNombre());
    }

    private void actualizarInfoJugadores() {
        panelJugadores.removeAll();
        Jugador[] jugadores = service.getCola().toArray();
        Jugador actual = service.getJugadorActual();
        for (int i = 0; i < jugadores.length; i++) {
            Jugador j = jugadores[i];
            if (j == null) continue;
            JPanel card = new JPanel(new GridLayout(2, 1, 0, 2));
            Color c = UITheme.JUGADOR_COLORS[i % UITheme.JUGADOR_COLORS.length];
            card.setBackground(j == actual ? c.darker() : UITheme.BG_CARD);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(c, j == actual ? 2 : 1),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)));

            JLabel nombre = new JLabel(j.getNombre() + (j == actual ? " ◀" : ""));
            nombre.setFont(new Font("SansSerif", Font.BOLD, 13));
            nombre.setForeground(UITheme.TEXT_PRIMARY);

            JLabel stats = new JLabel("Pos: " + j.getPosicion() + " | ✅" + j.getAciertos() + " ❌" + j.getErrores());
            stats.setFont(UITheme.FONT_SMALL);
            stats.setForeground(UITheme.TEXT_MUTED);

            card.add(nombre);
            card.add(stats);
            panelJugadores.add(card);
        }
        panelJugadores.revalidate();
        panelJugadores.repaint();
    }

    private void actualizarHistorial() {
        Movimiento[] movs = service.getHistorial().toArray();
        StringBuilder sb = new StringBuilder();
        for (Movimiento m : movs) {
            if (m != null) sb.append(m.toString()).append("\n");
        }
        areaHistorial.setText(sb.toString());
        areaHistorial.setCaretPosition(0);
    }

    private void mostrarMensaje(String titulo, String msg, Color color) {
        JLabel lbl = new JLabel("<html><div style='text-align:center'>" + msg + "</div></html>",
                SwingConstants.CENTER);
        lbl.setFont(UITheme.FONT_BODY);
        lbl.setForeground(color);
        JOptionPane.showMessageDialog(this, lbl, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    private void confirmarSalida() {
        int op = JOptionPane.showConfirmDialog(this, "¿Salir del juego?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) System.exit(0);
    }

    private TitledBorder crearTitledBorder(String titulo) {
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UITheme.ACCENT_BLUE, 1), titulo);
        tb.setTitleColor(UITheme.ACCENT_BLUE);
        tb.setTitleFont(UITheme.FONT_SMALL);
        return tb;
    }
}
