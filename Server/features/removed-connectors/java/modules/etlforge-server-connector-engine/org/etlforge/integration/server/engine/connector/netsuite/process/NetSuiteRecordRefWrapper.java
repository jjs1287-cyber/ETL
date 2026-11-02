package org.etlforge.integration.server.engine.connector.netsuite.process;

import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteDerivedTypes;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class NetSuiteRecordRefWrapper {
   private final ConnectorNetSuiteDerivedTypes recordRefDeriveType;

   NetSuiteRecordRefWrapper() {
      this(null);
   }

   public NetSuiteRecordRefWrapper(ConnectorNetSuiteDerivedTypes var1) {
      this.recordRefDeriveType = var1;
   }

   public boolean noRecordRefDerivedType() {
      return this.recordRefDeriveType == null;
   }

   public boolean isCustomizationRef(String var1, String var2) {
      if (this.noRecordRefDerivedType()) {
         return false;
      }

      if (!this.recordRefDeriveType.containsKey(var2)) {
         return false;
      }

      XsdGlobalElementName var3 = this.recordRefDeriveType.getDerivedObjectExists(var2);
      XsdGlobalElementName var4 = XsdGlobalElementName.newType(var1, var2);
      return var3.equals(var4);
   }
}
