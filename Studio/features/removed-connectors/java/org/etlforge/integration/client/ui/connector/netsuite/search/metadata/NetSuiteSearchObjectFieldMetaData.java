package org.etlforge.integration.client.ui.connector.netsuite.search.metadata;

public class NetSuiteSearchObjectFieldMetaData {
   final String fieldName;
   final String fieldType;

   NetSuiteSearchObjectFieldMetaData(String var1, String var2) {
      this.fieldName = var1;
      this.fieldType = var2;
   }

   public String getFieldType() {
      return this.fieldType;
   }

   public String getFieldName() {
      return this.fieldName;
   }

   @Override
   public String toString() {
      return "fname=" + this.fieldName + " ftype=" + this.fieldType;
   }
}
