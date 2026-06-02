package com.juego.ui;

import com.juego.model.Casilla;
import com.juego.model.Jugador;
import com.juego.model.estructuras.Grafo;
import com.juego.service.TableroService;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Panel del tablero con animación de desplazamiento de fichas.
 */
public class TableroPanel extends JPanel {

    private static final int COLS      = 10;
    private static final int ROWS      = 5;
    private static final int CELL_SIZE = 64;
    private static final int PADDING   = 8;

    // ── Animación ──────────────────────────────────────────────────────────
    /** Índice del jugador que está animando, -1 = ninguno */
    private int jugadorAnimando = -1;
    /** Posición en píxeles que se está interpolando */
    private float animX, animY;
    /** Paso actual de la animación (0..ANIM_STEPS) */
    private int animStep = 0;
    private static final int ANIM_STEPS = 20;
    private Timer animTimer;
    private Runnable onAnimDone;

    private final TableroService service;

    public TableroPanel(TableroService service) {
        this.service = service;
        setPreferredSize(new Dimension(COLS * CELL_SIZE + PADDING * 2,
                                       ROWS * CELL_SIZE + PADDING * 2));
        setBackground(UITheme.BG_DARK);
    }

    // ── API pública para animar ──────────────────────────────────────────

    /**
     * Anima el jugador {@code jugadorIdx} desde la casilla {@code desde} hasta {@code hasta}.
     * Llama {@code onDone} al terminar.
     */
    public void animarMovimiento(int jugadorIdx, int desde, int hasta, Runnable onDone) {
        if (animTimer != null && animTimer.isRunning()) animTimer.stop();

        jugadorAnimando = jugadorIdx;
        onAnimDone = onDone;

        Point p0 = fichaCentro(jugadorIdx, desde, 0);
        Point p1 = fichaCentro(jugadorIdx, hasta, 0);
        animX = p0.x; animY = p0.y;
        animStep = 0;

        animTimer = new Timer(16, e -> {       // ~60 fps
            animStep++;
            float t = (float) animStep / ANIM_STEPS;
            // ease-out cúbico
            t = 1 - (1 - t) * (1 - t) * (1 - t);
            animX = lerp(p0.x, p1.x, t);
            animY = lerp(p0.y, p1.y, t);
            repaint();
            if (animStep >= ANIM_STEPS) {
                ((Timer) e.getSource()).stop();
                jugadorAnimando = -1;
                repaint();
                if (onAnimDone != null) onAnimDone.run();
            }
        });
        animTimer.start();
    }

    private float lerp(float a, float b, float t) { return a + (b - a) * t; }

    // ── Pintura ──────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Casilla[] casillas = service.getTablero().toArray();
        for (Casilla c : casillas) {
            Point p = celdaPos(c.getNumero());
            drawCasilla(g2, p.x, p.y, c);
        }

        drawConexiones(g2);

