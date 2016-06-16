package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by rmugattarov on 16.06.2016.
 */
public class GatheredStatistics {
    protected static volatile BigDecimal instrumentOneMean;
    protected static volatile BigDecimal instrumentTwoMeanNov2014;
    protected static volatile BigDecimal instrumentThreeMax;
    protected static volatile Map<String, TreeSet<InstrumentData>> genericInstrumentStatistics = new HashMap<>();

    public static BigDecimal getInstrumentOneMean() {
        return instrumentOneMean;
    }

    public static BigDecimal getInstrumentTwoMeanNov2014() {
        return instrumentTwoMeanNov2014;
    }

    public static BigDecimal getInstrumentThreeMax() {
        return instrumentThreeMax;
    }

    public static BigDecimal getGenericInstrumentStatistics(String instrumentId) {
        BigDecimal result = null;
        TreeSet<InstrumentData> instrumentStatistics = genericInstrumentStatistics.get(instrumentId);
        if (instrumentStatistics != null) {
            for (InstrumentData instrumentData : instrumentStatistics) {
                if (result == null) {
                    result = BigDecimal.ZERO;
                }
                result = result.add(new BigDecimal(instrumentData.getValue()));
            }
        }
        return result;
    }
}
