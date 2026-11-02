package org.etlforge.util.persist;

import java.io.File;
import java.io.IOException;

public interface FilePersistor extends Persistor {
   void store(File var1) throws IOException;
}
