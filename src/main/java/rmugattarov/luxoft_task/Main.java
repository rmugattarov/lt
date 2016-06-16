package rmugattarov.luxoft_task;

import rmugattarov.luxoft_task.constants.DbConstants;
import rmugattarov.luxoft_task.constants.InstrumentConstants;
import rmugattarov.luxoft_task.impl.FileInstrumentDataProviderImpl;
import rmugattarov.luxoft_task.tasks.statistics.CalculateInstrumentStatisticsTask;
import rmugattarov.luxoft_task.tasks.statistics.GatheredStatistics;

import java.io.FileNotFoundException;
import java.sql.*;

/**
 * Created by rmugattarov on 14.06.2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException, SQLException {
        setUpDb();
        Thread calculationThread = new Thread(new CalculateInstrumentStatisticsTask(new FileInstrumentDataProviderImpl(args[0])));
        calculationThread.start();
        calculationThread.join();
        System.out.println();
        System.out.printf("InstrumentOneMean : %f\r\n", GatheredStatistics.getInstrumentOneMean());
        System.out.printf("InstrumentTwoMeanNov2014 : %f\r\n", GatheredStatistics.getInstrumentTwoMeanNov2014());
        System.out.printf("InstrumentThreeMax : %f\r\n", GatheredStatistics.getInstrumentThreeMax());
        System.out.printf("%s latest 10 sum : %f\r\n", InstrumentConstants.INSTRUMENT_FOUR, GatheredStatistics.getGenericInstrumentStatistics(InstrumentConstants.INSTRUMENT_FOUR));
        System.out.println();
        tearDownDb();
        System.exit(0);
    }

    private static void tearDownDb() {
        try {
            DriverManager.getConnection(DbConstants.CONN_URL + ";drop=true");
        } catch (SQLException e) {
            System.out.printf("Dropped in-memory DB. Error code : %d\r\n", e.getErrorCode());
        }
    }

    private static void setUpDb() throws SQLException {
        DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        Connection connection = DriverManager.getConnection(DbConstants.CONN_URL + ";create=true");
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE " + DbConstants.MULTIPLIER_TABLE + " (" + DbConstants.ID_COL + " BIGINT GENERATED ALWAYS AS IDENTITY, " + DbConstants.NAME_COL + " VARCHAR(64), " + DbConstants.MULTIPLIER_COL + " DOUBLE)");
        statement.executeUpdate("INSERT INTO " + DbConstants.MULTIPLIER_TABLE + " (" + DbConstants.NAME_COL + ", " + DbConstants.MULTIPLIER_COL + ") VALUES ('" + InstrumentConstants.INSTRUMENT_ONE + "', 2.0)");
        statement.executeUpdate("INSERT INTO " + DbConstants.MULTIPLIER_TABLE + " (" + DbConstants.NAME_COL + ", " + DbConstants.MULTIPLIER_COL + ") VALUES ('" + InstrumentConstants.INSTRUMENT_TWO + "', 0.5)");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DbConstants.MULTIPLIER_TABLE);
        System.out.println(DbConstants.MULTIPLIER_TABLE);
        while (resultSet.next()) {
            System.out.printf("%d | %s | %f\r\n", resultSet.getInt(DbConstants.ID_COL), resultSet.getString(DbConstants.NAME_COL), resultSet.getDouble(DbConstants.MULTIPLIER_COL));
        }
        statement.close();
        connection.close();
    }
}
