package com.juego.model;

public class Casilla {
    public enum Tipo { NORMAL, ESCALERA, SERPIENTE, RETO }

    private final int numero;
    private final Tipo tipo;
    private final int destino;

    public Casilla(int numero, Tipo tipo, int destino) {
        this.numero = numero;
        this.tipo = tipo;
        this.destino = destino;
    }

    public Casilla(int numero) {
        this(numero, Tipo.NORMAL, numero);
    }

    public int getNumero()  { return numero; }
    public Tipo getTipo()   { return tipo; }
    public int getDestino() { return destino; }

    public String getDificultad() {
        if (numero <= 15) return "FÁCIL";
        if (numero <= 35) return "MEDIA";
        return "DIFÍCIL";
    }

    @Override
    public String toString() {
        return "Casilla[" + numero + "/" + tipo + (tipo != Tipo.NORMAL ? "->" + destino : "") + "]";
    }
}
