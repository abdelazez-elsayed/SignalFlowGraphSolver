public class Edge {
    private Vertex sourceVertex;
    private Vertex forwardVertex;
    private double gain;
    public Edge(Vertex sourceVertex, Vertex forwardVertex, double gain) {
        this.sourceVertex = sourceVertex;
        this.forwardVertex = forwardVertex;
        this.gain = gain;
    }
    public Vertex getSourceVertex() {
        return sourceVertex;
    }

    public Vertex getForwardVertex() {
        return forwardVertex;
    }

    public double getGain() {
        return gain;
    }



    public void setSourceVertex(Vertex sourceVertex) {
        this.sourceVertex = sourceVertex;
    }

    public void setForwardVertex(Vertex forwardVertex) {
        this.forwardVertex = forwardVertex;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }
}
