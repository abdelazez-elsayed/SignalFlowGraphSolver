import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Vertex> vertices;
    private double pathGain;
    private ArrayList<String> VNames;

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public double getPathGain() {
        return pathGain;
    }

    public void setPathGain(double pathGain) {
        this.pathGain = pathGain;
    }

    public ArrayList<String> getVertices() {
        VNames = new ArrayList<>();
        for(Vertex v : vertices)
            VNames.add(v.getName());
        return VNames;
    }
    public List<Vertex> getVerticesAsVertex() {
        return vertices;
    }

    public Path(List<Vertex> vertices, double pathGain) {
        this.vertices = vertices;
        this.pathGain = pathGain;
    }
    public Path(){
        vertices = new ArrayList<>();

    }
    public void addVertexToPath(Vertex V){
        vertices.add(V);
    }
}
