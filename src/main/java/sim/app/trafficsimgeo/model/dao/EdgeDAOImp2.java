package sim.app.trafficsimgeo.model.dao;

import sim.app.trafficsimgeo.logic.agent.Route;
import sim.app.trafficsimgeo.model.entity.Edge;
import sim.app.trafficsimgeo.model.entity.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class EdgeDAOImp2 implements EdgeDAO {

    private static List<Edge> edgesCache;
    private static Random r;

    public EdgeDAOImp2() throws Exception {
        // TODO: 26/03/2018 aki uso la imp 1
        if (edgesCache == null) {
            r = new Random();
            EdgeDAO edgeDAO = new EdgeDAOImp1();
            edgesCache = edgeDAO.findAll();
        }
    }

    @Override
    public Edge findRandomOutputEdge(Node node, Edge distinct) {
        Edge mEdge = null;
        List<Edge> mList = new LinkedList<>();
        for (Edge edge : edgesCache) {
            boolean condition = (edge.getId() != distinct.getId()) && (
                    (edge.getNodeFrom().getId() == node.getId() && edge.isOnewayFromTo())
                            || (edge.getNodeTo().getId() == node.getId() && edge.isOnewayToFrom())
            );
            if (condition)
                mList.add(edge);
        }
        if (!mList.isEmpty())
            mEdge = mList.get(r.nextInt(mList.size()));
        return mEdge;
    }

//    @Override
//    public void create(Edge edge) throws Exception {
//
//    }
//
//    @Override
//    public void delete(Edge edge) throws Exception {
//
//    }
//
//    @Override
//    public void update(Edge edge) throws Exception {
//
//    }

//    @Override
//    public Edge findById(int id) {
//        Edge edge = null;
//        int i = 0;
//        do {
//            if (edgesCache.get(i).getId() == id)
//                edge = edgesCache.get(i);
//        } while (edge == null && ++i < edgesCache.size());
//        return edge;
//    }

    @Override
    public Edge findById(int id) {
        Edge result = null;
        for (Edge edge : edgesCache)
            if (edge.getId() == id) {
                result = edge;
                break;
            }
        return result;
    }

    @Override
    public List<Edge> findAll() {
        return new LinkedList<>(edgesCache);
    }

    public List<Edge> findByRandom(int count) {
        List<Edge> mList = new LinkedList<>();
        List<Edge> mFullList = findAll();
        for (int i = 0; i < count; i++)
            mList.add(mFullList.remove(r.nextInt(mFullList.size())));
        return mList;
    }

    @Override
    public List<Edge> findAdjacentToTheNode(Node node) {
        List<Edge> mList = new LinkedList<>();
        for (Edge edge : edgesCache) {
            boolean condition = edge.getNodeFrom().getId() == node.getId()
                    || edge.getNodeTo().getId() == node.getId();
            if (condition)
                mList.add(edge);
        }
        return mList;
    }

    @Override
    public List<Edge> findAllOutputEdge(Node node, Edge distinct) {
        List<Edge> mList = new LinkedList<>();
        for (Edge edge : edgesCache) {
            boolean condition = edge.getId() != distinct.getId()
                    && (
                    (edge.getNodeFrom().getId() == node.getId() && edge.isOnewayFromTo())
                            || (edge.getNodeTo().getId() == node.getId() && edge.isOnewayToFrom())
            );
            if (condition)
                mList.add(edge);
        }
        return mList;
    }

    @Override
    public List<Edge> findAllInputEdge(Node node) throws Exception {
        return new EdgeDAOImp1().findAllInputEdge(node);
    }

    @Override
    public int cardinalityOfTheNode(Node node) throws Exception {
        return new EdgeDAOImp1().cardinalityOfTheNode(node);
    }

    @Override
    public Route getDijkstraRoute(Node arrival, Node destiny) throws Exception {
        return new EdgeDAOImp1().getDijkstraRoute(arrival, destiny);
    }
}
