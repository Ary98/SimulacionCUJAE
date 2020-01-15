package sim.app.trafficsimgeo.logic.agent;

import sim.util.geo.MasonGeometry;

import java.util.Random;

public class Indicator {
    private Light state;
    private MasonGeometry location;

    public Indicator(MasonGeometry location) {
        this.location = location;
        this.location.setUserData(this);
        state = Light.getByRandom(new Random().nextInt(3));
    }

    //la luz amarilla es no intermitente (fija), indica que el vehiculo debe detenerse
    public Light getState() {
        return state;
    }

    void setState(Light state) {
        this.state = state;
    }

    public MasonGeometry getGeometry() {
        return location;
    }
}
