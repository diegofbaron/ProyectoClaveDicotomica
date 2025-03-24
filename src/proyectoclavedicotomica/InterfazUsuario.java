/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

import org.graphstream.ui.swing_viewer.ViewPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class InterfazUsuario extends JFrame {
    private Arbol arbol;
    private TablaHash tablaHash;
    private JLabel preguntaLabel;
    private JButton siButton, noButton, cargarButton, buscarButton, mostrarArbolButton;
    private JButton verEspecieButton, reiniciarButton;
    private JTextField buscarField;
    private JTextArea resultadoArea;
    private JFrame ventanaArbol;
    private JPanel panelCentral;
    private JScrollPane scrollResultados;

    public InterfazUsuario() {
        arbol = new Arbol();
        tablaHash = new TablaHash();
        configurarInterfaz();
    }

    private void configurarInterfaz() {
        setTitle("Clave Dicotómica");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cargarButton = new JButton("Cargar JSON");
        mostrarArbolButton = new JButton("Mostrar Árbol");
        panelSuperior.add(cargarButton);
        panelSuperior.add(mostrarArbolButton);

        // Panel central
        panelCentral = new JPanel(new BorderLayout());
        preguntaLabel = new JLabel(" Cargue un archivo JSON para comenzar", SwingConstants.CENTER);
        preguntaLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Panel de botones Sí/No
        JPanel panelBotones = new JPanel();
        siButton = new JButton("Sí");
        noButton = new JButton("No");
        siButton.addActionListener(e -> manejarRespuesta(true));
        noButton.addActionListener(e -> manejarRespuesta(false));
        siButton.setEnabled(false);
        noButton.setEnabled(false);
        panelBotones.add(siButton);
        panelBotones.add(noButton);

        // Área de resultados con scroll
        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        scrollResultados = new JScrollPane(resultadoArea);

        // Panel de botones adicionales
        verEspecieButton = new JButton("Ver Especie Actual");
        reiniciarButton = new JButton("Reiniciar");
        JPanel panelBotonesAdicionales = new JPanel();
        panelBotonesAdicionales.add(verEspecieButton);
        panelBotonesAdicionales.add(reiniciarButton);

        // Configuración del panel central
        panelCentral.add(preguntaLabel, BorderLayout.NORTH);
        panelCentral.add(panelBotones, BorderLayout.CENTER);
        panelCentral.add(scrollResultados, BorderLayout.SOUTH);
        panelCentral.add(panelBotonesAdicionales, BorderLayout.PAGE_END);

        // Panel derecho (búsqueda)
        JPanel panelDerecha = new JPanel(new BorderLayout());
        buscarField = new JTextField(15);
        buscarButton = new JButton("Buscar Especie");
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.add(new JLabel("Nombre:"));
        panelBusqueda.add(buscarField);
        panelBusqueda.add(buscarButton);
        panelDerecha.add(panelBusqueda, BorderLayout.NORTH);

        // Contenedor principal
        JPanel panelContenedor = new JPanel(new GridLayout(1, 2));
        panelContenedor.add(panelCentral);
        panelContenedor.add(panelDerecha);

        // Ensamblado final
        add(panelSuperior, BorderLayout.NORTH);
        add(panelContenedor, BorderLayout.CENTER);

        // Listeners
        cargarButton.addActionListener(e -> cargarJSON());
        buscarButton.addActionListener(this::buscarEspecie);
        mostrarArbolButton.addActionListener(e -> mostrarArbolGrafico());
        verEspecieButton.addActionListener(e -> mostrarEspecieActual());
        reiniciarButton.addActionListener(e -> reiniciarFlujo());
    }

    private void cargarJSON() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FiltroJSON());
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                arbol = new Arbol();
                arbol.cargarDesdeJSON(archivo.getAbsolutePath());
                tablaHash = new TablaHash();
                llenarTablaHash(arbol.getRaiz());

                SwingUtilities.invokeLater(() -> {
                    siButton.setEnabled(true);
                    noButton.setEnabled(true);
                    reiniciarFlujo();
                });
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error al cargar el archivo:\n" + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void llenarTablaHash(Nodo nodo) {
        if (nodo == null) return;
        if (nodo.esHoja()) {
            tablaHash.insertar(nodo.getEspecie(), nodo);
        }
        llenarTablaHash(nodo.getIzquierdo());
        llenarTablaHash(nodo.getDerecho());
    }

    private void reiniciarFlujo() {
        SwingUtilities.invokeLater(() -> {
            if (arbol.getRaiz() == null) {
                JOptionPane.showMessageDialog(null, 
                    "Cargue un archivo primero.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            arbol.reiniciarNodoActual();
            actualizarPregunta();
            actualizarPanelCentral();
        });
    }

    private void manejarRespuesta(boolean respuesta) {
    if (arbol == null || arbol.getRaiz() == null) {
        JOptionPane.showMessageDialog(null, 
            "Cargue un archivo JSON primero.", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Avanzar en el árbol según la respuesta
    arbol.avanzar(respuesta);

    // Actualizar la interfaz
    SwingUtilities.invokeLater(() -> {
        actualizarPregunta();
        actualizarPanelCentral();
    });
}

    private void mostrarEspecieActual() {
        String especie = arbol.getEspecieActual();
        if (especie != null) {
            JOptionPane.showMessageDialog(null, 
                "Especie actual: " + especie, 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "Aún no has llegado a una especie. ¡Sigue respondiendo!", 
                "Información", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarPregunta() {
    if (arbol == null || arbol.getRaiz() == null) {
        preguntaLabel.setText("Cargue un archivo JSON para comenzar");
        return;
    }

    String especieActual = arbol.getEspecieActual();
    if (especieActual != null) {
        preguntaLabel.setText("Especie actual: " + especieActual);
        siButton.setEnabled(false);
        noButton.setEnabled(false);
    } else {
        preguntaLabel.setText(arbol.getPreguntaActual());
    }
}

    private void actualizarPanelCentral() {
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private void buscarEspecie(ActionEvent e) {
        String especieInput = buscarField.getText().trim();
        if (especieInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Ingrese un nombre de especie.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Búsqueda por hash
        long inicioHash = System.nanoTime();
        Nodo resultadoHash = tablaHash.buscar(especieInput);
        long tiempoHash = System.nanoTime() - inicioHash;

        // Búsqueda en árbol
        long inicioArbol = System.nanoTime();
        Nodo resultadoArbol = arbol.buscar(especieInput);
        long tiempoArbol = System.nanoTime() - inicioArbol;

        // Mostrar resultados
        resultadoArea.setText("");
        if (resultadoHash != null) {
            resultadoArea.append("[Hash] Especie: " + resultadoHash.getEspecie() + "\n");
            resultadoArea.append("Tiempo: " + tiempoHash + " ns\n");
            resultadoArea.append("Camino: " + obtenerCamino(resultadoHash) + "\n\n");
        }

        if (resultadoArbol != null) {
            resultadoArea.append("[Árbol] Especie: " + resultadoArbol.getEspecie() + "\n");
            resultadoArea.append("Tiempo: " + tiempoArbol + " ns\n");
            resultadoArea.append("Camino: " + obtenerCamino(resultadoArbol) + "\n");
        } else {
            resultadoArea.append("Especie no encontrada.\n");
        }
    }

    private String obtenerCamino(Nodo especie) {
        StringBuilder camino = new StringBuilder();
        Nodo actual = especie;
        while (actual != null) {
            String texto = actual.getPregunta() != null 
                ? actual.getPregunta() 
                : "Especie: " + actual.getEspecie();
            camino.insert(0, texto + " → ");
            actual = arbol.obtenerPadre(actual);
        }
        if (camino.length() > 3) camino.setLength(camino.length() - 3);
        return camino.toString();
    }

    private void mostrarArbolGrafico() {
        if (arbol == null || arbol.getRaiz() == null) {
            JOptionPane.showMessageDialog(null, 
                "Primero cargue un archivo JSON.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ventanaArbol == null) {
            ventanaArbol = new JFrame("Visualización del Árbol");
            ventanaArbol.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventanaArbol.setSize(800, 600);
        }

        VisualizadorArbol visualizador = new VisualizadorArbol();
        ViewPanel panelArbol = visualizador.mostrarArbol(arbol.getRaiz());
        ventanaArbol.getContentPane().removeAll();
        ventanaArbol.add(panelArbol);
        ventanaArbol.revalidate();
        ventanaArbol.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfazUsuario().setVisible(true);
        });
    }
}