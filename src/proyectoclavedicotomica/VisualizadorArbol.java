/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;


import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

public class VisualizadorArbol {
    private Graph grafo;

    public VisualizadorArbol() {
        System.setProperty("org.graphstream.ui", "swing");
        grafo = new SingleGraph("Clave Dicotómica");
        grafo.setAttribute("ui.stylesheet", 
            "node.pregunta { fill-color: #FFA500; size: 30px; text-size: 14; }" +
            "node.especie { fill-color: #90EE90; size: 30px; text-size: 14; }" +
            "edge { text-alignment: above; text-size: 12; }");
    }

    public ViewPanel mostrarArbol(Nodo raiz) {
        grafo.clear();
        agregarNodos(raiz, null);
        grafo.setAttribute("ui.layout", "org.graphstream.ui.layout.HierarchicalLayout");
        
        // Añadir etiquetas "Sí" y "No"
        for (int i = 0; i < grafo.getEdgeCount(); i++) {
            Edge edge = grafo.getEdge(i);
            Node nodoPadre = edge.getSourceNode();
            Node nodoHijo = edge.getTargetNode();

            Nodo padre = (Nodo) nodoPadre.getAttribute("nodo");
            Nodo hijo = (Nodo) nodoHijo.getAttribute("nodo");

            if (padre != null && hijo != null) {
                edge.setAttribute("ui.label", (padre.getIzquierdo() == hijo) ? "Sí" : "No");
            }
        }

        Viewer viewer = new SwingViewer(grafo, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false);
        viewer.enableAutoLayout();
        return viewPanel;
    }

    private void agregarNodos(Nodo nodo, String parentId) {
        if (nodo == null) return;

        String id = nodo.getClave();
        Node nodoGrafo = grafo.getNode(id);
        if (nodoGrafo == null) {
            nodoGrafo = grafo.addNode(id);
            String etiqueta = (nodo.getPregunta() != null) ? nodo.getPregunta() : nodo.getEspecie();
            nodoGrafo.setAttribute("ui.label", etiqueta);
            nodoGrafo.setAttribute("ui.class", nodo.esHoja() ? "especie" : "pregunta");
            nodoGrafo.setAttribute("nodo", nodo);
        }

        if (parentId != null) {
            String edgeId = parentId + "-" + id;
            if (grafo.getEdge(edgeId) == null) {
                grafo.addEdge(edgeId, parentId, id, true);
            }
        }

        agregarNodos(nodo.getIzquierdo(), id);
        agregarNodos(nodo.getDerecho(), id);
    }
}