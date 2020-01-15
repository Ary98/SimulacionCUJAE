package sim.app.trafficsimgeo.logic.controller;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.linearref.LengthIndexedLine;
import sim.app.trafficsimgeo.logic.agent.*;
import sim.app.trafficsimgeo.logic.util.FacadeOfTools;
import sim.app.trafficsimgeo.model.dao.*;
import sim.app.trafficsimgeo.model.entity.Edge;
import sim.app.trafficsimgeo.model.entity.Node;
import sim.app.trafficsimgeo.model.entity.Statistical;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.geo.GeomVectorField;
import sim.util.Bag;
import sim.util.geo.MasonGeometry;
import sim.util.geo.PointMoveTo;

import java.util.LinkedList;
import java.util.List;

public class TrafficSimGeo extends SimState {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    //    quantity constants
    // TODO: 12/06/2018  
    public static final int VEHICLE_LIMIT = 50;

    public static final double REASON_STEPS_PER_SYSTEM_TIME = 1;
    //    in meters
    private static final double MINIMUM_DISTANCE_BETWEEN_VEHICLES = 2;
    private static final double DISTANCE_TO_SEMAPHORE = 5;
    //    in seconds
    private static final double MAXIMUM_WAIT = 1000;


    public static double minimumDistanceBetweenVehiclesInIndex() {
        return FacadeOfTools.convertFromMtoIndex(MINIMUM_DISTANCE_BETWEEN_VEHICLES);
    }

    public static double distanceToSemaphoreInIndex() {
        return FacadeOfTools.convertFromMtoIndex(DISTANCE_TO_SEMAPHORE);
    }

    public static double maximumWaitInSystemTime() {
        return FacadeOfTools.convertFromStoSystemTime(MAXIMUM_WAIT);
    }


    private int initialVehicleNumber;
    private double vehicleCollisionProbability;
    private double unwiseVehicleCollisionProbability;
    private double probabilityOfBeingReckless;
    private double probabilityOfPassYellow;
    private double probabilityGetFirst;
    private int averageSpeedOfVehicles;
    //time it takes to disappear vehicles after the accident
    public int accidentTime = 120;
    private StatisticalDAO statisticalDAO;

    private AgStatistical agStatistical;
    private AgAmbient ambiental;

    private GeomVectorField streets;
    private GeomVectorField vehicles;
    private GeomVectorField semaphores;
    private GeomVectorField semaphoreIndicators;
    private GeomVectorField corners;
    private GeomVectorField arrivalNodes;

    //singleton
    private static TrafficSimGeo singletonInstance;

    public static TrafficSimGeo getInstance() throws Exception {
        if (singletonInstance == null)
            singletonInstance = new TrafficSimGeo(System.currentTimeMillis());
        return singletonInstance;
    }

    private TrafficSimGeo(long seed) throws Exception {
        super(seed);
        averageSpeedOfVehicles = Config.averageSpeedOfVehicles;
        initialVehicleNumber = Config.initialVehicleNumber;
        vehicleCollisionProbability = (float) Config.vehicleCollisionProbability;
        unwiseVehicleCollisionProbability = Config.unwiseVehicleCollisionProbability;
        probabilityOfBeingReckless = Config.probabilityOfBeingReckless;
        probabilityOfPassYellow = Config.probabilityPassYellow;
        probabilityGetFirst = Config.vehicleGetFirstProbability;
        streets = new GeomVectorField(WIDTH, HEIGHT);
        vehicles = new GeomVectorField(WIDTH, HEIGHT);
        semaphores = new GeomVectorField(WIDTH, HEIGHT);
        semaphoreIndicators = new GeomVectorField(WIDTH, HEIGHT);
        corners = new GeomVectorField(WIDTH, HEIGHT);
        arrivalNodes = new GeomVectorField(WIDTH, HEIGHT);
        statisticalDAO = new StatisticalDAOImp();
        ambiental = new AgAmbient(this);

        loadGisData();
    }

