package sim.app.trafficsimgeo.logic.agent;

import sim.app.trafficsimgeo.logic.controller.TrafficSimGeo;
import sim.app.trafficsimgeo.model.dao.EdgeDAO;
import sim.app.trafficsimgeo.model.dao.EdgeDAOImp1;
import sim.app.trafficsimgeo.model.dao.NodeDAO;
import sim.app.trafficsimgeo.model.dao.NodeDAOImp1;
import sim.app.trafficsimgeo.model.entity.Edge;
import sim.app.trafficsimgeo.model.entity.Node;

import java.util.LinkedList;
import java.util.List;

public class AgDijkstraArrival extends AgArrival {

    public AgDijkstraArrival(Node locationNode, List<Route> routes) {
        super(locationNode);
        this.routes = routes;
    }

    private List<Route> routes;

    @Override
    protected boolean createVehicle(TrafficSimGeo trafficSimGeo, Node locationNode) throws Exception {
        Route route = routes.get(trafficSimGeo.random.nextInt(routes.size()));
        return trafficSimGeo.createDijkstraVehicle(locationNode, route);
    }


}
