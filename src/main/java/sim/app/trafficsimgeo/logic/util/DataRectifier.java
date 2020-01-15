package sim.app.trafficsimgeo.logic.util;

import sim.app.trafficsimgeo.model.dao.EdgeDAO;
import sim.app.trafficsimgeo.model.dao.EdgeDAOImp1;
import sim.app.trafficsimgeo.model.dao.NodeDAO;
import sim.app.trafficsimgeo.model.dao.NodeDAOImp1;
import sim.app.trafficsimgeo.model.entity.Node;

import java.util.List;

public class DataRectifier {

    static void rectifyCardinality() throws Exception {
        NodeDAO nodeDAO = new NodeDAOImp1();
        EdgeDAO edgeDAO = new EdgeDAOImp1();
        List<Node> nodeList = nodeDAO.findAll();
        for (Node node : nodeList) {
            int realCardinality = edgeDAO.cardinalityOfTheNode(node);
            if (realCardinality != node.getCardinality()) {
                node.setCardinality(realCardinality);
                nodeDAO.setCardinality(node);
            }
        }
        nodeDAO.removeIsolatedNodes();
    }
}
