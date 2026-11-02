package org.etlforge.integration.design.netsuite.search.wsdl;

import com.google.common.base.Strings;
import java.io.Serializable;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;

public class NetSuiteSearchField implements Serializable {
   private final String fieldName;
   private final String fieldTypeLocalName;
   private final String fieldTypeNamespace;

   public NetSuiteSearchField(String var1, String var2, String var3) {
      this.checkNotNullorEmpty("fieldName", var1);
      this.checkNotNullorEmpty("fieldTypeNamespace", var2);
      this.checkNotNullorEmpty("fieldTypeLocalName", var3);
      this.fieldName = var1;
      this.fieldTypeNamespace = var2;
      this.fieldTypeLocalName = var3;
   }

   void checkNotNullorEmpty(String var1, String var2) {
      if (Strings.isNullOrEmpty(var2)) {
         throw new ConnectorNetSuiteDefinitionError(var1 + " cannot be empty!");
      }
   }

   public String getFieldTypeLocalName() {
      return this.fieldTypeLocalName;
   }

   public String getFieldTypeNamespace() {
      return this.fieldTypeNamespace;
   }

   public String getFieldName() {
      return this.fieldName;
   }
}
