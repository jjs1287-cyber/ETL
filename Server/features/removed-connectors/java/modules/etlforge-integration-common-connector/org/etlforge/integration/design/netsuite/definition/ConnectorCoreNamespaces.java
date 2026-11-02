package org.etlforge.integration.design.netsuite.definition;

import java.io.Serializable;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;

public class ConnectorCoreNamespaces implements Serializable {
   private static final String messageNamespaceKey = "urn:messages_";
   private static final String coreNamespaceKey = "urn:core_";
   private String messageNamespace = null;
   private String coreNamespace = null;

   public void setCoreNamespace(String var1) {
      if (this.messageNamespace == null && isMessageNamespace(var1)) {
         this.messageNamespace = var1;
      }

      if (this.coreNamespace == null && isCoreNamespace(var1)) {
         this.coreNamespace = var1;
      }
   }

   public boolean isNotSet() {
      return this.messageNamespace == null || this.coreNamespace == null;
   }

   public static boolean isMessageNamespace(String var0) {
      return var0.startsWith("urn:messages_");
   }

   public static boolean isCoreNamespace(String var0) {
      return var0.startsWith("urn:core_");
   }

   public String getMessageNamespace() {
      checkNamespace(this.messageNamespace);
      return this.messageNamespace;
   }

   public String getCoreNamespace() {
      checkNamespace(this.coreNamespace);
      return this.coreNamespace;
   }

   private static void checkNamespace(String var0) {
      if (var0 == null) {
         throw new ConnectorNetSuiteDefinitionError("no definition for namespace " + var0);
      }
   }
}
