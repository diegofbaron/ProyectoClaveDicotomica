/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Arbol {
    private Nodo raiz;

    // Constructor
    public Arbol() {
        this.raiz = null;
    }

    // Método para insertar una pregunta o una especie en el árbol
    public void insertar(String pregunta, boolean respuesta, String especie) {
        raiz = insertarRec(raiz, pregunta, respuesta, especie);
    }

    // Método recursivo para insertar
    private Nodo insertarRec(Nodo nodo, String pregunta, boolean respuesta, String especie) {
        if (nodo == null) {
            return new Nodo(pregunta, especie);
        }

        // Si la respuesta es "Sí", vamos al hijo izquierdo
        if (respuesta) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, pregunta, respuesta, especie);
        }
        // Si la respuesta es "No", vamos al hijo derecho
        else {
            nodo.derecho = insertarRec(nodo.derecho, pregunta, respuesta, especie);
        }

        return nodo;
    }

    // Método para obtener la pregunta actual
    public String getPreguntaActual() {
        if (raiz == null) {
            return null;
        }
        return raiz.pregunta;
    }

    // Método para avanzar al siguiente nodo según la respuesta del usuario
    public void avanzar(boolean respuesta) {
        if (raiz != null) {
            if (respuesta) {
                raiz = raiz.izquierdo;
            } else {
                raiz = raiz.derecho;
            }
        }
    }

    // Método para verificar si se ha llegado a una especie (nodo hoja)
    public boolean esEspecie() {
        return raiz != null && raiz.esHoja();
    }

    // Método para obtener la especie actual (nodo hoja)
    public String getEspecieActual() {
        if (raiz != null && raiz.esHoja()) {
            return raiz.especie;
        }
        return null;
    }

    // Método para cargar el árbol desde un archivo JSON
    public void cargarDesdeJSON(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea);
            }
            procesarJSON(contenido.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para procesar manualmente el JSON
    
    private void procesarJSON(String json) {
    // Eliminamos los espacios y saltos de línea
    json = json.replaceAll("\\s", "");

    // Extraemos las especies y las preguntas
    int inicioEspecie = json.indexOf("\"") + 1;
    int finEspecie = json.indexOf("\"", inicioEspecie);
    String especie = json.substring(inicioEspecie, finEspecie);

    int inicioPregunta = json.indexOf("\"", finEspecie + 1) + 1;
    int finPregunta = json.indexOf("\"", inicioPregunta);
    String pregunta = json.substring(inicioPregunta, finPregunta);

    int inicioRespuesta = json.indexOf(":", finPregunta) + 1;
    boolean respuesta = json.substring(inicioRespuesta, inicioRespuesta + 4).trim().equals("true");

    // Insertamos la pregunta y la especie en el árbol
    this.insertar(pregunta, respuesta, especie);
}

    // Clase interna para representar un nodo del árbol
    private class Nodo {
        String pregunta;  // La pregunta (nodo interno)
        String especie;   // La especie (nodo hoja)
        Nodo izquierdo;   // Hijo izquierdo (respuesta "Sí")
        Nodo derecho;     // Hijo derecho (respuesta "No")

        // Constructor para nodos internos (preguntas)
        public Nodo(String pregunta, String especie) {
            this.pregunta = pregunta;
            this.especie = especie;
            this.izquierdo = null;
            this.derecho = null;
        }

        // Método para verificar si el nodo es una hoja
        public boolean esHoja() {
            return izquierdo == null && derecho == null;
        }
    }
}