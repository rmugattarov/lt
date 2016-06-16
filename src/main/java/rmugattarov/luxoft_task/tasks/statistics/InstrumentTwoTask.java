package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class InstrumentTwoTask implements Runnable {
    private static final BigDecimal BIG_DECIMAL_TWO = new BigDecimal(2);
    private final InstrumentData instrumentData;

    public InstrumentTwoTask(InstrumentData instrumentData) {
        this.instrumentData = instrumentData;
    }

    @Override
    public void run() {
        LocalDate localDate = instrumentData.getLocalDate();
        if (localDate.getYear() == 2014 && localDate.getMonth() == Month.NOVEMBER) {
            BigDecimal instrumentValueAsBigDecimal = new BigDecimal(instrumentData.getValue());
            if (GatheredStatistics.instrumentTwoMeanNov2014 == null) {
                GatheredStatistics.instrumentTwoMeanNov2014 = instrumentValueAsBigDecimal;
            } else {
                GatheredStatistics.instrumentTwoMeanNov2014 = GatheredStatistics.instrumentTwoMeanNov2014.add(instrumentValueAsBigDecimal).divide(BIG_DECIMAL_TWO);
            }
        }
    }
}
