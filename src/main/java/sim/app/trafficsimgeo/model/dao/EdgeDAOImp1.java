package sim.app.trafficsimgeo.model.dao;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKBReader;
import org.sqlite.SQLiteException;
import sim.app.trafficsimgeo.logic.agent.Route;
import sim.app.trafficsimgeo.logic.controller.Config;
import sim.app.trafficsimgeo.model.datasource.DataSource;
import sim.app.trafficsimgeo.model.datasource.SQLiteDataSourceImp;
import sim.app.trafficsimgeo.model.entity.Edge;
import sim.app.trafficsimgeo.model.entity.Node;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class EdgeDAOImp1 implements EdgeDAO {
    private DataSource mDataSource;

    public static final String TABLE_NAME = "roads";
    private static final String ID_COLUMN = "id";
    public static final String NODE_FROM_COLUMN = "node_from";
    public static final String NODE_TO_COLUMN = "node_to";
    private static final String GEOMETRY_COLUMN = "geometry";
    private static final String GEOMETRY_AS_WKB = "wkb";
    public static final String ONEWAY_FROMTO_COLUMN = "oneway_fromto";
    public static final String ONEWAY_TOFROM_COLUMN = "oneway_tofrom";
    private static final String NAME_COLUMN = "name";
    //from net
    private static final String ID_NET_COLUMN = "ArcRowid";
    public static final String NODE_FROM_NET_COLUMN = "NodeFrom";
    public static final String NODE_TO_NET_COLUMN = "NodeTo";

    private static final String TEMPLATE_QUERY = "SELECT " +
            ID_COLUMN + ", " +
            NODE_FROM_COLUMN + ", " +
            NODE_TO_COLUMN + ", " +
            ONEWAY_FROMTO_COLUMN + ", " +
            ONEWAY_TOFROM_COLUMN + ", " +
            NAME_COLUMN + ", " +
            "AsBinary(" + GEOMETRY_COLUMN + ") AS '" + GEOMETRY_AS_WKB + "' ";


    public EdgeDAOImp1() {
//        mDataSource = new SQLiteDataSourceImp("124y51.sqlite");
        mDataSource = new SQLiteDataSourceImp(Config.pathOfDbSpatiaLite);
    }

    @Override
    public Edge findById(int id) throws Exception {
        return new EdgeDAOImp2().findById(id);
    }

    /*public List<Edge> findByLane() throws Exception {
        List<Edge> mList = new LinkedList<>();

        String query = TEMPLATE_QUERY +
                "FROM " + TABLE_NAME +
                "WHERE other_tags -> 'lanes' >= '2'";

        mDataSource.connect();
        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
        while (resultSet.next()) {
            mList.add(map(resultSet));
        }
        resultSet.getStatement().close();
//        resultSet.close();
        mDataSource.disconnect();
        return mList;
    }*/

    @Override
    public List<Edge> findAll() throws Exception {

        List<Edge> mList = new LinkedList<>();

        String query = TEMPLATE_QUERY +
                "FROM " + TABLE_NAME;

        mDataSource.connect();
        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
        while (resultSet.next()) {
            mList.add(map(resultSet));
        }
        resultSet.getStatement().close();
//        resultSet.close();
        mDataSource.disconnect();
        return mList;
    }

    private Edge map(Object result) throws Exception {
        Edge mEdge = new Edge();
        if (result != null && result instanceof ResultSet) {
            ResultSet resultSet = (ResultSet) result;
            WKBReader wkbReader = new WKBReader(new GeometryFactory(new PrecisionModel(), 4326));
            LineString geometry = (LineString) wkbReader.read(resultSet.getBytes(resultSet.findColumn(GEOMETRY_AS_WKB)));
            mEdge.setId(resultSet.getInt(ID_COLUMN));

            NodeDAO nodeDao = new NodeDAOImp1();

            mEdge.setOnewayFromTo(resultSet.getBoolean(ONEWAY_FROMTO_COLUMN));
            mEdge.setOnewayToFrom(resultSet.getBoolean(ONEWAY_TOFROM_COLUMN));

            mEdge.setNodeFrom(nodeDao.findById(resultSet.getInt(NODE_FROM_COLUMN)));
            mEdge.setNodeTo(nodeDao.findById(resultSet.getInt(NODE_TO_COLUMN)));

            mEdge.setName(resultSet.getString(NAME_COLUMN));

            mEdge.setGeometry(geometry);
        } else throw new Exception("Result is null or is not instance of java.sql.ResultSet");

        return mEdge;
    }

    @Override
    public List<Edge> findByRandom(int count) throws Exception {
        List<Edge> mList = new LinkedList<>();
        String query = TEMPLATE_QUERY +
                "FROM " + TABLE_NAME + " " +
                "ORDER BY RANDOM() " +
                "LIMIT " + count;

        mDataSource.connect();
        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
        while (resultSet.next()) {
            mList.add(map(resultSet));
        }
        resultSet.close();
        mDataSource.disconnect();
        return mList;
    }

    @Override
    public List<Edge> findAdjacentToTheNode(Node node) throws Exception {
        List<Edge> mList = new LinkedList<>();

        String query = TEMPLATE_QUERY +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + NODE_FROM_COLUMN + " = " + node.getId() + " " +
                "OR " + NODE_TO_COLUMN + " = " + node.getId();

        mDataSource.connect();
        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
        while (resultSet.next()) {
            mList.add(map(resultSet));
        }
        resultSet.close();
        mDataSource.disconnect();
        return mList;
    }

    @Override
    public Edge findRandomOutputEdge(Node node, Edge distinct) throws Exception {
        Edge mEdge = null;
        String query = TEMPLATE_QUERY +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + ID_COLUMN + " != " + distinct.getId() + " " +
                "AND (" +
                "(" + NODE_FROM_COLUMN + " = " + node.getId() + " AND " + ONEWAY_FROMTO_COLUMN + " = 1) " +
                "OR (" + NODE_TO_COLUMN + " = " + node.getId() + " AND " + ONEWAY_TOFROM_COLUMN + " = 1) " +
                ")" +
                "ORDER BY RANDOM() " +
                "LIMIT 1";


        mDataSource.connect();
        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
        if (resultSet.next()) {
            mEdge = map(resultSet);
        }
        resultSet.getStatement().close();
        mDataSource.disconnect();
        return mEdge;
    }

    @Override
    public List<Edge> findAllOutputEdge(Node node, Edge distinct) throws Exception {
        List<Edge> mList = new LinkedList<>();

        String query = TEMPLATE_QUERY +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + ID_COLUMN + " != " + distinct.getId() + " " +
                "AND (" +
                "(" + NODE_FROM_COLUMN + " = " + node.getId() + " AND " + ONEWAY_FROMTO_COLUMN + " = 1) " +
                "OR (" + NODE_TO_COLUMN + " = " + node.getId() + " AND " + ONEWAY_TOFROM_COLUMN + " = 1) " +
                ")";

        mDataSource.connect();
        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
        while (resultSet.next()) {
            mList.add(map(resultSet));
        }
        resultSet.close();
        mDataSource.disconnect();
        return mList;
    }

    @Override
    public List<Edge> findAllInputEdge(Node node) throws Exception {
        List<Edge> mList = new LinkedList<>();

        String query = TEMPLATE_QUERY +
                "FROM " + TABLE_NAME + " " +
                "WHERE " +
                "(" + NODE_FROM_COLUMN + " = " + node.getId() + " AND " + ONEWAY_TOFROM_COLUMN + " = 1) " +
                "OR (" + NODE_TO_COLUMN + " = " + node.getId() + " AND " + ONEWAY_FROMTO_COLUMN + " = 1) ";

        mDataSource.connect();
        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
        while (resultSet.next()) {
            mList.add(map(resultSet));
        }
        resultSet.close();
        mDataSource.disconnect();
        return mList;
    }

    @Override
    public int cardinalityOfTheNode(Node node) throws Exception {
        int cardinality = -1;
        int id = node.getId();
        String query = "SELECT count(" + ID_COLUMN + ") AS 'cardinality' \n" +
                "FROM " + TABLE_NAME + " \n" +
                "WHERE " + NODE_FROM_COLUMN + " = " + id + " OR " + NODE_TO_COLUMN + " = " + id + " ;";
        mDataSource.connect();
        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
        if (resultSet.next())
            cardinality = resultSet.getInt("cardinality");
        resultSet.close();
        mDataSource.disconnect();
        return cardinality;
    }

    @Override
    public Route getDijkstraRoute(Node arrival, Node destiny) throws Exception {
        Route mRoutesList = new Route();
        String query = "SELECT " + ID_NET_COLUMN + " \n" +
                "FROM " + TABLE_NAME + "_net \n" +
                "WHERE " + NODE_FROM_NET_COLUMN + " = " + arrival.getId() + " \n" +
                "AND " + NODE_TO_NET_COLUMN + " = " + destiny.getId() + " \n" +
                "AND " + ID_NET_COLUMN + " NOT NULL ;";
        try {
            mDataSource.connect();
            ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
            while (resultSet.next()) {
                int edgeId = resultSet.getInt(ID_NET_COLUMN);
                mRoutesList.add(this.findById(edgeId));
            }
            resultSet.close();
            mDataSource.disconnect();
        } catch (SQLiteException e) {
//            e.printStackTrace();
        }
        return mRoutesList.isEmpty() ? null : mRoutesList;
    }
}
