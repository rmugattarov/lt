package rmugattarov.luxoft_task.tasks;

import rmugattarov.luxoft_task.api.InstrumentDataProvider;
import rmugattarov.luxoft_task.dto.InstrumentData;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class FillInstrumentDataQueueTask implements Runnable {
    private final InstrumentDataProvider instrumentDataProvider;
    private final BlockingQueue<InstrumentData> blockingQueue;

    public FillInstrumentDataQueueTask(InstrumentDataProvider instrumentDataProvider, BlockingQueue<InstrumentData> blockingQueue) {
        this.instrumentDataProvider = instrumentDataProvider;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            while (instrumentDataProvider.hasNextEntry()) {
                InstrumentData instrumentData = instrumentDataProvider.nextEntry();
                blockingQueue.put(instrumentData);
            }
            System.out.println("\r\n>> Read data task complete\r\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (instrumentDataProvider instanceof Closeable) {
                try {
                    ((Closeable) instrumentDataProvider).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
