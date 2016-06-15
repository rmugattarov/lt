package rmugattarov.luxoft_task.impl;

import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class FileInstrumentDataProviderImplTest {
    @Test
    public void test_file_instrument_data_provider() throws FileNotFoundException {
        String filePath = this.getClass().getClassLoader().getResource("example_input.txt").getPath();
        FileInstrumentDataProviderImpl instrumentDataProvider = new FileInstrumentDataProviderImpl(filePath);
        long lineCount = 0;
        while(instrumentDataProvider.hasNextEntry()) {
            System.out.printf("%d) %s\r\n", ++lineCount, instrumentDataProvider.nextEntry());
        }
        instrumentDataProvider.close();
    }

}