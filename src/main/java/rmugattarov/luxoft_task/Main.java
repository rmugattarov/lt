package rmugattarov.luxoft_task;

import rmugattarov.luxoft_task.impl.FileInstrumentDataProviderImpl;
import rmugattarov.luxoft_task.tasks.statistics.CalculateInstrumentStatisticsTask;
import rmugattarov.luxoft_task.tasks.statistics.GatheredStatistics;

import java.io.FileNotFoundException;

/**
 * Created by rmugattarov on 14.06.2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        Thread calculationThread = new Thread(new CalculateInstrumentStatisticsTask(new FileInstrumentDataProviderImpl(args[0])));
        calculationThread.start();
        calculationThread.join();
        System.out.printf("InstrumentOneMean : %f\r\n", GatheredStatistics.getInstrumentOneMean());
        System.out.printf("InstrumentTwoMeanNov2014 : %f\r\n", GatheredStatistics.getInstrumentTwoMeanNov2014());
        System.out.printf("InstrumentThreeMax : %f\r\n", GatheredStatistics.getInstrumentThreeMax());
        System.out.printf("INSTRUMENT4 latest 10 sum : %f\r\n", GatheredStatistics.getGenericInstrumentStatistics("INSTRUMENT4"));
    }
}
