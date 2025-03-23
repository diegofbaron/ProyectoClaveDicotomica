/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

import java.util.LinkedList;
import java.util.List;

public class TablaHash {
    private static final int TAMANO = 128;
    private List<Nodo>[] tabla;

    public TablaHash() {
        tabla = new LinkedList[TAMANO];
        for (int i = 0; i < TAMANO; i++) {
            tabla[i] = new LinkedList<>();
        }
    }

    public void insertar(String clave, Nodo valor) {
        int indice = Math.abs(clave.hashCode()) % TAMANO;
        tabla[indice].add(valor);
    }

    public Nodo buscar(String clave) {
    int indice = Math.abs(clave.hashCode()) % TAMANO;
    for (Nodo nodo : tabla[indice]) {
        if (normalizarTexto(nodo.getEspecie()).equals(clave)) {
            return nodo;
        }
    }
    return null;
}

private String normalizarTexto(String texto) {
    return texto.trim().toLowerCase().replace(" ", "");
}
}