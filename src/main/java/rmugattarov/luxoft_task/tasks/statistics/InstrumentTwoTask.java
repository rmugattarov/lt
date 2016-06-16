package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class InstrumentTwoTask implements Runnable {
    private final InstrumentData instrumentData;

    public InstrumentTwoTask(InstrumentData instrumentData) {
        this.instrumentData = instrumentData;
    }

    @Override
    public void run() {
        LocalDate localDate = instrumentData.getLocalDate();
        if (localDate.getYear() == 2014 && localDate.getMonth() == Month.NOVEMBER) {
            BigDecimal instrumentValueAsBigDecimal = new BigDecimal(instrumentData.getValue());
            if (GatheredStatistics.instrumentTwoNov2014Sum == null) {
                GatheredStatistics.instrumentTwoNov2014Sum = instrumentValueAsBigDecimal;
            } else {
                GatheredStatistics.instrumentTwoNov2014Sum = GatheredStatistics.instrumentTwoNov2014Sum.add(instrumentValueAsBigDecimal);
            }
            GatheredStatistics.instrumentTwoNov2014ElementCount = GatheredStatistics.instrumentTwoNov2014ElementCount.add(BigInteger.ONE);
        }
    }
}
