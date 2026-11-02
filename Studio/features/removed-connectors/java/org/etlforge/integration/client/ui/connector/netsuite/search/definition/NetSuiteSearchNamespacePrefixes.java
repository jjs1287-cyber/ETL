package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.design.netsuite.definition.ConnectorCoreNamespaces;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersion;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersionFactory;
import org.etlforge.integration.etlforgeconnector.XmlQName;

public class NetSuiteSearchNamespacePrefixes implements Serializable {
   private final Map<String, String> namespacePrefix;
   private final ConnectorCoreNamespaces coreNamespaces;
   private final NetSuiteWsdlVersion wsdlVersion;

   public NetSuiteSearchNamespacePrefixes(Map<String, String> var1) {
      this.namespacePrefix = var1;
      this.coreNamespaces = new ConnectorCoreNamespaces();

      for (Entry var3 : var1.entrySet()) {
         if (!this.coreNamespaces.isNotSet()) {
            break;
         }

         String var4 = (String)var3.getKey();
         this.coreNamespaces.setCoreNamespace(var4);
      }

      this.wsdlVersion = NetSuiteWsdlVersionFactory.fromNamespace(this.coreNamespaces.getCoreNamespace());
   }

   public NetSuiteWsdlVersion getNetSuiteWsdlVersion() {
      return this.wsdlVersion;
   }

   public String getQName(String var1, String var2) {
      String var3 = this.getPrefix(var1);
      return XmlQName.getQName(var3, var2);
   }

   public String getPrefix(String var1) {
      String var2 = this.namespacePrefix.get(var1);
      if (var2 == null) {
         throw new ConnectorNetSuiteWsdlError("no prefix defined for namespace " + var1);
      } else {
         return var2;
      }
   }

   public String getMessageNamespace() {
      return this.coreNamespaces.getMessageNamespace();
   }

   public String getCoreNamespace() {
      return this.coreNamespaces.getCoreNamespace();
   }
}
