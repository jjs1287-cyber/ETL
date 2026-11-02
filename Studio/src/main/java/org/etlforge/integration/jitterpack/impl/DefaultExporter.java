package org.etlforge.integration.jitterpack.impl;

import java.io.File;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.jitterpack.ExportParameters;
import org.etlforge.integration.client.project.jitterpack.Exporter;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackException;

public final class DefaultExporter implements Exporter {
   @Override
   public JitterPack pack(ManagedProject var1, String var2, File var3, ExportParameters var4) throws JitterPackException {
      JitterPackFormat var5 = JitterPackUtils.getPackFormat(var4);
      return var5.pack(var1, var2, var3);
   }
}
