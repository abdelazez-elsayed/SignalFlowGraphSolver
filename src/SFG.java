import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;

import java.util.ArrayList;
import java.util.List;

public class SFG {
    private Vertex inputNode;
    private Vertex outputNode;
    private ArrayList<Double> DeltaK;
    private ArrayList<Path> forwardPaths;
    private ArrayList<Path> individualLoops;
    private ArrayList<ArrayList<ArrayList<Path>>> nonTouchingLoops;
    private Graph<String, DefaultWeightedEdge> myGraph = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    private Double Delta;
    Double totalTF;
    public SFG(ArrayList<Vertex> vertices,ArrayList<Edge> edges){
        this.inputNode = vertices.get(0);
        this.outputNode = vertices.get(vertices.size()-1);
        for(Vertex vertex : vertices){
            myGraph.addVertex(vertex.getName());
        }
        for(Edge edgeData : edges){
            //System.out.println(edgeData.toString());
            DefaultWeightedEdge edge = myGraph.addEdge(edgeData.getSourceVertex().getName(), edgeData.getForwardVertex().getName());
            myGraph.setEdgeWeight(edge, edgeData.getGain());
        }
    }
    public boolean addVertex(String name){
        if(!myGraph.containsVertex(name)){
        myGraph.addVertex(name);
        return true;
        }
        else
            return false;
    }

