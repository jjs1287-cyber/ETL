package org.etlforge.integration.server.netsuite.factory;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteDerivedTypes;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinition;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDerivedTypes;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteSearchObjectListFactory {
   private final Set<String> searchRecordTypeNamePattern = Sets.newHashSet();
   private final NetSuiteDefinition wsdlDef;

   public NetSuiteSearchObjectListFactory(NetSuiteDefinition var1) {
      this.wsdlDef = var1;
   }

   public Set<String> getSearchObjectNamePattern(Set<String> var1) {
      this.searchRecordTypeNamePattern.clear();
      NetSuiteDerivedTypes var2 = this.wsdlDef.getDerivedTypes();
      boolean var3 = var2.hasBaseType("SearchRecordBasic");
      if (var3) {
         ConnectorNetSuiteDerivedTypes var4 = new ConnectorNetSuiteDerivedTypes("SearchRecordBasic", var2);
         this.processSearchObjectNamePattern(var4.getKeySet(), var1);
      } else {
         ConnectorNetSuiteDerivedTypes var5 = new ConnectorNetSuiteDerivedTypes("SearchRecord", var2);
         this.processSearchObjectNamePattern(var5.getKeySet(), var1);
      }

      return this.searchRecordTypeNamePattern;
   }

   private void processSearchObjectNamePattern(Set<String> var1, Set<String> var2) {
      for (String var4 : var1) {
         boolean var5 = NetSuiteSearchObjectName.isSupportedSearchObject(var4);
         if (var5) {
            String var6 = NetSuiteSearchObjectName.getObjectNameFromSearchBasic(var4);
            if (var2.contains(var6)) {
               this.searchRecordTypeNamePattern.add(var6);
            }
         }
      }
   }
}
