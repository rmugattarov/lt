package rmugattarov.luxoft_task.impl;

import rmugattarov.luxoft_task.api.InstrumentDataProvider;
import rmugattarov.luxoft_task.api.InstrumentStatisticsCalculator;
import rmugattarov.luxoft_task.constants.InstrumentConstants;
import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.tasks.FillInstrumentDataQueueTask;
import rmugattarov.luxoft_task.tasks.ProcessInstrumentDataTask;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class InstrumentStatisticsCalculatorImpl implements InstrumentStatisticsCalculator {

    private final BlockingQueue<InstrumentData> blockingQueue = new LinkedBlockingQueue<>();
    private final InstrumentDataProvider instrumentDataProvider;
    private final Map<String, BigDecimal> instrumentStatistics = new HashMap<>();
    private final Map<String, TreeSet<InstrumentData>> genericInstrumentStatistics = new HashMap<>();

    public InstrumentStatisticsCalculatorImpl(InstrumentDataProvider instrumentDataProvider) {
        this.instrumentDataProvider = instrumentDataProvider;
    }

    @Override
    public void start() {
        beginFillingInstrumentDataQueue();
    }

    private void beginFillingInstrumentDataQueue() {
        FillInstrumentDataQueueTask task = new FillInstrumentDataQueueTask(instrumentDataProvider, blockingQueue);
        new Thread(task).start();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        while (true) {
            try {
                InstrumentData instrumentData = blockingQueue.take();
                if (instrumentData == InstrumentData.PROVIDER_EXHAUSTED) {
                    break;
                }
                executorService.execute(new ProcessInstrumentDataTask(instrumentStatistics, genericInstrumentStatistics, instrumentData));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public BigDecimal getInstrumentOneMean() {
        return instrumentStatistics.get(InstrumentConstants.INSTRUMENT_ONE);
    }

    @Override
    public BigDecimal getInstrumentTwoMeanForNovember2014() {
        return instrumentStatistics.get(InstrumentConstants.INSTRUMENT_TWO);
    }

    @Override
    public BigDecimal getInstrumentThreeMax() {
        return instrumentStatistics.get(InstrumentConstants.INSTRUMENT_THREE);
    }

    @Override
    public BigDecimal getGenericInstrumentSumOfLatestTen(String instrumentId) {
        TreeSet<InstrumentData> treeSet = genericInstrumentStatistics.get(instrumentId);
        BigDecimal result = BigDecimal.ZERO;
        for (InstrumentData instrumentData : treeSet) {
            result = result.add(new BigDecimal(instrumentData.getValue()));
        }
        return result;
    }
}
