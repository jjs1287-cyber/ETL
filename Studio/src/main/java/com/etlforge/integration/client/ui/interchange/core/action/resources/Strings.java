package com.etlforge.integration.client.ui.interchange.core.action.resources;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;

public final class Strings extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.name("action.ic.ImportProjectData"), "I&mport Project Data..."},
      {ActionDictionaryKeys.tooltip("action.ic.ImportProjectData"), "Import individual items from another project"},
      {ActionDictionaryKeys.name("action.ic.ProfileOperation"), "Profile Operation"},
      {ActionDictionaryKeys.tooltip("action.ic.ProfileOperation"), "Profiles an Operation on a server, by running and timing it"},
      {ActionDictionaryKeys.name("action.ic.RestoreBackup"), "&Restore From Backup..."},
      {ActionDictionaryKeys.tooltip("action.ic.RestoreBackup"), "Restore project data from a backup"},
      {ActionDictionaryKeys.name("action.ic.ProjectPlugins"), "Required &Plugins"},
      {ActionDictionaryKeys.tooltip("action.ic.ProjectPlugins"), "Display information about which plugins are currently in used in the project"}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
