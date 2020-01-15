package sim.app.trafficsimgeo.logic.agent;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import sim.app.trafficsimgeo.logic.controller.Config;
import sim.app.trafficsimgeo.logic.controller.TrafficSimGeo;
import sim.app.trafficsimgeo.logic.util.FacadeOfTools;
import sim.app.trafficsimgeo.model.entity.Node;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.geo.MasonGeometry;

public class AgArrival implements Steppable {

    protected Node locationNode;
    protected int arriveAmount;
    protected int timeBetweenArrival;
    protected double lastVehicleArrivalTime;
    protected MasonGeometry masonGeometry;
    // Square side in meters
    protected static final double SIDE = 1;

    @Override
    public void step(SimState simState) {
        TrafficSimGeo trafficSimGeo = (TrafficSimGeo) simState;
        double nowTime = simState.schedule.getTime();

        if (nowTime - lastVehicleArrivalTime >= timeBetweenArrival) {
            try {
                for (int i = 0; i < arriveAmount; i++) {
                    if (!createVehicle(trafficSimGeo, locationNode))
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            lastVehicleArrivalTime = nowTime;
        }

    }

    protected boolean createVehicle(TrafficSimGeo trafficSimGeo, Node locationNode) throws Exception {
        return trafficSimGeo.createVehicle(locationNode);
    }

    public AgArrival(Node locationNode) {
        this.locationNode = locationNode;
        this.arriveAmount = Config.arriveAmount;
        this.timeBetweenArrival = Config.timeBetweenArrival;
        this.lastVehicleArrivalTime = 0;
    }

    public int getArriveAmount() {
        return arriveAmount;
    }

    public void setArriveAmount(int arriveAmount) {
        if (arriveAmount >= 0)
            this.arriveAmount = arriveAmount;
    }

    public int getTimeBetweenArrival() {
        return timeBetweenArrival;
    }

    public void setTimeBetweenArrival(int timeBetweenArrival) {
        if (timeBetweenArrival > 0)
            this.timeBetweenArrival = timeBetweenArrival;
    }

    public Geometry getGeometry() {
        return locationNode.getGeometry();
    }

    public MasonGeometry getMasonGeometry() {
        if (masonGeometry == null) {
            Polygon square = FacadeOfTools.createSquare(locationNode.getGeometry(), SIDE);
            masonGeometry = new MasonGeometry(square);
            masonGeometry.setUserData(this);
        }
        return masonGeometry;
    }

    @Override
    public String toString() {
        String toString = getClass().getName() + " [node = " + locationNode.getId() + "]";
        return toString;
    }
}
