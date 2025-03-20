/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

/**
 *
 * @author diego
 */
public class Arbol {
    private Nodo raiz;

    // Constructor
    public Arbol() {
        this.raiz = null;
    }

    // Método para insertar un nodo en el árbol
    public void insertar(String pregunta, boolean respuesta) {
        raiz = insertarRec(raiz, pregunta, respuesta);
    }

    // Método recursivo para insertar un nodo
    private Nodo insertarRec(Nodo nodo, String pregunta, boolean respuesta) {
        if (nodo == null) {
            return new Nodo(pregunta, respuesta);
        }

        // Si la respuesta es "Sí", vamos al hijo izquierdo
        if (respuesta) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, pregunta, respuesta);
        }
        // Si la respuesta es "No", vamos al hijo derecho
        else {
            nodo.derecho = insertarRec(nodo.derecho, pregunta, respuesta);
        }

        return nodo;
    }

    // Método para buscar una especie en el árbol
    public Nodo buscar(String especie) {
        return buscarRec(raiz, especie);
    }

    // Método recursivo para buscar una especie
    private Nodo buscarRec(Nodo nodo, String especie) {
        if (nodo == null || nodo.pregunta.equals(especie)) {
            return nodo;
        }

        // Buscar en el subárbol izquierdo
        Nodo resultadoIzquierdo = buscarRec(nodo.izquierdo, especie);
        if (resultadoIzquierdo != null) {
            return resultadoIzquierdo;
        }

        // Buscar en el subárbol derecho
        return buscarRec(nodo.derecho, especie);
    }

    // Clase interna para representar un nodo del árbol
    private class Nodo {
        String pregunta;
        boolean respuesta;
        Nodo izquierdo;
        Nodo derecho;

        public Nodo(String pregunta, boolean respuesta) {
            this.pregunta = pregunta;
            this.respuesta = respuesta;
            this.izquierdo = null;
            this.derecho = null;
        }
    }
}
