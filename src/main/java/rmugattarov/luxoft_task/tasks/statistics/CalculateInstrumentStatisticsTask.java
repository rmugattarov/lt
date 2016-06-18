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
    private final BlockingQueue<InstrumentData> commonQueue = new LinkedBlockingQueue<>(10);
    private final BlockingQueue<InstrumentData> mondayQueue = new LinkedBlockingQueue<>(10);
    private final BlockingQueue<InstrumentData> tuesdayQueue = new LinkedBlockingQueue<>(10);
    private final BlockingQueue<InstrumentData> wednesdayQueue = new LinkedBlockingQueue<>(10);
    private final BlockingQueue<InstrumentData> thursdayQueue = new LinkedBlockingQueue<>(10);
    private final BlockingQueue<InstrumentData> fridayQueue = new LinkedBlockingQueue<>(10);
    private final InstrumentDataProvider instrumentDataProvider;

    public CalculateInstrumentStatisticsTask(InstrumentDataProvider instrumentDataProvider) {
        this.instrumentDataProvider = instrumentDataProvider;
    }

    @Override
    public void run() {
        Thread providerThread = new Thread(new FillInstrumentDataQueueTask(instrumentDataProvider, commonQueue));
        Thread mondayTask = new Thread(new InstrumentTask(DayOfWeek.MONDAY, mondayQueue));
        Thread tuesdayTask = new Thread(new InstrumentTask(DayOfWeek.TUESDAY, tuesdayQueue));
        Thread wednesdayTask = new Thread(new InstrumentTask(DayOfWeek.WEDNESDAY, wednesdayQueue));
        Thread thursdayTask = new Thread(new InstrumentTask(DayOfWeek.THURSDAY, thursdayQueue));
        Thread fridayTask = new Thread(new InstrumentTask(DayOfWeek.FRIDAY, fridayQueue));

        providerThread.start();
        mondayTask.start();
        tuesdayTask.start();
        wednesdayTask.start();
        thursdayTask.start();
        fridayTask.start();

        while (true) {
            try {
                InstrumentData instrumentData = commonQueue.take();
                if (instrumentData == null) {
                    continue;
                }
                if (instrumentData == InstrumentData.PROVIDER_EXHAUSTED) {
                    mondayQueue.put(InstrumentData.PROVIDER_EXHAUSTED);
                    tuesdayQueue.put(InstrumentData.PROVIDER_EXHAUSTED);
                    wednesdayQueue.put(InstrumentData.PROVIDER_EXHAUSTED);
                    thursdayQueue.put(InstrumentData.PROVIDER_EXHAUSTED);
                    fridayQueue.put(InstrumentData.PROVIDER_EXHAUSTED);
                    break;
                } else {
                    LocalDate localDate = instrumentData.getLocalDate();
                    switch (localDate.getDayOfWeek()) {
                        case MONDAY:
                            mondayQueue.put(instrumentData);
                            break;
                        case TUESDAY:
                            tuesdayQueue.put(instrumentData);
                            break;
                        case WEDNESDAY:
                            wednesdayQueue.put(instrumentData);
                            break;
                        case THURSDAY:
                            thursdayQueue.put(instrumentData);
                            break;
                        case FRIDAY:
                            fridayQueue.put(instrumentData);
                            break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        shutdown(providerThread, mondayTask, tuesdayTask, wednesdayTask, thursdayTask, fridayTask);
    }

    private void shutdown(Thread providerThread, Thread mondayTask, Thread tuesdayTask, Thread wednesdayTask, Thread thursdayTask, Thread fridayTask) {
        try {
            providerThread.join(60000);
            mondayTask.join(60000);
            tuesdayTask.join(60000);
            wednesdayTask.join(60000);
            thursdayTask.join(60000);
            fridayTask.join(60000);

            providerThread.interrupt();
            mondayTask.interrupt();
            tuesdayTask.interrupt();
            wednesdayTask.interrupt();
            thursdayTask.interrupt();
            fridayTask.interrupt();

            System.out.println("\r\n>> Producer/Consumer threads shutdown complete\r\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
