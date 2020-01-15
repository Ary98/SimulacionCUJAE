package sim.app.trafficsimgeo.model.dao;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKBReader;
import sim.app.trafficsimgeo.logic.controller.Config;
import sim.app.trafficsimgeo.model.datasource.DataSource;
import sim.app.trafficsimgeo.model.datasource.SQLiteDataSourceImp;
import sim.app.trafficsimgeo.model.entity.Node;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class NodeDAOImp1 implements NodeDAO {
    private DataSource mDataSource;

    private static final String TABLE_NAME = "roads_nodes";
    private static final String NODE_ID_COLUMN = "node_id";
    private static final String CARDINALITY_COLUMN = "cardinality";
    private static final String GEOMETRY_COLUMN = "geometry";
    private static final String GEOMETRY_AS_WKB = "wkb";
    private static final String TEMPLATE_QUERY = "SELECT " +
            NODE_ID_COLUMN + ", " +
            CARDINALITY_COLUMN + ", " +
            "AsBinary(" + TABLE_NAME + "." + GEOMETRY_COLUMN + ") AS '" + GEOMETRY_AS_WKB + "' ";

    public NodeDAOImp1() {
//        mDataSource = new SQLiteDataSourceImp("124y51.sqlite");
        mDataSource = new SQLiteDataSourceImp(Config.pathOfDbSpatiaLite);
    }

    @Override
    public List<Node> findArrivalNodes() throws Exception {
        List<Node> mList = new LinkedList<>();

        String query = TEMPLATE_QUERY +
                "FROM " + TABLE_NAME + " " +
                "INNER JOIN " + EdgeDAOImp1.TABLE_NAME + " " +
                "ON (" + NODE_ID_COLUMN + " " +
                "= " + EdgeDAOImp1.NODE_FROM_COLUMN + " " +
                "OR " + NODE_ID_COLUMN + " " +
                "= " + EdgeDAOImp1.NODE_TO_COLUMN + " ) " +
                "WHERE " + CARDINALITY_COLUMN + " = 1 " +
                "AND ( " +
                "(" + NODE_ID_COLUMN + " = " + EdgeDAOImp1.NODE_FROM_COLUMN + " AND " + EdgeDAOImp1.ONEWAY_FROMTO_COLUMN + " = 1) " +
                "OR " +
                "(" + NODE_ID_COLUMN + " = " + EdgeDAOImp1.NODE_TO_COLUMN + " AND " + EdgeDAOImp1.ONEWAY_TOFROM_COLUMN + " = 1) " +
                ")";

        mDataSource.connect();

        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);

        while (resultSet.next()) {
            mList.add(map(resultSet));
        }
        resultSet.getStatement().close();
        mDataSource.disconnect();

        return mList;
    }

    @Override
    public List<Node> findTerminalNodes() throws Exception {
        List<Node> mList = new LinkedList<>();

        String query = TEMPLATE_QUERY +
                "FROM " + TABLE_NAME + " " +
                "INNER JOIN " + EdgeDAOImp1.TABLE_NAME + " " +
                "ON (" + NODE_ID_COLUMN + " " +
                "= " + EdgeDAOImp1.NODE_FROM_COLUMN + " " +
                "OR " + NODE_ID_COLUMN + " " +
                "= " + EdgeDAOImp1.NODE_TO_COLUMN + " ) " +
                "WHERE " + CARDINALITY_COLUMN + " = 1 " +
                "AND ( " +
                "(" + NODE_ID_COLUMN + " = " + EdgeDAOImp1.NODE_FROM_COLUMN + " AND " + EdgeDAOImp1.ONEWAY_TOFROM_COLUMN + " = 1) " +
                "OR " +
                "(" + NODE_ID_COLUMN + " = " + EdgeDAOImp1.NODE_TO_COLUMN + " AND " + EdgeDAOImp1.ONEWAY_FROMTO_COLUMN + " = 1) " +
                ")";

        mDataSource.connect();

        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);

        while (resultSet.next()) {
            mList.add(map(resultSet));
        }
        resultSet.getStatement().close();
        mDataSource.disconnect();

        return mList;
    }

    @Override
    public boolean setCardinality(Node nodeUpdate) {
        boolean result = true;
        String query = "UPDATE " + TABLE_NAME + " \n" +
                "SET " + CARDINALITY_COLUMN + " = " + nodeUpdate.getCardinality() + " \n" +
                "WHERE " + NODE_ID_COLUMN + " = " + nodeUpdate.getId() + " ;";
        try {
            mDataSource.connect();
            mDataSource.executeUpdate(query);
            mDataSource.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @Override
    public boolean removeIsolatedNodes() {
        boolean result = true;
        String query = "DELETE FROM " + TABLE_NAME + " \n" +
                "WHERE " + CARDINALITY_COLUMN + " = 0 ;";
        try {
            mDataSource.connect();
            mDataSource.executeUpdate(query);
            mDataSource.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @Override
    public Node findById(int id) throws Exception {
        Node mNode = null;

        String query = TEMPLATE_QUERY +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + NODE_ID_COLUMN + " = " + id;

        mDataSource.connect();

        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);

        if (resultSet.next()) mNode = map(resultSet);

        resultSet.getStatement().close();
        mDataSource.disconnect();

        return mNode;
    }

    @Override
    public List<Node> findAll() throws Exception {
        List<Node> mList = new LinkedList<>();
        String query = TEMPLATE_QUERY +
                "FROM " + TABLE_NAME;

        mDataSource.connect();
        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
        while (resultSet.next()) {
            mList.add(map(resultSet));
        }
        resultSet.close();
        mDataSource.disconnect();
        return mList;
    }

    private Node map(Object result) throws Exception {
        Node mNode = new Node();
        if (result != null && result instanceof ResultSet) {
            ResultSet resultSet = (ResultSet) result;
            WKBReader wkbReader = new WKBReader(new GeometryFactory(new PrecisionModel(), 4326));
            Point geometry = (Point) wkbReader.read(resultSet.getBytes(resultSet.findColumn(GEOMETRY_AS_WKB)));
            mNode.setId(resultSet.getInt(NODE_ID_COLUMN));
            mNode.setCardinality(resultSet.getInt(CARDINALITY_COLUMN));
            mNode.setGeometry(geometry);
        } else
            throw new Exception("Result is null or is not instance of java.sql.ResultSet");
        return mNode;
    }

    @Override
    public List<Node> findByRandom(int count) throws Exception {
        List<Node> mList = new LinkedList<>();
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
}
