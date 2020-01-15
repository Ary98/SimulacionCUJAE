package sim.app.trafficsimgeo.logic.util.garbage;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import sim.util.geo.MasonGeometry;

public class TestPolygon {

    private void test1() {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Polygon polygon = geometryFactory.createPolygon(
                new Coordinate[]{
                        new Coordinate(-82.431000, 23.073200),
                        new Coordinate(-82.431000, 23.073100),
                        new Coordinate(-82.430900, 23.073100),
                        new Coordinate(-82.430900, 23.073200),
                        new Coordinate(-82.431000, 23.073200)
                });

        System.out.println("Area: " + (float) polygon.getArea());
        System.out.println("Length: " + (float) polygon.getLength());
        MasonGeometry test = new MasonGeometry(polygon);
    }

    public static void main(String[] args) {
        String a = "nombre:09 Ruben";
        System.out.println(a);

        String[] b = a.split(":09 ");
        for (String s : b) {
            System.out.println(s);
        }
    }

}
