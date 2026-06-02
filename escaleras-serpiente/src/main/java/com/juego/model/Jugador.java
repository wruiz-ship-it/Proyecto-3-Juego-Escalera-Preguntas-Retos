package com.juego.model;

public class Jugador {
    private final String nombre;
    private int posicion;
    private int aciertos;
    private int errores;
    private boolean pierdeTurno;
    private final String color;

    public Jugador(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = 0;
        this.aciertos = 0;
        this.errores = 0;
        this.pierdeTurno = false;
    }

    public String getNombre()       { return nombre; }
    public int getPosicion()        { return posicion; }
    public int getAciertos()        { return aciertos; }
    public int getErrores()         { return errores; }
    public boolean isPierdeTurno()  { return pierdeTurno; }
    public String getColor()        { return color; }

    public void setPosicion(int p)      { this.posicion = p; }
    public void setPierdeTurno(boolean p) { this.pierdeTurno = p; }
    public void sumarAcierto()          { this.aciertos++; }
    public void sumarError()            { this.errores++; }

    public int getPuntaje() { return aciertos * 10 - errores * 5; }

    @Override
    public String toString() { return nombre; }
}
