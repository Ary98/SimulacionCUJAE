package sim.app.trafficsimgeo.logic.agent;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.linearref.LengthIndexedLine;
import sim.app.trafficsimgeo.logic.controller.TrafficSimGeo;
import sim.app.trafficsimgeo.logic.util.FacadeOfTools;
import sim.app.trafficsimgeo.model.dao.EdgeDAOImp2;
import sim.app.trafficsimgeo.model.entity.Edge;
import sim.app.trafficsimgeo.model.entity.Node;
import sim.app.trafficsimgeo.model.dao.EdgeDAO;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.geo.MasonGeometry;
import sim.util.geo.PointMoveTo;

public class AgVehicle implements Steppable {

    protected static final long serialVersionUID = -1113018274619047013L;
    // point that denotes agent's position
    protected MasonGeometry location;
    // The base speed of the agent.
    protected double baseMoveRate;
    // How much to move the agent by in each step(); may become negative if
    // agent is moving from the end to the start of current line.
    protected double moveRate;
    // Used by agent to walk along line segment; assigned in setNewRoute()
    protected LengthIndexedLine segment;
    protected double startIndex; // start position of current line
    protected double endIndex; // end position of current line
    protected double signalCheckIndex; // signal check position
    protected double currentIndex;
    protected PointMoveTo pointMoveTo;
    protected Edge currentEdge;
    protected Edge nextEdge;
    protected Stoppable stoppable;
    protected EdgeDAO edgeDAO;
    protected boolean nextDirection;
    protected AgSemaphore mySemaphore;
    //lane where the vehicle is located
    protected int lane;

    public double getMyMomentOfTheLastAccident() {
        return myMomentOfTheLastAccident;
    }

    public void setMyMomentOfTheLastAccident(double myMomentOfTheLastAccident) {
        this.myMomentOfTheLastAccident = myMomentOfTheLastAccident;
    }

    //-1 if the vehicle not is crashed
    protected double myMomentOfTheLastAccident;
    //true if the vehicle failed to comply with a signal
    protected boolean unwise;
    //true if the vehicle pass the yellow light
    protected boolean passYellow;
    //true if you committed at least one violation
    protected boolean offender;
    //instant that you arrive at the simulation
    protected double myMomentOfArrival;
    //number of steps you've been waiting for
    protected int numberOfStepsOnHold;
    //true if you are waiting now
    protected boolean waiting;

    public AgVehicle(SimState state, Node startNode) throws Exception {
        TrafficSimGeo trafficState = (TrafficSimGeo) state;
        waiting = false;
        offender = false;
        numberOfStepsOnHold = 0;
        myMomentOfArrival = state.schedule.getTime();
        edgeDAO = new EdgeDAOImp2();
        myMomentOfTheLastAccident = -1;
        unwise = false;
        passYellow = false;
        baseMoveRate = FacadeOfTools.convertFromKMxHtoIndexXSystemTime(trafficState.randomSpeedVehicle());
        startIndex = 0.0;
        endIndex = 0.0;
        currentIndex = 0.0;
        signalCheckIndex = 0.0;
        pointMoveTo = new PointMoveTo();
        nextEdge = edgeDAO.findAdjacentToTheNode(startNode).get(0);
        boolean sameDirection = startNode.getId() == nextEdge.getNodeFrom().getId();
        location = new MasonGeometry(startNode.getGeometry());
        location.isMovable = true;
        location.setUserData(this);
        setNewRoute(trafficState, sameDirection);
    }


    /**
     * @return geometry representing agent location
     */
    public MasonGeometry getGeometry() {
        return location;
    }

    /**
     * true if the agent has arrivedToEquine at the target intersection
     */
    protected boolean arrivedToEquine() {
        return (moveRate > 0 && currentIndex >= endIndex)
                || (moveRate < 0 && currentIndex <= startIndex);
    }

    protected boolean arrivedToWhiteLine() {
        return (moveRate > 0 && currentIndex >= signalCheckIndex)
                || (moveRate < 0 && currentIndex <= signalCheckIndex);
    }

    /**
     * randomly selects an adjacent route to traverse
     */
    protected void findNewPath(TrafficSimGeo trafficState) throws Exception {
        Node node = moveRate > 0 ? currentEdge.getNodeTo() : currentEdge.getNodeFrom();
        nextEdge = edgeDAO.findRandomOutputEdge(node, currentEdge);
        if (nextEdge != null)
            nextDirection = nextEdge.getNodeFrom().getId() == node.getId();
        mySemaphore = trafficState.findSemaphore(getMyFinalPosition());
    }

    public Node getMyFinalPosition() {
        return moveRate > 0 ? currentEdge.getNodeTo() : currentEdge.getNodeFrom();
    }

