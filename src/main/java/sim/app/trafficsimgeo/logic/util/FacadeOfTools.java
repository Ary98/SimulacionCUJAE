package sim.app.trafficsimgeo.logic.util;

import com.vividsolutions.jts.geom.*;
import sim.app.trafficsimgeo.model.entity.Node;

public abstract class FacadeOfTools {

    public static Polygon createSquare(Point center, double side) {
        return SquareGenerator.createSquare(center, side);
    }

    public static Node cloneNode(Node originalNode) {
        return NodeClonator.cloneNode(originalNode);
    }

    public static double convertFromMtoIndex(double m) {
        return UnitConverter.convertFromMtoIndex(m);
    }

    public static double convertFromStoSystemTime(double s) {
        return UnitConverter.convertFromStoSystemTime(s);
    }

    public static double convertFromSystemTimeToS(double systemTime) {
        return UnitConverter.convertFromSystemTimeToS(systemTime);
    }

    public static double convertFromKMxHtoIndexXSystemTime(double kmXh) {
        return UnitConverter.convertFromKMxHtoIndexXSystemTime(kmXh);
    }

    public static void waiting(int seconds) {
        TimeManager.waiting(seconds);
    }


    public static boolean rectifyCardinality() {
        boolean ok = true;
        try {
            DataRectifier.rectifyCardinality();
        } catch (Exception e) {
            ok = false;
            e.printStackTrace();
        }
        return ok;
    }

}
