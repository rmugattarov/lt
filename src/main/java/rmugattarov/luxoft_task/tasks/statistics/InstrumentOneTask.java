package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class InstrumentOneTask implements Runnable {
    private final InstrumentData instrumentData;

    public InstrumentOneTask(InstrumentData instrumentData) {
        this.instrumentData = instrumentData;
    }

    @Override
    public void run() {
        BigDecimal instrumentValueAsBigDecimal = new BigDecimal(instrumentData.getValue());
        if (GatheredStatistics.instrumentOneSum == null) {
            GatheredStatistics.instrumentOneSum = instrumentValueAsBigDecimal;
        } else {
            GatheredStatistics.instrumentOneSum = GatheredStatistics.instrumentOneSum.add(instrumentValueAsBigDecimal);
        }
        GatheredStatistics.instrumentOneElementCount = GatheredStatistics.instrumentOneElementCount.add(BigInteger.ONE);
    }
}
