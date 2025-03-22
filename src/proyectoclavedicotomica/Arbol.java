/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

import org.json.JSONArray;
import org.json.JSONObject;
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
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), pregunta, respuesta, especie));
        }
        // Si la respuesta es "No", vamos al hijo derecho
        else {
            nodo.setDerecho(insertarRec(nodo.getDerecho(), pregunta, respuesta, especie));
        }

        return nodo;
    }

    // Método para obtener la pregunta actual
    public String getPreguntaActual() {
        if (raiz == null) {
            return null;
        }
        return raiz.getPregunta();
    }

    // Método para avanzar al siguiente nodo según la respuesta del usuario
    public void avanzar(boolean respuesta) {
        if (raiz != null) {
            if (respuesta) {
                raiz = raiz.getIzquierdo();
            } else {
                raiz = raiz.getDerecho();
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
            return raiz.getEspecie();
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

    // Método para procesar el JSON y construir el árbol
    private void procesarJSON(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray especiesArray = jsonObject.getJSONArray("ClaveDicotomica");

        for (int i = 0; i < especiesArray.length(); i++) {
            JSONObject especieObject = especiesArray.getJSONObject(i);
            String nombreEspecie = especieObject.keys().next();
            JSONArray preguntasArray = especieObject.getJSONArray(nombreEspecie);

            Nodo nodoActual = null;
            for (int j = 0; j < preguntasArray.length(); j++) {
                JSONObject preguntaObject = preguntasArray.getJSONObject(j);
                String pregunta = preguntaObject.keys().next();
                boolean respuesta = preguntaObject.getBoolean(pregunta);

                if (nodoActual == null) {
                    this.insertar(pregunta, respuesta, nombreEspecie);
                    nodoActual = this.raiz;
                } else {
                    if (respuesta) {
                        nodoActual.setIzquierdo(new Nodo(pregunta, nombreEspecie));
                        nodoActual = nodoActual.getIzquierdo();
                    } else {
                        nodoActual.setDerecho(new Nodo(pregunta, nombreEspecie));
                        nodoActual = nodoActual.getDerecho();
                    }
                }
            }
        }
    }
}