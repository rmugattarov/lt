package rmugattarov.luxoft_task;

import rmugattarov.luxoft_task.accumulator.StatisticsReader;
import rmugattarov.luxoft_task.constants.DbConstants;
import rmugattarov.luxoft_task.constants.InstrumentConstants;
import rmugattarov.luxoft_task.impl.FileInstrumentDataProviderImpl;
import rmugattarov.luxoft_task.tasks.statistics.CalculateInstrumentStatisticsTask;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Date;

/**
 * Created by rmugattarov on 14.06.2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException, SQLException {
        setUpDb();
        Thread calculationThread = new Thread(new CalculateInstrumentStatisticsTask(new FileInstrumentDataProviderImpl(args[0])));
        calculationThread.start();
        while (calculationThread.isAlive()) {
            Thread.sleep(5000);
            logStatistics();
        }
        System.out.println("\r\n>> Final reading:");
        logStatistics();
        System.out.println("\r\n>> Program complete. Shutting down...");
    }

    private static void logStatistics() {
        System.out.printf("\r\n>> Statistics for %1$tH:%1$tM:%1$tS\r\n", new Date());
        System.out.printf("> InstrumentOneMean : %f\r\n", StatisticsReader.getInstrumentOneMean());
        System.out.printf("> InstrumentTwoMeanNov2014 : %f\r\n", StatisticsReader.getInstrumentTwoMeanNov2014());
        System.out.printf("> InstrumentThreeMax : %f\r\n", StatisticsReader.getInstrumentThreeMax());
        System.out.printf("> %s latest 10 sum : %f\r\n", InstrumentConstants.INSTRUMENT_FOUR, StatisticsReader.getGenericInstrumentStatistics(InstrumentConstants.INSTRUMENT_FOUR));
    }

    private static void setUpDb() throws SQLException {
        Connection connection = DriverManager.getConnection(DbConstants.CONN_URL);
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE " + DbConstants.MULTIPLIER_TABLE + " (" + DbConstants.ID_COL + " BIGINT GENERATED ALWAYS AS IDENTITY, " + DbConstants.NAME_COL + " VARCHAR(64), " + DbConstants.MULTIPLIER_COL + " DOUBLE)");
        statement.executeUpdate("INSERT INTO " + DbConstants.MULTIPLIER_TABLE + " (" + DbConstants.NAME_COL + ", " + DbConstants.MULTIPLIER_COL + ") VALUES ('" + InstrumentConstants.INSTRUMENT_ONE + "', 2.0)");
        statement.executeUpdate("INSERT INTO " + DbConstants.MULTIPLIER_TABLE + " (" + DbConstants.NAME_COL + ", " + DbConstants.MULTIPLIER_COL + ") VALUES ('" + InstrumentConstants.INSTRUMENT_TWO + "', 0.5)");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DbConstants.MULTIPLIER_TABLE);
        System.out.printf("\r\n>> %s\r\n", DbConstants.MULTIPLIER_TABLE);
        while (resultSet.next()) {
            System.out.printf("> %d | %s | %f\r\n", resultSet.getInt(DbConstants.ID_COL), resultSet.getString(DbConstants.NAME_COL), resultSet.getDouble(DbConstants.MULTIPLIER_COL));
        }
        System.out.println();
        statement.close();
        connection.close();
    }
}
