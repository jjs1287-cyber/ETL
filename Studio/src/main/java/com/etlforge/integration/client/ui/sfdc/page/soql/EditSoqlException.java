package com.etlforge.integration.client.ui.sfdc.page.soql;

public final class EditSoqlException extends Exception {
   private final String title;

   public EditSoqlException(String var1, String var2) {
      super(var2);
      this.title = var1;
   }

   public String getTitle() {
      return this.title;
   }
}
