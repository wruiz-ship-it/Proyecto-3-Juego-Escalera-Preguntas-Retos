package com.juego.util;

import com.juego.model.Pregunta;
import com.juego.model.estructuras.ArbolBST;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PreguntaLoader {

    private PreguntaLoader() {}

    public static void cargar(ArbolBST arbol) {
        try (InputStream is = PreguntaLoader.class.getResourceAsStream("/preguntas.txt")) {
            if (is != null) {
                cargarDesdeStream(is, arbol);
            } else {
                cargarDefecto(arbol);
            }
        } catch (Exception e) {
            cargarDefecto(arbol);
        }
    }

    private static void cargarDesdeStream(InputStream is, ArbolBST arbol) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String linea;
        int id = 1;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty() || linea.startsWith("#")) continue;
            String[] partes = linea.split("\\|");
            if (partes.length < 7) continue;
            String dif = partes[0].trim();
            String enunciado = partes[1].trim();
            String[] opciones = { partes[2].trim(), partes[3].trim(), partes[4].trim(), partes[5].trim() };
            int resp = Integer.parseInt(partes[6].trim());
            arbol.insertar(new Pregunta(id++, enunciado, opciones, resp, dif));
        }
        br.close();
        if (arbol.getTamanio() == 0) cargarDefecto(arbol);
    }

    private static void cargarDefecto(ArbolBST arbol) {
        // FÁCIL
        arbol.insertar(new Pregunta(1, "¿Cuánto es 5 + 3?",
                new String[]{"6", "8", "7", "9"}, 1, "FÁCIL"));
        arbol.insertar(new Pregunta(2, "¿Cuál es la capital de Colombia?",
                new String[]{"Medellín", "Cali", "Bogotá", "Barranquilla"}, 2, "FÁCIL"));
        arbol.insertar(new Pregunta(3, "¿Cuántos días tiene una semana?",
                new String[]{"5", "6", "7", "8"}, 2, "FÁCIL"));
        arbol.insertar(new Pregunta(4, "¿De qué color es el cielo despejado?",
                new String[]{"Verde", "Azul", "Rojo", "Amarillo"}, 1, "FÁCIL"));
        arbol.insertar(new Pregunta(5, "¿Cuánto es 10 x 2?",
                new String[]{"15", "20", "12", "25"}, 1, "FÁCIL"));
        // MEDIA
        arbol.insertar(new Pregunta(6, "¿Cuántos planetas tiene el Sistema Solar?",
                new String[]{"7", "9", "8", "10"}, 2, "MEDIA"));
        arbol.insertar(new Pregunta(7, "¿Cuál es el elemento químico del agua?",
                new String[]{"CO2", "H2O", "NaCl", "O2"}, 1, "MEDIA"));
        arbol.insertar(new Pregunta(8, "¿En qué año comenzó la Segunda Guerra Mundial?",
                new String[]{"1935", "1941", "1939", "1945"}, 2, "MEDIA"));
        arbol.insertar(new Pregunta(9, "¿Quién escribió Don Quijote?",
                new String[]{"Lope de Vega", "Cervantes", "Quevedo", "Góngora"}, 1, "MEDIA"));
        arbol.insertar(new Pregunta(10, "¿Cuánto es la raíz cuadrada de 144?",
                new String[]{"10", "14", "12", "11"}, 2, "MEDIA"));
        // DIFÍCIL
        arbol.insertar(new Pregunta(11, "¿Cuál es la constante de gravitación universal G?",
                new String[]{"6.67×10⁻¹¹", "9.8 m/s²", "3×10⁸", "1.6×10⁻¹⁹"}, 0, "DIFÍCIL"));
        arbol.insertar(new Pregunta(12, "¿Cuántos bits tiene un byte?",
                new String[]{"4", "16", "8", "32"}, 2, "DIFÍCIL"));
        arbol.insertar(new Pregunta(13, "¿Qué estructura de datos usa LIFO?",
                new String[]{"Cola", "Lista", "Pila", "Árbol"}, 2, "DIFÍCIL"));
        arbol.insertar(new Pregunta(14, "¿Cuál es la complejidad de búsqueda en BST balanceado?",
                new String[]{"O(n)", "O(n²)", "O(log n)", "O(1)"}, 2, "DIFÍCIL"));
        arbol.insertar(new Pregunta(15, "¿Cuánto es el logaritmo en base 2 de 1024?",
                new String[]{"8", "12", "10", "16"}, 2, "DIFÍCIL"));
    }
}
