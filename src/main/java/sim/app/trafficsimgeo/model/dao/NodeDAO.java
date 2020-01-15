package sim.app.trafficsimgeo.model.dao;

import sim.app.trafficsimgeo.model.entity.Node;

import java.util.List;

public interface NodeDAO extends DAO<Node> {

    List<Node> findArrivalNodes() throws Exception;

    List<Node> findTerminalNodes() throws Exception;

    boolean setCardinality(Node nodeUpdate);

    boolean removeIsolatedNodes();
}
