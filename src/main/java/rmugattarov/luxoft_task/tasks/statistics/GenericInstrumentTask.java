package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.dto.Multiplier;
import rmugattarov.luxoft_task.impl.DbInstrumentMultiplierProvider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class GenericInstrumentTask implements Runnable {
    private BlockingQueue<InstrumentData> queue;

    public GenericInstrumentTask(BlockingQueue<InstrumentData> queue) {
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
            final String instrumentId = instrumentData.getInstrumentId();
            BigDecimal value = instrumentData.getValue();
            Multiplier multiplierDto = DbInstrumentMultiplierProvider.getInstrumentMultiplier(instrumentData.getInstrumentId());
            BigDecimal multiplier = multiplierDto.getMultiplier();
            if (multiplier != null) {
                value = value.multiply(multiplier);
                instrumentData = new InstrumentData(instrumentId, instrumentData.getLocalDate(), value);
            }
            TreeSet<InstrumentData> treeSet = StatisticsAccumulator.genericInstrumentStatistics.get(instrumentId);
            if (treeSet == null) {
                treeSet = new TreeSet<>((Comparator<InstrumentData>) (o1, o2) -> o1.getLocalDate().compareTo(o2.getLocalDate()));
                treeSet.add(instrumentData);
                StatisticsAccumulator.genericInstrumentStatistics.put(instrumentId, treeSet);
            } else if (treeSet.size() < 10) {
                treeSet.add(instrumentData);
            } else {
                LocalDate earliestDate = treeSet.first().getLocalDate();
                if (instrumentData.getLocalDate().isAfter(earliestDate)) {
                    treeSet.pollFirst();
                    treeSet.add(instrumentData);
                }
            }
        }
        System.out.println("\r\n>> Generic Instrument task complete\r\n");
    }
}
