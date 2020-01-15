package sim.app.trafficsimgeo.logic.controller;

public abstract class Config {

    // config
    public static String pathOfDbSpatiaLite = "maps\\CDmio.osm.db";
    public static int averageSpeedOfVehicles = ConfigFinal.averageSpeedOfVehicles;
    public static int initialVehicleNumber = ConfigFinal.initialVehicleNumber;
    public static int timeBetweenArrival = ConfigFinal.timeBetweenArrival;
    public static int arriveAmount = ConfigFinal.arriveAmount;
    public static int cantMaxVehicles = ConfigFinal.cantMaxVehicles;
    public static int defaultGreenTime = ConfigFinal.defaultGreenTime;
    public static int defaultYellowTime = ConfigFinal.defaultYellowTime;
    public static int defaultRedTime = ConfigFinal.defaultRedTime;
    public static double vehicleGetFirstProbability = ConfigFinal.vehicleGetFirstProbability;
    public static double vehicleCollisionProbability = ConfigFinal.vehicleCollisionProbability;
    public static double unwiseVehicleCollisionProbability = ConfigFinal.unwiseVehicleCollisionProbability;
    public static double probabilityOfBeingReckless = ConfigFinal.probabilityOfBeingReckless;
    public static double probabilityPassYellow = ConfigFinal.probabilityPassYellow;
    public static int temperature = ConfigFinal.initialTemperature;

    // final
    public static final String pathOfDbStatistical = "statistical.sqlite";

}
