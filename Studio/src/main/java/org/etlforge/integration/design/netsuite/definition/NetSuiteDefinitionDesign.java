package org.etlforge.integration.design.netsuite.definition;

import java.io.Serializable;
import org.etlforge.integration.design.netsuite.config.NetSuiteConfig;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteDefinitionSearch;

public class NetSuiteDefinitionDesign implements Serializable {
   private final NetSuiteDefinition nsdef;
   private final NetSuiteDefinitionSearch nsdefSearch;
   private final NetSuiteConfig config;

   public NetSuiteDefinitionDesign(NetSuiteDefinition var1, NetSuiteDefinitionSearch var2, NetSuiteConfig var3) {
      this.nsdef = var1;
      this.nsdefSearch = var2;
      this.config = var3;
   }

   public NetSuiteDefinition getNetSuiteDefinition() {
      return this.nsdef;
   }

   public NetSuiteDefinitionSearch getNetSuiteDefinitionSearch() {
      return this.nsdefSearch;
   }

   public NetSuiteConfig getNetSuiteConfig() {
      return this.config;
   }
}
