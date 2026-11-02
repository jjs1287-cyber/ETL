package com.etlforge.integration.client.ui.sfdc.bulkload.actions;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;

public final class Strings extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.name("action.ic.BulkLoadHistory"), "Result History"},
      {ActionDictionaryKeys.tooltip("action.ic.BulkLoadHistory"), "Show the history of this bulk load operation"}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
