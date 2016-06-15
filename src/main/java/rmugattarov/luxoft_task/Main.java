package rmugattarov.luxoft_task;

import rmugattarov.luxoft_task.api.InstrumentStatisticsCalculator;
import rmugattarov.luxoft_task.impl.FileInstrumentDataProviderImpl;
import rmugattarov.luxoft_task.impl.InstrumentStatisticsCalculatorImpl;

import java.io.FileNotFoundException;

/**
 * Created by rmugattarov on 14.06.2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        InstrumentStatisticsCalculator calculator = new InstrumentStatisticsCalculatorImpl(new FileInstrumentDataProviderImpl(args[0]));
        calculator.start();
    }
}
