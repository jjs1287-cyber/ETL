package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.UndoRedoMode;

public final class UndoRedoModePreference {
   public static final EnumPreference<UndoRedoMode> INSTANCE = create();

   public static UndoRedoMode get() {
      return INSTANCE.get();
   }

   private static EnumPreference<UndoRedoMode> create() {
      String[] var0 = new String[]{"Operations"};
      String var1 = "Graph.UndoRedoMode";
      return ApplicationConfiguration.getPreferenceFactory().newEnumPreference(var0, var1, UndoRedoMode.ALL, UndoRedoMode.class);
   }

   private UndoRedoModePreference() {
   }
}
