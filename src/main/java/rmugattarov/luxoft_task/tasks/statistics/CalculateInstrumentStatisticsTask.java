package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.api.InstrumentDataProvider;
import rmugattarov.luxoft_task.constants.FileSourceConstants;
import rmugattarov.luxoft_task.constants.InstrumentConstants;
import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.tasks.FillInstrumentDataQueueTask;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class CalculateInstrumentStatisticsTask implements Runnable {
    private final BlockingQueue<InstrumentData> commonQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<InstrumentData> instrumentOneQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<InstrumentData> instrumentTwoQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<InstrumentData> instrumentThreeQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<InstrumentData> genericInstrumentQueue = new LinkedBlockingQueue<>();
    private InstrumentDataProvider instrumentDataProvider;

    public CalculateInstrumentStatisticsTask(InstrumentDataProvider instrumentDataProvider) {
        this.instrumentDataProvider = instrumentDataProvider;
    }

    @Override
    public void run() {
        Thread providerThread = new Thread(new FillInstrumentDataQueueTask(instrumentDataProvider, commonQueue));
        Thread instrumentOneTask = new Thread(new InstrumentOneTask(instrumentOneQueue));
        Thread instrumentTwoTask = new Thread(new InstrumentTwoTask(instrumentTwoQueue));
        Thread instrumentThreeTask = new Thread(new InstrumentThreeTask(instrumentThreeQueue));
        Thread genericInstrumentTask = new Thread(new GenericInstrumentTask(genericInstrumentQueue));

        providerThread.start();
        instrumentOneTask.start();
        instrumentTwoTask.start();
        instrumentThreeTask.start();
        genericInstrumentTask.start();

        while (true) {
            try {
                InstrumentData instrumentData = commonQueue.take();

                if (instrumentData == null) {
                    continue;
                }
                if (instrumentData == InstrumentData.PROVIDER_EXHAUSTED) {
                    instrumentOneQueue.put(InstrumentData.PROVIDER_EXHAUSTED);
                    instrumentTwoQueue.put(InstrumentData.PROVIDER_EXHAUSTED);
                    instrumentThreeQueue.put(InstrumentData.PROVIDER_EXHAUSTED);
                    genericInstrumentQueue.put(InstrumentData.PROVIDER_EXHAUSTED);
                    break;
                } else {
                    LocalDate localDate = instrumentData.getLocalDate();
                    DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY || localDate.isAfter(FileSourceConstants.TODAY)) {
                        continue;
                    }
                    String instrumentId = instrumentData.getInstrumentId();
                    switch (instrumentId) {
                        case InstrumentConstants.INSTRUMENT_ONE:
                            instrumentOneQueue.put(instrumentData);
                            break;
                        case InstrumentConstants.INSTRUMENT_TWO:
                            instrumentTwoQueue.put(instrumentData);
                            break;
                        case InstrumentConstants.INSTRUMENT_THREE:
                            instrumentThreeQueue.put(instrumentData);
                            break;
                        default:
                            genericInstrumentQueue.put(instrumentData);
                            break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        shutdown(providerThread, instrumentOneTask, instrumentTwoTask, instrumentThreeTask, genericInstrumentTask);
    }

    private void shutdown(Thread providerThread, Thread instrumentOneTask, Thread instrumentTwoTask, Thread instrumentThreeTask, Thread genericInstrumentTask) {
        try {
            providerThread.join(60000);
            instrumentOneTask.join(60000);
            instrumentTwoTask.join(60000);
            instrumentThreeTask.join(60000);
            genericInstrumentTask.join(60000);

            providerThread.interrupt();
            instrumentOneTask.interrupt();
            instrumentTwoTask.interrupt();
            instrumentThreeTask.interrupt();
            genericInstrumentTask.interrupt();

            System.out.println("\r\n>> Producer/Consumer threads shutdown complete\r\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
