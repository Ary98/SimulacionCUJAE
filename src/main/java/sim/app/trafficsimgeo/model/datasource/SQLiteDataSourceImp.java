package sim.app.trafficsimgeo.model.datasource;

import org.sqlite.SQLiteConfig;

import java.sql.*;

public class SQLiteDataSourceImp implements DataSource {

    private String DATABASE_URL;
    private Connection connection;
    private Statement statement;

    public SQLiteDataSourceImp(String DATABASE_URL) {
        this.DATABASE_URL = DATABASE_URL;
    }

    @Override
    public void connect() throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");

        // Activate the dynamic load of extensions, totally required by SpatiaLite.
        SQLiteConfig config = new SQLiteConfig();
        config.enableLoadExtension(true);


        // Create the connection to the database.
        connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_URL, config.toProperties());

        // Upload SpatiaLite
//        statement = connection.createStatement();
//        String executeQuery = "SELECT load_extension('mod_spatialite')";
//        statement.execute(executeQuery);
//        statement.close();

        executeQuery("SELECT load_extension('mod_spatialite')");
    }

    @Override
    public void disconnect() throws SQLException {
        connection.close();
    }

    @Override
    public Object executeQuery(Object args) throws SQLException {
        Object resultQuery;
        statement = connection.createStatement();
        resultQuery =  statement.executeQuery(args.toString());
        return resultQuery;

    }

    @Override
    public void executeUpdate(Object args) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate(args.toString());
    }

}