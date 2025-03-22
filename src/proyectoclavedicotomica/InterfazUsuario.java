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
                long inicio = System.nanoTime();
                Nodo resultado = arbol.buscar(especie);  // Llamada al método buscar
                long fin = System.nanoTime();
                long tiempoArbol = fin - inicio;

                if (resultado != null) {
                    preguntaLabel.setText("Especie encontrada: " + resultado.getEspecie() +
                            " | Tiempo Árbol: " + tiempoArbol + " ns");
                } else {
                    preguntaLabel.setText("Especie no encontrada.");
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

    public static void main(String[] args) {
        new InterfazUsuario();
    }
}