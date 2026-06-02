package com.juego.model;

public class Pregunta {
    private final String enunciado;
    private final String[] opciones;
    private final int respuestaCorrecta;
    private final String dificultad;
    private final int id;

    public Pregunta(int id, String enunciado, String[] opciones, int respuestaCorrecta, String dificultad) {
        this.id = id;
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
        this.dificultad = dificultad;
    }

    public int getId()               { return id; }
    public String getEnunciado()     { return enunciado; }
    public String[] getOpciones()    { return opciones; }
    public int getRespuestaCorrecta(){ return respuestaCorrecta; }
    public String getDificultad()    { return dificultad; }

    public boolean esCorrecta(int respuesta) { return respuesta == respuestaCorrecta; }
}