    @Override
    public void start() {
        super.start();
        vehicles.clear(); // clear any existing agents from previous runs
        semaphores.clear();
        semaphoreIndicators.clear();
        corners.clear();
        arrivalNodes.clear();

        vehicles.setMBR(streets.getMBR());
        semaphores.setMBR(streets.getMBR());
        semaphoreIndicators.setMBR(streets.getMBR());
        corners.setMBR(streets.getMBR());
        arrivalNodes.setMBR(streets.getMBR());

        agStatistical = new AgStatistical();
        schedule.scheduleRepeating(agStatistical);

        ambiental = new AgAmbient(this);
        schedule.scheduleRepeating(ambiental);

        try {
            addVehicleAgents();
            addSemaphoreAgents();
            addArrivalAgents();
            addCorners();
        } catch (Exception e) {
            e.printStackTrace();
        }
        schedule.scheduleRepeating(vehicles.scheduleSpatialIndexUpdater(), Integer.MAX_VALUE, 1.0);
    }

    private void loadGisData() throws Exception {
        System.out.println("Start reading the model");
        EdgeDAO edgeDAO = new EdgeDAOImp2();
        EdgeDAO a = new EdgeDAOImp1();
        //System.out.println(((EdgeDAOImp1) a).findByLane().isEmpty());
        List<Edge> mRoadsList = edgeDAO.findAll();
        for (Edge edge : mRoadsList) {
            LineString geometry = edge.getGeometry();
            MasonGeometry geom = new MasonGeometry(geometry);
            geom.setUserData(edge);
            streets.addGeometry(geom);
        }
        System.out.println("Done");
    }

    private void addCorners() throws Exception {
        NodeDAO nodeDAO = new NodeDAOImp2();
        List<Node> nodeList = nodeDAO.findAll();
        for (Node node : nodeList) {
            if (node.getCardinality() > 2) {
                Geometry geometry = FacadeOfTools.createSquare(node.getGeometry(), 1);
                MasonGeometry masonGeometry = new MasonGeometry(geometry);
                Corner corner = new Corner(this, node);
                masonGeometry.setUserData(corner);
                corners.addGeometry(masonGeometry);
            }
        }
    }

    public GeomVectorField getCorners() {
        return corners;
    }

    private void addVehicleAgents() throws Exception {
        NodeDAO nodeDAO = new NodeDAOImp2();
        List<Node> initialLocationsNodes = nodeDAO.findByRandom(initialVehicleNumber);
        for (Node node : initialLocationsNodes) {
            createVehicle(node);
        }
    }

    public AgSemaphore createSemaphore(Node node) throws Exception {
        AgSemaphore agSemaphore = new AgSemaphore(this, node);
        Stoppable stoppable = schedule.scheduleRepeating(agSemaphore);
        agSemaphore.setStoppable(stoppable);
        semaphores.addGeometry(agSemaphore.getMasonGeometry());
        return agSemaphore;
    }

    private void addSemaphoreAgents() throws Exception {
        System.out.println("Start looking for semaphores nodes");
        for (Corner corner : cornersList())
            if (corner.isSignalized())
                createSemaphore(corner.getLocationNode());
        System.out.println("Done");
    }

