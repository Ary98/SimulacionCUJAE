package sim.app.trafficsimgeo.logic.util;

public class TimeManager {
    static void waiting(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
//                e.printStackTrace();
        }
    }
}
