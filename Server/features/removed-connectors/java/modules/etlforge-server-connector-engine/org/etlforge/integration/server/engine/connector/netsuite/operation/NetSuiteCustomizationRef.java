package org.etlforge.integration.server.engine.connector.netsuite.operation;

import com.google.common.base.Strings;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;

public class NetSuiteCustomizationRef {
   private String internalId = null;
   private String refType = null;
   private String name = null;

   public NetSuiteCustomizationRef() {
   }

   public NetSuiteCustomizationRef(String var1, String var2) {
      this.internalId = var1;
      this.refType = var2;
      this.name = null;
   }

   public String getInternalId() {
      return this.internalId;
   }

   public void setInternalId(String var1) {
      if (Strings.isNullOrEmpty(var1)) {
         throw new ConnectorWebServiceRuntimeException("internalId can not be empty in GetCustomizationId response");
      }

      this.internalId = var1.toLowerCase();
   }

   public String getRefType() {
      return this.refType;
   }

   public void setRefType(String var1) {
      this.refType = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   @Override
   public String toString() {
      return "internalId=" + this.internalId + ";name=" + this.name + ";type=" + this.refType + " ";
   }
}
