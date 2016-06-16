package rmugattarov.luxoft_task;

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
        System.out.printf("INSTRUMENT4 latest 10 sum : %f\r\n", GatheredStatistics.getGenericInstrumentStatistics("INSTRUMENT4"));
        System.out.println();
        tearDownDb();
        System.exit(0);
    }

    private static void tearDownDb() {
        try {
            DriverManager.getConnection("jdbc:derby:memory:MemDB;drop=true");
        } catch (SQLException e) {
            System.out.printf("Dropped in-memory DB. Error code : %d\r\n", e.getErrorCode());
        }
    }

    private static void setUpDb() throws SQLException {
        DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        Connection connection = DriverManager.getConnection("jdbc:derby:memory:MemDB;create=true");
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE INSTRUMENT_MULTIPLIER (ID BIGINT GENERATED ALWAYS AS IDENTITY, NAME VARCHAR(64), MULTIPLIER DOUBLE)");
        statement.executeUpdate("INSERT INTO INSTRUMENT_MULTIPLIER (NAME, MULTIPLIER) VALUES ('INSTRUMENT1', 2.0)");
        statement.executeUpdate("INSERT INTO INSTRUMENT_MULTIPLIER (NAME, MULTIPLIER) VALUES ('INSTRUMENT2', 0.5)");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM INSTRUMENT_MULTIPLIER");
        while (resultSet.next()) {
            System.out.printf("%d | %s | %f\r\n", resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3));
        }
        statement.close();
        connection.close();
    }
}