        Jugador[] jugadores = service.getCola().toArray();
        for (int i = 0; i < jugadores.length; i++) {
            if (jugadores[i] == null) continue;
            if (jugadores[i].getPosicion() > 0) {
                if (i == jugadorAnimando) {
                    // Dibujar en posición interpolada
                    drawFichaEn(g2, jugadores[i], i, (int) animX, (int) animY);
                } else {
                    int offset = countJugadoresEnCasilla(jugadores, jugadores[i].getPosicion(), i);
                    drawFicha(g2, jugadores[i], i, offset);
                }
            }
        }
    }

    private void drawCasilla(Graphics2D g2, int x, int y, Casilla c) {
        Color bg = switch (c.getTipo()) {
            case ESCALERA  -> new Color(30, 100, 50);
            case SERPIENTE -> new Color(100, 25, 25);
            case RETO      -> new Color(60, 40, 120);
            default        -> (c.getNumero() % 2 == 0) ? new Color(38, 38, 64) : new Color(42, 42, 72);
        };
        g2.setColor(bg);
        g2.fillRoundRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2, 8, 8);

        Color border = c.getNumero() == 50 ? UITheme.ACCENT_GOLD : new Color(60, 60, 90);
        g2.setColor(border);
        g2.setStroke(c.getNumero() == 50 ? new BasicStroke(2.5f) : new BasicStroke(1f));
        g2.drawRoundRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2, 8, 8);
        g2.setStroke(new BasicStroke(1f));

        g2.setFont(UITheme.FONT_BOARD);
        g2.setColor(UITheme.TEXT_MUTED);
        g2.drawString(String.valueOf(c.getNumero()), x + 5, y + 14);

        String icono = switch (c.getTipo()) {
            case ESCALERA  -> "🪜";
            case SERPIENTE -> "🐍";
            case RETO      -> "❓";
            default        -> c.getNumero() == 50 ? "🏆" : "";
        };
        if (!icono.isEmpty()) {
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            g2.drawString(icono, x + CELL_SIZE / 2 - 10, y + CELL_SIZE / 2 + 8);
        }
    }

    private void drawConexiones(Graphics2D g2) {
        Grafo.Arista[] aristas = service.getGrafo().todasLasAristas();
        for (Grafo.Arista a : aristas) {
            if (a == null) continue;
            Point from = celdaCentro(a.origen);
            Point to   = celdaCentro(a.destino);
            boolean escalera = a.tipo == Grafo.TipoArista.ESCALERA;

            // Sombra
            g2.setColor(new Color(0, 0, 0, 60));
            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(from.x + 2, from.y + 2, to.x + 2, to.y + 2);

            // Línea principal
            Color lineColor = escalera ? new Color(100, 220, 100, 200) : new Color(220, 80, 80, 200);
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    0, escalera ? null : new float[]{10, 5}, 0));
            g2.drawLine(from.x, from.y, to.x, to.y);
            drawArrow(g2, from, to, escalera ? new Color(120, 255, 120) : new Color(255, 100, 100));
            g2.setStroke(new BasicStroke(1f));
        }
    }

    private void drawArrow(Graphics2D g2, Point from, Point to, Color color) {
        double dx = to.x - from.x, dy = to.y - from.y;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return;
        dx /= len; dy /= len;
        int ax = to.x - (int)(dx * 16), ay = to.y - (int)(dy * 16);
        Path2D arrow = new Path2D.Double();
        arrow.moveTo(to.x, to.y);
        arrow.lineTo(ax + dy * 7, ay - dx * 7);
        arrow.lineTo(ax - dy * 7, ay + dx * 7);
        arrow.closePath();
        g2.setColor(color);
        g2.fill(arrow);
    }

    private void drawFicha(Graphics2D g2, Jugador j, int jugadorIdx, int offset) {
        Point p = celdaPos(j.getPosicion());
        int fichaSize = 18;
        int fx = p.x + 4 + (offset % 2) * (fichaSize + 2);
        int fy = p.y + CELL_SIZE - fichaSize - 4 - (offset / 2) * (fichaSize + 2);
        drawFichaCirculo(g2, j, jugadorIdx, fx, fy, fichaSize);
    }

    private void drawFichaEn(Graphics2D g2, Jugador j, int jugadorIdx, int cx, int cy) {
        int fichaSize = 20;
        int fx = cx - fichaSize / 2;
        int fy = cy - fichaSize / 2;
        // Sombra animada
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillOval(fx + 3, fy + 3, fichaSize, fichaSize);
        drawFichaCirculo(g2, j, jugadorIdx, fx, fy, fichaSize);
    }

    private void drawFichaCirculo(Graphics2D g2, Jugador j, int jugadorIdx, int fx, int fy, int fichaSize) {
        Color c = UITheme.JUGADOR_COLORS[jugadorIdx % UITheme.JUGADOR_COLORS.length];
        // Brillo
        g2.setColor(c.brighter());
        g2.fillOval(fx - 1, fy - 1, fichaSize + 2, fichaSize + 2);
        g2.setColor(c);
        g2.fillOval(fx, fy, fichaSize, fichaSize);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(fx, fy, fichaSize, fichaSize);

        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        String inicial = j.getNombre().substring(0, 1).toUpperCase();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(inicial,
                fx + (fichaSize - fm.stringWidth(inicial)) / 2,
                fy + (fichaSize + fm.getAscent() - fm.getDescent()) / 2);
        g2.setStroke(new BasicStroke(1f));
    }

    // ── Helpers de coordenadas ────────────────────────────────────────────

    private int countJugadoresEnCasilla(Jugador[] jugadores, int casilla, int maxIdx) {
        int count = 0;
        for (int i = 0; i < maxIdx; i++) {
            if (jugadores[i] != null && jugadores[i].getPosicion() == casilla) count++;
        }
        return count;
    }

    /** Centro de la ficha de jugadorIdx en la casilla dada (para animación) */
    private Point fichaCentro(int jugadorIdx, int casilla, int offset) {
        if (casilla <= 0) casilla = 1;
        Point p = celdaPos(casilla);
        int fichaSize = 18;
        int fx = p.x + 4 + (offset % 2) * (fichaSize + 2) + fichaSize / 2;
        int fy = p.y + CELL_SIZE - fichaSize - 4 - (offset / 2) * (fichaSize + 2) + fichaSize / 2;
        return new Point(fx, fy);
    }

    private Point celdaPos(int num) {
        int idx = num - 1;
        int row = idx / COLS;
        int col = idx % COLS;
        if (row % 2 == 1) col = COLS - 1 - col;
        int visualRow = ROWS - 1 - row;
        return new Point(PADDING + col * CELL_SIZE, PADDING + visualRow * CELL_SIZE);
    }

    private Point celdaCentro(int num) {
        Point p = celdaPos(num);
        return new Point(p.x + CELL_SIZE / 2, p.y + CELL_SIZE / 2);
    }
}
