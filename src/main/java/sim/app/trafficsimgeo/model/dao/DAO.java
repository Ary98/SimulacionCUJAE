package sim.app.trafficsimgeo.model.dao;

import java.util.List;

public interface DAO<Entity> {

    Entity findById(int id) throws Exception;

    List<Entity> findAll() throws Exception;

    List<Entity> findByRandom(int count) throws Exception;


}
