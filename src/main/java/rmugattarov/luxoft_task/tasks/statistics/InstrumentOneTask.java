package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.impl.DbInstrumentMultiplierProvider;

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
        BigDecimal value = instrumentData.getValue();
        Double multiplier = DbInstrumentMultiplierProvider.getInstrumentMultiplier(instrumentData.getInstrumentId());
        if (multiplier != null) {
            value = value.multiply(new BigDecimal(multiplier));
        }
        if (GatheredStatistics.instrumentOneSum == null) {
            GatheredStatistics.instrumentOneSum = value;
        } else {
            GatheredStatistics.instrumentOneSum = GatheredStatistics.instrumentOneSum.add(value);
        }
        GatheredStatistics.instrumentOneElementCount = GatheredStatistics.instrumentOneElementCount.add(BigInteger.ONE);
    }
}
