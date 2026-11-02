package org.etlforge.integration.design.netsuite.definition;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;

public class NetSuiteNamespacePreferedPrefix implements Serializable {
   private final Map<String, String> namespacePrefix = Maps.newHashMap();
   private final ConnectorCoreNamespaces coreNamespaces = new ConnectorCoreNamespaces();

   public void putPreferedPrefix(String var1, String var2) {
      if (!Strings.isNullOrEmpty(var2) && !this.namespacePrefix.containsKey(var1)) {
         this.coreNamespaces.setCoreNamespace(var1);
         this.namespacePrefix.put(var1, var2);
      }
   }

   public Map<String, String> getNamespacePrefix() {
      return this.namespacePrefix;
   }

   public String getMessageNamespace() {
      return this.coreNamespaces.getMessageNamespace();
   }

   public String getCoreNamespace() {
      return this.coreNamespaces.getCoreNamespace();
   }

   public String getMessageNamespacePrefix() {
      return this.getPrefixExists(this.getMessageNamespace());
   }

   public String getCoreNamespacePrefix() {
      return this.getPrefixExists(this.getCoreNamespace());
   }

   public String getPrefixExists(String var1) {
      String var2 = this.namespacePrefix.get(var1);
      if (Strings.isNullOrEmpty(var2)) {
         throw new ConnectorNetSuiteDefinitionError("no prefix for namespace " + var1);
      } else {
         return var2;
      }
   }
}
