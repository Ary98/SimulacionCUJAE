package sim.app.trafficsimgeo.model.dao;

import sim.app.trafficsimgeo.logic.agent.Route;
import sim.app.trafficsimgeo.model.entity.Edge;
import sim.app.trafficsimgeo.model.entity.Node;

import java.util.List;

public interface EdgeDAO extends DAO<Edge> {
    //    find the arcs (Edge) adjacent to the node (Node)
    List<Edge> findAdjacentToTheNode(Node node) throws Exception;

    Edge findRandomOutputEdge(Node node, Edge distinct) throws Exception;

    List<Edge> findAllOutputEdge(Node node, Edge distinct) throws Exception;

    List<Edge> findAllInputEdge(Node node) throws Exception;

    int cardinalityOfTheNode(Node node) throws Exception;

    Route getDijkstraRoute (Node arrival, Node destiny) throws Exception;

}
