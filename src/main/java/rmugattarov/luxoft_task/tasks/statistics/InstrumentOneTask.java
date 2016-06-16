package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class InstrumentOneTask implements Runnable {
    private static final BigDecimal BIG_DECIMAL_TWO = new BigDecimal(2);
    private final InstrumentData instrumentData;

    public InstrumentOneTask(InstrumentData instrumentData) {
        this.instrumentData = instrumentData;
    }

    @Override
    public void run() {
        BigDecimal instrumentValueAsBigDecimal = new BigDecimal(instrumentData.getValue());
        if (GatheredStatistics.instrumentOneMean == null) {
            GatheredStatistics.instrumentOneMean = instrumentValueAsBigDecimal;
        } else {
            GatheredStatistics.instrumentOneMean = GatheredStatistics.instrumentOneMean.add(instrumentValueAsBigDecimal).divide(BIG_DECIMAL_TWO);
        }
    }
}
