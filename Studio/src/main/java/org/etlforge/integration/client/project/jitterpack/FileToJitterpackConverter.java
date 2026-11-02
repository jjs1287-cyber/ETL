package org.etlforge.integration.client.project.jitterpack;

import java.io.File;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.util.version.UnsupportedVersionException;

public final class FileToJitterpackConverter {
   public JitterPack convert(File var1, Importer var2) throws JitterPackException, UnsupportedVersionException {
      DescriptorExtractor var3 = new DescriptorExtractor(var1, ImportParameters.DEFAULT, var2, null);
      JitterPackDescriptor var4 = var3.extract();
      ApplicationLog.getMainLogger().info("Extracted JitterPackDescriptor: " + var4);
      return new JitterPack(var1, var4);
   }
}
