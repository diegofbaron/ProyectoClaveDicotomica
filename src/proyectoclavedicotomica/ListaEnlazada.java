/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

/**
 *
 * @author diego
 */
public class ListaEnlazada {
    private NodoLista cabeza;

    public void agregar(Nodo dato) {
        NodoLista nuevo = new NodoLista(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoLista actual = cabeza;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
    }

    public Nodo buscar(String especie) {
        NodoLista actual = cabeza;
        while (actual != null) {
            if (actual.dato.getEspecie() != null && 
                normalizarTexto(actual.dato.getEspecie()).equals(especie)) {
                return actual.dato;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    private String normalizarTexto(String texto) {
        return texto.trim().toLowerCase().replace(" ", "");
    }

    private class NodoLista {
        Nodo dato;
        NodoLista siguiente;

        public NodoLista(Nodo dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }
}

