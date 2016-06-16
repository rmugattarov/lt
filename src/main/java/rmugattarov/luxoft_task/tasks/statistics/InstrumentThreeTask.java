package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.dto.Multiplier;
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
        Multiplier multiplierDto = DbInstrumentMultiplierProvider.getInstrumentMultiplier(instrumentData.getInstrumentId());
        BigDecimal multiplier = multiplierDto.getMultiplier();
        if (multiplier != null) {
            value = value.multiply(multiplier);
        }
        if (StatisticsAccumulator.instrumentThreeMax == null) {
            StatisticsAccumulator.instrumentThreeMax = value;
        } else {
            StatisticsAccumulator.instrumentThreeMax = StatisticsAccumulator.instrumentThreeMax.max(value);
        }
    }
}
