package sim.app.trafficsimgeo.logic.agent;

import sim.app.trafficsimgeo.logic.controller.Config;
import sim.app.trafficsimgeo.logic.controller.TrafficSimGeo;
import sim.engine.SimState;
import sim.engine.Steppable;

public class AgAmbient implements Steppable {

    int temperature;
    int top;
    long lastChange;
    boolean tendencyUp;

    public AgAmbient(SimState simState) {
        top = Config.temperature;
        temperature = Config.temperature;
        tendencyUp = simState.random.nextBoolean();
        lastChange = 0;
    }

    @Override
    public void step(SimState simState) {
        if (System.currentTimeMillis() - lastChange > 5000) {
            double changeTemperature = simState.random.nextDouble();
            if (tendencyUp)
                tendencyUp(changeTemperature);
            else tendencyDown(changeTemperature);
            ((TrafficSimGeo)simState).ambientSensation(temperature);
            lastChange = System.currentTimeMillis();
            System.out.println("Temp" + temperature + "º");
        }
    }

    private void tendencyUp(double random){
        if (random < 0.4 && temperature - 1 > top){
            temperature -= 1;
        }
        else if (random > 0.7 && temperature + 1 < 42){
            temperature += 1;
        }
    }

    private void tendencyDown(double random){
        if (random < 0.4 && temperature - 1 > 20){
            temperature -= 1;
        }
        else if (random > 0.7 && temperature + 1 < top){
            temperature += 1;
        }
    }
}