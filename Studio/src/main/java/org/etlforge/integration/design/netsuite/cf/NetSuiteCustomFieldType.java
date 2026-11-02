package org.etlforge.integration.design.netsuite.cf;

import java.io.Serializable;

public class NetSuiteCustomFieldType implements Serializable, Comparable<NetSuiteCustomFieldType> {
   private final String fieldType;
   private final String customFieldType;
   private final String customFieldTypeSearch;
   private final String customFieldTypeSearchColumn;

   public NetSuiteCustomFieldType(String var1, String var2, String var3, String var4) {
      this.fieldType = var1;
      this.customFieldType = var2;
      this.customFieldTypeSearch = var3;
      this.customFieldTypeSearchColumn = var4;
   }

   public int compareTo(NetSuiteCustomFieldType var1) {
      return this.fieldType.compareTo(this.fieldType);
   }

   public String getFieldType() {
      return this.fieldType;
   }

   public String getCustomFieldType() {
      return this.customFieldType;
   }

   public String getCustomFieldTypeSearch() {
      return this.customFieldTypeSearch;
   }

   public String getCustomFieldTypeSearchColumn() {
      return this.customFieldTypeSearchColumn;
   }
}
