package sim.app.trafficsimgeo.model.dao;

import sim.app.trafficsimgeo.logic.controller.Config;
import sim.app.trafficsimgeo.model.datasource.DataSource;
import sim.app.trafficsimgeo.model.datasource.SQLiteDataSourceImp;
import sim.app.trafficsimgeo.model.entity.Statistical;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class StatisticalDAOImp implements StatisticalDAO {

    private DataSource mDataSource;

    public StatisticalDAOImp() {
        mDataSource = new SQLiteDataSourceImp(Config.pathOfDbStatistical);
    }

    private static final String TABLE_NAME = "stats_table";
    private static final String ID_COLUMN = "id";
    private static final String VEHICLE_NUMBER_INPUT_COLUMN = "vehicle_number_input";
    private static final String VEHICLE_NUMBER_OUTPUT_COLUMN = "vehicle_number_output";
    private static final String NUMBER_OF_ACCIDENTS_FOR_INFRINGEMENT_COLUMN = "number_of_accidents_for_infringement";
    private static final String NUMBER_OF_ACCIDENTS_FOR_IMPRUDENCE_COLUMN = "number_of_accidents_for_imprudence";
    private static final String NUMBER_OF_INFRACTIONS_COLUMN = "number_of_infractions";
    private static final String AVERAGE_SPEED_OF_VEHICLES_COLUMN = "average_speed_of_vehicles";
    private static final String NUMBER_OF_OFFENDERS_COLUMN = "number_of_offenders";
    private static final String AVERAGE_SYSTEM_TIME_COLUMN = "average_system_time";
    private static final String AVERAGE_TIME_ON_HOLD_COLUMN = "average_time_on_hold";
    private static final String SIGNALIZED_COLUMN = "signalized";
    private static final String TOTAL_SIMULATION_TIME_COLUMN = "total_simulation_time";
    private static final String MOMENT_COLUMN = "moment";

    @Override
    public void create(Statistical newStatistical) throws Exception {
        String query = "INSERT INTO '" + TABLE_NAME + "' ( " +
                VEHICLE_NUMBER_INPUT_COLUMN + ", " +
                VEHICLE_NUMBER_OUTPUT_COLUMN + ", " +
                NUMBER_OF_ACCIDENTS_FOR_INFRINGEMENT_COLUMN + ", " +
                NUMBER_OF_ACCIDENTS_FOR_IMPRUDENCE_COLUMN + ", " +
                NUMBER_OF_INFRACTIONS_COLUMN + ", " +
                NUMBER_OF_OFFENDERS_COLUMN + ", " +
                AVERAGE_SPEED_OF_VEHICLES_COLUMN + ", " +
                AVERAGE_SYSTEM_TIME_COLUMN + ", " +
                AVERAGE_TIME_ON_HOLD_COLUMN + ", " +
                SIGNALIZED_COLUMN + ", " +
                TOTAL_SIMULATION_TIME_COLUMN +
                " ) " +
                "VALUES ( " +
                newStatistical.getVehicleNumberInput() + ", " +
                newStatistical.getVehicleNumberOutput() + ", " +
                newStatistical.getNumberOfAccidentsForInfringement() + ", " +
                newStatistical.getNumberOfAccidentsForImprudence() + ", " +
                newStatistical.getNumberOfInfractions() + ", " +
                newStatistical.getNumberOfOffenders() + ", " +
                newStatistical.getAverageSpeedOfVehicles() + ", " +
                newStatistical.getAverageSystemTime() + ", " +
                newStatistical.getAverageTimeOnHold() + ", " +
                newStatistical.isSignalizedInt() + ", " +
                newStatistical.getTotalSimulationTime() +
                " );";
        mDataSource.connect();
        mDataSource.executeUpdate(query);
        mDataSource.disconnect();
    }

    @Override
    public void removeAll() throws Exception {
        String query = "DELETE FROM '" + TABLE_NAME + "' ; ";
        mDataSource.connect();
        mDataSource.executeUpdate(query);
        mDataSource.disconnect();
    }

    @Override
    public Statistical findById(int id) throws Exception {
        Statistical mStatistical = null;

        String query = "SELECT * " +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + ID_COLUMN + " = " + id;

        mDataSource.connect();
        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
        if (resultSet.next()) mStatistical = map(resultSet);
        resultSet.getStatement().close();
        mDataSource.disconnect();
        return mStatistical;
    }

    @Override
    public List<Statistical> findAll() throws Exception {
        List<Statistical> mList = new LinkedList<>();

        String query = "SELECT * " +
                "FROM " + TABLE_NAME + " ; ";

        mDataSource.connect();
        ResultSet resultSet = (ResultSet) mDataSource.executeQuery(query);
        while (resultSet.next())
            mList.add(map(resultSet));
        resultSet.getStatement().close();
        mDataSource.disconnect();
        return mList;
    }

    @Override
    public List<Statistical> findByRandom(int count) throws Exception {
        return null;
    }

    private Statistical map(Object result) throws Exception {
        Statistical mStatistical = new Statistical();
        if (result != null && result instanceof ResultSet) {
            ResultSet resultSet = (ResultSet) result;
            mStatistical.setId(resultSet.getInt(ID_COLUMN));
            mStatistical.setVehicleNumberInput(resultSet.getInt(VEHICLE_NUMBER_INPUT_COLUMN));
            mStatistical.setVehicleNumberOutput(resultSet.getInt(VEHICLE_NUMBER_OUTPUT_COLUMN));
            mStatistical.setNumberOfAccidentsForImprudence(resultSet.getInt(NUMBER_OF_ACCIDENTS_FOR_IMPRUDENCE_COLUMN));
            mStatistical.setNumberOfAccidentsForInfringement(resultSet.getInt(NUMBER_OF_ACCIDENTS_FOR_INFRINGEMENT_COLUMN));
            mStatistical.setNumberOfInfractions(resultSet.getInt(NUMBER_OF_INFRACTIONS_COLUMN));
            mStatistical.setNumberOfOffenders(resultSet.getInt(NUMBER_OF_OFFENDERS_COLUMN));
            mStatistical.setAverageSpeedOfVehicles(resultSet.getDouble(AVERAGE_SPEED_OF_VEHICLES_COLUMN));
            mStatistical.setAverageSystemTime(resultSet.getDouble(AVERAGE_SYSTEM_TIME_COLUMN));
            mStatistical.setAverageTimeOnHold(resultSet.getDouble(AVERAGE_TIME_ON_HOLD_COLUMN));
            mStatistical.setSignalized(resultSet.getBoolean(SIGNALIZED_COLUMN));
            mStatistical.setTotalSimulationTime(resultSet.getDouble(TOTAL_SIMULATION_TIME_COLUMN));
            // TODO: 13/06/2018 la fecha no pincha
//            mStatistical.setMoment(resultSet.getDate(MOMENT_COLUMN));

        } else
            throw new Exception("Result is null or is not instance of java.sql.ResultSet");
        return mStatistical;
    }

}
