
SELECT load_extension('G:\workspace\DayanMaven\mod_spatialite.dll');

--/*nodos inicio*/
--SELECT roads_nodes.node_id, AsBinary(roads_nodes.geometry) AS 'binary_geometry'
--FROM roads_nodes
--INNER JOIN roads
--ON (roads_nodes.node_id = roads.node_from OR roads_nodes.node_id = roads.node_to)
--WHERE roads_nodes.cardinality = 1 AND (
--(roads_nodes.node_id = roads.node_from AND roads.oneway_fromto = 1)OR(roads_nodes.node_id = roads.node_to AND roads.oneway_tofrom = 1)
--);
--
--/*arco de un nodo inicio y direccion*/
--SELECT id, node_from, node_to, AsBinary(geometry) AS 'binary_geometry', oneway_fromto =  AS 'same_direction'
--FROM roads
--WHERE roads.node_from = ? OR roads.node_to = ? ;
--
--/*todos los arcos*/
SELECT id, node_from, node_to
--,AsBinary(geometry) AS 'binary_geometry'
FROM roads ;
