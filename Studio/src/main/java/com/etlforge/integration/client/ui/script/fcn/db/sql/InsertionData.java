package com.etlforge.integration.client.ui.script.fcn.db.sql;

final class InsertionData {
   public final String text;
   public final int selectionStart;
   public final int selectionEnd;

   public InsertionData(String var1) {
      this(var1, var1.length());
   }

   public InsertionData(String var1, int var2) {
      this(var1, var2, var2);
   }

   public InsertionData(String var1, int var2, int var3) {
      this.text = var1;
      this.selectionStart = var2;
      this.selectionEnd = var3;
   }
}
