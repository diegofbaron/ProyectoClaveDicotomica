/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

/**
 *
 * @author diego
 */
public class Nodo {
    // Atributos
    private String pregunta;      // La pregunta asociada al nodo
    private boolean respuesta;    // La respuesta (true = Sí, false = No)
    private Nodo izquierdo;       // Referencia al hijo izquierdo
    private Nodo derecho;         // Referencia al hijo derecho

    // Constructor
    public Nodo(String pregunta, boolean respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.izquierdo = null;
        this.derecho = null;
    }

    // Getters y Setters
    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public boolean getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(boolean respuesta) {
        this.respuesta = respuesta;
    }

    public Nodo getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(Nodo izquierdo) {
        this.izquierdo = izquierdo;
    }

    public Nodo getDerecho() {
        return derecho;
    }

    public void setDerecho(Nodo derecho) {
        this.derecho = derecho;
    }

    // Método para verificar si el nodo es una hoja (no tiene hijos)
    public boolean esHoja() {
        return izquierdo == null && derecho == null;
    }

    @Override
    public String toString() {
        return "Nodo{" +
                "pregunta='" + pregunta + '\'' +
                ", respuesta=" + respuesta +
                '}';
    }
}
