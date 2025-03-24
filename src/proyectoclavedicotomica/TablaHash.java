/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;



public class TablaHash {
    private static final int TAMANO = 128;
    private ListaEnlazada[] tabla;

    public TablaHash() {
        tabla = new ListaEnlazada[TAMANO];
        for (int i = 0; i < TAMANO; i++) {
            tabla[i] = new ListaEnlazada();
        }
    }

    public void insertar(String clave, Nodo valor) {
        String claveNormalizada = normalizarTexto(clave);
        int indice = Math.abs(claveNormalizada.hashCode()) % TAMANO;
        tabla[indice].agregar(valor);
    }

    public Nodo buscar(String clave) {
        String claveNormalizada = normalizarTexto(clave);
        int indice = Math.abs(claveNormalizada.hashCode()) % TAMANO;
        return tabla[indice].buscar(claveNormalizada);
    }

    private String normalizarTexto(String texto) {
        return texto.trim().toLowerCase().replace(" ", "");
    }
}