package sim.app.trafficsimgeo.model.entity;

import com.vividsolutions.jts.geom.LineString;

public class Edge extends PartOfGraph {
//    private int id;
    private Node nodeFrom;
    private Node nodeTo;
//    private LineString geometry;
    private boolean onewayFromTo;
    private boolean onewayToFrom;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public Node getNodeFrom() {
        return nodeFrom;
    }

    public void setNodeFrom(Node nodeFrom) {
        this.nodeFrom = nodeFrom;
    }

    public Node getNodeTo() {
        return nodeTo;
    }

    public void setNodeTo(Node nodeTo) {
        this.nodeTo = nodeTo;
    }

    @Override
    public LineString getGeometry() {
        return (LineString) geometry;
    }

    public void setGeometry(LineString geometry) {
        this.geometry = geometry;
    }

    public boolean isOnewayFromTo() {
        return onewayFromTo;
    }

    public void setOnewayFromTo(boolean onewayFromTo) {
        this.onewayFromTo = onewayFromTo;
    }

    public boolean isOnewayToFrom() {
        return onewayToFrom;
    }

    public void setOnewayToFrom(boolean onewayToFrom) {
        this.onewayToFrom = onewayToFrom;
    }

    public Edge() {
    }

    @Override
    public String toString() {
        String toString = "";
        String[] array = getClass().getName().split("\\.");
        toString += array.length > 0 ? array[array.length - 1] : "Data =";
        toString += " [id = " + id + ", nodeFrom = " + nodeFrom + ", nodeTo = " + nodeTo + "]";
        return toString;
    }

}
