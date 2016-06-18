package rmugattarov.luxoft_task.impl;

import rmugattarov.luxoft_task.api.FileInstrumentDataProvider;
import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.util.DtoUtils;

import java.io.*;
import java.util.Scanner;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class FileInstrumentDataProviderImpl implements FileInstrumentDataProvider {
    private final Scanner scanner;
    private boolean providerExhausted = false;

    public FileInstrumentDataProviderImpl(String filePath) throws FileNotFoundException {
        scanner = new Scanner(new BufferedInputStream(new FileInputStream(filePath)), "UTF-8");
    }

    @Override
    public boolean hasNextEntry() {
        boolean hasNextLine = scanner.hasNextLine();
        if (!hasNextLine && !providerExhausted) {
            hasNextLine = true;
            providerExhausted = true;
        }
        return hasNextLine;
    }

    @Override
    public InstrumentData nextEntry() {
        return providerExhausted ? InstrumentData.PROVIDER_EXHAUSTED : DtoUtils.convertStringToInstrumentData(scanner.nextLine());
    }

    @Override
    public void close() {
        scanner.close();
    }
}
