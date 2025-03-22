/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class InterfazUsuario extends JFrame {
    private Arbol arbol;
    private TablaHash tablaHash;
    private JLabel preguntaLabel;
    private JButton siButton;
    private JButton noButton;
    private JButton cargarButton;
    private JTextField buscarField;
    private JButton buscarButton;
    private JTextArea resultadoArea;

    public InterfazUsuario() {
        arbol = new Arbol();
        tablaHash = new TablaHash();

        setTitle("Clave Dicotómica");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Componentes de la interfaz
        preguntaLabel = new JLabel("Cargue un archivo JSON para comenzar.");
        siButton = new JButton("Sí");
        noButton = new JButton("No");
        cargarButton = new JButton("Cargar Archivo JSON");
        buscarField = new JTextField(20);
        buscarButton = new JButton("Buscar Especie");
        resultadoArea = new JTextArea(10, 50);
        resultadoArea.setEditable(false);

        // Deshabilitar botones "Sí" y "No" inicialmente
        siButton.setEnabled(false);
        noButton.setEnabled(false);

        // Configurar el layout
        JPanel panel = new JPanel();
        panel.add(cargarButton);
        panel.add(preguntaLabel);
        panel.add(siButton);
        panel.add(noButton);
        panel.add(buscarField);
        panel.add(buscarButton);
        panel.add(new JScrollPane(resultadoArea));
        add(panel);

        // Manejar la carga del archivo JSON
        cargarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File archivo = fileChooser.getSelectedFile();
                    arbol.cargarDesdeJSON(archivo.getAbsolutePath());
                    llenarTablaHash();
                    preguntaLabel.setText(arbol.getPreguntaActual());
                    siButton.setEnabled(true);
                    noButton.setEnabled(true);
                }
            }
        });

        // Manejar la respuesta "Sí"
        siButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                arbol.avanzar(true);
                actualizarInterfaz();
            }
        });

        // Manejar la respuesta "No"
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                arbol.avanzar(false);
                actualizarInterfaz();
            }
        });

        // Manejar la búsqueda de especie
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String especie = buscarField.getText();
                resultadoArea.setText("");  // Limpiar el área de resultados

                // Búsqueda en el árbol
                long inicioArbol = System.nanoTime();
                Nodo resultadoArbol = arbol.buscar(especie);
                long finArbol = System.nanoTime();
                long tiempoArbol = finArbol - inicioArbol;

                // Búsqueda en la tabla hash
                long inicioHash = System.nanoTime();
                Nodo resultadoHash = tablaHash.buscar(especie);
                long finHash = System.nanoTime();
                long tiempoHash = finHash - inicioHash;

                // Mostrar resultados
                if (resultadoArbol != null) {
                    resultadoArea.append("Especie encontrada en el árbol: " + resultadoArbol.getEspecie() +
                            " | Tiempo Árbol: " + tiempoArbol + " ns\n");
                } else {
                    resultadoArea.append("Especie no encontrada en el árbol.\n");
                }

                if (resultadoHash != null) {
                    resultadoArea.append("Especie encontrada en la tabla hash: " + resultadoHash.getEspecie() +
                            " | Tiempo Hash: " + tiempoHash + " ns\n");
                } else {
                    resultadoArea.append("Especie no encontrada en la tabla hash.\n");
                }
            }
        });

        setVisible(true);
    }

    // Método para actualizar la interfaz después de cada respuesta
    private void actualizarInterfaz() {
        if (arbol.esEspecie()) {
            preguntaLabel.setText("Especie encontrada: " + arbol.getEspecieActual());
            siButton.setEnabled(false);
            noButton.setEnabled(false);
        } else {
            preguntaLabel.setText(arbol.getPreguntaActual());
        }
    }

    // Método para llenar la tabla hash con las especies del árbol
    private void llenarTablaHash() {
        // Este método debe recorrer el árbol y agregar todas las especies a la tabla hash
        // Aquí se asume que el árbol ya está cargado
        // Puedes implementar un recorrido del árbol (por ejemplo, inorden) para llenar la tabla hash
        // Este es un ejemplo simplificado:
        if (arbol.getEspecieActual() != null) {
            tablaHash.insertar(arbol.getEspecieActual(), new Nodo("", arbol.getEspecieActual()));
        }
    }

    public static void main(String[] args) {
        new InterfazUsuario();
    }
}