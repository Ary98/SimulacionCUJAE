package sim.app.trafficsimgeo.view;

import sim.app.trafficsimgeo.model.entity.Edge;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.util.geo.MasonGeometry;

import java.awt.*;

public class StreetsLabelPortrayal extends LabelledPortrayal2D {

    private static final long serialVersionUID = 1L;

    public StreetsLabelPortrayal(SimplePortrayal2D child, Paint paint) {
        super(child, null, paint, true);
    }

    @Override
    public String getLabel(Object object, DrawInfo2D info) {
        String label = "No name";
        if (object instanceof MasonGeometry) {
            MasonGeometry mg = (MasonGeometry) object;
            Object userData = mg.getUserData();
            if (userData instanceof Edge) {
                Edge edge = (Edge) userData;
                label = edge.getName();
            }
        }
        return label;
    }

}
