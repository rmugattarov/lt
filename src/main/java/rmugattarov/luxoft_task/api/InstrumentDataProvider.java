package rmugattarov.luxoft_task.api;

import rmugattarov.luxoft_task.dto.InstrumentData;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public interface InstrumentDataProvider {
    boolean hasNextEntry();

    InstrumentData nextEntry();
}
