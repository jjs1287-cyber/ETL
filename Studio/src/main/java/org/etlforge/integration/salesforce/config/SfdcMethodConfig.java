package org.etlforge.integration.salesforce.config;

public interface SfdcMethodConfig {
   String getMethodName();

   String getInputMessageName();

   String getOutputMessageName();

   String getSimpleRootPathInRequest();

   String getSimpleRootPathInResponse();

   boolean requestHasSimpleMappingTreeRepresentation();

   boolean responseHasSimpleMappingTreeRepresentation();

   String getProperty(String var1);

   Iterable<ConstantHeaderMapping> getConstantHeaderMappings();
}
