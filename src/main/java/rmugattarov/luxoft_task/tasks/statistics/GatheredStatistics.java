package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Created by rmugattarov on 16.06.2016.
 */
public class GatheredStatistics {
    protected static volatile BigDecimal instrumentOneSum;
    protected static volatile BigInteger instrumentOneElementCount = BigInteger.ZERO;
    protected static volatile BigDecimal instrumentTwoNov2014Sum;
    protected static volatile BigInteger instrumentTwoNov2014ElementCount = BigInteger.ZERO;
    protected static volatile BigDecimal instrumentThreeMax;
    protected static volatile Map<String, TreeSet<InstrumentData>> genericInstrumentStatistics = new HashMap<>();

    public static BigDecimal getInstrumentOneMean() {
        BigDecimal result = null;
        if (instrumentOneSum != null && !Objects.equals(instrumentOneElementCount, BigInteger.ZERO)) {
            result = instrumentOneSum.divide(new BigDecimal(instrumentOneElementCount), BigDecimal.ROUND_HALF_UP);
        }
        return result;
    }

    public static BigDecimal getInstrumentTwoMeanNov2014() {
        BigDecimal result = null;
        if (instrumentTwoNov2014Sum != null && !Objects.equals(instrumentTwoNov2014ElementCount, BigInteger.ZERO)) {
            result = instrumentTwoNov2014Sum.divide(new BigDecimal(instrumentTwoNov2014ElementCount), BigDecimal.ROUND_HALF_UP);
        }
        return result;
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
                result = result.add(instrumentData.getValue());
            }
        }
        return result;
    }
}
