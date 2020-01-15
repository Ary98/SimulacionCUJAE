package sim.app.trafficsimgeo.model.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class Statistical {
    private int id;
    private int vehicleNumberInput;
    private int vehicleNumberOutput;
    private int numberOfAccidentsForInfringement;
    private int numberOfAccidentsForImprudence;
    private int numberOfInfractions;
    private int numberOfOffenders;
    private double averageSpeedOfVehicles;
    private double averageSystemTime;
    private double averageTimeOnHold;
    private boolean signalized;
    private double totalSimulationTime;
    protected Date moment;

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    public Statistical() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVehicleNumberInput() {
        return vehicleNumberInput;
    }

    public void setVehicleNumberInput(int vehicleNumberInput) {
        this.vehicleNumberInput = vehicleNumberInput;
    }

    public int getVehicleNumberOutput() {
        return vehicleNumberOutput;
    }

    public void setVehicleNumberOutput(int vehicleNumberOutput) {
        this.vehicleNumberOutput = vehicleNumberOutput;
    }

    public int getNumberOfAccidentsForInfringement() {
        return numberOfAccidentsForInfringement;
    }

    public void setNumberOfAccidentsForInfringement(int numberOfAccidentsForInfringement) {
        this.numberOfAccidentsForInfringement = numberOfAccidentsForInfringement;
    }

    public int getNumberOfAccidentsForImprudence() {
        return numberOfAccidentsForImprudence;
    }

    public void setNumberOfAccidentsForImprudence(int numberOfAccidentsForImprudence) {
        this.numberOfAccidentsForImprudence = numberOfAccidentsForImprudence;
    }

    public int getNumberOfInfractions() {
        return numberOfInfractions;
    }

    public void setNumberOfInfractions(int numberOfInfractions) {
        this.numberOfInfractions = numberOfInfractions;
    }

    public int getNumberOfOffenders() {
        return numberOfOffenders;
    }

    public void setNumberOfOffenders(int numberOfOffenders) {
        this.numberOfOffenders = numberOfOffenders;
    }

    public double getAverageSystemTime() {
        return averageSystemTime;
    }

    public void setAverageSystemTime(double averageSystemTime) {
        this.averageSystemTime = averageSystemTime;
    }

    public double getAverageTimeOnHold() {
        return averageTimeOnHold;
    }

    public void setAverageTimeOnHold(double averageTimeOnHold) {
        this.averageTimeOnHold = averageTimeOnHold;
    }

    public boolean isSignalized() {
        return signalized;
    }

    public int isSignalizedInt() {
        return signalized ? 1 : 0;
    }

    public void setSignalized(boolean signalized) {
        this.signalized = signalized;
    }

    public double getTotalSimulationTime() {
        return totalSimulationTime;
    }

    public void setTotalSimulationTime(double totalSimulationTime) {
        this.totalSimulationTime = totalSimulationTime;
    }

    public double getAverageSpeedOfVehicles() {
        return averageSpeedOfVehicles;
    }

    public void setAverageSpeedOfVehicles(double averageSpeedOfVehicles) {
        this.averageSpeedOfVehicles = averageSpeedOfVehicles;
    }
}
