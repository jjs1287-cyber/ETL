package org.etlforge.integration.client.ui.interchange.locatable.actions;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;

public final class Strings extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.name("action.ic.ExportSourceSettings"), "Export Settings..."},
      {ActionDictionaryKeys.tooltip("action.ic.ExportSourceSettings"), "Export the settings from this source to other sources."},
      {ActionDictionaryKeys.name("action.ic.ExportTargetSettings"), "Export Settings..."},
      {ActionDictionaryKeys.tooltip("action.ic.ExportTargetSettings"), "Export the settings from this target to other targets."},
      {ActionDictionaryKeys.name("action.ic.SourceToTarget"), "Copy to New Target"},
      {ActionDictionaryKeys.tooltip("action.ic.SourceToTarget"), "Create a new target using the settings from this source."},
      {ActionDictionaryKeys.name("action.ic.TargetToSource"), "Copy to New Source"},
      {ActionDictionaryKeys.tooltip("action.ic.TargetToSource"), "Create a new source using the settings from this target."},
      {ActionDictionaryKeys.name("action.ic.TestConnection"), "Test Connection"},
      {ActionDictionaryKeys.tooltip("action.ic.TestConnection"), "Test the connection to the selected source or target"},
      {ActionDictionaryKeys.accelerator("action.ic.TestConnection"), "ctrl pressed F11"}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
