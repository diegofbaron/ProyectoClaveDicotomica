/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author diego
 */
/**
 * Clase que representa un nodo en el árbol.
 */
public class Nodo {
    private String pregunta;
    private String especie;
    private Nodo izquierdo;
    private Nodo derecho;
    private String clave;

    public Nodo(String pregunta, String especie) {
        this.pregunta = pregunta;
        this.especie = especie;
        this.izquierdo = null;
        this.derecho = null;
        this.clave = generarClaveUnica();
    }

    private String generarClaveUnica() {
        return (pregunta != null) 
            ? pregunta.trim().toLowerCase()
                .replace(" ", "")
                .replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o")
                .replace("ú", "u")
            : "Especie-" + especie.trim().toLowerCase()
                .replace(" ", "")
                .replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o")
                .replace("ú", "u");
    }

    public String getPregunta() { return pregunta; }
    public String getEspecie() { return especie; }
    public Nodo getIzquierdo() { return izquierdo; }
    public Nodo getDerecho() { return derecho; }
    public String getClave() { return clave; }
    
    public void setEspecie(String especie) { this.especie = especie; }
    public void setIzquierdo(Nodo izquierdo) { this.izquierdo = izquierdo; }
    public void setDerecho(Nodo derecho) { this.derecho = derecho; }

    public boolean esHoja() {
        return (izquierdo == null && derecho == null);
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}