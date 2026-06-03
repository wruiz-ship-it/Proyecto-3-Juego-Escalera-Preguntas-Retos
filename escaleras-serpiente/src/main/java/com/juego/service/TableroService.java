package com.juego.service;
import java.awt.Color;

import com.juego.model.Casilla;
import com.juego.model.Jugador;
import com.juego.model.Movimiento;
import com.juego.model.Pregunta;
import com.juego.model.estructuras.*;
import com.juego.util.PreguntaLoader;

import java.util.Random;

public class TableroService {

    private final ListaEnlazada tablero;
    private final ColaJugadores cola;
    private final PilaHistorial historial;
    private final TablaHash tablaJugadores;
    private final ArbolBST arbolPreguntas;
    private final Grafo grafo;
    private final Random random;

    private boolean juegoTerminado;
    private Jugador ganador;
    private boolean turnoExtra;

    public TableroService() {
        tablero        = new ListaEnlazada();
        cola           = new ColaJugadores();
        historial      = new PilaHistorial();
        tablaJugadores = new TablaHash(16);
        arbolPreguntas = new ArbolBST();
        grafo          = new Grafo();
        random         = new Random();
        juegoTerminado = false;
        turnoExtra     = false;

        inicializarTablero();
        inicializarGrafo();
        PreguntaLoader.cargar(arbolPreguntas);
    }

    private void inicializarTablero() {
        for (int i = 1; i <= 50; i++) {
            Casilla c = switch (i) {
                // Escaleras (suben)
                case 3  -> new Casilla(i, Casilla.Tipo.ESCALERA, 22);
                case 8  -> new Casilla(i, Casilla.Tipo.ESCALERA, 30);
                case 16 -> new Casilla(i, Casilla.Tipo.ESCALERA, 26);
                case 31 -> new Casilla(i, Casilla.Tipo.ESCALERA, 44);
                case 36 -> new Casilla(i, Casilla.Tipo.ESCALERA, 48);
                // Serpientes (bajan)
                case 17 -> new Casilla(i, Casilla.Tipo.SERPIENTE, 6);
                case 25 -> new Casilla(i, Casilla.Tipo.SERPIENTE, 11);
                case 33 -> new Casilla(i, Casilla.Tipo.SERPIENTE, 14);
                case 40 -> new Casilla(i, Casilla.Tipo.SERPIENTE, 20);
                case 46 -> new Casilla(i, Casilla.Tipo.SERPIENTE, 28);
                // Casillas de reto — más casillas
                case 5,7,10,12,15,18,21,23,27,29,34,37,41,43,45,49
                    -> new Casilla(i, Casilla.Tipo.RETO, i);
                default -> new Casilla(i);
            };
            tablero.agregar(c);
        }
    }

    private void inicializarGrafo() {
        grafo.agregarArista(3,  22, Grafo.TipoArista.ESCALERA);
        grafo.agregarArista(8,  30, Grafo.TipoArista.ESCALERA);
        grafo.agregarArista(16, 26, Grafo.TipoArista.ESCALERA);
        grafo.agregarArista(31, 44, Grafo.TipoArista.ESCALERA);
        grafo.agregarArista(36, 48, Grafo.TipoArista.ESCALERA);
        grafo.agregarArista(17, 6,  Grafo.TipoArista.SERPIENTE);
        grafo.agregarArista(25, 11, Grafo.TipoArista.SERPIENTE);
        grafo.agregarArista(33, 14, Grafo.TipoArista.SERPIENTE);
        grafo.agregarArista(40, 20, Grafo.TipoArista.SERPIENTE);
        grafo.agregarArista(46, 28, Grafo.TipoArista.SERPIENTE);
    }

    public void registrarJugador(String nombre, Color color) {
        // Acepta Color de java.awt para compatibilidad
        Jugador j = new Jugador(nombre, colorToHex(color));
        cola.encolar(j);
        tablaJugadores.insertar(nombre, j);
    }

    public void registrarJugador(String nombre, String colorHex) {
        Jugador j = new Jugador(nombre, colorHex);
        cola.encolar(j);
        tablaJugadores.insertar(nombre, j);
    }

    private String colorToHex(java.awt.Color c) {
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }

    public int lanzarDado() {
        return random.nextInt(6) + 1;
    }

