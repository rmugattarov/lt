package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.dto.Multiplier;
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
        Multiplier multiplierDto = DbInstrumentMultiplierProvider.getInstrumentMultiplier(instrumentData.getInstrumentId());
        BigDecimal multiplier = multiplierDto.getMultiplier();
        if (multiplier != null) {
            value = value.multiply(multiplier);
        }
        if (StatisticsAccumulator.instrumentOneSum == null) {
            StatisticsAccumulator.instrumentOneSum = value;
        } else {
            StatisticsAccumulator.instrumentOneSum = StatisticsAccumulator.instrumentOneSum.add(value);
        }
        StatisticsAccumulator.instrumentOneElementCount = StatisticsAccumulator.instrumentOneElementCount.add(BigInteger.ONE);
    }
}
