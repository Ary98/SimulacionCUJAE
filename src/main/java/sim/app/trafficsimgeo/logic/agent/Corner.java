package sim.app.trafficsimgeo.logic.agent;

import sim.app.trafficsimgeo.logic.controller.TrafficSimGeo;
import sim.app.trafficsimgeo.model.entity.Node;

public class Corner {

    private TrafficSimGeo trafficSimGeo;
    private Node locationNode;
    private AgSemaphore semaphore;
    int signal;

    public Corner(TrafficSimGeo trafficSimGeo, Node locationNode) {
        this.trafficSimGeo = trafficSimGeo;
        this.locationNode = locationNode;
        this.semaphore = null;
    }

    public boolean isSignalized() {
        return semaphore != null;
    }

    public void setSignalized(boolean signalized) {
        if (signalized) {
            try {
                semaphore = trafficSimGeo.createSemaphore(locationNode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            semaphore.stop(trafficSimGeo);
            semaphore = null;
        }
    }

    public Node getLocationNode() {
        return locationNode;
    }
}