package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class InstrumentThreeTask implements Runnable {
    private final InstrumentData instrumentData;

    public InstrumentThreeTask(InstrumentData instrumentData) {
        this.instrumentData = instrumentData;
    }

    @Override
    public void run() {
        BigDecimal instrumentValueAsBigDecimal = new BigDecimal(instrumentData.getValue());
        if (GatheredStatistics.instrumentThreeMax == null) {
            GatheredStatistics.instrumentThreeMax = instrumentValueAsBigDecimal;
        } else {
            GatheredStatistics.instrumentThreeMax = GatheredStatistics.instrumentOneMean.max(instrumentValueAsBigDecimal);
        }
    }
}
