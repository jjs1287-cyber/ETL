package com.etlforge.integration.server.engine.bulkload;

import java.util.List;

public class SalesforceResultStatusReport {
   final long recordNumber;
   final boolean success;
   final boolean created;
   final String Message;

   public SalesforceResultStatusReport(long var1, boolean var3, boolean var4, String var5) {
      this.recordNumber = var1;
      this.success = var3;
      this.created = var4;
      this.Message = var5;
   }

   static SalesforceResultStatusReport parse(long var0, List<String> var2) throws Exception {
      boolean var3 = Boolean.parseBoolean((String)var2.get(1));
      boolean var4 = Boolean.parseBoolean((String)var2.get(2));
      String var5 = var3 ? (String)var2.get(0) : (String)var2.get(3);
      return new SalesforceResultStatusReport(var0, var3, var4, var5);
   }

   static boolean IsAction(String var0, String var1) {
      return var0.equalsIgnoreCase(var1);
   }

   String getSuccessStatusString(String var1) {
      return getSuccessStatusString(var1, this.created);
   }

   static String getSuccessStatusString(String var0, boolean var1) {
      if (IsAction(var0, "UPSERT")) {
         return var1 ? "Item Created" : "Item Updated";
      } else if (IsAction(var0, "DELETE")) {
         return "Item Deleted";
      } else if (IsAction(var0, "CREATE")) {
         return "Item Created";
      } else {
         return IsAction(var0, "UPDATE") ? "Item Updated" : "Unknown Action " + var0;
      }
   }
}
