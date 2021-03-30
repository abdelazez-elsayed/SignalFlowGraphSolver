import java.util.LinkedList;
import java.util.List;
import org.jgrapht.graph.*;
public class Vertex {
    public Vertex(String name) {
        this.name = name;
    }

    private DirectedWeightedPseudograph<Vertex,Edge> graph;
    private int ID;
    private String  name;


    public int getID() {
        return ID;
    }
    public void setID(int n){
        ID = n;
    }

    public String getName() {
        return name;
    }

    private List<Edge> edges = new LinkedList<Edge>();
   public Vertex(int ID,DirectedWeightedPseudograph<Vertex,Edge> graph){
       this.graph=graph;
               this.ID=ID;
   }
   public void addEdge(Edge edge){
       edges.add(edge);
   }

    public List<Edge> getEdges() {
        return edges;
    }
}
