package com.juego.model.estructuras;

import com.juego.model.Casilla;

public class ListaEnlazada {

    private static class Nodo {
        Casilla casilla;
        Nodo siguiente;
        Nodo(Casilla c) { this.casilla = c; }
    }

    private Nodo cabeza;
    private int tamanio;

    public ListaEnlazada() {
        cabeza = null;
        tamanio = 0;
    }

    public void agregar(Casilla casilla) {
        Nodo nuevo = new Nodo(casilla);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamanio++;
    }

    public Casilla obtener(int numero) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.casilla.getNumero() == numero) return actual.casilla;
            actual = actual.siguiente;
        }
        return null;
    }

    public int getTamanio() { return tamanio; }

    public Casilla[] toArray() {
        Casilla[] arr = new Casilla[tamanio];
        Nodo actual = cabeza;
        int i = 0;
        while (actual != null) {
            arr[i++] = actual.casilla;
            actual = actual.siguiente;
        }
        return arr;
    }
}
