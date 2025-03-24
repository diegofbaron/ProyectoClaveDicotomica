/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;



import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Arbol {
    private Nodo raiz;
    private Nodo nodoActual;
    private ListaNodos nodosCreados;
    public ListaPadres padres;

    private static class ListaNodos {
        private Nodo[] nodos;
        private int tamaño;

        public ListaNodos() {
            nodos = new Nodo[100];
            tamaño = 0;
        }

        public void agregar(Nodo nodo) {
            if (tamaño >= nodos.length) {
                Nodo[] nuevos = new Nodo[nodos.length * 2];
                System.arraycopy(nodos, 0, nuevos, 0, tamaño);
                nodos = nuevos;
            }
            nodos[tamaño++] = nodo;
        }

        public Nodo buscar(String clave) {
            for (int i = 0; i < tamaño; i++) {
                if (nodos[i].getClave().equals(clave)) {
                    return nodos[i];
                }
            }
            return null;
        }
    }

    public static class ListaPadres {
        private ParNodo[] pares;
        private int tamaño;

        public ListaPadres() {
            pares = new ParNodo[100];
            tamaño = 0;
        }

        public void agregar(Nodo hijo, Nodo padre) {
            if (tamaño >= pares.length) {
                ParNodo[] nuevos = new ParNodo[pares.length * 2];
                System.arraycopy(pares, 0, nuevos, 0, tamaño);
                pares = nuevos;
            }
            pares[tamaño++] = new ParNodo(hijo, padre);
        }

        public Nodo obtenerPadre(Nodo hijo) {
            for (int i = 0; i < tamaño; i++) {
                if (pares[i].hijo == hijo) return pares[i].padre;
            }
            return null;
        }

        public void clear() {
            tamaño = 0;
        }

        private static class ParNodo {
            Nodo hijo;
            Nodo padre;

            public ParNodo(Nodo hijo, Nodo padre) {
                this.hijo = hijo;
                this.padre = padre;
            }
        }
    }

    public Arbol() {
        this.raiz = null;
        this.nodoActual = null;
        this.nodosCreados = new ListaNodos();
        this.padres = new ListaPadres();
    }

    public void cargarDesdeJSON(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) contenido.append(linea);
            
            procesarJSON(contenido.toString());
            nodoActual = raiz;
            JOptionPane.showMessageDialog(null, "Árbol cargado correctamente");
            imprimirArbol();
        } catch (IOException | JSONException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el JSON:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarJSON(String json) throws JSONException {
    JSONObject jsonObject = new JSONObject(json);
    String clavePrincipal = jsonObject.keys().next();
    JSONArray especiesArray = jsonObject.getJSONArray(clavePrincipal);

    raiz = null;
    nodosCreados = new ListaNodos();
    padres.clear();

    for (int i = 0; i < especiesArray.length(); i++) {
        JSONObject especieObject = especiesArray.getJSONObject(i);
        String nombreEspecie = especieObject.keys().next();
        JSONArray preguntasArray = especieObject.getJSONArray(nombreEspecie);

        Nodo parent = raiz;

        for (int j = 0; j < preguntasArray.length(); j++) {
            JSONObject preguntaObj = preguntasArray.getJSONObject(j);
            String preguntaTextoOriginal = preguntaObj.keys().next();
            String preguntaTexto = normalizarTexto(preguntaTextoOriginal);
            boolean respuesta = preguntaObj.getBoolean(preguntaTextoOriginal);

            // Buscar o crear nodo
            Nodo nodoActual = nodosCreados.buscar(preguntaTexto);
            if (nodoActual == null) {
                nodoActual = new Nodo(preguntaTexto, null);
                nodoActual.setClave(preguntaTexto);
                nodosCreados.agregar(nodoActual);
                if (raiz == null) raiz = nodoActual;
            }

            // Vincular al padre según la respuesta
            if (parent != null) {
                if (respuesta) {
                    if (parent.getIzquierdo() == null) {
                        parent.setIzquierdo(nodoActual);
                    }
                } else {
                    if (parent.getDerecho() == null) {
                        parent.setDerecho(nodoActual);
                    }
                }
                padres.agregar(nodoActual, parent);
            }

            parent = nodoActual; // Movido DENTRO del bucle de preguntas
        }

        // Asignar especie al último nodo (sin eliminar sus hijos)
        if (parent != null) {
            parent.setEspecie(nombreEspecie);
        }
    }
}

    private String normalizarTexto(String texto) {
        return texto.trim()
            .toLowerCase()
            .replace(" ", "")
            .replace("á", "a").replace("é", "e")
            .replace("í", "i").replace("ó", "o")
            .replace("ú", "u");
    }

    public void imprimirArbol() {
        imprimirNodo(raiz, 0);
    }

    private void imprimirNodo(Nodo nodo, int nivel) {
        if (nodo == null) return;
        String espacios = "  ".repeat(nivel);
        String tipo = nodo.esHoja() ? "[HOJA] " : "[NODO] ";
        System.out.println(espacios + tipo + nodo.getPregunta() + " | Especie: " + nodo.getEspecie());
        imprimirNodo(nodo.getIzquierdo(), nivel + 1);
        imprimirNodo(nodo.getDerecho(), nivel + 1);
    }

    public void avanzar(boolean respuesta) {
    if (nodoActual == null) return;

    Nodo siguiente = respuesta ? nodoActual.getIzquierdo() : nodoActual.getDerecho();
    if (siguiente != null) {
        nodoActual = siguiente;
    } else {
        // Mostrar especie si es hoja aunque no tenga hijos
        if (nodoActual.esHoja()) {
            JOptionPane.showMessageDialog(null, "Especie identificada: " + nodoActual.getEspecie());
        } else {
            JOptionPane.showMessageDialog(null, "¡Camino inválido! Reiniciando...");
        }
        reiniciarNodoActual();
    }
}

    public Nodo buscar(String especie) {
        String nombreNormalizado = normalizarTexto(especie);
        return buscarRec(raiz, nombreNormalizado);
    }

    private Nodo buscarRec(Nodo nodo, String especie) {
        if (nodo == null) return null;
        if (nodo.esHoja() && normalizarTexto(nodo.getEspecie()).equals(especie)) {
            return nodo;
        }
        Nodo izquierda = buscarRec(nodo.getIzquierdo(), especie);
        return (izquierda != null) ? izquierda : buscarRec(nodo.getDerecho(), especie);
    }

    public Nodo getRaiz() {
        return raiz;
    }

    public void reiniciarNodoActual() {
        nodoActual = raiz;
    }

    public String getPreguntaActual() {
        return (nodoActual != null) ? nodoActual.getPregunta() : "Cargue un JSON para comenzar";
    }

    public boolean esEspecie() {
        return (nodoActual != null) && nodoActual.esHoja();
    }

    public String getEspecieActual() {
        return (nodoActual != null && nodoActual.esHoja()) ? nodoActual.getEspecie() : null;
    }

    public Nodo obtenerPadre(Nodo hijo) {
        return padres.obtenerPadre(hijo);
    }
}