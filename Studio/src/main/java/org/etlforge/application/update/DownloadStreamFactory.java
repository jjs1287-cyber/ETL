package org.etlforge.application.update;

import com.google.common.io.InputSupplier;
import java.io.InputStream;

public interface DownloadStreamFactory {
   InputSupplier<InputStream> newStreamSupplier(String var1);
}
