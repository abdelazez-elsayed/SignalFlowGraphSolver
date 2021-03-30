import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.fxml.FXML;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;


public class Control {
    static     ArrayList<Edge> edgesList = new ArrayList<>();
    static ArrayList<Vertex> verticesList;
    SFG sfg;
    int totalNodesNumber;
      @FXML
      TextField nodesBox;
      @FXML
      TextField fromBox;
    @FXML
    TextField toBox;
    @FXML
    TextField weightBox;
    @FXML
    TextArea outputArea;
    @FXML
    public void AddNodes(){
        if(verticesList==null)  verticesList = new ArrayList<>();
            int n = Integer.parseInt(nodesBox.getText());

            for(int i = totalNodesNumber+1; i<=totalNodesNumber+n; i++){
                Vertex v = new Vertex("X"+i);
                v.setID(i);
                verticesList.add(v);
            }
             totalNodesNumber += n;

    }
    public void disply(){
        Stage window;

        Graph<String, String> g = new DigraphEdgeList<>();
        Button closeButton = new Button("Close");

        Label label = new Label("You can drag vertices for more visualization");

        ArrayList<String> helper = new ArrayList<>();
//... see example below
        if(verticesList != null) {

            for (Vertex vertex : verticesList)
                g.insertVertex(vertex.getName());
        }
        if(edgesList != null){
        for(Edge edge : edgesList){
            if(!helper.contains(String.valueOf(edge.getGain()))){
                g.insertEdge(edge.getSourceVertex().getName(),edge.getForwardVertex().getName(),String.valueOf(edge.getGain()));
                helper.add(String.valueOf(edge.getGain()));
            }else {
                String s = String.valueOf(edge.getGain());
                do {
                    s += " ";
                }while (helper.contains(s));
                helper.add(s);
                g.insertEdge(edge.getSourceVertex().getName(),edge.getForwardVertex().getName(),s);

            }
        }}




        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);

        Scene scene = new Scene(graphView,1024, 768);

        window = new Stage(StageStyle.DECORATED);
        graphView.getChildren().addAll(label);
       window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Graph Visualization");
        closeButton.setOnAction(e -> window.close());
        window.setScene(scene);
        graphView.init();
        window.showAndWait();


    }

    public void addEdge(){
        if(edgesList==null) edgesList=new ArrayList<>();
        int from = Integer.parseInt(fromBox.getText());
        int to = Integer.parseInt(toBox.getText());
        double weight = Double.parseDouble(weightBox.getText());

        if(from>=1 && from <=totalNodesNumber && to >=1 && to<=totalNodesNumber ){
            Edge edge =findEdge(from,to);
            if(edge==null) {
                Vertex v1 = verticesList.get(from - 1);
                Vertex v2 = verticesList.get(to - 1);


                edge = new Edge(v1, v2, weight);
                edgesList.add(edge);
            }else {
                edge.setGain(edge.getGain()+weight);
            }
        }
    }

    public void removeEdge(){
        int from = Integer.parseInt(fromBox.getText());
        int to = Integer.parseInt(toBox.getText());
        double weight = Double.parseDouble(weightBox.getText());
        for(int i=0;i<edgesList.size();i++){
            Edge e = edgesList.get(i);
            if(e.getSourceVertex().getID()==from &&e.getForwardVertex().getID()==to ){
                e.setGain(e.getGain()-weight);
                if(e.getGain()==0)
                    edgesList.remove(e);
            }
        }

    }
    private Edge findEdge(int from,int to){
        for(Edge edge : edgesList){
            if(edge.getSourceVertex().getID()==from &&edge.getForwardVertex().getID()==to ){
               return edge;
            }
        }
        return null;
    }

    public void calcualteTF(){
        sfg = new SFG(verticesList,edgesList);
        sfg.getTotalTransferFunction();
        String s = String.valueOf(sfg.getTotalTransferFunction());
        outputArea.setText(s);

    }


}
