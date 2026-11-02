package org.etlforge.integration.design.netsuite.config;

import java.io.Serializable;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldImplementedDefinition;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldTypeConverter;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearch;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearchDefinition;

public class NetSuiteConfig implements Serializable {
   private static final int defaultExecutionCacheSize = 5;
   private static final int defaultDesignCacheSize = 2;
   private final int executionCacheSize;
   private final int designCacheSize;
   private final NetSuiteCustomSearchDefinition customSearchDef;
   private final NetSuiteCustomFieldImplementedDefinition customFieldDef;
   private final NetSuiteCustomFieldTypeConverter typeConverter;

   public NetSuiteConfig(
      int var1, int var2, NetSuiteCustomFieldImplementedDefinition var3, NetSuiteCustomSearchDefinition var4, NetSuiteCustomFieldTypeConverter var5
   ) {
      this.executionCacheSize = var1 > 1 ? var1 : 5;
      this.designCacheSize = var2 > 1 ? var2 : 2;
      this.customSearchDef = var4;
      this.customFieldDef = var3;
      this.typeConverter = var5;
   }

   public int getExecutionCacheSize() {
      return this.executionCacheSize;
   }

   public int getDesignCacheSize() {
      return this.designCacheSize;
   }

   public NetSuiteCustomFieldTypeConverter getNSCustomFieldTypeConverter() {
      return this.typeConverter;
   }

   public NetSuiteCustomFieldImplementedDefinition getNetSuiteCustomFieldDefinition() {
      return this.customFieldDef;
   }

   public NetSuiteCustomSearchDefinition getNetSuiteCustomSearchDefinition() {
      return this.customSearchDef;
   }

   public final NetSuiteCustomSearch getNetSuiteCustomSearch(String var1) {
      return this.customSearchDef.getNetSuiteCustomSearch(var1);
   }

   public boolean isCustomSearchObject(String var1) {
      return this.customSearchDef.isCustomSearchObject(var1);
   }

   public boolean hasCustomFieldImplementation(String var1) {
      return this.customSearchDef.isGenericCustomSearch(var1) || this.customFieldDef.hasCustomFieldImplementation(var1);
   }
}
