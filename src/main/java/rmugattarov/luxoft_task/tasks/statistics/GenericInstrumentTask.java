package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.dto.Multiplier;
import rmugattarov.luxoft_task.impl.DbInstrumentMultiplierProvider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class GenericInstrumentTask implements Runnable {
    private InstrumentData instrumentData;

    public GenericInstrumentTask(InstrumentData instrumentData) {
        this.instrumentData = instrumentData;
    }

    @Override
    public void run() {
        final String instrumentId = instrumentData.getInstrumentId();
        BigDecimal value = instrumentData.getValue();
        Multiplier multiplierDto = DbInstrumentMultiplierProvider.getInstrumentMultiplier(instrumentData.getInstrumentId());
        BigDecimal multiplier = multiplierDto.getMultiplier();
        if (multiplier != null) {
            value = value.multiply(multiplier);
            instrumentData = new InstrumentData(instrumentId, instrumentData.getLocalDate(), value);
        }
        TreeSet<InstrumentData> treeSet = GatheredStatistics.genericInstrumentStatistics.get(instrumentId);
        if (treeSet == null) {
            treeSet = new TreeSet<>((Comparator<InstrumentData>) (o1, o2) -> o1.getLocalDate().compareTo(o2.getLocalDate()));
            treeSet.add(instrumentData);
            GatheredStatistics.genericInstrumentStatistics.put(instrumentId, treeSet);
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
}
