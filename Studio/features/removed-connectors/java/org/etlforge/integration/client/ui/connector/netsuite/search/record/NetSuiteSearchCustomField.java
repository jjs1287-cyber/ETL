package org.etlforge.integration.client.ui.connector.netsuite.search.record;

public class NetSuiteSearchCustomField {
   private static final String SEARCHCUSTOMFIELDPREFIX = "(CF)";
   private final String searchFieldName;
   private final String scriptID;
   private final String searchCustomFieldType;
   private final String searchColumnCustomFieldType;

   public NetSuiteSearchCustomField(String var1, String var2, String var3, String var4) {
      this.scriptID = var2;
      this.searchFieldName = var1;
      this.searchCustomFieldType = var3;
      this.searchColumnCustomFieldType = var4;
   }

   public String getSearchFieldName() {
      return this.searchFieldName;
   }

   public static String getNameWithoutPrefix(String var0) {
      return var0.startsWith("(CF)") ? var0.substring("(CF)".length()) : var0;
   }

   public static String getNameWithPrefix(String var0) {
      return "(CF)" + var0;
   }

   public String getScriptID() {
      return this.scriptID;
   }

   public String getSearchCustomFieldType() {
      return this.searchCustomFieldType;
   }

   public String getSearchColumnCustomFieldType() {
      return this.searchColumnCustomFieldType;
   }

   @Override
   public String toString() {
      return "searchCustomFieldType=" + this.getSearchCustomFieldType() + ";searchFieldName=" + this.getSearchFieldName() + ";scriptID=" + this.getScriptID();
   }
}
