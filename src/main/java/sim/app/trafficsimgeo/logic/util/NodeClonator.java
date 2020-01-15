package sim.app.trafficsimgeo.logic.util;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import sim.app.trafficsimgeo.model.entity.Node;

public class NodeClonator {

    static Node cloneNode(Node originalNode) {
        Node cloneNode = originalNode;
        if (originalNode != null) {
            cloneNode = new Node();
            cloneNode.setId(originalNode.getId());
            cloneNode.setCardinality(originalNode.getCardinality());

            CoordinateSequence cloneCoordinateSequence =
                    (CoordinateSequence) originalNode.getGeometry().getCoordinateSequence().clone();
            Point cloneGeometry =
                    new Point(cloneCoordinateSequence, new GeometryFactory(new PrecisionModel(), 4326));
            cloneNode.setGeometry(cloneGeometry);

        }
        return cloneNode;
    }

}
