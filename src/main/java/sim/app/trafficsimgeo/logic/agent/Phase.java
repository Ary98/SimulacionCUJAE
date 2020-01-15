package sim.app.trafficsimgeo.logic.agent;

import sim.app.trafficsimgeo.logic.controller.TrafficSimGeo;
import sim.app.trafficsimgeo.model.entity.Edge;
import sim.app.trafficsimgeo.model.entity.Node;

import java.util.List;

class Phase {

    private int greenTime;
    private int yellowTime;
    private int redTime;
    private List<InputWithOutputs> listOfInputWithOutputsEnabled;
    private Indicator indicator;


    Phase(TrafficSimGeo trafficState, int greenTime, int redTime, int yellowTime, List<InputWithOutputs> listOfInputWithOutputsEnabled, Node semaphoreLocation) {
        this.greenTime = greenTime;
        this.yellowTime = yellowTime;
        this.redTime = redTime;
        this.listOfInputWithOutputsEnabled = listOfInputWithOutputsEnabled;
        createIndicators(trafficState, semaphoreLocation);
    }

    private void createIndicators(TrafficSimGeo trafficState, Node semaphoreLocation) {
        Edge edge = listOfInputWithOutputsEnabled.get(0).getInputEdge();
        indicator = trafficState.createIndicator(semaphoreLocation, edge);
    }

    int getGreenTime() {
        return greenTime;
    }

    public void setGreenTime(int greenTime) {
        if (greenTime >= 0)
            this.greenTime = greenTime;
    }

    int getRedTime() {
        return greenTime;
    }

    public void setRedTime(int redTime) {
        if (redTime >= 0)
            this.redTime = redTime;
    }

    int getYellowTime() {
        return yellowTime;
    }

    public void setYellowTime(int greenTime) {
        if (greenTime >= 0)
            this.yellowTime = greenTime;
    }

    List<InputWithOutputs> getListOfInputWithOutputsEnabled() {
        return listOfInputWithOutputsEnabled;
    }

    void nextState() {
        switch (indicator.getState()){
            case GREEN: indicator.setState(Light.YELLOW); break;
            case YELLOW: indicator.setState(Light.RED); break;
            case RED: indicator.setState(Light.GREEN); break;
        }
    }

    Light getActiveState(){
        return indicator.getState();
    }

}