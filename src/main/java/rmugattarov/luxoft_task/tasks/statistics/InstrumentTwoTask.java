package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.dto.Multiplier;
import rmugattarov.luxoft_task.impl.DbInstrumentMultiplierProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.BlockingQueue;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class InstrumentTwoTask implements Runnable {
    private BlockingQueue<InstrumentData> queue;

    public InstrumentTwoTask(BlockingQueue<InstrumentData> queue) {
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
            LocalDate localDate = instrumentData.getLocalDate();
            if (localDate.getYear() == 2014 && localDate.getMonth() == Month.NOVEMBER) {
                BigDecimal value = instrumentData.getValue();
                Multiplier multiplierDto = DbInstrumentMultiplierProvider.getInstrumentMultiplier(instrumentData.getInstrumentId());
                BigDecimal multiplier = multiplierDto.getMultiplier();
                if (multiplier != null) {
                    value = value.multiply(multiplier);
                }
                if (StatisticsAccumulator.instrumentTwoNov2014Sum == null) {
                    StatisticsAccumulator.instrumentTwoNov2014Sum = value;
                } else {
                    StatisticsAccumulator.instrumentTwoNov2014Sum = StatisticsAccumulator.instrumentTwoNov2014Sum.add(value);
                }
                StatisticsAccumulator.instrumentTwoNov2014ElementCount = StatisticsAccumulator.instrumentTwoNov2014ElementCount.add(BigInteger.ONE);
            }
        }
        System.out.println("\r\n>> Instrument Two task complete\r\n");
    }
}
