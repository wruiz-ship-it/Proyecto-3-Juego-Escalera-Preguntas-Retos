package com.juego.model.estructuras;

import com.juego.model.Pregunta;

public class ArbolBST {

    private static class Nodo {
        Pregunta pregunta;
        Nodo izquierdo;
        Nodo derecho;
        Nodo(Pregunta p) { this.pregunta = p; }
    }

    private Nodo raiz;
    private int tamanio;

    public ArbolBST() { raiz = null; tamanio = 0; }

    public void insertar(Pregunta pregunta) {
        raiz = insertarRec(raiz, pregunta);
        tamanio++;
    }

    private Nodo insertarRec(Nodo nodo, Pregunta pregunta) {
        if (nodo == null) return new Nodo(pregunta);
        if (pregunta.getId() < nodo.pregunta.getId())
            nodo.izquierdo = insertarRec(nodo.izquierdo, pregunta);
        else if (pregunta.getId() > nodo.pregunta.getId())
            nodo.derecho = insertarRec(nodo.derecho, pregunta);
        return nodo;
    }

    public Pregunta buscar(int id) {
        return buscarRec(raiz, id);
    }

    private Pregunta buscarRec(Nodo nodo, int id) {
        if (nodo == null) return null;
        if (id == nodo.pregunta.getId()) return nodo.pregunta;
        if (id < nodo.pregunta.getId()) return buscarRec(nodo.izquierdo, id);
        return buscarRec(nodo.derecho, id);
    }

    public Pregunta[] obtenerPorDificultad(String dificultad) {
        // Inorder traversal, filter by difficulty
        Pregunta[] temp = new Pregunta[tamanio];
        int[] idx = {0};
        inorder(raiz, dificultad, temp, idx);
        Pregunta[] resultado = new Pregunta[idx[0]];
        System.arraycopy(temp, 0, resultado, 0, idx[0]);
        return resultado;
    }

    private void inorder(Nodo nodo, String dificultad, Pregunta[] arr, int[] idx) {
        if (nodo == null) return;
        inorder(nodo.izquierdo, dificultad, arr, idx);
        if (nodo.pregunta.getDificultad().equals(dificultad))
            arr[idx[0]++] = nodo.pregunta;
        inorder(nodo.derecho, dificultad, arr, idx);
    }

    public Pregunta[] toArray() {
        Pregunta[] arr = new Pregunta[tamanio];
        int[] idx = {0};
        inorderAll(raiz, arr, idx);
        return arr;
    }

    private void inorderAll(Nodo nodo, Pregunta[] arr, int[] idx) {
        if (nodo == null) return;
        inorderAll(nodo.izquierdo, arr, idx);
        arr[idx[0]++] = nodo.pregunta;
        inorderAll(nodo.derecho, arr, idx);
    }

    public int getTamanio() { return tamanio; }
}
