package org.etlforge.integration.client.project.jitterpack;

import java.io.File;

public interface JitterPackLocator {
   void search(File var1, boolean var2, ImportParameters var3, boolean var4, JitterPackLocatorCallback var5);

   void stop();
}
