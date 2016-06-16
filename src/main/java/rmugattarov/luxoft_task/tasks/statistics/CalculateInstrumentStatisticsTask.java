package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.api.InstrumentDataProvider;
import rmugattarov.luxoft_task.constants.InstrumentConstants;
import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.tasks.FillInstrumentDataQueueTask;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.concurrent.*;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class CalculateInstrumentStatisticsTask implements Runnable {
    private final BlockingQueue<InstrumentData> blockingQueue = new LinkedBlockingQueue<>();
    private InstrumentDataProvider instrumentDataProvider;

    public CalculateInstrumentStatisticsTask(InstrumentDataProvider instrumentDataProvider) {
        this.instrumentDataProvider = instrumentDataProvider;
    }

    @Override
    public void run() {
        ExecutorService producerExecutor = Executors.newSingleThreadExecutor();
        producerExecutor.execute(new FillInstrumentDataQueueTask(instrumentDataProvider, blockingQueue));

        ExecutorService instrumentOneExecutor = Executors.newSingleThreadExecutor();
        ExecutorService instrumentTwoExecutor = Executors.newSingleThreadExecutor();
        ExecutorService instrumentThreeExecutor = Executors.newSingleThreadExecutor();
        ExecutorService genericInstrumentExecutor = Executors.newSingleThreadExecutor();

        while (true) {
            try {
                InstrumentData instrumentData = blockingQueue.take();
                if (instrumentData == InstrumentData.PROVIDER_EXHAUSTED) {
                    break;
                }
                if (instrumentData == null) {
                    continue;
                }
                LocalDate localDate = instrumentData.getLocalDate();
                DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                    continue;
                }
                String instrumentId = instrumentData.getInstrumentId();
                switch (instrumentId) {
                    case InstrumentConstants.INSTRUMENT_ONE:
                        instrumentOneExecutor.execute(new InstrumentOneTask(instrumentData));
                        break;
                    case InstrumentConstants.INSTRUMENT_TWO:
                        instrumentTwoExecutor.execute(new InstrumentTwoTask(instrumentData));
                        break;
                    case InstrumentConstants.INSTRUMENT_THREE:
                        instrumentThreeExecutor.execute(new InstrumentThreeTask(instrumentData));
                        break;
                    default:
                        genericInstrumentExecutor.execute(new GenericInstrumentTask(instrumentData));
                        break;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        shutdown(producerExecutor, instrumentOneExecutor, instrumentTwoExecutor, instrumentThreeExecutor, genericInstrumentExecutor);
    }

    private void shutdown(ExecutorService producerExecutor, ExecutorService instrumentOneExecutor, ExecutorService instrumentTwoExecutor, ExecutorService instrumentThreeExecutor, ExecutorService genericInstrumentExecutor) {
        producerExecutor.shutdown();
        instrumentOneExecutor.shutdown();
        instrumentTwoExecutor.shutdown();
        instrumentThreeExecutor.shutdown();
        genericInstrumentExecutor.shutdown();

        try {
            producerExecutor.awaitTermination(1, TimeUnit.MINUTES);
            instrumentOneExecutor.awaitTermination(1, TimeUnit.MINUTES);
            instrumentTwoExecutor.awaitTermination(1, TimeUnit.MINUTES);
            instrumentThreeExecutor.awaitTermination(1, TimeUnit.MINUTES);
            genericInstrumentExecutor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        producerExecutor.shutdownNow();
        instrumentOneExecutor.shutdownNow();
        instrumentTwoExecutor.shutdownNow();
        instrumentThreeExecutor.shutdownNow();
        genericInstrumentExecutor.shutdownNow();
    }
}
