package sim.app.trafficsimgeo.model.entity;

import com.vividsolutions.jts.geom.Geometry;

public abstract class PartOfGraph {
    protected int id;
    protected Geometry geometry;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract Geometry getGeometry();

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
