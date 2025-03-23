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

    public Nodo(String pregunta, String especie) {
        this.pregunta = pregunta;
        this.especie = especie;
        this.izquierdo = null;
        this.derecho = null;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
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

    public boolean esHoja() {
        return (izquierdo == null && derecho == null);
    }

    @Override
    public String toString() {
        return "Nodo{" + "pregunta='" + pregunta + '\'' + ", especie='" + especie + '\'' + '}';
    }
}