package org.etlforge.application.ui.window.action.resources;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;
import org.etlforge.application.ui.job.ProcessConsoleIcons;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;

public class Icons extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {"help.Icon.Small", CommonIcons.HELP_16},
      {ActionDictionaryKeys.smallIcon("action.Redo"), ApplicationResources.ICONS.getIcon("Redo.16")},
      {ActionDictionaryKeys.mediumIcon("action.Redo"), ApplicationResources.ICONS.getIcon("Redo.24")},
      {ActionDictionaryKeys.largeIcon("action.Redo"), ApplicationResources.ICONS.getIcon("Redo.32")},
      {ActionDictionaryKeys.smallIcon("action.Undo"), ApplicationResources.ICONS.getIcon("Undo.16")},
      {ActionDictionaryKeys.mediumIcon("action.Undo"), ApplicationResources.ICONS.getIcon("Undo.24")},
      {ActionDictionaryKeys.largeIcon("action.Undo"), ApplicationResources.ICONS.getIcon("Undo.32")},
      {ActionDictionaryKeys.smallIcon("action.Save"), ApplicationResources.ICONS.getIcon("Save.16")},
      {ActionDictionaryKeys.mediumIcon("action.Save"), ApplicationResources.ICONS.getIcon("Save.24")},
      {ActionDictionaryKeys.largeIcon("action.Save"), ApplicationResources.ICONS.getIcon("Save.32")},
      {ActionDictionaryKeys.smallIcon("action.SaveAll"), ApplicationResources.ICONS.getIcon("SaveAll.16")},
      {ActionDictionaryKeys.mediumIcon("action.SaveAll"), ApplicationResources.ICONS.getIcon("SaveAll.24")},
      {ActionDictionaryKeys.largeIcon("action.SaveAll"), ApplicationResources.ICONS.getIcon("SaveAll.32")},
      {ActionDictionaryKeys.smallIcon("action.NextEditor"), ApplicationResources.ICONS.getIcon("NextEditor.16")},
      {ActionDictionaryKeys.mediumIcon("action.NextEditor"), ApplicationResources.ICONS.getIcon("NextEditor.24")},
      {ActionDictionaryKeys.largeIcon("action.NextEditor"), ApplicationResources.ICONS.getIcon("NextEditor.32")},
      {ActionDictionaryKeys.smallIcon("action.PreviousEditor"), ApplicationResources.ICONS.getIcon("PreviousEditor.16")},
      {ActionDictionaryKeys.mediumIcon("action.PreviousEditor"), ApplicationResources.ICONS.getIcon("PreviousEditor.24")},
      {ActionDictionaryKeys.largeIcon("action.PreviousEditor"), ApplicationResources.ICONS.getIcon("PreviousEditor.32")},
      {ActionDictionaryKeys.smallIcon("action.ErrorLog"), CommonIcons.ERROR_16},
      {ActionDictionaryKeys.mediumIcon("action.ErrorLog"), CommonIcons.ERROR_24},
      {ActionDictionaryKeys.smallIcon("action.ProcessConsole"), ProcessConsoleIcons.PROCESS_OFF_16},
      {ActionDictionaryKeys.mediumIcon("action.ProcessConsole"), ProcessConsoleIcons.PROCESS_OFF_16},
      {ActionDictionaryKeys.smallIcon("action.Logs"), ApplicationResources.ICONS.getIcon("LogViewer.16")},
      {ActionDictionaryKeys.smallIcon("action.OpenAppDir"), CommonIcons.OPEN_16}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
