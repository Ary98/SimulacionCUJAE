package sim.app.trafficsimgeo.logic.agent;

public enum Light {

    GREEN, YELLOW, RED;

    public static Light getByRandom(int random){
        Light a = null;
        switch (random){
            case 0: a = GREEN; break;
            case 1: a = YELLOW; break;
            case 2: a = RED; break;
        }
        return a;
    }

}
