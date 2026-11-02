package org.etlforge.integration.client.ui.debug.action.resources;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;
import org.etlforge.integration.client.ui.debug.DebugIcons;

public final class DebugActionResources extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.name("action.debug.Stop"), "Stop Current Test"},
      {ActionDictionaryKeys.tooltip("action.debug.Stop"), "Stop the current testing session"},
      {ActionDictionaryKeys.smallIcon("action.debug.Stop"), DebugIcons.STOP_16},
      {ActionDictionaryKeys.mediumIcon("action.debug.Stop"), DebugIcons.STOP_24},
      {ActionDictionaryKeys.accelerator("action.debug.Stop"), "ctrl pressed CANCEL"},
      {ActionDictionaryKeys.name("action.debug.Resume"), "Resume Current Test"},
      {ActionDictionaryKeys.tooltip("action.debug.Resume"), "Resume the current testing session"},
      {ActionDictionaryKeys.smallIcon("action.debug.Resume"), DebugIcons.CONTINUE_16},
      {ActionDictionaryKeys.largeIcon("action.debug.Resume"), DebugIcons.CONTINUE_32},
      {ActionDictionaryKeys.accelerator("action.debug.Resume"), "pressed F8"},
      {ActionDictionaryKeys.name("action.debug.Pause"), "Pause Current Test"},
      {ActionDictionaryKeys.tooltip("action.debug.Pause"), "Pause the current testing session"},
      {ActionDictionaryKeys.smallIcon("action.debug.Pause"), DebugIcons.PAUSE_16},
      {ActionDictionaryKeys.mediumIcon("action.debug.Pause"), DebugIcons.PAUSE_24},
      {ActionDictionaryKeys.name("action.debug.Step"), "Step Test"},
      {ActionDictionaryKeys.tooltip("action.debug.Step"), "Run the next statement in the current test"},
      {ActionDictionaryKeys.smallIcon("action.debug.Step"), DebugIcons.STEP_16},
      {ActionDictionaryKeys.mediumIcon("action.debug.Step"), DebugIcons.STEP_24},
      {ActionDictionaryKeys.largeIcon("action.debug.Step"), DebugIcons.STEP_32},
      {ActionDictionaryKeys.accelerator("action.debug.Step"), "pressed F6"}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
