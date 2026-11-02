package com.etlforge.integration.client.ui.sfdc.bulkload.actions;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;
import org.etlforge.integration.activity.ui.resources.ActivityIcons;

public final class Icons extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.smallIcon("action.ic.BulkLoadHistory"), ActivityIcons.HISTORY_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.BulkLoadHistory"), ActivityIcons.HISTORY_24},
      {ActionDictionaryKeys.largeIcon("action.ic.BulkLoadHistory"), ActivityIcons.HISTORY_32}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
