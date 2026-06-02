package com.juego.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UITheme {

    public static final Color BG_DARK      = new Color(18, 18, 32);
    public static final Color BG_PANEL     = new Color(28, 28, 48);
    public static final Color BG_CARD      = new Color(38, 38, 64);
    public static final Color ACCENT_GOLD  = new Color(255, 200, 50);
    public static final Color ACCENT_BLUE  = new Color(80, 150, 255);
    public static final Color ACCENT_RED   = new Color(255, 80, 80);
    public static final Color ACCENT_GREEN = new Color(80, 220, 120);
    public static final Color TEXT_PRIMARY = new Color(235, 235, 245);
    public static final Color TEXT_MUTED   = new Color(150, 150, 180);

    public static final String[] COLORES_JUGADOR = {"#E74C3C","#3498DB","#2ECC71","#F39C12"};
    public static final Color[] JUGADOR_COLORS = {
        new Color(231, 76, 60),
        new Color(52, 152, 219),
        new Color(46, 204, 113),
        new Color(243, 156, 18)
    };

    public static final Font FONT_TITLE   = new Font("SansSerif", Font.BOLD, 28);
    public static final Font FONT_H2      = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_BODY    = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_SMALL   = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_BOARD   = new Font("SansSerif", Font.BOLD, 11);

    private UITheme() {}

    public static JButton crearBoton(String texto, Color bg) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bg.brighter());
                } else {
                    g2.setColor(bg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 42));
        return btn;
    }

    public static JLabel crearTitulo(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(ACCENT_GOLD);
        return lbl;
    }

    public static JPanel crearPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(BG_DARK);
        return p;
    }

    public static Border crearBorde(Color color) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        );
    }
}
