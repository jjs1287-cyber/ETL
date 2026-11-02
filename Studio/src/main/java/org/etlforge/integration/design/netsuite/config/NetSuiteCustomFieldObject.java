package org.etlforge.integration.design.netsuite.config;

import com.google.common.base.Strings;

public class NetSuiteCustomFieldObject implements Comparable<NetSuiteCustomFieldObject> {
   static final String getCustomizationTypeColumn = "transactionColumnCustomField";
   private final String objectName;
   private final String objectColumnName;
   private final String getCustomizationType;
   private final String appliesToObject;
   private final String appliesToObjectColumn;

   public NetSuiteCustomFieldObject(String var1, String var2, String var3) {
      this(var1, var2, var3, null, null);
   }

   public NetSuiteCustomFieldObject(String var1, String var2, String var3, String var4, String var5) {
      this.objectName = var1;
      this.getCustomizationType = var2;
      this.appliesToObject = var3;
      this.appliesToObjectColumn = var4;
      this.objectColumnName = var5;
   }

   public int compareTo(NetSuiteCustomFieldObject var1) {
      return this.getKey().compareTo(var1.getKey());
   }

   private String getKey() {
      return this.getCustomizationType + this.objectName;
   }

   public static String getGetcustomizationtypecolumn() {
      return "transactionColumnCustomField";
   }

   public String getObjectName() {
      return this.objectName;
   }

   public String getObjectColumnName() {
      return this.objectColumnName;
   }

   public String getGetCustomizationType() {
      return this.getCustomizationType;
   }

   public String getAppliesToObject() {
      return this.appliesToObject;
   }

   public String getAppliesToObjectColumn() {
      return this.appliesToObjectColumn;
   }

   public boolean hasObjectColumn() {
      return !Strings.isNullOrEmpty(this.objectColumnName);
   }
}
