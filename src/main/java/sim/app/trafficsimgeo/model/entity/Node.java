package sim.app.trafficsimgeo.model.entity;

import com.vividsolutions.jts.geom.Point;

public class Node extends PartOfGraph {
//    private int id;
    private int cardinality;
//    private Point geometry;

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public int getCardinality() {
        return cardinality;
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }

    @Override
    public Point getGeometry() {
        return (Point) geometry;
    }

//    public void setGeometry(Point geometry) {
//        this.geometry = geometry;
//    }

    public Node() {
    }

    @Override
    public String toString() {
        String toString = "";
        String[] array = getClass().getName().split("\\.");
        toString += array.length > 0 ? array[array.length - 1] : "Data =";
        toString += " [id = " + id + ", cardinality = " + cardinality + "]";
        return toString;
    }
}
