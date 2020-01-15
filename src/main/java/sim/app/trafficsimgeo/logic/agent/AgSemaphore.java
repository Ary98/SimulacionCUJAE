package sim.app.trafficsimgeo.logic.agent;

import com.vividsolutions.jts.geom.Polygon;
import sim.app.trafficsimgeo.logic.controller.Config;
import sim.app.trafficsimgeo.logic.controller.TrafficSimGeo;
import sim.app.trafficsimgeo.logic.util.FacadeOfTools;
import sim.app.trafficsimgeo.model.dao.EdgeDAO;
import sim.app.trafficsimgeo.model.dao.EdgeDAOImp2;
import sim.app.trafficsimgeo.model.entity.Edge;
import sim.app.trafficsimgeo.model.entity.Node;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.geo.MasonGeometry;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class AgSemaphore implements Steppable {
    private List<Phase> cycle;
    private Phase currentPhase;
    private Iterator<Phase> iterator;
    private Node locationNode;
    private double lastStateChangeTime;
    private MasonGeometry masonGeometry;

    public void setStoppable(Stoppable stoppable) {
        this.stoppable = stoppable;
    }

    private Stoppable stoppable;
    // Square side in meters
    private static final double SIDE = 0.5;
    EdgeDAO edgeDAO;

    //time it takes a full cycle
    public int getCycleTime() {
        int time = 0;
        for (Phase phase : cycle) {
            time += phase.getGreenTime();
            time += phase.getYellowTime();
            time += phase.getRedTime();
        }
        return time;
    }

    public List<Phase> getCycle() {
        return cycle;
    }

    public void setCycle(List<Phase> cycle) {
        this.cycle = cycle;
    }

    @Override
    public void step(SimState state) {
        double nowTime = state.schedule.getTime();

        if ((currentPhase.getActiveState() == Light.GREEN &&
                nowTime - lastStateChangeTime >= FacadeOfTools.convertFromStoSystemTime(currentPhase.getGreenTime()))
                || (currentPhase.getActiveState() == Light.YELLOW &&
                nowTime - lastStateChangeTime >=  FacadeOfTools.convertFromStoSystemTime(currentPhase.getYellowTime()))
                || (currentPhase.getActiveState() == Light.RED &&
                nowTime - lastStateChangeTime >=  FacadeOfTools.convertFromStoSystemTime(currentPhase.getRedTime()))){
            next();
            lastStateChangeTime = nowTime;
        }

        /*if (nowTime - lastStateChangeTime >=
                FacadeOfTools.convertFromStoSystemTime(currentPhase.getGreenTime() + currentPhase.getYellowTime())) {
            next();
            lastStateChangeTime = nowTime;
        }*/
        /*System.out.println(state.schedule.getTime());
        System.out.println(currentPhase.getActiveState());*/
    }

    public AgSemaphore(SimState state, Node locationNode) throws Exception {
        edgeDAO = new EdgeDAOImp2();
        TrafficSimGeo trafficState = (TrafficSimGeo) state;
        lastStateChangeTime = 0;
        this.locationNode = locationNode;
        cycle = new LinkedList();
//        get input edge list
        List<Edge> inputEdgeList = edgeDAO.findAllInputEdge(locationNode);
//        if(inputEdgeList.size() == 0)
//            new Exception("you can not create semaphore without phases");
        for (Edge input : inputEdgeList) {
            List<Edge> outputEdgeList = edgeDAO.findAllOutputEdge(locationNode, input);
            List<InputWithOutputs> inputWithOutputsList = new LinkedList<>();
            InputWithOutputs inputWithOutputs = new InputWithOutputs(input, outputEdgeList);
            inputWithOutputsList.add(inputWithOutputs);
//            int greenTime = 10 + state.random.nextInt(11);
            int greenTime = Config.defaultGreenTime;
            int yellowTime = Config.defaultYellowTime;
            int redTime = Config.defaultRedTime;
            Phase phase = new Phase(trafficState, greenTime, redTime, yellowTime, inputWithOutputsList, locationNode);
            cycle.add(phase);
        }
        next();
    }

    private void next() throws NoSuchElementException {
        if (iterator == null || !iterator.hasNext()) {
            iterator = cycle.iterator();
        }
        /*if (currentPhase != null) {
            currentPhase.nextState();
        }*/
        currentPhase = iterator.next();
        currentPhase.nextState();
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public MasonGeometry getMasonGeometry() {
        if (masonGeometry == null) {
            Polygon square = FacadeOfTools.createSquare(locationNode.getGeometry(), SIDE);
            masonGeometry = new MasonGeometry(square);
            masonGeometry.setUserData(this);
        }
        return masonGeometry;
    }

    public String getAgentType() {
        return "Semaphore";
    }

    public boolean onGreen(Edge currentEdge, Edge newEdge) {
        boolean onGreen = false;
        if(currentPhase.getActiveState() == Light.GREEN) {
            for (InputWithOutputs inputWithOutputs : currentPhase.getListOfInputWithOutputsEnabled()) {
                if (inputWithOutputs.getInputEdge().getId() == currentEdge.getId()) {
                    for (Edge edgeOutput : inputWithOutputs.getOutputEdges()) {
                        if (edgeOutput.getId() == newEdge.getId()) {
                            onGreen = true;
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return onGreen;
    }

    @Override
    public String toString() {
        String toString = getClass().getName() + " [node = " + locationNode.getId() + ", currentPhase = " + currentPhase.getActiveState() + "]";
        return toString;
    }

    public Node getLocationNode() {
        return locationNode;
    }


    public void stop(TrafficSimGeo trafficState) {
        stoppable.stop();
        trafficState.deleteSemaphore(this);
    }

}