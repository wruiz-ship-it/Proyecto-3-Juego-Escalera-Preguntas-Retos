package com.juego.model.estructuras;

import com.juego.model.Movimiento;

public class PilaHistorial {

    private static class Nodo {
        Movimiento movimiento;
        Nodo siguiente;
        Nodo(Movimiento m) { this.movimiento = m; }
    }

    private Nodo cima;
    private int tamanio;

    public PilaHistorial() {
        cima = null;
        tamanio = 0;
    }

    public void apilar(Movimiento movimiento) {
        Nodo nuevo = new Nodo(movimiento);
        nuevo.siguiente = cima;
        cima = nuevo;
        tamanio++;
    }

    public Movimiento desapilar() {
        if (cima == null) return null;
        Movimiento m = cima.movimiento;
        cima = cima.siguiente;
        tamanio--;
        return m;
    }

    public Movimiento verCima() {
        return cima != null ? cima.movimiento : null;
    }

    public boolean estaVacia() { return cima == null; }
    public int getTamanio()    { return tamanio; }

    public Movimiento[] toArray() {
        Movimiento[] arr = new Movimiento[tamanio];
        Nodo actual = cima;
        int i = 0;
        while (actual != null) {
            arr[i++] = actual.movimiento;
            actual = actual.siguiente;
        }
        return arr;
    }
}
