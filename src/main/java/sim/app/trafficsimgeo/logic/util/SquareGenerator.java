package sim.app.trafficsimgeo.logic.util;

import com.vividsolutions.jts.geom.*;

public class SquareGenerator {

    // Meters by coordinates
    private static final double MBC = 0.0001;

    // create square by the center point
    static Polygon createSquare(Point center, double side) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        double pivot = side / 2 * MBC;
        Coordinate centerCoordinate = center.getCoordinate();
        Coordinate[] coordinatedPolygon = new Coordinate[5];
        coordinatedPolygon[0] = new Coordinate(centerCoordinate.x + pivot, centerCoordinate.y + pivot);
        coordinatedPolygon[1] = new Coordinate(centerCoordinate.x + pivot, centerCoordinate.y - pivot);
        coordinatedPolygon[2] = new Coordinate(centerCoordinate.x - pivot, centerCoordinate.y - pivot);
        coordinatedPolygon[3] = new Coordinate(centerCoordinate.x - pivot, centerCoordinate.y + pivot);
        coordinatedPolygon[4] = coordinatedPolygon[0];
        return geometryFactory.createPolygon(coordinatedPolygon);
    }

}
