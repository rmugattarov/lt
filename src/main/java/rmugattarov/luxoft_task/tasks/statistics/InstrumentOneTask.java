package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.dto.Multiplier;
import rmugattarov.luxoft_task.impl.DbInstrumentMultiplierProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class InstrumentOneTask implements Runnable {
    private BlockingQueue<InstrumentData> queue;

    public InstrumentOneTask(BlockingQueue<InstrumentData> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            InstrumentData instrumentData = null;
            try {
                instrumentData = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (instrumentData == InstrumentData.PROVIDER_EXHAUSTED) {
                break;
            }
            if (instrumentData == null) {
                continue;
            }
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
        System.out.println("\r\n>> Instrument One task complete\r\n");
    }
}
