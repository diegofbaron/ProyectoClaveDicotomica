/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

/**
 *
 * @author diego
 */
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

public class VisualizadorArbol {
    private Graph grafo;
    private SwingViewer visor;
    private ViewPanel panelVisual;

    public VisualizadorArbol() {
        System.setProperty("org.graphstream.ui", "swing");
        grafo = new SingleGraph("Árbol Dicotómico");
        grafo.setAttribute("ui.stylesheet", 
            "node { fill-color: #FFA500; size: 20px; text-alignment: under; }");
    }

    public ViewPanel mostrarArbol(Nodo raiz) {
        if (raiz == null) return null;
        
        grafo.clear(); // Limpiar grafo existente
        agregarNodos(raiz);
        
        // Configurar visor integrable en Swing
        visor = new SwingViewer(
            grafo, 
            SwingViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD
        );
        
        panelVisual = (ViewPanel) visor.addDefaultView(false);
        return panelVisual;
    }

    private void agregarNodos(Nodo nodo) {
        if (nodo == null) return;

        String id = (nodo.getPregunta() != null) 
            ? nodo.getPregunta() 
            : nodo.getEspecie();
        
        grafo.addNode(id).setAttribute("ui.label", id);

        if (nodo.getIzquierdo() != null) {
            String hijoId = (nodo.getIzquierdo().getPregunta() != null) 
                ? nodo.getIzquierdo().getPregunta() 
                : nodo.getIzquierdo().getEspecie();
            grafo.addEdge(id + "-izq", id, hijoId);
            agregarNodos(nodo.getIzquierdo());
        }

        if (nodo.getDerecho() != null) {
            String hijoId = (nodo.getDerecho().getPregunta() != null) 
                ? nodo.getDerecho().getPregunta() 
                : nodo.getDerecho().getEspecie();
            grafo.addEdge(id + "-der", id, hijoId);
            agregarNodos(nodo.getDerecho());
        }
    }
}