/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Arbol {
    private Nodo raiz;
    private Nodo nodoActual;

    public Arbol() {
        this.raiz = null;
        this.nodoActual = null;
    }

    public String getPreguntaActual() {
        return (nodoActual != null) ? nodoActual.getPregunta() : "Cargue un JSON para comenzar";
    }

    public void avanzar(boolean respuesta) {
        if (nodoActual != null) {
            nodoActual = respuesta ? nodoActual.getIzquierdo() : nodoActual.getDerecho();
        }
    }

    public boolean esEspecie() {
        return (nodoActual != null) && nodoActual.esHoja();
    }

    public String getEspecieActual() {
        return (nodoActual != null && nodoActual.esHoja()) ? nodoActual.getEspecie() : null;
    }

    public Nodo buscar(String especie) {
        return buscarRec(raiz, normalizarTexto(especie));
    }

    private Nodo buscarRec(Nodo nodo, String especie) {
        if (nodo == null) return null;
        if (nodo.esHoja() && normalizarTexto(nodo.getEspecie()).equals(especie)) {
            return nodo;
        }
        Nodo izquierda = buscarRec(nodo.getIzquierdo(), especie);
        return (izquierda != null) ? izquierda : buscarRec(nodo.getDerecho(), especie);
    }

    private String normalizarTexto(String texto) {
        return texto.trim().toLowerCase().replace(" ", "");
    }

    public void cargarDesdeJSON(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) contenido.append(linea);
            
            procesarJSON(contenido.toString());
            nodoActual = raiz;
            JOptionPane.showMessageDialog(null, "Árbol cargado correctamente");
        } catch (IOException | JSONException e) {
            JOptionPane.showMessageDialog(null, "Error en el JSON:\n" + e.getMessage());
        }
    }

    private void procesarJSON(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        String clavePrincipal = jsonObject.keys().next();
        JSONArray especiesArray = jsonObject.getJSONArray(clavePrincipal);

        for (int i = 0; i < especiesArray.length(); i++) {
            JSONObject especieObject = especiesArray.getJSONObject(i);
            String nombreEspecie = especieObject.keys().next();
            JSONArray preguntasArray = especieObject.getJSONArray(nombreEspecie);

            Nodo current = raiz;
            for (int j = 0; j < preguntasArray.length(); j++) {
                JSONObject preguntaObj = preguntasArray.getJSONObject(j);
                String pregunta = preguntaObj.keys().next();
                boolean respuesta = preguntaObj.getBoolean(pregunta);

                if (current == null) {
                    raiz = new Nodo(pregunta, null);
                    current = raiz;
                } else {
                    if (respuesta) {
                        if (current.getIzquierdo() == null) {
                            current.setIzquierdo(new Nodo(pregunta, null));
                        }
                        current = current.getIzquierdo();
                    } else {
                        if (current.getDerecho() == null) {
                            current.setDerecho(new Nodo(pregunta, null));
                        }
                        current = current.getDerecho();
                    }
                }
            }
            
            if (!current.esHoja()) {
                throw new JSONException("Especie '" + nombreEspecie + "' no tiene preguntas suficientes");
            }
            current.setEspecie(nombreEspecie);
        }
    }

    public Nodo getRaiz() {
        return raiz;
    }
}