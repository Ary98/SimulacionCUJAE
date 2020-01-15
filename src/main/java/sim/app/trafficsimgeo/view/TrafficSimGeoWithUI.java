package sim.app.trafficsimgeo.view;

import sim.app.trafficsimgeo.logic.agent.*;
import sim.app.trafficsimgeo.logic.controller.TrafficSimGeo;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.geo.GeomPortrayal;
import sim.portrayal.geo.GeomVectorFieldPortrayal;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TrafficSimGeoWithUI extends GUIState {

    public static final double VEHICLE_SCALE = 0.000035;
    public static final double INDICATOR_SCALE = 0.000020;

    private Display2D display;
    private JFrame displayFrame;

    private GeomVectorFieldPortrayal streetsPortrayal = new GeomVectorFieldPortrayal();
    private GeomVectorFieldPortrayal vehiclesPortrayal = new GeomVectorFieldPortrayal();
    private GeomVectorFieldPortrayal semaphoresPortrayal = new GeomVectorFieldPortrayal();
    private GeomVectorFieldPortrayal semaphoreIndicatorsPortrayal = new GeomVectorFieldPortrayal();
    private GeomVectorFieldPortrayal arrivalNodesPortrayal = new GeomVectorFieldPortrayal();
    private GeomVectorFieldPortrayal cornersPortrayal = new GeomVectorFieldPortrayal();

    public TrafficSimGeoWithUI(SimState state) {
        super(state);
    }

    public TrafficSimGeoWithUI() throws Exception {
//        super(new TrafficSimGeo(System.currentTimeMillis()));
        super(TrafficSimGeo.getInstance());
    }

    @Override
    public void init(Controller controller) {
        super.init(controller);

        display = new Display2D(TrafficSimGeo.WIDTH, TrafficSimGeo.HEIGHT, this);
        display.attach(streetsPortrayal, "Streets", true);
        display.attach(semaphoresPortrayal, "Semaphores", true);
        display.attach(vehiclesPortrayal, "Vehicles", true);
        display.attach(semaphoreIndicatorsPortrayal, "Semaphores indicators", true);
        display.attach(arrivalNodesPortrayal, "Arrival Nodes", true);
        display.attach(cornersPortrayal, "Corners", true);
        displayFrame = display.createFrame();
        controller.registerFrame(displayFrame);
        displayFrame.setVisible(true);
    }


    @Override
    public void start() {
        super.start();
        setupPortrayals();
        repaint();
    }

    // to solve the problem of visualization error
    private boolean increasedScale = false;
    private final double variation = 0.002;

    private void repaint() {
        double scale = display.getScale();
        increasedScale = !increasedScale;
        scale += (increasedScale ? 1 : -1) * variation;
        display.setScale(scale);
    }

    @Override
    public boolean step() {
        differentiateCrashedVehicles();
        differentiateViolatorVehicles();
        differentiateSemaphoreIndicators();
        return super.step();
    }

    private void differentiateSemaphoreIndicators() {
        TrafficSimGeo trafficSimGeo = (TrafficSimGeo) state;
        List<Indicator> indicatorList = trafficSimGeo.indicatorsList();
        for (Indicator indicator : indicatorList) {
            Color color = null;
            switch (indicator.getState()){
                case GREEN: color = Color.green; break;
                case YELLOW: color = Color.yellow; break;
                case RED: color = Color.red; break;
            }
            semaphoreIndicatorsPortrayal.setPortrayalForObject(indicator, new GeomPortrayal(color, INDICATOR_SCALE));
        }
    }

    private void differentiateCrashedVehicles() {
        TrafficSimGeo trafficSimGeo = (TrafficSimGeo) state;
        List crashedVehicles = trafficSimGeo.crashedVehicles();
        for (Object o : crashedVehicles) {
            vehiclesPortrayal.setPortrayalForObject(o, new GeomPortrayal(Color.red, VEHICLE_SCALE));
        }
    }

    private void differentiateViolatorVehicles() {
        TrafficSimGeo trafficSimGeo = (TrafficSimGeo) state;
        List violatorVehicles = trafficSimGeo.violatorVehicles();
        for (Object o : violatorVehicles) {
            vehiclesPortrayal.setPortrayalForObject(o, new GeomPortrayal(Color.orange, VEHICLE_SCALE));
        }

    }


    private void setupPortrayals() {

        TrafficSimGeo traffic = (TrafficSimGeo) state;

        streetsPortrayal.setField(traffic.getStreets());
        StreetsLabelPortrayal portrayal = new StreetsLabelPortrayal(new GeomPortrayal(Color.BLACK), Color.BLUE);
        streetsPortrayal.setPortrayalForAll(portrayal);

        semaphoresPortrayal.setField(traffic.getSemaphores());
        semaphoresPortrayal.setPortrayalForClass(AgSemaphore.class, new GeomPortrayal(Color.MAGENTA));

        vehiclesPortrayal.setField(traffic.getVehicles());
//        vehiclesPortrayal.setPortrayalForClass(AgVehicle.class, new GeomPortrayal(Color.decode("#22A9E0"), 0.000025));
//        vehiclesPortrayal.setPortrayalForClass(AgDijkstraVehicle.class, new GeomPortrayal(Color.decode("#22A9E0"), 0.000025));
        vehiclesPortrayal.setPortrayalForClass(AgVehicle.class, new GeomPortrayal(Color.decode("#22A9E0"), VEHICLE_SCALE));
        vehiclesPortrayal.setPortrayalForClass(AgDijkstraVehicle.class, new GeomPortrayal(Color.decode("#22A9E0"), VEHICLE_SCALE));

        semaphoreIndicatorsPortrayal.setField(traffic.getSemaphoreIndicators());
        semaphoreIndicatorsPortrayal.setPortrayalForClass(Indicator.class, new GeomPortrayal(Color.red, INDICATOR_SCALE));

        cornersPortrayal.setField(traffic.getCorners());
        cornersPortrayal.setPortrayalForAll(new GeomPortrayal(Color.PINK));

        arrivalNodesPortrayal.setField(traffic.getArrivalNodes());
        arrivalNodesPortrayal.setPortrayalForAll(new GeomPortrayal(Color.black));

        display.reset();
//        display.setBackdrop(new Color(43, 43, 43));
//        display.setBackdrop(Color.decode("#73787A"));
        display.setBackdrop(Color.WHITE);
        display.repaint();
    }


    public static void main(String[] args) throws Exception {
        new TrafficSimGeoWithUI().createController();
    }

    @Override
    public Object getSimulationInspectedObject() {
        return state;
    }

    @Override
    public Inspector getInspector() {
        Inspector i = super.getInspector();
        i.setVolatile(true);
        return i;
    }

//    public static String getName() { return "Tutorial 5: Hooke's Law"; }

}
