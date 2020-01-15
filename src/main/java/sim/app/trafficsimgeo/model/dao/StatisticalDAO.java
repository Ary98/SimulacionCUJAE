package sim.app.trafficsimgeo.model.dao;

import sim.app.trafficsimgeo.model.entity.Statistical;

public interface StatisticalDAO extends DAO<Statistical> {

    void create(Statistical statistical) throws Exception;

    void removeAll() throws Exception;
}
