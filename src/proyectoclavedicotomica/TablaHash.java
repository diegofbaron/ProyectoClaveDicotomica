/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

/**
 *
 * @author diego
 */
public class TablaHash {
    private static final int TAMANO_TABLA = 128;
    private Nodo[] tabla;

    // Constructor
    public TablaHash() {
        tabla = new Nodo[TAMANO_TABLA];
    }

    // Método para insertar una especie en la tabla hash
    public void insertar(String especie, Nodo nodo) {
        int hash = calcularHash(especie);
        tabla[hash] = nodo;
    }

    // Método para buscar una especie en la tabla hash
    public Nodo buscar(String especie) {
        int hash = calcularHash(especie);
        return tabla[hash];
    }

    // Método para calcular el hash de una especie
    private int calcularHash(String especie) {
        return Math.abs(especie.hashCode()) % TAMANO_TABLA;
    }
}
