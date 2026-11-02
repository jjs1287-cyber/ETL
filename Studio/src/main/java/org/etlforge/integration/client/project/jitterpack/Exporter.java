package org.etlforge.integration.client.project.jitterpack;

import java.io.File;
import org.etlforge.integration.client.project.ManagedProject;

public interface Exporter {
   JitterPack pack(ManagedProject var1, String var2, File var3, ExportParameters var4) throws JitterPackException;
}
