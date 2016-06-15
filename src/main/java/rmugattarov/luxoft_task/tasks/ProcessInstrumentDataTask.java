package rmugattarov.luxoft_task.tasks;

import rmugattarov.luxoft_task.constants.InstrumentConstants;
import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class ProcessInstrumentDataTask implements Runnable {
    private static final BigDecimal BIG_DECIMAL_TWO = new BigDecimal(2);
    private final Map<String, BigDecimal> instrumentStatistics;
    private final InstrumentData instrumentData;

    public ProcessInstrumentDataTask(Map<String, BigDecimal> instrumentStatistics, InstrumentData instrumentData) {
        this.instrumentStatistics = instrumentStatistics;
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
    }

    private void processGenericInstrument(String instrumentId) {

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
