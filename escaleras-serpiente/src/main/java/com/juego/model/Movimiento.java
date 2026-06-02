package com.juego.model;

public class Movimiento {
    private final String jugador;
    private final int posicionAnterior;
    private final int posicionNueva;
    private final int dado;
    private final String evento;

    public Movimiento(String jugador, int posicionAnterior, int posicionNueva, int dado, String evento) {
        this.jugador = jugador;
        this.posicionAnterior = posicionAnterior;
        this.posicionNueva = posicionNueva;
        this.dado = dado;
        this.evento = evento;
    }

    public String getJugador()         { return jugador; }
    public int getPosicionAnterior()   { return posicionAnterior; }
    public int getPosicionNueva()      { return posicionNueva; }
    public int getDado()               { return dado; }
    public String getEvento()          { return evento; }

    @Override
    public String toString() {
        return jugador + ": " + posicionAnterior + " → " + posicionNueva +
               " (dado:" + dado + ") " + evento;
    }
}