    public ArrayList<Path> getForwardPaths(){
        if(forwardPaths != null) return forwardPaths;
        forwardPaths = new ArrayList<>();
        AllDirectedPaths<String,DefaultWeightedEdge> pathsFinder = new AllDirectedPaths<>(myGraph);
        List<GraphPath<String,DefaultWeightedEdge>>graphPaths =pathsFinder.getAllPaths(inputNode.getName(),outputNode.getName(),true,Integer.MAX_VALUE);
            for (GraphPath<String,DefaultWeightedEdge> graphPath : graphPaths){
                       double gain =1;
                       List<DefaultWeightedEdge> edges = graphPath.getEdgeList();
                        List<String> Vertices = graphPath.getVertexList();
                        Path path = new Path();
                    for(DefaultWeightedEdge edge : edges){
                        gain *= myGraph.getEdgeWeight(edge);

                    }
                    for (String s : Vertices){
                        Vertex v = new Vertex(s);
                        path.addVertexToPath(v);
                    }
                    path.setPathGain(gain);
                    forwardPaths.add(path);
            }
            return forwardPaths;
    }
    public ArrayList<Path> getIndividualLoops(){
        if(individualLoops != null)return individualLoops;
        individualLoops = new ArrayList<>();
        TarjanSimpleCycles<String,DefaultWeightedEdge> cyclesFinder = new TarjanSimpleCycles<>(myGraph);
        List<List<String>> loops = cyclesFinder.findSimpleCycles();
        for(List<String> loop : loops){
            double gain = getGain(loop);
            Path path = new Path();
             for (String s : loop){
                        Vertex v = new Vertex(s);
                        path.addVertexToPath(v);
             }
            path.setPathGain(gain);
            individualLoops.add(path);
        }

        return individualLoops;


    }
    public ArrayList<ArrayList<ArrayList<Path>>> getAllNonTouchingLoops(){
            if(nonTouchingLoops != null) return nonTouchingLoops;
            ArrayList<Path> individualLoops = getIndividualLoops();
        ArrayList<ArrayList<ArrayList<Path>>> nonTouchingLoops = new ArrayList<>();
            boolean flag = false;
            for(int i=2; !flag; i++){
                ArrayList<ArrayList<Path>> nCombinations = Utility.getAllCombinations(individualLoops, i);
                ArrayList<ArrayList<Path>> nNonTouchingLoops = new ArrayList<>();
                for(ArrayList<Path> combination : nCombinations){
                    if(isNonTouchingLoops(combination)){
                        nNonTouchingLoops.add(combination);
                    }
                }
                if(nNonTouchingLoops.isEmpty()){
                    flag = true;
                    continue;
                }
                nonTouchingLoops.add(nNonTouchingLoops);
            }
            this.nonTouchingLoops=nonTouchingLoops;
            return nonTouchingLoops;
    }
    public Double getDelta(){
        if(this.Delta != null ) return Delta;

        double sumOfIndvLoops = 0 ;

        for(Path path : getIndividualLoops()){
           sumOfIndvLoops += path.getPathGain();
        }
        Delta = 1-sumOfIndvLoops;
        int sign = 1;
        double sumOfAllnonTouchingLoops=0;
        for(ArrayList<ArrayList<Path>> nonTouchingLoops : getAllNonTouchingLoops()){
            double sumOfGainProductsOfNNonTouchingLoops = 0;
            for(ArrayList<Path> combination : nonTouchingLoops){
                double gainProductOfCombination = 1;
                for(Path loop : combination){
                    gainProductOfCombination *= loop.getPathGain();
                }
                sumOfGainProductsOfNNonTouchingLoops += gainProductOfCombination;
            }
            sumOfAllnonTouchingLoops += sign * sumOfGainProductsOfNNonTouchingLoops;
            sign *= -1;
        }
        this.Delta +=  sumOfAllnonTouchingLoops;
        return Delta;

    }
    public ArrayList<Double> getDeltaK(){
        if(DeltaK != null){
            return DeltaK;
        }

        DeltaK = new ArrayList<>();

        for(Path forwardPath : getForwardPaths()){
            double sumOfIndividualLoopGains = 0;
            for(Path loop : getIndividualLoops()){
                if(isNonTouchingWithForwardPath(forwardPath, loop)) {
                    sumOfIndividualLoopGains += loop.getPathGain();
                }
            }

            int sign = 1;
            double resultOfAllNonTouchingLoops = 0;

            for(ArrayList<ArrayList<Path>> nNonTouchingLoops : getAllNonTouchingLoops()){
                double sumOfGainProductsOfNNonTouchingLoops = 0;
                for(ArrayList<Path> combination : nNonTouchingLoops){
                    if(isNonTouchingWithForwardPath(forwardPath, combination)) {
                        double gainProductOfCombination = 1;
                        for (Path loop : combination) {
                            gainProductOfCombination *= loop.getPathGain();
                        }
                        sumOfGainProductsOfNNonTouchingLoops += gainProductOfCombination;
                    }
                }
                resultOfAllNonTouchingLoops += sign * sumOfGainProductsOfNNonTouchingLoops;
                sign *= -1;
            }

            double deltaI = 1 - sumOfIndividualLoopGains + resultOfAllNonTouchingLoops;
            DeltaK.add(deltaI);
        }

        return DeltaK;
    }
    public double getTotalTransferFunction(){
        if(totalTF != null){
            return totalTF;
        }

        double sumOfProducts = 0;
        int i=0;

        for(Path forwardPath : getForwardPaths()){
            sumOfProducts += forwardPath.getPathGain() * getDeltaK().get(i);
            i++;
        }

        totalTF= sumOfProducts / getDelta();

        return totalTF;
    }
    private boolean isNonTouchingWithForwardPath(Path forwardPath, ArrayList<Path> nonTouchingCombination){
        ArrayList<String> allVerticesInCombination = new ArrayList<>();
        for(Path loop : nonTouchingCombination){
            allVerticesInCombination.addAll(loop.getVertices());
        }
        for(String vertex : forwardPath.getVertices()){
            if(allVerticesInCombination.contains(vertex))
                return false;
        }
        return true;
    }
    private boolean isNonTouchingWithForwardPath(Path forwardPath, Path loop){

        for(String vertex : forwardPath.getVertices()){
            if(loop.getVertices().contains(vertex))
                return false;
        }
        return true;
    }
    private boolean isNonTouchingLoops(ArrayList<Path> combination){
        for(int i=0; i<combination.size(); i++){
            for(int j=0; j<combination.get(i).getVertices().size(); j++){
                String vertex = combination.get(i).getVertices().get(j);
                for(int k=i+1; k<combination.size(); k++){
                    if(combination.get(k).getVertices().contains(vertex)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private double getGain(List<String> cycle) {
        double gain = 1;
        for(int i=0; i<cycle.size()-1; i++){
            DefaultWeightedEdge edge = myGraph.getEdge(cycle.get(i), cycle.get(i+1));
            gain *= myGraph.getEdgeWeight(edge);
        }
        DefaultWeightedEdge edge = myGraph.getEdge(cycle.get(cycle.size()-1), cycle.get(0));
        gain *= myGraph.getEdgeWeight(edge);
        return gain;
    }

}
