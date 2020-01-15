package sim.app.trafficsimgeo.logic.agent;

import sim.app.trafficsimgeo.logic.controller.Config;
import sim.app.trafficsimgeo.logic.controller.TrafficSimGeo;
import sim.app.trafficsimgeo.logic.util.FacadeOfTools;
import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.LinkedList;
import java.util.List;

public class AgStatistical implements Steppable {
    private int vehicleNumberInput;
    private int vehicleNumberOutput;
    private int numberOfAccidentsForInfringement;
    private int numberOfAccidentsForImprudence;
    private int numberOfInfractions;
    private int numberOfOffenders;

    private int cantMaxVehicles;

    //number of steps waiting for each vehicle
    private List<Integer> stepsOnHold;
    private List<Double> timesInTheSystem;

    public AgStatistical() {
        stepsOnHold = new LinkedList<>();
        timesInTheSystem = new LinkedList<>();
        cantMaxVehicles = Config.cantMaxVehicles;
    }

    public int getVehicleNumberInput() {
        return vehicleNumberInput;
    }

    public synchronized void addVehicleNumberInput() {
        this.vehicleNumberInput++;
    }

    public int getVehicleNumberOutput() {
        return vehicleNumberOutput;
    }

    public synchronized void addVehicleNumberOutput() {
        this.vehicleNumberOutput++;
    }

    public int getNumberOfInfractions() {
        return numberOfInfractions;
    }

    public int getNumberOfAccidentsForInfringement() {
        return numberOfAccidentsForInfringement;
    }

    public void addNumberOfAccidentsForInfringement() {
        this.numberOfAccidentsForInfringement++;
    }

    public int getNumberOfAccidentsForImprudence() {
        return numberOfAccidentsForImprudence;
    }

    public void addNumberOfAccidentsForImprudence() {
        this.numberOfAccidentsForImprudence++;
    }

    public synchronized void addNumberOfInfractions() {
        this.numberOfInfractions++;
    }

    public List<Integer> getStepsOnHold() {
        return stepsOnHold;
    }

    public List<Double> getTimesInTheSystem() {
        return timesInTheSystem;
    }

    public int getNumberOfOffenders() {
        return numberOfOffenders;
    }

    public synchronized void addNumberOfOffenders() {
        this.numberOfOffenders++;
    }

    public int getNumberOfAccidents() {
        return numberOfAccidentsForImprudence + numberOfAccidentsForInfringement;
    }

    @Override
    public void step(SimState simState) {
        if (cantMaxVehicles > 0 && vehicleNumberOutput >= cantMaxVehicles) {
            simState.finish();
        }
        TrafficSimGeo trafficState = (TrafficSimGeo) simState;
        List<AgVehicle> vehicleList = trafficState.agVehiclesList();

        for (AgVehicle vehicle : vehicleList) {
//            if (vehicle.maximumWaitExceeded(trafficState)) {
//                vehicle.stop(trafficState);
//            } else
            if (vehicle.getMyMomentOfTheLastAccident() > 0 && trafficState.schedule.getTime() - vehicle.getMyMomentOfTheLastAccident() > trafficState.getAccidentTime()) {
                vehicle.stop(trafficState);
            }
        }


    }

    public double getAverageRealTimeInSystem() {
        return FacadeOfTools.convertFromStoSystemTime(getAverageSystemTime());
    }

    private double getAverageSystemTime() {
        double sum = 0;
        for (Double aDouble : timesInTheSystem) {
            sum += aDouble;
        }
        int amount = timesInTheSystem.size();
        return amount > 0 ? sum / amount : 0;
    }

    private double getAverageStepsOnHold() {
        int sum = 0;
        for (Integer aInteger : stepsOnHold) {
            sum += aInteger;
        }
        int amount = stepsOnHold.size();
        return amount > 0 ? sum / amount : 0;
    }

    public double getAverageRealTimeOnHold() {
        return FacadeOfTools.convertFromStoSystemTime(getAverageStepsOnHold() * TrafficSimGeo.REASON_STEPS_PER_SYSTEM_TIME);
    }

    public int getCantMaxVehicles() {
        return cantMaxVehicles;
    }

    public void setCantMaxVehicles(int cantMaxVehicles) {
        this.cantMaxVehicles = cantMaxVehicles;
    }
}
