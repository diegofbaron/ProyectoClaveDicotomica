/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class InterfazUsuario extends JFrame {
    private Arbol arbol;
    private TablaHash tablaHash;
    private JLabel preguntaLabel;
    private JButton siButton, noButton, cargarButton, buscarButton, mostrarArbolButton;
    private JTextField buscarField;
    private JTextArea resultadoArea;

    public InterfazUsuario() {
        arbol = new Arbol();
        tablaHash = new TablaHash();
        configurarInterfaz();
    }

    private void configurarInterfaz() {
        setTitle("Clave Dicotómica");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        preguntaLabel = new JLabel("Cargue un archivo JSON para comenzar.");
        siButton = new JButton("Sí"); 
        noButton = new JButton("No");
        cargarButton = new JButton("Cargar JSON");
        buscarField = new JTextField(20); 
        buscarButton = new JButton("Buscar");
        resultadoArea = new JTextArea(10, 50); 
        resultadoArea.setEditable(false);
        mostrarArbolButton = new JButton("Mostrar Árbol");

        siButton.setEnabled(false); 
        noButton.setEnabled(false);

        JPanel panel = new JPanel();
        panel.add(cargarButton); 
        panel.add(preguntaLabel);
        panel.add(siButton); 
        panel.add(noButton); 
        panel.add(buscarField);
        panel.add(buscarButton); 
        panel.add(mostrarArbolButton);
        panel.add(new JScrollPane(resultadoArea));
        add(panel);

        cargarButton.addActionListener(e -> cargarJSON());
        siButton.addActionListener(e -> avanzar(true));
        noButton.addActionListener(e -> avanzar(false));
        buscarButton.addActionListener(e -> buscarEspecie());
        mostrarArbolButton.addActionListener(e -> mostrarArbolGrafico());

        setVisible(true);
    }

    private void cargarJSON() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FiltroJSON());
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            arbol.cargarDesdeJSON(archivo.getAbsolutePath());
            llenarTablaHash(arbol.getRaiz());
            actualizarInterfaz();
        }
    }

    private void avanzar(boolean respuesta) {
        arbol.avanzar(respuesta);
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        if (arbol.esEspecie()) {
            preguntaLabel.setText("Especie encontrada: " + arbol.getEspecieActual());
            siButton.setEnabled(false); 
            noButton.setEnabled(false);
        } else {
            preguntaLabel.setText(arbol.getPreguntaActual());
            siButton.setEnabled(true); 
            noButton.setEnabled(true);
        }
        resultadoArea.setText(arbolToString(arbol.getRaiz(), ""));
    }

    private void llenarTablaHash(Nodo nodo) {
        if (nodo == null) return;
        if (nodo.esHoja()) {
            tablaHash.insertar(nodo.getEspecie().toLowerCase().replace(" ", ""), nodo);
        }
        llenarTablaHash(nodo.getIzquierdo());
        llenarTablaHash(nodo.getDerecho());
    }

    private String arbolToString(Nodo nodo, String prefijo) {
        if (nodo == null) return "";
        String str = prefijo + nodo.getPregunta() + "\n";
        if (nodo.esHoja()) str += prefijo + "-> " + nodo.getEspecie() + "\n";
        str += arbolToString(nodo.getIzquierdo(), prefijo + "  ");
        str += arbolToString(nodo.getDerecho(), prefijo + "  ");
        return str;
    }

    private void buscarEspecie() {
        String especie = buscarField.getText().trim().toLowerCase().replace(" ", "");
        if (especie.isEmpty()) {
            resultadoArea.setText("Ingrese un nombre de especie.");
            return;
        }

        resultadoArea.setText("");

        long inicioArbol = System.nanoTime();
        Nodo resultadoArbol = arbol.buscar(especie);
        long tiempoArbol = System.nanoTime() - inicioArbol;

        long inicioHash = System.nanoTime();
        Nodo resultadoHash = tablaHash.buscar(especie);
        long tiempoHash = System.nanoTime() - inicioHash;

        if (resultadoArbol != null) {
            resultadoArea.append("Árbol: " + resultadoArbol.getEspecie() + " (" + tiempoArbol + " ns)\n");
        } else {
            resultadoArea.append("Especie no encontrada en el árbol.\n");
        }

        if (resultadoHash != null) {
            resultadoArea.append("Hash: " + resultadoHash.getEspecie() + " (" + tiempoHash + " ns)\n");
        } else {
            resultadoArea.append("Especie no encontrada en la tabla hash.\n");
        }
    }

    private void mostrarArbolGrafico() {
    if (arbol.getRaiz() == null) {
        JOptionPane.showMessageDialog(null, "Primero cargue un archivo JSON.");
        return;
    }

    VisualizadorArbol visualizador = new VisualizadorArbol();
    ViewPanel panelArbol = visualizador.mostrarArbol(arbol.getRaiz());

    JFrame ventanaArbol = new JFrame("Árbol Dicotómico");
    ventanaArbol.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    ventanaArbol.add(panelArbol);
    ventanaArbol.pack();
    ventanaArbol.setLocationRelativeTo(null);
    ventanaArbol.setVisible(true);
}

    private void agregarNodosGrafico(Graph graph, Nodo nodo) {
        if (nodo == null) return;
        String id = (nodo.getPregunta() != null) ? nodo.getPregunta() : nodo.getEspecie();
        graph.addNode(id).setAttribute("ui.label", id);

        if (nodo.getIzquierdo() != null) {
            String hijoId = (nodo.getIzquierdo().getPregunta() != null) ? 
                nodo.getIzquierdo().getPregunta() : nodo.getIzquierdo().getEspecie();
            graph.addEdge(id + "-" + hijoId, id, hijoId);
            agregarNodosGrafico(graph, nodo.getIzquierdo());
        }

        if (nodo.getDerecho() != null) {
            String hijoId = (nodo.getDerecho().getPregunta() != null) ? 
                nodo.getDerecho().getPregunta() : nodo.getDerecho().getEspecie();
            graph.addEdge(id + "-" + hijoId, id, hijoId);
            agregarNodosGrafico(graph, nodo.getDerecho());
        }
    }

    public static void main(String[] args) {
        new InterfazUsuario();
    }
}