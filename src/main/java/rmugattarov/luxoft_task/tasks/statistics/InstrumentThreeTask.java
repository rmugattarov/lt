package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.impl.DbInstrumentMultiplierProvider;

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
        BigDecimal value = instrumentData.getValue();
        Double multiplier = DbInstrumentMultiplierProvider.getInstrumentMultiplier(instrumentData.getInstrumentId());
        if (multiplier != null) {
            value = value.multiply(new BigDecimal(multiplier));
        }
        if (GatheredStatistics.instrumentThreeMax == null) {
            GatheredStatistics.instrumentThreeMax = value;
        } else {
            GatheredStatistics.instrumentThreeMax = GatheredStatistics.instrumentThreeMax.max(value);
        }
    }
}
