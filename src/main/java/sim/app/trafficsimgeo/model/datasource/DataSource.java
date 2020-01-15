package sim.app.trafficsimgeo.model.datasource;

public interface DataSource {

    void connect() throws Exception;

    void disconnect() throws Exception;

    Object executeQuery(Object args) throws Exception;

    void executeUpdate(Object args) throws Exception;
}
