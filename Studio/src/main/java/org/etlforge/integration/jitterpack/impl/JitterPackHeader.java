package org.etlforge.integration.jitterpack.impl;

import java.io.File;
import java.io.IOException;
import org.etlforge.integration.client.project.jitterpack.IllegalDataFormatException;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;

interface JitterPackHeader {
   JitterPackDescriptor extractFromPackedData(File var1) throws IllegalDataFormatException, IOException;

   void write(JitterPackDescriptor var1, File var2) throws IOException;
}
