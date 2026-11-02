package org.etlforge.integration.salesforce.config;

import com.google.common.collect.Maps;
import java.util.Map;

class DefaultSfdcMethodConfig implements SfdcMethodConfig {
   private final String methodName;
   private final Map<String, String> paths;
   private final Map<String, ConstantHeaderMapping> constantHeaderMappings;

   public DefaultSfdcMethodConfig(String var1) {
      this.methodName = var1;
      this.paths = Maps.newHashMap();
      this.constantHeaderMappings = Maps.newHashMap();
   }

   @Override
   public final String getMethodName() {
      return this.methodName;
   }

   @Override
   public final String getInputMessageName() {
      return this.methodName + "Request";
   }

   @Override
   public final String getOutputMessageName() {
      return this.methodName + "Response";
   }

   @Override
   public String getSimpleRootPathInRequest() {
      return null;
   }

   @Override
   public String getSimpleRootPathInResponse() {
      return null;
   }

   @Override
   public boolean requestHasSimpleMappingTreeRepresentation() {
      return false;
   }

   @Override
   public boolean responseHasSimpleMappingTreeRepresentation() {
      return false;
   }

   @Override
   public final String getProperty(String var1) {
      return this.paths.get(var1);
   }

   protected final void addProperty(String var1, String var2) {
      this.paths.put(var1, var2);
   }

   @Override
   public final Iterable<ConstantHeaderMapping> getConstantHeaderMappings() {
      return this.constantHeaderMappings.values();
   }

   protected final void addConstantHeaderMapping(ConstantHeaderMapping var1) {
      this.constantHeaderMappings.put(var1.getTargetPath(), var1);
   }
}