    /**
     * have the agent move along new route
     *
     * @param sameDirection true if agent at start of line else agent placed at end
     */
    protected void setNewRoute(TrafficSimGeo trafficState, boolean sameDirection) throws Exception {
        if (nextEdge != null) {
            currentEdge = nextEdge;
            // TODO: 08-abr-18 sustituir 2 por la cantidad de carriles de la calle actual
            int numberOfLanes = 2;
            lane = trafficState.random.nextInt(numberOfLanes) + 1;
            if (!sameDirection) lane *= -1;
            segment = new LengthIndexedLine(currentEdge.getGeometry());
            startIndex = segment.getStartIndex();
            endIndex = segment.getEndIndex();
            Coordinate startCoordinate;

            if (sameDirection) {
                startCoordinate = segment.extractPoint(startIndex);
                currentIndex = startIndex;
                moveRate = baseMoveRate; // ensure we move forward along segment
                signalCheckIndex = endIndex - trafficState.minimumDistanceBetweenVehiclesInIndex();
            } else {
                startCoordinate = segment.extractPoint(endIndex);
                currentIndex = endIndex;
                moveRate = -baseMoveRate; // ensure we move backward along segment
                signalCheckIndex = startIndex + trafficState.minimumDistanceBetweenVehiclesInIndex();
            }
            moveTo(startCoordinate);
            findNewPath(trafficState);
        } else
            stop(trafficState);
    }

    // move the agent to the given coordinates

    protected void moveTo(Coordinate c) {
        pointMoveTo.setCoordinate(c);
        location.getGeometry().apply(pointMoveTo);
        location.geometry.geometryChanged();
    }

    protected boolean maximumWaitExceeded(TrafficSimGeo trafficState) {
        double myWaitTime = numberOfStepsOnHold * trafficState.REASON_STEPS_PER_SYSTEM_TIME;
        return myWaitTime >= TrafficSimGeo.maximumWaitInSystemTime();
    }

    @Override
    public void step(SimState state) {
        TrafficSimGeo trafficState = (TrafficSimGeo) state;
        resetMoveRate(trafficState);
        Edge oldEdge = currentEdge;
        double oldIndex = currentIndex;
        if (!this.isCrashed()) {
            try {
                move(trafficState);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.maximumWaitExceeded(trafficState)) {
                this.stop(trafficState);
            }
        } else {
            if (this.maximumWaitExceeded(trafficState)) {
                this.stop(trafficState);
            } else if (trafficState.schedule.getTime() - myMomentOfTheLastAccident > trafficState.getAccidentTime()) {
                this.stop(trafficState);
            }
        }
        //validate if you are waiting
        waiting = oldIndex == currentIndex && oldEdge == currentEdge;
        if (waiting) numberOfStepsOnHold++;
        //eliminate by breakage
    }

    protected void move(TrafficSimGeo trafficState) throws Exception {
        AgVehicle obstructingVehicle = vehicleNearby(trafficState);
        if (obstructingVehicle == null) {
            moveWithoutObstacle(trafficState);
        }
        else if (vehicleGetFirst(trafficState)){
            getFirst(obstructingVehicle);
        }
        else if (theVehicleIsGoingToCrash(trafficState, obstructingVehicle)) {
            boolean forInfringement = this.unwise || obstructingVehicle.unwise;
            trafficState.reportAccident(forInfringement);
            double nowTime = trafficState.schedule.getTime();
            this.myMomentOfTheLastAccident = nowTime;
            obstructingVehicle.myMomentOfTheLastAccident = nowTime;
        }
    }

    protected void moveWithoutObstacle(TrafficSimGeo trafficState) throws Exception {
        if (!arrivedToWhiteLine()) {
                moveAlongPath1();
            }
            else {
                if (mySemaphore != null) {
                    checkSemaphore(trafficState);
                }
                else {
                    if (!arrivedToEquine())
                        moveAlongPath2();
                    else {
                        if (getMyFinalPosition().getCardinality() == 1) {
                            stop(trafficState);
                        }
                        else
                            setNewRoute(trafficState, nextDirection);
                    }
                }
            }
    }

    protected void getFirst(AgVehicle obs){
        System.out.println(moveRate);

        double move =  moveRate + TrafficSimGeo.minimumDistanceBetweenVehiclesInIndex();

        System.out.println(currentIndex);
        System.out.println(obs.currentIndex);

        if(moveRate > 0) {
            currentIndex += (obs.currentIndex + move);
        }
        else{
            currentIndex -= (obs.currentIndex - move);
        }

        System.out.println(currentIndex);
        System.out.println(obs.currentIndex);

        Coordinate currentPos = segment.extractPoint(currentIndex);
        moveTo(currentPos);
    }

