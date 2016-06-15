package rmugattarov.luxoft_task.api;

import java.math.BigDecimal;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public interface InstrumentStatisticsCalculator {
    void start();
    BigDecimal getInstrumentOneMean();
    BigDecimal getInstrumentTwoMeanForNovember2014();
    BigDecimal getInstrumentThreeMax();
    BigDecimal getGenericInstrumentSumOfLatestTen(String instrumentId);
}
