package com.juego.model.estructuras;

public class Grafo {

    public enum TipoArista { ESCALERA, SERPIENTE }

    public static class Arista {
        public final int origen;
        public final int destino;
        public final TipoArista tipo;
        Arista siguiente;

        public Arista(int origen, int destino, TipoArista tipo) {
            this.origen = origen;
            this.destino = destino;
            this.tipo = tipo;
        }
    }

    private static class NodoVertice {
        int casilla;
        Arista listaAristas;
        NodoVertice siguiente;
        NodoVertice(int casilla) { this.casilla = casilla; }
    }

    private NodoVertice vertices;
    private int numVertices;
    private int numAristas;

    public Grafo() { vertices = null; numVertices = 0; numAristas = 0; }

    private NodoVertice obtenerOCrear(int casilla) {
        NodoVertice actual = vertices;
        while (actual != null) {
            if (actual.casilla == casilla) return actual;
            actual = actual.siguiente;
        }
        NodoVertice nuevo = new NodoVertice(casilla);
        nuevo.siguiente = vertices;
        vertices = nuevo;
        numVertices++;
        return nuevo;
    }

    public void agregarArista(int origen, int destino, TipoArista tipo) {
        NodoVertice nodo = obtenerOCrear(origen);
        Arista nueva = new Arista(origen, destino, tipo);
        nueva.siguiente = nodo.listaAristas;
        nodo.listaAristas = nueva;
        numAristas++;
    }

    public Arista obtenerArista(int casilla) {
        NodoVertice nodo = vertices;
        while (nodo != null) {
            if (nodo.casilla == casilla) return nodo.listaAristas;
            nodo = nodo.siguiente;
        }
        return null;
    }

    public Arista[] todasLasAristas() {
        Arista[] arr = new Arista[numAristas];
        int i = 0;
        NodoVertice nv = vertices;
        while (nv != null) {
            Arista a = nv.listaAristas;
            while (a != null) { arr[i++] = a; a = a.siguiente; }
            nv = nv.siguiente;
        }
        return arr;
    }

    public int getNumVertices() { return numVertices; }
    public int getNumAristas()  { return numAristas; }
}