    protected boolean vehicleGetFirst(TrafficSimGeo trafficState){
        double getFirstProb = waiting ? 0 : trafficState.getProbabilityGetFirst();
        return trafficState.random.nextDouble() < getFirstProb;
    }

    protected boolean theVehicleIsGoingToCrash(TrafficSimGeo trafficState, AgVehicle obstructingVehicle) {
        double collisionProbability = waiting ? 0 : unwise || obstructingVehicle.unwise ?
                trafficState.getUnwiseVehicleCollisionProbability() : trafficState.getVehicleCollisionProbability();
        return trafficState.random.nextDouble() < collisionProbability;
    }

    protected void checkSemaphore(TrafficSimGeo trafficState) {
        if (mySemaphore.onGreen(currentEdge, nextEdge)
                || (mySemaphore.getCurrentPhase().getActiveState() == Light.RED && tryToPassRecklessly(trafficState))
                || (mySemaphore.getCurrentPhase().getActiveState() == Light.YELLOW
                && tryToPassOnYellow(trafficState))
        )
            mySemaphore = null;
    }

    protected boolean tryToPassRecklessly(TrafficSimGeo trafficState) {
        double probabilityOfBeingReckless = trafficState.getProbabilityOfBeingReckless();
        unwise = trafficState.random.nextDouble() < probabilityOfBeingReckless;
        if (unwise) {
            offender = true;
            trafficState.reportInfraction();
        }
        return unwise;
    }

    protected boolean tryToPassOnYellow(TrafficSimGeo simGeo){
        double probabilityYellow = simGeo.getProbabilityOfPassYellow();
        passYellow = simGeo.random.nextDouble() < probabilityYellow;
        if (passYellow){
            offender = true;
            simGeo.reportInfraction();
        }
        return passYellow;
    }

    // move agent along current line segment
    protected void moveAlongPath1() {
        currentIndex += moveRate;

        // Truncate movement to end of line segment
        if (moveRate < 0) { // moving from endIndex to startIndex
            if (currentIndex < signalCheckIndex) {
                currentIndex = signalCheckIndex;
            }
        } else { // moving from startIndex to endIndex
            if (currentIndex > signalCheckIndex) {
                currentIndex = signalCheckIndex;
            }
        }
        Coordinate currentPos = segment.extractPoint(currentIndex);
        moveTo(currentPos);
    }

    // move agent along current line segment
    protected void moveAlongPath2() {
        currentIndex += moveRate;
        // Truncate movement to end of line segment
        if (moveRate < 0) { // moving from endIndex to startIndex
            if (currentIndex < startIndex) {
                currentIndex = startIndex;
            }
        } else { // moving from startIndex to endIndex
            if (currentIndex > endIndex) {
                currentIndex = endIndex;
            }
        }
        Coordinate currentPos = segment.extractPoint(currentIndex);
        moveTo(currentPos);
    }

    protected void stop(TrafficSimGeo trafficState) {
        stoppable.stop();
        trafficState.deleteVehicle(this);
    }

    public void setStoppable(Stoppable stoppable) {
        this.stoppable = stoppable;
    }

    @Override
    public String toString() {
        String toString = getClass().getName();
        return toString;
    }

    //true if you have a vehicle nearby
    protected AgVehicle vehicleNearby(TrafficSimGeo trafficState) {
        return trafficState.vehicleObstructedByAnother(this);
    }

    public double getNextIndex() {
        double newIndex = currentIndex + moveRate;
        if (moveRate < 0) { // moving from endIndex to startIndex
            if (newIndex < startIndex) {
                newIndex = startIndex;
            }
        } else {
            if (newIndex > endIndex) {
                newIndex = endIndex;
            }
        }
        return newIndex;
    }

    private void resetMoveRate(TrafficSimGeo trafficState){
        baseMoveRate = FacadeOfTools.convertFromKMxHtoIndexXSystemTime(trafficState.randomSpeedVehicle());
    }

    public Edge getCurrentEdge() {
        return currentEdge;
    }

    public double getMoveRate() {
        return moveRate;
    }

    public double getCurrentIndex() {
        return currentIndex;
    }

    public int getLane() {
        return lane;
    }

    public boolean isCrashed() {
        return myMomentOfTheLastAccident >= 0;
    }


    public boolean isUnwise() {
        return unwise;
    }

    public void setUnwise(boolean unwise) {
        this.unwise = unwise;
    }

    public double getMyMomentOfArrival() {
        return myMomentOfArrival;
    }

    public int getNumberOfStepsOnHold() {
        return numberOfStepsOnHold;
    }

    public boolean isOffender() {
        return offender;
    }
}
