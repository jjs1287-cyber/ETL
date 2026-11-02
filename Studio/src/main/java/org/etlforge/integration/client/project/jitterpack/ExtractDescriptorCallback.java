package org.etlforge.integration.client.project.jitterpack;

public interface ExtractDescriptorCallback {
   void descriptorExtracted(JitterPackDescriptor var1);

   void failed(String var1, Throwable var2);
}