    private void addArrivalAgents() throws Exception {
        System.out.println("Start looking for arrival nodes");
        NodeDAO nodeDAO = new NodeDAOImp2();
        LinkedList<Node> mNodeList = (LinkedList<Node>) nodeDAO.findArrivalNodes();
        for (Node node : mNodeList) {
            AgArrival agArrival;
            List<Route> routes = createRoutesList(node);
            if (routes == null || routes.isEmpty())
                agArrival = new AgArrival(node);
            else
                agArrival = new AgDijkstraArrival(node, routes);
            schedule.scheduleRepeating(agArrival);
            arrivalNodes.addGeometry(agArrival.getMasonGeometry());
        }
        System.out.println("Done");
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public int getInitialVehicleNumber() {
        return initialVehicleNumber;
    }

    public void setInitialVehicleNumber(int initialVehicleNumber) {
        if (initialVehicleNumber >= 0 && initialVehicleNumber <= VEHICLE_LIMIT)
            this.initialVehicleNumber = initialVehicleNumber;
    }

    public int getVehicleNumber() {
//        return vehicleNumber;
        return vehicles.getGeometries().size();
    }

    public AgStatistical getAgStatistical() {
        return agStatistical;
    }

    public static void main(String[] args) {
        doLoop(TrafficSimGeo.class, args);
        System.exit(0);
    }

    /**
     * @param node where the vehicle will be created
     * @return true if the vehicle is created, false otherwise
     */
    public synchronized boolean createVehicle(Node node) throws Exception {
        boolean vehicleIsCreated = false;
        if (getVehicleNumber() < VEHICLE_LIMIT) {
            Node nodeClone = FacadeOfTools.cloneNode(node);
            AgVehicle agVehicle = new AgVehicle(this, nodeClone);
            vehicles.addGeometry(agVehicle.getGeometry());
            Stoppable stoppable = schedule.scheduleRepeating(agVehicle);
            agVehicle.setStoppable(stoppable);
            agStatistical.addVehicleNumberInput();
            vehicleIsCreated = true;
        }
        return vehicleIsCreated;
    }

    public synchronized boolean createDijkstraVehicle(Node node, Route route) throws Exception {
        boolean vehicleIsCreated = false;
        if (getVehicleNumber() < VEHICLE_LIMIT) {
            Node nodeClone = FacadeOfTools.cloneNode(node);
            AgDijkstraVehicle agVehicle = new AgDijkstraVehicle(this, nodeClone, route);
            vehicles.addGeometry(agVehicle.getGeometry());
            Stoppable stoppable = schedule.scheduleRepeating(agVehicle);
            agVehicle.setStoppable(stoppable);
            agStatistical.addVehicleNumberInput();
            vehicleIsCreated = true;
        }
        return vehicleIsCreated;
    }

    public List<Route> createRoutesList(Node arrival) throws Exception {
        EdgeDAO edgeDAO = new EdgeDAOImp1();
        NodeDAO nodeDAO = new NodeDAOImp1();
        List<Route> routes = new LinkedList<>();
        List<Node> nodesTerminal = nodeDAO.findTerminalNodes();
        for (Node nodeT : nodesTerminal)
            if (nodeT.getId() != arrival.getId()) {
                Route route = edgeDAO.getDijkstraRoute(arrival, nodeT);
                if (route != null)
                    routes.add(route);
            }
        return routes;
    }


    public Indicator createIndicator(Node semaphoreLocation, Edge edge) {
        Node nodeClone = FacadeOfTools.cloneNode(semaphoreLocation);
        MasonGeometry masonGeometry = new MasonGeometry(nodeClone.getGeometry());
        Indicator indicator = new Indicator(masonGeometry);
        masonGeometry.isMovable = true;
        semaphoreIndicators.addGeometry(indicator.getGeometry());
        boolean sameDirection = semaphoreLocation.getId() == edge.getNodeFrom().getId();
        LengthIndexedLine segment = new LengthIndexedLine(edge.getGeometry());
        double startIndex;
        if (sameDirection)
            startIndex = segment.getStartIndex() + distanceToSemaphoreInIndex() / 2;
        else
            startIndex = segment.getEndIndex() - distanceToSemaphoreInIndex() / 2;
        Coordinate startCoordinate = segment.extractPoint(startIndex);
        PointMoveTo pointMoveTo = new PointMoveTo(startCoordinate);
        masonGeometry.getGeometry().apply(pointMoveTo);
        masonGeometry.getGeometry().geometryChanged();
        masonGeometry.isMovable = false;
        return indicator;
    }

    /**
     * @param agVehicle
     */
    public void deleteVehicle(AgVehicle agVehicle) {
        vehicles.getGeometries().remove(agVehicle.getGeometry());
        reportVehicleData(agVehicle);
    }

    public void deleteSemaphore(AgSemaphore agSemaphore) {
        semaphores.getGeometries().remove(agSemaphore.getMasonGeometry());
    }

    /**
     * find for a semaphore in a geometry
     *
     * @param node node where the semaphore will be searched
     * @return a semaphore that is in that geometry or null if there is none
     */
    public AgSemaphore findSemaphore(Node node) {
        AgSemaphore agSemaphore = null;
        Bag bag = semaphores.getContainingObjects(node.getGeometry());
        for (Object o : bag)
            if (o instanceof MasonGeometry) {
                MasonGeometry mg = (MasonGeometry) o;
                if (mg.getUserData() instanceof AgSemaphore) {
                    agSemaphore = (AgSemaphore) mg.getUserData();
                    break;
                }
            }
        return agSemaphore;
    }

    public AgVehicle vehicleObstructedByAnother(AgVehicle vehicle) {
        AgVehicle obstructingVehicle = null;
        double newIndex = vehicle.getNextIndex();
        for (AgVehicle pivotVehicle : agVehiclesList()) {
            if (vehicle != pivotVehicle)
                if (vehicle.getCurrentEdge().getId() == pivotVehicle.getCurrentEdge().getId())
                    if (vehicle.getLane() == pivotVehicle.getLane()) {
                        // direction -> 1 or -1
                        double direction = vehicle.getMoveRate() / Math.abs(vehicle.getMoveRate());
                        double distance = (pivotVehicle.getCurrentIndex() - newIndex) * direction;
                        if (distance > 0 && distance <= minimumDistanceBetweenVehiclesInIndex()) {
                            obstructingVehicle = pivotVehicle;
                            break;
                        }
                    }
        }
        return obstructingVehicle;
    }

    public GeomVectorField getStreets() {
        return streets;
    }

    public GeomVectorField getVehicles() {
        return vehicles;
    }

    public GeomVectorField getSemaphores() {
        return semaphores;
    }

    public GeomVectorField getSemaphoreIndicators() {
        return semaphoreIndicators;
    }

    public void setSemaphoreIndicators(GeomVectorField semaphoreIndicators) {
        this.semaphoreIndicators = semaphoreIndicators;
    }

    public GeomVectorField getArrivalNodes() {
        return arrivalNodes;
    }

    public double getVehicleCollisionProbability() {
        return vehicleCollisionProbability;
    }

    public void setVehicleCollisionProbability(double vehicleCollisionProbability) {
        if (vehicleCollisionProbability >= 0 && vehicleCollisionProbability <= 1)
            this.vehicleCollisionProbability = vehicleCollisionProbability;
    }

    public double getUnwiseVehicleCollisionProbability() {
        return unwiseVehicleCollisionProbability;
    }

    public void setUnwiseVehicleCollisionProbability(double unwiseVehicleCollisionProbability) {
        if (unwiseVehicleCollisionProbability >= 0 && unwiseVehicleCollisionProbability <= 1)
            this.unwiseVehicleCollisionProbability = unwiseVehicleCollisionProbability;
    }

    public double getProbabilityOfBeingReckless() {
        return probabilityOfBeingReckless;
    }

    public double getProbabilityOfPassYellow() {
        return probabilityOfPassYellow;
    }

    public double getProbabilityGetFirst() {
        return probabilityGetFirst;
    }

    public void setProbabilityOfBeingReckless(double probabilityOfBeingReckless) {
        if (probabilityOfBeingReckless >= 0 && probabilityOfBeingReckless <= 1)
            this.probabilityOfBeingReckless = probabilityOfBeingReckless;
    }

    public void setProbabilityOfPassYellow(double probabilityOfPassYellow) {
        if (probabilityOfPassYellow >= 0 && probabilityOfPassYellow <= 1)
            this.probabilityOfPassYellow = probabilityOfPassYellow;
    }

    public int getAccidentTime() {
        return accidentTime;
    }

    public void setAccidentTime(int accidentTime) {
        if (accidentTime < maximumWaitInSystemTime())
            this.accidentTime = accidentTime;
    }

    public void reportInfraction() {
        agStatistical.addNumberOfInfractions();
    }

    public void reportAccident(boolean forInfringement) {
        if (forInfringement) {
            agStatistical.addNumberOfAccidentsForInfringement();
        } else {
            agStatistical.addNumberOfAccidentsForImprudence();
        }
    }

    private void reportVehicleData(AgVehicle agVehicle) {
        agStatistical.addVehicleNumberOutput();
        agStatistical.getTimesInTheSystem().add(schedule.getTime() - agVehicle.getMyMomentOfArrival());
        agStatistical.getStepsOnHold().add(agVehicle.getNumberOfStepsOnHold());
        if (agVehicle.isOffender()) agStatistical.addNumberOfOffenders();
    }

    public List<AgVehicle> agVehiclesList() {
        List<AgVehicle> agVehiclesList = new LinkedList<>();
        for (Object o : vehicles.getGeometries()) {
            MasonGeometry masonGeometry = (MasonGeometry) o;
            AgVehicle vehicle = (AgVehicle) masonGeometry.getUserData();
            agVehiclesList.add(vehicle);
        }
        return agVehiclesList;
    }

    public List<Indicator> indicatorsList() {
        List<Indicator> indicatorsList = new LinkedList<>();
        for (Object o : semaphoreIndicators.getGeometries()) {
            MasonGeometry masonGeometry = (MasonGeometry) o;
            Indicator indicator = (Indicator) masonGeometry.getUserData();
            indicatorsList.add(indicator);
        }
        return indicatorsList;
    }

    public List<AgSemaphore> agSemaphoreList() {
        List<AgSemaphore> agSemaphoreList = new LinkedList<>();
        for (Object o : semaphores.getGeometries()) {
            MasonGeometry masonGeometry = (MasonGeometry) o;
            AgSemaphore semaphore = (AgSemaphore) masonGeometry.getUserData();
            agSemaphoreList.add(semaphore);
        }
        return agSemaphoreList;
    }

    public List<Corner> cornersList() {
        List<Corner> cornersList = new LinkedList<>();
        for (Object o : corners.getGeometries()) {
            MasonGeometry masonGeometry = (MasonGeometry) o;
            Corner corner = (Corner) masonGeometry.getUserData();
            cornersList.add(corner);
        }
        return cornersList;
    }

    public List<AgVehicle> crashedVehicles() {
        List<AgVehicle> result = new LinkedList<>();
        for (AgVehicle vehicle : agVehiclesList()) {
            if (vehicle.isCrashed())
                result.add(vehicle);
        }
        return result;
    }

    public List<AgVehicle> violatorVehicles() {
        List<AgVehicle> result = new LinkedList<>();
        for (AgVehicle vehicle : agVehiclesList()) {
            if (!vehicle.isCrashed() && vehicle.isOffender())
                result.add(vehicle);
        }
        return result;
    }

    public List<Edge> edgesList() {
        List<Edge> edgesList = new LinkedList<>();
        for (Object o : streets.getGeometries()) {
            MasonGeometry masonGeometry = (MasonGeometry) o;
            Edge street = (Edge) masonGeometry.getUserData();
            edgesList.add(street);
        }
        return edgesList;
    }

    @Override
    public void kill() {
        try {
            System.out.println("Generating report");
            generateReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.kill();
    }

    private void generateReport() throws Exception {
        Statistical mStatistical = new Statistical();
        mStatistical.setVehicleNumberInput(agStatistical.getVehicleNumberInput());
        mStatistical.setVehicleNumberOutput(agStatistical.getVehicleNumberOutput());
        mStatistical.setNumberOfAccidentsForImprudence(agStatistical.getNumberOfAccidentsForImprudence());
        mStatistical.setNumberOfAccidentsForInfringement(agStatistical.getNumberOfAccidentsForInfringement());
        mStatistical.setNumberOfInfractions(agStatistical.getNumberOfInfractions());
        mStatistical.setNumberOfOffenders(agStatistical.getNumberOfOffenders());
        mStatistical.setAverageSpeedOfVehicles(averageSpeedOfVehicles);
        mStatistical.setAverageSystemTime(agStatistical.getAverageRealTimeInSystem());
        mStatistical.setAverageTimeOnHold(agStatistical.getAverageRealTimeOnHold());
        mStatistical.setSignalized(!semaphores.getGeometries().isEmpty());
        mStatistical.setTotalSimulationTime(FacadeOfTools.convertFromSystemTimeToS(schedule.getTime()));
        statisticalDAO.create(mStatistical);
    }

    public int getAverageSpeedOfVehicles() {
        return averageSpeedOfVehicles;
    }

    public void setAverageSpeedOfVehicles(int averageSpeedOfVehicles) {
        this.averageSpeedOfVehicles = averageSpeedOfVehicles;
    }

    public double randomSpeedVehicle() {
        return averageSpeedOfVehicles + random.nextInt(21);
    }

    public void ambientSensation(int newTemperature){
        averageSpeedOfVehicles = (int)(newTemperature * 1.3);
        probabilityGetFirst = newTemperature * 0.0016;
        probabilityOfPassYellow = newTemperature * 0.0016;
        probabilityOfBeingReckless = newTemperature * 0.0006;
        unwiseVehicleCollisionProbability = newTemperature * 0.01;
        vehicleCollisionProbability = newTemperature * 0.0016;
    }
}
