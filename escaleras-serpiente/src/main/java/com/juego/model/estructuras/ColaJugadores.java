package com.juego.model.estructuras;

import com.juego.model.Jugador;

public class ColaJugadores {

    private static class Nodo {
        Jugador jugador;
        Nodo siguiente;
        Nodo(Jugador j) { this.jugador = j; }
    }

    private Nodo frente;
    private Nodo fin;
    private int tamanio;

    public ColaJugadores() {
        frente = null;
        fin = null;
        tamanio = 0;
    }

    public void encolar(Jugador jugador) {
        Nodo nuevo = new Nodo(jugador);
        if (fin == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    public Jugador desencolar() {
        if (frente == null) return null;
        Jugador j = frente.jugador;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamanio--;
        return j;
    }

    public Jugador verFrente() {
        return frente != null ? frente.jugador : null;
    }

    public boolean estaVacia() { return frente == null; }
    public int getTamanio()    { return tamanio; }

    /** Rotar: el jugador actual va al final de la cola */
    public void rotar() {
        if (tamanio <= 1) return;
        Jugador j = desencolar();
        encolar(j);
    }

    public Jugador[] toArray() {
        Jugador[] arr = new Jugador[tamanio];
        Nodo actual = frente;
        int i = 0;
        while (actual != null) {
            arr[i++] = actual.jugador;
            actual = actual.siguiente;
        }
        return arr;
    }
}
