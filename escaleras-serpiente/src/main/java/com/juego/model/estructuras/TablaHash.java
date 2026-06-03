package com.juego.model.estructuras;

import com.juego.model.Jugador;

public class TablaHash {

    private static class Entrada {
        String clave;
        Jugador valor;
        Entrada siguiente;
        Entrada(String clave, Jugador valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }

    private final Entrada[] tabla;
    private final int capacidad;
    private int tamanio;

    public TablaHash(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new Entrada[capacidad];
        this.tamanio = 0;
    }

    private int hash(String clave) {
        int h = 0;
        for (char c : clave.toCharArray()) h = (h * 31 + c) % capacidad;
        return Math.abs(h);
    }

    public void insertar(String clave, Jugador jugador) {
        int idx = hash(clave);
        Entrada actual = tabla[idx];
        while (actual != null) {
            if (actual.clave.equals(clave)) { actual.valor = jugador; return; }
            actual = actual.siguiente;
        }
        Entrada nueva = new Entrada(clave, jugador);
        nueva.siguiente = tabla[idx];
        tabla[idx] = nueva;
        tamanio++;
    }

    public Jugador buscar(String clave) {
        int idx = hash(clave);
        Entrada actual = tabla[idx];
        while (actual != null) {
            if (actual.clave.equals(clave)) return actual.valor;
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean contiene(String clave) { return buscar(clave) != null; }
    public int getTamanio()               { return tamanio; }

    public Jugador[] valores() {
        Jugador[] arr = new Jugador[tamanio];
        int i = 0;
        for (Entrada entrada : tabla) {
            Entrada actual = entrada;
            while (actual != null) {
                arr[i++] = actual.valor;
                actual = actual.siguiente;
            }
        }
        return arr;
    }
}
