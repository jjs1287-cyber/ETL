package com.etlforge.integration.client.ui.interchange.core.action.resources;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;
import org.etlforge.integration.client.ui.icons.ClientIcons;

public final class Icons extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.smallIcon("action.ic.ImportProjectData"), ClientIcons.getIcon("ImportProjectData.16")},
      {ActionDictionaryKeys.mediumIcon("action.ic.ImportProjectData"), ClientIcons.getIcon("ImportProjectData.24")},
      {ActionDictionaryKeys.largeIcon("action.ic.ImportProjectData"), ClientIcons.getIcon("ImportProjectData.32")},
      {ActionDictionaryKeys.smallIcon("action.ic.RestoreBackup"), ClientIcons.BACKUP_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.RestoreBackup"), ClientIcons.BACKUP_24},
      {ActionDictionaryKeys.largeIcon("action.ic.RestoreBackup"), ClientIcons.BACKUP_32}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
