package sim.app.trafficsimgeo.logic.agent;

import sim.app.trafficsimgeo.logic.controller.TrafficSimGeo;
import sim.app.trafficsimgeo.model.entity.Edge;
import sim.app.trafficsimgeo.model.entity.Node;
import sim.engine.SimState;

import java.util.List;

public class AgDijkstraVehicle extends AgVehicle {

    public AgDijkstraVehicle(SimState state, Node startNode, Route route) throws Exception {
        super(state, startNode);
        this.route = route;
        routeIndex = 1;
        findNewPath((TrafficSimGeo) state);
    }

    private Route route;
    private int routeIndex;

    /**
     * randomly selects an adjacent route to traverse
     *
     * @param trafficState
     */
    @Override
    protected void findNewPath(TrafficSimGeo trafficState) {
        if (route != null) {
            if (routeIndex < route.size()) {
                Node node = moveRate > 0 ? currentEdge.getNodeTo() : currentEdge.getNodeFrom();
                nextEdge = route.get(routeIndex++);
                nextDirection = nextEdge.getNodeFrom().getId() == node.getId();
            } else
                nextEdge = null;
            mySemaphore = trafficState.findSemaphore(getMyFinalPosition());
        }
    }

}
