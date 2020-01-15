package sim.app.trafficsimgeo.model.dao;

import sim.app.trafficsimgeo.model.entity.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NodeDAOImp2 implements NodeDAO {

    private static List<Node> nodesCache;
    private static Random r;

    public NodeDAOImp2() throws Exception {
        if (nodesCache == null) {
            r = new Random();
            NodeDAO nodeDAO = new NodeDAOImp1();
            nodesCache = nodeDAO.findAll();
        }
    }

    @Override
    public List<Node> findArrivalNodes() throws Exception {
        return new NodeDAOImp1().findArrivalNodes();
    }

    @Override
    public List<Node> findTerminalNodes() throws Exception {
        return new NodeDAOImp1().findTerminalNodes();
    }

    @Override
    public boolean setCardinality(Node nodeUpdate) {
        return false;
    }

    @Override
    public boolean removeIsolatedNodes() {
        return false;
    }

    @Override
    public Node findById(int id) {
        Node mNode = null;
        for (Node node : nodesCache) {
            boolean condition = node.getId() == id;
            if (condition){
                mNode = node;
                break;
            }
        }
        return mNode;
    }

    @Override
    public List<Node> findAll() {
        return new LinkedList<>(nodesCache);
    }

    @Override
    public List<Node> findByRandom(int count) {
        List<Node> mList = new LinkedList<>();
        List<Node> mFullList = findAll();
        for (int i = 0; i < count; i++)
            mList.add(mFullList.remove(r.nextInt(mFullList.size())));
        return mList;
    }

}
