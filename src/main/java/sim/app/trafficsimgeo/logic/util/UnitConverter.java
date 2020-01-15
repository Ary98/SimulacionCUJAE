package sim.app.trafficsimgeo.logic.util;

public class UnitConverter {
    //    reasons
    static final double REASON_SECONDS_PER_SYSTEM_TIME = 0.1;
    static final double REASON_INDEX_PER_METER = 0.00001;

    static double convertFromKMxHtoMxS(double kmXh) {
        double mXs = kmXh * 1000 / 3600;
        return mXs;
    }

    static double convertFromMxStoIndexXSystemTime(double mXs) {
        return mXs * REASON_INDEX_PER_METER * REASON_SECONDS_PER_SYSTEM_TIME;
    }

    static double convertFromKMxHtoIndexXSystemTime(double kmXh) {
        double mXs = convertFromKMxHtoMxS(kmXh) ;
        return convertFromMxStoIndexXSystemTime(mXs) ;
    }

    static double convertFromMtoIndex(double m) {
        return m * REASON_INDEX_PER_METER;
    }

    static double convertFromIndexToM(double index) {
        return index / REASON_INDEX_PER_METER;
    }

    static double convertFromStoSystemTime(double s) {
        return s / REASON_SECONDS_PER_SYSTEM_TIME;
    }

    static double convertFromSystemTimeToS(double systemTime) {
        return systemTime * REASON_SECONDS_PER_SYSTEM_TIME;
    }
}
