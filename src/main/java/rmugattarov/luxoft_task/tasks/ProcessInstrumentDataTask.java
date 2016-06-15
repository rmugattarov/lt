package rmugattarov.luxoft_task.tasks;

import rmugattarov.luxoft_task.constants.InstrumentConstants;
import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class ProcessInstrumentDataTask implements Runnable {
    private static final BigDecimal BIG_DECIMAL_TWO = new BigDecimal(2);
    private final Map<String, BigDecimal> instrumentStatistics;
    private final Map<String, TreeSet<InstrumentData>> genericInstrumentStatistics;
    private final InstrumentData instrumentData;

    public ProcessInstrumentDataTask(Map<String, BigDecimal> instrumentStatistics, Map<String, TreeSet<InstrumentData>> genericInstrumentStatistics, InstrumentData instrumentData) {
        this.instrumentStatistics = instrumentStatistics;
        this.genericInstrumentStatistics = genericInstrumentStatistics;
        this.instrumentData = instrumentData;
    }
    @Override
    public void run() {
        System.out.println(instrumentData);
        if (instrumentData == null) {
            return;
        }
        LocalDate localDate = instrumentData.getLocalDate();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return;
        }
        String instrumentId = instrumentData.getInstrumentId();
        switch (instrumentId) {
            case InstrumentConstants.INSTRUMENT_ONE:
                processInstrumentOne();
                break;
            case InstrumentConstants.INSTRUMENT_TWO:
                processInstrumentTwo();
                break;
            case InstrumentConstants.INSTRUMENT_THREE:
                processInstrumentThree();
                break;
            default:
                processGenericInstrument(instrumentId);
                break;
        }
        System.out.println("INSTRUMENT_ONE : " + instrumentStatistics.get(InstrumentConstants.INSTRUMENT_ONE));
        System.out.println("INSTRUMENT_TWO : " + instrumentStatistics.get(InstrumentConstants.INSTRUMENT_TWO));
        System.out.println("INSTRUMENT_THREE : " + instrumentStatistics.get(InstrumentConstants.INSTRUMENT_THREE));
        System.out.println("INSTRUMENT4 : " + instrumentStatistics.get("INSTRUMENT4"));
    }

    private void processGenericInstrument(String instrumentId) {
        TreeSet<InstrumentData> treeSet = genericInstrumentStatistics.get(instrumentId);
        if (treeSet == null) {
            treeSet = new TreeSet<>((Comparator<InstrumentData>) (o1, o2) -> o1.getLocalDate().compareTo(o2.getLocalDate()));
            treeSet.add(instrumentData);
            genericInstrumentStatistics.put(instrumentId, treeSet);
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

    private void processInstrumentThree() {
        BigDecimal value = instrumentStatistics.get(InstrumentConstants.INSTRUMENT_THREE);
        if (value == null) {
            instrumentStatistics.put(InstrumentConstants.INSTRUMENT_THREE, new BigDecimal(instrumentData.getValue()));
        } else {
            value = value.max(new BigDecimal(instrumentData.getValue()));
            instrumentStatistics.put(InstrumentConstants.INSTRUMENT_THREE, value);
        }
    }

    private void processInstrumentTwo() {
        LocalDate localDate = instrumentData.getLocalDate();
        if (localDate.getYear() == 2014 && localDate.getMonth() == Month.NOVEMBER) {
            BigDecimal value = instrumentStatistics.get(InstrumentConstants.INSTRUMENT_TWO);
            if (value == null) {
                instrumentStatistics.put(InstrumentConstants.INSTRUMENT_TWO, new BigDecimal(instrumentData.getValue()));
            } else {
                value = value.add(new BigDecimal(instrumentData.getValue())).divide(BIG_DECIMAL_TWO);
                instrumentStatistics.put(InstrumentConstants.INSTRUMENT_TWO, value);
            }
        }
    }

    private void processInstrumentOne() {
        BigDecimal value = instrumentStatistics.get(InstrumentConstants.INSTRUMENT_ONE);
        if (value == null) {
            instrumentStatistics.put(InstrumentConstants.INSTRUMENT_ONE, new BigDecimal(instrumentData.getValue()));
        } else {
            value = value.add(new BigDecimal(instrumentData.getValue())).divide(BIG_DECIMAL_TWO);
            instrumentStatistics.put(InstrumentConstants.INSTRUMENT_ONE, value);
        }
    }
}