    public ResultadoMovimiento moverJugadorActual(int dado) {
        Jugador actual = cola.verFrente();
        if (actual == null || juegoTerminado) return null;

        if (actual.isPierdeTurno()) {
            actual.setPierdeTurno(false);
            cola.rotar();
            historial.apilar(new Movimiento(actual.getNombre(), actual.getPosicion(),
                    actual.getPosicion(), dado, "⏭ Turno perdido"));
            return new ResultadoMovimiento(actual, actual.getPosicion(), actual.getPosicion(),
                    dado, ResultadoMovimiento.Tipo.PIERDE_TURNO, null);
        }

        int posAnterior = actual.getPosicion();
        int nuevaPos = posAnterior + dado;

        if (nuevaPos > 50) {
            cola.rotar();
            historial.apilar(new Movimiento(actual.getNombre(), posAnterior,
                    posAnterior, dado, "🚫 Excede meta"));
            return new ResultadoMovimiento(actual, posAnterior, posAnterior,
                    dado, ResultadoMovimiento.Tipo.EXCEDE, null);
        }

        actual.setPosicion(nuevaPos);
        Casilla casilla = tablero.obtener(nuevaPos);

        Grafo.Arista arista = grafo.obtenerArista(nuevaPos);
        ResultadoMovimiento.Tipo tipo = ResultadoMovimiento.Tipo.NORMAL;
        Pregunta pregunta = null;

        if (arista != null) {
            actual.setPosicion(arista.destino);
            tipo = arista.tipo == Grafo.TipoArista.ESCALERA
                    ? ResultadoMovimiento.Tipo.ESCALERA
                    : ResultadoMovimiento.Tipo.SERPIENTE;
            String evento = tipo == ResultadoMovimiento.Tipo.ESCALERA ? "🪜 Escalera" : "🐍 Serpiente";
            historial.apilar(new Movimiento(actual.getNombre(), posAnterior,
                    actual.getPosicion(), dado, evento + ": " + nuevaPos + "→" + arista.destino));
        } else if (casilla != null && casilla.getTipo() == Casilla.Tipo.RETO) {
            tipo = ResultadoMovimiento.Tipo.RETO;
            pregunta = obtenerPreguntaParaCasilla(nuevaPos);
            historial.apilar(new Movimiento(actual.getNombre(), posAnterior,
                    nuevaPos, dado, "❓ Reto en casilla " + nuevaPos));
        } else {
            historial.apilar(new Movimiento(actual.getNombre(), posAnterior,
                    nuevaPos, dado, "✅ Normal"));
        }

        if (actual.getPosicion() == 50) {
            juegoTerminado = true;
            ganador = actual;
            tipo = ResultadoMovimiento.Tipo.GANA;
        }

        if (tipo != ResultadoMovimiento.Tipo.RETO && !juegoTerminado) {
            if (turnoExtra) { turnoExtra = false; }
            else { cola.rotar(); }
        }

        return new ResultadoMovimiento(actual, posAnterior, actual.getPosicion(),
                dado, tipo, pregunta);
    }

    public void procesarRespuesta(boolean correcta) {
        Jugador actual = cola.verFrente();
        if (actual == null) return;
        if (correcta) {
            actual.sumarAcierto();
            turnoExtra = true;
        } else {
            actual.sumarError();
            actual.setPierdeTurno(true);
            turnoExtra = false;
            cola.rotar();
        }
    }

    private Pregunta obtenerPreguntaParaCasilla(int casilla) {
        String dif;
        if (casilla <= 15) dif = "FÁCIL";
        else if (casilla <= 35) dif = "MEDIA";
        else dif = "DIFÍCIL";

        Pregunta[] preguntas = arbolPreguntas.obtenerPorDificultad(dif);
        if (preguntas.length == 0) preguntas = arbolPreguntas.toArray();
        if (preguntas.length == 0) return null;
        return preguntas[random.nextInt(preguntas.length)];
    }

    public Jugador getJugadorActual()       { return cola.verFrente(); }
    public boolean isJuegoTerminado()       { return juegoTerminado; }
    public Jugador getGanador()             { return ganador; }
    public ListaEnlazada getTablero()       { return tablero; }
    public ColaJugadores getCola()          { return cola; }
    public PilaHistorial getHistorial()     { return historial; }
    public Grafo getGrafo()                 { return grafo; }
    public Jugador buscarJugador(String n)  { return tablaJugadores.buscar(n); }

    public Jugador[] getRanking() {
        Jugador[] jugadores = tablaJugadores.valores();
        for (int i = 0; i < jugadores.length - 1; i++) {
            for (int j = 0; j < jugadores.length - 1 - i; j++) {
                if (jugadores[j] == null) continue;
                if (jugadores[j + 1] != null && jugadores[j].getPuntaje() < jugadores[j + 1].getPuntaje()) {
                    Jugador tmp = jugadores[j];
                    jugadores[j] = jugadores[j + 1];
                    jugadores[j + 1] = tmp;
                }
            }
        }
        return jugadores;
    }

    public static class ResultadoMovimiento {
        public enum Tipo { NORMAL, ESCALERA, SERPIENTE, RETO, PIERDE_TURNO, EXCEDE, GANA }
        public final Jugador jugador;
        public final int posAnterior;
        public final int posNueva;
        public final int dado;
        public final Tipo tipo;
        public final Pregunta pregunta;

        public ResultadoMovimiento(Jugador jugador, int posAnterior, int posNueva,
                                   int dado, Tipo tipo, Pregunta pregunta) {
            this.jugador = jugador;
            this.posAnterior = posAnterior;
            this.posNueva = posNueva;
            this.dado = dado;
            this.tipo = tipo;
            this.pregunta = pregunta;
        }
    }
}
